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
package org.jooq.lambda.tuple;

import java.util.Optional;

/**
 * A range is a special {@link Tuple2} with two times the same type.
 *
 * @author Lukas Eder
 */
public class Range<T extends Comparable<T>> extends Tuple2<T, T> {

    public Range(T v1, T v2) {
        super(r(v1, v2));
    }

    public Range(Tuple2<T, T> tuple) {
        this(tuple.v1, tuple.v2);
    }

    private static <T extends Comparable<T>> Tuple2<T, T> r(T t1, T t2) {
        return t1.compareTo(t2) <= 0 ? new Tuple2<>(t1, t2) : new Tuple2<>(t2, t1);
    }

    public boolean overlaps(Tuple2<T, T> other) {
        return Tuple2.overlaps(this, other);
    }

    public boolean overlaps(T t1, T t2) {
        return overlaps(new Range(t1, t2));
    }

    public Optional<Range<T>> intersect(Tuple2<T, T> other) {
        return Tuple2.intersect(this, other).map(Range::new);
    }

    public Optional<Range<T>> intersect(T t1, T t2) {
        return intersect(new Range(t1, t2));
    }
}
