package parser.ProtocolValidationTest;

import java.util.ArrayList;

import java.util.List;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import protocolosv2.Operation;
import protocolosv2.Protocol;

public class SequenceParser {
	BoolVarOperations boolVarOp = new BoolVarOperations();
	private IntVarOperations intVarOp = new IntVarOperations();
	private Protocol protocol;
	
	public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	//returns a list of all solutions that occur deadlock.
	//If the operands have the same values presented in the solution, then deadlock occurs.
	public List<Solution> findDeadLockSolutions(){
		//Initialize choco solver model
		Model model = new Model("Find Deadlock Solution");
		List<BoolVar> sequences = getSequences(model);
		if (sequences!=null && !sequences.isEmpty()) {
			//Add clauses list to the model
			for(int k = 0; k < sequences.size(); k++) {
				model.arithm(sequences.get(k), "=", 0).post();
			}

			return model.getSolver().findAllSolutions();
		}
		return null;
	}
	
	//returns a list of some solutions that occur Non Determinism.
	//If the operands have the same values presented in the solution, then Non Determinism occurs.
	public List<Solution> findNonDeterminismSolutions(){
		//Initialize choco solver model
		Model model = new Model("Same Guard Condition");
		List<BoolVar> sequences = getSequences(model);
		if (sequences!=null && !sequences.isEmpty()) {
			//Add clauses list to the model
			for (int i = 0; i < sequences.size(); i++) {
				for(int j = i+1; j < sequences.size(); j++) {
					model.arithm(sequences.get(i), "+", sequences.get(j), "=", 2 ).post();
					List<Solution> solutions = model.getSolver().findAllSolutions();
					if(solutions != null) {
						return solutions;
					}
				}
			}
			return model.getSolver().findAllSolutions();
		}
		return null;
	}
	
	//return all correct solutions in a list of sequences with the same output step.
	//Use the choco solver to get possible solutions and return them
	public List<Solution> findAllSolutions() {
		//Initialize choco solver model
		Model model = new Model("Same Guard Condition");
		List<BoolVar> sequences = getSequences(model);
		if (sequences!=null && !sequences.isEmpty()) {
			//Add clauses list to the model
			List<BoolVar> auxSeq = new ArrayList<BoolVar>();
			for(int k = 1; k < sequences.size(); k++) {
				if (auxSeq.isEmpty()) {
					model.arithm(sequences.get(k-1), "+", sequences.get(k), "<", 2).post();
					BoolVar aux = model.arithm(sequences.get(k-1), "+", sequences.get(k), "=", 1).reify();
					auxSeq.add(aux);
				}else if(k < sequences.size()-1){
					model.arithm(auxSeq.get(auxSeq.size()-1), "+", sequences.get(k), "<", 2).post();
					BoolVar aux = model.arithm(auxSeq.get(auxSeq.size()-1), "+", sequences.get(k), "=", 1).reify();
					auxSeq.add(aux);
				}else {
					model.arithm(auxSeq.get(auxSeq.size()-1), "+", sequences.get(k), "=", 1).post();
				}
			}

			//List<Solution> solutions = new ArrayList<Solution>();
			return model.getSolver().findAllSolutions();
		}
		return null;
	}
	
	//verify whether if there are sequences with the same output step
	public boolean isThereSameOutputStep() {
		for (int i = 0; i < getProtocol().getSequence().size(); i++) {
			for (int j = i+1; j < getProtocol().getSequence().size(); j++) {
				if(getProtocol().getSequence().get(i).getOutputStep() == getProtocol().getSequence().get(j).getOutputStep()) {
					return true;
				}
			}
		}
		return false;
	}

	//get the sequences with the same output step
	public List<BoolVar> getSequences(Model model) {
		//Lista de operandos
		List<BoolVar>  boolVars = new ArrayList<BoolVar>();
		List<IntVar>  intVars = new ArrayList<IntVar>();
		//List of operations with the operations of each sequence
		List<Operation> operations = new ArrayList<Operation>();
		//Loop to get sequences with the same output step
		for (int i = 0; i < getProtocol().getSequence().size(); i++) {
			//List of boolVars with a boolVar for each sequence
			//These boolVar are the conditions guard
			List<BoolVar> sequences = new ArrayList<BoolVar>();
			
			for (int j = i+1; j < getProtocol().getSequence().size(); j++) {
				if(getProtocol().getSequence().get(i).getOutputStep() == getProtocol().getSequence().get(j).getOutputStep()) {	
					Operation opi = getProtocol().getSequence().get(i).getOperation();
					Operation opj = getProtocol().getSequence().get(j).getOperation();
					if(!operations.contains(opi)) {
						operations.add(opi);
						createSequenceList(boolVars, intVars, sequences, model, opi);
					}
					if(!operations.contains(opj)) {
						operations.add(opj);
						createSequenceList(boolVars, intVars, sequences, model, opj);
					}
				}
			}
			
			if (sequences!=null && !sequences.isEmpty()) {
				return sequences;
			}
		}
		return null;
	}		
	
	//Create Clauses for one operation
	public void createSequenceList(List<BoolVar> boolVars, List<IntVar> intVars, List<BoolVar> sequences, Model model, Operation operation){
		//Verify the operator of each operation.
		//Create the clauses according to the operator and 
		//add them on the clauses list
		switch (operation.getOperator()) {
			case AND:
			case OR:
			case IMPLIES:
			case XOR:
				boolVarOp.createBoolVars(boolVars, model, operation);
				
				boolVarOp.addSequences(sequences, operation, boolVars, boolVarOp.getIndexes());
				break;
			
			case EQUAL:
			case EQUAL_OR_GREATER:
			case EQUAL_OR_SMALLER:
			case BIGGER_THAN:
			case SMALLER_THAN:
				intVarOp.createIntVars(intVars, model, operation);//ATENCAO
				
				intVarOp.addSequences(sequences, operation, intVars, intVarOp.getIndexes());								
				break;
			case SUM:								
				break;							
			case MINUS:								
				break;								
			case MULTIPLICATION:								
				break;								
			case DIVISION:							
				break;									
			case AFFIRMATION:								
				break;					
			default:
				boolVarOp.createBoolVars(boolVars, model, operation);
				int[] indexes = boolVarOp.getIndexes();
				BoolVar bool = model.arithm(boolVars.get(indexes[0]), "+", boolVars.get(indexes[0]), "=", 0).reify();
				sequences.add(bool);
				break;
		}
	}
}
