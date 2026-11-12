package com.uniconet.app.network


object ApiConfig {
    const val ELASTIC_IP: String = "52.7.137.127"
    const val PORT: String = ":8080"
    const val BASE_URL: String = "http://$ELASTIC_IP$PORT/api/"

    const val TIMEOUT_SECONDS: Long = 15
    const val CONNECT_TIMEOUT_SECONDS: Long = 10
    const val READ_TIMEOUT_SECONDS: Long = 15
}
