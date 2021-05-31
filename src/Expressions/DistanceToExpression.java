package Expressions;

import Helpers.Context;
import Turtle.Turtle;
import Visitor.AbstractVisitor;

import java.awt.geom.Point2D;

public class DistanceToExpression implements AbstractExpression<Void>{

    private AbstractExpression<Void> nextExpression;
    private String key;
    private String key2;
    private int value;

    public DistanceToExpression(String key, String key2) {
        this.key = key;
        this.key2 = key2;
    }

    @Override
    public Void interpret(Context values) {
        Turtle temporaryTurtle = values.getTurtle();
        double distanceTo = temporaryTurtle.distanceTo(values.getPoint(key2));
        this.value = (int)Math.round(distanceTo);
        System.out.println("Distance " + this.value);
        values.setValue(key, this.value);
        if (nextExpression != null)
            nextExpression.interpret(values);
        return null;
    }

    @Override
    public void setNext(AbstractExpression<Void> exp) {
        this.nextExpression = exp;
    }

    @Override
    public AbstractExpression<Void> getNext() {
        return nextExpression;
    }

    public String getKey() {
        return this.key;
    }
    public String getKey2() {
        return this.key2;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public void accept(AbstractVisitor<Void> v) {
        v.visitDistanceToExpression(this);
    }
}
