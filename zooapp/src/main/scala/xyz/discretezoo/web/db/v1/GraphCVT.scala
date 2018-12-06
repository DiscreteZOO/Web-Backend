package xyz.discretezoo.web.db.v1

import xyz.discretezoo.web.db.ZooPostgresProfile.api._

case class GraphCVT(
                     zooid: Int,
                     cvtIndex: Option[Int],
                     symcubicIndex: Option[Int],
                     isMoebiusLadder: Boolean,
                     isPrism: Boolean,
                     isSPX: Boolean,
                     truncation: Option[Int] // foreign key for table graph
)

class GraphsCVT(tag: Tag) extends Table[GraphCVT](tag, "graph_cvt") {

  def zooid: Rep[Int] = column[Int]("zooid", O.PrimaryKey)
  def cvtIndex = column[Option[Int]]("cvt_index")
  def symcubicIndex = column[Option[Int]]("symcubic_index")
  def isMoebiusLadder = column[Boolean]("is_moebius_ladder")
  def isPrism = column[Boolean]("is_prism")
  def isSPX = column[Boolean]("is_spx")
  def truncation = column[Option[Int]]("truncation")

  def * = (
    zooid,
    cvtIndex,
    symcubicIndex,
    isMoebiusLadder,
    isPrism,
    isSPX,
    truncation
  ) <> ((GraphCVT.apply _).tupled, GraphCVT.unapply)

}