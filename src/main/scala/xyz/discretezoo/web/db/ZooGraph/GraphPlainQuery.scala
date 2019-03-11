package xyz.discretezoo.web.db.ZooGraph

import slick.jdbc.GetResult
import xyz.discretezoo.web.PlainSQLSupport

object GraphPlainQuery extends PlainSQLSupport[Graph] {

  override val tableName: String = "ZOO_GRAPH"
  override implicit val getResult: GetResult[Graph] = GetResult(r => Graph(
    r.<<, r.<<,
    r.<<, r.<<, r.<<,
    r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<,
    r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))

  val inCollection: Map[String, String] = Map(
    "CVT" -> "INDEX_CVT",
    "CAT" -> "INDEX_SYMCUBIC",
    "VT" -> "INDEX_VT"
  )

}
