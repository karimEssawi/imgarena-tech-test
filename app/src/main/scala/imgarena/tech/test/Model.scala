package imgarena.tech.test

case class Eventpayload (

                          delayStatus: Option[String],
                          details: Option[Details],
                          eventElementType: Option[String],
                          faultType: Option[String],
                          matchStatus: Option[Matchstatus],
                          matchTime: Option[String],
                          nextServer: Option[Nextserver],
                          numOverrules: Option[Long],
                          playerId: Option[Long],
                          reason: Option[String],
                          score: Option[Score],
                          seqNum: Option[Long],
                          server: Option[Server],
                          team: Option[String],
                          time: Option[String],
                          timestamp: Option[String],
                          won: Option[String]
                        )
case class Details (

                     pointType: Option[String],
                     scoredBy: Option[String]
                   )


case class Matchstatus (

                         courtNum: Option[Long],
                         firstServer: Option[String],
                         matchState: Option[Matchstate],
                         numSets: Option[Long],
                         scoringType: Option[String],
                         teamAPlayer1: Option[String],
                         teamAPlayersDetails: Option[Teamaplayersdetails],
                         teamBPlayer1: Option[String],
                         teamBPlayersDetails: Option[Teambplayersdetails],
                         tieBreakType: Option[String],
                         tossChooser: Option[String],
                         tossWinner: Option[String],
                         umpire: Option[String],
                         umpireCode: Option[String],
                         umpireCountry: Option[String]
                       )
case class Matchstate (

                        challengeEnded: Option[String],
                        evaluationStarted: Option[String],
                        locationTimestamp: Option[String],
                        playerId: Option[Long],
                        state: Option[String],
                        team: Option[String],
                        treatmentEnded: Option[String],
                        treatmentLocation: Option[String],
                        treatmentStarted: Option[String],
                        won: Option[String]
                      )


case class Teamaplayersdetails (

                                 player1Country: Option[String],
                                 player1Id: Option[String]
                               )


case class Teambplayersdetails (

                                 player1Country: Option[String],
                                 player1Id: Option[String]
                               )


case class Nextserver (

                        team: Option[String]
                      )


case class Score (

                   currentGameScore: Option[Currentgamescore],
                   currentGmatch_elementameScore: Option[Currentgmatch_elementamescore],
                   currentSetScore: Option[Currentsetscore],
                   overallSetScore: Option[Overallsetscore],
                   previousSetsScore: Seq[Previoussetsscore]
                 )
case class Currentgamescore (

                              gameType: Option[String],
                              pointsA: Option[String],
                              pointsB: Option[String]
                            )


case class Currentgmatch_elementamescore (

                                           gameType: Option[String],
                                           pointsA: Option[String],
                                           pointsB: Option[String]
                                         )


case class Currentsetscore (

                             gamesA: Option[Long],
                             gamesB: Option[Long]
                           )


case class Overallsetscore (

                             setsA: Option[Long],
                             setsB: Option[Long]
                           )


case class Previoussetsscore (

                               gamesA: Option[Long],
                               gamesB: Option[Long],
                               tieBreakScore: Option[Tiebreakscore]
                             )
case class Tiebreakscore (

                           pointsA: Option[Long],
                           pointsB: Option[Long]
                         )


case class Server (

                    team: Option[String]
                  )