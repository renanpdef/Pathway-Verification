package parser.PathwayVerificationTableGeneration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pathwayMetamodel.Choice;
import pathwayMetamodel.Operand;
import pathwayMetamodel.Operation;
import pathwayMetamodel.Operator;
import pathwayMetamodel.Pathway;
import pathwayMetamodel.Sequence;

public class OperationInformation {
	
	Pathway pathway;
	List<Operation> pathwayOperations;
	List<Operand> pathwayOperands;
	Map<String,Integer> operandsNumberMap;
	Map<Operator,Integer> operatorsNumberMap;
	Map<Operator,Integer> realOperatorsNumberMap;
	
	public OperationInformation(Pathway pathway) {
		this.pathway = pathway;
		this.pathwayOperations = getOperationsList();
		this.pathwayOperands = getOperandsList(pathwayOperations);
		this.operandsNumberMap = getOperandNumberIndividually();
		this.operatorsNumberMap = getOperatorsNumber();
		this.realOperatorsNumberMap = getRealOperatorsNumber();
		
	}
	
	public int getOperationsNumber() {
		return pathwayOperations.size();
	}
	
	public int getRealOperationNumber() {
		int operationNumber = 0;
		for (int i = 0; i < pathwayOperations.size(); i++) {
			if(pathwayOperations.get(i).getOperand().size() > 1) {
				operationNumber += pathwayOperations.get(i).getOperand().size()-1;
			}
			else {
				operationNumber++;
			}
		}
		return operationNumber;
	}
	
	public int getOperandsNumber() {
		return pathwayOperands.size();
	}
	
	public int getNumericOperandsNumber() {
		return operandsNumberMap.containsKey("Numeric")?operandsNumberMap.get("Numeric"):0;
	}
	
	public int getYesOrNoOperandsNumber() {
		return operandsNumberMap.containsKey("YesOrNo")?operandsNumberMap.get("YesOrNo"):0;
	}
	
	public int getChoiceOperandsNumber() {
		return operandsNumberMap.containsKey("Choice")?operandsNumberMap.get("Choice"):0;
	}
	
	public int getChoiceOptionsNumber() {
		int choiceOptionsNumber = 0;
		for (int i = 0; i < pathwayOperands.size(); i++) {
			if(pathwayOperands.get(i).getClass().toString().contains("Choice")) {
				Choice choiceOperand = (Choice) pathwayOperands.get(i);
				choiceOptionsNumber += choiceOperand.getOption().size();
			}
		}
		return choiceOptionsNumber;
	}
	
	public int getAdditionOperatorNumber() {
		return operatorsNumberMap.containsKey(Operator.ADDITION)?operatorsNumberMap.get(Operator.ADDITION):0;
	}
	
	public int getSubtractionOperatorNumber() {
		return operatorsNumberMap.containsKey(Operator.SUBTRACTION)?operatorsNumberMap.get(Operator.SUBTRACTION):0;
	}
	
	public int getMultiplicationOperatorNumber() {
		return operatorsNumberMap.containsKey(Operator.MULTIPLICATION)?operatorsNumberMap.get(Operator.MULTIPLICATION):0;
	}
	
	public int getDivisionOperatorNumber() {
		return operatorsNumberMap.containsKey(Operator.DIVISION)?operatorsNumberMap.get(Operator.DIVISION):0;
	}
	
	public int getAndOperatorNumber() {
		return operatorsNumberMap.containsKey(Operator.AND)?operatorsNumberMap.get(Operator.AND):0;
	}
	
	public int getOrOperatorNumber() {
		return operatorsNumberMap.containsKey(Operator.OR)?operatorsNumberMap.get(Operator.OR):0;
	}
	
	public int getXorOperatorNumber() {
		return operatorsNumberMap.containsKey(Operator.XOR)?operatorsNumberMap.get(Operator.XOR):0;
	}
	
	public int getImpliesOperatorNumber() {
		return operatorsNumberMap.containsKey(Operator.IMPLIES)?operatorsNumberMap.get(Operator.IMPLIES):0;
	}
	
	public int getAffirmationOperatorNumber() {
		return operatorsNumberMap.containsKey(Operator.AFFIRMATION)?operatorsNumberMap.get(Operator.AFFIRMATION):0;
	}
	
	public int getNotOperatorNumber() {
		return operatorsNumberMap.containsKey(Operator.NOT)?operatorsNumberMap.get(Operator.NOT):0;
	}
	
	public int getEqualOperatorNumber() {
		return operatorsNumberMap.containsKey(Operator.EQUAL)?operatorsNumberMap.get(Operator.EQUAL):0;
	}
	
	public int getGreaterOperatorNumber() {
		return operatorsNumberMap.containsKey(Operator.GREATER_THAN)?operatorsNumberMap.get(Operator.GREATER_THAN):0;
	}
	
	public int getGreaterEqualOperatorNumber() {
		return operatorsNumberMap.containsKey(Operator.GREATER_OR_EQUAL)?operatorsNumberMap.get(Operator.GREATER_OR_EQUAL):0;
	}
	
	public int getLessOperatorNumber() {
		return operatorsNumberMap.containsKey(Operator.LESS_THAN)?operatorsNumberMap.get(Operator.LESS_THAN):0;
	}
	
	public int getLessEqualOperatorNumber() {
		return operatorsNumberMap.containsKey(Operator.LESS_OR_EQUAL)?operatorsNumberMap.get(Operator.LESS_OR_EQUAL):0;
	}
	
	public int getRealAdditionOperatorNumber() {
		return realOperatorsNumberMap.containsKey(Operator.ADDITION)?realOperatorsNumberMap.get(Operator.ADDITION):0;
	}
	
	public int getRealSubtractionOperatorNumber() {
		return realOperatorsNumberMap.containsKey(Operator.SUBTRACTION)?realOperatorsNumberMap.get(Operator.SUBTRACTION):0;
	}
	
	public int getRealMultiplicationOperatorNumber() {
		return realOperatorsNumberMap.containsKey(Operator.MULTIPLICATION)?realOperatorsNumberMap.get(Operator.MULTIPLICATION):0;
	}
	
	public int getRealDivisionOperatorNumber() {
		return realOperatorsNumberMap.containsKey(Operator.DIVISION)?realOperatorsNumberMap.get(Operator.DIVISION):0;
	}
	
	public int getRealAndOperatorNumber() {
		return realOperatorsNumberMap.containsKey(Operator.AND)?realOperatorsNumberMap.get(Operator.AND):0;
	}
	
	public int getRealOrOperatorNumber() {
		return realOperatorsNumberMap.containsKey(Operator.OR)?realOperatorsNumberMap.get(Operator.OR):0;
	}
	
	public int getRealXorOperatorNumber() {
		return realOperatorsNumberMap.containsKey(Operator.XOR)?realOperatorsNumberMap.get(Operator.XOR):0;
	}
	
	public int getRealImpliesOperatorNumber() {
		return realOperatorsNumberMap.containsKey(Operator.IMPLIES)?realOperatorsNumberMap.get(Operator.IMPLIES):0;
	}
	
	public int getRealAffirmationOperatorNumber() {
		return realOperatorsNumberMap.containsKey(Operator.AFFIRMATION)?realOperatorsNumberMap.get(Operator.AFFIRMATION):0;
	}
	
	public int getRealNotOperatorNumber() {
		return realOperatorsNumberMap.containsKey(Operator.NOT)?realOperatorsNumberMap.get(Operator.NOT):0;
	}
	
	public int getRealEqualOperatorNumber() {
		return realOperatorsNumberMap.containsKey(Operator.EQUAL)?realOperatorsNumberMap.get(Operator.EQUAL):0;
	}
	
	public int getRealGreaterOperatorNumber() {
		return realOperatorsNumberMap.containsKey(Operator.GREATER_THAN)?realOperatorsNumberMap.get(Operator.GREATER_THAN):0;
	}
	
	public int getRealGreaterEqualOperatorNumber() {
		return realOperatorsNumberMap.containsKey(Operator.GREATER_OR_EQUAL)?realOperatorsNumberMap.get(Operator.GREATER_OR_EQUAL):0;
	}
	
	public int getRealLessOperatorNumber() {
		return realOperatorsNumberMap.containsKey(Operator.LESS_THAN)?realOperatorsNumberMap.get(Operator.LESS_THAN):0;
	}
	
	public int getRealLessEqualOperatorNumber() {
		return realOperatorsNumberMap.containsKey(Operator.LESS_OR_EQUAL)?realOperatorsNumberMap.get(Operator.LESS_OR_EQUAL):0;
	}
	 
	 
	
	private List<Operation> getOperationsList(){
		List<Sequence> transitions = pathway.getSequence();
		List<Operation> operations = new ArrayList<Operation>();
		for (int i = 0; i < transitions.size(); i++) {
			Operation operation = transitions.get(i).getOperation();
			if( operation!= null) {
				operations.add(operation);
				addNestedOperations(operations, operation);
			}
		}
		return operations;
	}

	private void addNestedOperations(List<Operation> operations, Operation operation) {
		for(int i = 0; i < operation.getOperand().size(); i++) {
			if(operation.getOperand().get(i).getClass().toString().contains("Operation")) {
				Operation nestedOperation = (Operation) operation.getOperand().get(i);
				operations.add(nestedOperation);
				addNestedOperations(operations, nestedOperation);
			}
		}
	}
	
	private List<Operand> getOperandsList(List<Operation> operations){
		List<Operand> operands = new ArrayList<Operand>();
		for (int i = 0; i < operations.size(); i++) {
			for (int j = 0; j < operations.get(i).getOperand().size(); j++) {
				if(!operations.get(i).getOperand().get(j).getClass().toString().contains("Operation")) {
					Operand operand = operations.get(i).getOperand().get(j);
					if(!operandContained(operands, operand)) {
						operands.add(operand);
					}
				}
			}
		}
		return operands;
	}
	
	private boolean operandContained(List<Operand> operands, Operand operand) {
		String name = operand.getName();
		for (int i = 0; i < operands.size(); i++) {
			String listName = operands.get(i).getName();
			if(name.equalsIgnoreCase(listName)) {
				return true;
			}
		}
		return false;
	}
	
	//returns an array of size 3 with the number of numeric operands, yesOrNo operands and choice operands respectively
	private Map<String,Integer> getOperandNumberIndividually() {
		Map<String,Integer> operandsNumberMap = new HashMap<String,Integer>();
		for (int i = 0; i < pathwayOperands.size(); i++) {
			if(pathwayOperands.get(i).getClass().toString().contains("Numeric")) {
				operandsNumberMap.put("Numeric", operandsNumberMap.containsKey("Numeric")?operandsNumberMap.get("Numeric")+1:1);
			}
			else if(pathwayOperands.get(i).getClass().toString().contains("YesOrNo")) {
				operandsNumberMap.put("YesOrNo", operandsNumberMap.containsKey("YesOrNo")?operandsNumberMap.get("YesOrNo")+1:1);
			}
			else {
				operandsNumberMap.put("Choice", operandsNumberMap.containsKey("Choice")?operandsNumberMap.get("Choice")+1:1);
			}
		}
		return operandsNumberMap;
	}
	
	private Map<Operator,Integer> getOperatorsNumber() {
		Map<Operator,Integer> operatorsNumberMap = new HashMap<Operator,Integer>();
		for (int i = 0; i < pathwayOperations.size(); i++) {
			Operator operator = pathwayOperations.get(i).getOperator();
			operatorsNumberMap.put(operator, operatorsNumberMap.containsKey(operator)?operatorsNumberMap.get(operator)+1:1);
		}
		return operatorsNumberMap;
	}
	
	private Map<Operator,Integer> getRealOperatorsNumber() {
		Map<Operator,Integer> realOperatorsNumberMap = new HashMap<Operator,Integer>();
		for (int i = 0; i < pathwayOperations.size(); i++) {
			Operator operator = pathwayOperations.get(i).getOperator();
			if(pathwayOperations.get(i).getOperand().size() > 1) {
				realOperatorsNumberMap.put(operator, realOperatorsNumberMap.containsKey(operator)?realOperatorsNumberMap.get(operator)+pathwayOperations.get(i).getOperand().size()-1:pathwayOperations.get(i).getOperand().size()-1);
			}
			else {
				realOperatorsNumberMap.put(operator, realOperatorsNumberMap.containsKey(operator)?realOperatorsNumberMap.get(operator)+1:1);
			}
		}
		return realOperatorsNumberMap;
	}
}
