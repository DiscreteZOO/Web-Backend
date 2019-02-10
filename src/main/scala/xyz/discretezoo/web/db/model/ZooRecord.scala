package xyz.discretezoo.web.db.model

import xyz.discretezoo.web.db.model.Graph.{GraphBoolean, GraphIndex, GraphNumeric}
import xyz.discretezoo.web.db.model.Maniplex.{ManiplexBoolean, ManiplexNumeric, ManiplexString}

sealed trait ZooRecord

case class GraphRecord(zooid: Int, index: GraphIndex, bool: GraphBoolean, numeric: GraphNumeric) extends ZooRecord
case class ManiplexRecord(numeric: ManiplexNumeric, bool: ManiplexBoolean, string: ManiplexString) extends ZooRecord
