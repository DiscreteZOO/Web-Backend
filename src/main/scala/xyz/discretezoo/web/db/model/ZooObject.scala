package xyz.discretezoo.web.db.model

import xyz.discretezoo.web.Parameter
import xyz.discretezoo.web.db.{Property, ZooPostgresProfile}

abstract class ZooObject {

  protected val indexColumnNames: Seq[String]
  protected def columnsIndex: Seq[Property] = indexColumnNames.map(f => Property(f, "index"))

  protected val boolColumnNames: Seq[String]
  protected def columnsBool: Seq[Property] = boolColumnNames.map(f => Property(f, "bool"))

  protected val intColumnNames: Seq[String]
  protected def columnsInt: Seq[Property] = intColumnNames.map(f => Property(f, "numeric"))

  protected val stringColumnNames: Seq[String]
  protected def columnsString: Seq[Property] = stringColumnNames.map(f => Property(f, "string"))

  protected def transformParameter(p: Parameter): Parameter

  def isValidFilterColumnName(s: String): Boolean = isValidIntColumnName(s) || isValidBoolColumnName(s)

  def all: Seq[Property] = columnsIndex ++ columnsBool ++ columnsInt ++ columnsString
  def getColumnList: String = all.map(p => s""""${p.name}"""").mkString(", ")

  def isValidIndexColumnName(s: String): Boolean = columnsIndex.map(_.name).contains(s)
  def isValidBoolColumnName(s: String): Boolean = columnsBool.map(_.name).contains(s)
  def isValidIntColumnName(s: String): Boolean = columnsInt.map(_.name).contains(s)
  def isValidStringColumnName(s: String): Boolean = columnsString.map(_.name).contains(s)

  def isValidQueryFilter(p: Parameter): Boolean = {
    val t = transformParameter(p)
    (isValidBoolColumnName(t.name) && isBoolValue(t.value)) ||
      (isValidIntColumnName(t.name) && isNumericCondition(t.value))
  }

  // assumes valid conditions
  def queryCondition(p: Parameter): String = {
    val t = transformParameter(p)
    val escapedColumnName = s""""${t.name}""""
    if (isValidBoolColumnName(t.name))
      (if (p.value == "false") "NOT " else "") + escapedColumnName
    else escapedColumnName + p.value.filter(_ > ' ')
  }

  def isBoolValue(s: String): Boolean = s == "true" || s == "false"
  def isNumericCondition(s: String): Boolean = {
    Seq("=", "<=", ">=", "<", ">", "<>", "!=").exists(op => {
      if (s.filter(_ > ' ').startsWith(op)) s.filter(_ > ' ').substring(op.length).forall(_.isDigit)
      else false
    })
  }

}
