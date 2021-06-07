package expressivo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Objects;

public class Decimal extends Expression {
    BigDecimal number;

    public Decimal(BigDecimal number) {
        if (number.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        this.number = number;
    }

    @Override
    public String toString() {
        if (number.compareTo(BigDecimal.ONE) < 0) {
            return number.toString().substring(1);
        }
        return number.toString();
    }

    @Override
    public boolean equals(Object thatObject) {
        if (thatObject == null) {
            return false;
        }
        if (thatObject instanceof Decimal) {
            return ((Decimal) thatObject).number.equals(number);
        }
        if (thatObject instanceof MyInteger) {
            return number.compareTo(new BigDecimal(((MyInteger) thatObject).number)) == 0;
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
