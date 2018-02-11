package parser.ProtocolVerificationTest;

import java.util.ArrayList;
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
		//Create a Boolvar with a constraint corresponding to the operator from operation and return it.
		//For each case, verify if the operands from operation are other operation or not.
		//When the operand is a operation:
			//For AND, OR, IMPLIES, XOR or Default (NOT) operators:
				//this function is called recursively with operand as a parameter.
			//For EQUAL, EQUAL_OR_GREATER, EQUAL_OR_SMALLER, BIGGER_THAN, SMALLER_THAN operators:
				//the function calculate is called.
		switch(operation.getOperator()) {
			case AND:
				return boolLogicalOperations(operation, boolVars, intVars, "+", "=", 2);
				
			case OR:
				return boolLogicalOperations(operation, boolVars, intVars, "+", "!=", 0);
				
			case IMPLIES:
				return boolLogicalOperations(operation, boolVars, intVars, "-", "!=", 1);
				
			case XOR:
				return boolLogicalOperations(operation, boolVars, intVars, "+", "=", 1);
				
			case EQUAL:
				return boolRelacionalOperations(operation, intVars, "="); 
				
			case EQUAL_OR_GREATER:
				return boolRelacionalOperations(operation, intVars, ">="); 
		
			case EQUAL_OR_SMALLER:
				return boolRelacionalOperations(operation, intVars, "<="); 
				 
			case BIGGER_THAN:
				return boolRelacionalOperations(operation, intVars, ">"); 
				
			case SMALLER_THAN:
				return boolRelacionalOperations(operation, intVars, "<");
			
			case AFFIRMATION:
				Model auxModel = new Model("Auxiliary Model");
				BoolVar boolSequence = null;
				if(operation.getOperand().get(0).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) operation.getOperand().get(0), boolVars, intVars), "=", 1).reify();
				}
				else {
					boolSequence = auxModel.arithm(boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(0))), "=", 1).reify();
				}		
				return boolSequence;
			
			//This is the NOT operator.
			default:
				Model auxModel1 = new Model("Auxiliary Model");
				BoolVar boolSequence1 = null;
				if(operation.getOperand().get(0).getClass().toString().contains("Operation")) {
					boolSequence1 = auxModel1.arithm(createBoolVarSequence((Operation) operation.getOperand().get(0), boolVars, intVars), "+", createBoolVarSequence((Operation) operation.getOperand().get(0), boolVars, intVars), "=", 0).reify();
				}
				else {
					boolSequence1 = auxModel1.arithm(boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(0))), "+", boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(0))), "=", 0).reify();
				}		
				return boolSequence1;
		}
	}
	
	//Return an IntVar as a result of a possible sums.
	//operation is the operation of a sequence from the protocol.
	//intVars is a list of the numeric operands that makes up all operations in the protocol.
	private IntVar calculate(Operation operation, List<IntVar> intVars) {
		IntVar result;
		if(operation.getOperand().get(0).getClass().toString().contains("Operation")) {
			result = calculate((Operation) operation.getOperand().get(0), intVars);
		}else {
			result = intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(0)));
		}
		
		for(int i=1; i < operation.getOperand().size(); i++) {
			String strOperand = operation.getOperand().get(i).getClass().toString(); //strOperand is a string to verify if the operand is a operation or not.
			
			//For each case, verify if the operands of operation are other operation or not.
			//When the operand is a operation, this method is called recursively with operand as a parameter.
			switch(operation.getOperator()) {
				case SUM:
					if(strOperand.contains("Operation")) {
						result = result.add(calculate((Operation) operation.getOperand().get(i), intVars)).intVar();
					}
					else {
						result = result.add(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(i)))).intVar();
					}
					break;
					
				case MINUS:
					if(strOperand.contains("Operation")) {
						result = result.sub(calculate((Operation) operation.getOperand().get(i), intVars)).intVar();
					}
					else {
						result = result.sub(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(i)))).intVar();
					}
					break;
					
				case MULTIPLICATION:
					if(strOperand.contains("Operation")) {
						result = result.mul(calculate((Operation) operation.getOperand().get(i), intVars)).intVar();
					}
					else {
						result = result.mul(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(i)))).intVar();
					}
					break;
					
				case DIVISION:
					if(strOperand.contains("Operation")) {
						result = result.div(calculate((Operation) operation.getOperand().get(i), intVars)).intVar();
					}
					else {
						result = result.div(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(i)))).intVar();
					}
					break;
					
				default:
					result = null;
					break;
					
			}
		}
		return result;
	}
	
	//Function used for EQUAL, EQUAL_OR_GREATER, EQUAL_OR_SMALLER, BIGGER_THAN, SMALLER_THAN operators:
	//return the operation as a boolVar with some constraints.
	private BoolVar boolRelacionalOperations(Operation operation, List<IntVar> intVars, String operator) {
		Model auxModel = new Model("Auxiliary Model");
		BoolVar boolSequence = null; //stores a sequence representation as BoolVar with some constraints.	
		List<BoolVar> boolOperation = new ArrayList<BoolVar>();//stores the operation as BoolVar.
		
		//Verify if the operands from operation are other operation or not.
		//When the operand is a operation the function calculate is called.
		for(int i = 1; i < operation.getOperand().size(); i++) {
			String strOperand1 = operation.getOperand().get(i-1).getClass().toString();
			String strOperand2 = operation.getOperand().get(i).getClass().toString();
			if(strOperand1.contains("Operation") && strOperand2.contains("Operation")) {
				boolOperation.add(auxModel.arithm(calculate((Operation) operation.getOperand().get(i-1), intVars), operator, calculate((Operation) operation.getOperand().get(i), intVars)).reify());
			}
			else if(strOperand1.contains("Operation") && !strOperand2.contains("Operation")) {
				boolOperation.add(auxModel.arithm(calculate((Operation) operation.getOperand().get(i-1), intVars), operator, intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(i)))).reify());
			}
			else if(!strOperand1.contains("Operation") && strOperand2.contains("Operation")) {
				boolOperation.add(auxModel.arithm(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(i-1))), operator,calculate((Operation) operation.getOperand().get(i), intVars)).reify());
			}
			else {
				boolOperation.add(auxModel.arithm(intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(i-1))), operator, intVars.get(operands.indexOfIntVar(intVars, operation.getOperand().get(i)))).reify());
			}
			if(boolOperation.size() > 1) {
				auxModel.arithm(boolOperation.get(boolOperation.size()-1), "+",boolOperation.get(boolOperation.size()-2), "=", 2).reifyWith(boolSequence);
			}else {
				boolSequence = boolOperation.get(boolOperation.size()-1);
			}
		}
		return boolSequence;
	}
	
	//Function used for AND, OR, IMPLIES, XOR operators:
	//Return the operation as a boolVar with some constraints.
	private BoolVar boolLogicalOperations(Operation operation, List<BoolVar> boolVars, List<IntVar> intVars, String operator1, String operator2, int n) {
		Model auxModel = new Model("Auxiliary Model");
		List<BoolVar> boolOperation = new ArrayList<BoolVar>(); //stores the operation as BoolVar.
		
		//Verify if the first operand from operation are other operation or not.
		//When the operand is a operation the function createBoolVarSequence is called.
		if(operation.getOperand().get(0).getClass().toString().contains("Operation")) {
			boolOperation.add(createBoolVarSequence((Operation) operation.getOperand().get(0), boolVars, intVars));
		}else {
			boolOperation.add(boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(0))));
		}
		//Verify if the operands from operation are other operation or not.
		//When the operand is a operation the function createBoolVarSequence is called.
		for(int i = 1; i < operation.getOperand().size(); i++) {
			String strOperand = operation.getOperand().get(i).getClass().toString();
			if(strOperand.contains("Operation")) {
				boolOperation.add(auxModel.arithm(boolOperation.get(boolOperation.size()-1), operator1, createBoolVarSequence((Operation) operation.getOperand().get(i), boolVars, intVars), operator2, n).reify());
			}else {
				boolOperation.add(auxModel.arithm(boolOperation.get(boolOperation.size()-1), operator1, boolVars.get(operands.indexOfBoolVar(boolVars, operation.getOperand().get(i))), operator2, n).reify());
			}		
		}
		return boolOperation.get(boolOperation.size()-1);
	}
}
