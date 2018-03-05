package com.nvisia.meetup.microservices.demo.config

import brave.Tracing
import mu.KLogging
import javax.inject.Inject
import javax.inject.Named
import javax.servlet.*
import javax.servlet.http.HttpServletRequest


@Named
class SleuthFilter @Inject constructor(private val tracing: Tracing) : Filter {

    companion object : KLogging()

    override fun init(filterConfig: FilterConfig?) {
        logger.info("init")
    }

    override fun destroy() {
        logger.info("destroy")
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        logger.debug("doFilter: {}",request)


        val context = tracing.propagation().extractor({
                req : HttpServletRequest, key -> req.getHeader(key) })
                .extract(request as HttpServletRequest)
        tracing.tracer().nextSpan(context).start()

        logger.debug("context: {}",context)

        chain.doFilter(request,response)
    }


}