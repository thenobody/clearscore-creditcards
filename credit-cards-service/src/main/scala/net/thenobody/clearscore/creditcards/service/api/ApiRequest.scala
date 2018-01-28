package net.thenobody.clearscore.creditcards.service.api

case class ApiRequest(firstName: String,
                      lastName: String,
                      dob: String,
                      score: Int,
                      employmentStatus: String,
                      salary: Int)
