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

import com.nvisia.meetup.microservices.demo.domain.api.FooApi
import com.nvisia.meetup.microservices.demo.domain.client.*
import com.nvisia.meetup.microservices.demo.service.FooApiChain
import mu.KLogging
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(basePackageClasses = [Foo1Client::class])
class DispatchFeignConfiguration {

    companion object : KLogging()

    @Bean
    fun fooApiChain(foo1: Foo1Client,
                    foo2: Foo2Client,
                    foo3: Foo3Client,
                    foo4: Foo4Client,
                    foo5: Foo5Client,
                    callerConfig: CallerConfig): FooApiChain {
        val clients = mutableListOf<FooApi>()

        for (caller in callerConfig.callerInstances) {
            when (caller.hostConfig.serviceId) {
                "foo1" -> clients.add(foo1)
                "foo2" -> clients.add(foo2)
                "foo3" -> clients.add(foo3)
                "foo4" -> clients.add(foo4)
                "foo5" -> clients.add(foo5)
            }
        }

        return FooApiChain(clients.toList())
    }
}
