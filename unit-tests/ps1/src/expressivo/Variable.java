package expressivo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class Variable extends Expression {
    private String variable;
    private static final Pattern variablePattern;

    static{
        variablePattern = Pattern.compile("[A-Za-z]+");
    }

    public Variable(String variable) {
        if (!variablePattern.matcher(variable).matches()) {
            throw new IllegalArgumentException();
        }
        this.variable = variable;
    }

    @Override
    public String toString() {
        return variable;
    }

    @Override
    public boolean equals(Object thatObject) {
        if (thatObject == null) {
            return false;
        }
        if (thatObject instanceof Variable) {
            return ((Variable) thatObject).variable.equals(variable);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(variable);
    }

    @Override
    public Expression differentiate(String variable) {
        if (variable.equals(this.variable)) {
            return new MyInteger(BigInteger.ONE);
        }else{
            return new MyInteger(BigInteger.ZERO);
        }
    }

    @Override
    public Expression simplify(Map<String, Double> environment) {
        if (environment.keySet().contains(variable)) {
            return new Decimal(new BigDecimal(environment.get(variable)));
        }else {
            return this;
        }
    }
}
