/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 5183 $
 *
 */
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.cucadiagram.BodyEnhanced2;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.Bibliotekon;
import net.sourceforge.plantuml.svek.Shape;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class EntityImageTips extends AbstractEntityImage {

	final private Rose rose = new Rose();
	private final ISkinParam skinParam;

	private final HtmlColor noteBackgroundColor;
	private final HtmlColor borderColor;

	private final Bibliotekon bibliotekon;

	private final double ySpacing = 10;

	public EntityImageTips(ILeaf entity, ISkinParam skinParam, Bibliotekon bibliotekon) {
		super(entity, EntityImageNote.getSkin(skinParam, entity));
		this.skinParam = skinParam;
		this.bibliotekon = bibliotekon;

		if (entity.getSpecificBackColor() == null) {
			noteBackgroundColor = rose.getHtmlColor(skinParam, ColorParam.noteBackground);
		} else {
			noteBackgroundColor = entity.getSpecificBackColor();
		}
		this.borderColor = rose.getHtmlColor(skinParam, ColorParam.noteBorder);
	}

	private Position getPosition() {
		if (getEntity().getCode().getFullName().endsWith(Position.RIGHT.name())) {
			return Position.RIGHT;
		}
		return Position.LEFT;
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public int getShield() {
		return 0;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		double width = 0;
		double height = 0;
		for (Map.Entry<String, Display> ent : getEntity().getTips().entrySet()) {
			final Display display = ent.getValue();
			final Dimension2D dim = getOpale(display).calculateDimension(stringBounder);
			height += dim.getHeight();
			height += ySpacing;
			width = Math.max(width, dim.getWidth());
		}
		return new Dimension2DDouble(width, height);
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		final IEntity other = bibliotekon.getOnlyOther(getEntity());

		final Shape shapeMe = bibliotekon.getShape(getEntity());
		final Shape shapeOther = bibliotekon.getShape(other);
		final Point2D positionMe = shapeMe.getPosition();
		final Point2D positionOther = shapeOther.getPosition();
		bibliotekon.getShape(getEntity());
		final Position position = getPosition();
		double height = 0;
		for (Map.Entry<String, Display> ent : getEntity().getTips().entrySet()) {
			final Display display = ent.getValue();
			final Rectangle2D memberPosition = shapeOther.getImage().getInnerPosition(ent.getKey(), stringBounder);
			final Opale opale = getOpale(display);
			final Dimension2D dim = opale.calculateDimension(stringBounder);
			final Point2D pp1 = new Point2D.Double(0, dim.getHeight() / 2);
			double x = positionOther.getX() - positionMe.getX();
			if (position == Position.RIGHT) {
				x += memberPosition.getMaxX();
			} else {
				x += 4;
			}
			final double y = positionOther.getY() - positionMe.getY() - height + memberPosition.getCenterY();
			final Point2D pp2 = new Point2D.Double(x, y);
			opale.setOpale(position.reverseDirection(), pp1, pp2);
			opale.drawU(ug);
			ug = ug.apply(new UTranslate(0, dim.getHeight() + ySpacing));
			height += dim.getHeight();
			height += ySpacing;
		}

	}

	private Opale getOpale(final Display display) {
		final HtmlColor fontColor = rose.getFontColor(skinParam, FontParam.NOTE);
		final UFont fontNote = skinParam.getFont(FontParam.NOTE, null, false);

		final TextBlock textBlock = new BodyEnhanced2(display, FontParam.NOTE, skinParam, HorizontalAlignment.LEFT,
				new FontConfiguration(fontNote, fontColor, skinParam.getHyperlinkColor(),
						skinParam.useUnderlineForHyperlink()));
		final Opale opale = new Opale(borderColor, noteBackgroundColor, textBlock, skinParam.shadowing(), true);
		return opale;
	}

}
