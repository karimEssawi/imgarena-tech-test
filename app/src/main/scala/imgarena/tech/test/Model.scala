package imgarena.tech.test

//case class Eventpayload (
//
//                          delayStatus: Option[String],
//                          details: Option[Details],
//                          eventElementType: Option[String],
//                          faultType: Option[String],
//                          matchStatus: Option[MatchStatus],
//                          matchTime: Option[String],
//                          nextServer: Option[NextServer],
//                          numOverrules: Option[Long],
//                          playerId: Option[Long],
//                          reason: Option[String],
//                          score: Option[Score],
//                          seqNum: Option[Long],
//                          server: Option[Server],
//                          team: Option[String],
//                          time: Option[String],
//                          timestamp: Option[String],
//                          won: Option[String]
//                        )
//case class Details (
//
//                     pointType: String,
//                     scoredBy: String
//                   )


//case class MatchStatus(
//
//                        courtNum: Option[Long],
//                        firstServer: Option[String],
//                        matchState: Option[MatchState],
//                        numSets: Option[Long],
//                        scoringType: Option[String],
//                        teamAPlayer1: Option[String],
//                        teamAPlayersDetails: Option[TeamAPlayersDetails],
//                        teamBPlayer1: Option[String],
//                        teamBPlayersDetails: Option[TeamBPlayersDetails],
//                        tieBreakType: Option[String],
//                        tossChooser: Option[String],
//                        tossWinner: Option[String],
//                        umpire: Option[String],
//                        umpireCode: Option[String],
//                        umpireCountry: Option[String]
//                       )
//case class MatchState(
//
//                        challengeEnded: Option[String],
//                        evaluationStarted: Option[String],
//                        locationTimestamp: Option[String],
//                        playerId: Option[Long],
//                        state: Option[String],
//                        team: Option[String],
//                        treatmentEnded: Option[String],
//                        treatmentLocation: Option[String],
//                        treatmentStarted: Option[String],
//                        won: Option[String]
//                      )


//case class TeamAPlayersDetails(
//
//                                 player1Country: Option[String],
//                                 player1Id: Option[String]
//                               )
//
//
//case class TeamBPlayersDetails(
//
//                                 player1Country: Option[String],
//                                 player1Id: Option[String]
//                               )


//case class NextServer(
//
//                        team: Option[String]
//                      )


//case class Score (
//
//                   currentGameScore: CurrentGameScore,
////                   currentGmatch_elementameScore: CurrentGmatch_elementameScore,
//                   currentSetScore: CurrentSetScore,
//                   overallSetScore: OverallSetScore,
//                   previousSetsScore: Seq[PreviousSetsScore]
//                 )
//case class CurrentGameScore(
//
//                              gameType: String,
//                              pointsA: String,
//                              pointsB: String
//                            )
//
//
//case class CurrentGmatch_elementameScore(
//
//                                           gameType: String,
//                                           pointsA: String,
//                                           pointsB: String
//                                         )
//
//
//case class CurrentSetScore(
//
//                             gamesA: Long,
//                             gamesB: Long
//                           )
//
//
//case class OverallSetScore(
//
//                             setsA: Long,
//                             setsB: Long
//                           )
//
//
//case class PreviousSetsScore(
//
//                               gamesA: Long,
//                               gamesB: Long,
//                               tieBreakScore: Option[TiebreakScore] = None
//                             )
//case class TiebreakScore(
//
//                           pointsA: Long,
//                           pointsB: Long
//                         )
//
//
//case class Server (
//
//                    team: String
//                  )

case class PointFlow(match_id: Long, message_id: Long, state_before_serve: StateBeforeServe, serve_outcome: ServeOutcome, serve_attempt: String, pre_serve_event: String, post_serve_event: String)
case class StateBeforeServe(server: String, physio: Int)
case class ServeOutcome(let: Int, fault: Int, team_a_scored: Int, team_b_scored: Int)
