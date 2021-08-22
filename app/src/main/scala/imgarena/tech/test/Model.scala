package imgarena.tech.test

case class PointFlow(match_id: Long, message_id: Long, state_before_serve: StateBeforeServe, serve_outcome: ServeOutcome, serve_attempt: String, pre_serve_event: String, post_serve_event: String)
case class StateBeforeServe(server: String, physio: Int)
case class ServeOutcome(let: Int, fault: Int, team_a_scored: Int, team_b_scored: Int)
