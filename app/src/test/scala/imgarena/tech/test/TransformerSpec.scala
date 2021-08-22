package imgarena.tech.test

import com.github.mrpowers.spark.fast.tests.DatasetComparer
import org.apache.spark.sql.functions.from_json
import org.junit.runner.RunWith
import org.scalatest.funspec.AnyFunSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TransformerSpec extends AnyFunSpec with TestUtil with DatasetComparer {

  import spark.implicits._

  describe("Transformer") {
    val transformer = new Transformer(spark)
    val sourceDf = spark.read.option("header", "true").csv(getClass.getResource("/keystrokes-test-data.csv").getPath)
    val schema = spark.read.json(sourceDf.map(r => r.getString(3))).schema
    val sourceDfWithSchema = sourceDf.withColumn("eventPayload", from_json($"match_element", schema))

    it("should flatten, encode, and enrich source data") {
      val actualDF = transformer.flatten(sourceDfWithSchema)

      val expectedDF = Seq(
        PointFlow(383269, 343, StateBeforeServe(server = "TeamA", physio = 0), ServeOutcome(let = 1, fault = 0, team_a_scored = 0, team_b_scored = 0), serve_attempt = "first", "MatchStatusUpdate", "PointLet"),
        PointFlow(383269, 345, StateBeforeServe(server = "TeamA", physio = 0), ServeOutcome(let = 0, fault = 0, team_a_scored = 1, team_b_scored = 0), serve_attempt = "first", "PointLet", "PointScored"),
        PointFlow(383269, 356, StateBeforeServe(server = "TeamB", physio = 1), ServeOutcome(let = 0, fault = 1, team_a_scored = 0, team_b_scored = 0), serve_attempt = "first", "PhysioCalled", "PointFault"),
        PointFlow(383269, 359, StateBeforeServe(server = "TeamB", physio = 0), ServeOutcome(let = 0, fault = 0, team_a_scored = 1, team_b_scored = 0), serve_attempt = "second", "PointFault", "PointScored")
      ).toDS()

      assertSmallDatasetEquality(actualDF, expectedDF, ignoreNullable = true)
    }

    it("should calculate missing overall set score") {
      val actualDF = transformer.calcOverallSetScore(sourceDfWithSchema).select($"score.overallSetScore.*")

      val expectedDF = Seq(
        (1,0), (0,0), (1,0), (2,1)
      ).toDF("setA", "setB")

      assertSmallDatasetEquality(actualDF, expectedDF, ignoreNullable = true)
    }
  }
}

