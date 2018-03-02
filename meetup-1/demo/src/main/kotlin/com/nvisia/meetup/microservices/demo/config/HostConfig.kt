package com.nvisia.meetup.microservices.demo.config


data class HostConfig(
        val scheme: String,
        val hostName: String,
        val port: Int)