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
package net.sourceforge.plantuml.statediagram.command;

import net.sourceforge.plantuml.baraye.EntityImp;
import net.sourceforge.plantuml.baraye.Quark;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.statediagram.StateDiagram;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandAddField extends SingleLineCommand2<StateDiagram> {

	public CommandAddField() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandAddField.class.getName(), RegexLeaf.start(), //
				new RegexOr( //
						new RegexLeaf("CODE3", "([%pLN_.]+)"), //
						new RegexLeaf("CODE4", "[%g]([^%g]+)[%g]")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(":"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("FIELD", "(.*)"), RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(StateDiagram diagram, LineLocation location, RegexResult arg)
			throws NoSuchColorException {
		final String codeString = arg.getLazzy("CODE", 0);

		final Quark quark;
		if (diagram.currentQuark().getName().equals(codeString) && diagram.currentQuark().getData() != null)
			quark = diagram.currentQuark();
		else
			quark = diagram.quarkInContext(diagram.cleanIdForQuark(codeString), false);

		EntityImp entity = (EntityImp) quark.getData();
		if (entity == null)
			entity = diagram.reallyCreateLeaf(quark, Display.getWithNewlines(quark), LeafType.STATE, null);

		final String field = arg.get("FIELD", 0);

		entity.getBodier().addFieldOrMethod(field);
		return CommandExecutionResult.ok();

	}

}
