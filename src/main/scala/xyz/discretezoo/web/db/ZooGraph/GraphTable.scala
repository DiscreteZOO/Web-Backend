package xyz.discretezoo.web.db.ZooGraph

import slick.collection.heterogeneous.HNil
import slick.lifted.ProvenShape
import xyz.discretezoo.web.db.ZooPostgresProfile.api._

final class GraphTable(tag: Tag) extends Table[Graph](tag, "ZOO_GRAPH") {

  def zooid: Rep[Int] = column[Int]("ZOOID", O.PrimaryKey)
  def order: Rep[Int] = column[Int]("ORDER")

  def indexCVT: Rep[Option[Int]] = column[Option[Int]]("INDEX_CVT")
  def indexSymCubic: Rep[Option[Int]] = column[Option[Int]]("INDEX_SYMCUBIC")
  def indexVT: Rep[Option[Int]] = column[Option[Int]]("INDEX_VT")

  def isArcTransitive: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_ARC_TRANSITIVE")
  def isBipartite: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_BIPARTITE")
  def isCayley: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_CAYLEY")
  def isDistanceRegular: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_DISTANCE_REGULAR")
  def isDistanceTransitive: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_DISTANCE_TRANSITIVE")
  def isEdgeTransitive: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_EDGE_TRANSITIVE")
  def isEulerian: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_EULERIAN")
  def isHamiltonian: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_HAMILTONIAN")
  def isMoebiusLadder: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_MOEBIUS_LADDER")
  def isOverfull: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_OVERFULL")
  def isPartialCube: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_PARTIAL_CUBE")
  def isPrism: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_PRISM")
  def isSplit: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_SPLIT")
  def isSPX: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_SPX")
  def isStronglyRegular: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_STRONGLY_REGULAR")

  def chromaticIndex: Rep[Option[Int]] = column[Option[Int]]("CHROMATIC_INDEX")
  def cliqueNumber: Rep[Option[Int]] = column[Option[Int]]("CLIQUE_NUMBER")
  def connectedComponentsNumber: Rep[Option[Int]] = column[Option[Int]]("CONNECTED_COMPONENTS_NUMBER")
  def diameter: Rep[Option[Int]] = column[Option[Int]]("DIAMETER")
  def girth: Rep[Option[Int]] = column[Option[Int]]("GIRTH")
  def oddGirth: Rep[Option[Int]] = column[Option[Int]]("ODD_GIRTH")
  def size: Rep[Option[Int]] = column[Option[Int]]("SIZE")
  def trianglesCount: Rep[Option[Int]] = column[Option[Int]]("TRIANGLES_COUNT")

  def * : ProvenShape[Graph] = (
    zooid :: order ::
      // indexes
      indexCVT :: indexSymCubic :: indexVT ::
      // booleans
      isArcTransitive :: isBipartite :: isCayley :: isDistanceRegular :: isDistanceTransitive :: isEdgeTransitive ::
      isEulerian :: isHamiltonian :: isMoebiusLadder:: isOverfull :: isPartialCube :: isPrism :: isSplit ::
      isSPX :: isStronglyRegular ::
      // numeric
      chromaticIndex :: cliqueNumber :: connectedComponentsNumber :: diameter :: girth :: oddGirth :: size ::
      trianglesCount ::
      HNil
    ).mapTo[Graph]

}