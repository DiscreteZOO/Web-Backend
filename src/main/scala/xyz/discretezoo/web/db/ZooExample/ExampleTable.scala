package xyz.discretezoo.web.db.ZooExample

import java.util.UUID
import slick.collection.heterogeneous.HNil
import slick.lifted.{ProvenShape, Rep}
import xyz.discretezoo.web.DynamicSupport.ColumnSelector
import xyz.discretezoo.web.ZooPostgresProfile.api._

final class ExampleTable(tag: Tag) extends Table[Example](tag, "ZOO_EXAMPLE") with ColumnSelector {

  def ID: Rep[UUID] = column[UUID]("ID", O.PrimaryKey)
  def mat: Rep[List[List[Int]]] = column[List[List[Int]]]("MAT")
  def trace: Rep[Int] = column[Int]("TRACE")
  def orthogonal: Rep[Boolean] = column[Boolean]("ORTHOGONAL")
  def eigenvalues: Rep[List[Int]] = column[List[Int]]("EIGENVALUES")
  def characteristic: Rep[List[List[Int]]] = column[List[List[Int]]]("CHARACTERISTIC")

  def * : ProvenShape[Example] = (
    ID ::
      mat ::
      trace ::
      orthogonal ::
      eigenvalues ::
      characteristic ::
      HNil
    ).mapTo[Example]

  val select: Map[String, Rep[_]] = Map(
    "ID" -> this.ID,
    "mat" -> this.mat,
    "trace" -> this.trace,
    "orthogonal" -> this.orthogonal,
    "eigenvalues" -> this.eigenvalues,
    "characteristic" -> this.characteristic
  )

  val inCollection: Map[String, Rep[Boolean]] = Map(
    "Ex" -> true
  )

}
