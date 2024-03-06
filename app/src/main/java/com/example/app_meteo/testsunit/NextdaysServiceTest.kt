package com.example.app_meteo.testsunit
import com.example.app_meteo.data.services.NextdaysService
import com.example.app_meteo.model.nextDaysModel.Daily
import com.example.app_meteo.model.nextDaysModel.DailyUnits
import com.example.app_meteo.model.nextDaysModel.NextDaysModel
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

class NextdaysServiceTest {

    private val nextdaysServiceSuccess: NextdaysService = createMockNextdaysService(successfulResponse = true)
    private val nextdaysServiceFailure: NextdaysService = createMockNextdaysService(successfulResponse = false)


    @Test
    fun test_getTheWeather_success() {
        runBlocking {
            val latitude = 37.7749
            val longitude = -122.4194
            val dailyParameters = listOf("temperature", "humidity")
            val timezone = "PST"
            val forecastDays = 5

            val expectedResponse = NextDaysModel(
                latitude = latitude,
                longitude = longitude,
                generationtimeMs = 123456789.0,
                utcOffsetSeconds = -28800,
                timezone = timezone,
                timezoneAbbreviation = "PST",
                elevation = 0,
                dailyUnits = DailyUnits(
                    time = "2024-03-05",
                    weathercode = "sunny",
                    temperature2mMax = "25",
                    temperature2mMin = "15",
                    sunrise = "06:00",
                    sunset = "18:00"
                ),
                daily = Daily(
                    time = arrayListOf("2024-03-05"),
                    weathercode = arrayListOf(800),
                    temperature_2m_max = arrayListOf(25.0),
                    temperature_2m_min = arrayListOf(15.0),
                    sunrise = arrayListOf("06:00"),
                    sunset = arrayListOf("18:00")
                )
            )

            val response = nextdaysServiceSuccess.getTheWeather(latitude, longitude, dailyParameters, timezone, forecastDays)

            assertEquals(expectedResponse, response.body())
        }
    }

    @Test
    fun test_getTheWeather_failure() {
        runBlocking {
            val latitude = 37.7749
            val longitude = -122.4194
            val dailyParameters = listOf("temperature", "humidity")
            val timezone = "PST"
            val forecastDays = 5

            val errorResponseBody = ResponseBody.create(MediaType.parse("application/json"), "{\"message\": \"Internal Server Error\"}")
            val errorResponse = Response.error<NextDaysModel>(500, errorResponseBody)

            val response = nextdaysServiceFailure.getTheWeather(latitude, longitude, dailyParameters, timezone, forecastDays)

            assertEquals(errorResponse.code(), response.code())
            assertEquals(errorResponse.errorBody()?.string(), response.errorBody()?.string())
        }
    }



    private fun createMockNextdaysService(successfulResponse: Boolean): NextdaysService {
        return object : NextdaysService {
            override suspend fun getTheWeather(
                latitude: Double,
                longitude: Double,
                dailyParameters: List<String>,
                timezone: String,
                forecastDays: Int
            ): Response<NextDaysModel> {
                if (successfulResponse) {
                    // Simuler une réponse réussie avec des données factices
                    val mockResponse = NextDaysModel(
                        latitude = latitude,
                        longitude = longitude,
                        generationtimeMs = 123456789.0,
                        utcOffsetSeconds = -28800,
                        timezone = timezone,
                        timezoneAbbreviation = "PST",
                        elevation = 0,
                        dailyUnits = DailyUnits(
                            time = "2024-03-05",
                            weathercode = "sunny",
                            temperature2mMax = "25",
                            temperature2mMin = "15",
                            sunrise = "06:00",
                            sunset = "18:00"
                        ),
                        daily = Daily(
                            time = arrayListOf("2024-03-05"),
                            weathercode = arrayListOf(800),
                            temperature_2m_max = arrayListOf(25.0),
                            temperature_2m_min = arrayListOf(15.0),
                            sunrise = arrayListOf("06:00"),
                            sunset = arrayListOf("18:00")
                        )
                    )
                    return Response.success(mockResponse)
                } else {
                    // Simuler une réponse d'erreur avec le code de statut 500
                    val errorResponseBody = ResponseBody.create(
                        MediaType.parse("application/json"),
                        "{\"message\": \"Internal Server Error\"}"
                    )
                    return Response.error(500, errorResponseBody)
                }
            }
        }
    }

}
