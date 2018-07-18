package xyz.discretezoo.web.db.v1

import xyz.discretezoo.web.db.ZooPostgresProfile.api._

case class GraphSPX(zooid: Int, rSPX: Int, sSPX: Int)

class GraphsSPX(tag: Tag) extends Table[GraphSPX](tag, "graph_cvt") {

  def zooid: Rep[Int] = column[Int]("zooid", O.PrimaryKey)
  def rSPX = column[Int]("spx_r")
  def sSPX = column[Int]("spx_s")

  def * = (
    zooid,
    rSPX,
    sSPX
  ) <> ((GraphSPX.apply _).tupled, GraphSPX.unapply)

}
