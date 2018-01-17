package parser.ProtocolValidationTest;

import java.util.List;



import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import protocolosv2.Operand;
import protocolosv2.Operation;

public class IntVarOperations {
	int index = 0;
		
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
					
					intVars.add(model.intVar(operation.getOperand().get(i).getName(), 1, 3));
				}
				else {
					IntVar intVar = auxModel.intVar(operation.getOperand().get(i).getName(), 1, 3);
					
					if(!contains(intVars, intVar)) {
						intVars.add(model.intVar(operation.getOperand().get(i).getName(), 1, 3));
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
