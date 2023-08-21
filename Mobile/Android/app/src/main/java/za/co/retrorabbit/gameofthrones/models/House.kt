package za.co.retrorabbit.gameofthrones.models

import com.google.gson.annotations.SerializedName

data class House(
    @SerializedName("url") val url: String,
    @SerializedName("name") val name: String,
    @SerializedName("region") val region: String,
    @SerializedName("coatOfArms") val coatOfArms: String,
    @SerializedName("words") val words: String,
    @SerializedName("titles") val titles: List<String>,
    @SerializedName("seats") val seats: List<String>,
    @SerializedName("currentLord") val currentLord: String,
    @SerializedName("heir") val heir: String,
    @SerializedName("overlord") val overlord: String,
    @SerializedName("founded") val founded: String,
    @SerializedName("founder") val founder: String,
    @SerializedName("diedOut") val diedOut: String,
    @SerializedName("ancestralWeapons") val ancestralWeapons: List<String>,
    @SerializedName("cadetBranches") val cadetBranches: List<String>,
    @SerializedName("swornMembers") val swornMembers: List<String>
)
