package parser.ProtocolVerificationTest;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import protocolosv2.Element;
import protocolosv2.Operation;
import protocolosv2.Protocol;
import protocolosv2.Sequence;

public class SequenceParser {
	private Operations operations = new Operations(); //Instantiates the class that handles operations between BoolBar variables.
	private Operands operands = new Operands();  //Instantiates the class that handles operations between BoolVar variables.
	private Protocol protocol; //The protocol to be analyzed.
	private Map<Element, List<Sequence>> mapElementOutputSequences = new HashMap<Element, List<Sequence>>(); //A map that stores all the elements and their respective output sequences from the protocol.
	
	// A constructor to initialize the protocol and the mapElementOutputSequences.
	public SequenceParser(Protocol protocol) {
		this.protocol = protocol;
		getModelFragmentsForVerification();
	}

	//Return a map with Elements as a key and a list of all solutions that occurs deadlock in the element as a value of the key.
	//If the operands have the same values in the solution, then deadlock occurs.
	//An element is in deadlock when all its output sequences are false.
	public Map<Element, List<Solution>> findDeadLockSolutions(){
		Map<Element, List<Solution>> mapSolutions = new HashMap<Element, List<Solution>>();
		for(int i = 0; i < mapElementOutputSequences.values().size(); i++){
			Model model = new Model("Find DeadLock Solutions: " + i);//Create a model to verify deadlock of a element in the mapElementOutputSequences with ChocoSolver.
			List<BoolVar> sequences = sequenceListToBoolVarList(model,(List<Sequence>) mapElementOutputSequences.values().toArray()[i]); //Get a list of BoolVar from a list of sequence of the mapElementOutputSequences.
			if (sequences!=null && !sequences.isEmpty()) {
				//Go through all the BoolVars in the list sequences.
				for(int k = 0; k < sequences.size(); k++) {
					model.arithm(sequences.get(k), "=", 0).post();//Post the constraint "the sequence i have to be false" to the model.
				}
				mapSolutions.put((Element) mapElementOutputSequences.keySet().toArray()[i], model.getSolver().findAllSolutions());//Put the Element and the solutions get from model in the mapSolutions.
			}
		}
		return mapSolutions;
	}
	
	//Return a map with Elements as a key and a list of some solutions that occur Non Determinism in the element as a value of the key.
	//If the operands have the same values presented in the solution, then Non Determinism occurs.
	//An element has a Non Determinism problem when at least two of its output sequences are true.
	public Map<Element, List<Solution>> findNonDeterminismSolutions(){
		Map<Element, List<Solution>> mapSolutions = new HashMap<Element, List<Solution>>();
		for(int k = 0; k < mapElementOutputSequences.values().size(); k++){
			Model model = new Model("Find Non Determinism Solution: " + k);//Create a model to verify non determinism of a element in the mapElementOutputSequences with ChocoSolver.
			List<BoolVar> sequences = sequenceListToBoolVarList(model,(List<Sequence>) mapElementOutputSequences.values().toArray()[k]);//Get a list of BoolVar from a list of sequence of the mapElementOutputSequences.
			if (sequences!=null && !sequences.isEmpty() && sequences.size()>1) {
				//Go through all the BoolVars in the list "sequences" to find the first list of solution that there are non determinism problem.
				for (int i = 0; i < sequences.size(); i++) {
					for(int j = i+1; j < sequences.size(); j++) {
						model.arithm(sequences.get(i), "+", sequences.get(j), "=", 2 ).post(); //Post the constraint "sequence i and sequence j have to be true" to the model.
						mapSolutions.put((Element) mapElementOutputSequences.keySet().toArray()[k], model.getSolver().findAllSolutions());//Put the Element and the solutions get from model in the mapSolutions.
						//If mapSolutions has already a list of solutions for the Element, then it returns the list.
						if(!mapSolutions.get((Element) mapElementOutputSequences.keySet().toArray()[k]).isEmpty()) {
							return mapSolutions;
						}
						model.getSolver().reset();
						model.unpost(model.getCstrs()[model.getCstrs().length-1]);
					}
				}
			}
		}
		return mapSolutions;
	}
	
	//Return a map with Elements as a key and a list of all valid solutions in the element as a value of the key.
	//An element has no problem when one, and only one, of its output sequences are true.
	public Map<Element, List<Solution>> findAllValidSolutions() {
		Map<Element, List<Solution>> mapSolutions = new HashMap<Element, List<Solution>>();
		for(int i = 0; i < mapElementOutputSequences.values().size(); i++){
			Model model = new Model("Find All Solutions: " + i); //Create a model to verify the valid solutions of a element in the mapElementOutputSequences with ChocoSolver.
			List<BoolVar> sequences = sequenceListToBoolVarList(model,(List<Sequence>) mapElementOutputSequences.values().toArray()[i]);//Get a list of BoolVar from a list of sequence of the mapElementOutputSequences.
			if (sequences!=null && !sequences.isEmpty()) {
				//Verify if there is only one boolvar in the list.
				if(sequences.size()==1) {
					model.arithm(sequences.get(0), "=", 1).post();//Post the constraint "the sequence have to be true" to the model.
					mapSolutions.put((Element) mapElementOutputSequences.keySet().toArray()[i], model.getSolver().findAllSolutions());
				}
				else {
					List<BoolVar> auxSeq = new ArrayList<BoolVar>();//Auxiliary list of boolvar to store the new boolvar variables.
					for(int k = 1; k < sequences.size(); k++) {
						if (auxSeq.isEmpty()) {
							model.arithm(sequences.get(k-1), "+", sequences.get(k), "<", 2).post();//Post the constraint "sequence k-1 and sequence k can't be both true" to the model.
							BoolVar aux = model.arithm(sequences.get(k-1), "+", sequences.get(k), "=", 1).reify();//Create a boolvar with the constraint "sequences.get(k-1) xor sequences.get(k)".
							auxSeq.add(aux);
						}else if(k < sequences.size()-1){
							model.arithm(auxSeq.get(auxSeq.size()-1), "+", sequences.get(k), "<", 2).post();//Post the constraint "the last boolvar in the auxSeq and sequence k can't be both true" to the model.
							BoolVar aux = model.arithm(auxSeq.get(auxSeq.size()-1), "+", sequences.get(k), "=", 1).reify();//Create a boolvar with the constraint "the last boolvar in the auxSeq xor sequences.get(k)".
							auxSeq.add(aux);
						//in the last sequence of the list
						}else {
							model.arithm(auxSeq.get(auxSeq.size()-1), "+", sequences.get(k), "=", 1).post();//Post the constraint "the last boolvar in the auxSeq xor sequence k" to the model.
						}
					}
					mapSolutions.put((Element) mapElementOutputSequences.keySet().toArray()[i], model.getSolver().findAllSolutions());//Put the Element and the solutions get from model in the mapSolutions.
				}
			}
		}
		return mapSolutions;
	}

	//get all the sequences from the protocol and put it into a map "mapElementOutputSequences".
	//mapElementOutputSequences associate sequences with their respective output steps.
	private void getModelFragmentsForVerification(){
		for (int i = 0; i < protocol.getSequence().size(); i++) {
			if(!mapElementOutputSequences.containsKey(protocol.getSequence().get(i).getOutputStep())) {
				List<Sequence> sequences = new ArrayList<Sequence>();
				mapElementOutputSequences.put(protocol.getSequence().get(i).getOutputStep(), sequences);
				mapElementOutputSequences.get(protocol.getSequence().get(i).getOutputStep()).add(protocol.getSequence().get(i));
			}else {
				mapElementOutputSequences.get(protocol.getSequence().get(i).getOutputStep()).add(protocol.getSequence().get(i));
			}
		}
	}
	
	//Tranform a list of sequences in a list of boolvar.
	//This boolvar are constrained with some operations.
	private List<BoolVar> sequenceListToBoolVarList(Model model, List<Sequence> sequences) {
		List<BoolVar>  boolVars = new ArrayList<BoolVar>();//boolVars is a list that will contain the operands as a boolvar variables.
		List<IntVar>  intVars = new ArrayList<IntVar>();//intVars is a list that will contain the operands as a intVar variables.
		List<BoolVar> boolSequences = new ArrayList<BoolVar>();//boolSequences is a list that will contain a sequence structure as a boolvar.
		//Go through all sequences in the list.
		for (int i = 0; i < sequences.size(); i++) {
			BoolVar boolSequence = sequenceToBoolVar(model, sequences.get(i), boolVars, intVars);
			if(boolSequence != null) {
				boolSequences.add(boolSequence);
			}			
		}
		return boolSequences;
	}		
	
	//function to convert a sequence in a boolvar.
	//model is a chocosolver model where the constraints will be post.
	//Sequence is the sequence that will be transform.
	//boolVars is a list that will contain the operands as a boolvar variables.
	//intVars is a list that will contain the operands as a intVar variables.
	private BoolVar sequenceToBoolVar(Model model, Sequence sequence, List<BoolVar> boolVars, List<IntVar> intVars){
		Operation operation = sequence.getOperation();
		operands.operandsIntoLists(boolVars, intVars, model, operation); //Update the lists boolVars and intVars with new operands from op.			
		return operations.createBoolVarSequence(operation, boolVars, intVars); //return the sequence as a BoolVar variable.	
	}	
}
