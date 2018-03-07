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
package com.nvisia.meetup.microservices.demo.service

import com.nvisia.meetup.microservices.demo.domain.model.Chaos
import com.nvisia.meetup.microservices.demo.domain.model.Foo
import mu.KLogging
import javax.inject.Inject
import javax.inject.Named

@Named
class FooServiceImpl @Inject constructor(private val fooApiChain: FooApiChain) : FooService  {

    companion object : KLogging()

    override fun execute(chaos : Chaos): Foo {
        logger.debug("execute: {}",chaos)

        if(chaos.latencyMilliseconds > 0) {
            Thread.sleep(chaos.latencyMilliseconds)
        }
        
        for(fooapi in fooApiChain.instances) {
            fooapi.helloFoo()
        }

        if(chaos.exception) {
            throw RuntimeException("EPIC FAILURE!!!")
        }

        return Foo()
    }
}
