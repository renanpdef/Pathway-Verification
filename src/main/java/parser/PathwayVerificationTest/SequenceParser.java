package parser.PathwayVerificationTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import pathwayMetamodel.Element;
import pathwayMetamodel.Operation;
import pathwayMetamodel.Pathway;
import pathwayMetamodel.Sequence;

public class SequenceParser {
	private OperationParser operations = new OperationParser(); //Instantiates the class that handles operations between BoolBar variables.
	private OperandParser operands = new OperandParser();  //Instantiates the class that handles operations between BoolVar variables.
	protected Pathway pathway; //The pathway to be analyzed.
	protected Map<Element, List<Sequence>> mapElementOutputSequences = new HashMap<Element, List<Sequence>>(); //A map that stores all the elements and their respective output sequences from the pathway.
	
	// A constructor to initialize the pathway and the mapElementOutputSequences.
	public SequenceParser(Pathway pathway) {
		this.pathway = pathway;
		getModelFragmentsForVerification();
	}
	
	//Return a map with Elements as a key and a set of sequences logically equivalent as a value of the key.
	//This function verify if the operations two or more sequences with the same output step are Logically Equivalent.
	public Map<Element, List<Sequence>> findLogicallyEquivalentSequence() {
		System.out.println(mapElementOutputSequences.size());
		System.out.println(mapElementOutputSequences.values().size());
		Map<Element, List<Sequence>> mapLogicallyEquivalentSequence = new HashMap<Element, List<Sequence>>();
		for(int i = 0; i < mapElementOutputSequences.values().size(); i++){
			Model model = new Model("Find All Solutions: " + i); //Create a model to verify the valid solutions of a element in the mapElementOutputSequences with ChocoSolver.
			List<Sequence> sequences = (List<Sequence>) mapElementOutputSequences.values().toArray()[i];
			List<BoolVar> boolSequences = sequenceListToBoolVarList(model, sequences);//Get a list of BoolVar from a list of sequence of the mapElementOutputSequences.
			if (boolSequences!=null && !boolSequences.isEmpty() && boolSequences.size() > 1) {
				List<Sequence> LogicallyEquivalentSequences = new ArrayList<Sequence>();
				for(int k1 = 0; k1 < boolSequences.size()-1; k1++) {
					for(int k2 = k1+1; k2< boolSequences.size(); k2++) {
						model.arithm(boolSequences.get(k1), "+", boolSequences.get(k2),"=", 1).post();
						Solution solution = model.getSolver().findSolution();
						if(solution == null) {
							LogicallyEquivalentSequences.add(sequences.get(k1));
							LogicallyEquivalentSequences.add(sequences.get(k2));
						}
						model.getSolver().reset();//reset the solver for a new interaction
						model.unpost(model.getCstrs()[model.getCstrs().length-1]);//unpost the last constraint.
					}
				}
				if(!LogicallyEquivalentSequences.isEmpty()) {
					mapLogicallyEquivalentSequence.put((Element) mapElementOutputSequences.keySet().toArray()[i], LogicallyEquivalentSequences);
				}
			}
		}
		return mapLogicallyEquivalentSequence;
	}

	//get all the sequences from the pathway and put it into a map "mapElementOutputSequences".
	//mapElementOutputSequences associate sequences with their respective output steps.
	private void getModelFragmentsForVerification(){
		for (int i = 0; i < pathway.getSequence().size(); i++) {
			if(!mapElementOutputSequences.containsKey(pathway.getSequence().get(i).getOutputStep())) {
				List<Sequence> sequences = new ArrayList<Sequence>();
				mapElementOutputSequences.put(pathway.getSequence().get(i).getOutputStep(), sequences);
				mapElementOutputSequences.get(pathway.getSequence().get(i).getOutputStep()).add(pathway.getSequence().get(i));
			}else {
				mapElementOutputSequences.get(pathway.getSequence().get(i).getOutputStep()).add(pathway.getSequence().get(i));
			}
		}
	}
	
	//Tranform a list of sequences in a list of boolvar.
	//This boolvar are constrained with some operations.
	protected List<BoolVar> sequenceListToBoolVarList(Model model, List<Sequence> sequences) {
		List<BoolVar>  booleanOperands = new ArrayList<BoolVar>();//booleanOperands is a list that will contain the operands as a boolvar variables.
		List<IntVar>  numericOperands = new ArrayList<IntVar>();//numericOperands is a list that will contain the operands as a intVar variables.
		List<BoolVar> boolSequences = new ArrayList<BoolVar>();//boolSequences is a list that will contain a sequence structure as a boolvar.
		//Go through all sequences in the list.
		for (int i = 0; i < sequences.size(); i++) {
			BoolVar boolSequence = sequenceToBoolVar(model, sequences.get(i), booleanOperands, numericOperands);
			if(boolSequence != null) {
				boolSequences.add(boolSequence);
			}
			redundantOperandChecking(model, booleanOperands, numericOperands);
		}
		return boolSequences;
	}		
	
	//function to convert a sequence in a boolvar.
	//model is a chocosolver model where the constraints will be post.
	//Sequence is the sequence that will be transform.
	//booleanOperands is a list that will contain the operands as a boolvar variables.
	//numericOperands is a list that will contain the operands as a intVar variables.
	protected BoolVar sequenceToBoolVar(Model model, Sequence sequence, List<BoolVar> booleanOperands, List<IntVar> numericOperands){
		if(sequence.getOperation() != null) {
			Operation operation = sequence.getOperation();
			operands.operandsIntoLists(booleanOperands, numericOperands, model, operation); //Update the lists booleanOperands and numericOperands with new operands from op.			
			return operations.createBoolVarSequence(operation, booleanOperands, numericOperands); //return the sequence as a BoolVar variable.	
		}else {
			return model.boolVar(sequence.getName());
		}
	}	
	
	//Method to ensure the same values from redundant operands
	private void redundantOperandChecking( Model model, List<BoolVar> booleanOperands, List<IntVar> numericOperands){
		for (int i = 0; i < numericOperands.size(); i++) {
			for (int j = 0; j < booleanOperands.size(); j++) {
				if(numericOperands.get(i).getName().equals(booleanOperands.get(j).getName())) {
					model.arithm(numericOperands.get(i), "+", booleanOperands.get(j), "!=",numericOperands.get(i).getUB()).post();
					model.arithm(numericOperands.get(i), "+", booleanOperands.get(j), "!=",1).post();
				}
			}
		}
	}
}
