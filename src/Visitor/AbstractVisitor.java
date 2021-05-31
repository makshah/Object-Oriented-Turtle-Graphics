package Visitor;

import Expressions.*;

public interface AbstractVisitor<E> {
	
	void visitMoveExpression(MoveExpression moveExp);
	void visitTurnExpression(TurnExpression turnExp);
	void visitAssignmentExpression(AssignmentExpression assignExpression);
	void visitPointAssignmentExpression(PointAssignmentExpression pointAssignExpression);
	void visitRepeatExpression(RepeatExpression repeatExpression);
	void visitDistanceToExpression(DistanceToExpression distanceToExpression);
	void visitBearingToExpression(BearingToExpression bearingToExpression);
	void visitEndExpression(EndExpression endExpression);
	E getResult();
}
