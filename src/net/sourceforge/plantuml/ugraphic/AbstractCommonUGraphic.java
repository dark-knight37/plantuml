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
package net.sourceforge.plantuml.ugraphic;

import net.sourceforge.plantuml.graphic.HtmlColor;

public abstract class AbstractCommonUGraphic implements UGraphic {

	private UStroke stroke = new UStroke();
	private UPattern pattern = UPattern.FULL;
	private boolean hidden = false;
	private HtmlColor backColor = null;
	private HtmlColor color = null;

	private UTranslate translate = new UTranslate();

	private final ColorMapper colorMapper;
	private UClip clip;
	private double scale = 1;

	public double dpiFactor() {
		return 1;
	}

	public UGraphic apply(UChange change) {
		final AbstractCommonUGraphic copy = copyUGraphic();
		if (change instanceof UTranslate) {
			copy.translate = ((UTranslate) change).scaled(scale).compose(copy.translate);
		} else if (change instanceof UClip) {
			copy.clip = (UClip) change;
			copy.clip = copy.clip.translate(getTranslateX(), getTranslateY());
		} else if (change instanceof UStroke) {
			copy.stroke = (UStroke) change;
		} else if (change instanceof UPattern) {
			copy.pattern = (UPattern) change;
		} else if (change instanceof UHidden) {
			copy.hidden = change == UHidden.HIDDEN;
		} else if (change instanceof UChangeBackColor) {
			copy.backColor = ((UChangeBackColor) change).getBackColor();
		} else if (change instanceof UChangeColor) {
			copy.color = ((UChangeColor) change).getColor();
		} else if (change instanceof UScale) {
			final double factor = ((UScale) change).getScale();
			copy.scale = scale * factor;
		}
		return copy;
	}

	final public UClip getClip() {
		return clip;
	}

	public AbstractCommonUGraphic(ColorMapper colorMapper) {
		this.colorMapper = colorMapper;
	}

	protected AbstractCommonUGraphic(AbstractCommonUGraphic other) {
		this.colorMapper = other.colorMapper;
		this.translate = other.translate;
		this.clip = other.clip;

		this.stroke = other.stroke;
		this.pattern = other.pattern;
		this.hidden = other.hidden;
		this.color = other.color;
		this.backColor = other.backColor;
		this.scale = other.scale;
	}

	protected abstract AbstractCommonUGraphic copyUGraphic();

	final public UParam getParam() {
		return new UParam() {

			public boolean isHidden() {
				return hidden;
			}

			public UStroke getStroke() {
				return stroke;
			}

			public HtmlColor getColor() {
				return color;
			}

			public HtmlColor getBackcolor() {
				return backColor;
			}

			public UPattern getPattern() {
				return pattern;
			}

			public double getScale() {
				return scale;
			}
		};
	}

	final protected double getTranslateX() {
		return translate.getDx();
	}

	final protected double getTranslateY() {
		return translate.getDy();
	}

	final public ColorMapper getColorMapper() {
		return new ColorMapperTransparentWrapper(colorMapper);
	}

	public void flushUg() {

	}

	public boolean matchesProperty(String propertyName) {
		return false;
	}

}
