package xyz.discretezoo.web

import slick.lifted.TableQuery
import xyz.discretezoo.web.db.ZooDb

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import xyz.discretezoo.web.ZooPostgresProfile.api._
import xyz.discretezoo.web.db.ZooExample.ExampleTable

object DbApp {

  def main(args: Array[String]): Unit = {

    object test extends TableQuery(new ExampleTable(_))

    try {
      Await.result(ZooDb.db.run(DBIO.seq(test.schema.create)), Duration.Inf)
    }

  }

}
