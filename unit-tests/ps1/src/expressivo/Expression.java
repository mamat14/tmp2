package expressivo;

import lib6005.parser.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * An immutable data type representing a polynomial expression of:
 * + and *
 * nonnegative integers and floating-point numbers
 * variables (case-sensitive nonempty strings of letters)
 *
 * <p>PS1 instructions: this is a required ADT interface.
 * You MUST NOT change its name or package or the names or type signatures of existing methods.
 * You may, however, add additional methods, or strengthen the specs of existing methods.
 * Declare concrete variants of Expression in their own Java source files.
 */
public abstract class Expression {

    // Datatype definition
    //   Expression = MyInteger(n:int) + Decimal(d:double) + Variable(var: string) +
    //   Sum(lE: Expression, rE: Expression) + Production(lE: Expression, rE: Expression)

    private static final Parser<ExpressionGrammar> parser;

    static {
        Parser<ExpressionGrammar> compile;
        try {
            compile = GrammarCompiler.compile(new File("src/expressivo/Expression.g"), ExpressionGrammar.EXPRESSION);
        } catch (UnableToParseException | IOException e) {
            throw new RuntimeException(e);
        }
        parser = compile;
    }

    /**
     * Parse an expression.
     *
     * @param input expression to parse, as defined in the PS1 handout.
     * @return expression AST for the input
     * @throws IllegalArgumentException if the expression is invalid
     */
    public static Expression parse(String input) {
        try {
            ParseTree<ExpressionGrammar> tree = parser.parse(input);

            Expression expression = buildExpression(tree);
            return expression;
        } catch (UnableToParseException e) {
            throw new IllegalArgumentException();
        }
    }

    private static Expression buildExpression(ParseTree<ExpressionGrammar> tree) {
        switch (tree.getName()) {
            case EXPRESSION:
                boolean first = true;
                Expression result = null;
                List<ParseTree<ExpressionGrammar>> products = tree.childrenByName(ExpressionGrammar.PRODUCT);
                for (ParseTree<ExpressionGrammar> product : products) {
                    if (first) {
                        result = buildExpression(product);
                        first = false;
                    } else {
                        result = new Sum(result, buildExpression(product));
                    }
                }
                if (first) {
                    throw new IllegalStateException("expression is empty");
                }
                return result;
            case PRODUCT:
                boolean firstProduct = true;
                Expression resultProduct = null;
                List<ParseTree<ExpressionGrammar>> values = tree.childrenByName(ExpressionGrammar.VALUE);
                for (ParseTree<ExpressionGrammar> value : values) {
                    if (firstProduct) {
                        resultProduct = buildExpression(value);
                        firstProduct = false;
                    } else {
                        resultProduct = new Product(resultProduct, buildExpression(value));
                    }
                }
                if (firstProduct) {
                    throw new IllegalStateException("expression is empty");
                }
                return resultProduct;
            case VALUE:
                if (!tree.childrenByName(ExpressionGrammar.NUMBER).isEmpty()) {
                    return buildExpression(tree.childrenByName(ExpressionGrammar.NUMBER).get(0));
                } else if (!tree.childrenByName(ExpressionGrammar.VARIABLE).isEmpty()) {
                    return buildExpression(tree.childrenByName(ExpressionGrammar.VARIABLE).get(0));
                } else if (!tree.childrenByName(ExpressionGrammar.EXPRESSION).isEmpty()) {
                    return buildExpression(tree.childrenByName(ExpressionGrammar.EXPRESSION).get(0));
                } else {
                    throw new IllegalStateException("Should never got here");
                }
            case NUMBER:
                if (!tree.childrenByName(ExpressionGrammar.DECIMAL).isEmpty()) {
                    return buildExpression(tree.childrenByName(ExpressionGrammar.DECIMAL).get(0));
                } else if (!tree.childrenByName(ExpressionGrammar.INTEGER).isEmpty()) {
                    return buildExpression(tree.childrenByName(ExpressionGrammar.INTEGER).get(0));
                } else {
                    throw new IllegalStateException("Should never be here");
                }
            case DECIMAL:
                return new Decimal(new BigDecimal(tree.getContents()));
            case INTEGER:
                return new MyInteger(new BigInteger(tree.getContents()));
            case VARIABLE:
                return new Variable(tree.getContents());
            case WHITESPACE:
                throw new IllegalStateException();
        }
        throw new IllegalStateException("Should never got here.");
    }
    /**
     * @return a parsable representation of this expression, such that
     * for all e:Expression, e.equals(Expression.parse(e.toString())).
     */
    @Override
    public abstract String toString();

    /**
     * @param thatObject any object
     * @return true if and only if this and thatObject are structurally-equal
     * Expressions, as defined in the PS1 handout.
     */
    @Override
    public abstract boolean equals(Object thatObject);

    /**
     * @return hash code value consistent with the equals() definition of structural
     * equality, such that for all e1,e2:Expression,
     * e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public abstract int hashCode();

    /**
     * Returns derivative of current expression. It does not simplify result, just straight rules are applied.
     * For example derivative of x*x equals to 1 * x + x * 1.
     * @param variable variable to differentiate by.
     * @return {@code Expression} which is a derivative of expression.
     */
    public abstract Expression differentiate(String variable);

    /**
     * Returns a simplified expression with varibles that exists in environment.keySet() are substituted with
     * correspondent values. If no values are left expression required be simplified to one value.Other simplification
     * isn't specified for now.
     * @param environment map requires string is
     * @return
     */
    public abstract Expression simplify(Map<String,Double> environment);
    public enum ExpressionGrammar {
        EXPRESSION,
        VALUE,
        NUMBER,
        DECIMAL,
        INTEGER,
        VARIABLE,
        PRODUCT,
        WHITESPACE
    }

    /* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires permission of course staff.
     */
}
