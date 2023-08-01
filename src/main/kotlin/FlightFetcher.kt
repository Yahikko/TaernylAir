import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.*

private const val BASE_URL = "http://kotlin-book.bignerdranch.com/2e"
private const val FLIGHT_ENDPOINT = "$BASE_URL/flight"
private const val LOYALTY_ENDPOINT = "$BASE_URL/loyalty"

private var status = ""

fun main() {

    runBlocking {
        launch {
            var flight: FlightStatus
            while (true) {
                flight = fetchFlight("Madrigal")
                if (status == "On Time" || status == "Delayed") {
                    break
                }
                println("Searching for another flight")
            }
            println(flight)
        }
    }
}


suspend fun fetchFlight(passengerName: String): FlightStatus = coroutineScope {
    val client = HttpClient(CIO)
    println("Started")
    val flightResponse = async {
        client.get<String>(FLIGHT_ENDPOINT).also {
        }
    }
    val loyaltyResponse = async {
        client.get<String>(LOYALTY_ENDPOINT).also {
        }
    }
    delay(500)
    val flightStatus = FlightStatus.parse(
        passengerName = passengerName,
        flightResponse = flightResponse.await(),
        loyaltyResponse = loyaltyResponse.await()
    )
    status = flightStatus.status
    flightStatus
}