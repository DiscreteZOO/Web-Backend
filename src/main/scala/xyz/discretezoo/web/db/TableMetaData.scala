package xyz.discretezoo.web.db

trait TableMetaData {
  val booleanNotNullable: Seq[String]
  val numericNotNullable: Seq[String]
}
