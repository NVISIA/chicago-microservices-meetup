/*
 *  Copyright (C) DAIS TECHNOLOGY, INC - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 */

package com.nvisia.meetup.microservices.demo.filter

import com.nvisia.meetup.microservices.demo.service.FooService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

/**
 * Test that Trace Id is added in response
 *
 * @author Julio Cesar Villalta III <jvillalta@nvisia.com>
 */
@SpringBootTest
@AutoConfigureMockMvc
class TraceIdFilterTest extends Specification {

    private static final String TRACE_ID_HEADER_KEY = 'X-B3-TraceId'
    private static final String HELLO_ENDPOINT = '/hello'

    @Autowired
    MockMvc mvc

    void 'Test Do filter - no trace id provided in request'() {
        when: 'hello endpoint called, should return successfully (Status 200)'
        MvcResult result = mvc.perform(get(HELLO_ENDPOINT))
                              .andExpect(MockMvcResultMatchers.status().is(200))
                              .andReturn()

        MockHttpServletResponse response = result.response

        then: '''inspect response headers, and ensure they contain trace id 
                header with non null value'''
        response.containsHeader(TRACE_ID_HEADER_KEY)
        response.getHeader(TRACE_ID_HEADER_KEY)
    }

    @TestConfiguration
    static class MockConfig {
        private DetachedMockFactory factory = new DetachedMockFactory()

        @Bean
        FooService fooService() {
            factory.Mock(FooService)
        }
    }
}
