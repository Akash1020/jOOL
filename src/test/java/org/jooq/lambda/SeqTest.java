/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.lambda;

import org.jooq.lambda.tuple.Tuple2;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static org.jooq.lambda.tuple.Tuple.tuple;
import static org.junit.Assert.assertEquals;

/**
 * @author Lukas Eder
 */
public class SeqTest {

    @Test
    public void testZipEqualLength() {
        List<Tuple2<Integer, String>> list = Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c")).toList();

        assertEquals(3, list.size());
        assertEquals(1, (int) list.get(0).v1);
        assertEquals(2, (int) list.get(1).v1);
        assertEquals(3, (int) list.get(2).v1);
        assertEquals("a", list.get(0).v2);
        assertEquals("b", list.get(1).v2);
        assertEquals("c", list.get(2).v2);
    }

    @Test
    public void testZipDifferingLength() {
        List<Tuple2<Integer, String>> list = Seq.of(1, 2).zip(Seq.of("a", "b", "c", "d")).toList();

        assertEquals(2, list.size());
        assertEquals(1, (int) list.get(0).v1);
        assertEquals(2, (int) list.get(1).v1);
        assertEquals("a", list.get(0).v2);
        assertEquals("b", list.get(1).v2);
    }

    @Test
    public void testZipWithIndex() {
        assertEquals(asList(), Seq.of().zipWithIndex().toList());
        assertEquals(asList(tuple("a", 0L)), Seq.of("a").zipWithIndex().toList());
        assertEquals(asList(tuple("a", 0L), tuple("b", 1L)), Seq.of("a", "b").zipWithIndex().toList());
        assertEquals(asList(tuple("a", 0L), tuple("b", 1L), tuple("c", 2L)), Seq.of("a", "b", "c").zipWithIndex().toList());
    }

    @Test
    public void testDuplicate() {
        Supplier<Tuple2<Seq<Integer>, Seq<Integer>>> reset = () -> Seq.of(1, 2, 3).duplicate();
        Tuple2<Seq<Integer>, Seq<Integer>> duplicate;

        duplicate = reset.get();
        assertEquals(asList(1, 2, 3), duplicate.v1.toList());
        assertEquals(asList(1, 2, 3), duplicate.v2.toList());

        duplicate = reset.get();
        assertEquals(asList(1, 2, 3, 1, 2, 3), duplicate.v1.concat(duplicate.v2).toList());

        duplicate = reset.get();
        assertEquals(asList(tuple(1, 1), tuple(2, 2), tuple(3, 3)), duplicate.v1.zip(duplicate.v2).toList());
    }

    @Test
    public void testIntersperse() {
        assertEquals(asList(), Seq.of().intersperse(0).toList());
        assertEquals(asList(1), Seq.of(1).intersperse(0).toList());
        assertEquals(asList(1, 0, 2), Seq.of(1, 2).intersperse(0).toList());
        assertEquals(asList(1, 0, 2, 0, 3), Seq.of(1, 2, 3).intersperse(0).toList());
    }

    @Test
    public void testToString() throws IOException {
        assertEquals("123", Seq.of(1, 2, 3).toString());
        assertEquals("1, 2, 3", Seq.of(1, 2, 3).toString(", "));
        assertEquals("1, null, 3", Seq.of(1, null, 3).toString(", "));
    }

    @Test
    public void testSlice() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        assertEquals(asList(3, 4, 5), s.get().slice(2, 5).toList());
        assertEquals(asList(4, 5, 6), s.get().slice(3, 6).toList());
        assertEquals(asList(), s.get().slice(4, 1).toList());
        assertEquals(asList(1, 2, 3, 4, 5, 6), s.get().slice(0, 6).toList());
        assertEquals(asList(1, 2, 3, 4, 5, 6), s.get().slice(-1, 6).toList());
        assertEquals(asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), s.get().slice(-1, 12).toList());
    }

    @Test
    public void testToCollection() {
        assertEquals(asList(1, 2, 2, 3), Seq.of(1, 2, 2, 3).toCollection(LinkedList::new));
    }

    @Test
    public void testToList() {
        assertEquals(asList(1, 2, 2, 3), Seq.of(1, 2, 2, 3).toList());
    }

    @Test
    public void testToSet() {
        assertEquals(new HashSet<>(asList(1, 2, 3)), Seq.of(1, 2, 2, 3).toSet());
    }

    @Test
    public void testToMap() {
        Map<String, Integer> expected = new HashMap<>();
        expected.put("a", 1);
        expected.put("b", 2);
        expected.put("c", 3);
        assertEquals(expected, Seq.of(tuple("a", 1), tuple("b", 2), tuple("c", 3)).toMap(Tuple2::v1, Tuple2::v2));
    }

    @Test
    public void testSkipWhile() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 2, 3, 4, 5);

        assertEquals(asList(1, 2, 3, 4, 5), s.get().skipWhile(i -> false).toList());
        assertEquals(asList(3, 4, 5), s.get().skipWhile(i -> i % 3 != 0).toList());
        assertEquals(asList(3, 4, 5), s.get().skipWhile(i -> i < 3).toList());
        assertEquals(asList(4, 5), s.get().skipWhile(i -> i < 4).toList());
        assertEquals(asList(), s.get().skipWhile(i -> true).toList());
    }

    @Test
    public void testSkipUntil() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 2, 3, 4, 5);

        assertEquals(asList(), s.get().skipUntil(i -> false).toList());
        assertEquals(asList(3, 4, 5), s.get().skipUntil(i -> i % 3 == 0).toList());
        assertEquals(asList(3, 4, 5), s.get().skipUntil(i -> i == 3).toList());
        assertEquals(asList(4, 5), s.get().skipUntil(i -> i == 4).toList());
        assertEquals(asList(1, 2, 3, 4, 5), s.get().skipUntil(i -> true).toList());
    }

    @Test
    public void testLimitWhile() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 2, 3, 4, 5);

        assertEquals(asList(), s.get().limitWhile(i -> false).toList());
        assertEquals(asList(1, 2), s.get().limitWhile(i -> i % 3 != 0).toList());
        assertEquals(asList(1, 2), s.get().limitWhile(i -> i < 3).toList());
        assertEquals(asList(1, 2, 3), s.get().limitWhile(i -> i < 4).toList());
        assertEquals(asList(1, 2, 3, 4, 5), s.get().limitWhile(i -> true).toList());
    }

    @Test
    public void testLimitUntil() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 2, 3, 4, 5);

        assertEquals(asList(1, 2, 3, 4, 5), s.get().limitUntil(i -> false).toList());
        assertEquals(asList(1, 2), s.get().limitUntil(i -> i % 3 == 0).toList());
        assertEquals(asList(1, 2), s.get().limitUntil(i -> i == 3).toList());
        assertEquals(asList(1, 2, 3), s.get().limitUntil(i -> i == 4).toList());
        assertEquals(asList(), s.get().limitUntil(i -> true).toList());
    }

    @Test
    public void testPartition() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 2, 3, 4, 5, 6);

        assertEquals(asList(1, 3, 5), s.get().partition(i -> i % 2 != 0).v1.toList());
        assertEquals(asList(2, 4, 6), s.get().partition(i -> i % 2 != 0).v2.toList());

        assertEquals(asList(2, 4, 6), s.get().partition(i -> i % 2 == 0).v1.toList());
        assertEquals(asList(1, 3, 5), s.get().partition(i -> i % 2 == 0).v2.toList());

        assertEquals(asList(1, 2, 3), s.get().partition(i -> i <= 3).v1.toList());
        assertEquals(asList(4, 5, 6), s.get().partition(i -> i <= 3).v2.toList());

        assertEquals(asList(1, 2, 3, 4, 5, 6), s.get().partition(i -> true).v1.toList());
        assertEquals(asList(), s.get().partition(i -> true).v2.toList());

        assertEquals(asList(), s.get().partition(i -> false).v1.toList());
        assertEquals(asList(1, 2, 3, 4, 5, 6), s.get().partition(i -> false).v2.toList());
    }

    @Test
    public void testSplitAt() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 2, 3, 4, 5, 6);

        assertEquals(asList(), s.get().splitAt(0).v1.toList());
        assertEquals(asList(1, 2, 3, 4, 5, 6), s.get().splitAt(0).v2.toList());

        assertEquals(asList(1), s.get().splitAt(1).v1.toList());
        assertEquals(asList(2, 3, 4, 5, 6), s.get().splitAt(1).v2.toList());

        assertEquals(asList(1, 2, 3), s.get().splitAt(3).v1.toList());
        assertEquals(asList(4, 5, 6), s.get().splitAt(3).v2.toList());

        assertEquals(asList(1, 2, 3, 4, 5, 6), s.get().splitAt(6).v1.toList());
        assertEquals(asList(), s.get().splitAt(6).v2.toList());

        assertEquals(asList(1, 2, 3, 4, 5, 6), s.get().splitAt(7).v1.toList());
        assertEquals(asList(), s.get().splitAt(7).v2.toList());
    }

    @Test
    public void testSplitAtHead() {
        assertEquals(Optional.empty(), Seq.of().splitAtHead().v1);
        assertEquals(asList(), Seq.of().splitAtHead().v2.toList());

        assertEquals(Optional.of(1), Seq.of(1).splitAtHead().v1);
        assertEquals(asList(), Seq.of(1).splitAtHead().v2.toList());

        assertEquals(Optional.of(1), Seq.of(1, 2).splitAtHead().v1);
        assertEquals(asList(2), Seq.of(1, 2).splitAtHead().v2.toList());

        assertEquals(Optional.of(1), Seq.of(1, 2, 3).splitAtHead().v1);
        assertEquals(Optional.of(2), Seq.of(1, 2, 3).splitAtHead().v2.splitAtHead().v1);
        assertEquals(Optional.of(3), Seq.of(1, 2, 3).splitAtHead().v2.splitAtHead().v2.splitAtHead().v1);
        assertEquals(asList(2, 3), Seq.of(1, 2, 3).splitAtHead().v2.toList());
        assertEquals(asList(3), Seq.of(1, 2, 3).splitAtHead().v2.splitAtHead().v2.toList());
        assertEquals(asList(), Seq.of(1, 2, 3).splitAtHead().v2.splitAtHead().v2.splitAtHead().v2.toList());
    }

    @Test
    public void testMinByMaxBy() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 2, 3, 4, 5, 6);

        assertEquals(1, (int) s.get().maxBy(t -> Math.abs(t - 5)).get());
        assertEquals(5, (int) s.get().minBy(t -> Math.abs(t - 5)).get());

        assertEquals(6, (int) s.get().maxBy(t -> "" + t).get());
        assertEquals(1, (int) s.get().minBy(t -> "" + t).get());
    }

    @Test
    public void testUnzip() {
        Supplier<Seq<Tuple2<Integer, String>>> s = () -> Seq.of(tuple(1, "a"), tuple(2, "b"), tuple(3, "c"));

        Tuple2<Seq<Integer>, Seq<String>> u1 = Seq.unzip(s.get());
        assertEquals(asList(1, 2, 3), u1.v1.toList());
        assertEquals(asList("a", "b", "c"), u1.v2.toList());

        Tuple2<Seq<Integer>, Seq<String>> u2 = Seq.unzip(s.get(), v1 -> -v1, v2 -> v2 + "!");
        assertEquals(asList(-1, -2, -3), u2.v1.toList());
        assertEquals(asList("a!", "b!", "c!"), u2.v2.toList());

        Tuple2<Seq<Integer>, Seq<String>> u3 = Seq.unzip(s.get(), t -> tuple(-t.v1, t.v2 + "!"));
        assertEquals(asList(-1, -2, -3), u3.v1.toList());
        assertEquals(asList("a!", "b!", "c!"), u3.v2.toList());

        Tuple2<Seq<Integer>, Seq<String>> u4 = Seq.unzip(s.get(), (t1, t2) -> tuple(-t1, t2 + "!"));
        assertEquals(asList(-1, -2, -3), u4.v1.toList());
        assertEquals(asList("a!", "b!", "c!"), u4.v2.toList());
    }

    @Test
    public void testFold() {
        Supplier<Seq<String>> s = () -> Seq.of("a", "b", "c");

        assertEquals("abc", s.get().foldLeft("", String::concat));
        assertEquals("-a-b-c", s.get().foldLeft(new StringBuilder(), (u, t) -> u.append("-").append(t)).toString());
        assertEquals(3, (int) s.get().foldLeft(0, (u, t) -> u + t.length()));

        assertEquals("abc", s.get().foldRight("", String::concat));
        assertEquals("-c-b-a", s.get().foldRight(new StringBuilder(), (t, u) -> u.append("-").append(t)).toString());
        assertEquals(3, (int) s.get().foldRight(0, (t, u) -> u + t.length()));
    }

    @Test
    public void testUnfold() {
        assertEquals(
            asList(0, 1, 2, 3, 4),
            Seq.unfold(0, i -> i < 5 ? Optional.of(tuple(i, i + 1)) : Optional.empty()).toList());

        assertEquals(
            asList('a', 'b', 'c', 'd', 'e'),
            Seq.unfold(0, i -> i < 5 ? Optional.of(tuple((char) ('a' + i), i + 1)) : Optional.empty()).toList());
    }

    @Test
    public void testReverse() {
        assertEquals(asList(3, 2, 1), Seq.of(1, 2, 3).reverse().toList());
    }

    @Test
    public void testCycle() {
        assertEquals(asList(1, 2, 1, 2, 1, 2), Seq.of(1, 2).cycle().limit(6).toList());
        assertEquals(asList(1, 2, 3, 1, 2, 3), Seq.of(1, 2, 3).cycle().limit(6).toList());
    }
}
