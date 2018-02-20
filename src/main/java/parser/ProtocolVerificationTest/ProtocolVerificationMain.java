package parser.ProtocolVerificationTest;

import java.util.List;
import java.util.Map;
import org.chocosolver.solver.Solution;
import protocolosv2.Element;
import protocolosv2.Protocol;
import protocolosv2.Sequence;

public class ProtocolVerificationMain {
    
	public static void main( String[] args )    {
        String file1 = "XMIs_Real_Test\\meningite.xmi";
        
        ProtocolReader protocolReader = new ProtocolReader(file1);
        Protocol protocol = protocolReader.createProtocol();
        FindSolutions findSolutions = new FindSolutions(protocol);
        FindInaccessibleStep inaccessibleStep = new FindInaccessibleStep(protocol);
        
        //print all valid solutions for variables in protocol
        printFoundSolutions(findSolutions.findAllValidSolutions(), "FIND ALL VALID SOLUTIONS");
        System.out.println("-------------------------------------------------------------------------");
        
        //print some solutions that occur non determinism problem
        printFoundSolutions(findSolutions.findNonDeterminismSolutions(), "FIND NON DETERMINISM SOLUTIONS");
        System.out.println("-------------------------------------------------------------------------");
        
        //print all solutions that occur deadlock
        printFoundSolutions(findSolutions.findDeadLockSolutions(), "FIND DEADLOCK SOLUTIONS");
        System.out.println("-------------------------------------------------------------------------");
        
        //print logically equivalent sequences
        printEquivalentSequences(findSolutions.findLogicallyEquivalentSequence());
        System.out.println("-------------------------------------------------------------------------");
        
        //Verify if there are Inaccessible Step
        printInaccessibleStep(inaccessibleStep.findInaccessibleSteps());
        System.out.println("-------------------------------------------------------------------------");
    }
    
    public static void printFoundSolutions(Map<Element, List<Solution>> mapSolutions, String solutionsName) {
    	System.out.println("\n" + solutionsName);
    	//Go through all lists of solutions from mapDeadLockSolutions.
        for (int k = 0; k < mapSolutions.size(); k++) {
        	List<Solution> solutionsList = (List<Solution>) mapSolutions.values().toArray()[k];
        	Element step = (Element) mapSolutions.keySet().toArray()[k];//get the output step of the sequences which was analyzed.
        	System.out.println("\n" + step.getClass().getSimpleName() +": "+ step.getName());//print the name of the output step.
        	printSequencesOperations(step);
        	//Go through the list of solutions
        	for (int i = 0; i < solutionsList.size(); i++) {
	        	String str[] = solutionsList.get(i).toString().split(",");
	        	for(int j = 0; j < str.length; j++) {
	        		//print just true operands from the vector str.
	        		//REIF is a constraint that was reify to a BoolVar.
	        		//exp is a result from a mathematical expression.
	        		if(!str[j].contains("REIF") && !str[j].contains("exp")) {
	        			System.out.print(str[j]);
	        		}
	        	}
	        	System.out.println();
	        }
        }
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
