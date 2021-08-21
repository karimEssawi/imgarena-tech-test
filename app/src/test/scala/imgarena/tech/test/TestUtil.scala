package imgarena.tech.test

import org.apache.spark.sql.SparkSession

trait TestUtil {
  lazy val spark =
    SparkSession
      .builder()
      .master("local")
      .appName("spark session")
      .config("spark.sql.shuffle.partitions", "1")
      .getOrCreate()

  spark.sparkContext.setLogLevel("WARN")
}
