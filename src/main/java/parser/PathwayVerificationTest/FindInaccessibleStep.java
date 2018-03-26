package parser.PathwayVerificationTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.BoolVar;
import org.eclipse.emf.common.util.EList;

import pathwayMetamodel.Element;
import pathwayMetamodel.Pathway;
import pathwayMetamodel.Sequence;

public class FindInaccessibleStep extends SequenceParser {
	
	public FindInaccessibleStep(Pathway pathway) {
		super(pathway);
	}

	//Return a set of Elements that is a inaccessible step.
	//An element is a inaccessible step if there are no valid path to get to the element.
	public List<Element> findInaccessibleSteps() {
		List<Element> accessibleElements = new ArrayList<Element>();
		for(int i = 0; i < mapElementOutputSequences.values().size(); i++){
			Model model = new Model("Find All Solutions: " + i); //Create a model to verify the valid solutions of a element in the mapElementOutputSequences with ChocoSolver.
			Element element = (Element) mapElementOutputSequences.keySet().toArray()[i];
			//The root element has no input sequence
			if(element.getInputSequences().isEmpty()) {
				accessibleElements.add(element);
			}
			List<Sequence> sequences = (List<Sequence>) mapElementOutputSequences.values().toArray()[i];
			List<BoolVar> boolSequences = sequenceListToBoolVarList(model, sequences);//Get a list of BoolVar from a list of sequence of the mapElementOutputSequences.
			if (boolSequences!=null && !boolSequences.isEmpty()) {
				//Verify if there is only one boolvar in the list.
				if(boolSequences.size()==1) {
					model.arithm(boolSequences.get(0), "=", 1).post();//Post the constraint "the sequence have to be true" to the model.
				}
				else {
					List<BoolVar> auxSeq = new ArrayList<BoolVar>();//Auxiliary list of boolvar to store the new boolvar variables.
					if(boolSequences.size() == 2) {
						model.arithm(boolSequences.get(0), "+", boolSequences.get(1), "=", 1).post();
					}else {
						for(int k = 1; k < boolSequences.size(); k++) {
							if (auxSeq.isEmpty()) {
								model.arithm(boolSequences.get(k-1), "+", boolSequences.get(k), "<", 2).post();//Post the constraint "sequence k-1 and sequence k can't be both true" to the model.
								BoolVar aux = model.arithm(boolSequences.get(k-1), "+", boolSequences.get(k), "=", 1).reify();//Create a boolvar with the constraint "boolSequences.get(k-1) xor boolSequences.get(k)".
								auxSeq.add(aux);
							}else if(k < boolSequences.size()-1){
								model.arithm(auxSeq.get(auxSeq.size()-1), "+", boolSequences.get(k), "<", 2).post();//Post the constraint "the last boolvar in the auxSeq and sequence k can't be both true" to the model.
								BoolVar aux = model.arithm(auxSeq.get(auxSeq.size()-1), "+", boolSequences.get(k), "=", 1).reify();//Create a boolvar with the constraint "the last boolvar in the auxSeq xor boolSequences.get(k)".
								auxSeq.add(aux);
							//in the last sequence of the list
							}else {
								model.arithm(auxSeq.get(auxSeq.size()-1), "+", boolSequences.get(k), "=", 1).post();//Post the constraint "the last boolvar in the auxSeq xor sequence k" to the model.
							}
						}
					}
				}
				for(int j = 0; j < boolSequences.size(); j++) {
					model.arithm(boolSequences.get(j), "=", 1).post();
					if(model.getSolver().findSolution() != null) {
						if(!accessibleElements.contains(sequences.get(j).getInputStep())) {
							accessibleElements.add(sequences.get(j).getInputStep());
						}
					}
					model.getSolver().reset();//reset the solver for a new interaction
					model.unpost(model.getCstrs()[model.getCstrs().length-1]);//unpost the last constraint.
				}
			}
			
		}
		List<Element> inaccessibleElements = getInaccessibleElements(accessibleElements, pathway.getElement());
		return inaccessibleElements;
	}
	
	//Function to pruning a set of possible accessible steps and return all inaccessible elements
	private List<Element> getInaccessibleElements(List<Element> accessibleElements, List<Element> elements) {
		List<Element> inaccessibleElements = new ArrayList<Element>();
		for(int i = 0; i < elements.size(); i++) {
			//verify if the element is not in the list accessibleElements.
			if(!accessibleElements.contains(elements.get(i))) {
				inaccessibleElements.add(elements.get(i));
			}
			else {
				int count = 0;
				//go through the set of input sequence of element
				for(int j = 0; j < elements.get(i).getInputSequences().size(); j++) {
					//check if the output step of the sequence is not in the list accessibleElements and increase the count. 
					if(!accessibleElements.contains(elements.get(i).getInputSequences().get(j).getOutputStep())) {
						count++;
					}
				}
				//if all output step of this sequences are inaccessible step then this element is a inaccessible step too.
				if(count == elements.get(i).getInputSequences().size() && count != 0) {
					inaccessibleElements.add(elements.get(i));
					accessibleElements.remove(elements.get(i));
				}
			}
		}
		return inaccessibleElements;
	}
}
