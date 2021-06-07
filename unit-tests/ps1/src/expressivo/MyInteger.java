package expressivo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Objects;

public class MyInteger extends Expression {
    BigInteger number;

    public MyInteger(BigInteger number) {
        if(number.compareTo(BigInteger.ZERO) < 0) throw new IllegalArgumentException();
        this.number = number;
    }

    @Override
    public String toString() {
        return String.valueOf(number);
    }

    @Override
    public boolean equals(Object thatObject) {
        if (thatObject == null) {
            return false;
        }
        if (thatObject instanceof MyInteger) {
            return ((MyInteger) thatObject).number.equals(number);
        }
        if (thatObject instanceof Decimal) {
            return ((Decimal) thatObject).number.compareTo(new BigDecimal(number)) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(number);
    }

    @Override
    public Expression differentiate(String variable) {
        return new MyInteger(BigInteger.ZERO);
    }

    @Override
    public Expression simplify(Map<String, Double> environment) {
        return this;
    }
}
