package com.sysgears.simplecalculator.computer;

import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;

/**
 * Attempts to calculate a received math expression according to the
 * math precedence. The ideas behind the algorithm are next:
 * <p><ul>
 * <li>recursively open all the parentheses by calculating
 * the enclosed expressions</li>
 * <li>calculate the remaining parts of the expression according
 * to operators precedence</li>
 * <li>all possible operators are stored in {@link Operators} class</li>
 * </ul></p>
 */
public class ComputerBruteForce extends Computer {
    /**
     * Finds all parts of the expression which are enclosed in parentheses.
     * Computes such parts and put the value instead of the respective
     * enclosed part. Removes parentheses respectively.
     * Computes the remaining arithmetic expression
     *
     * @param expression String contains a valid math expression
     * @return String contains an expression with open parentheses
     * @throws InvalidInputExpressionException if the incoming string has an
     *                                         invalid format
     */
    String openParentheses(String expression) throws InvalidInputExpressionException {
        while (expression.contains("(")) {
            String parenthesesExpression = getParenthesesExpression(expression);
            expression = expression.replace(
                    "(" + parenthesesExpression + ")",
                    openParentheses(parenthesesExpression));
        }

        return computeArithmeticExpression(expression);
    }

    /**
     * Finds and returns a part of the expression which is enclosed in
     * parentheses. Searching starts from the left side of the expression.
     *
     * @param expression String contains a valid math expression
     * @return String contains an enclosed expression that can be empty
     */
    String getParenthesesExpression(final String expression) {
        int startIndex = expression.indexOf('(');
        int endIndex = startIndex;

        for (int counter = 1; counter > 0; ) {
            if (expression.charAt(++endIndex) == '(') {
                counter++;
            } else if (expression.charAt(endIndex) == ')') {
                counter--;
            }
        }

        return expression.substring(startIndex + 1, endIndex);
    }

    /**
     * Computes the received expression according to the math rules.
     * The ideas lie behind the algorithm are next:
     * <p><ul>
     * <li>iterates by all possible operators that exist in {@code Operators}
     * class</li>
     * <li>finds a binary expression that use such an operator</li>
     * <li>computes the expression</li>
     * <li>put the value instead of the respective part until all the
     * possible parts are computed</li>
     * </ul></p>
     *
     * @param expression String contains a valid math expression without parentheses
     * @return String contains the calculated expression
     * @throws InvalidInputExpressionException if the incoming string has an
     *                                         invalid format
     */
    String computeArithmeticExpression(String expression) throws InvalidInputExpressionException {
        for (Operators operator : Operators.values()) {
            while (containOperator(expression, operator)) {
                String leftOperand = getBinaryExpression(expression, operator);
                String rightOperand = getBinaryExpression(expression, operator);

/*                expression =
                        expression.
                                replace(leftOperand + operator.getDepiction() + rightOperand, calculatedValue).
                                replace("+-", "-");*/
            }
        }

        return expression;
    }

    /**
     * Checks incoming string whether it contains the required operator.
     *
     * @param expression String contains a valid math expression without parentheses
     * @param operator   the required operator
     * @return true if such the operator is found
     */
    boolean containOperator(final String expression, final Operators operator) {
        return expression.charAt(0) == '-'
                ? expression.indexOf(operator.getDepiction(), 1) != -1
                : expression.contains(operator.getDepiction());
    }

    /**
     * Returns the binary expression with specified operator
     *
     * @param expression String contains a valid math expression without parentheses.
     * @param operator   the required operator
     * @return String contains the binary expression
     */

    // TODO test it
    String getBinaryExpression(final String expression, final Operators operator) {
        int operatorIndex = expression.indexOf(operator.getDepiction(), 1);
        int leftBound = operatorIndex - 1;
        int rightBound = operatorIndex;

        while (leftBound > 0 && leftBound < expression.length() &&
                (Character.isDigit(expression.charAt(leftBound)) || expression.charAt(leftBound) == '.')) {

            leftBound--;
        }

        while (rightBound < expression.length() &&
                (Character.isDigit(expression.charAt(rightBound)) || expression.charAt(rightBound) == '.')) {

            rightBound++;
        }

        if (leftBound > 0 && expression.charAt(leftBound) != '-') {
            leftBound++; // step back
        }

        return expression.substring(leftBound, rightBound);
    }
}