/* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;


import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static expressivo.Commands.differentiate;
import static expressivo.Commands.simplify;
import static expressivo.Expression.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the static methods of Commands.
 */
public class CommandsTest {

    //testing strategy
    //differentiate:
    // constant
    // variable:
    //  : by same variable
    //  : by other variable
    // product
    // sum
    // bad input throws IllegalArgumentException
    //TODO simplify

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testDifferentiateConstant() {
        assertEquals(parse("0"), parse(differentiate("14", "x")));
    }

    @Test
    public void testDifferentiateVariableByItself() {
        assertEquals(parse("1"), parse(differentiate("xyz", "xyz")));
    }

    @Test
    public void testDifferentiateVariableByOther() {
        assertEquals(parse("0"), parse(differentiate("xyz", "x")));
    }

    @Test
    public void testDifferentiateProduct() {
        assertEquals(parse("(1 * x) + (x * 1)"), parse(differentiate("x * x", "x")));
    }

    @Test
    public void testDifferentiateSum() {
        assertEquals(parse("1 + 1"), parse(differentiate("x + x", "x")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDifferentiateBadInout() {
        differentiate("x + aaq qqwe", "x");
    }

    //simplify
    @Test
    public void testSimplifyEmptyEnvNoPresent(){
        Map<String, Double> environment = new HashMap<>();
        assertEquals(parse("x * y * z + 5"),  parse(simplify("x * y * z + 5", environment)));
    }

    @Test
    public void testSimplifyOneVariableNoPresent(){
        Map<String, Double> environment = new HashMap<>();
        environment.put("m", 5.);
        assertEquals(parse("x * y * z + 5"), parse(simplify("x * y * z + 5", environment)));
    }

    @Test
    public void testSimplifyOneVariableAllPresentSum() {
        Map<String, Double> environment = new HashMap<>();
        environment.put("m", 2.);
        assertEquals(parse("7"),parse(simplify("m + 5", environment)));
    }

    @Test
    public void testSimplifyOneVariableAllPresentProduct() {
        Map<String, Double> environment = new HashMap<>();
        environment.put("m", 2.);
        assertEquals(parse("10"), parse(simplify("m * 5", environment)));
    }

    @Test
    public void testSimplifyManyVariableOneOfManyPresent() {
        Map<String, Double> environment = new HashMap<>();
        environment.put("m", 2.);
        environment.put("qwe", 123123.);
        assertEquals(parse("2 + n + 5"), parse(simplify("m + n + 5", environment)));
    }

    @Test
    public void testSimplifyManyVariablesAllPresent() {
        Map<String, Double> environment = new HashMap<>();
        environment.put("m", 2.);
        environment.put("n", 3.);
        assertEquals(parse("8"), parse(simplify("m * n + m", environment)));
    }
}
