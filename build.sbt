val xgboostSparkPath = "file:///Users/ericchiu/spark-2.3.0-bin-hadoop2.7/sparkRDD_xgb/lib/xgboost4j-spark-0.8-SNAPSHOT-jar-with-dependencies.jar"

val xgboostPath = "file:///Users/ericchiu/spark-2.3.0-bin-hadoop2.7/sparkRDD_xgb/lib/xgboost4j-0.8-SNAPSHOT-jar-with-dependencies.jar"

val xgboostExamplePath = "file:///Users/ericchiu/spark-2.3.0-bin-hadoop2.7/sparkRDD_xgb/lib/xgboost4j-example-0.8-SNAPSHOT-jar-with-dependencies.jar"

name := "Spark XGBoost"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % "2.3.0" ,
  "org.apache.spark" %% "spark-mllib" % "2.3.0",
  "ml.dmlc" % "xgboost4j" % "0.8" % "provided" from xgboostPath,
  "ml.dmlc" % "xgboost4j-spark" % "0.8" ,
  "ml.dmlc" % "xgboost4j-example" % "0.8"
)

