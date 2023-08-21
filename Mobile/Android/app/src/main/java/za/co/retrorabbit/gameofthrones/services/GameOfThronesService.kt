package za.co.retrorabbit.gameofthrones.services

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import za.co.retrorabbit.gameofthrones.models.Book
import za.co.retrorabbit.gameofthrones.models.DataViewModel
import za.co.retrorabbit.gameofthrones.models.House
import za.co.retrorabbit.gameofthrones.models.IDataViewModel
import za.co.retrorabbit.gameofthrones.models.IDataViewModelList
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

fun <T> getData(endpoint: Call<T>?, model: IDataViewModel<T>, factory: T) {

    endpoint?.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            val data: T = response.body() ?: factory

            model.onDataChange(data)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
        }
    })
}

fun <T> getData(endpoint: Call<List<T>>?, model: IDataViewModelList<T>) {

    endpoint?.enqueue(object : Callback<List<T>> {
        override fun onResponse(call: Call<List<T>>, response: Response<List<T>>) {
            val data: List<T> = response.body() ?: emptyList()

            model.onDataChange(data)
        }

        override fun onFailure(call: Call<List<T>>, t: Throwable) {
        }
    })
}

fun <T> getData(endpoint: Call<T>?, model: IDataViewModelList<T>, factory: T) {

    endpoint?.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            val data: T = response.body() ?: factory

            model.onDataAdd(data)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
        }
    })
}