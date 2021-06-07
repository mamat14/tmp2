package expressivo;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static expressivo.Expression.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ExpressionTest {
    //Partition:
    // parse:
    // 1:odnochlens
    //   1.1:variables // lengt n:1, many +
    //      1.1.1:spaces are not allowed
    //      1.1.2:case-sensitiveness
    //   1.2: numbers
    //      1.2.1: целые
    //              парсинг нуля!([1-9][0-9]+ не учитывает 0)
    //              целые не имеют верхней границы
    //      1.2.2 десятичные
    //          1.2.2.1: десятичные с точкой без последующих цифр запрещены
    //          1.2.2.2: десятичные с точкой с последующими нулями разрешены
    //          1.2.2.3: десятичные без нуля в начале
    //      1.2.2: запрещены пробелы
    //      1.2.3: неотрицательность
    // 2:combination of odnochlens
    //   2.1: sum
    //      2.1.1: sum of numbers with variables
    //      2.1.2: sum of only variables
    //      2.1.3: sum of only numbers
    //      2.1.4: sum ignores whitespace
    //   2.1: product
    //      2.2.1: product of numbers with variables
    //      2.2.2: product of only variables
    //      2.2.3: product of only numbers
    //      2.2.4: product ignores whitespace
    //      2.2.5: implicit product banned: e.g 3xy
    // 3:parentheses
    //      3.1 opened = closed
    //          3.1.1: opened >= closed until an end of string.
    //          3.1.2: exists moment where opened < closed at some moment
    //      3.2 opened > closed
    //      3.3 opened < closed

    // equals
    //  1. numbers
    //      1.1 correct case e.g 3 equals 3
    //      1.1 integer equals to decimal in case of zero fractional. e.g 1 equals 1.0
    //  2. variables
    //      1.1 correct case. e.g. foo equals foo
    //      1.2 variable are case sensitive
    //  3. expressions consists of numbers and variables
    //      sum equals sum
    //      product equals product
    //      sum does not equals product

    //toString
    // same partition as in parse, but consider just correct cases

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    //----------------------------------------------------------------------------------------------------------------------------
    //1.1
    @Test
    public void testParseVariablesAllowed1() {
        parse("y");
    }

    @Test
    public void testParseVariablesAllowed2() {
        parse("Z");
    }

    @Test
    public void testParseVariablesAllowed3() {
        parse("fOO");
    }

    @Test
    public void testParseVariablesAllowed4() {
        parse("bar");
    }

    //1.1.1
    @Test(expected = IllegalArgumentException.class)
    public void testParseSpacesInVariablesAreNotAllowed() {
        parse("p i");
    }

    //----------------------------------------------------------------------------------------------------------------------------

    //1.2.1
    @Test
    public void testParseDecimal() {
        parse("1234567890.0123456789");
    }

    //1.2.1
    @Test
    public void testParseInteger() {
        parse("9087654321");
    }

    //1.2.1.1
    @Test(expected = IllegalArgumentException.class)
    public void testParseDecimalWithoutNumbersAfterPoint() {
        parse("5411.");
    }

    //1.2.2
    @Test(expected = IllegalArgumentException.class)
    public void testParseDecimalWithSpacesAroundPoint() {
        parse("0 .4");
    }

    //1.2.2
    @Test(expected = IllegalArgumentException.class)
    public void testParseIntegerWithSpacesAroundPoint() {
        parse("5 4");
    }

    //1.2.3
    @Test(expected = IllegalArgumentException.class)
    public void testParseNegativeInteger() {
        parse("-54");
    }

    //
    @Test
    public void testParseZero() {
        parse("0");
    }
    //
    @Test
    public void testParseZeroDecimal() {
        parse("0.0");
    }
    //
    @Test
    public void testParseCantParseVeryBigInteger(){
        parse("111111111111111111111111111111111111111111111111");
    }
    //----------------------------------------------------------------------------------------------------------------------------

    //2.1.1
    @Test
    public void testParseSumVariablePlusNumber() {
        parse("4+abc");
    }

    //2.1.2
    @Test
    public void testParseSumOfTwoNumbers() {
        parse("4+5.1");
    }

    //2.1.3
    @Test
    public void testParseSumOfThreeVariables() {
        parse("foo + bar+baz");
    }

    //2.1.4
    @Test
    public void testParseSumIgnoresWhitespaces() {
        parse("foo     +       2.4   + asaAASAASD + azzxc + 11.2123121312312312312321312312");
    }

    //----------------------------------------------------------------------------------------------------------------------------

    //2.2.1
    @Test
    public void testParseProductVariablePlusNumber() {
        parse("4*abc");
    }

    //2.2.2
    @Test
    public void testParseProductOfTwoNumbers() {
        parse("4*5.1");
    }

    //2.2.3
    @Test
    public void testParseProductOfThreeVariables() {
        parse("foo * bar*baz");
    }

    //2.2.4
    @Test
    public void testParseProductIgnoresWhitespaces() {
        parse("foo     *       2.4   * asaAASAASD * azzxc * 11.2123121312312312312321312312");
    }

    //2.2.5
    @Test(expected = IllegalArgumentException.class)
    public void testParseImplicitProduct() {
        parse("3xy");
    }

    //----------------------------------------------------------------------------------------------------------------------------

    @Test
    public void testParseCorrectOrder(){
        Expression e = parse("2 + 2 * 2");
        assertEquals(e,parse("2 + (2 * 2)"));
    }

    //----------------------------------------------------------------------------------------------------------------------------

    //3.1
    //3.1.1
    @Test
    public void testParseCorrectParenthesis1() {
        parse("(3 + 2) * 5");
    }


    @Test
    public void testParseCorrectParenthesis2() {
        parse("((((a + b))+(3 + 2))) * 5");
    }


    @Test
    public void testParseCorrectParenthesis3() {
        parse("(3 + 2) * (asd + (asdasd))");
    }

    //3.1.1
    @Test(expected = IllegalArgumentException.class)
    public void testParseIncorrectStructure() {
        parse("(3 + abc))(");
    }

    //3.2
    @Test(expected = IllegalArgumentException.class)
    public void testParseOpenedParenthesisMoreThanClosed() {
        parse("(((asdasdas) + asda + 3)");
    }

    //3.3
    @Test(expected = IllegalArgumentException.class)
    public void testParseOpenedParenthesisLessThanClosed() {
        parse("(qqqwe + asd))");
    }

    //----------------------------------------------------------------------------------------------------------------------------

    @Test
    public void testParseManySpaces() {
        parse("(2*x    )+    (    y*x    )");
    }

    @Test
    public void testParseHard() {
        parse("4 + 3 * x + 2 * x * x + 1 * x * x * (((x)))");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseProductOneOperand() {
        parse("3 *");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseSomeBad1() {
        parse("3 + asdf )");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseSomeBad2() {
        parse("3 +* xy");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseSomeBad3() {
        parse("3 - xy");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseSomeBad4() {
        parse("+ 3 y");
    }

    //----------------------------------------------------------------------------------------------------------------------------

    // equals
    //  1. numbers
    //      3.1.1 correct case e.g 3 equals 3
    //      3.1.2 integer equals to decimal in case of zero fractional. e.g 1 equals 1.0
    //  2. variables
    //      3.2.1 correct case. e.g. foo equals foo
    //      3.2.2 variable are case sensitive
    //  3. expressions consists of numbers and variables
    //      3.3.1 sum equals sum
    //      3.3.2 product equals product
    //      3.3.3 sum does not equals product

    //3.1
    @Test
    public void testEqualsNumbers(){
        assertEquals(parse("3"), parse("3"));
        assertEquals(parse("3.5"), parse("3.5"));
        assertNotEquals(parse(" 123"), parse("123.0000000001 "));
        assertNotEquals(parse("3"), parse("35"));
    }

    @Test
    public void testEqualsDecimalWithZeroFractionEqualsInteger() {
        assertEquals(parse("1"), parse("1.0"));
    }

    //3.2.1
    @Test
    public void testEqualsVariables(){
        assertEquals(parse("abc "), parse(" abc"));
        assertEquals(parse("Abf"), parse("Abf"));
        assertNotEquals(parse("asdf"), parse("asdfe"));
        assertNotEquals(parse("qqwe"), parse("qwe"));
    }

    //3.2.2
    @Test
    public void testEqualsVariablesCaseSensitive() {
        assertNotEquals(parse("Foo"), parse("foo"));
    }


    @Test
    public void testEqualsSumDoesNotEqualsProduct() {
        assertNotEquals(parse("a + b"), parse("a * b"));
    }

    @Test
    public void testEqualsStructure(){
        assertEquals(parse("((a+ b)) + c"), parse("(a + b) + (c)"));
    }

    //----------------------------------------------------------------------------------------------------------------------------

    //differentiate:
    // constant
    // variable:
    //  : by same variable
    //  : by other variable
    // product
    // sum
    @Test
    public void testDifferentiateConstant(){
        assertEquals(parse("0"), parse("14").differentiate("x"));
    }

    @Test
    public void testDifferentiateVariableByItself(){
        assertEquals(parse("1"), parse("xyz").differentiate("xyz"));
    }

    @Test
    public void testDifferentiateVariableByOther(){
        assertEquals(parse("0"), parse("xyz").differentiate("x"));
    }

    @Test
    public void testDifferentiateProduct(){
        assertEquals(parse("(1 * x) + (x * 1)"), parse("x * x").differentiate("x"));
    }
    @Test
    public void testDifferentiateSum(){
        assertEquals(parse("1 + 1"), parse("x + x").differentiate("x"));
    }

    //simplify
    // environment:
    //      empty, one element, many elements;
    //      no, one, all variables present,
    //      operation product, sum
    @Test
    public void testSimplifyEmptyEnvNoPresent(){
        Map<String, Double> environment = new HashMap<>();
        assertEquals(parse("x * y * z + 5"), parse("x * y * z + 5").simplify(environment));
    }

    @Test
    public void testSimplifyOneVariableNoPresent(){
        Map<String, Double> environment = new HashMap<>();
        environment.put("m", 5.);
        assertEquals(parse("x * y * z + 5"), parse("x * y * z + 5").simplify(environment));
    }

    @Test
    public void testSimplifyOneVariableAllPresentSum() {
        Map<String, Double> environment = new HashMap<>();
        environment.put("m", 2.);
        assertEquals(parse("7"), parse("m + 5").simplify(environment));
    }

    @Test
    public void testSimplifyOneVariableAllPresentProduct() {
        Map<String, Double> environment = new HashMap<>();
        environment.put("m", 2.);
        assertEquals(parse("10"), parse("m * 5").simplify(environment));
    }

    @Test
    public void testSimplifyManyVariableOneOfManyPresent() {
        Map<String, Double> environment = new HashMap<>();
        environment.put("m", 2.);
        environment.put("qwe", 123123.);
        assertEquals(parse("2 + n + 5"), parse("m + n + 5").simplify(environment));
    }

    @Test
    public void testSimplifyManyVariablesAllPresent() {
        Map<String, Double> environment = new HashMap<>();
        environment.put("m", 2.);
        environment.put("n", 3.);
        assertEquals(parse("8"), parse("m * n + m").simplify(environment));
    }
}
