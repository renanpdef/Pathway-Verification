package parser.ProtocolValidationTest;

import java.util.ArrayList;
import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import protocolosv2.Operand;
import protocolosv2.Operation;
import protocolosv2.Sequence;
import protocolosv2.impl.OperandImpl;

public class BoolVarOperations { 
	int index = 0;
	
	//return a sequence representation as a BoolVar.
	//op is the operation of each sequence with the same output step.
	//boolVars is a list of the logical operands that make up all operation in the protocol.
	//index is a vector with the index of the operands in the list boolVars that are used in the operation op. 
	public BoolVar createBoolVarSequence(Operation op, List<BoolVar> boolVars){
		Model auxModel = new Model("Auxiliary Model");
		BoolVar boolSequence = null;
		
		switch(op.getOperator()) {
			case AND:
				if(op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) op.getOperand().get(0), boolVars), "+", createBoolVarSequence((Operation) op.getOperand().get(1), boolVars), "=", 2).reify();
				}
				else if(op.getOperand().get(0).getClass().toString().contains("Operation") && !op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) op.getOperand().get(0), boolVars), "+", boolVars.get(indexOf(boolVars, op.getOperand().get(1))), "=", 2).reify();
				}
				else if(!op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(boolVars.get(indexOf(boolVars, op.getOperand().get(0))), "+",createBoolVarSequence((Operation) op.getOperand().get(1), boolVars), "=", 2).reify();
				}
				else {
					boolSequence = auxModel.arithm(boolVars.get(indexOf(boolVars, op.getOperand().get(0))), "+", boolVars.get(indexOf(boolVars, op.getOperand().get(1))), "=", 2).reify();
				}		
				return boolSequence;
				
			case OR:
				if(op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) op.getOperand().get(0), boolVars), "+", createBoolVarSequence((Operation) op.getOperand().get(1), boolVars), "!=", 0).reify();
				}
				else if(op.getOperand().get(0).getClass().toString().contains("Operation") && !op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) op.getOperand().get(0), boolVars), "+", boolVars.get(indexOf(boolVars, op.getOperand().get(1))), "!=", 0).reify();
				}
				else if(!op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(boolVars.get(indexOf(boolVars, op.getOperand().get(0))), "+",createBoolVarSequence((Operation) op.getOperand().get(1), boolVars), "!=", 0).reify();
				}
				else {
					boolSequence = auxModel.arithm(boolVars.get(indexOf(boolVars, op.getOperand().get(0))), "+", boolVars.get(indexOf(boolVars, op.getOperand().get(1))), "!=", 0).reify();
				}		
				return boolSequence;
				
			case IMPLIES:
				if(op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) op.getOperand().get(0), boolVars), "-", createBoolVarSequence((Operation) op.getOperand().get(1), boolVars), "!=", 1).reify();
				}
				else if(op.getOperand().get(0).getClass().toString().contains("Operation") && !op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) op.getOperand().get(0), boolVars), "-", boolVars.get(indexOf(boolVars, op.getOperand().get(1))), "!=", 1).reify();
				}
				else if(!op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(boolVars.get(indexOf(boolVars, op.getOperand().get(0))), "-",createBoolVarSequence((Operation) op.getOperand().get(1), boolVars), "!=", 1).reify();
				}
				else {
					boolSequence = auxModel.arithm(boolVars.get(indexOf(boolVars, op.getOperand().get(0))), "-", boolVars.get(indexOf(boolVars, op.getOperand().get(1))), "!=", 1).reify();
				}		
				return boolSequence;
				
			case XOR:
				if(op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) op.getOperand().get(0), boolVars), "+", createBoolVarSequence((Operation) op.getOperand().get(1), boolVars), "=", 1).reify();
				}
				else if(op.getOperand().get(0).getClass().toString().contains("Operation") && !op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) op.getOperand().get(0), boolVars), "+", boolVars.get(indexOf(boolVars, op.getOperand().get(1))), "=", 1).reify();
				}
				else if(!op.getOperand().get(0).getClass().toString().contains("Operation") && op.getOperand().get(1).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(boolVars.get(indexOf(boolVars, op.getOperand().get(0))), "+",createBoolVarSequence((Operation) op.getOperand().get(1), boolVars), "=", 1).reify();
				}
				else {
					boolSequence = auxModel.arithm(boolVars.get(indexOf(boolVars, op.getOperand().get(0))), "+", boolVars.get(indexOf(boolVars, op.getOperand().get(1))), "=", 1).reify();
				}		
				return boolSequence;
				
			default:
				if(op.getOperand().get(0).getClass().toString().contains("Operation")) {
					boolSequence = auxModel.arithm(createBoolVarSequence((Operation) op.getOperand().get(0), boolVars), "+", createBoolVarSequence((Operation) op.getOperand().get(0), boolVars), "=", 0).reify();
				}
				else {
					boolSequence = auxModel.arithm(boolVars.get(indexOf(boolVars, op.getOperand().get(0))), "+", boolVars.get(indexOf(boolVars, op.getOperand().get(0))), "=", 0).reify();
				}		
				return boolSequence;
		}
	}
		
	//get the operands from the operation and put it in the list boolVars if the list do not contain it.
	public void operandsIntoBoolVarList(List<BoolVar> boolVars, Model model, Operation operation) {
		for(int i = 0; i < operation.getOperand().size(); i++) {
			Model auxModel = new Model("Auxiliary Model");
			if(operation.getOperand().get(i).getClass().toString().contains("Operation")) {
				operandsIntoBoolVarList(boolVars, model, (Operation) operation.getOperand().get(i));
			}
			else {
			
				if(operation.getOperand().get(i).getName() == null || operation.getOperand().get(i).getName() == "") {
					String name = operation.getOperator().getName() + index++;		
					
					operation.getOperand().get(i).setName(name);
					
					boolVars.add(model.boolVar(operation.getOperand().get(i).getName()));
				}
				else {
					BoolVar boolVar = auxModel.boolVar(operation.getOperand().get(i).getName());
					
					if(!contains(boolVars, boolVar)) {
						boolVars.add(model.boolVar(operation.getOperand().get(i).getName()));
					}
				}
			}	
		}
	}
		
	//Verify whether list boolVars already has the boolvar
	public boolean contains(List<BoolVar> boolVars, BoolVar boolVar) {
		String name = boolVar.getName();
		for(int i = 0; i < boolVars.size(); i++) {
			String bName = boolVars.get(i).getName();
			if(bName.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	//retun the index of the boolVar from the list boolVars. 
	public int indexOf(List<BoolVar> boolVars, Operand operand) {
		String name = operand.getName();
		for(int i = 0; i < boolVars.size(); i++) {
			String bName = boolVars.get(i).getName();
			if(bName.equals(name)) {
				return i;
			}
		}
		return -1;
	}
}
