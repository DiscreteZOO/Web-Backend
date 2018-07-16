package xyz.discretezoo.web.db.conditions

case class IntCondition(column: String)

object IntCondition {

  private def isValidColumnName(s: String): Boolean = {
    Seq( // graphs
      "order",
      "chromatic_index",
      "clique_number",
      "connected_components_number",
      "diameter",
      "girth",
      "number_of_loops",
      "odd_girth",
      "size",
      "triangles_count"
    ).contains(s)
  }

  def fromString(s: String): Option[IntCondition] = if (isValidColumnName(s)) Some(IntCondition(s)) else None
}