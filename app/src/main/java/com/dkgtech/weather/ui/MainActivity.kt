package com.dkgtech.weather.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dkgtech.weather.api.ApiInterface
import com.dkgtech.weather.databinding.ActivityMainBinding
import com.dkgtech.weather.model.WeatherModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {

            fetchWeatherData()


        }
    }

    private fun fetchWeatherData() {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)

        val response =
            retrofit.getWeatherData("jaipur", "217cd3c66b96e700acdc081385f76d53", "metrics")

        response.enqueue(object : Callback<WeatherModel> {
            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    with(binding) {
                        val temperature = responseBody.main.temp.toString()
                        val humidity = responseBody.main.humidity.toString()
                        val windSpeed = responseBody.wind.speed.toString()
                        val sunRise = responseBody.sys.sunrise.toString()
                        val sunSet = responseBody.sys.sunset.toString()
                        val seaLevel = responseBody.main.pressure.toString()
                        val condition = responseBody.weather.firstOrNull()?.main ?: "unknown"
                        val maxTemp = responseBody.main.temp_max.toString()
                        val minTemp = responseBody.main.temp_min.toString()

                    }
                }
            }

            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }
}