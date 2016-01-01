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

import org.jooq.lambda.tuple.Tuple2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;

import static java.util.Comparator.naturalOrder;
import static org.jooq.lambda.tuple.Tuple.tuple;

/**
 * A set of additional {@link Collector} implementations.
 * <p>
 * The class name isn't set in stone and will change.
 *
 * @author Lukas Eder
 */
public class Agg {

    /**
     * Get a {@link Collector} that calculates the <code>MEDIAN()</code> function given natural ordering.
     */
    public static <T extends Comparable<? super T>> Collector<T, ?, Optional<T>> median() {
        return percentileDiscBy(0.5, t -> t, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>MEDIAN()</code> function given a specific ordering.
     */
    public static <T> Collector<T, ?, Optional<T>> median(Comparator<? super T> comparator) {
        return percentileDiscBy(0.5, t -> t, comparator);
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>MEDIAN()</code> function given natural ordering.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Optional<T>> medianBy(Function<? super T, ? extends U> function) {
        return percentileDiscBy(0.5, function, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>MEDIAN()</code> function given a specific ordering.
     */
    public static <T, U> Collector<T, ?, Optional<T>> medianBy(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return percentileDiscBy(0.5, function, comparator);
    }

    /**
     * Get a {@link Collector} that calculates the <code>PERCENTILE_DISC(percentile)</code> function given natural ordering.
     */
    public static <T extends Comparable<? super T>> Collector<T, ?, Optional<T>> percentileDisc(double percentile) {
        return percentileDisc(percentile, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>PERCENTILE_DISC(percentile)</code> function given a specific ordering.
     */
    public static <T> Collector<T, ?, Optional<T>> percentileDisc(double percentile, Comparator<? super T> comparator) {
        return percentileDiscBy(percentile, t -> t, comparator);
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>PERCENTILE_DISC(percentile)</code> function given natural ordering.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Optional<T>> percentileDiscBy(double percentile, Function<? super T, ? extends U> function) {
        return percentileDiscBy(percentile, function, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>PERCENTILE_DISC(percentile)</code> function given a specific ordering.
     */
    public static <T, U> Collector<T, ?, Optional<T>> percentileDiscBy(double percentile, Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        if (percentile < 0.0 || percentile > 1.0)
            throw new IllegalArgumentException("Percentile must be between 0.0 and 1.0");

        // At a later stage, we'll optimise this implementation in case that function is the identity function
        return Collector.of(
            () -> new ArrayList<Tuple2<T, U>>(),
            (l, v) -> l.add(tuple(v, function.apply(v))),
            (l1, l2) -> {
                l1.addAll(l2);
                return l1;
            },
            l -> {
                int size = l.size();

                if (size == 0)
                    return Optional.empty();
                else if (size == 1)
                    return Optional.of(l.get(0).v1);

                l.sort(Comparator.comparing(t -> t.v2, comparator));

                if (percentile == 0.0)
                    return Optional.of(l.get(0).v1);
                else if (percentile == 1.0)
                    return Optional.of(l.get(size - 1).v1);

                // x.5 should be rounded down
                return Optional.of(l.get((int) -Math.round(-(size * percentile + 0.5)) - 1).v1);
            }
        );
    }
}
