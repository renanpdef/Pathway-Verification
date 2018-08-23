package parser.PathwayVerificationTest;

import java.util.ArrayList;
import java.util.List;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import pathwayMetamodel.Choice;
import pathwayMetamodel.Numeric;
import pathwayMetamodel.Operation;
import pathwayMetamodel.Operator;
import pathwayMetamodel.YesOrNo;

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
				Numeric operand = (Numeric) operation.getOperand().get(i);
				//if the name of operand is null or "".
				if(operand.getName() == null || operand.getName() == "") {
					String name = operation.getOperator().getName() + index++;		
					operand.setName(name);
				}
				IntVar intVar = auxModel.intVar(operand.getName(), new int[] {0,1,2,3,4,5,25,50,75,100});
				//if numericOperands list still doesn't contain the new intVar.
				if(!containsIntVar(numericOperands, intVar)) {
					//double operandValue = getOperandValue(operation.getOperand().get(i).toString());
					double operandValue = 0;
					if(operand.getValue() != null) {
						operandValue = operand.getValue();
					}
					if(operandValue != 0) {
						numericOperands.add(model.intVar(operand.getName(), (int)operandValue));
					}else {
						numericOperands.add(model.intVar(operand.getName(), new int[] {0,1,2,3,4,5,25,50,75,100}));
					}
				}
				
			}
			//if operand is a Choice operand.
			else if(operation.getOperand().get(i).getClass().toString().contains("Choice")) {
				Choice operand = (Choice) operation.getOperand().get(i);
				//if the name of operand is null or "".
				if(operand.getName() == null || operand.getName() == "") {
					String name = operation.getOperator().getName() + index++;		
					operand.setName(name);
				}
				IntVar intVar = auxModel.intVar(operand.getName(), new int[] {0,1});
				//if numericOperands list still doesn't contain the new intVar.
				if(!containsIntVar(numericOperands, intVar)) {
					double operandValue = 0;
					if(operand.getValue() != null) {
						operandValue = operand.getValue();
					}
					//if the choice operand has options variables
					//the weights of each option are joined in a single string "options" separated by semicolons
					if(operand.getOption().size() > 0){
						String options = "";
						for(int option = 0; option < operand.getOption().size(); option++) {
							options += (int)operand.getOption().get(option).getWeight() + ";";
						}
						//All existing subsets by combining the weights. 
						//Also represented as a string separated by comma and semicolon.
						String domainSTR = getsDomain("", options);
						int[] domain = stringDomainToIntDomain((int)operandValue, domainSTR);
						domain = removeRedundancy(domain);
						sortArray(domain);
						numericOperands.add(model.intVar(operand.getName(), domain));
					}
				}
			}
			//if operand is a YesOrNo (boolean) operand.
			else {
				YesOrNo operand = (YesOrNo) operation.getOperand().get(i);
				//if the name of operand is null or "".
				if(operand.getName() == null || operand.getName() == "") {
					String name = operation.getOperator().getName() + index++;		
					operand.setName(name);
				}
				if(operation.getOperator() == Operator.ADDITION || operation.getOperator() == Operator.SUBTRACTION || operation.getOperator() == Operator.MULTIPLICATION || operation.getOperator() == Operator.DIVISION) {
					IntVar intVar = auxModel.intVar(operand.getName(), new int[] {0,1});
					//if numericOperands list still doesn't contain the new intVar.
					if(!containsIntVar(numericOperands, intVar)) {
						//double operandWeight = getOperandWeight(operation.getOperand().get(i).toString());
						double operandWeight = operand.getWeight();
						numericOperands.add(model.intVar(operand.getName(), new int[] {0, (int)operandWeight}));
					}
				}else {
					BoolVar boolVar = auxModel.boolVar(operand.getName());
					//if booleanOperands list don't already contain the new boolVar.
					if(!containsBoolVar(booleanOperands, boolVar)) {
						booleanOperands.add(model.boolVar(operand.getName()));
					}
				}
			}	
		}
	}
		
	private String getsDomain(String auxOptions, String options) {
		if(options.equals("")) {
			return auxOptions;
		}
		else {
			String[] strArray = options.split(";");
			String str = strArray[0];
			return getsDomain(auxOptions + str + ",", options.substring(str.length()+1)) + ";" + getsDomain(auxOptions, options.substring(str.length()+1));
		}
	}
	
	private int[] stringDomainToIntDomain(int operandValue, String domainSTR) {
		String[] domainArray = domainSTR.split(";");
		int[] domain = new int[domainArray.length + 1];
		domain[0] = operandValue;
		for(int i = 0; i < domainArray.length; i++) {
			String[] subDomain = domainArray[i].split(",");
			int addition = operandValue;
			for (int j = 0; j < subDomain.length; j++) {
				addition += Integer.parseInt(subDomain[j]);
			}
			domain[i+1] = addition;
		}
		return domain;
	}
	
	private int[] removeRedundancy(int[] domain) {
		int cont = 0;
        for(int i = 0; i < domain.length-1; i++){  
            for(int j = i+1; j < domain.length; j++){  
               if(domain[i] != -1 && domain[i] == domain[j]){  
                    domain[j] = -1;
                    cont++;
               }  
            }  
        }
        int[] newDomain = new int[domain.length-cont];
        int index = 0;
        for (int i = 0; i < domain.length; i++) {
			if(domain[i] != -1) {
				newDomain[index] = domain[i];
				index++;
			}
		}
        return newDomain;
	}

	private void sortArray(int[] domain) {
		int aux;  
        for(int i = 0; i < domain.length-1; i++){  
            for(int j = i+1; j < domain.length; j++){  
               if(domain[i] > domain[j]){  
                    aux = domain[i];  
                    domain[i] = domain[j];  
                    domain[j] = aux;  
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
}
