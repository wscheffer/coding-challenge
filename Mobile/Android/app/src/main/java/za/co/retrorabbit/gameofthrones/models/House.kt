package za.co.retrorabbit.gameofthrones.models

import com.google.gson.annotations.SerializedName

data class House(
    @SerializedName("url") val url: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("region") val region: String? = null,
    @SerializedName("coatOfArms") val coatOfArms: String? = null,
    @SerializedName("words") val words: String? = null,
    @SerializedName("titles") val titles: List<String> = arrayListOf(),
    @SerializedName("seats") val seats: List<String> = arrayListOf(),
    @SerializedName("currentLord") val currentLord: String? = null,
    @SerializedName("heir") val heir: String? = null,
    @SerializedName("overlord") val overlord: String? = null,
    @SerializedName("founded") val founded: String? = null,
    @SerializedName("founder") val founder: String? = null,
    @SerializedName("diedOut") val diedOut: String? = null,
    @SerializedName("ancestralWeapons") val ancestralWeapons: List<String> = arrayListOf(),
    @SerializedName("cadetBranches") val cadetBranches: List<String> = arrayListOf(),
    @SerializedName("swornMembers") val swornMembers: List<String> = arrayListOf()
)
