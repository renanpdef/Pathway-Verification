package parser.PathwayVerification;

import java.io.IOException;

public class ResultsTableGeneratorMain {

	public static void main(String[] args) throws IOException {
		ResultsTableGenerator results = new ResultsTableGenerator();
		System.out.println("Deadlock Results: ");
		results.createResultTable("dl");
		System.out.println("Inaccessible Steps Results: ");
		results.createResultTable("is");
		System.out.println("Non-Determinism Results: ");
		results.createResultTable("nd");

	}

}
