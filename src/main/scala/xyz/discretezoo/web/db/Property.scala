package xyz.discretezoo.web.db

case class Property(name: String, kind: String) {
  def displayName: String = name.replace('_', ' ')
}
