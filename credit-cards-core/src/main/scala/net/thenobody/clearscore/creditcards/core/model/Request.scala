package net.thenobody.clearscore.creditcards.core.model

import com.github.nscala_time.time.Imports._

case class Request(
  firstName: String,
  lastName: String,
  dateOfBirth: DateTime,
  score: Int,
  employmentStatus: EmploymentStatus,
  salary: Int
)

object Request {
  val DateOfBirthFormatter = DateTimeFormat.forPattern("yyyy/MM/dd")
}