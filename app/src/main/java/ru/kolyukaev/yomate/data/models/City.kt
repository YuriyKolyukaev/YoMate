package ru.kolyukaev.yomate.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class City(
    @SerializedName("country")
    val country: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("coord")
    val coord: Coord
) : Serializable

