package xyz.discretezoo.web

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import xyz.discretezoo.web.db.ZooGraph.Graph
import xyz.discretezoo.web.db.ZooManiplex.Maniplex

// collect your json format instances into a support trait:
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol with NullOptions {

  implicit val formatCount: RootJsonFormat[Count] = jsonFormat1(Count)

  implicit val formatParameter: RootJsonFormat[Parameter] = jsonFormat2(Parameter)
  implicit val formatSearchParameters: RootJsonFormat[SearchParameters] = jsonFormat3(SearchParameters)
  implicit val formatResultsParameters: RootJsonFormat[ResultsParameters] = jsonFormat4(ResultsParameters)

  implicit def formatSearchResults[T: JsonFormat]: RootJsonFormat[SearchResult[T]] =
    rootFormat(lazyFormat(jsonFormat2(SearchResult.apply[T])))

//  https://stackoverflow.com/questions/53713834/parsing-more-than-22-fields-with-spray-json-without-nesting-case-classes

  implicit object ManiplexJsonWriter extends JsonWriter[Maniplex]  {

    def write(m: Maniplex): JsValue = JsObject(
      List(
        Some("uuid" -> m.UUID.toString.toJson),
        Some("is_polytope" -> m.isPolytope.toJson),
        Some("is_regular" -> m.isRegular.toJson),
        Some("orbits" -> m.orbits.toJson),
        Some("rank" -> m.rank.toJson),
        Some("small_group_id" -> m.smallGroupId.toJson),
        Some("small_group_order" -> m.smallGroupOrder.toJson)
      ).flatten: _*
    )

  }

  implicit object GraphJsonWriter extends JsonWriter[Graph]  {

      def write(g: Graph): JsValue = JsObject(
        List(
          Some("zooid" -> g.zooid.toJson),
          Some("order" -> g.order.toJson),

          Some("index_cvt" -> g.indexCVT.toJson),
          Some("index_symcubic" -> g.indexSymCubic.toJson),
          Some("index_vt" -> g.indexVT.toJson),

          Some("is_arc_transitive" -> g.isArcTransitive.toJson),
          Some("is_bipartite" -> g.isBipartite.toJson),
          Some("is_cayley" -> g.isCayley.toJson),
          Some("is_distance_regular" -> g.isDistanceRegular.toJson),
          Some("is_distance_transitive" -> g.isDistanceTransitive.toJson),
          Some("is_edge_transitive" -> g.isEdgeTransitive.toJson),
          Some("is_eulerian" -> g.isEulerian.toJson),
          Some("is_hamiltonian" -> g.isHamiltonian.toJson),
          Some("is_moebius_ladder" -> g.isMoebiusLadder.toJson),
          Some("is_overfull" -> g.isOverfull.toJson),
          Some("is_partial_cube" -> g.isPartialCube.toJson),
          Some("is_prism" -> g.isPrism.toJson),
          Some("is_split" -> g.isSplit.toJson),
          Some("is_spx" -> g.isSPX.toJson),
          Some("is_strongly_regular" -> g.isStronglyRegular.toJson),

          Some("chromatic_index" -> g.chromaticIndex.toJson),
          Some("clique_number" -> g.cliqueNumber.toJson),
          Some("connected_components_number" -> g.connectedComponentsNumber.toJson),
          Some("diameter" -> g.diameter.toJson),
          Some("girth" -> g.girth.toJson),
          Some("odd_girth" -> g.oddGirth.toJson),
          Some("size" -> g.size.toJson),
          Some("triangles_count" -> g.trianglesCount.toJson)
        ).flatten: _*
      )

  }

}