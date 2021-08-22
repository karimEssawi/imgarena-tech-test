package imgarena.tech.test

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._


object App extends App {
  def run(sourceDataPath: String, resultsRootPath: String): Unit = {
    val sparkSession = SparkSession.builder().config("spark.master", "local[*]").getOrCreate()
    sparkSession.sparkContext.setLogLevel("WARN")

    import sparkSession.implicits._
    val dataIO = new DataIO(sparkSession)
    val transformer = new Transformer(sparkSession)

    val sourceDf = dataIO.readCSV(sourceDataPath)

    val jsonSchema = dataIO.inferJsonSchema(sourceDf, schemaIndex = 3)

    val dfWithSchema = sourceDf.withColumn("eventPayload", from_json($"match_element", jsonSchema)).persist()

    println("============== Flattening and enriching source dataframe ==================")
    val flattenedDf = transformer.flatten(dfWithSchema)
    dataIO.write(flattenedDf, s"$resultsRootPath/match-data-flattened-and-enriched")

    println("============== Filling missing overall set scores for PointScored events ==================")
    val overallSetScoreDf = transformer.calcOverallSetScore(dfWithSchema)
    dataIO.write(overallSetScoreDf, s"$resultsRootPath/points-scored-with-overall-score-sets-filled")

  }

  run(this.getClass.getResource("/keystrokes-for-tech-test.csv").getPath, "spark_output/")
}
