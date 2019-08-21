package parser.PathwayVerification;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.chocosolver.solver.Solution;
import pathwayMetamodel.Element;
import pathwayMetamodel.Pathway;

public class ResultsTableGenerator {
    
	//Array with every xmi pathways to be verified
	String path = "Pathways/";
	File file = new File(path);
	String[] pathwayFiles =  file.list();
	
	//Method to create a table in a file txt with lots of information about the pathways
	public void createResultTable(String function) throws IOException {
		FileWriter table = new FileWriter(function + "ResultTable.txt");
		PrintWriter recordTable = new PrintWriter(table);
		String header = "Pathways; Result";
		recordTable.println(header);
		for (int i = 0; i < pathwayFiles.length; i++) {
			if(i != 28) {
				String pathwayName = pathwayFiles[i].replaceAll(".xmi", "");
				pathwayName = pathwayName.replaceAll(" - ", "_");
				
				PathwayReader pathwayReader = new PathwayReader(path + pathwayFiles[i]);
		        Pathway pathway = pathwayReader.createPathway();
		        FindSolutions findSolutions = new FindSolutions(pathway);
		        FindInaccessibleStep inaccessibleStep = new FindInaccessibleStep(pathway);
		        String result = "";
				if(function.equalsIgnoreCase("dl")) {
					result += resultDlNDSolutions(findSolutions.findDeadLockSolutions());
				}else if(function.equalsIgnoreCase("nd")) {
					result += resultDlNDSolutions(findSolutions.findNonDeterminismSolutions());
				}else if(function.equalsIgnoreCase("is")) {
					result += resultInaccessibleStep(inaccessibleStep.findInaccessibleSteps());
				}else {
					//No occurrence
					findSolutions.findLogicallyEquivalentSequence();
				}
								
		        String line = pathwayName + ";" + result;
		        
		        recordTable.println(line);
		        
		        System.out.println(i + ": " + line);
			}
		}
		recordTable.close();
	}
	
    private Map<String, List<String>> resultDlNDSolutions(Map<Element, List<Solution>> mapSolutions) {
    	Map<String, List<String>> result = new HashMap<String, List<String>>();
    	//Go through all lists of solutions from mapSolutions.
        for (int k = 0; k < mapSolutions.size(); k++) {
        	String state = "";
        	List<String> operands = new ArrayList<String>();
        	List<Solution> solutionsList = (List<Solution>) mapSolutions.values().toArray()[k];
        	Element step = (Element) mapSolutions.keySet().toArray()[k];//get the output step of the sequences which was analyzed.
        	if(step != null) {
	        	state += step.getType() +": "+ step.getName();
        	}else {
        		state += step;
        	}
        	//printSequencesOperations(step);
        	//Go through the list of solutions
        	for (int i = 0; i < solutionsList.size(); i++) {
	        	String str[] = solutionsList.get(i).toString().split(",");
	        	for(int j = 0; j < str.length; j++) {
	        		//print just true operands from the vector str.
	        		//REIF is a constraint that was reify to a BoolVar.
	        		//exp is a result from a mathematical expression.
	        		if(!str[j].contains("REIF") && !str[j].contains("exp") && !str[j].equals(" ")) {
	        			operands.add(str[j].replaceAll("Solution: ", ""));
	        		}
	        	}
	        }
        	result.put(state, operands);
        }
        return result;
    }
        
    private List<String> resultInaccessibleStep(List<Element> elements) {
    	List<String> states = new ArrayList<String>();
    	for(int k = 0; k < elements.size(); k++) {
    		states.add(elements.get(k).getType() +": "+ elements.get(k).getName());//print the name of the Element.
    	}
    	return states;		
    }
}
