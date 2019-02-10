package xyz.discretezoo.web.db.ZooGraph

import slick.lifted.Rep
import xyz.discretezoo.web.db.{Filter, BooleanCondition, NumericCondition}

object GraphFilters extends Filter[GraphTable] {

  override def boolean(g: GraphTable, condition: BooleanCondition): Rep[Boolean] = {
    val value: Rep[Option[Boolean]] = condition.field match {
      case "is_arc_transitive" => g.isArcTransitive
      case "is_bipartite" => g.isBipartite
      case "is_cayley" => g.isCayley
      case "is_distance_regular" => g.isDistanceRegular
      case "is_distance_transitive" => g.isDistanceTransitive
      case "is_edge_transitive" => g.isEdgeTransitive
      case "is_eulerian" => g.isEulerian
      case "is_forest" => g.isForest
      case "is_hamiltonian" => g.isHamiltonian
      case "is_overfull" => g.isOverfull
      case "is_partial_cube" => g.isPartialCube
      case "is_split" => g.isSplit
      case "is_strongly_regular" => g.isStronglyRegular
    }
    booleanNullableFilter(value, condition.b)
  }

  override def numeric(g: GraphTable, condition: NumericCondition): Rep[Boolean] = {
    if (GraphTable.numericNotNullable.contains(condition.field)) {
      val value: Rep[Int] = condition.field match {
        case "zooid" => g.zooid
        case "order" => g.order
      }
      numericNotNullableFilter(value, condition.op, condition.i)
    }
    else {
      val value: Rep[Option[Int]] = condition.field match {
        case "chromatic_index" => g.chromaticIndex
        case "clique_number" => g.cliqueNumber
        case "connected_components_number" => g.connectedComponentsNumber
        case "diameter" => g.diameter
        case "girth" => g.girth
        case "odd_girth" => g.oddGirth
        case "size" => g.size
        case "triangles_count" => g.trianglesCount
      }
      numericNullableFilter(value, condition.op, condition.i)
    }
  }
}
