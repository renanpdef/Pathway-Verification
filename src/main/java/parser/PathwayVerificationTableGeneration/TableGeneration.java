package parser.PathwayVerificationTableGeneration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import parser.PathwayVerificationTest.PathwayReader;
import pathwayMetamodel.Pathway;

public class TableGeneration {
	//Array with every xmi pathways to be verified
	String path = "Pathways/";
	File file = new File(path);
	String[] pathwayFiles =  file.list();
	
	//Method to create a table in a file txt with lots of information about the pathways
	public void createTable() throws IOException {
		FileWriter table = new FileWriter("pathwaysTable.txt");
		PrintWriter recordTable = new PrintWriter(table);
		String header = "Pathway, States, Transitions, Path, Deadlock, Nondeterminism, Inaccessible States, Equivalent Transitions, Time (ms)";
		recordTable.println(header);
		for (int i = 0; i < pathwayFiles.length; i++) {
			PathwayReader pathwayReader = new PathwayReader(path + pathwayFiles[i]);
	        Pathway pathway = pathwayReader.createPathway();
	        Information info = new Information(pathway); //object with functions to extract information about the pathways
	   
			String pathwayName = pathwayFiles[i].replaceAll(".xmi", "");
			System.out.println(i + pathwayName);
			ArrayList<Integer> problems = new ArrayList<Integer>(){{add(35);}}; //set with defective pathways
			if(!problems.contains(i)) {
				long beginTime = System.currentTimeMillis();
				int deadlock = info.getDeadlockNumber();
				int nonDeterminism = info.getNonDeterminismNumber();
				int inaccessible = info.getInaccessibleStepNumber();
		        int equivalentTransition = info.getEquivalentTransitionsNumber();
		        long executionTime = System.currentTimeMillis() - beginTime;
		        int states = info.getStatesNumber();
		        int transitions = info.getTransitionsNumber();
		        int paths = info.pathNumber();
		   
		        String line = pathwayName + ",=" + states + ",=" + transitions + ",=" + paths + ",=" + deadlock + ",=" + nonDeterminism + ",=" + inaccessible + ",=" + equivalentTransition + ",=" + executionTime;
		        
		        recordTable.println(line);
		        
		        System.out.println(pathwayName + " gravado! " + i);
			}else {
				System.out.println(pathwayName + " COM PROBLEMA!!! " + i);
			}
	        
		}
		recordTable.close();
	}
}
