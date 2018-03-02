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
import org.junit.Assert
import org.junit.Test


class CallerConfigTest {

    @Test
    fun testCallerConfigEmpty() {
        val config = CallerConfig("","","")

        Assert.assertTrue(config.callerInstances.isEmpty())
    }

    @Test
    fun testCallerConfigSingleInstanceAndNoExceptionsAndNoLatencies() {
        val config = CallerConfig("9001","","")

        Assert.assertEquals(
                listOf( CallerInstance(HostConfig("http","localhost",9001), Chaos(0,false))),
                config.callerInstances)
    }

    @Test
    fun testCallerConfigMultipleInstancesAndNoExceptionsAndNoLatencies() {
        val config = CallerConfig("9001,9002,9003","","")

        Assert.assertEquals(
                listOf( CallerInstance(HostConfig("http","localhost",9001),Chaos(0,false)),
                        CallerInstance(HostConfig("http","localhost",9002),Chaos(0,false)),
                        CallerInstance(HostConfig("http","localhost",9003),Chaos(0,false))
                ),
                config.callerInstances)

    }

    @Test
    fun testCallerConfigMultipleInstancesAndNoExceptionsAndSomeLatencies() {
        val config = CallerConfig("9001,9002,9003","9002-30000","")

        Assert.assertEquals(
                listOf( CallerInstance(HostConfig("http","localhost",9001),Chaos(0,false)),
                        CallerInstance(HostConfig("http","localhost",9002),Chaos(30000,false)),
                        CallerInstance(HostConfig("http","localhost",9003),Chaos(0,false))
                ),
                config.callerInstances)

    }

    @Test
    fun testCallerConfigMultipleInstancesAndSomeExceptionsAndNoLatencies() {
        val config = CallerConfig("9001,9002,9003","","9002")

        Assert.assertEquals(
                listOf( CallerInstance(HostConfig("http","localhost",9001),Chaos(0,false)),
                        CallerInstance(HostConfig("http","localhost",9002),Chaos(0,true)),
                        CallerInstance(HostConfig("http","localhost",9003),Chaos(0,false))
                ),
                config.callerInstances)

    }

    @Test
    fun testCallerConfigMultipleInstancesAndSomeExceptionsAndSomeLatencies() {
        val config = CallerConfig("9001,9002,9003","9001-30000,9002-40000","9003")

        Assert.assertEquals(
                listOf( CallerInstance(HostConfig("http","localhost",9001),Chaos(30000,false)),
                        CallerInstance(HostConfig("http","localhost",9002),Chaos(40000,false)),
                        CallerInstance(HostConfig("http","localhost",9003),Chaos(0,true))
                ),
                config.callerInstances)

    }
}