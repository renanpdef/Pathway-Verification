package parser.ProtocolValidationTest;

import java.util.List;
import java.util.Map;

import org.chocosolver.solver.Solution;

import protocolosv2.Element;

public class Solutions {
    public static void main( String[] args )    {
        String file1 = "Test_Non_Determinism.xmi";
        
        ProtocolReader protocolReader = new ProtocolReader(file1);
        SequenceParser sequenceParser = new SequenceParser(protocolReader.getProtocol());
        
        System.out.println("Are there sequences with the same output step on teste.xmi? "
        							+ sequenceParser.isThereSameOutputStep());
        
        Map<Element, List<Solution>> mapAllSolutions = sequenceParser.findAllSolutions();
        
        System.out.println("Find All Possible Solutions:");
        for (int k = 0; k < mapAllSolutions.size(); k++) {
        	List<Solution> allSolutions = (List<Solution>) mapAllSolutions.values().toArray()[k];
        	System.out.println("Elemento " + k);
	        for (int i = 0; i < allSolutions.size(); i++) {
	        	String str[] = allSolutions.get(i).toString().split(",");
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
