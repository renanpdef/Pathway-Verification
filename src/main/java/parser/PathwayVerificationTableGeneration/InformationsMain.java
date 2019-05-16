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
		statisticsTableGeneration.createStatisticTable("dl");
		statisticsTableGeneration.createStatisticTable("nd");
		statisticsTableGeneration.createStatisticTable("et");
		statisticsTableGeneration.createStatisticTable("is");
	}
}
