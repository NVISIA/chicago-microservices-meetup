package com.nvisia.meetup.microservices.demo.config

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
                listOf( CallerInstance(HostConfig("http","localhost",9001),0,false)),
                config.callerInstances)
    }

    @Test
    fun testCallerConfigMultipleInstancesAndNoExceptionsAndNoLatencies() {
        val config = CallerConfig("9001,9002,9003","","")

        Assert.assertEquals(
                listOf( CallerInstance(HostConfig("http","localhost",9001),0,false),
                        CallerInstance(HostConfig("http","localhost",9002),0,false),
                        CallerInstance(HostConfig("http","localhost",9003),0,false)
                ),
                config.callerInstances)

    }

    @Test
    fun testCallerConfigMultipleInstancesAndNoExceptionsAndSomeLatencies() {
        val config = CallerConfig("9001,9002,9003","9002-30000","")

        Assert.assertEquals(
                listOf( CallerInstance(HostConfig("http","localhost",9001),0,false),
                        CallerInstance(HostConfig("http","localhost",9002),30000,false),
                        CallerInstance(HostConfig("http","localhost",9003),0,false)
                ),
                config.callerInstances)

    }

    @Test
    fun testCallerConfigMultipleInstancesAndSomeExceptionsAndNoLatencies() {
        val config = CallerConfig("9001,9002,9003","","9002")

        Assert.assertEquals(
                listOf( CallerInstance(HostConfig("http","localhost",9001),0,false),
                        CallerInstance(HostConfig("http","localhost",9002),0,true),
                        CallerInstance(HostConfig("http","localhost",9003),0,false)
                ),
                config.callerInstances)

    }

    @Test
    fun testCallerConfigMultipleInstancesAndSomeExceptionsAndSomeLatencies() {
        val config = CallerConfig("9001,9002,9003","9001-30000,9002-40000","9003")

        Assert.assertEquals(
                listOf( CallerInstance(HostConfig("http","localhost",9001),30000,false),
                        CallerInstance(HostConfig("http","localhost",9002),40000,false),
                        CallerInstance(HostConfig("http","localhost",9003),0,true)
                ),
                config.callerInstances)

    }
}