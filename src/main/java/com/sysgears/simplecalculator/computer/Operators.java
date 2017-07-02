package com.sysgears.simplecalculator.computer;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Contains allowed math operators and their logic. The precedence of the
 * operators is their order in this class. Minus must be before plus
 * according to algorithms' logic. All the computes heavily rely on
 * {@code double} type, however in Java such calculations lead to round-off
 * errors, and this type is constrained by number size. So, it can be
 * changed to {@code BigDecimal} so as to solve problems above.
 * <p>
 *     Parentheses symbols, '--', '+-' and '-+' CANNOT use either as
 *     operators or as a part of an operator
 * </p>
 */
public enum Operators {
    /**
     * A math power operator
     */
    POWER("^") {
        @Override
        public Double calculate(final double x, final double y) {
            return convertNegativeZero((x < 0 ? -1 : 1) * Math.pow(x, y));
        }
    },
    /**
     * A math power divide
     */
    DIVIDE("/") {
        @Override
        public Double calculate(final double x, final double y) {
            if (Double.compare(convertNegativeZero(y), 0) == 0) {
                throw new ArithmeticException();
            }

            return convertNegativeZero(x / y);
        }
    },
    /**
     * A math multiply operator
     */
    MULTIPLY("*") {
        @Override
        public Double calculate(final double x, final double y) {
            return convertNegativeZero(x * y);
        }
    },
    /**
     * A math subtract operator
     */
    SUBTRACT("-") {
        @Override
        public Double calculate(final double x, final double y) {
            return convertNegativeZero(x - y);
        }
    },
    /**
     * A math add operator
     */
    ADD("+") {
        @Override
        public Double calculate(final double x, final double y) {
            return convertNegativeZero(x + y);
        }
    };

    /**
     * The string representation of the operator
     */
    private String representation;

    /**
     * Constructs an object
     *
     * @param representation The string representation of the operator
     */
    Operators(String representation) {
        this.representation = representation;
    }

    /**
     * Builds a RegExp string contains all the operators
     *
     * @return The RegExp string contains all the operators
     */
    static String getRegExp() {
        StringBuilder builder = new StringBuilder("[");

        for (Operators operator : values()) {
            builder.append(operator.getRegExpRepresentation());
        }

        return builder.append("]{1}").toString();
    }

    /**
     * This function convert <code>value</code> to +0.0, if it is equal
     * to -0.0 so as to obtain a predictable behaviour of compare functions
     *
     * @param value a value to convert
     * @return converted or the same value
     */
    static double convertNegativeZero(double value) {
        if (value == 0.0) {
            value = 0.0;  // convert -0.0 to +0.0
        }

        return value;
    }

    /**
     * Contains the math logic of the operator
     *
     * @param x The left operand
     * @param y The right operand
     * @return The computed value
     * @throws ArithmeticException If an arithmetic error is happen
     */
    public abstract Double calculate(final double x, final double y) throws ArithmeticException;

    /**
     * Returns the string representation of the operator
     *
     * @return The string representation of the operator
     */
    public String getRepresentation() {
        return representation;
    }

    /**
     * Returns the RegExp string representation of the operator
     *
     * @return The normalized string representation of the operator
     */
    public String getRegExpRepresentation() {
        return Pattern.quote(representation);
    }

    /**
     * Builds and returns the string representation of the operator list
     *
     * @return The string with the description of all the operators
     */
    public static String getList() {
        return Stream.of(values()).
                map(e -> "\t" + e + "(" + e.getRepresentation() + ")").
                collect(Collectors.joining(System.lineSeparator()));
    }
}