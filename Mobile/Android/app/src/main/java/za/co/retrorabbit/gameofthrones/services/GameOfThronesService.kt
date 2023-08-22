package za.co.retrorabbit.gameofthrones.services

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import za.co.retrorabbit.gameofthrones.models.Book
import za.co.retrorabbit.gameofthrones.models.House
import za.co.retrorabbit.gameofthrones.models.IDataViewModel
import za.co.retrorabbit.gameofthrones.models.IDataViewModelList
import za.co.retrorabbit.gameofthrones.models.Person

interface GameOfThronesService {

    companion object {
        var BASE_URL = "https://anapioficeandfire.com/api/"
    }

    @GET("houses?pageSize=20")
    fun getHouses(): Call<List<House>>?


    @GET("houses/{id}")
    fun getHouse(@Path("id") id: Int): Call<House>?

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
    }) ?: run {
        model.onDataChange(factory)
    }
}

fun <T> getData(endpoint: Call<T>?, model: IDataViewModelList<T>, factory: T) {

    endpoint?.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            val data: T = response.body() ?: factory

            model.onDataAdd(data)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
        }
    }) ?: run {
        model.onDataAdd(factory)
    }
}