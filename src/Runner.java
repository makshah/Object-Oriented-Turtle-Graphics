import java.io.FileNotFoundException;
import java.util.List;

import Commands.Command;
import Helpers.Context;
import Helpers.Evaluator;
import Turtle.Turtle;
import Visitor.AbstractVisitor;
import Visitor.DistanceCalculationVisitor;
import Visitor.StepByStepExecutionVisitor;


public class Runner {

public static void main(String[] args) throws FileNotFoundException {	
		
	Evaluator exp = new Evaluator("turtle.txt");
	exp.makeSyntaxTree();
	Context context = exp.getContext();
	Turtle t = exp.interpret(context);

	System.out.println("X " + Math.round(t.location().getX())+" Y " + Math.round(t.location().getY()));

	Turtle t2 = new Turtle();
	context.setTurtle(t2);

	AbstractVisitor<List<Command>> v = new StepByStepExecutionVisitor(context);
	exp.accept(v);
	List<Command> commandList = v.getResult();

	for(int i=0;i<commandList.size();i++){
		commandList.get(i).execute();
	}

	System.out.println("Turtle location: " + Math.round(t2.location().getX()) + " " + Math.round(t2.location().getY()));


	AbstractVisitor<Integer> distanceVisitor = new DistanceCalculationVisitor(context);
	exp.accept(distanceVisitor);

	System.out.println("Distance Visitor: "+ distanceVisitor.getResult());
	}
}
