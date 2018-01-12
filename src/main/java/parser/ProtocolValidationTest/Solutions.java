package parser.ProtocolValidationTest;

import java.util.List;

import org.chocosolver.solver.Solution;

public class Solutions {
    public static void main( String[] args )    {
        String file1 = "Test_Non_Determinism.xmi";
        
        ProtocolReader protocolReader = new ProtocolReader(file1);
        SequenceParser sequenceParser = new SequenceParser();
        sequenceParser.setProtocol(protocolReader.getProtocol());
        
        System.out.println("Are there sequences with the same output step on teste.xmi? "
        							+ sequenceParser.isThereSameOutputStep());
        
        List<Solution> allSolutions = sequenceParser.findAllSolutions();
        
        System.out.println("Find All Possible Solutions:");
        for (int i = 0; i < allSolutions.size(); i++) {
        	System.out.println(allSolutions.get(i));
        }
        
        List<Solution> nonDeterminismSolutions = sequenceParser.findNonDeterminismSolutions();
        System.out.println("\nFind Non Determinism Solutions:");
        for (int i = 0; i < nonDeterminismSolutions.size(); i++) {
        	System.out.println(nonDeterminismSolutions.get(i));
        }
        
        List<Solution> deadLockSolutions = sequenceParser.findDeadLockSolutions();
        System.out.println("\nFind DeadLock Solutions:");
        for (int i = 0; i < deadLockSolutions.size(); i++) {
        	System.out.println(deadLockSolutions.get(i));
        }
        
    }
}
