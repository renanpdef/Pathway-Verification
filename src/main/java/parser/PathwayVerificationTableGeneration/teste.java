package parser.PathwayVerificationTableGeneration;

import java.io.File;

import parser.PathwayVerificationTest.PathwayReader;
import pathwayMetamodel.Pathway;

public class teste {

	public static void main(String[] args) {
		
		String path = "Pathways/";
		File file = new File(path);
		String[] pathwayFiles =  file.list();
		
		int i = 53;
		PathwayReader pathwayReader = new PathwayReader(path + pathwayFiles[i]);
	    Pathway pathway = pathwayReader.createPathway();
	    Information info = new Information(pathway); //object with functions to extract information about the pathways
	    String pathwayName = pathwayFiles[i].replaceAll(".xmi", "");
	    System.out.println(i + pathwayName);
	    System.out.println(info.getStatesNumber());
	    int deadlock = info.getDeadlockNumber();
	    System.out.println(deadlock);

	}

}
