package parser.PathwayVerificationTableGeneration;
import java.io.IOException;

import parser.PathwayVerificationTest.PathwayReader;
import pathwayMetamodel.Pathway;


public class InformationsMain {

	public static void main(String[] args) throws IOException, InterruptedException {
		
//		TableGeneration tableGeneration = new TableGeneration();
//		tableGeneration.createTable();
		
//		OperationTableGaneration operationTableGeneration = new OperationTableGaneration();
//		operationTableGeneration.createTable();
		
//		CompleteTableGeneration completeTableGaneration = new CompleteTableGeneration();
//		completeTableGaneration.createTable();
		
//		RunTimeTableGeneration runTimeTableGaneration = new RunTimeTableGeneration();
//		runTimeTableGaneration.createTable();
		
		StatisticsTableGeneration statisticsTableGeneration = new StatisticsTableGeneration();
		System.out.println("DEADLOCK");
		statisticsTableGeneration.createStatisticTable("dl");
		System.out.println("NON DETERMINISM");
		statisticsTableGeneration.createStatisticTable("nd");
		System.out.println("EQUIVALENT TRANSITIONS");
		statisticsTableGeneration.createStatisticTable("et");
		System.out.println("INACCESSIBLE STEPS");
		statisticsTableGeneration.createStatisticTable("is");
	}
}
