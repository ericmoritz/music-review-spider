name := "music-review-spider"

version := "latest"

scalaVersion := "2.10.4"

libraryDependencies += "org.jsoup" % "jsoup" % "1.8.1"

libraryDependencies += "org.specs2" %% "specs2-core" % "2.4.15" % "test"

libraryDependencies += "com.netaporter" %% "scala-uri" % "0.4.4"

libraryDependencies += "org.json4s" %% "json4s-native" % "3.2.10"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.2.0"

libraryDependencies += "org.openrdf.sesame" % "sesame-repository" % "2.7.14"

libraryDependencies += "org.openrdf.sesame" % "sesame-repository-sparql" % "2.7.14"

scalacOptions in Test ++= Seq("-Yrangepos")

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

enablePlugins(JavaAppPackaging)

net.virtualvoid.sbt.graph.Plugin.graphSettings
