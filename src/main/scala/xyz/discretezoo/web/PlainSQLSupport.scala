package xyz.discretezoo.web

import slick.ast.Ordering.{Asc, Desc, Direction}
import slick.jdbc.GetResult
import xyz.discretezoo.web.ZooPostgresProfile.api._

trait PlainSQLSupport[T <: ZooObject] {

  val inCollection: Map[String, String]
  val tableName: String
  implicit val getResult: GetResult[T]

  def count(qp: SearchParam): DBIO[Int] = {
    sql"""SELECT COUNT(*) FROM "#$tableName"
                          WHERE #${plainGetWhere(qp)};""".as[Int].head
  }

  def get(qp: ResultParam): DBIO[Seq[T]] = {
    sql"""SELECT * FROM "#$tableName"
                   WHERE #${plainGetWhere(qp.parameters)}
                   #${plainSortBy(qp.sort)}
                   LIMIT #${qp.limit} OFFSET #${qp.offset};""".as[T]
  }

  private def plainSortBy(sortBy: Seq[(String, Direction)]): String = {
    sortBy.map(t => s"${t._1}${if (t._2 == Desc) " DESC" else ""}").mkString(", ")
  }

  private def plainFilter(condition: Condition): String = {
    condition match {
      case c: BooleanCondition => s"""(${if (!c.b) "not " else ""}"${c.field}")"""
      case c: NumericCondition => s"""("${c.field}" ${c.op} ${c.i})"""
    }
  }

  private def plainGetWhere(sp: SearchParam): String = {
    val collections = sp.collections.map(inCollection.get)
      .collect({ case Some(v) => s"""("$v" IS NOT NULL)""" }).mkString(" OR ")
    (sp.filter.map(plainFilter) :+ s"($collections)").mkString(" AND ")
  }

}
