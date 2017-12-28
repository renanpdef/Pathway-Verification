package parser.ProtocolValidationTest;

import java.util.List;

import org.chocosolver.solver.Solution;

public class App {
    public static void main( String[] args )    {
        String file1 = "Test_Non_Determinism.xmi";
        
        SequenceParser seq = new SequenceParser(file1);
        
        System.out.println("Are there sequences with the same output step on teste.xmi? "+seq.isThereSameOutputStep());
        
        List<Solution> solutions = seq.findNonDeterminism();
        for (int i = 0; i < solutions.size(); i++) {
        	System.out.println(solutions.get(i));
        }
        
    }
}
