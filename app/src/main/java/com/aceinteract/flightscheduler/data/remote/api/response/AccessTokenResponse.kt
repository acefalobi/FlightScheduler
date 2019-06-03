package com.aceinteract.flightscheduler.data.remote.api.response

import com.google.gson.annotations.SerializedName

/**
 * Response object from access token request.
 */
data class AccessTokenResponse(
    /**
     * Access token gotten from request.
     */
    @field:SerializedName("access_token") val accessToken: String?
)