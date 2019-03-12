package xyz.discretezoo.web.db.ZooManiplex

import java.util.UUID
import slick.jdbc.GetResult
import xyz.discretezoo.web.PlainSQLSupport

//TODO: deal with UUIDs implicitly: https://github.com/slick/slick/issues/161

object ManiplexPlainQuery extends PlainSQLSupport[Maniplex] {

  override val tableName: String = "ZOO_MANIPLEX"
  override implicit val getResult: GetResult[Maniplex] = GetResult(r => Maniplex(r.nextObject.asInstanceOf[UUID], r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))

  val inCollection: Map[String, String] = Map(
    "M2" -> "UUID"
  )

}
