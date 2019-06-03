package com.aceinteract.flightscheduler.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Data class for flight schedule.
 */
@Parcelize
data class FlightSchedule(

    /**
     * Flights in the schedule.
     */
    @field:SerializedName("Flight") val flights: List<Flight>

) : Parcelable {

    /**
     * Data class containing flight details.
     */
    @Parcelize
    data class Flight(

        /**
         * Departure details of flight.
         */
        @field:SerializedName("Departure") val departure: Stop,

        /**
         * Arrival details of this flight.
         */
        @field:SerializedName("Arrival") val arrival: Stop,

        /**
         * Details of carrier offering this flight
         */
        @field:SerializedName("MarketingCarrier") val carrier: Carrier

    ) : Parcelable {

        /**
         * Airline offering this flight.
         */
        @IgnoredOnParcel
        var airline: Airline? = null

        /**
         * Data class for arrival and departure stops for a flight.
         */
        @Parcelize
        data class Stop(

            /**
             * Airport code where stop is happening.
             */
            @field:SerializedName("AirportCode") val airportCode: String,

            /**
             * Scheduled time for stop.
             */
            @field:SerializedName("ScheduledTimeLocal") val scheduledTime: ScheduledTime,

            /**
             * Positional coordinates of the stop.
             */
            var position: Position.Coordinate? = null

        ) : Parcelable {

            /**
             * Data class for scheduled times for stops.
             */
            @Parcelize
            data class ScheduledTime(

                /**
                 * Time and date for schedule.
                 */
                @field:SerializedName("DateTime") val dateTime: Calendar

            ) : Parcelable {
                /**
                 * Gets time from date time calendar.
                 */
                val timeInString: String
                    get() = dateTime.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0') +
                            ":${dateTime.get(Calendar.MINUTE).toString().padStart(2, '0')}"
            }

        }

        /**
         * Data class for carrier details.
         */
        @Parcelize
        data class Carrier(
            /**
             * ID for the airline.
             */
            @field:SerializedName("AirlineID") val airlineID: String,

            /**
             * Number of the Airline flight.
             */
            @field:SerializedName("FlightNumber") val flightNumber: String
        ) : Parcelable

    }

}