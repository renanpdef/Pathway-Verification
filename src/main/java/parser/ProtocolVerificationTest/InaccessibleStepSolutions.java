package parser.ProtocolVerificationTest;

import java.util.List;
import java.util.Map;

import org.chocosolver.solver.Solution;

import protocolosv2.Element;

public class InaccessibleStepSolutions {

	public static void main(String[] args) {
		String file1 = "XMIs_Test\\Test_01.xmi";
        
        ProtocolReader protocolReader = new ProtocolReader(file1);
        InaccessibleStep inaccessibleStep = new InaccessibleStep(protocolReader.createProtocol());
        
        Map<String, List<Solution>> mapInaccessibleStep = inaccessibleStep.inaccessibleStepsSolutions();
        for(int k = 0; k < mapInaccessibleStep.size(); k++) {
        	List<Solution> inaccessibleStepSolutions = (List<Solution>) mapInaccessibleStep.values().toArray()[k];
        	String stepName = (String) mapInaccessibleStep.keySet().toArray()[k];
        	System.out.println(stepName);
        	//Go through the list of solutions
        	for (int i = 0; i < inaccessibleStepSolutions.size(); i++) {
	        	String str[] = inaccessibleStepSolutions.get(i).toString().split(",");
	        	for(int j = 0; j < str.length; j++) {
	        		//print just true operands from the vector str.
	        		//REIF is a constraint that was reify to a BoolVar.
	        		//exp is a result from a mathematical expression.
	        		if(!str[j].contains("REIF") && !str[j].contains("exp") && !str[j].contains("not")) {
	        			System.out.print(str[j]);
	        		}
	        	}
	        	System.out.println();
	        }
        }
	}

}
