package xyz.discretezoo.web.db.v1

import xyz.discretezoo.web.db.ZooPostgresProfile.api._

case class GraphVT(zooid: Int, vtIndex: Option[Int])

class GraphsVT(tag: Tag) extends Table[GraphVT](tag, "graph_cvt") {

  def zooid: Rep[Int] = column[Int]("zooid", O.PrimaryKey)
  def vtIndex = column[Option[Int]]("vt_index")

  def * = (
    zooid,
    vtIndex
  ) <> ((GraphVT.apply _).tupled, GraphVT.unapply)

}