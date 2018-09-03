package parser.PathwayVerificationTableGeneration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.BoolVar;

import parser.PathwayVerificationTest.FindInaccessibleStep;
import parser.PathwayVerificationTest.FindSolutions;
import pathwayMetamodel.Element;
import pathwayMetamodel.Pathway;
import pathwayMetamodel.Sequence;

public class Information {
	Pathway pathway;
	FindSolutions findSolutions;
	FindInaccessibleStep inaccessibleStep;
	
	
	public Information(Pathway pathway) {
		this.pathway = pathway;
		this.findSolutions = new FindSolutions(pathway);
		this.inaccessibleStep = new FindInaccessibleStep(pathway);
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
		List<Element> states = inaccessibleStep.findInaccessibleSteps();
		return states.size();
	}
	
//	public int pathNumber() {
//		List<Element> visitedElements = new ArrayList<Element>();
//		List<Sequence> transitions = new ArrayList<Sequence>();
//		Stack<Element> elementsStack = new Stack<Element>();
//		int pathNumber = 0;
//		
//		//Get the initial step (root) of the pathway and put it in the stack, the lists of 
//		//visited elements  and accessible elements (The initial step is always accessible)
//		Element initialElement = getInitialStep(pathway.getElement());
//		elementsStack.push(initialElement);
//		
//		//Loop to find all accessible elements
//		//It works as depth-first search
//		while(elementsStack.isEmpty() == false) {
//			Element element = elementsStack.pop(); //get the last element from the stack
//			System.out.println(element.getName());
//			int index = 0;
//			//Check if the step has not already been visited
//			if(!visitedElements.contains(element)) {
//				if(element == null || element.getOutputSequences().size() == 0) {
//					pathNumber++;
//					System.out.println("----------------");
//					//visitedElements.clear();
//				}else {
//					//Loop to find the next step to be verified
//					for(index = 0; index < element.getOutputSequences().size(); index++) {
//						Element inputStep = element.getOutputSequences().get(index).getInputStep();
//						if(!visitedElements.contains(inputStep)){
//							System.out.println("ADD: " + inputStep.getName());
//							elementsStack.push(inputStep);
//						}
//					}
//					visitedElements.add(element);
//				}
//			}
//		}
//		System.out.println(pathNumber);
//		return pathNumber;
//	}
	
	public int pathNumber() {
		//Map<Element,Element> linkedStates = new HashMap<Element, Element>();
		List<Sequence> transitions = new ArrayList<Sequence>();
		Stack<Element> elementsStack = new Stack<Element>();
		Stack<Sequence> transitionsStack = new Stack<Sequence>();
		int pathNumber = 0;
		
		//Get the initial step (root) of the pathway and put it in the stack, the lists of 
		//visited elements  and accessible elements (The initial step is always accessible)
		Element initialElement = getInitialStep(pathway.getElement());
		elementsStack.push(initialElement);
		
		//Loop to find all accessible elements
		//It works as depth-first search
		while(elementsStack.isEmpty() == false) {
			Element element = elementsStack.pop(); //get the last element from the stack
			if(!transitionsStack.isEmpty()) {
				transitions.add(transitionsStack.pop());
			}
			//System.out.println(element.getName());
			//Check if the step has not already been visited
			if(element == null || element.getOutputSequences().size() == 0) {
				pathNumber++;
				//System.out.println("----------------");
				if(!transitionsStack.isEmpty()) {
					removeTransitons(transitions, transitionsStack.lastElement());
				}
				//visitedElements.clear();
			}else {
				//Loop to find the next step to be verified
				boolean addTransition = false;
				for(int index = 0; index < element.getOutputSequences().size(); index++) {
					Sequence transition = element.getOutputSequences().get(index);
					if(!transitions.contains(transition)) {
						Element inputStep = element.getOutputSequences().get(index).getInputStep();
						//System.out.println("ADD: " + inputStep.getName());
						elementsStack.push(inputStep);
						transitionsStack.push(transition);
						addTransition = true;
					}
				}
				if(addTransition == false) {
					if(!transitionsStack.isEmpty()) {
						removeTransitons(transitions, transitionsStack.lastElement());
					}
				}
			}
		}
		//System.out.println(pathNumber);
		return pathNumber;
	}
	
	private void removeTransitons(List<Sequence> transitions, Sequence transition) {
		int j = transitions.size()-1;
		for (int i = transitions.size()-1; i >= 0; i--) {
			if(transitions.get(i) == transition) {
				break;
			}
			else if(transitions.get(i).getOutputStep() == transition.getOutputStep()) {
				transitions.remove(i);
				break;
			}else {
				transitions.remove(i);
			}
		}
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
