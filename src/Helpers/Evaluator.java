package Helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

import Turtle.Turtle;
import Visitor.AbstractVisitor;
import Expressions.*;


// This method will first read the program from the text file
// then it will create an expression object by parsing the lines (commands)
// finally it will make a syntax tree and represent it as an Abstract Syntax Tree.

public class Evaluator {

	private String fileName;
	private String expression;
	private int fileLineCounter = 0, nestedRepeatCount = 0;
	private String[] expressionSplitArray;
	private Context context = new Context();
	private AbstractExpression<Void> root = null, leaf = null;
	private Stack<AbstractExpression<Void>> repeatStack = new Stack<>();
	private Stack<AbstractExpression<Integer>> repeatCountStack = new Stack<>();

	public Evaluator(String fileName) throws FileNotFoundException {
		this.fileName = fileName;
	}


	public void makeSyntaxTree() throws FileNotFoundException {
		context.removeValues();
		context.removePoints();
		Scanner fileReader = new Scanner(new File(fileName));
		while (fileReader.hasNextLine()) {
			fileLineCounter++;
			expression = fileReader.nextLine().trim();
			expressionSplitArray = expression.split(" ");
			if (expressionSplitArray.length == 2) {
				String argument = expressionSplitArray[1].trim();
				String operation = expressionSplitArray[0].trim();
				checkForSyntaxError(operation, argument);
				if (argument.startsWith("#")) {
					handleExpressionWithVariable(operation);
				} else {
					handleExpressionWithConstant(operation);
				}

			} else if (expressionSplitArray.length == 1
					|| expressionSplitArray.length == 3
					|| expressionSplitArray.length == 4) {
				checkForSyntaxError(expression, expressionSplitArray.length);
				if (root == null) {
					handleExpRoot(expression, null);
				} else {
					handleExpLeaf(expression, null);
				}
			} else {
				fileReader.close();
				throw new RuntimeException("Expression at line " + fileLineCounter + " is not defined");
			}
		}
		fileReader.close();

		if (nestedRepeatCount != 0) {
			throw new RuntimeException("End expression is missing at Line " + fileLineCounter );
		}
	}

	public void handleExpressionWithVariable(String operation) {
		String name = expressionSplitArray[1].trim();
		AbstractExpression<Integer> variable = new Variable(name, nestedRepeatCount);
		if (operation.equalsIgnoreCase("repeat")) repeatCountStack.push(variable);
		if (root == null) {
			handleExpRoot(operation, variable);
		} else {
			handleExpLeaf(operation, variable);
		}
	}

	public Turtle interpret(Context context) {
		root.interpret(context);
		return context.getTurtle();
	}

	public void accept(AbstractVisitor v) {
		AbstractExpression<Void> exp = root;

		while (exp != null) {
			exp.accept(v);
			exp = exp.getNext();
		}
	}

	public Context getContext() {
		return context;
	}

	public void handleExpressionWithConstant(String operation) {
		String value = expressionSplitArray[1].trim();
		int IntegerValue = 0;
		try {
			IntegerValue = Integer.parseInt(value);
		} catch (Exception e) {
		}

		AbstractExpression<Integer> constant = new Constant(IntegerValue);
		if (operation.equalsIgnoreCase("repeat"))
			repeatCountStack.push(constant);
		if (root == null) {
			handleExpRoot(operation, constant);
		} else {
			handleExpLeaf(operation, constant);
		}
	}

	// we create a stack to handle the repeat logic.

	public void handleExpLeaf(String operation,
			AbstractExpression<Integer> argument) {
		AbstractExpression<Void> temporaryExpression;
		if (operation.equalsIgnoreCase("move")) {
			temporaryExpression = new MoveExpression(argument);
			leaf.setNext(temporaryExpression);
			leaf = temporaryExpression;
		} else if (operation.equalsIgnoreCase("turn")) {
			temporaryExpression = new TurnExpression(argument);
			leaf.setNext(temporaryExpression);
			leaf = temporaryExpression;
		} else if (operation.equalsIgnoreCase("repeat")) {
			temporaryExpression = new RepeatExpression(argument);
			leaf.setNext(temporaryExpression);
			leaf = temporaryExpression;
			nestedRepeatCount++;
			repeatStack.push(leaf);
		} else if (operation.equalsIgnoreCase("end")) {
			temporaryExpression = new EndExpression(repeatStack.pop(),
					repeatCountStack.pop());
			leaf.setNext(temporaryExpression);
			leaf = temporaryExpression;
			nestedRepeatCount--;
		} else if (operation.startsWith("#")) {
			if (nestedRepeatCount > 0)
				operation = nestedRepeatCount + operation;
			temporaryExpression = parseAssignmentExpression(operation);
			leaf.setNext(temporaryExpression);
			leaf = temporaryExpression;
		} else if (!expression.isEmpty()) {
			throw new RuntimeException("Syntax error at line "
					+ fileLineCounter);
		}
	}

	public void handleExpRoot(String operation,
							  AbstractExpression<Integer> argument) {

		if (operation.equalsIgnoreCase("move")) {
			root = new MoveExpression(argument);
			leaf = root;
		} else if (operation.equalsIgnoreCase("turn")) {
			root = new TurnExpression(argument);
			leaf = root;
		} else if (operation.equalsIgnoreCase("repeat")) {
			nestedRepeatCount++;
			root = new RepeatExpression(argument);
			leaf = root;
			repeatStack.push(leaf);
		} else if (operation.startsWith("#")) {
			root = parseAssignmentExpression(operation);
			leaf = root;
		} else if (!expression.isEmpty())
			throw new RuntimeException("Syntax error at line "
					+ fileLineCounter);
	}

	public AbstractExpression<Void> parseAssignmentExpression(String expression) {

		AbstractExpression<Void> exp;
		String[] keyValues = expression.split("=");
		String key = keyValues[0].trim();
		String variableKey = "";
		int value = 0;
		double xCoordinate = 0.0;
		double yCoordinate = 0.0;
		boolean pointValue = false;
		boolean distanceTo = false;
		boolean bearingTo = false;
		try {
			if(keyValues[1].contains(",")){
				pointValue = true;
				String[] point = keyValues[1].trim().split(",");
				xCoordinate = Double.parseDouble(point[0].trim());
				yCoordinate = Double.parseDouble(point[1].trim());
			}
			else if(keyValues[1].contains("distanceTo")){
				distanceTo = true;
				String[] temp = keyValues[1].trim().split(" ");
				variableKey = temp[1].trim();
				System.out.println(key + " " + variableKey);
			}
			else if(keyValues[1].contains("bearingTo")){
				bearingTo = true;
				String[] temp = keyValues[1].trim().split(" ");
				variableKey = temp[1].trim();
				System.out.println(key + " " + variableKey);
			}
			else{
				value = Integer.parseInt(keyValues[1].trim());
			}

		} catch (Exception e) {
		}
		if(pointValue){
			key = key.replace("P","");
			System.out.println(key + " " + xCoordinate + " " + yCoordinate);
			exp = new PointAssignmentExpression(key, xCoordinate, yCoordinate);
		}else if(distanceTo){
			exp = new DistanceToExpression(key, variableKey);
		}else if(bearingTo){
			exp = new BearingToExpression(key, variableKey);
		}else{
			exp = new AssignmentExpression(key, value);
		}
		return exp;
	}

	public void checkForSyntaxError(String operation, String argument) {
		if (operation.equalsIgnoreCase("move")
				|| operation.equalsIgnoreCase("turn")
				|| operation.equalsIgnoreCase("repeat")) {
			if (!argument.startsWith("#")) {
				try {
					Integer.parseInt(argument);
				} catch (Exception e) {
					throw new RuntimeException(
							"Given value is not an Integer at line "
									+ fileLineCounter);
				}
			}
		} else
			throw new RuntimeException("Unidentified expression at Line "
					+ fileLineCounter);
	}

	public void checkForSyntaxError(String expression, int length) {
		if (length == 4) {
			if (expression.startsWith("#P")) {
				String[] expArray = expression.split("=");
				try {
					expArray[1] = expArray[1].trim();
					String[] expArgs = expArray[1].split(",");
					Integer.parseInt(expArgs[0].trim());
					Integer.parseInt(expArgs[1].trim());
				}
				catch (Exception e) {
					throw new RuntimeException(
							"Given x and y coordinates for point is not an Integer at Line "
									+ fileLineCounter);
				}
			} else if(expression.startsWith("#")){
				String[] expArray = expression.split("=");
				try {
					String[] expArgs = expArray[1].trim().split(" ");
					if(!(expArgs[0].equals("bearingTo") || expArgs[0].equals("distanceTo"))){
						throw new Exception();
					}
					if(!expArgs[1].startsWith("#")){
						throw new Exception();
					}
				}catch (Exception e){
					throw new RuntimeException(" Unidentified expression at Line "
							+ fileLineCounter);
				}
			} else
				throw new RuntimeException("Unidentified expression at Line "
						+ fileLineCounter);
		} else if(length == 3){
			if (expression.startsWith("#")) {
				String[] expArray = expression.split(" ");
				try {
					Integer.parseInt(expArray[2]);
				} catch (Exception e) {
					throw new RuntimeException(
							"Given value is not an Integer at Line "
									+ fileLineCounter);
				}
			} else {
				throw new RuntimeException("Unidentified expression at Line"
						+ fileLineCounter);
			}
		} else{
			if(expression.equalsIgnoreCase("end")
					|| expression.isEmpty()){
			}else{
				throw new RuntimeException("Unidentified expression at Line "
						+ fileLineCounter);
			}
		}
	}
}
