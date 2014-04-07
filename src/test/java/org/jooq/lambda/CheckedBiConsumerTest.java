/**
 * Copyright (c) 2014, Data Geekery GmbH, contact@datageekery.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.lambda;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;

import static org.junit.Assert.*;

/**
 * @author Lukas Eder
 */
public class CheckedBiConsumerTest {

    @Test
    public void testCheckedBiConsumer() {
        BiConsumer<Object, Object> test = Unchecked.biConsumer(
            (o1, o2) -> {
                throw new Exception(o1 + ":" + o2);
            }
        );

        assertBiConsumer(test, RuntimeException.class);
    }

    @Test
    public void testCheckedBiConsumerWithCustomHandler() {
        BiConsumer<Object, Object> test = Unchecked.biConsumer(
            (o1, o2) -> {
                throw new Exception(o1 + ":" + o2);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertBiConsumer(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedObjIntConsumer() {
        ObjIntConsumer<Object> test = Unchecked.objIntConsumer(
            (o1, o2) -> {
                throw new Exception(o1 + ":" + o2);
            }
        );

        assertObjIntConsumer(test, RuntimeException.class);
    }

    @Test
    public void testCheckedObjIntConsumerWithCustomHandler() {
        ObjIntConsumer<Object> test = Unchecked.objIntConsumer(
            (o1, o2) -> {
                throw new Exception(o1 + ":" + o2);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertObjIntConsumer(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedObjLongConsumer() {
        ObjLongConsumer<Object> test = Unchecked.objLongConsumer(
            (o1, o2) -> {
                throw new Exception(o1 + ":" + o2);
            }
        );

        assertObjLongConsumer(test, RuntimeException.class);
    }

    @Test
    public void testCheckedObjLongConsumerWithCustomHandler() {
        ObjLongConsumer<Object> test = Unchecked.objLongConsumer(
            (o1, o2) -> {
                throw new Exception(o1 + ":" + o2);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertObjLongConsumer(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedObjDoubleConsumer() {
        ObjDoubleConsumer<Object> test = Unchecked.objDoubleConsumer(
            (o1, o2) -> {
                throw new Exception(o1 + ":" + o2);
            }
        );

        assertObjDoubleConsumer(test, RuntimeException.class);
    }

    @Test
    public void testCheckedObjDoubleConsumerWithCustomHandler() {
        ObjDoubleConsumer<Object> test = Unchecked.objDoubleConsumer(
            (o1, o2) -> {
                throw new Exception(o1 + ":" + o2);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertObjDoubleConsumer(test, IllegalStateException.class);
    }

    private <E extends RuntimeException> void assertBiConsumer(BiConsumer<Object, Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(null, null);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "null:null");
        }

        try {
            Map<String, Integer> map = new LinkedHashMap<>();
            map.put("a", 1);
            map.put("b", 2);
            map.put("c", 3);
            map.forEach(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "a:1");
        }
    }

    private <E extends RuntimeException> void assertObjIntConsumer(ObjIntConsumer<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(null, 0);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "null:0");
        }
    }

    private <E extends RuntimeException> void assertObjLongConsumer(ObjLongConsumer<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(null, 0L);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "null:0");
        }
    }

    private <E extends RuntimeException> void assertObjDoubleConsumer(ObjDoubleConsumer<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(null, 0.0);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "null:0.0");
        }
    }

    private <E extends RuntimeException> void assertException(Class<E> type, RuntimeException e, String message) {
        assertEquals(type, e.getClass());
        assertEquals(Exception.class, e.getCause().getClass());
        assertEquals(message, e.getCause().getMessage());
    }
}
