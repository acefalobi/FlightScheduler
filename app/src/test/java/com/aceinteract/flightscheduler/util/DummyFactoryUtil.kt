package com.aceinteract.flightscheduler.util

import com.aceinteract.flightscheduler.data.entity.*
import com.aceinteract.flightscheduler.data.remote.api.response.AccessTokenResponse
import com.aceinteract.flightscheduler.data.remote.api.response.AirlineResponse
import com.aceinteract.flightscheduler.data.remote.api.response.FlightSchedulesResponse
import com.aceinteract.flightscheduler.data.remote.api.response.NearestAirportsResponse
import java.util.*
import kotlin.collections.ArrayList

object DummyFactoryUtil {

    fun createAccessToken(accessToken: String) = AccessTokenResponse(accessToken)

    fun createAirlineResponse(airlineID: String, name: String) =
        AirlineResponse(
            AirlineResponse.AirlineResource(
                AirlineResponse.AirlineResource.AirlineData(
                    Airline(airlineID, Name.SingleName(Name("en", name)))
                )
            )
        )

    fun createNearestAirportsResponse(count: Int) =
        NearestAirportsResponse(
            NearestAirportsResponse.AirportsResource(
                NearestAirportsResponse.AirportsResource.AirportData(
                    ArrayList<AirportRemote>().apply {
                        repeat(count) {
                            add(
                                AirportRemote(
                                    "XYZ",
                                    Position(Position.Coordinate(0.0, 0.0)),
                                    Name.Names(arrayListOf(Name("en", "Random airport"))),
                                    Distance(99, "KM")
                                )
                            )
                        }
                    }
                )
            )
        )

    fun createAirport(code: String) =
        Airport(
            "City State", code, "State Country", "0.0", "0.0",
            "Random airport", "City State"
        )

    fun createAirports(count: Int) =
        ArrayList<Airport>().apply {
            repeat(count) {
                add(
                    Airport(
                        "City State", "XYZ", "State Country", "0.0", "0.0",
                        "Random airport", "City State"
                    )
                )
            }
        }

    fun createFlightSchedules(count: Int, airportCode: String, airline: Airline) =
        FlightSchedulesResponse(
            FlightSchedulesResponse.FlightScheduleResource(
                ArrayList<FlightSchedule>().apply {
                    repeat(count) {
                        add(
                            FlightSchedule(
                                arrayListOf(
                                    FlightSchedule.Flight(
                                        FlightSchedule.Flight.Stop(
                                            airportCode,
                                            FlightSchedule.Flight.Stop.ScheduledTime(Calendar.getInstance())
                                        ),
                                        FlightSchedule.Flight.Stop(
                                            airportCode,
                                            FlightSchedule.Flight.Stop.ScheduledTime(Calendar.getInstance())
                                        ),
                                        FlightSchedule.Flight.Carrier(airline.airlineID, "1234")
                                    ).also { it.airline = airline }
                                )
                            )
                        )
                    }
                }

            )
        )

}