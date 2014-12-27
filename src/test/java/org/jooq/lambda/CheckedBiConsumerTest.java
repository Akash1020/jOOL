/**
 * Copyright (c) 2014-2015, Data Geekery GmbH, contact@datageekery.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

        assertBiConsumer(test, UncheckedException.class);
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

        assertObjIntConsumer(test, UncheckedException.class);
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

        assertObjLongConsumer(test, UncheckedException.class);
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

        assertObjDoubleConsumer(test, UncheckedException.class);
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
