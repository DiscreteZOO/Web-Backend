package xyz.discretezoo.web.db.ZooGraph

import slick.collection.heterogeneous.HNil
import slick.lifted.ProvenShape

import xyz.discretezoo.web.db.DynamicSupport
import xyz.discretezoo.web.db.ZooPostgresProfile.api._

final class GraphTable(tag: Tag) extends Table[Graph](tag, "ZOO_GRAPH") with DynamicSupport.ColumnSelector {

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
  
  val select: Map[String, Rep[_]] = Map(
    "zooid" -> this.zooid,
    "order" -> this.order,
    
    "index_cvt" -> this.indexCVT,
    "index_symcubic" -> this.indexSymCubic,
    "index_vt" -> this.indexVT,

    "is_arc_transitive" -> this.isArcTransitive,
    "is_bipartite" -> this.isBipartite,
    "is_cayley" -> this.isCayley,
    "is_distance_regular" -> this.isDistanceRegular,
    "is_distance_transitive" -> this.isDistanceTransitive,
    "is_edge_transitive" -> this.isEdgeTransitive,
    "is_eulerian" -> this.isEulerian,
    "is_hamiltonian" -> this.isHamiltonian,
    "is_moebius_ladder" -> this.isMoebiusLadder,
    "is_overfull" -> this.isOverfull,
    "is_partial_cube" -> this.isPartialCube,
    "is_prism" -> this.isPrism,
    "is_split" -> this.isSplit,
    "is_spx" -> this.isSPX,
    "is_strongly_regular" -> this.isStronglyRegular,

    "chromatic_index" -> this.chromaticIndex,
    "clique_number" -> this.cliqueNumber,
    "connected_components_number" -> this.connectedComponentsNumber,
    "diameter" -> this.diameter,
    "girth" -> this.girth,
    "odd_girth" -> this.oddGirth,
    "size" -> this.size,
    "triangles_count" -> this.trianglesCount
  )

  val inCollection: Map[String, Rep[Boolean]] = Map(
    "CVT" -> this.indexCVT.nonEmpty,
    "CAT" -> this.indexSymCubic.nonEmpty,
    "VT" -> this.indexVT.nonEmpty
  )

}