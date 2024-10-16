package com.example.tasky.auth


import com.google.gson.annotations.SerializedName

data class UserAccessTokenResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("expirationTimestamp")
    val expirationTimestamp: Long
)