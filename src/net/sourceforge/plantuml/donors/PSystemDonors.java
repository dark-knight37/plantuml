/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 * 
 *
 */
package net.sourceforge.plantuml.donors;

import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.code.CompressionBrotli;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderImpl;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.version.PSystemVersion;

public class PSystemDonors extends AbstractPSystem {
	public static final String DONORS = "6mK70AmER5DRWVz05mqtRsdC3wE5OakdUGahTcCbkRKA_62GTnixOmk2vuhY2Yf0NeWrj-s_RbXK54A7"
			+ "AlohojhVCdtjt3gOud9H7GSveQY5J2TYN0IjrbiiTJefoOCyfvl_omKs9aZlhB4fyl0ue1ip6cFXn6XC"
			+ "-pyg6_HVT0LQmDEeR5bKvfRu4AdqPTSkbKjo1YU8B2kSajPYAeRT8iNTDePvtR93aA_5wuWySVFh6_MY"
			+ "kzbSpLA99n5nZf93r-stzNsdjn-GB838njZek661A2HFUi7OSd2XSWW0oAx9C3QiM7bYG0iVT88_lYWB"
			+ "XRTKOqq7Yihf1H6u_I4AIFgZl1Oncru656ePBUgMDDNnyIMbD9liANzj6exHDqwMc06e2UG_w8dktbPT"
			+ "8ZtZPK0azGt9jsm2ZIJt6jzXeazK7uYJKG3MGpDbF3-ghQMiMbt7-CsZ8TmjbMks6__6AoV47FyUahAu"
			+ "j82y0HqQ2x3a7-XOB7I7TBMo2_Vgd5gyGmG4fqo3XTwcs6l-7xj8CLJ8RcHX3lrql1aDs59NITuWVcg5"
			+ "-AnpWgGiBJuFVPusFfDO4yAuzoNiczKi5Vz_xcLcK8AWZM6eg8i9q9mvjAfDYYW2ju3-ZN1JbxpehhTh"
			+ "EzvU3yLhX2xelPDCdBjDZ_PFnGsaQocarK7fT9a9O6coOZQLonO9Ojg1JgFACwuimhxTPLc-R2nvBEma"
			+ "pHgDeMZJtPKdARU7Udet3AdtPlzkxIMj2LoWe31vYHgho6P7z-wuQ5xFOB2xqWCFfCyuvxwNNqhf5e0Y"
			+ "osDDbuNY6U47VnwwzCVzMTJ6ukSR_v1NqUDNyfZIMoZlbdX_KvGLFQUaNO38ShqAaSYu2QwF9djgiKpb"
			+ "i8BiMDcY2u-nOR1V2jp20sxhaK5hDgLLlQ-kGn7y45_qeFBQH69sj3iupZ0U1KR7l2DvR8lNaCnUGF4S"
			+ "9dXA9w0nKjAHlXR9o7ijqMsTVloqneNQDhubtEDGUCT4inuRU4u15EoX6m00";

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final UDrawable result = getGraphicStrings();
		final ImageBuilder imageBuilder = new ImageBuilder(new ColorMapperIdentity(), 1.0, HtmlColorUtils.WHITE,
				getMetadata(), null, 0, 0, null, false);
		imageBuilder.setUDrawable(result);
		return imageBuilder.writeImageTOBEMOVED(fileFormat, seed, os);
	}

	private UDrawable getGraphicStrings() throws IOException {
		final List<TextBlock> cols = getCols(getDonors(), 4, 5);
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				final TextBlockBackcolored header = GraphicStrings.createBlackOnWhite(Arrays
						.asList("<b>Special thanks to our sponsors and donors !"));
				header.drawU(ug);
				final StringBounder stringBounder = ug.getStringBounder();
				ug = ug.apply(new UTranslate(0, header.calculateDimension(stringBounder).getHeight()));
				double x = 0;
				double lastX = 0;
				double y = 0;
				for (TextBlock tb : cols) {
					final Dimension2D dim = tb.calculateDimension(stringBounder);
					tb.drawU(ug.apply(new UTranslate(x, 0)));
					lastX = x;
					x += dim.getWidth() + 10;
					y = Math.max(y, dim.getHeight());
				}
				final UImage logo = new UImage(PSystemVersion.getPlantumlImage());
				ug.apply(new UTranslate(lastX, y - logo.getHeight())).draw(logo);
			}
		};
	}

	public static List<TextBlock> getCols(List<String> lines, final int nbCol, final int reserved) throws IOException {
		final List<TextBlock> result = new ArrayList<TextBlock>();
		final int maxLine = (lines.size() + (nbCol - 1) + reserved) / nbCol;
		for (int i = 0; i < lines.size(); i += maxLine) {
			final List<String> current = lines.subList(i, Math.min(lines.size(), i + maxLine));
			result.add(GraphicStrings.createBlackOnWhite(current));
		}
		return result;
	}

	private List<String> getDonors() throws IOException {
		final List<String> lines = new ArrayList<String>();
		final Transcoder t = new TranscoderImpl(new CompressionBrotli());
		final String s = t.decode(DONORS).replace('*', '.');
		final StringTokenizer st = new StringTokenizer(s, BackSlash.NEWLINE);
		while (st.hasMoreTokens()) {
			lines.add(st.nextToken());
		}
		return lines;
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Donors)");
	}

	public static PSystemDonors create() {
		return new PSystemDonors();
	}

}
