package com.aceinteract.flightscheduler.data.entity

import com.google.gson.annotations.SerializedName

/**
 * Data object for containing names in different languages
 */
data class Name(
    /**
     * Language code of the name
     */
    @field:SerializedName("@LanguageCode") val languageCode: String,
    /**
     * Value of the name
     */
    @field:SerializedName("$") val value: String
) {

    /**
     * Data object for containing the name resource from the api
     */
    data class Names(
        /**
         * The name object
         */
        @field:SerializedName("Name") val name: List<Name>
    )

    /**
     * Data object for containing the name resource for a single name from the api
     */
    data class SingleName(
        /**
         * The name object
         */
        @field:SerializedName("Name") val name: Name
    )

}