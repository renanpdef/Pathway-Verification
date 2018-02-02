package parser.ProtocolVerificationTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import protocolosv2.Element;
import protocolosv2.Operation;
import protocolosv2.Protocol;
import protocolosv2.Sequence;

public class InaccessibleStep {
	private Operations operations = new Operations(); //Instantiates the class that handles operations between BoolBar variables.
	private Operands operands = new Operands();  //Instantiates the class that handles operations between BoolVar variables.
	private Protocol protocol; //The protocol to be analyzed.
	private List<BoolVar>  boolVars = new ArrayList<BoolVar>();//boolVars is a list that will contain the operands as a boolvar variables.
	private List<IntVar>  intVars = new ArrayList<IntVar>();//intVars is a list that will contain the operands as a intVar variables.
	private Elements opElements = new Elements();
	private Model model = new Model("Inaccessible Step");
	private Map<Element, List<Sequence>> mapElementInputSequences = new HashMap<Element, List<Sequence>>(); //A map that stores all the elements and their respective output sequences from the protocol.
	
	// A constructor to initialize the protocol and the mapElementInputSequences.
	public InaccessibleStep(Protocol protocol) {
		this.protocol = protocol;
		getModelFragmentsForVerification();
	}
	
	//get all the sequences from the protocol and put it into a map "mapElementInputSequences".
	//mapElementInputSequences associate sequences with their respective input steps.
	private void getModelFragmentsForVerification(){
		for (int i = 0; i < protocol.getSequence().size(); i++) {
			if(!mapElementInputSequences.containsKey(protocol.getSequence().get(i).getInputStep())) {
				List<Sequence> sequences = new ArrayList<Sequence>();
				mapElementInputSequences.put(protocol.getSequence().get(i).getInputStep(), sequences);
				mapElementInputSequences.get(protocol.getSequence().get(i).getInputStep()).add(protocol.getSequence().get(i));
			}else {
				mapElementInputSequences.get(protocol.getSequence().get(i).getInputStep()).add(protocol.getSequence().get(i));
			}
		}
	}
	
	//Return a map with the element's name as a key and a list of all solutions that the element is a inaccessible step as a value of the key.
	//If all input sequences of the element are false then this element is false and it's a inaccessible step.
	//If a input sequence of the element is true but the output step from sequence is false, then the element is false too.
	//If all output step from all imput squences from a element are false, then this element is false too.
	public  List<String> inaccessibleStepsSolutions(){
		List<String> inaccessibleStep = new ArrayList<String>();
		Map<String, List<Solution>> mapSolutions = new HashMap<String, List<Solution>>();//map that will be return.
		List<List<BoolVar>>boolSequencesList = new ArrayList<List<BoolVar>>();//A list of a set of all imput sequences of a element as a BoolVar.
		List<List<Element>>outputStepList = new ArrayList<List<Element>>();// A list of a set of element that is the output step of the sequences above.	
		//Convert all sequences in protocol to boolVar and put it into boolSequencesList.
		for(int i = 0; i < mapElementInputSequences.values().size(); i++){
			List<Sequence> sequences = (List<Sequence>) mapElementInputSequences.values().toArray()[i];
			Map<BoolVar, Element> mapBoolSequences = sequenceListToMap(model,sequences); //Get a map with the Boolvar sequence as key and the output step of this sequence as value.
			boolSequencesList.add(setToBoolVarList(mapBoolSequences.keySet()));
			outputStepList.add(collectionToElementList(mapBoolSequences.values()));
		}
		//boolElements is a list of the elements in mapElementInputSequences as BoolVar.
		List<BoolVar> boolElements = opElements.elementsToBoolvarList(collectionToElementList(mapElementInputSequences.keySet()), boolSequencesList, model);
		//Interact over all set of sequence in boolSequencesList.
		for(int j1 = 0; j1 < boolSequencesList.size(); j1++) {
			List<BoolVar> auxBools = new ArrayList<BoolVar>();
			//Interact over each sequence in the set boolSequencesList.g(j1).
			for(int j2 = 0; j2 < boolSequencesList.get(j1).size(); j2++) {
				int index = indexOf(boolElements, outputStepList.get(j1).get(j2)); //the index of the output step of the sequence in boolElements.
				//if the output step is not defined in boolElement, that is, It's not the initial step.
				if(index != -1) {
					//auxBool is true if the sequence and its output step are true.
					BoolVar auxBool = model.arithm(boolElements.get(index), "+", boolSequencesList.get(j1).get(j2), "=", 2).reify();
					auxBools.add(auxBool);
					if(j2 == boolSequencesList.get(j1).size()-1) {
						int size = auxBools.size();
						//interact over auxBools
						for(int l = 0; l < size-1; l++){
							BoolVar auxBool2 = model.arithm(auxBools.get(l), "+", auxBools.get(auxBools.size()-1), ">", 0).reify();
							auxBools.add(auxBool2);
						}
						//the element in boolElements is true if one of BoolVar in auxBools is true.
						model.arithm(auxBools.get(auxBools.size()-1), "=", 1).reifyWith(boolElements.get(j1));
					}
				}else {
					//the element in boolElements is true if its input sequence is true.
					model.arithm(boolSequencesList.get(j1).get(j2), "=", 1).reifyWith(boolElements.get(j1));
				}
			}
		}
		//Interact over all BoolVar Elements in boolElements.
		for(int k = 0; k < boolElements.size(); k++) {
			model.arithm(boolElements.get(k), "=", 1).post(); // post the constraint: the element k is true, that is, it's not a inaccessible step.
			//mapSolutions.put(boolElements.get(k).getName(), model.getSolver().findAllSolutions());
			if(model.getSolver().findAllSolutions().isEmpty()) {
				inaccessibleStep.add(boolElements.get(k).getName());
			}
			model.getSolver().reset(); //reset the solver for a new interaction
			model.unpost(model.getCstrs()[model.getCstrs().length-1]); //unpost the last constraint.
		}
		
		return inaccessibleStep;
	}
	
	//Tranform a list of sequences in a Map of a sequence representation as BoolVar and its output step.
	//This boolvar are constrained with some operations.
	private Map<BoolVar, Element> sequenceListToMap(Model model, List<Sequence> sequences) {
		Map<BoolVar, Element> mapBoolSequences = new HashMap<BoolVar, Element>();//boolSequences is a list that will contain a sequence structure as a boolvar.
		//Go through all sequences in the list.
		for (int i = 0; i < sequences.size(); i++) {
			BoolVar boolSequence = sequenceToBoolVar(model, sequences.get(i), boolVars, intVars);
			if(boolSequence != null) {
				mapBoolSequences.put(boolSequence, sequences.get(i).getOutputStep());
			}			
		}
		return mapBoolSequences;
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
	
	//return the index of the element in the list boolElements.
	//If the element is not in the list, return -1.
	private int indexOf(List<BoolVar> boolElements, Element element) {
		String elementName = element.getClass().getSimpleName() +"::"+ element.getName();
		for (int i = 0; i < boolElements.size(); i++) {
			if(boolElements.get(i).getName().equals(elementName)) {
				return i;
			}
		}
		return -1;
	}
	
	//Convert a set of BoolVar in a list.
	private List<BoolVar> setToBoolVarList(Set<BoolVar> set){
		List<BoolVar> list = new ArrayList<BoolVar>();
		for(int i = 0; i < set.size(); i++) {
			list.add((BoolVar) set.toArray()[i]);
		}
		return list;
	}
	
	//COnvert a collection of element in a list.
	private List<Element> collectionToElementList(Collection<Element> collection){
		List<Element> list = new ArrayList<Element>();
		for(int i = 0; i < collection.size(); i++) {
			list.add((Element) collection.toArray()[i]);
		}
		return list;
	}
}
