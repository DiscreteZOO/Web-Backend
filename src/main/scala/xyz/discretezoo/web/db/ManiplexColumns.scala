package xyz.discretezoo.web.db

import xyz.discretezoo.web.Parameter

object ManiplexColumns {

  def getColumnList: String = all.map(p => s""""${p.name}"""").mkString(", ")

  def all: Seq[Property] = columnsBool ++ columnsInt ++ columnsString

  def isValidBoolColumnName(s: String): Boolean = columnsBool.map(_.name).contains(s)
  def isValidIntColumnName(s: String): Boolean = columnsInt.map(_.name).contains(s)
  def isValidStringColumnName(s: String): Boolean = columnsString.map(_.name).contains(s)
  def isValidFilterColumnName(s: String): Boolean = {
    val testableName = s.toUpperCase
    isValidIntColumnName(testableName) || isValidBoolColumnName(testableName) || isValidStringColumnName(testableName)
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

  def isValidQueryFilter(p: Parameter): Boolean = {
    (isValidBoolColumnName(p.name.toUpperCase) && isBoolValue(p.value)) ||
      (isValidIntColumnName(p.name.toUpperCase) && isNumericCondition(p.value)) ||
      (isValidStringColumnName(p.name.toUpperCase) && isStringCondition(p.value))
  }

  // assumes valid conditions
  def queryCondition(p: Parameter): String = {
    val escapedColumnName = s""""${p.name.toUpperCase}""""
    if (isValidBoolColumnName(p.name.toUpperCase)) {
      (if (p.value == "false") "NOT " else "") + escapedColumnName
    }
    else if (isValidIntColumnName(p.name.toUpperCase)) {
      escapedColumnName + p.value.filter(_ > ' ')
    }
    else s"""$escapedColumnName = "${parseStringCondition(p.value)}""""
  }

  private def columnsBool: Seq[Property] = Seq(
    "IS_POLYTOPE",
    "IS_REGULAR"
  ).map(f => Property(f, "bool"))

  private def columnsInt: Seq[Property] = Seq(
    "ORBITS",
    "RANK",
    "SMALL_GROUP_ID",
    "SMALL_GROUP_ORDER"
  ).map(f => Property(f, "bool"))

  private def columnsString: Seq[Property] = Seq(
    "SYMMETRY_TYPE",
  ).map(f => Property(f, "bool"))
}
