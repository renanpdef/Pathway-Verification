package parser.PathwayVerificationTableGeneration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.BoolVar;

import parser.PathwayVerification.FindInaccessibleStep;
import parser.PathwayVerification.FindSolutions;
import pathwayMetamodel.Element;
import pathwayMetamodel.Operand;
import pathwayMetamodel.Operation;
import pathwayMetamodel.Pathway;
import pathwayMetamodel.Sequence;

//Class to extract information about the pathways
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
	
	public int pathNumber() {
		List<Sequence> path = new ArrayList<Sequence>(); //a set of transitions forming a path
		Stack<Element> elementsStack = new Stack<Element>();
		Stack<Sequence> transitionsStack = new Stack<Sequence>();
		int pathNumber = 0;
		
		//Get the initial step (root) of the pathway and put it in the stack, the lists of 
		Element initialElement = getInitialStep(pathway.getElement());
		elementsStack.push(initialElement);
		
		//Loop to find all paths
		//It works as depth-first search
		while(elementsStack.isEmpty() == false) {
			Element element = elementsStack.pop(); //remove the last element from the stack
			//add the last transition in the path
			if(!transitionsStack.isEmpty()) {
				path.add(transitionsStack.pop());
			}
			//Check if the step is a final step
			if(element == null || element.getOutputSequences().size() == 0) {
				pathNumber++;
				if(!transitionsStack.isEmpty()) {
					removeTransitons(path, transitionsStack.lastElement());
				}
			}else {
				//Loop to find the next step to be verified
				boolean addTransition = false;
				for(int index = 0; index < element.getOutputSequences().size(); index++) {
					Sequence transition = element.getOutputSequences().get(index);
					if(!path.contains(transition)) {
						Element inputStep = element.getOutputSequences().get(index).getInputStep();
						//System.out.println("ADD: " + inputStep.getName());
						elementsStack.push(inputStep);
						transitionsStack.push(transition);
						addTransition = true;
					}
				}
				if(addTransition == false) {
					if(!transitionsStack.isEmpty()) {
						removeTransitons(path, transitionsStack.lastElement());
					}
				}
			}
		}
		return pathNumber;
	}
	
	//Remove transitions that have not yet been traversed by in the new path.
	private void removeTransitons(List<Sequence> path, Sequence transition) {
		int j = path.size()-1;
		for (int i = path.size()-1; i >= 0; i--) {
			if(path.get(i) == transition) {
				break;
			}
			else if(path.get(i).getOutputStep() == transition.getOutputStep()) {
				path.remove(i);
				break;
			}else {
				path.remove(i);
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
