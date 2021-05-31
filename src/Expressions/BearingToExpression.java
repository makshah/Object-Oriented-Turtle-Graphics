package Expressions;

import Helpers.Context;
import Turtle.Turtle;
import Visitor.AbstractVisitor;

public class BearingToExpression implements AbstractExpression<Void>{

    private AbstractExpression<Void> nextExpression;
    private String key;
    private String variableKey;
    private int value;

    public BearingToExpression(String key, String variableKey) {
        this.key = key;
        this.variableKey = variableKey;
    }
    @Override
    public Void interpret(Context values) {
        Turtle temporaryTurtle = values.getTurtle();
        double bearingTo = temporaryTurtle.bearingTo(values.getPoint(variableKey));
        this.value = (int)Math.round(bearingTo);
        values.setValue(key, this.value);
        if (nextExpression != null)
            nextExpression.interpret(values);
        return null;
    }

    @Override
    public void setNext(AbstractExpression exp) {
        this.nextExpression = exp;
    }

    @Override
    public AbstractExpression getNext() {
        return nextExpression;
    }

    public String getKey() {
        return this.key;
    }
    public String getVariableKey() {
        return this.variableKey;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public void accept(AbstractVisitor v) {
        v.visitBearingToExpression(this);
    }
}
