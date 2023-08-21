package za.co.retrorabbit.gameofthrones.models

import android.os.Build
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class Book(
    @SerializedName("url") var url: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("isbn") var isbn: String? = null,
    @SerializedName("authors") var authors: ArrayList<String> = arrayListOf(),
    @SerializedName("numberOfPages") var numberOfPages: Int? = null,
    @SerializedName("publisher") var publisher: String? = null,
    @SerializedName("country") var country: String? = null,
    @SerializedName("mediaType") var mediaType: String? = null,
    @SerializedName("released") var released: String? = null,
    @SerializedName("characters") var characters: ArrayList<String> = arrayListOf(),
    @SerializedName("povCharacters") var povCharacters: ArrayList<String> = arrayListOf()
) {
    val releasedFormatted: String?
        get() =
            released?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                    val date = LocalDate.parse(released, formatter)
                    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    date.format(formatter)
                } else {
                    var sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    val date = sdf.parse(released);
                    sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    sdf.format(date)
                }
            } ?: ""
}