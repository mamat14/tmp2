package expressivo;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

public class Sum extends Expression {
    private Expression ex1;
    private Expression ex2;

    public Sum(Expression ex1, Expression ex2) {
        this.ex1 = ex1;
        this.ex2 = ex2;
    }

    @Override
    public String toString() {
        return "(" + ex1 + " + " + ex2 + ")";
    }

    @Override
    public boolean equals(Object thatObject) {
        if (thatObject == null) {
            return false;
        }
        if (thatObject instanceof Sum) {
            Sum that = ((Sum) thatObject);
            return that.ex1.equals(ex1) && that.ex2.equals(ex2);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ex1, ex2);
    }

    @Override
    public Expression differentiate(String variable) {
        return new Sum(ex1.differentiate(variable), ex2.differentiate(variable));
    }

    @Override
    public Expression simplify(Map<String, Double> environment) {
        Expression ex1Simp = ex1.simplify(environment);
        Expression ex2Simp = ex2.simplify(environment);

        if (ex1Simp instanceof Decimal && ex2Simp instanceof Decimal) {
            return new Decimal(((Decimal) ex1Simp).number.add(((Decimal) ex2Simp).number));

        } else if (ex1Simp instanceof MyInteger && ex2Simp instanceof Decimal) {
            return new Decimal((new BigDecimal(((MyInteger) ex1Simp).number).add(((Decimal) ex2Simp).number)));
        } else if (ex1Simp instanceof Decimal && ex2Simp instanceof MyInteger) {
            return new Decimal(((Decimal) ex1Simp).number.add(new BigDecimal(((MyInteger) ex2Simp).number)));
        } else if (ex1Simp instanceof MyInteger && ex2Simp instanceof MyInteger) {
            return new Decimal(new BigDecimal(((MyInteger) ex1Simp).number).add(new BigDecimal(((MyInteger) ex2Simp).number)));
        } else {
            return new Sum(ex1Simp, ex2Simp);
        }
    }
}
