package com.nvisia.meetup.microservices.demo.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

/**
 *     Parses input configuration from command line.
 *
 *     ./gradlew -Dserver.port=9001 -Dcom.nvisia.demo.chain=9002,9003,9004 -Dcom.nvisia.demo.latency = 9003-5000,9002-5000 -Dcom.nvisia.demo.exception=9004
 */
@Configuration
class CallerConfig {

    val callerInstances : List<CallerInstance>

    constructor(
            @Value("\${com.nvisia.demo.chain:}")  callChainString : String,
            @Value("\${com.nvisia.demo.latency:}") latencyConfig : String,
            @Value("\${com.nvisia.demo.exception:}") exceptionConfig : String) {

        callerInstances = toInstances(
                parseCallersList(callChainString),
                parsePortToTimeDelayMap(latencyConfig),
                parseExceptionCallerPortNumbersSet(exceptionConfig))
    }

    private fun parseCallersList(callChainString: String) : List<HostConfig> {
        return callChainString.split(',')
                .filter { !it.isEmpty() }
                .map { portString ->
                    HostConfig("http","localhost",portString.toInt())
                }
    }

    private fun parsePortToTimeDelayMap(latencyConfig: String) : Map<Int,Long> {
        //9003-5000,9002-5000
        return latencyConfig.split(",")
                .filter { token -> token.count {it == '-'} == 1}
                .map { token -> token.split("-")}
                .map { pair ->  pair[0].toInt() to pair[1].toLong() }
                .toMap()
    }

    private fun parseExceptionCallerPortNumbersSet(exceptionConfig: String) : Set<Int> {
        return HashSet(exceptionConfig.split(',')
                .filter { !it.isEmpty() }
                .map { string -> string.toInt()})
    }

    private fun toInstances(
            callers : List<HostConfig>,
            latencyMap: Map<Int,Long>,
            exceptionCallers: Set<Int>) : List<CallerInstance> {

        return callers.map { caller -> CallerInstance(caller, latencyMap[caller.port]?:0, exceptionCallers.contains(caller.port))}
    }
}