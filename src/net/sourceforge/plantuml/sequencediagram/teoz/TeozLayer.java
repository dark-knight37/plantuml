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
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.png.PngTitler;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class TeozLayer extends AbstractTextBlock implements TextBlock {

	private final PngTitler titler;
	private Dimension2D dimension;
	private final FontParam param;

	public TeozLayer(PngTitler titler, StringBounder stringBounder, FontParam param) {
		this.titler = titler;
		this.param = param;

		dimension = new Dimension2DDouble(0, 0);
		if (titler != null && titler.getTextBlock() != null) {
			dimension = titler.getTextBlock().calculateDimension(stringBounder);
		}
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return dimension;
	}

	public FontParam getParam() {
		return param;
	}

	public void drawU(UGraphic ug) {
		if (titler != null) {
			titler.getTextBlock().drawU(ug);
		}
	}

}
