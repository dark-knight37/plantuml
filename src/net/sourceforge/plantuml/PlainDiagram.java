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
 */
package net.sourceforge.plantuml;

import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;

import java.io.IOException;
import java.io.OutputStream;

import static net.sourceforge.plantuml.ugraphic.ImageBuilder.plainImageBuilder;

// This class doesnt feel like a wonderful idea, just a stepping stone towards something
public abstract class PlainDiagram extends AbstractPSystem {

	@Override
	protected ImageData exportDiagramNow(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {

		final UDrawable drawable = getRootDrawable(fileFormatOption);

		final ImageBuilder builder = plainImageBuilder(drawable, fileFormatOption)
				.margin(getDefaultMargins())
				.metadata(fileFormatOption.isWithMetadata() ? getMetadata() : null)
				.seed(seed());

		return adjustImageBuilder(builder).write(os);
	}

	// kind of a kludge but good enough for now!
	protected ImageBuilder adjustImageBuilder(ImageBuilder builder) {
		return builder;
	}

	protected abstract UDrawable getRootDrawable(FileFormatOption fileFormatOption) throws IOException;
}
