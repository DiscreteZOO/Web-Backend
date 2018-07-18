package xyz.discretezoo.web.db.v1

import java.util.UUID

import xyz.discretezoo.web.db.ZooPostgresProfile.api._

case class GraphTypes(
                       isBipartite: Boolean,
//                       isCartesianProduct: Option[Boolean],
//                       isChordal: Option[Boolean],
//                       isCirculant: Option[Boolean],
//                       isCircularPlanar: Option[Boolean],
                       isDistanceRegular: Boolean,
                       isDistanceTransitive: Boolean,
                       isEulerian: Boolean,
                       isForest: Boolean,
//                       isGallaiTree: Option[Boolean],
                       isHamiltonian: Option[Boolean],
//                       isInterval: Option[Boolean],
//                       isLineGraph: Option[Boolean],
                       isPartialCube: Boolean,
//                       isPerfect: Option[Boolean],
//                       isPlanar: Option[Boolean],
//                       isPrime: Option[Boolean],
//                       isRegular: Boolean,
                       isSplit: Boolean,
                       isStronglyRegular: Boolean,
                       isTree: Option[Boolean]
                     )

case class OtherProperties(
//                            averageDistance: Option[Float],
                            chromaticIndex: Option[Int],
//                            chromaticNumber: Option[Int],
//                            clusterTransitivity: Option[Float],
//                            clusteringAverage: Option[Float],
//                            fractionalChromaticIndex: Option[Int],
//                            isAsteroidalTripleFree: Option[Boolean],
//                            isEvenHoleFree: Option[Boolean],
//                            isLongAntiholeFree: Option[Boolean],
//                            isLongHoleFree: Option[Boolean],
//                            isOddHoleFree: Option[Boolean],
                            isOverfull: Boolean,
//                            lovaszTheta: Option[Float],
//                            maximumAverageDegree: Option[Float],
//                            spanningTreesCount: Option[Int],
//                            szegedIndex: Option[Int],
//                            treewidth: Option[Int],
//                            wienerIndex: Option[Int],
//                            zagreb1Index: Option[Int],
//                            zagreb2Index: Option[Int]
                          )

case class SymmetryProperties(
                               isArcTransitive: Boolean,
                               isCayley: Boolean,
                               isEdgeTransitive: Boolean
//                               isVertexTransitive: Boolean
                             )

case class Graph(
                  zooid: Int,
                  data: String,
                  name: Option[String],
                  order: Int,
                  averageDegree: Float,
                  cliqueNumber: Int,
                  connectedComponentsNumber: Int,
                  diameter: Option[Int],
//                  edgeConnectivity: Option[Int],
//                  genus: Option[Int],
                  girth: Option[Int],
                  hasMultipleEdges: Boolean,
                  numberOfLoops: Int,
                  oddGirth: Option[Int],
//                  radius: Option[Int],
                  size: Int,
                  trianglesCount: Int,
//                  vertexConnectivity: Option[Int],
                  graphTypes: GraphTypes,
                  symmetryProperties: SymmetryProperties,
                  otherProperties: OtherProperties
                )

class Graphs(tag: Tag) extends Table[Graph](tag, "graph") {

  def zooid: Rep[Int] = column[Int]("zooid", O.PrimaryKey)
  def data = column[String]("data")

  def averageDegree = column[Float]("average_degree")
//  def averageDistance = column[Option[Float]]("average_distance")
  def name = column[Option[String]]("name")
  def order = column[Int]("order")
  def chromaticIndex = column[Option[Int]]("chromatic_index")
//  def chromaticNumber = column[Option[Int]]("chromatic_number")
  def cliqueNumber = column[Int]("clique_number")
//  def clusterTransitivity = column[Option[Float]]("cluster_transitivity")
//  def clusteringAverage = column[Option[Float]]("clustering_average")
  def connectedComponentsNumber = column[Int]("connected_components_number")
  def diameter = column[Option[Int]]("diameter")
//  def edgeConnectivity = column[Option[Int]]("edge_connectivity")
//  def fractionalChromaticIndex = column[Option[Int]]("fractional_chromatic_index")
//  def genus = column[Option[Int]]("genus")
  def girth = column[Option[Int]]("girth")
  def hasMultipleEdges = column[Boolean]("has_multiple_edges")
  def isArcTransitive = column[Boolean]("is_arc_transitive")
//  def isAsteroidalTripleFree = column[Option[Boolean]]("is_asteroidal_triple_free")
  def isBipartite = column[Boolean]("is_bipartite")
//  def isCartesianProduct = column[Option[Boolean]]("is_cartesian_product")
  def isCayley = column[Boolean]("is_cayley")
//  def isChordal = column[Option[Boolean]]("is_chordal")
//  def isCirculant = column[Option[Boolean]]("is_circulant")
//  def isCircularPlanar = column[Option[Boolean]]("is_circular_planar")
  def isDistanceRegular = column[Boolean]("is_distance_regular")
  def isDistanceTransitive = column[Boolean]("is_distance_transitive")
  def isEdgeTransitive = column[Boolean]("is_edge_transitive")
  def isEulerian = column[Boolean]("is_eulerian")
//  def isEvenHoleFree = column[Option[Boolean]]("is_even_hole_free")
  def isForest = column[Boolean]("is_forest")
//  def isGallaiTree = column[Option[Boolean]]("is_gallai_tree")
  def isHamiltonian = column[Option[Boolean]]("is_hamiltonian")
//  def isInterval = column[Option[Boolean]]("is_interval")
//  def isLineGraph = column[Option[Boolean]]("is_line_graph")
//  def isLongAntiholeFree = column[Option[Boolean]]("is_long_antihole_free")
//  def isLongHoleFree = column[Option[Boolean]]("is_long_hole_free")
//  def isOddHoleFree = column[Option[Boolean]]("is_odd_hole_free")
  def isOverfull = column[Boolean]("is_overfull")
  def isPartialCube = column[Boolean]("is_partial_cube")
//  def isPerfect = column[Option[Boolean]]("is_perfect")
//  def isPlanar = column[Option[Boolean]]("is_planar")
//  def isPrime = column[Option[Boolean]]("is_prime")
  def isRegular = column[Boolean]("is_regular")
  def isSplit = column[Boolean]("is_split")
  def isStronglyRegular = column[Boolean]("is_strongly_regular")
  def isTree = column[Option[Boolean]]("is_tree")
  def isVertexTransitive = column[Boolean]("is_vertex_transitive")
//  def lovaszTheta = column[Option[Float]]("lovasz_theta")
//  def maximumAverageDegree = column[Option[Float]]("maximum_average_degree")
  def numberOfLoops = column[Int]("number_of_loops")
  def oddGirth = column[Option[Int]]("odd_girth")
//  def radius = column[Option[Int]]("radius")
  def size = column[Int]("size")
//  def spanningTreesCount = column[Option[Int]]("spanning_trees_count")
//  def szegedIndex = column[Option[Int]]("szeged_index")
//  def treewidth = column[Option[Int]]("treewidth")
  def trianglesCount = column[Int]("triangles_count")
//  def vertexConnectivity = column[Option[Int]]("vertex_connectivity")
//  def wienerIndex = column[Option[Int]]("wiener_index")
//  def zagreb1Index = column[Option[Int]]("zagreb1_index")
//  def zagreb2Index = column[Option[Int]]("zagreb1_index")

  def graphTypesProjection = (
    isBipartite,
//    isCartesianProduct,
//    isChordal,
//    isCirculant,
//    isCircularPlanar,
    isDistanceRegular,
    isDistanceTransitive,
    isEulerian,
    isForest,
//    isGallaiTree,
    isHamiltonian,
//    isInterval,
//    isLineGraph,
    isPartialCube,
//    isPerfect,
//    isPlanar,
//    isPrime,
//    isRegular,
    isSplit,
    isStronglyRegular,
    isTree
  ) <> ((GraphTypes.apply _).tupled, GraphTypes.unapply)

  def otherPropertiesProjection = (
//    averageDistance,
    chromaticIndex,
//    chromaticNumber,
//    clusterTransitivity,
//    clusteringAverage,
//    fractionalChromaticIndex,
//    isAsteroidalTripleFree,
//    isEvenHoleFree,
//    isLongAntiholeFree,
//    isLongHoleFree,
//    isOddHoleFree,
    isOverfull,
//    lovaszTheta,
//    maximumAverageDegree,
//    spanningTreesCount,
//    szegedIndex,
//    treewidth,
//    wienerIndex,
//    zagreb1Index,
//    zagreb2Index
  ) <> ((OtherProperties.apply _).tupled, OtherProperties.unapply)

  def symmetryPropertiesProjection = (
    isArcTransitive,
    isCayley,
    isEdgeTransitive
//    isVertexTransitive
  ) <> ((SymmetryProperties.apply _).tupled, SymmetryProperties.unapply)

  def * = (
    zooid,
    data,
    name,
    order,
    averageDegree,
    cliqueNumber,
    connectedComponentsNumber,
    diameter,
//    edgeConnectivity,
//    genus,
    girth,
    hasMultipleEdges,
    numberOfLoops,
    oddGirth,
//    radius,
    size,
    trianglesCount,
//    vertexConnectivity,
    graphTypesProjection,
    symmetryPropertiesProjection,
    otherPropertiesProjection
  ) <> ((Graph.apply _).tupled, Graph.unapply)

}