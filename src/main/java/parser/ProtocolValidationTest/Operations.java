package parser.ProtocolValidationTest;

import java.util.List;


import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import protocolosv2.Operation;

public class Operations { 
	IntVarOperations intOp = new IntVarOperations(); //Instantiates the class that handles operations between IntVar variables.
	BoolVarOperations boolOp = new BoolVarOperations();  //Instantiates the class that handles operations between BoolVar variables.
	int index = 0;
	
	//return a sequence representation as a BoolVar.
	//op is the operation of each sequence with the same output step.
	//boolVars is a list of the logical operands that make up all operation in the protocol.
	//index is a vector with the index of the operands in the list boolVars that are used in the operation op. 
	public BoolVar createBoolVarSequence(Operation op, List<BoolVar> boolVars, List<IntVar> intVars){
		Model auxModel = new Model("Auxiliary Model");
		BoolVar boolSequence = null;
		String varA = op.getOperand().get(0).getClass().toString();
		String varB = op.getOperand().get(1).getClass().toString(); 
		
		switch(op.getOperator()) {
			case AND:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) op.getOperand().get(0), boolVars, intVars), "+", createBoolVarSequence((Operation) op.getOperand().get(1), boolVars, intVars), "=", 2).reify();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) op.getOperand().get(0), boolVars, intVars), "+", boolVars.get(boolOp.indexOf(boolVars, op.getOperand().get(1))), "=", 2).reify();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(boolVars.get(boolOp.indexOf(boolVars, op.getOperand().get(0))), "+",createBoolVarSequence((Operation) op.getOperand().get(1), boolVars, intVars), "=", 2).reify();
				}
				else {
					boolSequence = auxModel.arithm(boolVars.get(boolOp.indexOf(boolVars, op.getOperand().get(0))), "+", boolVars.get(boolOp.indexOf(boolVars, op.getOperand().get(1))), "=", 2).reify();
				}		
				return boolSequence;
				
			case OR:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) op.getOperand().get(0), boolVars, intVars), "+", createBoolVarSequence((Operation) op.getOperand().get(1), boolVars, intVars), "!=", 0).reify();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) op.getOperand().get(0), boolVars, intVars), "+", boolVars.get(boolOp.indexOf(boolVars, op.getOperand().get(1))), "!=", 0).reify();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(boolVars.get(boolOp.indexOf(boolVars, op.getOperand().get(0))), "+",createBoolVarSequence((Operation) op.getOperand().get(1), boolVars, intVars), "!=", 0).reify();
				}
				else {
					boolSequence = auxModel.arithm(boolVars.get(boolOp.indexOf(boolVars, op.getOperand().get(0))), "+", boolVars.get(boolOp.indexOf(boolVars, op.getOperand().get(1))), "!=", 0).reify();
				}		
				return boolSequence;
				
			case IMPLIES:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) op.getOperand().get(0), boolVars, intVars), "-", createBoolVarSequence((Operation) op.getOperand().get(1), boolVars, intVars), "!=", 1).reify();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) op.getOperand().get(0), boolVars, intVars), "-", boolVars.get(boolOp.indexOf(boolVars, op.getOperand().get(1))), "!=", 1).reify();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(boolVars.get(boolOp.indexOf(boolVars, op.getOperand().get(0))), "-",createBoolVarSequence((Operation) op.getOperand().get(1), boolVars, intVars), "!=", 1).reify();
				}
				else {
					boolSequence = auxModel.arithm(boolVars.get(boolOp.indexOf(boolVars, op.getOperand().get(0))), "-", boolVars.get(boolOp.indexOf(boolVars, op.getOperand().get(1))), "!=", 1).reify();
				}		
				return boolSequence;
				
			case XOR:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) op.getOperand().get(0), boolVars, intVars), "+", createBoolVarSequence((Operation) op.getOperand().get(1), boolVars, intVars), "=", 1).reify();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) op.getOperand().get(0), boolVars, intVars), "+", boolVars.get(boolOp.indexOf(boolVars, op.getOperand().get(1))), "=", 1).reify();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(boolVars.get(boolOp.indexOf(boolVars, op.getOperand().get(0))), "+",createBoolVarSequence((Operation) op.getOperand().get(1), boolVars, intVars), "=", 1).reify();
				}
				else {
					boolSequence = auxModel.arithm(boolVars.get(boolOp.indexOf(boolVars, op.getOperand().get(0))), "+", boolVars.get(boolOp.indexOf(boolVars, op.getOperand().get(1))), "=", 1).reify();
				}		
				return boolSequence;
				
			case EQUAL:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) op.getOperand().get(0), intVars), "=", calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) op.getOperand().get(0), intVars), "=", intVars.get(intOp.indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(intVars.get(intOp.indexOf(intVars, op.getOperand().get(0))), "=",calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else {
					boolSequence = auxModel.arithm(intVars.get(intOp.indexOf(intVars, op.getOperand().get(0))), "=", intVars.get(intOp.indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				return boolSequence; 
				
			case EQUAL_OR_GREATER:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) op.getOperand().get(0), intVars), ">=", calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) op.getOperand().get(0), intVars), ">=", intVars.get(intOp.indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(intVars.get(intOp.indexOf(intVars, op.getOperand().get(0))), ">=",calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else {
					boolSequence = auxModel.arithm(intVars.get(intOp.indexOf(intVars, op.getOperand().get(0))), ">=", intVars.get(intOp.indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				return boolSequence; 
		
			case EQUAL_OR_SMALLER:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) op.getOperand().get(0), intVars), "<=", calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) op.getOperand().get(0), intVars), "<=", intVars.get(intOp.indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(intVars.get(intOp.indexOf(intVars, op.getOperand().get(0))), "<=",calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else {
					boolSequence = auxModel.arithm(intVars.get(intOp.indexOf(intVars, op.getOperand().get(0))), "<=", intVars.get(intOp.indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				return boolSequence; 
				 
			case BIGGER_THAN:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) op.getOperand().get(0), intVars), ">", calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) op.getOperand().get(0), intVars), ">", intVars.get(intOp.indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(intVars.get(intOp.indexOf(intVars, op.getOperand().get(0))), ">",calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else {
					boolSequence = auxModel.arithm(intVars.get(intOp.indexOf(intVars, op.getOperand().get(0))), ">", intVars.get(intOp.indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				return boolSequence; 
				
			case SMALLER_THAN:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) op.getOperand().get(0), intVars), "<", calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) op.getOperand().get(0), intVars), "<", intVars.get(intOp.indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					boolSequence = auxModel.arithm(intVars.get(intOp.indexOf(intVars, op.getOperand().get(0))), "<",calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else {
					boolSequence = auxModel.arithm(intVars.get(intOp.indexOf(intVars, op.getOperand().get(0))), "<", intVars.get(intOp.indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				return boolSequence; 
				
			default:
				if(varA.contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) op.getOperand().get(0), boolVars, intVars), "+", createBoolVarSequence((Operation) op.getOperand().get(0), boolVars, intVars), "=", 0).reify();
				}
				else {
					boolSequence = auxModel.arithm(boolVars.get(boolOp.indexOf(boolVars, op.getOperand().get(0))), "+", boolVars.get(boolOp.indexOf(boolVars, op.getOperand().get(0))), "=", 0).reify();
				}		
				return boolSequence;
		}
	}
		
	public IntVar calculate(Operation op, List<IntVar> intVars) {
		IntVar result;
		String varA = op.getOperand().get(0).getClass().toString();
		String varB = op.getOperand().get(1).getClass().toString(); 
		
		switch(op.getOperator()) {
			case SUM:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					result = calculate((Operation) op.getOperand().get(0), intVars).add(calculate((Operation) op.getOperand().get(1), intVars)).intVar();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					result = calculate((Operation) op.getOperand().get(0), intVars).add(intVars.get(intOp.indexOf(intVars, op.getOperand().get(1)))).intVar();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					result = intVars.get(intOp.indexOf(intVars, op.getOperand().get(0))).add(calculate((Operation) op.getOperand().get(1), intVars)).intVar();
				}
				else {
					result = intVars.get(intOp.indexOf(intVars, op.getOperand().get(0))).add(intVars.get(intOp.indexOf(intVars, op.getOperand().get(1)))).intVar();
				}
				return result;
				
			case MINUS:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					result = calculate((Operation) op.getOperand().get(0), intVars).sub(calculate((Operation) op.getOperand().get(1), intVars)).intVar();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					result = calculate((Operation) op.getOperand().get(0), intVars).sub(intVars.get(intOp.indexOf(intVars, op.getOperand().get(1)))).intVar();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					result = intVars.get(intOp.indexOf(intVars, op.getOperand().get(0))).sub(calculate((Operation) op.getOperand().get(1), intVars)).intVar();
				}
				else {
					result = intVars.get(intOp.indexOf(intVars, op.getOperand().get(0))).sub(intVars.get(intOp.indexOf(intVars, op.getOperand().get(1)))).intVar();
				}
				return result;
				
			case MULTIPLICATION:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					result = calculate((Operation) op.getOperand().get(0), intVars).mul(calculate((Operation) op.getOperand().get(1), intVars)).intVar();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					result = calculate((Operation) op.getOperand().get(0), intVars).mul(intVars.get(intOp.indexOf(intVars, op.getOperand().get(1)))).intVar();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					result = intVars.get(intOp.indexOf(intVars, op.getOperand().get(0))).mul(calculate((Operation) op.getOperand().get(1), intVars)).intVar();
				}
				else {
					result = intVars.get(intOp.indexOf(intVars, op.getOperand().get(0))).mul(intVars.get(intOp.indexOf(intVars, op.getOperand().get(1)))).intVar();
				}
				return result;
				
			case DIVISION:
				if(varA.contains("Operation") && varB.contains("Operation")) {
					result = calculate((Operation) op.getOperand().get(0), intVars).div(calculate((Operation) op.getOperand().get(1), intVars)).intVar();
				}
				else if(varA.contains("Operation") && !varB.contains("Operation")) {
					result = calculate((Operation) op.getOperand().get(0), intVars).div(intVars.get(intOp.indexOf(intVars, op.getOperand().get(1)))).intVar();
				}
				else if(!varA.contains("Operation") && varB.contains("Operation")) {
					result = intVars.get(intOp.indexOf(intVars, op.getOperand().get(0))).div(calculate((Operation) op.getOperand().get(1), intVars)).intVar();
				}
				else {
					result = intVars.get(intOp.indexOf(intVars, op.getOperand().get(0))).div(intVars.get(intOp.indexOf(intVars, op.getOperand().get(1)))).intVar();
				}
				return result;
				
			default:
				return null;
		}
	}
}
