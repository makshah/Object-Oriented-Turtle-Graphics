package Expressions;

import Helpers.Context;
import Visitor.AbstractVisitor;

import java.awt.geom.Point2D;

public class PointAssignmentExpression implements AbstractExpression{

    private AbstractExpression<Void> nextExpression;
    private String key;
    private Point2D point;

    public PointAssignmentExpression(String key, double xValue, double yValue) {
        this.key = key;
        this.point = new Point2D.Double(xValue,yValue);
    }

    @Override
    public Object interpret(Context values) {
        values.setPoint(key, point);
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

    public Point2D getPoint() {
        return this.point;
    }

    @Override
    public void accept(AbstractVisitor v) {
        v.visitPointAssignmentExpression(this);
    }
}
