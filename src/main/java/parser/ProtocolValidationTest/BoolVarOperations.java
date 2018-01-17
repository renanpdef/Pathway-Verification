package parser.ProtocolValidationTest;

import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;

import protocolosv2.Operand;
import protocolosv2.Operation;

public class BoolVarOperations {
	int index = 0;
	
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
