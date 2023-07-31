import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*

private const val BASE_URL = "http://kotlin-book.bignerdranch.com/2e"
private const val FLIGHT_ENDPOINT = "$BASE_URL/flight"
private const val LOYALTY_ENDPOINT = "$BASE_URL/loyalty"

fun main() {

    val passengersPerFlight = 75
    val numberOfLight = 1000
    val checkedInPassengers = atomic(0)

    runBlocking {
        repeat(numberOfLight) {
            launch(Dispatchers.Default) {
                checkedInPassengers += passengersPerFlight
            }
        }
    }

    println(checkedInPassengers)

        runBlocking {
            println("Started")
            launch {
                val flight = fetchFlight("Madrigal")
                println(flight)
            }
            println("Finished")
        }
}


suspend fun fetchFlight(passengerName: String): FlightStatus = coroutineScope {
    val client = HttpClient(CIO)
    val flightResponse = async {
        println("Started flightResponse")
        client.get<String>(FLIGHT_ENDPOINT).also {
            println("Finished flightResponse")
        }
    }
    val loyaltyResponse = async {
        println("Started loyaltyResponse")
        client.get<String>(LOYALTY_ENDPOINT).also {
            println("Finished loyaltyResponse")
        }
    }
    delay(500)
    println("Combining flight data")
    FlightStatus.parse(
        passengerName = passengerName,
        flightResponse = flightResponse.await(),
        loyaltyResponse = loyaltyResponse.await()
    )
}