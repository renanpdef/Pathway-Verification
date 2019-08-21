package parser.PathwayVerificationTableGeneration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import parser.PathwayVerificationTest.PathwayReader;
import pathwayMetamodel.Pathway;

public class RunTimeTableGeneration {
	//Array with every xmi pathways to be verified
	String path = "Pathways/";
	File file = new File(path);
	String[] pathwayFiles =  file.list();
	
	//Method to create a table in a file txt with lots of information about the pathways
	public void createTable(String function) throws IOException {
		FileWriter table = new FileWriter(function + "PathwaysRunTimeTable.txt");
		PrintWriter recordTable = new PrintWriter(table);
		String header = "Pathways, Time (ms)";
		recordTable.println(header);
		for (int i = 0; i < pathwayFiles.length; i++) {
			if(i != 28) {
				String pathwayName = pathwayFiles[i].replaceAll(".xmi", "");
				pathwayName = pathwayName.replaceAll(" - ", "_");
				
				long executionTime = runTimeMedia(i, function);
				
		        String line = pathwayName + ",=" + executionTime/1000000.0;
		        
		        recordTable.println(line);
		        
		        System.out.println(i + ": " + line);
			}
		}
		recordTable.close();
	}
	
	private long runTimeMedia(int pathwayNumber, String function) {
		List<Long> runTimes = new ArrayList<Long>();
		for(int i = 0; i < 12; i++) {
			long executionTime = runTimeCalculate(pathwayNumber, function);
			runTimes.add(executionTime);
		}
		runTimes.sort(null);
		long executionTimeSum = 0;
		for (int j = 1; j < runTimes.size()-1; j++) {
			executionTimeSum += runTimes.get(j);
		}
		return executionTimeSum/10;
	}
	
	//Method to create a table in a file txt with lots of information about the pathways
	private long runTimeCalculate(int pathwayNumber, String function) {
		PathwayReader pathwayReader = new PathwayReader(path + pathwayFiles[pathwayNumber]);
        Pathway pathway = pathwayReader.createPathway();
        Information info = new Information(pathway);
        
		long beginTime = System.nanoTime();
		if(function.equalsIgnoreCase("dl")) {
			info.getDeadlockNumber();
		}else if(function.equalsIgnoreCase("nd")) {
			info.getNonDeterminismNumber();
		}else if(function.equalsIgnoreCase("is")) {
			info.getInaccessibleStepNumber();
		}else {
			info.getEquivalentTransitionsNumber();
		}
        long executionTime = System.nanoTime() - beginTime;
        return executionTime;
	}
}
