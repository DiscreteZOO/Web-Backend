package xyz.discretezoo.web.db.ZooExample

import java.util.UUID
import slick.jdbc.GetResult
import xyz.discretezoo.web.PlainSQLSupport

object ExamplePlainQuery extends PlainSQLSupport[Example] {

  override val tableName: String = "ZOO_EXAMPLE"
  override implicit val getResult: GetResult[Example] = GetResult(r => Example(
    r.nextObject.asInstanceOf[UUID],
    r.nextObject.asInstanceOf[List[List[Int]]],
    r.<<,
    r.<<,
    r.nextObject.asInstanceOf[List[Int]],
    r.nextObject.asInstanceOf[List[List[Int]]]))

  val inCollection: Map[String, String] = Map(
    "Ex" -> "ID"
  )

}