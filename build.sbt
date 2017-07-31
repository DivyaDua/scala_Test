lazy val common = Seq {
  name := "ScalaFinal"
  version := "1.0"
  scalaVersion := "2.12.3"
  libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.3" % "test"
}



lazy val inventory = project.settings(common)

lazy val checkout_service = project.settings(common)

lazy val notification_service = project.settings(common)

lazy val inventory_sdk = project.settings(common)


lazy val root = (project in file(".")).
  aggregate(inventory, checkout_service, notification_service, inventory_sdk)
        