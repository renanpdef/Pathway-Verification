package parser.ProtocolValidationTest;

import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import protocolosv2.Operand;
import protocolosv2.Operation;

public class Operands {
	int index = 0;
	
	//get the operands from operation and put it in boolVars list or intVars list.
	//Create the variable for model.
	public void operandsIntoLists(List<BoolVar> boolVars, List<IntVar> intVars, Model model, Operation operation) {
		//Go through all operands in operation.
		for(int i = 0; i < operation.getOperand().size(); i++) {
			Model auxModel = new Model("Auxiliary Model");
			//If operand is a operation.
			if(operation.getOperand().get(i).getClass().toString().contains("Operation")) {
				operandsIntoLists(boolVars, intVars, model, (Operation) operation.getOperand().get(i));
			}
			//if operand is a Numeric operand.
			else if(operation.getOperand().get(i).getClass().toString().contains("Numeric")) {
				//if the operand's name is null or "".
				if(operation.getOperand().get(i).getName() == null || operation.getOperand().get(i).getName() == "") {
					String name = operation.getOperator().getName() + index++;		
					operation.getOperand().get(i).setName(name);
					intVars.add(model.intVar(operation.getOperand().get(i).getName(), 1, 3));
				}
				else {
					IntVar intVar = auxModel.intVar(operation.getOperand().get(i).getName(), 1, 3);
					//if intVars list don't already contain the new intVar.
					if(!containsIntVar(intVars, intVar)) {
						intVars.add(model.intVar(operation.getOperand().get(i).getName(), 1, 3));
					}
				}
			}
			//if operand is a YesOrNo (boolean) operand.
			else {
				//if the operand's name is null or "".
				if(operation.getOperand().get(i).getName() == null || operation.getOperand().get(i).getName() == "") {
					String name = operation.getOperator().getName() + index++;		
					operation.getOperand().get(i).setName(name);
					boolVars.add(model.boolVar(operation.getOperand().get(i).getName()));
				}
				else {
					BoolVar boolVar = auxModel.boolVar(operation.getOperand().get(i).getName());
					//if boolVars list don't already contain the new boolVar.
					if(!containsBoolVar(boolVars, boolVar)) {
						boolVars.add(model.boolVar(operation.getOperand().get(i).getName()));
					}
				}
			}	
		}
	}
		
	//Verify whether list boolVars already has the boolvar
	public boolean containsBoolVar(List<BoolVar> boolVars, BoolVar boolVar) {
		String name = boolVar.getName();
		for(int i = 0; i < boolVars.size(); i++) {
			String bName = boolVars.get(i).getName();
			if(bName.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	//Verify whether list intVars already has the intVar
		public boolean containsIntVar(List<IntVar> intVars, IntVar intVar) {
			String name = intVar.getName();
			for(int i = 0; i < intVars.size(); i++) {
				String bName = intVars.get(i).getName();
				
				if(bName.equals(name)) {
					return true;
				}			
			}
			return false;
		}
	
	//retun the index of the boolVar from the list boolVars. 
	public int indexOfBoolVar(List<BoolVar> boolVars, Operand operand) {
		String name = operand.getName();
		for(int i = 0; i < boolVars.size(); i++) {
			String bName = boolVars.get(i).getName();
			if(bName.equals(name)) {
				return i;
			}
		}
		return -1;
	}
	
	//retun the index of the intVar from the list intVars.
		public int indexOfIntVar(List<IntVar> intVars, Operand operand) {
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
