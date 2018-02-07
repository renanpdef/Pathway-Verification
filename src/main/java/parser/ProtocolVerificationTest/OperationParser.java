package parser.ProtocolVerificationTest;

import java.util.List;


import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import protocolosv2.Operation;

public class OperationParser { 
	OperandParser operands = new OperandParser();  //Instantiates the class that handles operations between BoolVar variables.
	int index = 0;
	
	//return a sequence representation as a BoolVar.
	//operation is the operation of a sequence from the protocol.
	//boolVars is a list of the logical operands that makes up all operations in the protocol.
	//intVars is a list of the numeric operands that makes up all operations in the protocol.
	public BoolVar createBoolVarSequence(Operation operation, List<BoolVar> boolVars, List<IntVar> intVars){
		Model auxModel = new Model("Auxiliary Model");
		BoolVar boolSequence = null;
		
		//operation.getOperand() is a list of operands from operation whose size can be 1 or 2
		//varA and varB is a string to verify if the operand is a operation or not.
		String varA = operation.getOperand().get(0).getClass().toString();
		String varB = "";
		
		//whether there are two operands in operation.
		if(operation.getOperand().size() > 1) {
			varB = operation.getOperand().get(1).getClass().toString();
		}
		
		//Create a Boolvar with a constraint corresponding to the operator from operation and return it.
		//For each case, verify if the operands from operation are other operation or not.
		//When the operand is a operation:
			//For AND, OR, IMPLIES, XOR or Default (NOT) operators:
				//this function is called recursively with operand as a parameter.
			//For EQUAL, EQUAL_OR_GREATER, EQUAL_OR_SMALLER, BIGGER_THAN, SMALLER_THAN operators:
				//the function calculate is called.
		switch(operation.getOperator()) {
			case AND:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) operation.getOperand().get(0), boolVars, intVars), "+", createBoolVarSequence((Operation) operation.getOperand().get(1), boolVars, intVars), "=", 2).reify();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) operation.getOperand().get(0), boolVars, intVars), "+", boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(1))), "=", 2).reify();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(0))), "+",createBoolVarSequence((Operation) operation.getOperand().get(1), boolVars, intVars), "=", 2).reify();
				}
				else {
					boolSequence = auxModel.arithm(boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(0))), "+", boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(1))), "=", 2).reify();
				}		
				return boolSequence;
				
			case OR:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) operation.getOperand().get(0), boolVars, intVars), "+", createBoolVarSequence((Operation) operation.getOperand().get(1), boolVars, intVars), "!=", 0).reify();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) operation.getOperand().get(0), boolVars, intVars), "+", boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(1))), "!=", 0).reify();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(0))), "+",createBoolVarSequence((Operation) operation.getOperand().get(1), boolVars, intVars), "!=", 0).reify();
				}
				else {
					boolSequence = auxModel.arithm(boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(0))), "+", boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(1))), "!=", 0).reify();
				}		
				return boolSequence;
				
			case IMPLIES:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) operation.getOperand().get(0), boolVars, intVars), "-", createBoolVarSequence((Operation) operation.getOperand().get(1), boolVars, intVars), "!=", 1).reify();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) operation.getOperand().get(0), boolVars, intVars), "-", boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(1))), "!=", 1).reify();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(0))), "-",createBoolVarSequence((Operation) operation.getOperand().get(1), boolVars, intVars), "!=", 1).reify();
				}
				else {
					boolSequence = auxModel.arithm(boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(0))), "-", boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(1))), "!=", 1).reify();
				}		
				return boolSequence;
				
			case XOR:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) operation.getOperand().get(0), boolVars, intVars), "+", createBoolVarSequence((Operation) operation.getOperand().get(1), boolVars, intVars), "=", 1).reify();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) operation.getOperand().get(0), boolVars, intVars), "+", boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(1))), "=", 1).reify();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(0))), "+",createBoolVarSequence((Operation) operation.getOperand().get(1), boolVars, intVars), "=", 1).reify();
				}
				else {
					boolSequence = auxModel.arithm(boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(0))), "+", boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(1))), "=", 1).reify();
				}		
				return boolSequence;
				
			case EQUAL:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) operation.getOperand().get(0), intVars), "=", calculate((Operation) operation.getOperand().get(1), intVars)).reify();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) operation.getOperand().get(0), intVars), "=", intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(1)))).reify();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(0))), "=",calculate((Operation) operation.getOperand().get(1), intVars)).reify();
				}
				else {
					boolSequence = auxModel.arithm(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(0))), "=", intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(1)))).reify();
				}
				return boolSequence; 
				
			case EQUAL_OR_GREATER:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) operation.getOperand().get(0), intVars), ">=", calculate((Operation) operation.getOperand().get(1), intVars)).reify();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) operation.getOperand().get(0), intVars), ">=", intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(1)))).reify();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(0))), ">=",calculate((Operation) operation.getOperand().get(1), intVars)).reify();
				}
				else {
					boolSequence = auxModel.arithm(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(0))), ">=", intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(1)))).reify();
				}
				return boolSequence; 
		
			case EQUAL_OR_SMALLER:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) operation.getOperand().get(0), intVars), "<=", calculate((Operation) operation.getOperand().get(1), intVars)).reify();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) operation.getOperand().get(0), intVars), "<=", intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(1)))).reify();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(0))), "<=",calculate((Operation) operation.getOperand().get(1), intVars)).reify();
				}
				else {
					boolSequence = auxModel.arithm(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(0))), "<=", intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(1)))).reify();
				}
				return boolSequence; 
				 
			case BIGGER_THAN:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) operation.getOperand().get(0), intVars), ">", calculate((Operation) operation.getOperand().get(1), intVars)).reify();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) operation.getOperand().get(0), intVars), ">", intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(1)))).reify();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(0))), ">",calculate((Operation) operation.getOperand().get(1), intVars)).reify();
				}
				else {
					boolSequence = auxModel.arithm(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(0))), ">", intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(1)))).reify();
				}
				return boolSequence; 
				
			case SMALLER_THAN:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) operation.getOperand().get(0), intVars), "<", calculate((Operation) operation.getOperand().get(1), intVars)).reify();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) operation.getOperand().get(0), intVars), "<", intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(1)))).reify();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(0))), "<",calculate((Operation) operation.getOperand().get(1), intVars)).reify();
				}
				else {
					boolSequence = auxModel.arithm(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(0))), "<", intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(1)))).reify();
				}
				return boolSequence;
			
			case AFFIRMATION:
				if(varA.contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) operation.getOperand().get(0), boolVars, intVars), "=", 1).reify();
				}
				else {
					boolSequence = auxModel.arithm(boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(0))), "=", 1).reify();
				}		
				return boolSequence;
			
			//This is the NOT operator.
			default:
				if(varA.contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) operation.getOperand().get(0), boolVars, intVars), "+", createBoolVarSequence((Operation) operation.getOperand().get(0), boolVars, intVars), "=", 0).reify();
				}
				else {
					boolSequence = auxModel.arithm(boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(0))), "+", boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(0))), "=", 0).reify();
				}		
				return boolSequence;
		}
	}
	
	//Return an IntVar as a result of a possible sums.
	//operation is the operation of a sequence from the protocol.
	//intVars is a list of the numeric operands that makes up all operations in the protocol.
	public IntVar calculate(Operation operation, List<IntVar> intVars) {
		IntVar result;
		
		//operation.getOperand() is a list of operands from operation whose size can be 1 or 2;
		//varA and varB are strings to verify if the operand is a operation or not.
		String varA = operation.getOperand().get(0).getClass().toString();
		String varB = operation.getOperand().get(1).getClass().toString(); 
		
		//For each case, verify if the operands of operation are other operation or not.
		//When the operand is a operation, this method is called recursively with operand as a parameter.
		switch(operation.getOperator()) {
			case SUM:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					result = calculate((Operation) operation.getOperand().get(0), intVars).add(calculate((Operation) operation.getOperand().get(1), intVars)).intVar();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					result = calculate((Operation) operation.getOperand().get(0), intVars).add(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(1)))).intVar();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					result = intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(0))).add(calculate((Operation) operation.getOperand().get(1), intVars)).intVar();
				}
				else {
					result = intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(0))).add(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(1)))).intVar();
				}
				return result;
				
			case MINUS:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					result = calculate((Operation) operation.getOperand().get(0), intVars).sub(calculate((Operation) operation.getOperand().get(1), intVars)).intVar();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					result = calculate((Operation) operation.getOperand().get(0), intVars).sub(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(1)))).intVar();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					result = intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(0))).sub(calculate((Operation) operation.getOperand().get(1), intVars)).intVar();
				}
				else {
					result = intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(0))).sub(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(1)))).intVar();
				}
				return result;
				
			case MULTIPLICATION:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					result = calculate((Operation) operation.getOperand().get(0), intVars).mul(calculate((Operation) operation.getOperand().get(1), intVars)).intVar();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					result = calculate((Operation) operation.getOperand().get(0), intVars).mul(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(1)))).intVar();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					result = intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(0))).mul(calculate((Operation) operation.getOperand().get(1), intVars)).intVar();
				}
				else {
					result = intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(0))).mul(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(1)))).intVar();
				}
				return result;
				
			case DIVISION:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					result = calculate((Operation) operation.getOperand().get(0), intVars).div(calculate((Operation) operation.getOperand().get(1), intVars)).intVar();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					result = calculate((Operation) operation.getOperand().get(0), intVars).div(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(1)))).intVar();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					result = intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(0))).div(calculate((Operation) operation.getOperand().get(1), intVars)).intVar();
				}
				else {
					result = intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(0))).div(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(1)))).intVar();
				}
				return result;
				
			default:
				return null;
		}
	}
}
