package imgarena.tech.test

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}

class DataIO(spark: SparkSession) {
  import spark.implicits._

  def readCSV(path: String): DataFrame =
    spark
      .read
      .option("header", true)
//      .option("mergeSchema", true)
      .csv(path)

  def inferJsonSchema(df: DataFrame, schemaIndex: Int): StructType =
    spark.read.json(df.map(r => r.getString(schemaIndex))).schema

  def write(df: DataFrame, path: String): Unit = {
    df.coalesce(1)
      .write
      .option("header", "true")
      .mode("overwrite")
      .csv(path)
  }
}
