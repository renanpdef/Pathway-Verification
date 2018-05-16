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

public class FindInaccessibleStep2 extends SequenceParser {
	
	public FindInaccessibleStep2(Pathway pathway) {
		super(pathway);
	}

	//Return a set of Elements that is a inaccessible step.
	//An element is a inaccessible step if there are no valid path to get to the element.
	public List<Element> findInaccessibleSteps() {
		List<Element> visitedElementList = new ArrayList<Element>();
		List<Element> accessibleElements = new ArrayList<Element>();
		List<Element> inaccessibleElements = new ArrayList<Element>();
		
		Element root = pathway.getElement().get(0);
		accessibleElements.add(root);
		visitedElementList.add(root);
		Sequence sequence = root.getOutputSequences().get(0);
		int pos = 0;
		
		while(visitedElementList.size() < pathway.getElement().size()) {
			Model model = new Model("Find All Solutions: " + 1); //Create a model to verify a valid path with ChocoSolver.
			List<Sequence> sequenceList = new ArrayList<Sequence>();
			sequenceList.add(sequence);
			List<BoolVar> boolSequences = sequenceListToBoolVarList(model, sequenceList);//Get a list of BoolVar from a list of sequence of the mapElementOutputSequences.
			
			if (boolSequences!=null && !boolSequences.isEmpty()) {
				for(int i = 0; i < boolSequences.size(); i++) {
					model.arithm(boolSequences.get(i), "=", 1).post();//Post the constraint "the sequence have to be true" to the model.
				}
				if(model.getSolver().findSolution() != null) {
					accessibleElements.add(sequence.getInputStep());
					if(sequence.getInputStep().getOutputSequences() != null) {
						sequence = sequence.getInputStep().getOutputSequences().get(pos);
					}else {
						pos++;
						sequence = sequence.getOutputStep().getOutputSequences().get(pos);
					}
				}else {
					inaccessibleElements.add(sequence.getInputStep());
				}
			}
			
		}
		
		return null;
	}

}
