package com.dkgtech.weather.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import com.dkgtech.weather.R
import com.dkgtech.weather.api.ApiInterface
import com.dkgtech.weather.databinding.ActivityMainBinding
import com.dkgtech.weather.model.WeatherModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {

            fetchWeatherData("Mumbai")
            searchCity()

        }
    }

    private fun searchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun fetchWeatherData(cityName: String) {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)

        val response =
            retrofit.getWeatherData("$cityName", "217cd3c66b96e700acdc081385f76d53", "metrics")

        response.enqueue(object : Callback<WeatherModel> {
            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    with(binding) {
                        val temperature = responseBody.main.temp.toString()
                        val humidity = responseBody.main.humidity.toString()
                        val windSpeed = responseBody.wind.speed.toString()
                        val sunRise = responseBody.sys.sunrise.toLong()
                        val sunSet = responseBody.sys.sunset.toLong()
                        val seaLevel = responseBody.main.pressure.toString()
                        val condition = responseBody.weather.firstOrNull()?.main ?: "unknown"
                        val maxTemp = responseBody.main.temp_max.toString()
                        val minTemp = responseBody.main.temp_min.toString()

                        txtCity.text = responseBody.name
                        txtTemp.text = "$temperature °C"
                        txtWeather.text = condition
                        txtMaxTemp.text = "Max : $maxTemp °C"
                        txtMinTemp.text = "Min : $minTemp °C"
                        txtHumidity.text = "$humidity %"
                        txtWindSpeed.text = "$windSpeed m/s"
                        txtSunrise.text = "${time(sunRise)}"
                        txtSunset.text = "${time(sunSet)}"
                        txtSea.text = "$seaLevel hPa"
                        txtCondition.text = condition
                        txtDayName.text = dayName(System.currentTimeMillis())
                        txtDate.text = date()
                        txtCity.text = cityName

                        changeWeatherAccordingToCondition(condition)

                    }
                }
            }

            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun changeWeatherAccordingToCondition(conditions: String) {
        when (conditions) {
            "Clear Sky", "Sunny", "Clear", "Smoke" -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

            "Partly Clouds", "Clouds", "Overcast", "Mist", "Foggy" -> {
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }

            "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Rain", "Heavy Rain" -> {
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }

            "Light Snow", "Moderate Snow", "Heavy Snow", "Snow", "Blizzard" -> {
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }

            else -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
        }
        binding.lottieAnimationView.playAnimation()
    }

    fun dayName(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date())
    }

    fun time(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp * 1000))
    }

    fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

}