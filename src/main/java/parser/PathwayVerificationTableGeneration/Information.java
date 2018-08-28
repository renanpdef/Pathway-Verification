package parser.PathwayVerificationTableGeneration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.BoolVar;

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
//						//if(!visitedElements.contains(inputStep)){
//							System.out.println("ADD: " + inputStep.getName());
//							elementsStack.push(inputStep);
//						//}
//					}
//					visitedElements.add(element);
//				}
//			}
//		}
//		System.out.println(pathNumber);
//		return pathNumber;
//	}
	
	public int pathNumber() {
		List<Element> visitedElements = new ArrayList<Element>();
		Stack<Element> elementsStack = new Stack<Element>();
		List<Sequence> sequenceList = new ArrayList<Sequence>();

		//Get the initial step (root) of the pathway and put it in the stack, the lists of 
		//visited elements  and accessible elements (The initial step is always accessible)
		Element initialElement = getInitialStep(pathway.getElement());
		visitedElements.add(initialElement);
		elementsStack.push(initialElement);
		int pathNumber = 0;
		
		//Loop to find all accessible elements
		//It works as depth-first search
		while(elementsStack.isEmpty() == false) {
			Element element = elementsStack.lastElement(); //get the last element from the stack
			int index = 0;
			//Loop to find the next step to be verified
			for(index = 0; index < element.getOutputSequences().size(); index++) {
				Element inputStep = element.getOutputSequences().get(index).getInputStep();
				//Check if the step has not already been visited
				if(!visitedElements.contains(inputStep)) {
					visitedElements.add(inputStep);
					break;
				}
			}
			//Check if there are no more step to be verified from element
			//Remove the element from the stack and the last sequence from the sequenceList
			if(index == element.getOutputSequences().size()) {
				elementsStack.pop();
				if(sequenceList.size() > 0) {
					sequenceList.remove(sequenceList.size()-1);
				}
			}else {
				//Get the output sequece from element an add to sequenceList
				Sequence sequence = element.getOutputSequences().get(index);
				sequenceList.add(sequence);				
				if (sequenceList!=null && !sequenceList.isEmpty()) {
					if(sequence.getInputStep() != null && sequence.getInputStep().getOutputSequences() != null && sequence.getInputStep().getOutputSequences().size() != 0) {
						elementsStack.push(sequence.getInputStep());
					}else {
						pathNumber++;
						sequenceList.remove(sequenceList.size()-1);
					}
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
