import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import Commands.Command;
import Helpers.Context;
import Helpers.Evaluator;
import Turtle.Turtle;
import Visitor.AbstractVisitor;
import Visitor.DistanceCalculationVisitor;
import Visitor.StepByStepExecutionVisitor;

public class TurtleGraphicsTest {

    private double delta = 0.01;

    @Test
    public void turtleTest() throws FileNotFoundException {

        Evaluator evaluator = new Evaluator("turtle1.txt");
        evaluator.makeSyntaxTree();
        Context context = evaluator.getContext();

        Turtle turtleInterpreter = getResultUsingInterpreter(context, evaluator);
        List<Command> commandList = getCommandListUsingVisitor(context, evaluator);

        Turtle turtleVisitor = new Turtle();
        for (int i = 0; i < commandList.size(); i++) {
            turtleVisitor = commandList.get(i).execute();
        }

        int totalDistance = getTotalDistanceUsingVisitor(context, evaluator);
        Assert.assertEquals(totalDistance,32);
    }

    @Test
    public void turtleTestXCoordinate() throws FileNotFoundException {

        Evaluator evaluator = new Evaluator("turtle.txt");
        evaluator.makeSyntaxTree();
        Context context = evaluator.getContext();

        Turtle turtleInterpreter = getResultUsingInterpreter(context, evaluator);
        List<Command> commandList = getCommandListUsingVisitor(context, evaluator);

        Turtle turtleVisitor = new Turtle();
        for (int i = 0; i < commandList.size(); i++) {
            turtleVisitor = commandList.get(i).execute();
        }

        int totalDistance = getTotalDistanceUsingVisitor(context, evaluator);
        Assert.assertEquals(context.getTurtle().location().getX(), 32400, delta);
    }

    @Test
    public void turtleTestXCoordinateAfterMove() throws FileNotFoundException {

        Evaluator evaluator = new Evaluator("turtle1.txt");
        evaluator.makeSyntaxTree();
        Context context = evaluator.getContext();

        Turtle turtleInterpreter = getResultUsingInterpreter(context, evaluator);
        List<Command> commandList = getCommandListUsingVisitor(context, evaluator);

        Turtle turtleVisitor = new Turtle();
        for (int i = 0; i < commandList.size(); i++) {
            turtleVisitor = commandList.get(i).execute();
        }

        int totalDistance = getTotalDistanceUsingVisitor(context, evaluator);
        Assert.assertEquals(context.getTurtle().location().getX(), 19.98779099427003, delta);
    }

    @Test
    public void turtleTestYCoordinateAfterMove() throws FileNotFoundException {

        Evaluator evaluator = new Evaluator("turtle1.txt");
        evaluator.makeSyntaxTree();
        Context context = evaluator.getContext();

        Turtle turtleInterpreter = getResultUsingInterpreter(context, evaluator);
        List<Command> commandList = getCommandListUsingVisitor(context, evaluator);

        Turtle turtleVisitor = new Turtle();
        for (int i = 0; i < commandList.size(); i++) {
            turtleVisitor = commandList.get(i).execute();
        }

        int totalDistance = getTotalDistanceUsingVisitor(context, evaluator);
        Assert.assertEquals(context.getTurtle().location().getY(), 19.60214353214409, delta);
    }


    public Turtle getResultUsingInterpreter(Context context, Evaluator evaluator) {
        context.setTurtle(new Turtle());
        Turtle turtle = evaluator.interpret(context);
        return turtle;
    }

    public List<Command> getCommandListUsingVisitor(Context context,
                                                    Evaluator evaluator) {
        context.setTurtle(new Turtle());
        AbstractVisitor<List<Command>> commandVisitor = new StepByStepExecutionVisitor(
                context);
        evaluator.accept(commandVisitor);
        List<Command> commandList = commandVisitor.getResult();
        return commandList;
    }

    public int getTotalDistanceUsingVisitor(Context context, Evaluator evaluator) {
        context.setTurtle(new Turtle());
        AbstractVisitor<Integer> distanceVisitor = new DistanceCalculationVisitor(
                context);
        evaluator.accept(distanceVisitor);
        int totalDistance = distanceVisitor.getResult();
        return totalDistance;
    }

}
