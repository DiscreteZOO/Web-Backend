package xyz.discretezoo.web.db

object GraphColumns {

  def getColumnList: String = all.map(p => s""""${p.name}"""").mkString(", ")

  def all: Seq[Property] = columnsIndex ++ columnsBool ++ columnsInt

  def isValidBoolColumnName(s: String): Boolean = columnsBool.map(_.name).contains(s)
  def isValidIntColumnName(s: String): Boolean = columnsInt.map(_.name).contains(s)
  def isValidIndexColumnName(s: String): Boolean = columnsIndex.map(_.name).contains(s)
  def isValidFilterColumnName(s: String): Boolean = isValidIntColumnName(s) || isValidBoolColumnName(s)

  def isBoolValue(s: String): Boolean = s == "true" || s == "false"
  def isNumericCondition(s: String): Boolean = {
    Seq("=", "<=", ">=", "<", ">", "<>", "!=").exists(op => {
      if (s.filter(_ > ' ').startsWith(op)) s.filter(_ > ' ').substring(op.length).forall(_.isDigit)
      else false
    })
  }

  def isValidQueryFilter(p: (String, String)): Boolean = {
    (isValidBoolColumnName(p._1) && isBoolValue(p._2)) || (isValidIntColumnName(p._1) && isNumericCondition(p._2))
  }

  // assumes valid conditions
  def queryCondition(p: (String, String)): String = {
    val escapedColumnName = s""""${p._1}""""
    if (isValidBoolColumnName(p._1))
      (if (p._2 == "false") "NOT " else "") + escapedColumnName
    else escapedColumnName + p._2.filter(_ > ' ')
  }

  // CVT: "truncation"

  private def columnsBool: Seq[Property] = Seq(
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

  private def columnsInt: Seq[Property] = Seq(
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

  private def columnsIndex: Seq[Property] = Seq(
    "cvt_index", // CVT
    "symcubic_index", // CVT
    "vt_index" // VT
  ).map(f => Property(f, "index"))

}
