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
package net.sourceforge.plantuml.mindmap;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamColors;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileBoxOld;
import net.sourceforge.plantuml.awt.geom.Dimension2D;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class FingerImpl implements Finger, UDrawable {

	private final Idea idea;
	private final ISkinParam skinParam;
	private final Direction direction;
	private boolean drawPhalanx = true;

	private final List<FingerImpl> nail = new ArrayList<>();
	private Tetris tetris = null;

	public static FingerImpl build(Idea idea, ISkinParam skinParam, Direction direction) {
		final FingerImpl result = new FingerImpl(idea, skinParam, direction);
		for (Idea child : idea.getChildren())
			result.addInNail(build(child, skinParam, direction));

		return result;
	}

	public void addInNail(FingerImpl child) {
		nail.add(child);
	}

	private FingerImpl(Idea idea, ISkinParam skinParam, Direction direction) {
		this.idea = idea;
		this.skinParam = skinParam;
		this.direction = direction;
	}

	private ClockwiseTopRightBottomLeft getMargin() {
		return getStyle().getMargin();
	}

	public void drawU(final UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final TextBlock phalanx = getPhalanx();
		final Dimension2D dimPhalanx = phalanx.calculateDimension(stringBounder);
		if (drawPhalanx) {
			final double posY = -getPhalanxThickness(stringBounder) / 2;
			final double posX = direction == Direction.RIGHT ? 0 : -dimPhalanx.getWidth();
			phalanx.drawU(ug.apply(new UTranslate(posX, posY)));
		}
		final Point2D p1 = new Point2D.Double(
				direction == Direction.RIGHT ? dimPhalanx.getWidth() : -dimPhalanx.getWidth(), 0);

		for (int i = 0; i < nail.size(); i++) {
			final FingerImpl child = nail.get(i);
			final SymetricalTeePositioned stp = getTetris(stringBounder).getElements().get(i);
			final double x = direction == Direction.RIGHT ? dimPhalanx.getWidth() + getX12()
					: -dimPhalanx.getWidth() - getX12();
			final Point2D p2 = new Point2D.Double(x, stp.getY());
			child.drawU(ug.apply(new UTranslate(p2)));
			drawLine(ug.apply(getLinkColor()).apply(getUStroke()), p1, p2);
		}

	}

	private HColor getLinkColor() {
		final Style styleArrow = getStyleArrow();
		return styleArrow.value(PName.LineColor).asColor(skinParam.getThemeStyle(), skinParam.getIHtmlColorSet());
	}

	private UStroke getUStroke() {
		final Style styleArrow = getStyleArrow();
		return styleArrow.getStroke();
	}

	private void drawLine(UGraphic ug, Point2D p1, Point2D p2) {
		// final ULine line = new ULine(p1, p2);
		// ug.apply(new UTranslate(p1)).draw(line);
		final UPath path = new UPath();
		final double delta1 = direction == Direction.RIGHT ? 10 : -10;
		final double delta2 = direction == Direction.RIGHT ? 25 : -25;
		path.moveTo(p1);
		path.lineTo(p1.getX() + delta1, p1.getY());
		path.cubicTo(p1.getX() + delta2, p1.getY(), p2.getX() - delta2, p2.getY(), p2.getX() - delta1, p2.getY());
		path.lineTo(p2);
		ug.draw(path);
	}

	private Tetris getTetris(StringBounder stringBounder) {
		if (tetris == null) {
			tetris = new Tetris(idea.getLabel().toString());
			for (FingerImpl child : nail)
				tetris.add(child.asSymetricalTee(stringBounder));

			tetris.balance();
		}
		return tetris;
	}

	private SymetricalTee asSymetricalTee(StringBounder stringBounder) {
		final double thickness1 = getPhalanxThickness(stringBounder);
		final double elongation1 = getPhalanxElongation(stringBounder);
		if (nail.size() == 0)
			return new SymetricalTee(thickness1, elongation1, 0, 0);

		final double thickness2 = getNailThickness(stringBounder);
		final double elongation2 = getNailElongation(stringBounder);
		return new SymetricalTee(thickness1, elongation1 + getX1(), thickness2, getX2() + elongation2);
	}

	private double getX1() {
		return getMargin().getLeft();
	}

	private double getX2() {
		return getMargin().getRight() + 30;
	}

	public double getX12() {
		return getX1() + getX2();
	}

	public double getPhalanxThickness(StringBounder stringBounder) {
		return getPhalanx().calculateDimension(stringBounder).getHeight();
	}

	public double getPhalanxElongation(StringBounder stringBounder) {
		return getPhalanx().calculateDimension(stringBounder).getWidth();
	}

	private TextBlock getPhalanx() {
		if (drawPhalanx == false)
			return TextBlockUtils.empty(0, 0);

		final Style style = getStyle();

		if (idea.getShape() == IdeaShape.BOX) {
			final ISkinParam foo = new SkinParamColors(skinParam,
					Colors.empty().add(ColorType.BACK, idea.getBackColor()));
			final TextBlock box = FtileBoxOld.createMindMap(style, foo, idea.getLabel());
			final ClockwiseTopRightBottomLeft margin = getMargin();
			return TextBlockUtils.withMargin(box, 0, 0, margin.getTop(), margin.getBottom());
		}

		assert idea.getShape() == IdeaShape.NONE;
		final TextBlock text = idea.getLabel().create0(
				style.getFontConfiguration(skinParam.getThemeStyle(), skinParam.getIHtmlColorSet()),
				style.getHorizontalAlignment(), skinParam, style.wrapWidth(), CreoleMode.FULL, null, null);
		if (direction == Direction.RIGHT)
			return TextBlockUtils.withMargin(text, 3, 0, 1, 1);

		return TextBlockUtils.withMargin(text, 0, 3, 1, 1);
	}

	private Style getStyle() {
		if (nail.size() != idea.getChildren().size())
			throw new IllegalStateException();

		return idea.getStyle();
	}

	private Style getStyleArrow() {
		return idea.getStyleArrow();
	}

	public double getNailThickness(StringBounder stringBounder) {
		return getTetris(stringBounder).getHeight();
	}

	public double getNailElongation(StringBounder stringBounder) {
		return getTetris(stringBounder).getWidth();
	}

	public double getFullThickness(StringBounder stringBounder) {
		final double thickness1 = getPhalanxThickness(stringBounder);
		final double thickness2 = getNailThickness(stringBounder);
		// System.err.println("thickness1=" + thickness1 + " thickness2=" + thickness2);
		return Math.max(thickness1, thickness2);
	}

	public double getFullElongation(StringBounder stringBounder) {
		return getPhalanxElongation(stringBounder) + getNailElongation(stringBounder);
	}

	public void doNotDrawFirstPhalanx() {
		this.drawPhalanx = false;
	}

}
