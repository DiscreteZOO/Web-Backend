package xyz.discretezoo.web.db.model

import xyz.discretezoo.web.Parameter
import xyz.discretezoo.web.db.Property

abstract class Columns {

  protected def columnsIndex: Seq[Property]
  protected def columnsBool: Seq[Property]
  protected def columnsInt: Seq[Property]
  protected def columnsString: Seq[Property]

  protected def transformParameter(p: Parameter): Parameter

  def isValidFilterColumnName(s: String): Boolean

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
