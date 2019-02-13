package xyz.discretezoo.web.db.ZooGraph

import slick.lifted.Rep
import xyz.discretezoo.web.db.ColumnActions
import xyz.discretezoo.web.db.ZooPostgresProfile.api._

object GraphColumns extends ColumnActions[GraphTable] {

  override protected def isInCollection(g: GraphTable, collection: String): Rep[Boolean] = {
    val value: Option[Rep[Boolean]] = collection match {
      case "CVT" => Some(g.indexCVT.nonEmpty)
      case "CAT" => Some(g.indexSymCubic.nonEmpty)
      case "VT" => Some(g.indexVT.nonEmpty)
      case _ => None
    }
    value.getOrElse(false)
  }

  override protected def getBooleanColumn(g: GraphTable, column: String): Option[Rep[_ >: Boolean with Option[Boolean]]] = column match {
    case "is_arc_transitive" => Some(g.isArcTransitive)
    case "is_bipartite" => Some(g.isBipartite)
    case "is_cayley" => Some(g.isCayley)
    case "is_distance_regular" => Some(g.isDistanceRegular)
    case "is_distance_transitive" => Some(g.isDistanceTransitive)
    case "is_edge_transitive" => Some(g.isEdgeTransitive)
    case "is_eulerian" => Some(g.isEulerian)
    case "is_hamiltonian" => Some(g.isHamiltonian)
    case "is_moebius_ladder" => Some(g.isMoebiusLadder)
    case "is_overfull" => Some(g.isOverfull)
    case "is_partial_cube" => Some(g.isPartialCube)
    case "is_prism" => Some(g.isPrism)
    case "is_split" => Some(g.isSplit)
    case "is_spx" => Some(g.isSPX)
    case "is_strongly_regular" => Some(g.isStronglyRegular)
    case _ => None
  }

  override protected def getIntColumn(g: GraphTable, column: String): Option[Rep[_ >: Int with Option[Int]]] = column match {
    case "zooid" => Some(g.zooid)
    case "order" => Some(g.order)
    case "chromatic_index" => Some(g.chromaticIndex)
    case "clique_number" => Some(g.cliqueNumber)
    case "connected_components_number" => Some(g.connectedComponentsNumber)
    case "diameter" => Some(g.diameter)
    case "girth" => Some(g.girth)
    case "odd_girth" => Some(g.oddGirth)
    case "size" => Some(g.size)
    case "triangles_count" => Some(g.trianglesCount)
    // index
    case "CVT" => Some(g.indexCVT)
    case "CAT" => Some(g.indexSymCubic)
    case "VT" => Some(g.indexVT)
    case _ => None
  }

}
