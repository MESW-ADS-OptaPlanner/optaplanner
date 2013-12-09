package org.optaplanner.core.api.domain.value.buildin.bigdecimal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import org.optaplanner.core.api.domain.value.AbstractValueRange;
import org.optaplanner.core.api.domain.value.iterator.ValueRangeIterator;
import org.optaplanner.core.impl.util.RandomUtils;

public class BigDecimalValueRange extends AbstractValueRange<BigDecimal> {

    private final BigDecimal from;
    private final BigDecimal to;
    private final BigDecimal incrementUnit;
    private final int scale;

    /**
     * All parameters must have the same {@link BigDecimal#scale()}.
     * @param from inclusive minimum
     * @param to exclusive maximum, >= {@code from}
     */
    public BigDecimalValueRange(BigDecimal from, BigDecimal to) {
        this(from, to, BigDecimal.valueOf(1L, from.scale()));
    }

    /**
     * All parameters must have the same {@link BigDecimal#scale()}.
     * @param from inclusive minimum
     * @param to exclusive maximum, >= {@code from}
     * @param incrementUnit > 0
     */
    public BigDecimalValueRange(BigDecimal from, BigDecimal to, BigDecimal incrementUnit) {
        this.from = from;
        this.to = to;
        this.incrementUnit = incrementUnit;
        scale = from.scale();
        if (scale != to.scale()) {
            throw new IllegalArgumentException("The " + getClass().getSimpleName()
                    + " cannot have a to (" + to + ") scale (" + to.scale()
                    + ") which is different than its from (" + from + ") scale (" + scale + ").");
        }
        if (scale != incrementUnit.scale()) {
            throw new IllegalArgumentException("The " + getClass().getSimpleName()
                    + " cannot have a from (" + incrementUnit + ") scale (" + incrementUnit.scale()
                    + ") which is different than its from (" + from + ") scale (" + scale + ").");
        }
        if (to.compareTo(from) < 0) {
            throw new IllegalArgumentException("The " + getClass().getSimpleName()
                    + " cannot have a from (" + from + ") which is strictly higher than its to (" + to + ").");
        }
        if (incrementUnit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("The " + getClass().getSimpleName()
                    + " must have strictly positive incrementUnit (" + incrementUnit + ").");
        }

        if (!to.unscaledValue().subtract(from.unscaledValue()).remainder(incrementUnit.unscaledValue())
                .equals(BigInteger.ZERO)) {
            throw new IllegalArgumentException("The " + getClass().getSimpleName()
                    + " 's incrementUnit (" + incrementUnit
                    + ") must fit an integer number of times between from (" + from + ") and to (" + to + ").");
        }
    }

    @Override
    public boolean isCountable() {
        return true;
    }

    @Override
    public long getSize() {
        return to.unscaledValue().subtract(from.unscaledValue()).divide(incrementUnit.unscaledValue()).longValue();
    }

    @Override
    public BigDecimal get(long index) {
        if (index < 0L || index >= getSize()) {
            throw new IndexOutOfBoundsException("The index (" + index + ") must be >= 0 and < size ("
                    + getSize() + ").");
        }
        return incrementUnit.multiply(BigDecimal.valueOf(index)).add(from);
    }

    @Override
    public Iterator<BigDecimal> createOriginalIterator() {
        return new OriginalBigDecimalValueRangeIterator();
    }

    private class OriginalBigDecimalValueRangeIterator extends ValueRangeIterator<BigDecimal> {

        private BigDecimal upcoming = from;

        @Override
        public boolean hasNext() {
            return upcoming.compareTo(to) < 0;
        }

        @Override
        public BigDecimal next() {
            if (upcoming.compareTo(to) >= 0) {
                throw new NoSuchElementException();
            }
            BigDecimal next = upcoming;
            upcoming = upcoming.add(incrementUnit);
            return next;
        }

    }

    @Override
    public Iterator<BigDecimal> createRandomIterator(Random workingRandom) {
        return new RandomBigDecimalValueRangeIterator(workingRandom);
    }

    private class RandomBigDecimalValueRangeIterator extends ValueRangeIterator<BigDecimal> {

        private final Random workingRandom;
        private final long size = getSize();

        public RandomBigDecimalValueRangeIterator(Random workingRandom) {
            this.workingRandom = workingRandom;
        }

        @Override
        public boolean hasNext() {
            return size > 0L;
        }

        @Override
        public BigDecimal next() {
            long index = RandomUtils.nextLong(workingRandom, size);
            return incrementUnit.multiply(BigDecimal.valueOf(index)).add(from);
        }

    }

}