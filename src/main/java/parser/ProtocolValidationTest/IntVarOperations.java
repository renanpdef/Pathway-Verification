package parser.ProtocolValidationTest;

import java.util.List;


import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import protocolosv2.Operand;
import protocolosv2.Operation;
import protocolosv2.Sequence;

public class IntVarOperations {
	int index = 0;
	
	//return a sequence representation as a BoolVar.
	//op is the operation of each sequence with the same output step.
	//intVars is a list of the numeric operands that make up all operation in the protocol.
	//index is a vector with the index of the operands in the list intVars that are used in the operation op. 
	public BoolVar createBoolVarSequence(Operation op, List<IntVar> intVars) {
		Model auxModel = new Model("Axiliary IntVar Model");
		BoolVar boolSequence = null;
		
		switch(op.getOperator()) {
			case EQUAL:
				if(op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) op.getOperand().get(0), intVars), "=", calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else if(op.getOperand().get(0).getClass().toString().contains("Operation") && !op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) op.getOperand().get(0), intVars), "=", intVars.get(indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				else if(!op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(intVars.get(indexOf(intVars, op.getOperand().get(0))), "=",calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else {
					boolSequence = auxModel.arithm(intVars.get(indexOf(intVars, op.getOperand().get(0))), "=", intVars.get(indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				return boolSequence; 
				
			case EQUAL_OR_GREATER:
				if(op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) op.getOperand().get(0), intVars), ">=", calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else if(op.getOperand().get(0).getClass().toString().contains("Operation") && !op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) op.getOperand().get(0), intVars), ">=", intVars.get(indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				else if(!op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(intVars.get(indexOf(intVars, op.getOperand().get(0))), ">=",calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else {
					boolSequence = auxModel.arithm(intVars.get(indexOf(intVars, op.getOperand().get(0))), ">=", intVars.get(indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				return boolSequence; 
		
			case EQUAL_OR_SMALLER:
				if(op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) op.getOperand().get(0), intVars), "<=", calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else if(op.getOperand().get(0).getClass().toString().contains("Operation") && !op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) op.getOperand().get(0), intVars), "<=", intVars.get(indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				else if(!op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(intVars.get(indexOf(intVars, op.getOperand().get(0))), "<=",calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else {
					boolSequence = auxModel.arithm(intVars.get(indexOf(intVars, op.getOperand().get(0))), "<=", intVars.get(indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				return boolSequence; 
				 
			case BIGGER_THAN:
				if(op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) op.getOperand().get(0), intVars), ">", calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else if(op.getOperand().get(0).getClass().toString().contains("Operation") && !op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) op.getOperand().get(0), intVars), ">", intVars.get(indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				else if(!op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(intVars.get(indexOf(intVars, op.getOperand().get(0))), ">",calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else {
					boolSequence = auxModel.arithm(intVars.get(indexOf(intVars, op.getOperand().get(0))), ">", intVars.get(indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				return boolSequence; 
				
			case SMALLER_THAN:
				if(op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) op.getOperand().get(0), intVars), "<", calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else if(op.getOperand().get(0).getClass().toString().contains("Operation") && !op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(calculate((Operation) op.getOperand().get(0), intVars), "<", intVars.get(indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				else if(!op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(intVars.get(indexOf(intVars, op.getOperand().get(0))), "<",calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else {
					boolSequence = auxModel.arithm(intVars.get(indexOf(intVars, op.getOperand().get(0))), "<", intVars.get(indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				return boolSequence; 

			default:
				return boolSequence;
		}
	}
	
	public IntVar calculate(Operation op, List<IntVar> intVars) {
		Model auxModel = new Model("Axiliary IntVar Model");
		IntVar result = auxModel.intVar(0,4);
		BoolVar boolvar = null;
		
		switch(op.getOperator()) {
			case SUM:
				if(op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolvar = auxModel.arithm(result, "=", calculate((Operation) op.getOperand().get(0), intVars), "+", calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else if(op.getOperand().get(0).getClass().toString().contains("Operation") && !op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolvar = auxModel.arithm(result, "=", calculate((Operation) op.getOperand().get(0), intVars), "+", intVars.get(indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				else if(!op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolvar = auxModel.arithm(result, "=", intVars.get(indexOf(intVars, op.getOperand().get(0))), "+", calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else {
					boolvar = auxModel.arithm(result, "=", intVars.get(indexOf(intVars, op.getOperand().get(0))), "+", intVars.get(indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				auxModel.arithm(boolvar, "=", 1).post();
				return result;
				
			case MINUS:
				if(op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolvar = auxModel.arithm(result, "=", calculate((Operation) op.getOperand().get(0), intVars), "-", calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else if(op.getOperand().get(0).getClass().toString().contains("Operation") && !op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolvar = auxModel.arithm(result, "=", calculate((Operation) op.getOperand().get(0), intVars), "-", intVars.get(indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				else if(!op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolvar = auxModel.arithm(result, "=", intVars.get(indexOf(intVars, op.getOperand().get(0))), "-", calculate((Operation) op.getOperand().get(1), intVars)).reify();
				}
				else {
					boolvar = auxModel.arithm(result, "=", intVars.get(indexOf(intVars, op.getOperand().get(0))), "-", intVars.get(indexOf(intVars, op.getOperand().get(1)))).reify();
				}
				auxModel.arithm(boolvar, "=", 1).post();
				return result;
				
			case MULTIPLICATION:
				if(op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolvar = auxModel.times(calculate((Operation) op.getOperand().get(0), intVars), calculate((Operation) op.getOperand().get(1), intVars), result).reify();
				}
				else if(op.getOperand().get(0).getClass().toString().contains("Operation") && !op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolvar = auxModel.times(calculate((Operation) op.getOperand().get(0), intVars), intVars.get(indexOf(intVars, op.getOperand().get(1))), result).reify();
				}
				else if(!op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolvar = auxModel.times(intVars.get(indexOf(intVars, op.getOperand().get(0))), calculate((Operation) op.getOperand().get(1), intVars), result).reify();
				}
				else {
					boolvar = auxModel.times(intVars.get(indexOf(intVars, op.getOperand().get(0))), intVars.get(indexOf(intVars, op.getOperand().get(1))), result).reify();
				}
				auxModel.arithm(boolvar, "=", 1).post();
				return result;
				
			case DIVISION:
				if(op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolvar = auxModel.div(calculate((Operation) op.getOperand().get(0), intVars), calculate((Operation) op.getOperand().get(1), intVars), result).reify();
				}
				else if(op.getOperand().get(0).getClass().toString().contains("Operation") && !op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolvar = auxModel.div(calculate((Operation) op.getOperand().get(0), intVars), intVars.get(indexOf(intVars, op.getOperand().get(1))), result).reify();
				}
				else if(!op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolvar = auxModel.div(intVars.get(indexOf(intVars, op.getOperand().get(0))), calculate((Operation) op.getOperand().get(1), intVars), result).reify();
				}
				else {
					boolvar = auxModel.div(intVars.get(indexOf(intVars, op.getOperand().get(0))), intVars.get(indexOf(intVars, op.getOperand().get(1))), result).reify();
				}
				auxModel.arithm(boolvar, "=", 1).post();
				return result;
				
			default:
				return null;
		}
	}
		
	//get the operands from the operation and put it in the list intVars if the list do not contain it.
	//return the index of the operands of the operation in the intVars.
	public void operandsIntoIntVarList(List<IntVar> intVars, Model model, Operation operation) {		
		for(int i = 0; i < operation.getOperand().size(); i++) {
			Model auxModel = new Model("Auxiliary Model");
			if(operation.getOperand().get(i).getClass().toString().contains("Operation")) {
				operandsIntoIntVarList(intVars, model, (Operation) operation.getOperand().get(i));
			}
			else {
				if(operation.getOperand().get(i).getName() == null || operation.getOperand().get(i).getName() == "") {
					String name = operation.getOperator().getName() + index++;		
					
					operation.getOperand().get(i).setName(name);
					
					intVars.add(model.intVar(operation.getOperand().get(i).getName(), 0, 2));
				}
				else {
					IntVar intVar = auxModel.intVar(operation.getOperand().get(i).getName(), 0, 2);
					
					if(!contains(intVars, intVar)) {
						intVars.add(model.intVar(operation.getOperand().get(i).getName(), 0, 2));
					}
				}
			}
		}
	}
	
	//Verify whether list intVars already has the intVar
	public boolean contains(List<IntVar> intVars, IntVar intVar) {
		String name = intVar.getName();
		for(int i = 0; i < intVars.size(); i++) {
			String bName = intVars.get(i).getName();
			
			if(bName.equals(name)) {
				return true;
			}			
		}
		return false;
	}
	
	//retun the index of the intVar from the list intVars.
	public int indexOf(List<IntVar> intVars, Operand operand) {
		String name = operand.getName();
		for(int i = 0; i < intVars.size(); i++) {
			String bName = intVars.get(i).getName();
			if(bName.equals(name)) {
				return i;
			}
		}
		return -1;
	}
}
