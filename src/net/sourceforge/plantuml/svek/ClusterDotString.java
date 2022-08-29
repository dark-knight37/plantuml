/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 *
 * If you like this project or if you find it useful, you can support us at:
 *
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 *
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 * Contribution :  Hisashi Miyashita
 *
 *
 */
package net.sourceforge.plantuml.svek;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.AlignmentParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.cucadiagram.EntityPosition;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizVersion;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.USymbols;

public class ClusterDotString {

	private final Cluster cluster;
	private final ISkinParam skinParam;

	public ClusterDotString(Cluster cluster, ISkinParam skinParam) {
		this.cluster = cluster;
		this.skinParam = skinParam;
	}

	void printInternal(StringBuilder sb, Collection<SvekLine> lines, StringBounder stringBounder, DotMode dotMode,
			GraphvizVersion graphvizVersion, UmlDiagramType type) {
		final boolean thereALinkFromOrToGroup2 = isThereALinkFromOrToGroup(lines);
		boolean thereALinkFromOrToGroup1 = thereALinkFromOrToGroup2;
		final boolean useProtectionWhenThereALinkFromOrToGroup = graphvizVersion
				.useProtectionWhenThereALinkFromOrToGroup();
		if (useProtectionWhenThereALinkFromOrToGroup == false)
			thereALinkFromOrToGroup1 = false;

		if (thereALinkFromOrToGroup1)
			subgraphClusterNoLabel(sb, "a");

		final Set<EntityPosition> entityPositionsExceptNormal = entityPositionsExceptNormal();
		if (entityPositionsExceptNormal.size() > 0)
			for (SvekLine line : lines)
				if (line.isLinkFromOrTo(cluster.getGroup()))
					line.setProjectionCluster(cluster);

		boolean protection0 = protection0(type);
		boolean protection1 = protection1(type);
		if (entityPositionsExceptNormal.size() > 0 || useProtectionWhenThereALinkFromOrToGroup == false) {
			protection0 = false;
			protection1 = false;
		}

		if (protection0)
			subgraphClusterNoLabel(sb, "p0");

		sb.append("subgraph " + cluster.getClusterId() + " {");
		sb.append("style=solid;");
		sb.append("color=\"" + StringUtils.sharp000000(cluster.getColor()) + "\";");

		final String label;
		if (cluster.isLabel()) {
			final StringBuilder sblabel = new StringBuilder("<");
			SvekLine.appendTable(sblabel, cluster.getTitleAndAttributeWidth(), cluster.getTitleAndAttributeHeight() - 5,
					cluster.getTitleColor());
			sblabel.append(">");
			label = sblabel.toString();
			final HorizontalAlignment align = skinParam.getHorizontalAlignment(AlignmentParam.packageTitleAlignment,
					null, false, null);
			sb.append("labeljust=\"" + align.getGraphVizValue() + "\";");
		} else {
			label = "\"\"";
		}

		if (entityPositionsExceptNormal.size() > 0) {
			printClusterEntryExit(sb, stringBounder);
			if (hasPort())
				subgraphClusterNoLabel(sb, Cluster.ID_EE);
			else
				subgraphClusterWithLabel(sb, Cluster.ID_EE, label);

		} else {
			sb.append("label=" + label + ";");
			SvekUtils.println(sb);
		}

		if (thereALinkFromOrToGroup2)
			sb.append(Cluster.getSpecialPointId(cluster.getGroup()) + " [shape=point,width=.01,label=\"\"];");

		if (thereALinkFromOrToGroup1)
			subgraphClusterNoLabel(sb, "i");

		if (protection1)
			subgraphClusterNoLabel(sb, "p1");

		if (skinParam.useSwimlanes(type)) {
			sb.append("{rank = source; ");
			sb.append(getSourceInPoint(type));
			sb.append(" [shape=point,width=.01,label=\"\"];");
			sb.append(cluster.getMinPoint(type) + "->" + getSourceInPoint(type) + "  [weight=999];");
			sb.append("}");
			SvekUtils.println(sb);
			sb.append("{rank = sink; ");
			sb.append(getSinkInPoint(type));
			sb.append(" [shape=point,width=.01,label=\"\"];");
			sb.append("}");
			sb.append(getSinkInPoint(type) + "->" + cluster.getMaxPoint(type) + "  [weight=999];");
			SvekUtils.println(sb);
		}
		SvekUtils.println(sb);
		cluster.printCluster1(sb, lines, stringBounder);

		final SvekNode added = cluster.printCluster2(sb, lines, stringBounder, dotMode, graphvizVersion, type);
		if (entityPositionsExceptNormal.size() > 0)
			if (hasPort()) {
				sb.append(empty() + " [shape=rect,width=.01,height=.01,label=");
				sb.append(label);
				sb.append("];");
			} else if (added == null) {
				sb.append(empty() + " [shape=point,width=.01,label=\"\"];");
			}
		SvekUtils.println(sb);

		sb.append("}");
		if (protection1)
			sb.append("}");

		if (thereALinkFromOrToGroup1) {
			sb.append("}");
			sb.append("}");
		}
		if (entityPositionsExceptNormal.size() > 0)
			sb.append("}");

		if (protection0)
			sb.append("}");

		SvekUtils.println(sb);
	}

	private String getSourceInPoint(UmlDiagramType type) {
		if (skinParam.useSwimlanes(type))
			return "sourceIn" + cluster.getColor();

		return null;
	}

	private String getSinkInPoint(UmlDiagramType type) {
		if (skinParam.useSwimlanes(type))
			return "sinkIn" + cluster.getColor();

		return null;
	}

	private String empty() {
		// return "empty" + color;
		// We use the same node with one for thereALinkFromOrToGroup2 as an empty
		// because we cannot put a new node in the nested inside of the cluster
		// if thereALinkFromOrToGroup2 is enabled.
		return Cluster.getSpecialPointId(cluster.getGroup());
	}

	private boolean hasPort() {
		for (EntityPosition pos : entityPositionsExceptNormal())
			if (pos.isPort())
				return true;

		return false;
	}

	private Set<EntityPosition> entityPositionsExceptNormal() {
		final Set<EntityPosition> result = EnumSet.<EntityPosition>noneOf(EntityPosition.class);
		for (SvekNode sh : cluster.getNodes())
			if (sh.getEntityPosition() != EntityPosition.NORMAL)
				result.add(sh.getEntityPosition());

		return Collections.unmodifiableSet(result);
	}

	private void subgraphClusterNoLabel(StringBuilder sb, String id) {
		subgraphClusterWithLabel(sb, id, "\"\"");
	}

	private void subgraphClusterWithLabel(StringBuilder sb, String id, String label) {
		sb.append("subgraph " + cluster.getClusterId() + id + " {");
		sb.append("label=" + label + ";");
	}

	private void printClusterEntryExit(StringBuilder sb, StringBounder stringBounder) {
		printRanks(Cluster.RANK_SOURCE, withPositionProtected(stringBounder, EntityPosition.getInputs()), sb,
				stringBounder);
		printRanks(Cluster.RANK_SAME, withPositionProtected(stringBounder, EntityPosition.getSame()), sb,
				stringBounder);
		printRanks(Cluster.RANK_SINK, withPositionProtected(stringBounder, EntityPosition.getOutputs()), sb,
				stringBounder);
	}

	private void printRanks(String rank, List<? extends IShapePseudo> entries, StringBuilder sb,
			StringBounder stringBounder) {
		if (entries.size() > 0) {
			sb.append("{rank=" + rank + ";");
			for (IShapePseudo sh1 : entries)
				sb.append(sh1.getUid() + ";");

			sb.append("}");
			SvekUtils.println(sb);
			for (IShapePseudo sh2 : entries)
				sh2.appendShape(sb, stringBounder);

			SvekUtils.println(sb);
			if (hasPort()) {
				boolean arrow = false;
				String node = null;
				for (IShapePseudo sh : entries) {
					if (arrow)
						sb.append("->");

					arrow = true;
					node = sh.getUid();
					sb.append(node);
				}
				if (arrow)
					sb.append(" [arrowhead=none]");

				sb.append(';');
				SvekUtils.println(sb);
				sb.append(node + "->" + empty() + ";");
				SvekUtils.println(sb);
			}
		}
	}

	private List<? extends IShapePseudo> withPositionProtected(StringBounder stringBounder,
			Set<EntityPosition> targets) {
		final List<SvekNode> result = withPosition(targets);
		final double maxWith = getMaxWidthFromLabelForEntryExit(result, stringBounder);
		final double naturalSpace = 70;
		if (maxWith > naturalSpace)
			return addProtection(result, maxWith - naturalSpace);

		return result;
	}

	private List<IShapePseudo> addProtection(List<? extends IShapePseudo> entries, double width) {
		final List<IShapePseudo> result = new ArrayList<>();
		result.add(entries.get(0));
		for (int i = 1; i < entries.size(); i++) {
			// Pseudo space for the label
			result.add(new ShapePseudoImpl("psd" + cluster.diagram.getUniqueSequence(), width, 5));
			result.add(entries.get(i));
		}
		return result;
	}

	private List<SvekNode> withPosition(Set<EntityPosition> positions) {
		final List<SvekNode> result = new ArrayList<>();
		for (final Iterator<SvekNode> it = cluster.getNodes().iterator(); it.hasNext();) {
			final SvekNode sh = it.next();
			if (positions.contains(sh.getEntityPosition()))
				result.add(sh);

		}
		return result;
	}

	private double getMaxWidthFromLabelForEntryExit(List<? extends IShapePseudo> entries, StringBounder stringBounder) {
		double result = -Double.MAX_VALUE;
		for (IShapePseudo node : entries) {
			final double w = getMaxWidthFromLabelForEntryExit(node, stringBounder);
			if (w > result)
				result = w;

		}
		return result;
	}

	private double getMaxWidthFromLabelForEntryExit(IShapePseudo node, StringBounder stringBounder) {
		return node.getMaxWidthFromLabelForEntryExit(stringBounder);
	}

	private boolean protection0(UmlDiagramType type) {
		if (skinParam.useSwimlanes(type))
			return false;

		return true;
	}

	private boolean protection1(UmlDiagramType type) {
		if (cluster.getGroup().getUSymbol() == USymbols.NODE)
			return true;

		if (skinParam.useSwimlanes(type))
			return false;

		return true;
	}

	private boolean isThereALinkFromOrToGroup(Collection<SvekLine> lines) {
		for (SvekLine line : lines)
			if (line.isLinkFromOrTo(cluster.getGroup()))
				return true;

		return false;
	}

}
