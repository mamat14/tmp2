package expressivo;

import org.junit.Test;

import static expressivo.Expression.parse;

import static org.junit.Assert.assertEquals;

public class ExpressionToStringTest {
    //----------------------------------------------------------------------------------------------------------------------------
    //1.1
    @Test
    public void testToStringVariablesAllowed1() {
        Expression e = parse("y");
        assertEquals(e, parse(e.toString()));
    }

    @Test
    public void testToStringVariablesAllowed2() {
        Expression e = parse("Z");
        assertEquals(e, parse(e.toString()));
    }

    @Test
    public void testToStringVariablesAllowed3() {
        Expression e = parse("fOO");
        assertEquals(e, parse(e.toString()));
    }

    @Test
    public void testToStringVariablesAllowed4() {
        Expression e = parse("bar");
        assertEquals(e, parse(e.toString()));
    }

    //----------------------------------------------------------------------------------------------------------------------------

    //1.2.1
    @Test
    public void testToStringDecimal() {
        Expression e = parse("1234567890.0123456789");
        assertEquals(e, parse(e.toString()));

    }

    //1.2.1
    @Test
    public void testToStringInteger() {
        Expression e = parse("9087654321");
        assertEquals(e, parse(e.toString()));
    }

    //----------------------------------------------------------------------------------------------------------------------------

    //2.1.1
    @Test
    public void testToStringSumVariablePlusNumber() {
        Expression e = parse("4+abc");
        assertEquals(e, parse(e.toString()));
    }

    //2.1.2
    @Test
    public void testToStringSumOfTwoNumbers() {
        Expression e = parse("4+5.1");
        assertEquals(e, parse(e.toString()));
    }

    //2.1.3
    @Test
    public void testToStringSumOfThreeVariables() {
        Expression e = parse("foo+bar + baz");
        assertEquals(e, parse(e.toString()));
    }

    //2.1.4
    @Test
    public void testToStringSumIgnoresWhitespaces() {
        Expression e = parse("foo     +       2.4   + asaAASAASD + azzxc + 11.2123121312312312312321312312");
        assertEquals(e, parse(e.toString()));
    }

    //----------------------------------------------------------------------------------------------------------------------------

    //2.2.1
    @Test
    public void testToStringProductVariablePlusNumber() {
        Expression e = parse("4*abc");
        assertEquals(e, parse(e.toString()));
    }

    //2.2.2
    @Test
    public void testToStringProductOfTwoNumbers() {
        Expression e = parse("4*5.1");
        assertEquals(e, parse(e.toString()));
    }

    //2.2.3
    @Test
    public void testToStringProductOfThreeVariables() {
        Expression e = parse("foo * bar*baz");
        assertEquals(e, parse(e.toString()));
    }

    //2.2.4
    @Test
    public void testToStringProductIgnoresWhitespaces() {
        Expression e = parse("foo     *       2.4   * asaAASAASD * azzxc * 11.2123121312312312312321312312");
        assertEquals(e, parse(e.toString()));
    }

    //----------------------------------------------------------------------------------------------------------------------------

    //3.1
    //3.1.1
    @Test
    public void testToStringCorrectParenthesis1() {
        Expression e = parse("(3 + 2) * 5");
        assertEquals(e, parse(e.toString()));
    }


    @Test
    public void testToStringCorrectParenthesis2() {
        Expression e = parse("((((a + b)) + (3 + 2))) * 5");
        assertEquals(e, parse(e.toString()));
    }


    @Test
    public void testToStringCorrectParenthesis3() {
        Expression e = parse("(3 + 2) * (asd + (asdasd))");
        assertEquals(e, parse(e.toString()));
    }
    //----------------------------------------------------------------------------------------------------------------------------

    @Test
    public void testToStringManySpaces() {
        Expression e = parse("(2*x    )+    (    y*x    )");
        assertEquals(e, parse(e.toString()));
    }

    @Test
    public void testToStringHard() {
        Expression e = parse("4 + 3 * x + 2 * x * x + 1 * x * x * (((x)))");
        assertEquals(e, parse(e.toString()));
    }

    @Test
    public void testToStringPreservesOperatorsOrder() {
        Expression e = parse("(2 + 2) * 2");
        assertEquals(e, parse(e.toString()));
    }
}
