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
 * Revision $Revision: 19398 $
 *
 */
package net.sourceforge.plantuml.vizjs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class VizJsEngine {

	public static boolean isOk() {
		try {
			final Class classVizJS = Class.forName("ch.braincell.viz.VizJS");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private final Object viz;
	private final Method mExecute;

	public VizJsEngine() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final Class classVizJS = Class.forName("ch.braincell.viz.VizJS");
		final Method mCreate = classVizJS.getMethod("create");
		mExecute = classVizJS.getMethod("execute", String.class);
		this.viz = mCreate.invoke(null);
		System.err.println("Creating one engine");
	}

	public String execute(String dot) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		return (String) mExecute.invoke(viz, dot);
	}

}
