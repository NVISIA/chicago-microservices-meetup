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

import com.nvisia.meetup.microservices.demo.config.properties.CallerProperties
import com.nvisia.meetup.microservices.demo.domain.model.Chaos
import org.junit.Assert
import org.junit.Test


class CallerConfigTest {

    @Test
    fun testCallerConfigEmpty() {
        val callerProperties = CallerProperties(emptyList(), 0, false)
        val config = CallerConfig(callerProperties)
        Assert.assertTrue(config.callerInstances.isEmpty())
    }

    @Test
    fun testCallerConfigSingleInstanceAndNoExceptionsAndNoLatencies() {
        val callerProperties = CallerProperties(listOf("foo2"), 0, false)
        val config = CallerConfig(callerProperties)
        Assert.assertEquals(listOf(CallerInstance(HostConfig("foo2"))), config.callerInstances)
        Assert.assertEquals(Chaos(0, false), config.chaos)
    }

    @Test
    fun testCallerConfigMultipleInstancesAndNoExceptionsAndNoLatencies() {
        val callerProperties = CallerProperties(listOf("foo2", "foo3", "foo4"), 0, false)
        val config = CallerConfig(callerProperties)
        Assert.assertEquals(
            config.callerInstances,
            listOf(CallerInstance(HostConfig("foo2")),
                CallerInstance(HostConfig("foo3")),
                CallerInstance(HostConfig("foo4")))
        )

        Assert.assertEquals(Chaos(0, false), config.chaos)
    }

    @Test
    fun testCallerConfigMultipleInstancesAndNoExceptionsAndSomeLatencies() {
        val callerProperties = CallerProperties(listOf("foo2", "foo3", "foo4"), 30000, false)
        val config = CallerConfig(callerProperties)

        Assert.assertEquals(
            config.callerInstances,
            listOf(CallerInstance(HostConfig("foo2")),
                CallerInstance(HostConfig("foo3")),
                CallerInstance(HostConfig("foo4")))
        )
        Assert.assertEquals(Chaos(30000, false), config.chaos)
    }

    @Test
    fun testCallerConfigMultipleInstancesAndSomeExceptionsAndNoLatencies() {
        val callerProperties = CallerProperties(listOf("foo2", "foo3", "foo4"), 0, true)
        val config = CallerConfig(callerProperties)

        Assert.assertEquals(
            config.callerInstances,
            listOf(CallerInstance(HostConfig("foo2")),
                CallerInstance(HostConfig("foo3")),
                CallerInstance(HostConfig("foo4")))
        )

        Assert.assertEquals(Chaos(0, true), config.chaos)
    }

    @Test
    fun testCallerConfigMultipleInstancesAndSomeExceptionsAndSomeLatencies() {
        val callerProperties = CallerProperties(listOf("foo2", "foo3", "foo4"), 40000, true)
        val config = CallerConfig(callerProperties)

        Assert.assertEquals(
            config.callerInstances,
            listOf(CallerInstance(HostConfig("foo2")),
                CallerInstance(HostConfig("foo3")),
                CallerInstance(HostConfig("foo4")))
        )

        Assert.assertEquals(Chaos(40000, true), config.chaos)
    }
}
