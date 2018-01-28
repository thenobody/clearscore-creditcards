package net.thenobody.clearscore.creditcards.core.model

import enumeratum._
import EnumEntry._

sealed trait EmploymentStatus extends EnumEntry with CapitalSnakecase

object EmploymentStatus extends Enum[EmploymentStatus] {
  val values = findValues

  case object FullTime extends EmploymentStatus
  case object PartTime extends EmploymentStatus
  case object Student extends EmploymentStatus
  case object Unemployed extends EmploymentStatus
  case object Retired extends EmploymentStatus
}
