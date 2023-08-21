package za.co.retrorabbit.gameofthrones.models

import com.google.gson.annotations.SerializedName

data class Person(
    @SerializedName("url") var url: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("culture") var culture: String? = null,
    @SerializedName("born") var born: String? = null,
    @SerializedName("died") var died: String? = null,
    @SerializedName("titles") var titles: ArrayList<String> = arrayListOf(),
    @SerializedName("aliases") var aliases: ArrayList<String> = arrayListOf(),
    @SerializedName("father") var father: String? = null,
    @SerializedName("mother") var mother: String? = null,
    @SerializedName("spouse") var spouse: String? = null,
    @SerializedName("allegiances") var allegiances: ArrayList<String> = arrayListOf(),
    @SerializedName("books") var books: ArrayList<String> = arrayListOf(),
    @SerializedName("povBooks") var povBooks: ArrayList<String> = arrayListOf(),
    @SerializedName("tvSeries") var tvSeries: ArrayList<String> = arrayListOf(),
    @SerializedName("playedBy") var playedBy: ArrayList<String> = arrayListOf()
)