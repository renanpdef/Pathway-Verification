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
	
	public  Map<String, List<Solution>> inaccessibleStepsSolutions(){
		Map<String, List<Solution>> mapSolutions = new HashMap<String, List<Solution>>();
		List<List<BoolVar>>boolSequencesList = new ArrayList<List<BoolVar>>();
		List<List<Element>>outputStepList = new ArrayList<List<Element>>();
		//Convert all sequences in protocol to boolVar and put it into boolSequencesList.
		for(int i = 0; i < mapElementInputSequences.values().size(); i++){
			List<Sequence> sequences = (List<Sequence>) mapElementInputSequences.values().toArray()[i];
			Map<BoolVar, Element> mapBoolSequences = sequenceListToBoolVarList(model,sequences); //Get a list of BoolVar from a list of sequence of the mapElementOutputSequences.
			boolSequencesList.add(setToBoolVarList(mapBoolSequences.keySet()));
			outputStepList.add(collectionToElementList(mapBoolSequences.values()));
		}
		List<Element> elements = collectionToElementList(mapElementInputSequences.keySet());
		List<BoolVar> boolElements = opElements.elementsToBoolvarList(elements, boolSequencesList, model);
		for(int j1 = 0; j1 < boolSequencesList.size(); j1++) {
			List<BoolVar> auxBools = new ArrayList<BoolVar>();
			for(int j2 = 0; j2 < boolSequencesList.get(j1).size(); j2++) {
				int index = indexOf(boolElements, outputStepList.get(j1).get(j2));
				if(index != -1) {
					//model.arithm(boolElements.get(index), "=", 0).reifyWith(boolSequencesList.get(j1).get(j2));
					//BoolVar auxBool = model.arithm(boolElements.get(index), "+", boolSequencesList.get(j1).get(j2), "=", 2).reify();
					BoolVar auxBool = model.arithm(boolElements.get(index), "+", boolSequencesList.get(j1).get(j2), "=", 2).reify();
					auxBools.add(auxBool);
					int size = auxBools.size();
					if(j2 == boolSequencesList.get(j1).size()-1) {
						for(int l = 0; l < size-1; l++){
							BoolVar auxBool2 = model.arithm(auxBools.get(l), "+", auxBools.get(auxBools.size()-1), ">", 0).reify();
							auxBools.add(auxBool2);
						}
						model.arithm(auxBools.get(auxBools.size()-1), "=", 1).reifyWith(boolElements.get(j1));
					}
				}else {
					model.arithm(boolSequencesList.get(j1).get(j2), "=", 1).reifyWith(boolElements.get(j1));
				}
			}
		}
		
		for(int k = 0; k < boolElements.size(); k++) {
			model.arithm(boolElements.get(k), "=", 0).post();
			mapSolutions.put(boolElements.get(k).getName(), model.getSolver().findAllSolutions());
			model.getSolver().reset();
			model.unpost(model.getCstrs()[model.getCstrs().length-1]);
		}
		
		return mapSolutions;
	}
	
	//Tranform a list of sequences in a list of boolvar.
	//This boolvar are constrained with some operations.
	private Map<BoolVar, Element> sequenceListToBoolVarList(Model model, List<Sequence> sequences) {
		Map<BoolVar, Element> mapBoolSequences = new HashMap<BoolVar, Element>();//boolSequences is a list that will contain a sequence structure as a boolvar.
		//Go through all sequences in the list.
		for (int i = 0; i < sequences.size(); i++) {
			BoolVar boolSequence = sequenceToBoolVar(model, sequences.get(i), boolVars, intVars);
//			System.out.println(sequences.get(i).getName());
//			System.out.println(boolSequence.getName());
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
	
	private int indexOf(List<BoolVar> boolElements, Element element) {
		String elementName = element.getClass().getSimpleName() +"::"+ element.getName();
		for (int i = 0; i < boolElements.size(); i++) {
			if(boolElements.get(i).getName().equals(elementName)) {
				return i;
			}
		}
		return -1;
	}
	
	private List<BoolVar> setToBoolVarList(Set<BoolVar> set){
		List<BoolVar> list = new ArrayList<BoolVar>();
		for(int i = 0; i < set.size(); i++) {
			list.add((BoolVar) set.toArray()[i]);
		}
		return list;
	}
	
	private List<Element> collectionToElementList(Collection<Element> collection){
		List<Element> list = new ArrayList<Element>();
		for(int i = 0; i < collection.size(); i++) {
			list.add((Element) collection.toArray()[i]);
		}
		return list;
	}
}
