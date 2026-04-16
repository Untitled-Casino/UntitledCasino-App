package com.example.untitledcasino

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform