package xyz.discretezoo.web.db

import xyz.discretezoo.web.Parameter
import xyz.discretezoo.web.db.model.Columns

object ManiplexColumns extends Columns {

  def isValidFilterColumnName(s: String): Boolean = {
    val testableName = s.toUpperCase
    isValidIntColumnName(testableName) || isValidBoolColumnName(testableName) || isValidStringColumnName(testableName)
  }

  override protected def transformParameter(p: Parameter): Parameter = Parameter(p.name.toUpperCase, p.value)

  override protected def columnsIndex: Seq[Property] = Seq()

  override protected def columnsBool: Seq[Property] = Seq(
    "IS_POLYTOPE",
    "IS_REGULAR"
  ).map(f => Property(f, "bool"))

  override protected def columnsInt: Seq[Property] = Seq(
    "ORBITS",
    "RANK",
    "SMALL_GROUP_ID",
    "SMALL_GROUP_ORDER"
  ).map(f => Property(f, "bool"))

  override protected def columnsString: Seq[Property] = Seq(
    "SYMMETRY_TYPE",
  ).map(f => Property(f, "bool"))
}
