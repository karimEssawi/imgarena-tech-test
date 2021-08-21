package imgarena.tech.test

import com.github.mrpowers.spark.fast.tests.DatasetComparer
import org.apache.spark.sql.functions.{from_json, struct}
import org.junit.runner.RunWith
import org.scalatest.funspec.AnyFunSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TransformerSpec extends AnyFunSpec with TestUtil with DatasetComparer {

  import spark.implicits._

  describe("Transformer") {
    val transformer = new Transformer(spark)
    val sourceDf = spark.read.option("header", true).csv(getClass.getResource("/keystrokes-test-data.csv").getPath)
    val schema = spark.read.json(sourceDf.map(r => r.getString(3))).schema
    val sourceDfWithSchema = sourceDf.withColumn("eventPayload", from_json($"match_element", schema))

    it("should flatten and encode source data") {
      val actualDF = transformer.flatten(sourceDfWithSchema)

//      actualDF.show(false)
//      actualDF.printSchema

      val expectedDF = Seq(
        PointFlow(383269, 343, StateBeforeServe(server = "TeamA", physio = 0), ServeOutcome(let = 1, fault = 0, team_a_scored = 0, team_b_scored = 0), "MatchStatusUpdate", "PointLet"),
        PointFlow(383269, 345, StateBeforeServe(server = "TeamA", physio = 0), ServeOutcome(let = 0, fault = 0, team_a_scored = 1, team_b_scored = 0), "PointLet", "PointScored"),
        PointFlow(383269, 355, StateBeforeServe(server = "TeamB", physio = 0), ServeOutcome(let = 0, fault = 1, team_a_scored = 0, team_b_scored = 0), "PointScored", "PointFault"),
        PointFlow(383269, 358, StateBeforeServe(server = "TeamB", physio = 1), ServeOutcome(let = 0, fault = 0, team_a_scored = 1, team_b_scored = 0), "PhysioCalled", "PointScored")
      ).toDS()

      assertSmallDatasetEquality(actualDF, expectedDF, ignoreNullable = true)
    }

  }

}

//case class PointFlow(match_id: Long, state_before_serve: StateBeforeServe, serve_outcome: ServeOutcome, pre_serve_event: String, post_serve_event: String)
//case class StateBeforeServe(server: String, physio: Int)
//case class ServeOutcome(let: Int, fault: Int, team_a_scored: Int, team_b_scored: Int)
