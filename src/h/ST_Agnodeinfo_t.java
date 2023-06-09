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

final public class ST_Agnodeinfo_t extends ST_Agrec_s {

	public ST_shape_desc shape;
	public SHAPE_INFO shape_info;
	// public ST_polygon_t shape_info;
	public final ST_pointf coord = new ST_pointf();
	public double width, height;

	// "boxf bb",
	public double ht, lw, rw;
	public ST_textlabel_t label;
	public ST_textlabel_t xlabel;
	// "void *alg",
	public ST_Agedge_s alg = null;
	// "char state",
	// "unsigned char gui_state",
	// "boolean clustnode",
	// "unsigned char pinned",
	public int id, heapindex, hops;
	// "double *pos, dist",
	public int showboxes;

	public boolean has_port;
	// "node_t* rep",
	// "node_t *set",
	public int node_type, mark, onstack;
	public int ranktype, weight_class;
	public ST_Agnode_s next;
	public ST_Agnode_s prev;
	// "elist in, out, flat_out, flat_in, other",
	public final ST_elist in = new ST_elist();
	public final ST_elist out = new ST_elist();
	public final ST_elist flat_out = new ST_elist();
	public final ST_elist flat_in = new ST_elist();
	public final ST_elist other = new ST_elist();
	public ST_Agraph_s clust;
	public int UF_size;

	public ST_Agnode_s UF_parent;

	public ST_Agnode_s inleaf, outleaf;
	public int rank, order;
	public double mval;
	public final ST_elist save_in = new ST_elist();
	public final ST_elist save_out = new ST_elist();
	public final ST_elist tree_in = new ST_elist();
	public final ST_elist tree_out = new ST_elist();
	public ST_Agedge_s par;
	public int low, lim;
	public int priority;


	
}

// typedef struct Agnodeinfo_t {
// Agrec_t hdr;
// shape_desc *shape;
// void *shape_info;
// pointf coord;
// double width, height; /* inches */
// boxf bb;
// double ht, lw, rw;
// textlabel_t *label;
// textlabel_t *xlabel;
// void *alg;
// char state;
// unsigned char gui_state; /* Node state for GUI ops */
// boolean clustnode;
//
//
// unsigned char pinned;
// int id, heapindex, hops;
// double *pos, dist;
//
//
// unsigned char showboxes;
// boolean has_port;
// node_t* rep;
// node_t *set;
//
// /* fast graph */
// char node_type, mark, onstack;
// char ranktype, weight_class;
// node_t *next, *prev;
// elist in, out, flat_out, flat_in, other;
// graph_t *clust;
//
// /* for union-find and collapsing nodes */
// int UF_size;
// node_t *UF_parent;
// node_t *inleaf, *outleaf;
//
// /* for placing nodes */
// int rank, order; /* initially, order = 1 for ordered edges */
// double mval;
// elist save_in, save_out;
//
// /* for network-simplex */
// elist tree_in, tree_out;
// edge_t *par;
// int low, lim;
// int priority;
//
// double pad[1];
//
//
// } Agnodeinfo_t;