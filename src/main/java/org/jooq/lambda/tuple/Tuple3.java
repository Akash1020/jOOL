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
package org.jooq.lambda.tuple;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.jooq.lambda.function.Function1;
import org.jooq.lambda.function.Function3;

/**
 * A tuple of degree 3.
 *
 * @author Lukas Eder
 */
public class Tuple3<T1, T2, T3> implements Tuple, Comparable<Tuple3<T1, T2, T3>>, Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    public final T1 v1;

    public final T2 v2;

    public final T3 v3;

    public T1 v1() {
        return v1;
    }

    public T2 v2() {
        return v2;
    }

    public T3 v3() {
        return v3;
    }

    public Tuple3(Tuple3<T1, T2, T3> tuple) {
        this.v1 = tuple.v1;
        this.v2 = tuple.v2;
        this.v3 = tuple.v3;
    }

    public Tuple3(T1 v1, T2 v2, T3 v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    /**
     * Apply this tuple as arguments to a function.
     */
    public final <R> R map(Function3<T1, T2, T3, R> function) {
        return function.apply(this);
    }

    /**
     * Apply attribute 1 as argument to a function and return a new tuple with the substituted argument.
     */
    public final <U1> Tuple3<U1, T2, T3> map1(Function1<T1, U1> function) {
        return Tuple.tuple(function.apply(v1), v2, v3);
    }

    /**
     * Apply attribute 2 as argument to a function and return a new tuple with the substituted argument.
     */
    public final <U2> Tuple3<T1, U2, T3> map2(Function1<T2, U2> function) {
        return Tuple.tuple(v1, function.apply(v2), v3);
    }

    /**
     * Apply attribute 3 as argument to a function and return a new tuple with the substituted argument.
     */
    public final <U3> Tuple3<T1, T2, U3> map3(Function1<T3, U3> function) {
        return Tuple.tuple(v1, v2, function.apply(v3));
    }

    @Override
    public final Object[] array() {
        return new Object[] { v1, v2, v3 };
    }

    @Override
    public final List<?> list() {
        return Arrays.asList(array());
    }

    /**
     * The degree of this tuple: 3.
     */
    @Override
    public final int degree() {
        return 3;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final Iterator<Object> iterator() {
        return (Iterator<Object>) list().iterator();
    }

    @Override
    public int compareTo(Tuple3<T1, T2, T3> other) {
        int result;

        result = Tuples.compare(v1, other.v1); if (result != 0) return result;
        result = Tuples.compare(v2, other.v2); if (result != 0) return result;
        result = Tuples.compare(v3, other.v3); if (result != 0) return result;

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Tuple3))
            return false;

        @SuppressWarnings({ "unchecked", "rawtypes" })
        final Tuple3<T1, T2, T3> that = (Tuple3) o;

        if (!Objects.equals(v1, that.v1)) return false;
        if (!Objects.equals(v2, that.v2)) return false;
        if (!Objects.equals(v3, that.v3)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((v1 == null) ? 0 : v1.hashCode());
        result = prime * result + ((v2 == null) ? 0 : v2.hashCode());
        result = prime * result + ((v3 == null) ? 0 : v3.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return "("
             +        v1
             + ", " + v2
             + ", " + v3
             + ")";
    }

    @Override
    public Tuple3<T1, T2, T3> clone() {
        return new Tuple3<>(this);
    }
}
