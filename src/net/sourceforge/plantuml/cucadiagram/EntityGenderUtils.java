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
package net.sourceforge.plantuml.cucadiagram;

import net.sourceforge.plantuml.baraye.Entity;
import net.sourceforge.plantuml.baraye.EntityUtils;
import net.sourceforge.plantuml.text.Guillemet;

public class EntityGenderUtils {

	static public EntityGender byEntityType(final LeafType type) {
		return new EntityGender() {
			public boolean contains(Entity test) {
				return test.getLeafType() == type;
			}
		};
	}

	static public EntityGender byEntityAlone(final Entity entity) {
		return new EntityGender() {
			public boolean contains(Entity test) {
				return test.getUid().equals(entity.getUid());
			}
		};
	}

	static public EntityGender byStereotype(final String stereotype) {
		return new EntityGender() {
			public boolean contains(Entity test) {
				if (test.getStereotype() == null) {
					return false;
				}
				return stereotype.equals(test.getStereotype().getLabel(Guillemet.DOUBLE_COMPARATOR));
			}
		};
	}

	static public EntityGender byPackage(final Entity group) {
		if (group.isRoot()) {
			throw new IllegalArgumentException();
		}
		return new EntityGender() {
			public boolean contains(Entity test) {
				if (test.getParentContainer().isRoot()) {
					return false;
				}
				if (group == test.getParentContainer()) {
					return true;
				}
				return false;
			}
		};
	}

	static public EntityGender and(final EntityGender g1, final EntityGender g2) {
		return new EntityGender() {
			public boolean contains(Entity test) {
				return g1.contains(test) && g2.contains(test);
			}
		};
	}

	static public EntityGender all() {
		return new EntityGender() {
			public boolean contains(Entity test) {
				return true;
			}
		};
	}

	static public EntityGender emptyMethods() {
		return new EntityGender() {
			public boolean contains(Entity test) {
				return test.getBodier().getMethodsToDisplay().size() == 0;
			}
		};
	}

	static public EntityGender emptyFields() {
		return new EntityGender() {
			public boolean contains(Entity test) {
				return test.getBodier().getFieldsToDisplay().size() == 0;
			}
		};
	}

}
