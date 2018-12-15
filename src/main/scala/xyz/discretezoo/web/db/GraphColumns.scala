package xyz.discretezoo.web.db

import xyz.discretezoo.web.Parameter
import xyz.discretezoo.web.db.model.Columns

object GraphColumns extends Columns {

  def isValidFilterColumnName(s: String): Boolean = isValidIntColumnName(s) || isValidBoolColumnName(s)

  // CVT: "truncation"

  override protected def transformParameter(p: Parameter): Parameter = p

  protected def columnsIndex: Seq[Property] = Seq(
    "cvt_index", // CVT
    "symcubic_index", // CVT
    "vt_index" // VT
  ).map(f => Property(f, "index"))

  protected def columnsBool: Seq[Property] = Seq(
    "has_multiple_edges",
    "is_arc_transitive",
    "is_bipartite",
    "is_cayley",
    "is_distance_regular",
    "is_distance_transitive",
    "is_edge_transitive",
    "is_eulerian",
    "is_forest",
    "is_hamiltonian",
    "is_overfull",
    "is_partial_cube",
//    "is_regular",
    "is_split",
    "is_strongly_regular",
    "is_tree",
//    "is_vertex_transitive",
    // CVT
    "is_moebius_ladder",
    "is_prism",
    "is_spx"
  ).map(f => Property(f, "bool"))

  protected def columnsInt: Seq[Property] = Seq(
    "order",
//    "average_degree",
    "chromatic_index",
    "clique_number",
    "connected_components_number",
    "diameter",
    "girth",
    "number_of_loops",
    "odd_girth",
    "size",
    "triangles_count"
  ).map(f => Property(f, "numeric"))

  protected def columnsString: Seq[Property] = Seq()

}
