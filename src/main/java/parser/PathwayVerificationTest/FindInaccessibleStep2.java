package parser.PathwayVerificationTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.BoolVar;
import org.eclipse.emf.common.util.EList;

import pathwayMetamodel.Element;
import pathwayMetamodel.Pathway;
import pathwayMetamodel.Sequence;

public class FindInaccessibleStep2 extends SequenceParser {
	
	public FindInaccessibleStep2(Pathway pathway) {
		super(pathway);
	}

	//Return a set of Elements that is a inaccessible step.
	//An element is a inaccessible step if there are no valid path to get to the element.
	public List<Element> findInaccessibleSteps() {
		List<Element> accessibleElements = new ArrayList<Element>();
		List<Element> visitedElements = new ArrayList<Element>();
		Stack<Element> elementsStack = new Stack<Element>();
		List<Sequence> sequenceList = new ArrayList<Sequence>();

		Element initialElement = pathway.getElement().get(0);
		accessibleElements.add(initialElement);
		visitedElements.add(initialElement);
		elementsStack.push(initialElement);
		
		while(elementsStack.isEmpty() == false) {
			Model model = new Model("Find All Solutions: " + 1); //Create a model to verify a valid path with ChocoSolver.
			Element element = elementsStack.lastElement();
			int index = 0;
			for(index = 0; index < element.getOutputSequences().size(); index++) {
				Element inputStep = element.getOutputSequences().get(index).getInputStep();
				if(!visitedElements.contains(inputStep)) {
					visitedElements.add(inputStep);
					break;
				}
			}
			if(index == element.getOutputSequences().size()) {
				elementsStack.pop();
				if(sequenceList.size() > 0) {
					sequenceList.remove(sequenceList.size()-1);
				}
			}else {
				Sequence sequence = element.getOutputSequences().get(index);
				sequenceList.add(sequence);
				List<BoolVar> boolSequences = sequenceListToBoolVarList(model, sequenceList);//Get a list of BoolVar from a list of sequence of the mapElementOutputSequences.
				
				if (boolSequences!=null && !boolSequences.isEmpty()) {
					for(int j = 0; j < boolSequences.size(); j++) {
						model.arithm(boolSequences.get(j), "=", 1).post();//Post the constraint "the sequence have to be true" to the model.
					}
					if(model.getSolver().findSolution() != null) {
						accessibleElements.add(sequence.getInputStep());
						if(sequence.getInputStep().getOutputSequences() != null) {
							elementsStack.push(sequence.getInputStep());
						}else {
							sequenceList.remove(sequenceList.size()-1);
						}
					}else {
						sequenceList.remove(sequenceList.size()-1);
					}
				}
			}
		}
		return getInaccessibleElements(pathway.getElement(), accessibleElements);
	}

	private List<Element> getInaccessibleElements(List<Element> elements, List<Element> accessibleElements) {
		List<Element> inaccessibleElements = new ArrayList<Element>();
		for(int i = 0; i < elements.size(); i++) {
			if(!accessibleElements.contains(elements.get(i))) {
				inaccessibleElements.add(elements.get(i));
			}
		}
		return inaccessibleElements;
	}

}
