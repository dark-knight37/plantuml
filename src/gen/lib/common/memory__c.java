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
package gen.lib.common;
import static smetana.core.JUtils.memset;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import smetana.core.__ptr__;
import smetana.core.size_t;

public class memory__c {


@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/memory.c", name="", key="6hfkgng9qf75cucpojc4r8x6w", definition="void *zmalloc(size_t nbytes)")
public static __ptr__ zmalloc(size_t nbytes) {
ENTERING("6hfkgng9qf75cucpojc4r8x6w","zmalloc");
try {
    __ptr__ rv;
    if (nbytes.isZero())
	return null;
    rv = gmalloc(nbytes);
    memset(rv, 0, nbytes);
    return rv;
} finally {
LEAVING("6hfkgng9qf75cucpojc4r8x6w","zmalloc");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/memory.c", name="gmalloc", key="4mfikqpmxyxrke46i5xakatmc", definition="void *gmalloc(size_t nbytes)")
public static __ptr__ gmalloc(size_t nbytes) {
ENTERING("4mfikqpmxyxrke46i5xakatmc","gmalloc");
try {
    __ptr__ rv;
    if (nbytes.isZero())
	return null;
    rv = (__ptr__) nbytes.malloc();
    if (rv == null) {
    UNSUPPORTED("out of memory");
    }
    return rv;
} finally {
LEAVING("4mfikqpmxyxrke46i5xakatmc","gmalloc");
}
}




//3 1ed55yig6d18nhtbyqlf37jik
// void *grealloc(void *ptr, size_t size) 
@Unused
@Original(version="2.38.0", path="lib/common/memory.c", name="", key="1ed55yig6d18nhtbyqlf37jik", definition="void *grealloc(void *ptr, size_t size)")
public static __ptr__ grealloc(__ptr__ ptr, size_t size) {
ENTERING("1ed55yig6d18nhtbyqlf37jik","grealloc");
try {
    __ptr__ p = (__ptr__) size.realloc(ptr);
    return p;
} finally {
LEAVING("1ed55yig6d18nhtbyqlf37jik","grealloc");
}
}


}
