package xyz.discretezoo.web

import java.util.UUID

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{JsonFormat, _}
import xyz.discretezoo.web.ZooJsonAPI._
import xyz.discretezoo.web.db.ZooExample.Example
import xyz.discretezoo.web.db.ZooGraph.Graph
import xyz.discretezoo.web.db.ZooManiplex.Maniplex

// collect your json format instances into a support trait:
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol with NullOptions {

  implicit val formatCount: RootJsonFormat[Count] = jsonFormat1(Count)
  implicit val formatParameter: RootJsonFormat[Parameter] = jsonFormat2(Parameter)
  implicit val formatSearchParameters: RootJsonFormat[SearchParameters] = jsonFormat3(SearchParameters)
  implicit val formatResultsParameters: RootJsonFormat[ResultsParameters] = jsonFormat4(ResultsParameters)
//  implicit def formatSearchResults: RootJsonFormat[SearchResult] = jsonFormat2(SearchResult)

  implicit object formatUUID extends JsonFormat[UUID] {
    def write(u: UUID): JsValue = u.toString.toJson
    def read(json: JsValue) =
      throw new UnsupportedOperationException("Missing implementation for the UUID JsonReader")
  }
//
//  implicit object formatZooObject extends RootJsonFormat[ZooObject] {
//    override def write(o: ZooObject): JsValue = {
//      val columnList = o.select.collect({
//        case (s, u: UUID) => Some(s -> u.toJson)
//        case (s, v: Boolean) => Some(s -> v.toJson)
//        case (s, v: Int) => Some(s -> v.toJson)
//        case (s, Some(v: Boolean)) => Some(s -> v.toJson)
//        case (s, Some(v: Int)) => Some(s -> v.toJson)
//        case (s, None) => Some(s -> JsNull)
//      }).toList
//      JsObject(columnList.flatten: _*)
//    }
//    override def read(json: JsValue): ZooObject =
//      throw new UnsupportedOperationException("Missing implementation for the ZooObject JsonReader")
//  }

  implicit object formatZooObject extends RootJsonFormat[ZooObject] {
    override def write(o: ZooObject): JsValue = o match {
      case o: Graph => JsObject(
        List(
          Some("zooid" -> o.zooid.toJson),
          Some("order" -> o.order.toJson),
          Some("index_CVT" -> o.indexCVT.toJson),
          Some("index_symcubic" -> o.indexSymcubic.toJson),
          Some("index_VT" -> o.indexVT.toJson),
          Some("is_arc_transitive" -> o.isArcTransitive.toJson),
          Some("is_bipartite" -> o.isBipartite.toJson),
          Some("is_cayley" -> o.isCayley.toJson),
          Some("is_distance_regular" -> o.isDistanceRegular.toJson),
          Some("is_distance_transitive" -> o.isDistanceTransitive.toJson),
          Some("is_edge_transitive" -> o.isEdgeTransitive.toJson),
          Some("is_eulerian" -> o.isEulerian.toJson),
          Some("is_hamiltonian" -> o.isHamiltonian.toJson),
          Some("is_moebius_ladder" -> o.isMoebiusLadder.toJson),
          Some("is_overfull" -> o.isOverfull.toJson),
          Some("is_partial_cube" -> o.isPartialCube.toJson),
          Some("is_prism" -> o.isPrism.toJson),
          Some("is_split" -> o.isSplit.toJson),
          Some("is_SPX" -> o.isSPX.toJson),
          Some("is_strongly_regular" -> o.isStronglyRegular.toJson),
          Some("chromatic_index" -> o.chromaticIndex.toJson),
          Some("clique_number" -> o.cliqueNumber.toJson),
          Some("connected_components_number" -> o.connectedComponentsNumber.toJson),
          Some("diameter" -> o.diameter.toJson),
          Some("girth" -> o.girth.toJson),
          Some("odd_girth" -> o.oddGirth.toJson),
          Some("size" -> o.size.toJson),
          Some("triangles_count" -> o.trianglesCount.toJson)
        ).flatten: _*
      )
      case o: Maniplex => JsObject(
        List(
          Some("UUID" -> o.UUID.toJson),
          Some("is_polytope" -> o.isPolytope.toJson),
          Some("is_regular" -> o.isRegular.toJson),
          Some("orbits" -> o.orbits.toJson),
          Some("rank" -> o.rank.toJson),
          Some("small_group_id" -> o.smallGroupId.toJson),
          Some("small_group_order" -> o.smallGroupOrder.toJson)
        ).flatten: _*
      )
      case o: Example => JsObject(
        List(
          Some("ID" -> o.ID.toJson),
          Some("mat" -> o.mat.toJson),
          Some("trace" -> o.trace.toJson),
          Some("orthogonal" -> o.orthogonal.toJson),
          Some("eigenvalues" -> o.eigenvalues.toJson),
          Some("characteristic" -> o.characteristic.toJson)
        ).flatten: _*
      )
    }

    override def read(json: JsValue): ZooObject =
      throw new UnsupportedOperationException("Missing implementation for the ZooObject JsonReader")
  }

  implicit object formatSearchResults extends RootJsonFormat[SearchResult] {
    override def write(o: SearchResult): JsValue = {
      JsObject("pages" -> o.pages.toJson, "data" -> o.data.toJson)
    }
    override def read(json: JsValue): SearchResult =
      throw new UnsupportedOperationException("Missing implementation for the ZooObject JsonReader")
  }

//  https://stackoverflow.com/questions/53713834/parsing-more-than-22-fields-with-spray-json-without-nesting-case-classes

}