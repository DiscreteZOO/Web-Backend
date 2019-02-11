package xyz.discretezoo.web.db.ZooGraph

import slick.lifted.Rep
import xyz.discretezoo.web.db.{BooleanCondition, Filter, NumericCondition}

object GraphFilters extends Filter[GraphTable] {

  private val notNullable: Seq[String] = Seq("zooid", "order")

  override def boolean(g: GraphTable, condition: BooleanCondition): Rep[Boolean] = {
    val value: Rep[Option[Boolean]] = g match {
      case graph: GraphTableMain => condition.field match {
        case "is_arc_transitive" => graph.isArcTransitive
        case "is_bipartite" => graph.isBipartite
        case "is_cayley" => graph.isCayley
        case "is_distance_regular" => graph.isDistanceRegular
        case "is_distance_transitive" => graph.isDistanceTransitive
        case "is_edge_transitive" => graph.isEdgeTransitive
        case "is_eulerian" => graph.isEulerian
        case "is_forest" => graph.isForest
        case "is_hamiltonian" => graph.isHamiltonian
        case "is_overfull" => graph.isOverfull
        case "is_partial_cube" => graph.isPartialCube
        case "is_split" => graph.isSplit
        case "is_strongly_regular" => graph.isStronglyRegular
      }
      case graphCVT: GraphTableCVT => condition.field match {
        case "is_moebius_ladder" => graphCVT.isMoebiusLadder
        case "is_prism" => graphCVT.isPrism
        case "is_spx" => graphCVT.isSPX
      }
    } 
      

    booleanNullableFilter(value, condition.b)
  }

  override def numeric(g: GraphTable, condition: NumericCondition): Rep[Boolean] = {
    if (notNullable.contains(condition.field)) {
      val value: Rep[Int] = g match {
        case graph: GraphTableMain => condition.field match {
          case "zooid" => graph.zooid
          case "order" => graph.order
        }
        case graphCVT: GraphTableCVT => condition.field match {
          case "zooid" => graphCVT.zooid
        }
      }
      numericNotNullableFilter(value, condition.op, condition.i)
    }
    else {
      val value: Rep[Option[Int]] = g match {
        case graph: GraphTableMain => condition.field match {
          case "chromatic_index" => graph.chromaticIndex
          case "clique_number" => graph.cliqueNumber
          case "connected_components_number" => graph.connectedComponentsNumber
          case "diameter" => graph.diameter
          case "girth" => graph.girth
          case "odd_girth" => graph.oddGirth
          case "size" => graph.size
          case "triangles_count" => graph.trianglesCount
        }
      }
      numericNullableFilter(value, condition.op, condition.i)
    }
  }
}
