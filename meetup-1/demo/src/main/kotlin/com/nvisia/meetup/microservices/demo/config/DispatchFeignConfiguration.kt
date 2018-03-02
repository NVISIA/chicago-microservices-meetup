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
import com.nvisia.meetup.microservices.demo.service.FooApiChain
import feign.Feign
import feign.Logger
import mu.KLogging
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory
import org.springframework.cloud.openfeign.FeignClientsConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DispatchFeignConfiguration : FeignClientsConfiguration() {

    companion object : KLogging()

    @Bean fun fooApiChain(
            callerConfig : CallerConfig,chaosInterceptorFactory : ChaosInterceptorFactory,
            clientFactory : OkHttpClientFactory) : FooApiChain {
        val clients = mutableListOf<FooApi>()

        //https://cloud.spring.io/spring-cloud-static/Camden.SR6/#_creating_feign_clients_manually
        for(caller in callerConfig.callerInstances) {
            val callerUrl = urlForConfig(caller)
            logger.info("Configuring {}",callerUrl)

            val builder = baseBuilder(clientFactory)
            for(interceptor in chaosInterceptorFactory.requestInterceptorsFor(caller.chaos)) {
                builder.requestInterceptor(interceptor)
            }

            clients.add(builder.target(FooApi::class.java,callerUrl))
        }

        return FooApiChain(clients.toList())
    }

    private fun urlForConfig(callerInstance: CallerInstance) : String {
        val hostConfig = callerInstance.hostConfig
        return "${hostConfig.scheme}://${hostConfig.hostName}:${hostConfig.port}"
    }

    private fun  baseBuilder(clientFactory : OkHttpClientFactory) : Feign.Builder {
        val client = clientFactory.createBuilder(true).build()

        return feignBuilder(feignRetryer())
                .encoder(feignEncoder())
                .decoder(feignDecoder())
                .contract(feignContract(feignConversionService()))
                .logLevel(Logger.Level.FULL)
                .client(feign.okhttp.OkHttpClient(client))
    }

}