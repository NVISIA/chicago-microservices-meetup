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
import feign.Client
import feign.Feign
import feign.Logger
import feign.slf4j.Slf4jLogger
import mu.KLogging
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory
import org.springframework.cloud.openfeign.FeignClientsConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.reflect.KClass

@Configuration
class DispatchFeignConfiguration : FeignClientsConfiguration() {

    companion object : KLogging()

    @Bean fun fooApiChain(
            client : Client,
            callerConfig : CallerConfig,chaosInterceptorFactory : ChaosInterceptorFactory,
            clientFactory : OkHttpClientFactory) : FooApiChain {
        val clients = mutableListOf<FooApi>()

        //https://cloud.spring.io/spring-cloud-static/Camden.SR6/#_creating_feign_clients_manually
        for(caller in callerConfig.callerInstances) {
            val callerUrl = urlForConfig(caller)
            logger.info("Configuring {}",callerUrl)

            val builder = baseBuilder(client,FooApi::class)

            for(interceptor in chaosInterceptorFactory.requestInterceptorsFor(caller.chaos)) {
                builder.requestInterceptor(interceptor)
            }

            clients.add(builder.target(FooApi::class.java,callerUrl))
        }

        return FooApiChain(clients.toList())
    }

    private fun urlForConfig(callerInstance: CallerInstance) : String {
        val hostConfig = callerInstance.hostConfig
        return "http://${hostConfig.serviceId}"
    }

    private fun  baseBuilder(client : Client,target: KClass<FooApi>) : Feign.Builder {
        return feignBuilder(feignRetryer())
                .encoder(feignEncoder())
                .decoder(feignDecoder())
                .contract(feignContract(feignConversionService()))
                .logger(Slf4jLogger(target.qualifiedName))
                .logLevel(Logger.Level.FULL)
                .client(client)
    }

//    @Bean
//    @Scope("prototype")
//    fun feignBuilder(client: Client) : Feign.Builder  {
//        return baseBuilder(client)
//    }

}