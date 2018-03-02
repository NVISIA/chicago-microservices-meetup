package com.nvisia.meetup.microservices.demo.config


data class CallerInstance(
        val hostConfig: HostConfig,
        val latencyMilliesconds: Long,
        val exception: Boolean)