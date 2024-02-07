package com.example.app_meteo.utils

    import java.time.Instant
    import java.time.LocalDateTime
    import java.time.ZoneId
    import java.time.format.DateTimeFormatter

    object  Change {
        fun kelvinToCelsius(kelvin: Double): Double {
            return kelvin - 273.15
        }

        fun metersPerSecondToKmPerHour(metersPerSecond: Double): Double {
            return metersPerSecond * 3.6
        }

        fun unixTimestampToLocalTime(timestamp: Long): String {
            val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.of("Europe/Paris"))
            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
            return localDateTime.format(formatter)
        }

        fun timestampToReadableDate(timestamp: Long): String {
            val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.of("Europe/Paris"))
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            return localDateTime.format(formatter)
        }

        fun formatDates(date: String): String {
            return "${date.substring(8, 10)}/${date.substring(5, 7)}"

        }

        fun weatherCodetoIcon( weathecode : Int ) : String
        {
             if(weathecode in 1..49) {
                 return "@drawable/cloudy_sunny.png"
             }
            else if(weathecode in 51..99)
             {
                return  "@drawable/rain.png"
            }
            else{
                return  "@drawable/sun.png"
             }

        }
    }
