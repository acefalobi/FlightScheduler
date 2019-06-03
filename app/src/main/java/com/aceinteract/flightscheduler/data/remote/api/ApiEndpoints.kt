package com.aceinteract.flightscheduler.data.remote.api

/**
 * Contains all Api Endpoints used in the application.
 */
object ApiEndpoints {

    /**
     * Base Api url for Lufthansa.
     */
    const val BASE_URL: String = "https://api.lufthansa.com/v1/"

    /**
     * Api endpoints that allow the POST method.
     */
    object POST {
        /**
         * Endpoint for getting access token
         */
        const val ACCESS_TOKEN: String = "oauth/token"
    }

    /**
     * Api endpoints that allow the GET method.
     */
    object GET {
        /**
         * Endpoint for getting nearest airports
         */
        const val NEAREST_AIRPORTS: String = "references/airports/nearest/"

        /**
         * Endpoint for getting airlines
         */
        const val AIRLINES: String = "references/airlines/"

        /**
         * Endpoint for getting flight schedules
         */
        const val FLIGHT_SCHEDULES: String = "operations/schedules/"
    }

}
