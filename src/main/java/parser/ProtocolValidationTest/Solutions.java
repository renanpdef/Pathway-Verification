package parser.ProtocolValidationTest;

import java.util.List;
import java.util.Map;

import org.chocosolver.solver.Solution;

import protocolosv2.Element;

public class Solutions {
    public static void main( String[] args )    {
        String file1 = "XMIs_Test\\Test_02.xmi";
        
        ProtocolReader protocolReader = new ProtocolReader(file1);
        SequenceParser sequenceParser = new SequenceParser(protocolReader.createProtocol());
               
        Map<Element, List<Solution>> mapAllValidSolutions = sequenceParser.findAllValidSolutions();
        System.out.println("FIND ALL VALID SOLUTIONS:");
        for (int k = 0; k < mapAllValidSolutions.size(); k++) {
        	List<Solution> allValidSolutions = (List<Solution>) mapAllValidSolutions.values().toArray()[k];
        	Element step = (Element) mapAllValidSolutions.keySet().toArray()[k];
        	System.out.println("\n" + step.getClass().getSimpleName() +": "+ step.getName());
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
        System.out.println("\nFIND NON DETERMINISM SOLUTIONS");
        for (int k = 0; k < mapNonDeterminismSolutions.size(); k++) {
        	List<Solution> nonDeterminismSolutions = (List<Solution>) mapNonDeterminismSolutions.values().toArray()[k];
        	Element step = (Element) mapNonDeterminismSolutions.keySet().toArray()[k];
        	System.out.println("\n" + step.getClass().getSimpleName() +": "+ step.getName());
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
        System.out.println("\nFIND DEADLOCK SOLUTIONS");
        for (int k = 0; k < mapDeadLockSolutions.size(); k++) {
        	List<Solution> deadLockSolutions = (List<Solution>) mapDeadLockSolutions.values().toArray()[k];
        	Element step = (Element) mapDeadLockSolutions.keySet().toArray()[k];
        	System.out.println("\n" + step.getClass().getSimpleName() +": "+ step.getName());
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
