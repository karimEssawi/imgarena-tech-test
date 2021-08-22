package imgarena.tech.test

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

class DataIO(spark: SparkSession) {
  import spark.implicits._

  def readCSV(path: String): DataFrame =
    spark
      .read
      .option("header", "true")
      .csv(path)

  def inferJsonSchema(df: DataFrame, schemaIndex: Int): StructType =
    spark.read.json(df.map(_.getString(schemaIndex))).schema

  def write[T](df: Dataset[T], path: String): Unit = {
    df.coalesce(1)
      .write
      .option("header", "true")
      .mode("overwrite")
      .json(path)
  }
}
