package parser.PathwayVerificationTest;

import java.io.File;
import java.util.List;
import java.util.Map;
import org.chocosolver.solver.Solution;
import pathwayMetamodel.Element;
import pathwayMetamodel.Pathway;
import pathwayMetamodel.Sequence;

public class PathwayVerificationMain {
    
	public static void main( String[] args )    {
		String path = "Pathways/";
		File file = new File(path);
		String[] pathwayFiles =  file.list();
		String pathwayFile = path + pathwayFiles[0];
        
        PathwayReader pathwayReader = new PathwayReader(pathwayFile);
        Pathway pathway = pathwayReader.createPathway();
        FindSolutions findSolutions = new FindSolutions(pathway);
        FindInaccessibleStep inaccessibleStep = new FindInaccessibleStep(pathway);
        
//        //print all valid solutions for variables in pathway
//        printFoundSolutions(findSolutions.findAllValidSolutions(), "FIND ALL VALID SOLUTIONS");
//        System.out.println("-------------------------------------------------------------------------");
////      
        
        //print all solutions that occur deadlock
        printStatistics(findSolutions.findDeadLockSolutions(), "FIND DEADLOCK SOLUTIONS");
        System.out.println("-------------------------------------------------------------------------");

        //print some solutions that occur non determinism problem
        printStatistics(findSolutions.findNonDeterminismSolutions(), "FIND NON DETERMINISM SOLUTIONS");
        System.out.println("-------------------------------------------------------------------------");
  
        //print logically equivalent sequences
        printStatistics(findSolutions.findLogicallyEquivalentSequence(), "LOGICALLY EQUIVALENT SEQUENCES");
        System.out.println("-------------------------------------------------------------------------");
//        
//        //Verify if there are Inaccessible Step
        printStatistics(inaccessibleStep.findInaccessibleSteps(), "INACCESSIBLE STEPS");
        System.out.println("-------------------------------------------------------------------------");        
    }
    
    public static void printStatistics(Map<String, Double> mapStatistics, String solutionsName) {
    	System.out.println("\n" + solutionsName);
    	//Go through all lists of solutions from mapSolutions.
        System.out.println("Variables: " + mapStatistics.get("Variables"));
        System.out.println("Constraints: " + mapStatistics.get("Constraints"));
        System.out.println("ResolutionTime: " + mapStatistics.get("ResolutionTime"));
        System.out.println("Nodes: " + mapStatistics.get("Nodes"));
        System.out.println("BinaryTrees: " + mapStatistics.get("BinaryTrees"));
        System.out.println("Backtracks: " + mapStatistics.get("Backtracks"));
        System.out.println("Fails: " + mapStatistics.get("Fails"));
        System.out.println("Restarts: " + mapStatistics.get("Restarts"));
        System.out.println("Solutions: " + mapStatistics.get("Solutions"));
    }
    
    public static void printEquivalentSequences(Map<Element, List<Sequence>> mapEquivalentSequences) {
    	System.out.println("\nLOGICALLY EQUIVALENT SEQUENCES");
    	//Go through all lists of solutions from mapDeadLockSolutions.
        for (int k = 0; k < mapEquivalentSequences.size(); k++) {
        	List<Sequence> sequenceList = (List<Sequence>) mapEquivalentSequences.values().toArray()[k];
        	Element step = (Element) mapEquivalentSequences.keySet().toArray()[k];//get the output step of the sequences which was analyzed.
        	System.out.println("\n" + step.getClass().getSimpleName() +": "+ step.getName());//print the name of the output step.
        	for(int i = 0; i < sequenceList.size(); i++) {
        		System.out.println(sequenceList.get(i).getName() + ": " + sequenceList.get(i).getOperation().getName());
        	}
        	System.out.println();
        }
    }
    
    public static void printInaccessibleStep(List<Element> elements) {
    	System.out.println("\nINACCESSIBLE STEPS");
    	for(int k = 0; k < elements.size(); k++) {
    		System.out.println("\n" + elements.get(k).getClass().getSimpleName() +": "+ elements.get(k).getName());//print the name of the Element.
        }
    }
    
    public static void printSequencesOperations(Element element) {
    	for(int i = 0; i < element.getOutputSequences().size(); i ++) {
    		if(element.getOutputSequences().get(i).getOperation() != null) {
    			System.out.println(element.getOutputSequences().get(i).getName()+ ": " + element.getOutputSequences().get(i).getOperation().getName());
    		}
    	}
    }
}
