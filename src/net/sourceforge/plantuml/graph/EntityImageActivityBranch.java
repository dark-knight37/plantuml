/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
package net.sourceforge.plantuml.graph;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;

class EntityImageActivityBranch extends AbstractEntityImage {

	private final int size = 10;

	public EntityImageActivityBranch(IEntity entity) {
		super(entity);
	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(size * 2, size * 2);
	}

	@Override
	public void draw(ColorMapper colorMapper, Graphics2D g2d) {
		final Polygon p = new Polygon();
		p.addPoint(size, 0);
		p.addPoint(size * 2, size);
		p.addPoint(size, size * 2);
		p.addPoint(0, size);

		g2d.setColor(colorMapper.toColor(getYellow()));
		g2d.fill(p);
		g2d.setColor(colorMapper.toColor(getRed()));
		g2d.draw(p);
	}
}
