package parser.PathwayVerificationTableGeneration;
import java.io.IOException;

import parser.PathwayVerificationTest.PathwayReader;
import pathwayMetamodel.Pathway;


public class InformationsMain {

	public static void main(String[] args) throws IOException {
		
		TableGeneration tableGeneration = new TableGeneration();
		tableGeneration.createTable();
	}
}
