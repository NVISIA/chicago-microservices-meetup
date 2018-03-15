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
package com.nvisia.meetup.microservices.demo.filter

import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse

const val TRACE_ID_HEADER_KEY: String = "X-B3-TraceId"

/**
 * Filter to return Trace Id in Response Headers
 * Sleuth does not do this by default
 *
 * @author Julio Cesar Villalta III <jvillalta@nvisia.com>
 */
@Component
class TraceIdFilter : GenericFilterBean() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpServletResponse: HttpServletResponse = response as (HttpServletResponse)

        //get the value of traceId placed into MDC
        val traceIdValue: String? = MDC.get(TRACE_ID_HEADER_KEY)

        //if there is a value, set it in the response headers
        if (!StringUtils.isEmpty(traceIdValue)) {
            httpServletResponse.setHeader(TRACE_ID_HEADER_KEY, traceIdValue)
        }
        chain.doFilter(request, response)
    }
}

