package Expressions;

import Visitor.AbstractVisitor;
import Helpers.Context;

public interface AbstractExpression<E> {

	public E interpret(Context value);

	void setNext(AbstractExpression<E> exp);
	AbstractExpression<E> getNext();

	void accept(AbstractVisitor<E> v);

}
