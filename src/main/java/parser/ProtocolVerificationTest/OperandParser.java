package parser.ProtocolVerificationTest;

import java.util.List;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import protocolosv2.Operand;
import protocolosv2.Operation;
import protocolosv2.Operator;

public class OperandParser {
	int index = 0;
	
	//get the operands from operation and put it in booleanOperands list or numericOperands list.
	//Create the variable for model.
	public void operandsIntoLists(List<BoolVar> booleanOperands, List<IntVar> numericOperands, Model model, Operation operation) {
		//Go through all operands in operation.
		for(int i = 0; i < operation.getOperand().size(); i++) {
			Model auxModel = new Model("Auxiliary Model");
			//If operand is a operation.
			if(operation.getOperand().get(i).getClass().toString().contains("Operation")) {
				operandsIntoLists(booleanOperands, numericOperands, model, (Operation) operation.getOperand().get(i));
			}
			//if operand is a Numeric operand.
			else if(operation.getOperand().get(i).getClass().toString().contains("Numeric")) {
				//if the name of operand is null or "".
				if(operation.getOperand().get(i).getName() == null || operation.getOperand().get(i).getName() == "") {
					String name = operation.getOperator().getName() + index++;		
					operation.getOperand().get(i).setName(name);
				}
				IntVar intVar = auxModel.intVar(operation.getOperand().get(i).getName(), new int[] {0,1,2,3});
				//if numericOperands list still doesn't contain the new intVar.
				if(!containsIntVar(numericOperands, intVar)) {
					double operandValue = getOperandValue(operation.getOperand().get(i).toString());
					if(operandValue != 0) {
						numericOperands.add(model.intVar(operation.getOperand().get(i).getName(), (int)operandValue));
					}else {
						numericOperands.add(model.intVar(operation.getOperand().get(i).getName(), new int[] {0,1,2,3}));
					}
				}
				
			}
			//if operand is a YesOrNo (boolean) operand.
			else {
				//if the name of operand is null or "".
				if(operation.getOperand().get(i).getName() == null || operation.getOperand().get(i).getName() == "") {
					String name = operation.getOperator().getName() + index++;		
					operation.getOperand().get(i).setName(name);
				}
				
				if(operation.getOperator() == Operator.SUM || operation.getOperator() == Operator.MINUS || operation.getOperator() == Operator.MULTIPLICATION || operation.getOperator() == Operator.DIVISION) {
					IntVar intVar = auxModel.intVar(operation.getOperand().get(i).getName(), new int[] {0,1});
					//if numericOperands list still doesn't contain the new intVar.
					if(!containsIntVar(numericOperands, intVar)) {
						double operandWeight = getOperandWeight(operation.getOperand().get(i).toString());
						numericOperands.add(model.intVar(operation.getOperand().get(i).getName(), new int[] {0, (int)operandWeight}));
					}
				}else {
					BoolVar boolVar = auxModel.boolVar(operation.getOperand().get(i).getName());
					//if booleanOperands list don't already contain the new boolVar.
					if(!containsBoolVar(booleanOperands, boolVar)) {
						booleanOperands.add(model.boolVar(operation.getOperand().get(i).getName()));
					}
				}
			}	
		}
	}
		
	//Verify whether list booleanOperands already has the boolvar
	public boolean containsBoolVar(List<BoolVar> booleanOperands, BoolVar boolVar) {
		String name = boolVar.getName();
		for(int i = 0; i < booleanOperands.size(); i++) {
			String bName = booleanOperands.get(i).getName();
			if(bName.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	//Verify whether list numericOperands already has the intVar
		public boolean containsIntVar(List<IntVar> numericOperands, IntVar intVar) {
			String name = intVar.getName();
			for(int i = 0; i < numericOperands.size(); i++) {
				String bName = numericOperands.get(i).getName();
				
				if(bName.equalsIgnoreCase(name)) {
					return true;
				}			
			}
			return false;
		}
		
		private double getOperandValue(String str) {
			int pos1 = str.lastIndexOf("(")+1;
			int pos2 = str.lastIndexOf(")");
			char[] charValue = new char[pos2-pos1];
			str.getChars(pos1, pos2, charValue, 0);
			String strValue = String.copyValueOf(charValue);
			strValue = strValue.split(",")[2];
			strValue = strValue.toString().replaceFirst(" value: ", "");
			double doubleValue = 0;
			try {
				doubleValue = Double.parseDouble(strValue);
			}catch(NumberFormatException e){
			}
			return doubleValue;
		}
		
		private double getOperandWeight(String str) {
			int pos1 = str.lastIndexOf("(")+1;
			int pos2 = str.lastIndexOf(")");
			char[] charWeight = new char[pos2-pos1];
			str.getChars(pos1, pos2, charWeight, 0);
			String strWeight = String.copyValueOf(charWeight);
			strWeight = strWeight.split(",")[1];
			strWeight = strWeight.toString().replaceFirst(" weight: ", "");
			double doubleValue = 0;
			try {
				doubleValue = Double.parseDouble(strWeight);
			}catch(NumberFormatException e){
			}
			return doubleValue;
		}
}
