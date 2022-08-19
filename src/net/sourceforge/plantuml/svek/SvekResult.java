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
 * 
 *
 */
package net.sourceforge.plantuml.svek;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.awt.geom.Dimension2D;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UHidden;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColors;

public final class SvekResult extends AbstractTextBlock implements IEntityImage {

	private final DotData dotData;
	private final DotStringFactory dotStringFactory;

	public SvekResult(DotData dotData, DotStringFactory dotStringFactory) {
		this.dotData = dotData;
		this.dotStringFactory = dotStringFactory;
	}

	public void drawU(UGraphic ug) {

		for (Cluster cluster : dotStringFactory.getBibliotekon().allCluster())
			cluster.drawU(ug, dotData.getUmlDiagramType(), dotData.getSkinParam());

		final Style style2 = getDefaultStyleDefinition(null)
				.getMergedStyle(dotData.getSkinParam().getCurrentStyleBuilder());

		HColor color = style2.value(PName.LineColor).asColor(dotData.getSkinParam().getThemeStyle(),
				dotData.getSkinParam().getIHtmlColorSet());
		color = HColors.noGradient(color);

		for (SvekNode node : dotStringFactory.getBibliotekon().allNodes()) {
			final double minX = node.getMinX();
			final double minY = node.getMinY();
			final UGraphic ug2 = node.isHidden() ? ug.apply(UHidden.HIDDEN) : ug;
			final IEntityImage image = node.getImage();
			image.drawU(ug2.apply(new UTranslate(minX, minY)));
			if (image instanceof Untranslated)
				((Untranslated) image).drawUntranslated(ug.apply(color), minX, minY);

		}

		final Set<String> ids = new HashSet<>();

		for (SvekLine line : dotStringFactory.getBibliotekon().allLines()) {
			final UGraphic ug2 = line.isHidden() ? ug.apply(UHidden.HIDDEN) : ug;

			final StyleBuilder currentStyleBuilder = line.getCurrentStyleBuilder();
			final Style styleLine = getDefaultStyleDefinition(line.getStereotype()).getMergedStyle(currentStyleBuilder);
			color = styleLine.value(PName.LineColor).asColor(dotData.getSkinParam().getThemeStyle(),
					dotData.getSkinParam().getIHtmlColorSet());
			color = HColors.noGradient(color);

			line.drawU(ug2, styleLine.getStroke(), color, ids);
		}

	}

	private StyleSignature getDefaultStyleDefinition(Stereotype stereotype) {
		StyleSignature result = StyleSignatureBasic.of(SName.root, SName.element,
				dotData.getUmlDiagramType().getStyleName(), SName.arrow);

		return result.withTOBECHANGED(stereotype);
	}

	// Duplicate SvekResult / GeneralImageBuilder
	public HColor getBackcolor() {
		final Style style = StyleSignatureBasic.of(SName.root, SName.document)
				.getMergedStyle(dotData.getSkinParam().getCurrentStyleBuilder());
		return style.value(PName.BackGroundColor).asColor(dotData.getSkinParam().getThemeStyle(),
				dotData.getSkinParam().getIHtmlColorSet());
	}

	private MinMax minMax;

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		if (minMax == null) {
			minMax = TextBlockUtils.getMinMax(this, stringBounder, false);
			dotStringFactory.moveSvek(6 - minMax.getMinX(), 6 - minMax.getMinY());
		}
		return Dimension2DDouble.delta(minMax.getDimension(), 0, 12);
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public Margins getShield(StringBounder stringBounder) {
		return Margins.NONE;
	}

	public boolean isHidden() {
		return false;
	}

	public double getOverscanX(StringBounder stringBounder) {
		return 0;
	}

}
