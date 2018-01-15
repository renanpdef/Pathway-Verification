package parser.ProtocolValidationTest;

import java.util.List;
import java.util.Map;

import org.chocosolver.solver.Solution;

import protocolosv2.Element;

public class Solutions {
    public static void main( String[] args )    {
        String file1 = "Test_Non_Determinism.xmi";
        
        ProtocolReader protocolReader = new ProtocolReader(file1);
        SequenceParser sequenceParser = new SequenceParser(protocolReader.createProtocol());
               
        Map<Element, List<Solution>> mapAllValidSolutions = sequenceParser.findAllValidSolutions();
        System.out.println("Find All Valid Solutions:");
        for (int k = 0; k < mapAllValidSolutions.size(); k++) {
        	List<Solution> allValidSolutions = (List<Solution>) mapAllValidSolutions.values().toArray()[k];
        	System.out.println("Elemento " + k);
	        for (int i = 0; i < allValidSolutions.size(); i++) {
	        	String str[] = allValidSolutions.get(i).toString().split(",");
	        	for(int j = 0; j < str.length; j++) {
	        		if(!str[j].contains("REIF")) {
	        			System.out.print(str[j]);
	        		}
	        	}
	        	System.out.println();
	        }
        }
        
        Map<Element, List<Solution>> mapNonDeterminismSolutions = sequenceParser.findNonDeterminismSolutions();
        System.out.println("\nFind Non Determinism Solutions:");
        for (int k = 0; k < mapNonDeterminismSolutions.size(); k++) {
        	List<Solution> nonDeterminismSolutions = (List<Solution>) mapNonDeterminismSolutions.values().toArray()[k];
        	System.out.println("Elemento " + k);
	        for (int i = 0; i < nonDeterminismSolutions.size(); i++) {
	        	String str[] = nonDeterminismSolutions.get(i).toString().split(",");
	        	for(int j = 0; j < str.length; j++) {
	        		if(!str[j].contains("REIF")) {
	        			System.out.print(str[j]);
	        		}
	        	}
	        	System.out.println();
	        }
        }
        
        Map<Element, List<Solution>> mapDeadLockSolutions = sequenceParser.findDeadLockSolutions();
        System.out.println("\nFind DeadLock Solutions:");
        for (int k = 0; k < mapDeadLockSolutions.size(); k++) {
        	List<Solution> deadLockSolutions = (List<Solution>) mapDeadLockSolutions.values().toArray()[k];
        	System.out.println("Elemento " + k);
	        for (int i = 0; i < deadLockSolutions.size(); i++) {
	        	String str[] = deadLockSolutions.get(i).toString().split(",");
	        	for(int j = 0; j < str.length; j++) {
	        		if(!str[j].contains("REIF")) {
	        			System.out.print(str[j]);
	        		}
	        	}
	        	System.out.println();
	        }
        }
    }
}
