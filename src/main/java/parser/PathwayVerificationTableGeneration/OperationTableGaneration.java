package parser.PathwayVerificationTableGeneration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import parser.PathwayVerificationTest.PathwayReader;
import pathwayMetamodel.Pathway;

public class OperationTableGaneration {
	//Array with every xmi pathways to be verified
	String path = "Pathways/";
	File file = new File(path);
	String[] pathwayFiles =  file.list();
	
	//Method to create a table in a file txt with lots of information about the pathways
	public void createTable() throws IOException {
		FileWriter table = new FileWriter("pathwaysOperationTable.txt");
		PrintWriter recordTable = new PrintWriter(table);
		String header = "Pathway, Operation, Real Operation, Operands, Numeric, YesOrNo, Choice, Options, Addition, Real Addition, Subtraction, Real Subtraction, Multiplication, Real Multiplication, Division, Real Division, And, Real And, Or, Real Or, Xor, Real Xor, Implies, Real Implies, Affirmation, Real, Not Affirmation, Real Not, Equal, Real Equal, Greater, Real Greater, Greater or Equal, Real Greater or Equal, Less, Real Less, Less or Equal, Real Less or Equal";
		recordTable.println(header);
		for (int i = 0; i < pathwayFiles.length; i++) {
			PathwayReader pathwayReader = new PathwayReader(path + pathwayFiles[i]);
	        Pathway pathway = pathwayReader.createPathway();
	        OperationInformation opInfo = new OperationInformation(pathway); //object with functions to extract information about the pathways
	   
			String pathwayName = pathwayFiles[i].replaceAll(".xmi", "");
			System.out.println(i + pathwayName);
			ArrayList<Integer> problems = new ArrayList<Integer>(){{add(35);}}; //set with defective pathways
			if(!problems.contains(i)) {
				int operation = opInfo.getOperationsNumber();
				int realOperation = opInfo.getRealOperationNumber();
				int operands = opInfo.getOperandsNumber();
				int numeric = opInfo.getNumericOperandsNumber();
				int yesOrNo = opInfo.getYesOrNoOperandsNumber();
				int choice = opInfo.getChoiceOperandsNumber();
				int options = opInfo.getChoiceOptionsNumber();
				int addition = opInfo.getAdditionOperatorNumber();
				int subtraction = opInfo.getSubtractionOperatorNumber();
				int multiplication = opInfo.getMultiplicationOperatorNumber();
				int division = opInfo.getDivisionOperatorNumber();
				int and = opInfo.getAndOperatorNumber();
				int or = opInfo.getOrOperatorNumber();
				int xor = opInfo.getXorOperatorNumber();
				int implies = opInfo.getImpliesOperatorNumber();
				int affirmation = opInfo.getAffirmationOperatorNumber();
				int not = opInfo.getNotOperatorNumber();
				int equal = opInfo.getEqualOperatorNumber();
				int greater = opInfo.getGreaterOperatorNumber();
				int greaterOrEqual = opInfo.getGreaterEqualOperatorNumber();
				int less = opInfo.getLessOperatorNumber();
				int lessOrEqual = opInfo.getLessEqualOperatorNumber();
				int realAddition = opInfo.getRealAdditionOperatorNumber();
				int realSubtraction = opInfo.getRealSubtractionOperatorNumber();
				int realMultiplication = opInfo.getRealMultiplicationOperatorNumber();
				int realDivision = opInfo.getRealDivisionOperatorNumber();
				int realAnd = opInfo.getRealAndOperatorNumber();
				int realOr = opInfo.getRealOrOperatorNumber();
				int realXor = opInfo.getRealXorOperatorNumber();
				int realImplies = opInfo.getRealImpliesOperatorNumber();
				int realAffirmation = opInfo.getRealAffirmationOperatorNumber();
				int realNot = opInfo.getRealNotOperatorNumber();
				int realEqual = opInfo.getRealEqualOperatorNumber();
				int realGreater = opInfo.getRealGreaterOperatorNumber();
				int realGreaterOrEqual = opInfo.getRealGreaterEqualOperatorNumber();
				int realLess = opInfo.getRealLessOperatorNumber();
				int realLessOrEqual = opInfo.getRealLessEqualOperatorNumber();
		   
		        String line = pathwayName + ",=" + operation + ",=" + realOperation + ",=" + operands + ",=" + numeric + ",=" + yesOrNo + ",=" + choice + ",=" + options + ",=" + addition + ",=" + realAddition + ",=" + subtraction + ",=" + realSubtraction + ",=" + multiplication + ",=" + realMultiplication + ",=" + division + ",=" + realDivision + ",=" + and + ",=" + realAnd + ",=" + or + ",=" + realOr + ",=" + xor + ",=" + realXor + ",=" + implies + ",=" + realImplies + ",=" + affirmation + ",=" + realAffirmation + ",=" + not + ",=" + realNot + ",=" + equal + ",=" + realEqual + ",=" + greater + ",=" + realGreater + ",=" + greaterOrEqual + ",=" + realGreaterOrEqual + ",=" + less + ",=" + realLess + ",=" + lessOrEqual + ",=" + realLessOrEqual;
		        
		        recordTable.println(line);
		        
		        System.out.println(pathwayName + " gravado! " + i);
			}else {
				System.out.println(pathwayName + " COM PROBLEMA!!! " + i);
			}
	        
		}
		recordTable.close();
	}
}
