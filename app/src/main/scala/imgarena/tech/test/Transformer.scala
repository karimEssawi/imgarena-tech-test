package imgarena.tech.test

import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import imgarena.tech.test.DataFrameUtils._
import org.apache.spark.sql.types._

class Transformer(spark: SparkSession) {
  import spark.implicits._

  def flatten(df: DataFrame): Dataset[PointFlow] = {
    val window = Window.partitionBy("match_id").orderBy("message_id")

    df.withColumn("pre_serve_state", lag("eventPayload", 1).over(window))
      .withColumn("post_serve_state", lead("eventPayload", 1).over(window))
      //      .withColumn("serveId", row_number().over(window))
      .filter($"eventPayload.eventElementType" === "PointStarted")
//            .filter($"match_id".isin(Seq("29304", "30941"):_*))
      .withColumn("state_before_serve",
        struct(
          $"eventPayload.server.team" as "server",
          when($"pre_serve_state.eventElementType" === "PhysioCalled", 1).otherwise(0) as "physio",
        )
      )
      .withColumn("serve_outcome",
        struct(
          when($"post_serve_state.eventElementType" === "PointLet", 1).otherwise(0) as "let",
          when($"post_serve_state.eventElementType" === "PointFault", 1).otherwise(0) as "fault",
          when(
            $"post_serve_state.eventElementType" === "PointScored"
              && $"post_serve_state.details.scoredBy" === "TeamA", 1
          ).otherwise(0) as "team_a_scored",
          when(
            $"post_serve_state.eventElementType" === "PointScored"
              && $"post_serve_state.details.scoredBy" === "TeamB", 1
          ).otherwise(0) as "team_b_scored",
        )
      )
      .select(
        $"match_id" cast LongType,
        $"message_id" cast LongType,
        $"state_before_serve",
//        $"serveId",
        $"serve_outcome",
        $"pre_serve_state.eventElementType" as "pre_serve_event",
        $"post_serve_state.eventElementType" as "post_serve_event",
//        $"eventPayload",
      )
      .sort($"match_id", $"message_id")
      .as[PointFlow]
  }

  def enrichServeData(df: DataFrame): DataFrame =
    df
      .withColumn("serveAttempt",
        when($"pre_serve_event" === "PointFault", "second").otherwise("first")
      )

  def calcOverallSetScore(df: DataFrame): DataFrame = {
    df
      .filter($"eventPayload.eventElementType" === "PointScored")
//      .filter(size($"eventPayload.score.previousSetsScore") > 0)
      .select($"eventPayload.*")
      //      .as[Eventpayload]
      .withColumn("overallSetScore",
        transform($"score.previousSetsScore", c => {
          val delta = c("gamesA") - c("gamesB")
          struct(
            when(delta > 0, 1).otherwise(0) as "setA",
            when(delta < 0, 1).otherwise(0) as "setB"
          )
        })
      )
      .withColumn("overallSetScore",
        aggregate($"overallSetScore",
          struct(lit(0) as "setA", lit(0) as "setB"), (acc, v) => {
            struct(
              acc("setA") + v("setA") as "setsA",
              acc("setB") + v("setB") as "setsB",
            )
        })
      )
      .dropNestedColumn("score.overallSetScore")
      .withColumn("score", struct($"score.*", $"overallSetScore"))
      .drop("overallSetScore")
      .select(
        $"score",
        $"seqNum",
        $"server",
        $"details",
        $"matchTime",
        $"timestamp",
        $"nextServer",
        $"eventElementType"
      )
  }
}
