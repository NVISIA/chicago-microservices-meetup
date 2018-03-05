package com.nvisia.meetup.microservices.demo.config

import brave.Tracing
import feign.RequestInterceptor
import feign.RequestTemplate
import mu.KLogging


class SleuthFeignInterceptor(private val tracing: Tracing) : RequestInterceptor {

    companion object : KLogging()

    override fun apply(template: RequestTemplate) {
        val traceContext = tracing.currentTraceContext().get()
        logger.debug("traceContext={}",traceContext)

        tracing.propagation().injector( {carrier : RequestTemplate,key,value ->
            logger.trace("Writing sleuth trace header: {}={}",key,value)
            template.header(key,value)
        }).inject(traceContext,template)
    }

}