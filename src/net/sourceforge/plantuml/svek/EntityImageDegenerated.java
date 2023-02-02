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

import net.sourceforge.plantuml.awt.geom.XDimension2D;
import net.sourceforge.plantuml.awt.geom.XRectangle2D;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.ugraphic.UEmpty;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class EntityImageDegenerated implements IEntityImage {

	private final IEntityImage orig;
	private final double delta = 7;
	private final HColor backcolor;

	public EntityImageDegenerated(IEntityImage orig, HColor backcolor) {
		this.orig = orig;
		this.backcolor = backcolor;
	}

	public boolean isHidden() {
		return orig.isHidden();
	}

	public HColor getBackcolor() {
		// return orig.getBackcolor();
		return backcolor;
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return orig.calculateDimension(stringBounder).delta(delta * 2, delta * 2);
	}

	public MinMax getMinMax(StringBounder stringBounder) {
		return orig.getMinMax(stringBounder);
		// return orig.getMinMax(stringBounder).translate(new UTranslate(delta, delta));
		// return orig.getMinMax(stringBounder).appendToMax(delta, delta);
	}

	public XRectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
		return orig.getInnerPosition(member, stringBounder, strategy);
	}

	public void drawU(UGraphic ug) {
		orig.drawU(ug.apply(new UTranslate(delta, delta)));

		final XDimension2D dim = calculateDimension(ug.getStringBounder());
		ug.apply(new UTranslate(dim.getWidth() - delta, dim.getHeight() - delta)).draw(new UEmpty(delta, delta));

	}

	public ShapeType getShapeType() {
		return orig.getShapeType();
	}

	public Margins getShield(StringBounder stringBounder) {
		return orig.getShield(stringBounder);
	}

	public double getOverscanX(StringBounder stringBounder) {
		return orig.getOverscanX(stringBounder);
	}

}