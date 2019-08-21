package parser.PathwayVerificationTableGeneration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import parser.PathwayVerificationTest.FindInaccessibleStep;
import parser.PathwayVerificationTest.FindSolutions;
import parser.PathwayVerificationTest.PathwayReader;
import pathwayMetamodel.Pathway;

public class StatisticsTableGeneration {
	//Array with every xmi pathways to be verified
	String path = "Pathways/";
	File file = new File(path);
	String[] pathwayFiles =  file.list();

	
	//Method to create a table in a file txt with lots of information about the pathways
	public void createStatisticTable(String function) throws IOException {
		FileWriter table = new FileWriter(function + "StatisticTable.txt");
		PrintWriter recordTable = new PrintWriter(table);
		String header = "Pathway, Resolution Time, Variables, Constraints, BinaryTrees, Nodes, Backtracks, Fails, Restarts, Solutions";
		recordTable.println(header);
		for (int i = 0; i < pathwayFiles.length; i++) {
			PathwayReader pathwayReader = new PathwayReader(path + pathwayFiles[i]);
	        Pathway pathway = pathwayReader.createPathway();
	        FindSolutions findSolutions = new FindSolutions(pathway);
	        FindInaccessibleStep inaccessibleStep = new FindInaccessibleStep(pathway);
	        
			String pathwayName = pathwayFiles[i].replaceAll(".xmi", "");
			pathwayName = pathwayName.replaceAll(" - ", "_");
			System.out.println(i + pathwayName);
			ArrayList<Integer> problems = new ArrayList<Integer>(){{add(28);}}; //set with defective pathways
			if(!problems.contains(i)) {
				Map<String, Double> mapStatistics = new HashMap<String, Double>();
				if(function.equalsIgnoreCase("dl")) {
					mapStatistics = findSolutions.findDeadLockSolutions();
				}else if(function.equalsIgnoreCase("nd")) {
					mapStatistics = findSolutions.findNonDeterminismSolutions();
				}else if(function.equalsIgnoreCase("et")){
					mapStatistics = findSolutions.findLogicallyEquivalentSequence();
				}else {
					mapStatistics = inaccessibleStep.findInaccessibleSteps();
				}
			
		        String line = pathwayName + ",=" + mapStatistics.get("ResolutionTime") + ",=" + mapStatistics.get("Variables") + ",=" + mapStatistics.get("Constraints") + ",=" + mapStatistics.get("BinaryTrees") + ",=" + mapStatistics.get("Nodes") + ",=" + mapStatistics.get("Backtracks") + ",=" + mapStatistics.get("Fails") + ",=" + mapStatistics.get("Restarts") + ",=" + mapStatistics.get("Solutions");
		        
		        recordTable.println(line);
		        
		        System.out.println(pathwayName + " gravado! " + i);
			}else {
				System.out.println(pathwayName + " COM PROBLEMA!!! " + i);
			}
	        
		}
		recordTable.close();
	}
}
