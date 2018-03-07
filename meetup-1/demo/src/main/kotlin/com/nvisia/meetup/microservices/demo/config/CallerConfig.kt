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
 *     ./gradlew -Dserver.port=9001 -Dcom.nvisia.demo.chain=foo2,foo3,foo4 -Dcom.nvisia.demo.latency=5000 -Dcom.nvisia.demo.exception=true
 */
@Configuration
class CallerConfig {

    val callerInstances : List<CallerInstance>
    val chaos: Chaos

    constructor(
            @Value("\${com.nvisia.demo.chain:}")  callChainString : String,
            @Value("\${com.nvisia.demo.latency:}") latencyConfig : String,
            @Value("\${com.nvisia.demo.exception:}") exceptionConfig : String) {

        callerInstances = toInstances(
                parseCallersList(callChainString))
        chaos = Chaos(parseLatency(latencyConfig),isException(exceptionConfig))
    }

    private fun parseCallersList(callChainString: String) : List<HostConfig> {
        return callChainString.split(',')
                .filter { !it.isEmpty() }
                .map { HostConfig(it) }
    }

    private fun parseLatency(latencyConfig: String) : Long {
        return if (!latencyConfig.isEmpty()) latencyConfig.toLong() else 0
    }

    private fun isException(exceptionConfig: String) : Boolean {
        return if (!exceptionConfig.isEmpty()) exceptionConfig.toBoolean() else false
    }

    private fun toInstances(
            callers : List<HostConfig>) : List<CallerInstance> {
            return callers.map { CallerInstance(it) }
    }
}