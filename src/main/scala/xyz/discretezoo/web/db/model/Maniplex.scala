package xyz.discretezoo.web.db.model

import xyz.discretezoo.web.Parameter

object Maniplex extends ZooObject {

  override def isValidFilterColumnName(s: String): Boolean = {
    val testableName = s.toUpperCase
    isValidIntColumnName(testableName) || isValidBoolColumnName(testableName) || isValidStringColumnName(testableName)
  }

  override protected def transformParameter(p: Parameter): Parameter = Parameter(p.name.toUpperCase, p.value)

  override protected val indexColumnNames: Seq[String] = Seq()

  override protected val boolColumnNames: Seq[String] = Seq("IS_POLYTOPE", "IS_REGULAR")

  override protected val intColumnNames: Seq[String] = Seq("ORBITS", "RANK", "SMALL_GROUP_ID", "SMALL_GROUP_ORDER")

  override protected val stringColumnNames: Seq[String] = Seq("SYMMETRY_TYPE")


  case class ManiplexNumeric(orbits: Int, rank: Int, smallGroupId: Int, smallGroupOrder: Int)

  case class ManiplexBoolean(isPolytope: Boolean, isRegular: Boolean)

  case class ManiplexString(symmetryType: String)

}
