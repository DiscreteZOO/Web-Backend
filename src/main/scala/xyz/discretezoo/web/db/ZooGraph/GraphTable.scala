package xyz.discretezoo.web.db.ZooGraph

import slick.collection.heterogeneous.HNil
import slick.lifted.ProvenShape
import xyz.discretezoo.web.db.ZooTable
import xyz.discretezoo.web.db.ZooPostgresProfile.api._

sealed trait GraphTable extends ZooTable {
  def zooid: Rep[Int]
}


final class GraphTableMain(tag: Tag) extends Table[GraphMain](tag, "ZOO_GRAPH") with GraphTable {

  override def zooid: Rep[Int] = column[Int]("ZOOID", O.PrimaryKey)
  def order: Rep[Int] = column[Int]("ORDER")

  def isArcTransitive: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_ARC_TRANSITIVE")
  def isBipartite: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_BIPARTITE")
  def isCayley: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_CAYLEY")
  def isDistanceRegular: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_DISTANCE_REGULAR")
  def isDistanceTransitive: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_DISTANCE_TRANSITIVE")
  def isEdgeTransitive: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_EDGE_TRANSITIVE")
  def isEulerian: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_EULERIAN")
  def isForest: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_FOREST")
  def isHamiltonian: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_HAMILTONIAN")
  def isOverfull: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_OVERFULL")
  def isPartialCube: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_PARTIAL_CUBE")
  def isSplit: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_SPLIT")
  def isStronglyRegular: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_STRONGLY_REGULAR")

  def chromaticIndex: Rep[Option[Int]] = column[Option[Int]]("CHROMATIC_INDEX")
  def cliqueNumber: Rep[Option[Int]] = column[Option[Int]]("CLIQUE_NUMBER")
  def connectedComponentsNumber: Rep[Option[Int]] = column[Option[Int]]("CONNECTED_COMPONENTS_NUMBER")
  def diameter: Rep[Option[Int]] = column[Option[Int]]("DIAMETER")
  def girth: Rep[Option[Int]] = column[Option[Int]]("GIRTH")
  def oddGirth: Rep[Option[Int]] = column[Option[Int]]("ODD_GIRTH")
  def size: Rep[Option[Int]] = column[Option[Int]]("SIZE")
  def trianglesCount: Rep[Option[Int]] = column[Option[Int]]("TRIANGLES_COUNT")

  def * : ProvenShape[GraphMain] = (
    zooid :: order ::
      // booleans
      isArcTransitive :: isBipartite :: isCayley :: isDistanceRegular :: isDistanceTransitive :: isEdgeTransitive ::
      isEulerian :: isForest :: isHamiltonian :: isOverfull :: isPartialCube :: isSplit :: isStronglyRegular ::
      // numeric
      chromaticIndex :: cliqueNumber :: connectedComponentsNumber :: diameter :: girth :: oddGirth :: size ::
      trianglesCount ::
      HNil
    ).mapTo[GraphMain]

}


final class GraphTableCVT(tag: Tag) extends Table[GraphCVT](tag, "ZOO_GRAPH_CVT") with GraphTable {

  override def zooid: Rep[Int] = column[Int]("ZOOID", O.PrimaryKey)

  def indexCVT: Rep[Option[Int]] = column[Option[Int]]("INDEX_CVT")
  def indexSymCubic: Rep[Option[Int]] = column[Option[Int]]("INDEX_SYMCUBIC")

  def isMoebiusLadder: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_MOEBIUS_LADDER")
  def isPrism: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_PRISM")
  def isSPX: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_SPX")

  def * : ProvenShape[GraphCVT] = (
    zooid ::
      // index
      indexCVT :: indexSymCubic ::
      // booleans
      isMoebiusLadder :: isPrism :: isSPX ::
      HNil
    ).mapTo[GraphCVT]

}