/*
 * Copyright (c) 2018 NVISIA
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.nvisia.meetup.microservices.demo.config

import com.nvisia.meetup.microservices.demo.domain.model.Chaos
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

/**
 *     Parses input configuration from command line.
 *
 *     ./gradlew -Dserver.port=9001 -Dcom.nvisia.demo.chain=foo2,foo3,foo4 -Dcom.nvisia.demo.latency = foo1-5000,foo2-5000 -Dcom.nvisia.demo.exception=foo2
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
                .map { serviceId -> HostConfig(serviceId)
                }
    }

    private fun parsePortToTimeDelayMap(latencyConfig: String) : Map<String,Long> {
        //9003-5000,9002-5000
        return latencyConfig.split(",")
                .filter { token -> token.count {it == '-'} == 1}
                .map { token -> token.split("-")}
                .map { pair ->  pair[0] to pair[1].toLong() }
                .toMap()
    }

    private fun parseExceptionCallerPortNumbersSet(exceptionConfig: String) : Set<String> {
        return HashSet(exceptionConfig.split(',')
                .filter { !it.isEmpty() })
    }

    private fun toInstances(
            callers : List<HostConfig>,
            latencyMap: Map<String,Long>,
            exceptionCallers: Set<String>) : List<CallerInstance> {

        return callers.map { caller -> CallerInstance(caller,
                Chaos(latencyMap[caller.serviceId]?:0, exceptionCallers.contains(caller.serviceId)))
        }
    }
}