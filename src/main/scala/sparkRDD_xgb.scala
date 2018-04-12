/*
 Copyright (c) 2014 by Contributors
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package ml.dmlc.xgboost4j.scala.example.spark

import org.apache.spark.ml.feature.{LabeledPoint => MLLabeledPoint}
import org.apache.spark.ml.linalg.{DenseVector => MLDenseVector}
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

import ml.dmlc.xgboost4j.scala.spark.XGBoost
//import ml.dmlc.xgboost4j.scala.Booster

object SparkWithRDD {
  def main(args: Array[String]): Unit = {
    /*
    if (args.length != 5) {
      println(
        "usage: program num_of_rounds num_workers training_path test_path model_path")
      sys.exit(1)
    }
    */
    val sparkConf = new SparkConf().setMaster("local").setAppName("XGBoost-spark-example")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    //sparkConf.registerKryoClasses(Array(classOf[Booster]))
    implicit val sc = new SparkContext(sparkConf)
    
    // settings
    val inputTrainPath = "/Users/ericchiu/spark-2.3.0-bin-hadoop2.7/sparkRDD_xgb/data/traindata.csv"//args(2)
    val inputTestPath = "/Users/ericchiu/spark-2.3.0-bin-hadoop2.7/sparkRDD_xgb/data/testdata.csv"//args(3)
    val outputModelPath = "/Users/ericchiu/spark-2.3.0-bin-hadoop2.7/sparkRDD_xgb/model"//args(4)
    val outputTxtPath = "/Users/ericchiu/spark-2.3.0-bin-hadoop2.7/sparkRDD_xgb/predict"//args(4)
    // number of iterations
    val numRound = 10//args(0).toInt
    val num_workers = 1 //args(1).toInt

    // processing
    val trainCSV = sc.textFile(inputTrainPath).map(line =>line.split(",").map(_.trim.toDouble))
    val trainRDD = trainCSV.map(lp => MLLabeledPoint(lp.last, new MLDenseVector(lp.slice(0,lp.length-1))))
    trainRDD.take(10).foreach(println)
    
    val testCSV = sc.textFile(inputTestPath).map(line =>line.split(",").map(_.trim.toDouble))
    val testSet = testCSV.map(lp => new MLDenseVector(lp.slice(0,lp.length-1)))
    // training parameters
    val paramMap = List(
      "eta" -> 0.1f,
      "max_depth" -> 2,
      "objective" -> "binary:logistic").toMap

    //************
    val t0 = System.nanoTime()
    val xgboostModel = XGBoost.trainWithRDD(trainRDD, paramMap, numRound, nWorkers = num_workers,
      useExternalMemory = true)
    val t1 = System.nanoTime()
    println(s"Elapsed time: ${t1 - t0} ns")
    println("s")
    println("s")
    println("s")
    //************

    val predict = xgboostModel.predict(testSet, missingValue = Float.NaN)
    val result = predict.map(lp => lp.deep.mkString("\n"))
    // save model to HDFS path
    result.saveAsTextFile(outputTxtPath)
    xgboostModel.saveModelAsHadoopFile(outputModelPath)
    sc.stop()
  }
}