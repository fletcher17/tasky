package com.example.tasky.auth


import com.google.gson.annotations.SerializedName

data class UserSignInResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("accessTokenExpirationTimestamp")
    val accessTokenExpirationTimestamp: Long,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("userId")
    val userId: String
)
