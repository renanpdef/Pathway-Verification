package parser.PathwayVerificationTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.BoolVar;

import pathwayMetamodel.Element;
import pathwayMetamodel.Pathway;
import pathwayMetamodel.Sequence;

public class FindSolutions extends SequenceParser{

	public FindSolutions(Pathway pathway) {
		super(pathway);
	}

	//Return a map with Elements as a key and a list of all solutions that occurs deadlock in the element as a value of the key.
	//If the operands have the same values in the solution, then deadlock occurs.
	//An element is in deadlock when all its output sequences are false.
	public Map<String, Double> findDeadLockSolutions(){
		Map<String, Double> mapStatistics = new HashMap<String, Double>();
		int binaryTree = 0;
		for(int i = 0; i < mapElementOutputSequences.values().size(); i++){
			Model model = new Model("Find DeadLock Solutions: " + i);//Create a model to verify deadlock of a element in the mapElementOutputSequences with ChocoSolver.
			List<BoolVar> sequences = sequenceListToBoolVarList(model,(List<Sequence>) mapElementOutputSequences.values().toArray()[i]); //Get a list of BoolVar from a list of sequence of the mapElementOutputSequences.
			if (sequences!=null && !sequences.isEmpty()) {
				if(model.getCstrs().length != 0 || sequences.size() > 1){
					//Go through all the BoolVars in the list sequences.
					for(int k = 0; k < sequences.size(); k++) {
						model.arithm(sequences.get(k), "=", 0).post();//Post the constraint "the sequence i have to be false" to the model.
					}
					Solution solution = model.getSolver().findSolution();
					double nbVars = (double) model.getNbVars();
					mapStatistics.put("Variables", mapStatistics.containsKey("Variables")?mapStatistics.get("Variables")+nbVars:nbVars);
					double nbCstrs = (double) model.getNbCstrs();
					mapStatistics.put("Constraints", mapStatistics.containsKey("Constraints")?mapStatistics.get("Constraints")+nbCstrs:nbCstrs);
					double timeCount = (double) model.getSolver().getTimeCount();
					mapStatistics.put("ResolutionTime", mapStatistics.containsKey("ResolutionTime")?mapStatistics.get("ResolutionTime")+timeCount:timeCount);
					double nodeCount = (double) model.getSolver().getNodeCount();
					mapStatistics.put("Nodes", mapStatistics.containsKey("Nodes")?mapStatistics.get("Nodes")+nodeCount:nodeCount);
					double backTrackCount = (double) model.getSolver().getBackTrackCount();
					mapStatistics.put("Backtracks", mapStatistics.containsKey("Backtracks")?mapStatistics.get("Backtracks")+backTrackCount:backTrackCount);
					double failCount = (double) model.getSolver().getFailCount();
					mapStatistics.put("Fails", mapStatistics.containsKey("Fails")?mapStatistics.get("Fails")+failCount:failCount);
					double restartCount = (double) model.getSolver().getRestartCount();
					mapStatistics.put("Restarts", mapStatistics.containsKey("Restarts")?mapStatistics.get("Restarts")+restartCount:restartCount);
					double nbSolutions = (double) model.getSolver().getSolutionCount();
					mapStatistics.put("Solutions", mapStatistics.containsKey("Solutions")?mapStatistics.get("Solutions")+nbSolutions:nbSolutions);
					if(nodeCount > 0) {
						binaryTree++;
					}
				}
			}
		}
		mapStatistics.put("BinaryTrees", (double) binaryTree);
		return mapStatistics;
	}
	
	//Return a map with Elements as a key and a list of some solutions that occur Non Determinism in the element as a value of the key.
	//If the operands have the same values presented in the solution, then Non Determinism occurs.
	//An element has a Non Determinism problem when at least two of its output sequences are true.
	public Map<String, Double> findNonDeterminismSolutions(){
		Map<Element, List<Solution>> mapSolutions = new HashMap<Element, List<Solution>>();
		Map<String, Double> mapStatistics = new HashMap<String, Double>();
		int binaryTree = 0;
		for(int k = 0; k < mapElementOutputSequences.values().size(); k++){
			Model model = new Model("Find Non Determinism Solution: " + k);//Create a model to verify non determinism of a element in the mapElementOutputSequences with ChocoSolver.
			List<BoolVar> sequences = sequenceListToBoolVarList(model,(List<Sequence>) mapElementOutputSequences.values().toArray()[k]);//Get a list of BoolVar from a list of sequence of the mapElementOutputSequences.
			if (sequences!=null && !sequences.isEmpty() && sequences.size()>1) {
				//Go through all the BoolVars in the list "sequences" to find the first list of solution that there are non determinism problem.
				for (int i = 0; i < sequences.size(); i++) {
					for(int j = i+1; j < sequences.size(); j++) {
						model.arithm(sequences.get(i), "+", sequences.get(j), "=", 2 ).post(); //Post the constraint "sequence i and sequence j have to be true" to the model.
						Solution solution = model.getSolver().findSolution();
						double nbVars = (double) model.getNbVars();
						mapStatistics.put("Variables", mapStatistics.containsKey("Variables")?mapStatistics.get("Variables")+nbVars:nbVars);
						double nbCstrs = (double) model.getNbCstrs();
						mapStatistics.put("Constraints", mapStatistics.containsKey("Constraints")?mapStatistics.get("Constraints")+nbCstrs:nbCstrs);
						double timeCount = (double) model.getSolver().getTimeCount();
						mapStatistics.put("ResolutionTime", mapStatistics.containsKey("ResolutionTime")?mapStatistics.get("ResolutionTime")+timeCount:timeCount);
						double nodeCount = (double) model.getSolver().getNodeCount();
						mapStatistics.put("Nodes", mapStatistics.containsKey("Nodes")?mapStatistics.get("Nodes")+nodeCount:nodeCount);
						double backTrackCount = (double) model.getSolver().getBackTrackCount();
						mapStatistics.put("Backtracks", mapStatistics.containsKey("Backtracks")?mapStatistics.get("Backtracks")+backTrackCount:backTrackCount);
						double failCount = (double) model.getSolver().getFailCount();
						mapStatistics.put("Fails", mapStatistics.containsKey("Fails")?mapStatistics.get("Fails")+failCount:failCount);
						double restartCount = (double) model.getSolver().getRestartCount();
						mapStatistics.put("Restarts", mapStatistics.containsKey("Restarts")?mapStatistics.get("Restarts")+restartCount:restartCount);
						double nbSolutions = (double) model.getSolver().getSolutionCount();
						mapStatistics.put("Solutions", mapStatistics.containsKey("Solutions")?mapStatistics.get("Solutions")+nbSolutions:nbSolutions);
						System.out.println(nodeCount);
						System.out.println(failCount);
						System.out.println(backTrackCount);
						System.out.println(mapElementOutputSequences.values().toArray()[k]);
						if(nodeCount > 0) {
							binaryTree++;
						}
						if(solution != null) {
							List<Solution> solutions = new ArrayList<Solution>();
							solutions.add(solution);
							mapSolutions.put((Element) mapElementOutputSequences.keySet().toArray()[k], solutions);//Put the Element and the solutions get from model in the mapSolutions.
						}
						model.getSolver().reset();//reset the solver for a new interaction
						model.unpost(model.getCstrs()[model.getCstrs().length-1]);//unpost the last constraint.
					}
					//If mapSolutions has already a list of solutions for the Element, then it returns the list.
					if(mapSolutions.get((Element) mapElementOutputSequences.keySet().toArray()[k]) != null && !mapSolutions.get((Element) mapElementOutputSequences.keySet().toArray()[k]).isEmpty()) {
						break;
					}
				}
			}
		}
		mapStatistics.put("BinaryTrees", (double) binaryTree);
		return mapStatistics;
	}
	
	//Return a map with Elements as a key and a list of all valid solutions in the element as a value of the key.
	//An element has no problem when one, and only one, of its output sequences are true.
	public Map<Element, List<Solution>> findAllValidSolutions() {
		Map<Element, List<Solution>> mapSolutions = new HashMap<Element, List<Solution>>();
		for(int i = 0; i < mapElementOutputSequences.values().size(); i++){
			Model model = new Model("Find All Solutions: " + i); //Create a model to verify the valid solutions of a element in the mapElementOutputSequences with ChocoSolver.
			List<BoolVar> sequences = sequenceListToBoolVarList(model,(List<Sequence>) mapElementOutputSequences.values().toArray()[i]);//Get a list of BoolVar from a list of sequence of the mapElementOutputSequences.
			if (sequences!=null && !sequences.isEmpty()) {
				//Verify if there is only one boolvar in the list.
				if(sequences.size()==1) {
					if(model.getCstrs().length != 0){
						model.arithm(sequences.get(0), "=", 1).post();//Post the constraint "the sequence have to be true" to the model.
						mapSolutions.put((Element) mapElementOutputSequences.keySet().toArray()[i], model.getSolver().findAllSolutions());
					}
				}
				else {
					List<BoolVar> auxSeq = new ArrayList<BoolVar>();//Auxiliary list of boolvar to store the new boolvar variables.
					if(sequences.size() == 1) {
						model.arithm(sequences.get(0), "=", 1).post();
					}else if(sequences.size() == 2) {
						model.arithm(sequences.get(0), "+", sequences.get(1), "=", 1).post();
					}else {
						for(int k = 1; k < sequences.size(); k++) {
							if (auxSeq.isEmpty()) {
								model.arithm(sequences.get(k-1), "+", sequences.get(k), "<", 2).post();//Post the constraint "sequence k-1 and sequence k can't be both true" to the model.
								BoolVar aux = model.arithm(sequences.get(k-1), "+", sequences.get(k), "=", 1).reify();//Create a boolvar with the constraint "sequences.get(k-1) xor sequences.get(k)".
								auxSeq.add(aux);
							}else if(k < sequences.size()-1){
								model.arithm(auxSeq.get(auxSeq.size()-1), "+", sequences.get(k), "<", 2).post();//Post the constraint "the last boolvar in the auxSeq and sequence k can't be both true" to the model.
								BoolVar aux = model.arithm(auxSeq.get(auxSeq.size()-1), "+", sequences.get(k), "=", 1).reify();//Create a boolvar with the constraint "the last boolvar in the auxSeq xor sequences.get(k)".
								auxSeq.add(aux);
							//in the last sequence of the list
							}else {
								model.arithm(auxSeq.get(auxSeq.size()-1), "+", sequences.get(k), "=", 1).post();//Post the constraint "the last boolvar in the auxSeq xor sequence k" to the model.
							}
						}
					}
					mapSolutions.put((Element) mapElementOutputSequences.keySet().toArray()[i], model.getSolver().findAllSolutions());//Put the Element and the solutions get from model in the mapSolutions.
				}
			}
		}
		return mapSolutions;
	}
}
