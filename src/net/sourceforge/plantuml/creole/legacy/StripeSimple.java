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
package net.sourceforge.plantuml.creole.legacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.creole.CreoleContext;
import net.sourceforge.plantuml.creole.CreoleHorizontalLine;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.creole.Stripe;
import net.sourceforge.plantuml.creole.StripeStyle;
import net.sourceforge.plantuml.creole.StripeStyleType;
import net.sourceforge.plantuml.creole.atom.Atom;
import net.sourceforge.plantuml.creole.atom.AtomEmojiTwo;
import net.sourceforge.plantuml.creole.atom.AtomImg;
import net.sourceforge.plantuml.creole.atom.AtomMath;
import net.sourceforge.plantuml.creole.atom.AtomOpenIcon;
import net.sourceforge.plantuml.creole.atom.AtomSpace;
import net.sourceforge.plantuml.creole.atom.AtomSprite;
import net.sourceforge.plantuml.creole.command.Command;
import net.sourceforge.plantuml.creole.command.CommandCreoleColorAndSizeChange;
import net.sourceforge.plantuml.creole.command.CommandCreoleColorChange;
import net.sourceforge.plantuml.creole.command.CommandCreoleEmojiTwo;
import net.sourceforge.plantuml.creole.command.CommandCreoleExposantChange;
import net.sourceforge.plantuml.creole.command.CommandCreoleFontFamilyChange;
import net.sourceforge.plantuml.creole.command.CommandCreoleImg;
import net.sourceforge.plantuml.creole.command.CommandCreoleLatex;
import net.sourceforge.plantuml.creole.command.CommandCreoleMath;
import net.sourceforge.plantuml.creole.command.CommandCreoleMonospaced;
import net.sourceforge.plantuml.creole.command.CommandCreoleOpenIcon;
import net.sourceforge.plantuml.creole.command.CommandCreoleQrcode;
import net.sourceforge.plantuml.creole.command.CommandCreoleSizeChange;
import net.sourceforge.plantuml.creole.command.CommandCreoleSpace;
import net.sourceforge.plantuml.creole.command.CommandCreoleSprite;
import net.sourceforge.plantuml.creole.command.CommandCreoleStyle;
import net.sourceforge.plantuml.creole.command.CommandCreoleSvgAttributeChange;
import net.sourceforge.plantuml.creole.command.CommandCreoleUrl;
import net.sourceforge.plantuml.emojitwo.EmojiTwo;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.FontPosition;
import net.sourceforge.plantuml.graphic.FontStyle;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.ImgValign;
import net.sourceforge.plantuml.math.ScientificEquationSafe;
import net.sourceforge.plantuml.openiconic.OpenIcon;
import net.sourceforge.plantuml.security.SecurityUtils;
import net.sourceforge.plantuml.sprite.Sprite;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.utils.CharHidder;

public class StripeSimple implements Stripe {

	final private Atom header;

	final private List<Atom> atoms = new ArrayList<>();

	// final private List<Command> commands = new ArrayList<>();
	final private Map<Character, List<Command>> commands = new HashMap<>();

	private HorizontalAlignment align = HorizontalAlignment.LEFT;

	public void setCellAlignment(HorizontalAlignment align) {
		this.align = align;
	}

	public HorizontalAlignment getCellAlignment() {
		return align;
	}

	private FontConfiguration fontConfiguration;

	final private StripeStyle style;
	final private ISkinSimple skinParam;

	@Override
	public String toString() {
		return super.toString() + " " + atoms.toString();
	}

	public Atom getLHeader() {
		return header;
	}

	public StripeSimple(FontConfiguration fontConfiguration, StripeStyle style, CreoleContext context,
			ISkinSimple skinParam, CreoleMode modeSimpleLine) {
		this.fontConfiguration = fontConfiguration;
		this.style = style;
		this.skinParam = skinParam;

		addCommand(CommandCreoleStyle.createCreole(FontStyle.BOLD));
		addCommand(CommandCreoleStyle.createLegacy(FontStyle.BOLD));
		addCommand(CommandCreoleStyle.createLegacyEol(FontStyle.BOLD));

		addCommand(CommandCreoleStyle.createCreole(FontStyle.ITALIC));
		addCommand(CommandCreoleStyle.createLegacy(FontStyle.ITALIC));
		addCommand(CommandCreoleStyle.createLegacyEol(FontStyle.ITALIC));
		addCommand(CommandCreoleStyle.createLegacy(FontStyle.PLAIN));
		addCommand(CommandCreoleStyle.createLegacyEol(FontStyle.PLAIN));
		if (modeSimpleLine == CreoleMode.FULL) {
			addCommand(CommandCreoleStyle.createCreole(FontStyle.UNDERLINE));
		}
		addCommand(CommandCreoleStyle.createLegacy(FontStyle.UNDERLINE));
		addCommand(CommandCreoleStyle.createLegacyEol(FontStyle.UNDERLINE));
		addCommand(CommandCreoleStyle.createCreole(FontStyle.STRIKE));
		addCommand(CommandCreoleStyle.createLegacy(FontStyle.STRIKE));
		addCommand(CommandCreoleStyle.createLegacyEol(FontStyle.STRIKE));
		addCommand(CommandCreoleStyle.createCreole(FontStyle.WAVE));
		addCommand(CommandCreoleStyle.createLegacy(FontStyle.WAVE));
		addCommand(CommandCreoleStyle.createLegacyEol(FontStyle.WAVE));
		addCommand(CommandCreoleStyle.createLegacy(FontStyle.BACKCOLOR));
		addCommand(CommandCreoleStyle.createLegacyEol(FontStyle.BACKCOLOR));
		addCommand(CommandCreoleSizeChange.create());
		addCommand(CommandCreoleSizeChange.createEol());
		addCommand(CommandCreoleColorChange.create());
		addCommand(CommandCreoleColorChange.createEol());
		addCommand(CommandCreoleColorAndSizeChange.create());
		addCommand(CommandCreoleColorAndSizeChange.createEol());
		addCommand(CommandCreoleExposantChange.create(FontPosition.EXPOSANT));
		addCommand(CommandCreoleExposantChange.create(FontPosition.INDICE));
		addCommand(CommandCreoleImg.create());
		addCommand(CommandCreoleQrcode.create());
		addCommand(CommandCreoleOpenIcon.create());
		addCommand(CommandCreoleEmojiTwo.create());
		addCommand(CommandCreoleMath.create());
		addCommand(CommandCreoleLatex.create());
		addCommand(CommandCreoleSprite.create());
		addCommand(CommandCreoleSpace.create());
		addCommand(CommandCreoleFontFamilyChange.create());
		addCommand(CommandCreoleFontFamilyChange.createEol());
		addCommand(CommandCreoleMonospaced.create());
		addCommand(CommandCreoleUrl.create());
		if (SecurityUtils.allowSvgText()) {
			addCommand(CommandCreoleSvgAttributeChange.create());
		}

		this.header = style.getHeader(fontConfiguration, context);

		if (this.header != null) {
			this.atoms.add(this.header);
		}
	}

	private void addCommand(Command cmd) {
		final String starters = cmd.startingChars();
		for (int i = 0; i < starters.length(); i++) {
			final char ch = starters.charAt(i);
			List<Command> localList = commands.get(ch);
			if (localList == null) {
				localList = new ArrayList<Command>();
				commands.put(ch, localList);
			}
			localList.add(cmd);
		}
	}

	public List<Atom> getAtoms() {
		if (atoms.size() == 0) {
			atoms.add(AtomTextUtils.createLegacy(" ", fontConfiguration));
		}
		return Collections.unmodifiableList(atoms);
	}

	public FontConfiguration getActualFontConfiguration() {
		return fontConfiguration;
	}

	public void setActualFontConfiguration(FontConfiguration fontConfiguration) {
		this.fontConfiguration = fontConfiguration;
	}

	public void analyzeAndAdd(String line) {
		if (Objects.requireNonNull(line).contains("" + BackSlash.hiddenNewLine())) {
			throw new IllegalArgumentException(line);
		}
		line = CharHidder.hide(line);
		if (style.getType() == StripeStyleType.HEADING) {
			fontConfiguration = fontConfigurationForHeading(fontConfiguration, style.getOrder());
			modifyStripe(line);
		} else if (style.getType() == StripeStyleType.HORIZONTAL_LINE) {
			atoms.add(CreoleHorizontalLine.create(fontConfiguration, line, style.getStyle(), skinParam));
		} else {
			modifyStripe(line);
		}
	}

	private static FontConfiguration fontConfigurationForHeading(FontConfiguration fontConfiguration, int order) {
		if (order == 0) {
			fontConfiguration = fontConfiguration.bigger(4).bold();
		} else if (order == 1) {
			fontConfiguration = fontConfiguration.bigger(2).bold();
		} else if (order == 2) {
			fontConfiguration = fontConfiguration.bigger(1).bold();
		} else {
			fontConfiguration = fontConfiguration.italic();
		}
		return fontConfiguration;
	}

	public void addImage(String src, double scale) {
		atoms.add(AtomImg.create(src, ImgValign.TOP, 0, scale, null));
	}

	public void addQrcode(String src, double scale) {
		atoms.add(AtomImg.createQrcode(src, scale));
	}

	public void addSpace(int size) {
		atoms.add(AtomSpace.create(size));
	}

	public void addUrl(Url url) {
		atoms.add(AtomTextUtils.createUrl(url, fontConfiguration, skinParam));
	}

	public void addSprite(String src, double scale, HColor color) {
		final Sprite sprite = skinParam.getSprite(src);
		if (sprite != null)
			atoms.add(new AtomSprite(color, scale, fontConfiguration, sprite, null));
	}

	public void addOpenIcon(String src, double scale, HColor color) {
		final OpenIcon openIcon = OpenIcon.retrieve(src);
		if (openIcon != null)
			atoms.add(new AtomOpenIcon(color, scale, openIcon, fontConfiguration, null));
	}

	public void addEmojiTwo(String emoji) {
		final EmojiTwo emojiTwo = EmojiTwo.retrieve(emoji);
		if (emojiTwo != null)
			atoms.add(new AtomEmojiTwo(emojiTwo, 1, fontConfiguration));

	}

	public void addMath(ScientificEquationSafe math) {
		atoms.add(new AtomMath(math, fontConfiguration.getColor(), fontConfiguration.getExtendedColor(),
				skinParam.getColorMapper()));
	}

	private void modifyStripe(String line) {
		final StringBuilder pending = new StringBuilder();

		while (line.length() > 0) {
			final Command cmd = searchCommand(line);
			if (cmd == null) {
				pending.append(line.charAt(0));
				line = line.substring(1);
			} else {
				addPending(pending);
				line = cmd.executeAndGetRemaining(line, this);
			}
		}
		addPending(pending);
	}

	private void addPending(StringBuilder pending) {
		if (pending.length() == 0) {
			return;
		}
		atoms.add(AtomTextUtils.createLegacy(pending.toString(), fontConfiguration));
		pending.setLength(0);
	}

	private Command searchCommand(String line) {
		final List<Command> localList = commands.get(line.charAt(0));
		if (localList != null)
			for (Command cmd : localList) {
				final int i = cmd.matchingSize(line);
				if (i != 0) {
					return cmd;
				}
			}
		return null;
	}

	public ISkinSimple getSkinParam() {
		return skinParam;
	}

}
