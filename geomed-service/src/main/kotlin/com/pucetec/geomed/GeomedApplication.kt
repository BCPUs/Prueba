package com.pucetec.geomed

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GeomedApplication

fun main(args: Array<String>) {
    runApplication<GeomedApplication>(*args)
}
