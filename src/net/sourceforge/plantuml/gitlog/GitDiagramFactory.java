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
package net.sourceforge.plantuml.gitlog;

import java.util.Iterator;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.api.ThemeStyle;
import net.sourceforge.plantuml.command.PSystemAbstractFactory;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;

public class GitDiagramFactory extends PSystemAbstractFactory {

	public GitDiagramFactory() {
		super(DiagramType.GIT);
	}

	@Override
	public Diagram createSystem(ThemeStyle style, UmlSource source, ISkinSimple skinParam) {
		final GitTextArea textArea = new GitTextArea();

		final Iterator<StringLocated> it = source.iterator2();
		it.next();
		while (true) {
			final String line = it.next().getString();
			if (it.hasNext() == false)
				break;

			textArea.add(line);
		}
		return new GitDiagram(style, source, textArea);
	}

}
