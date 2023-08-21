package za.co.retrorabbit.gameofthrones.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import za.co.retrorabbit.gameofthrones.models.Book
import za.co.retrorabbit.gameofthrones.models.House
import za.co.retrorabbit.gameofthrones.models.Person

interface GameOfThronesService {

    companion object {
        var BASE_URL = "https://anapioficeandfire.com/api/"
    }

    @GET("houses?pageSize=20")
    fun getHouses(
//        @Query("name") query: String,
//        @Query("page") startIndex: Int,
//        @Query("pageSize") limit: Int
    ): Call<List<House>>?

    @GET("characters/{id}")
    fun getCharacter(@Path("id") id: Int): Call<Person>?

    @GET("books/{id}")
    fun getBooks(@Path("id") id: Int): Call<Book>?
}