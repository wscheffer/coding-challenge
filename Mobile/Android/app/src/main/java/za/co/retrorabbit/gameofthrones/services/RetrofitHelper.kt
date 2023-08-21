package za.co.retrorabbit.gameofthrones.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    private val gameOfThronesService: GameOfThronesService

    init {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(GameOfThronesService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        gameOfThronesService = retrofit.create(GameOfThronesService::class.java)
    }

    fun getGameOfThronesService(): GameOfThronesService {
        return gameOfThronesService
    }

    companion object {
        var instance = RetrofitClient()
    }
}
