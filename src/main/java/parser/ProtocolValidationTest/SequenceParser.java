package parser.ProtocolValidationTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.eclipse.jdt.core.dom.BooleanLiteral;

import protocolosv2.Element;
import protocolosv2.Operation;
import protocolosv2.Protocol;
import protocolosv2.Sequence;

public class SequenceParser {
	BoolVarOperations boolVarOp = new BoolVarOperations();
	private IntVarOperations intVarOp = new IntVarOperations();
	private Protocol protocol;
	private Map<Element, List<Sequence>> mapSequences = new HashMap<Element, List<Sequence>>();
//	private List<BoolVar>  boolVars = new ArrayList<BoolVar>();
//	private List<IntVar>  intVars = new ArrayList<IntVar>();
//	private Model model = new Model("Sequences Parser");
	
	public SequenceParser(Protocol protocol) {
		this.protocol = protocol;
		getModelFragmentsForVerification();
	}

	//returns a list of all solutions that occur deadlock.
	//If the operands have the same values presented in the solution, then deadlock occurs.
	public Map<Element, List<Solution>> findDeadLockSolutions(){
		Map<Element, List<Solution>> mapSolutions = new HashMap<Element, List<Solution>>();
		for(int i = 0; i < mapSequences.values().size(); i++){
			Model model = new Model("Find All Solutions: " + i);
			List<BoolVar> sequences = sequencesTranformation(model,(List<Sequence>) mapSequences.values().toArray()[i]);
			if (sequences!=null && !sequences.isEmpty()) {
				for(int k = 0; k < sequences.size(); k++) {
					model.arithm(sequences.get(k), "=", 0).post();
				}
				mapSolutions.put((Element) mapSequences.keySet().toArray()[i], model.getSolver().findAllSolutions());
			}
		}
		return mapSolutions;
	}
	
	//returns a list of some solutions that occur Non Determinism.
	//If the operands have the same values presented in the solution, then Non Determinism occurs.
	public Map<Element, List<Solution>> findNonDeterminismSolutions(){
		Map<Element, List<Solution>> mapSolutions = new HashMap<Element, List<Solution>>();
		for(int k = 0; k < mapSequences.values().size(); k++){
			Model model = new Model("Find Non Determinism Solution: " + k);
			List<BoolVar> sequences = sequencesTranformation(model,(List<Sequence>) mapSequences.values().toArray()[k]);
			if (sequences!=null && !sequences.isEmpty() && sequences.size()>1) {
				//Add clauses list to the model
				for (int i = 0; i < sequences.size(); i++) {
					for(int j = i+1; j < sequences.size(); j++) {
						model.arithm(sequences.get(i), "+", sequences.get(j), "=", 2 ).post();
						mapSolutions.put((Element) mapSequences.keySet().toArray()[k], model.getSolver().findAllSolutions());
						if(mapSolutions.get((Element) mapSequences.keySet().toArray()[k]) != null) {
							break;
						}
					}
					if(mapSolutions.get((Element) mapSequences.keySet().toArray()[k]) != null) {
						break;
					}
				}
			}
		}
		return mapSolutions;
	}
	
	//return all correct solutions in a list of sequences with the same output step.
	//Use the choco solver to get possible solutions and return them
	public Map<Element, List<Solution>> findAllSolutions() {
		//removeAllModelConstraints();
		Map<Element, List<Solution>> mapSolutions = new HashMap<Element, List<Solution>>();
		for(int i = 0; i < mapSequences.values().size(); i++){
			Model model = new Model("Find All Solutions: " + i);
			List<BoolVar> sequences = sequencesTranformation(model,(List<Sequence>) mapSequences.values().toArray()[i]);
			if (sequences!=null && !sequences.isEmpty()) {
				if(sequences.size()==1) {
					model.arithm(sequences.get(0), "=", 1).post();
					mapSolutions.put((Element) mapSequences.keySet().toArray()[i], model.getSolver().findAllSolutions());
				}
				else {
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
					mapSolutions.put((Element) mapSequences.keySet().toArray()[i], model.getSolver().findAllSolutions());
				}
			}
		}
		return mapSolutions;
	}
	
	//verify whether if there are sequences with the same output step
	public boolean isThereSameOutputStep() {
		for (int i = 0; i < protocol.getSequence().size(); i++) {
			for (int j = i+1; j < protocol.getSequence().size(); j++) {
				if(protocol.getSequence().get(i).getOutputStep() == protocol.getSequence().get(j).getOutputStep()) {
					return true;
				}
			}
		}
		return false;
	}

	//get all the sequences from the protocol and put it into a arraylist sequences.
	public void getModelFragmentsForVerification(){
		//Loop to get all the sequences same output step
		for (int i = 0; i < protocol.getSequence().size(); i++) {
			if(!mapSequences.containsKey(protocol.getSequence().get(i).getOutputStep())) {
				List<Sequence> sequences = new ArrayList<Sequence>();
				mapSequences.put(protocol.getSequence().get(i).getOutputStep(), sequences);
				mapSequences.get(protocol.getSequence().get(i).getOutputStep()).add(protocol.getSequence().get(i));
				//createSequenceList(mapSequences, protocol.getSequence().get(i));
			}else {
				mapSequences.get(protocol.getSequence().get(i).getOutputStep()).add(protocol.getSequence().get(i));
				//createSequenceList(mapSequences, protocol.getSequence().get(i));
			}
		}
	}
	
	//tranform a list of sequences in a list of boolvar.
	public List<BoolVar> sequencesTranformation(Model model, List<Sequence> sequences) {
		//Lista de operandos
		List<BoolVar>  boolVars = new ArrayList<BoolVar>();
		List<IntVar>  intVars = new ArrayList<IntVar>();
		List<BoolVar> boolSequences = new ArrayList<BoolVar>();
		//Loop to get sequences with the same output step
		for (int i = 0; i < sequences.size(); i++) {
			createSequenceList(model, sequences.get(i), boolVars, intVars, boolSequences);
		}
		return boolSequences;
	}		
	
	//Create Clauses for one operation
	public void createSequenceList(Model model, Sequence sequence, List<BoolVar> boolVars, List<IntVar> intVars, List<BoolVar> boolSequences){
		//Verify the operator of each operation.
		//Create the clauses according to the operator and 
		//add them on the clauses list
		Operation op = sequence.getOperation();
		switch (op.getOperator()) {
			case AND:
			case OR:
			case IMPLIES:
			case XOR:
				boolVarOp.createBoolVars(boolVars, model, op);
				
				boolVarOp.addSequences(boolSequences, sequence, boolVars, boolVarOp.getIndexes());
				break;
			
			case EQUAL:
			case EQUAL_OR_GREATER:
			case EQUAL_OR_SMALLER:
			case BIGGER_THAN:
			case SMALLER_THAN:
				intVarOp.createIntVars(intVars, model, op);//ATENCAO
				
				intVarOp.addSequences(boolSequences, sequence, intVars, intVarOp.getIndexes());								
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
				boolVarOp.createBoolVars(boolVars, model, op);
				int[] indexes = boolVarOp.getIndexes();
				BoolVar bool = model.arithm(boolVars.get(indexes[0]), "+", boolVars.get(indexes[0]), "=", 0).reify();
				boolSequences.add(bool);
				break;
		}
	}
	
	//Remove all constraint in the model
//	public void removeAllModelConstraints() {
//		Constraint constraint[] = model.getCstrs();
//		if(constraint != null) {
//			for (int i = 0; i < constraint.length; i++) {
//				System.out.println(constraint[i]);
//				model.unpost(constraint[i]);
//			}
//		}
//	}
	
}
