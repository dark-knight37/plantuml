/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of Smetana.
 * Smetana is a partial translation of Graphviz/Dot sources from C to Java.
 *
 * (C) Copyright 2009-2022, Arnaud Roques
 *
 * This translation is distributed under the same Licence as the original C program:
 * 
 *************************************************************************
 * Copyright (c) 2011 AT&T Intellectual Property 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: See CVS logs. Details at http://www.graphviz.org/
 *************************************************************************
 *
 * THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE PUBLIC
 * LICENSE ("AGREEMENT"). [Eclipse Public License - v 1.0]
 * 
 * ANY USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM CONSTITUTES
 * RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.
 * 
 * You may obtain a copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package h;

import smetana.core.UnsupportedStarStruct;
import smetana.core.__struct__;

final public class ST_boxf extends UnsupportedStarStruct {

	public final ST_pointf LL = new ST_pointf();
	public final ST_pointf UR = new ST_pointf();

	@Override
	public String toString() {
		return "LL=" + LL + " UR=" + UR;
	}

	public static ST_boxf[] malloc(int nb) {
		final ST_boxf result[] = new ST_boxf[nb];
		for (int i = 0; i < nb; i++) {
			result[i] = new ST_boxf();
		}
		return result;
	}

	@Override
	public ST_boxf copy() {
		final ST_boxf result = new ST_boxf();
		result.LL.___((__struct__) this.LL);
		result.UR.___((__struct__) this.UR);
		return result;
	}

	@Override
	public void ___(__struct__ value) {
		final ST_boxf other = (ST_boxf) value;
		this.LL.___(other.LL);
		this.UR.___(other.UR);
	}

}

// typedef struct { pointf LL, UR; } boxf;