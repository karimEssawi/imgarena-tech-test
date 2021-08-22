package imgarena.tech.test

import com.github.mrpowers.spark.fast.tests.DatasetComparer
import org.apache.spark.sql.DataFrame
import org.junit.runner.RunWith
import org.scalatest.Ignore
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatestplus.junit.JUnitRunner

import java.nio.file.Files

@RunWith(classOf[JUnitRunner])
@Ignore
class AppSpec extends AnyFunSpec with TestUtil with DatasetComparer with TableDrivenPropertyChecks {
  import spark.implicits._

  describe("App") {
    it("should output correct results into json files") {
      val tempDir = Files.createTempDirectory("test-results-").toFile
      val tempDirPath = tempDir.getPath
      tempDir.deleteOnExit()

      App.run(
        sourceDataPath = getClass.getResource("/keystrokes-test-data.csv").getPath,
        resultsRootPath = tempDirPath
      )

      val expectedFlattenedDF = Seq(
        PointFlow(383269, 343, StateBeforeServe(server = "TeamA", physio = 0), ServeOutcome(let = 1, fault = 0, team_a_scored = 0, team_b_scored = 0), serve_attempt = "first", "MatchStatusUpdate", "PointLet"),
        PointFlow(383269, 345, StateBeforeServe(server = "TeamA", physio = 0), ServeOutcome(let = 0, fault = 0, team_a_scored = 1, team_b_scored = 0), serve_attempt = "first", "PointLet", "PointScored"),
        PointFlow(383269, 356, StateBeforeServe(server = "TeamB", physio = 1), ServeOutcome(let = 0, fault = 1, team_a_scored = 0, team_b_scored = 0), serve_attempt = "first", "PhysioCalled", "PointFault"),
        PointFlow(383269, 359, StateBeforeServe(server = "TeamB", physio = 0), ServeOutcome(let = 0, fault = 0, team_a_scored = 1, team_b_scored = 0), serve_attempt = "second", "PointFault", "PointScored")
      ).toDF()

      val expectedOverallSetScoreDF = Seq(
        (1,0), (0,0), (1,0), (2,1)
      ).toDF("setA", "setB")

      val comparableDfs =
        Table(
          ("actualDFPath", "expectedDf"),
          ("match-data-flattened-and-enriched", expectedFlattenedDF),
          ("points-scored-with-overall-score-sets-filled", expectedOverallSetScoreDF),
        )

      forAll(comparableDfs) { (actualDFPath: String, expectedDF: DataFrame) =>
        val actualDF = spark.read
          .option("header", "true")
          .schema(expectedDF.schema)
          .json(s"$tempDirPath/$actualDFPath")

        assertSmallDatasetEquality(actualDF, expectedDF, ignoreNullable = true, orderedComparison = false)
      }
    }
  }
}
