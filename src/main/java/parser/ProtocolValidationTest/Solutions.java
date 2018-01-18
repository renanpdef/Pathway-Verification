package parser.ProtocolValidationTest;

import java.util.List;
import java.util.Map;

import org.chocosolver.solver.Solution;

import protocolosv2.Element;

public class Solutions {
    public static void main( String[] args )    {
        String file1 = "XMIs_Test\\Test_05.xmi";
        
        ProtocolReader protocolReader = new ProtocolReader(file1);
        SequenceParser sequenceParser = new SequenceParser(protocolReader.createProtocol());
        
        //Print all valid solutions.
        //parsing the return of the function findAllValidSolutions from sequenceParser.
        Map<Element, List<Solution>> mapAllValidSolutions = sequenceParser.findAllValidSolutions();
        System.out.println("FIND ALL VALID SOLUTIONS:");
        //Go through all lists of solutions from mapAllValidSolutions
        for (int k = 0; k < mapAllValidSolutions.size(); k++) {
        	List<Solution> allValidSolutions = (List<Solution>) mapAllValidSolutions.values().toArray()[k];
        	Element step = (Element) mapAllValidSolutions.keySet().toArray()[k]; //get the output step of the sequences which was analyzed. 
        	System.out.println("\n" + step.getClass().getSimpleName() +": "+ step.getName());//print the name of the output step.
	        //Go through the list of solutions
        	for (int i = 0; i < allValidSolutions.size(); i++) {
	        	String str[] = allValidSolutions.get(i).toString().split(",");
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
        
        //Print all solutions that occurs a non determinism problem.
        //parsing the return of the function findNonDeterminismSolutions from sequenceParser.
        Map<Element, List<Solution>> mapNonDeterminismSolutions = sequenceParser.findNonDeterminismSolutions();
        System.out.println("\nFIND NON DETERMINISM SOLUTIONS");
        //Go through all lists of solutions from mapNonDeterminismSolutions
        for (int k = 0; k < mapNonDeterminismSolutions.size(); k++) {
        	List<Solution> nonDeterminismSolutions = (List<Solution>) mapNonDeterminismSolutions.values().toArray()[k];
        	Element step = (Element) mapNonDeterminismSolutions.keySet().toArray()[k];//get the output step of the sequences which was analyzed.
        	System.out.println("\n" + step.getClass().getSimpleName() +": "+ step.getName());//print the name of the output step.
	        //Go through the list of solutions
	        for (int i = 0; i < nonDeterminismSolutions.size(); i++) {
	        	String str[] = nonDeterminismSolutions.get(i).toString().split(",");
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
        
        //Print all solutions that occurs a Deadlock problem.
        //parsing the return of the function findDeadLockSolutions from sequenceParser.
        Map<Element, List<Solution>> mapDeadLockSolutions = sequenceParser.findDeadLockSolutions();
        System.out.println("\nFIND DEADLOCK SOLUTIONS");
        //Go through all lists of solutions from mapDeadLockSolutions.
        for (int k = 0; k < mapDeadLockSolutions.size(); k++) {
        	List<Solution> deadLockSolutions = (List<Solution>) mapDeadLockSolutions.values().toArray()[k];
        	Element step = (Element) mapDeadLockSolutions.keySet().toArray()[k];//get the output step of the sequences which was analyzed.
        	System.out.println("\n" + step.getClass().getSimpleName() +": "+ step.getName());//print the name of the output step.
        	//Go through the list of solutions
        	for (int i = 0; i < deadLockSolutions.size(); i++) {
	        	String str[] = deadLockSolutions.get(i).toString().split(",");
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
}
