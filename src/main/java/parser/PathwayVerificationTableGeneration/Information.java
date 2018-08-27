package parser.PathwayVerificationTableGeneration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.chocosolver.solver.Solution;

import parser.PathwayVerificationTest.FindInaccessibleStep2;
import parser.PathwayVerificationTest.FindSolutions;
import pathwayMetamodel.Element;
import pathwayMetamodel.Pathway;
import pathwayMetamodel.Sequence;

public class Information {
	Pathway pathway;
	FindSolutions findSolutions;
	FindInaccessibleStep2 inaccessibleStep2;
	
	
	public Information(Pathway pathway) {
		this.pathway = pathway;
		this.findSolutions = new FindSolutions(pathway);
		this.inaccessibleStep2 = new FindInaccessibleStep2(pathway);
	}
	
	public int getStatesNumber() {
		return pathway.getElement().size();
	}
	
	public int getTransitionsNumber() {
		return pathway.getSequence().size();
	}
	
	public int getDeadlockNumber() {
		Map<Element, List<Solution>> mapSolutions = findSolutions.findDeadLockSolutions();
		return mapSolutions.size();
	}
	
	public int getNonDeterminismNumber() {
		Map<Element, List<Solution>> mapSolutions = findSolutions.findNonDeterminismSolutions();
		return mapSolutions.size();
	}
	
	public int getEquivalentTransitionsNumber() {
		Map<Element, List<Sequence>> mapSolutions = findSolutions.findLogicallyEquivalentSequence();
		return mapSolutions.size();
	}
	
	public int getInaccessibleStepNumber() {
		List<Element> states = inaccessibleStep2.findInaccessibleSteps();
		return states.size();
	}
	
	public int pathNumber() {
		List<Element> visitedElements = new ArrayList<Element>();
		Stack<Element> elementsStack = new Stack<Element>();
		int pathNumber = 0;
		
		//Get the initial step (root) of the pathway and put it in the stack, the lists of 
		//visited elements  and accessible elements (The initial step is always accessible)
		Element initialElement = getInitialStep(pathway.getElement());
		elementsStack.push(initialElement);
		
		//Loop to find all accessible elements
		//It works as depth-first search
		while(elementsStack.isEmpty() == false) {
			Element element = elementsStack.pop(); //get the last element from the stack
			int index = 0;
			//Check if the step has not already been visited
			if(!visitedElements.contains(element)) {
				if(element == null || element.getOutputSequences().size() == 0) {
					pathNumber++;
					visitedElements.clear();
				}else {
					//Loop to find the next step to be verified
					for(index = 0; index < element.getOutputSequences().size(); index++) {
						//if(element.getOutputSequences().get(index).getInputStep() != initialElement) {
							Element inputStep = element.getOutputSequences().get(index).getInputStep();
							elementsStack.push(inputStep);
						//}
					}
					visitedElements.add(element);
				}
			}
		}
		System.out.println(pathNumber);
		return pathNumber;
	}
	
	//Method to get the initial step from the pathway
	private Element getInitialStep(List<Element> elementList) {
		for (int i = 0; i < elementList.size(); i++) {
			if(elementList.get(i).isIsInitial()) {
				return elementList.get(i);
			}
		}
		return null;
	}

}
