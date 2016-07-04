/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * Revision $Revision: 8475 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.util.Collection;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.PositionedNote;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactoryDelegator;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.sequencediagram.NoteType;

public class FtileFactoryDelegatorAddNote extends FtileFactoryDelegator {

	public FtileFactoryDelegatorAddNote(FtileFactory factory) {
		super(factory);
	}

	@Override
	public Ftile addNote(Ftile ftile, Swimlane swimlane, Collection<PositionedNote> notes) {
		if (notes.size() == 0) {
			throw new IllegalArgumentException();
		}
		if (notes.size() > 1) {
			throw new IllegalArgumentException();
		}
		ISkinParam skinParam = skinParam();
		final PositionedNote note = notes.iterator().next();
		if (note.getColors() != null) {
			skinParam = note.getColors().mute(skinParam);
		}
		if (ftile == null) {
			return new FtileNoteAlone(skinParam.shadowing(), note.getDisplay(), skinParam,
					note.getType() == NoteType.NOTE, swimlane);
		}
		return FtileWithNoteOpale.create(ftile, notes, skinParam, true);
	}
}
