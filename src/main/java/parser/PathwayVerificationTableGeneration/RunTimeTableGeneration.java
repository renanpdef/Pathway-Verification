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
	public void createTable() throws IOException {
		FileWriter table = new FileWriter("pathwaysRunTimeTable.txt");
		PrintWriter recordTable = new PrintWriter(table);
		String header = "Pathways, Time (ns)";
		recordTable.println(header);
		for (int i = 0; i < pathwayFiles.length; i++) {
			if(i != 35) {
				String pathwayName = pathwayFiles[i].replaceAll(".xmi", "");
				
				long executionTime = runTimeMedia(i);
				
		        String line = pathwayName + ",=" + executionTime;
		        
		        recordTable.println(line);
		        
		        System.out.println(i + ": " + line);
			}
		}
		recordTable.close();
	}
	
	private long runTimeMedia(int pathwayNumber) {
		List<Long> runTimes = new ArrayList<Long>();
		for(int i = 0; i < 12; i++) {
			long executionTime = runTimeCalculate(pathwayNumber);
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
	private long runTimeCalculate(int pathwayNumber) {
		PathwayReader pathwayReader = new PathwayReader(path + pathwayFiles[pathwayNumber]);
        Pathway pathway = pathwayReader.createPathway();
        Information info = new Information(pathway);
        
		long beginTime = System.nanoTime();
		info.getDeadlockNumber();
		info.getNonDeterminismNumber();
		info.getInaccessibleStepNumber();
        info.getEquivalentTransitionsNumber();
        long executionTime = System.nanoTime() - beginTime;
        return executionTime;
	}
}
