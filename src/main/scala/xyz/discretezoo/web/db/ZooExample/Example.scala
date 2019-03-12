package xyz.discretezoo.web.db.ZooExample

import java.util.UUID
import xyz.discretezoo.web.ZooObject

case class Example(
                    ID: UUID,
                    mat: List[List[Int]],
                    trace: Int,
                    orthogonal: Boolean,
                    eigenvalues: List[Int],
                    characteristic: List[List[Int]]) extends ZooObject {

  def select: Map[String, _] = Map(
    "ID" -> this.ID,
    "mat" -> this.mat,
    "trace" -> this.trace,
    "orthogonal" -> this.orthogonal,
    "eigenvalues" -> this.eigenvalues,
    "characteristic" -> this.characteristic
  )

}