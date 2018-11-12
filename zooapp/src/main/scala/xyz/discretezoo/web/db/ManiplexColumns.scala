package xyz.discretezoo.web.db

import xyz.discretezoo.web.db.GraphColumns.{isValidBoolColumnName, _}

object ManiplexColumns {

  def getColumnList: String = all.map(p => s""""${p.name}"""").mkString(", ")

  def all: Seq[Property] = columnsBool ++ columnsInt

  def isValidBoolColumnName(s: String): Boolean = columnsBool.map(_.name).contains(s)
  def isValidIntColumnName(s: String): Boolean = columnsInt.map(_.name).contains(s)
  def isValidStringColumnName(s: String): Boolean = columnsString.map(_.name).contains(s)
  def isValidFilterColumnName(s: String): Boolean = {
    isValidIntColumnName(s) || isValidBoolColumnName(s) || isValidStringColumnName(s)
  }

  def isBoolValue(s: String): Boolean = s == "true" || s == "false"
  def isNumericCondition(s: String): Boolean = {
    Seq("=", "<=", ">=", "<", ">", "<>", "!=").exists(op => {
      if (s.filter(_ > ' ').startsWith(op)) s.filter(_ > ' ').substring(op.length).forall(_.isDigit)
      else false
    })
  }
  def isStringCondition(s: String): Boolean = {
    val cleared = s.filter(_ > ' ')
    if (cleared.startsWith("{") && cleared.endsWith("}")) {
      cleared.substring(1, cleared.length - 1).split(',').forall(_.forall(_.isDigit))
    }
    else false
  }

  // assumes valid condition
  def parseStringCondition(s: String): String = {
    val cleared = s.filter(_ > ' ')
    cleared.substring(1, cleared.length - 1).split(',').map(_.toInt).sorted.map(_.toString).mkString("-")
  }

  def isValidQueryFilter(p: (String, String)): Boolean = {
    (isValidBoolColumnName(p._1) && isBoolValue(p._2)) ||
      (isValidIntColumnName(p._1) && isNumericCondition(p._2)) ||
      (isValidStringColumnName(p._1) && isStringCondition(p._2))
  }

  // assumes valid conditions
  def queryCondition(p: (String, String)): String = {
    val escapedColumnName = s""""${p._1}""""
    if (isValidBoolColumnName(p._1))
      (if (p._2 == "false") "NOT " else "") + escapedColumnName
    else if (isValidIntColumnName(p._1)) escapedColumnName + p._2.filter(_ > ' ')
    else s"""$escapedColumnName = "${parseStringCondition(p._2)}""""
  }

  private def columnsBool: Seq[Property] = Seq(
    "IS_POLYTOPE",
    "IS_REGULAR"
  ).map(f => Property(f, "bool"))

  private def columnsInt: Seq[Property] = Seq(
    "ORBITS",
    "SMALL_GROUP_ORDER"
  ).map(f => Property(f, "bool"))

  private def columnsString: Seq[Property] = Seq(
    "SYMMETRY_TYPE",
  ).map(f => Property(f, "bool"))
}
