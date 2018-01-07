package parser.ProtocolValidationTest;

import java.util.List;

import org.chocosolver.solver.Solution;

public class App {
    public static void main( String[] args )    {
        String file1 = "Test_Non_Determinism.xmi";
        
        SequenceParser seq = new SequenceParser(file1);
        
        System.out.println("Are there sequences with the same output step on teste.xmi? "+seq.isThereSameOutputStep());
        
        List<Solution> all_s = seq.findAllSolutions();
        System.out.println("Find All Possible Solutions:");
        for (int i = 0; i < all_s.size(); i++) {
        	System.out.println(all_s.get(i));
        }
        
        List<Solution> nd_s = seq.findNonDeterminismSolutions();
        System.out.println("\nFind Non Determinism Solutions:");
        for (int i = 0; i < nd_s.size(); i++) {
        	System.out.println(nd_s.get(i));
        }
        
        List<Solution> dl_s = seq.findDeadLockSolutions();
        System.out.println("\nFind DeadLock Solutions:");
        for (int i = 0; i < dl_s.size(); i++) {
        	System.out.println(dl_s.get(i));
        }
        
    }
}
