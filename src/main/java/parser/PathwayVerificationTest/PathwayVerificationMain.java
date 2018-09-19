package parser.PathwayVerificationTest;

import java.util.List;
import java.util.Map;
import org.chocosolver.solver.Solution;
import pathwayMetamodel.Element;
import pathwayMetamodel.Pathway;
import pathwayMetamodel.Sequence;

public class PathwayVerificationMain {
    
	public static void main( String[] args )    {
		String[] files = {
				"Pathways/Hospital_Development/aminiorrexe_prematura.xmi", 
				"Pathways/Hospital_Development/cefaleia.xmi",
				"Pathways/Hospital_Development/dheg.xmi",
				"Pathways/Hospital_Development/diarreia.xmi",
				"Pathways/Hospital_Development/dor_abdominal.xmi",
				"Pathways/Hospital_Development/dor_toracica.xmi",
				"Pathways/Hospital_Development/fratura_exposta.xmi",
				"Pathways/Hospital_Development/itu__cistite.xmi",
				"Pathways/Hospital_Development/itu__pielonefrite.xmi",
				"Pathways/Hospital_Development/ivas.xmi",
				"Pathways/Hospital_Development/lombalgia.xmi",
				"Pathways/Hospital_Development/meningite.xmi",
				"Pathways/Hospital_Development/nascer_bem_toxoplasmose.xmi",
				"Pathways/Hospital_Development/neutropenia_febril.xmi",
				"Pathways/Hospital_Development/pneumonia__influenza.xmi",
				"Pathways/Hospital_Development/tratamento_de_sepse.xmi",
				"Pathways/Clinical_Development/acne.xmi",
				"Pathways/Clinical_Development/cefaleia.xmi",
				"Pathways/Clinical_Development/diabetes_gestacional.xmi",
				"Pathways/Clinical_Development/diarreia.xmi",
				"Pathways/Clinical_Development/dispepsia.xmi",
				"Pathways/Clinical_Development/dor_articular.xmi",
				"Pathways/Clinical_Development/faringite.xmi",
				"Pathways/Clinical_Development/hipertensao.xmi",
				"Pathways/Clinical_Development/insuficiencia_cardiaca.xmi",
				"Pathways/Clinical_Development/itu.xmi",
				"Pathways/Clinical_Development/medico_da_familia_asma_infantil.xmi",
				"Pathways/Clinical_Development/medico_da_familia_dor_lombar.xmi",
				"Pathways/Clinical_Development/medico_de_familia_dpoc.xmi",
				"Pathways/Clinical_Development/otite_media_aguda.xmi",
				"Pathways/Clinical_Development/parasitose.xmi",
				"Pathways/Clinical_Development/sifilis.xmi",
				"Pathways/Clinical_Development/vertigem.xmi",
				"Pathways/Clinical_Development/vulvovaginite.xmi",
				"Pathways/Clinical_Approval/viver_bem_bar__bbc__betabloqueador.xmi",
				"Pathways/Clinical_Approval/viver_bem_diabetes__dislipidemia.xmi",
				"Pathways/Clinical_Approval/viver_bem_diabetes__itu.xmi",
				"Pathways/Clinical_Approval/viver_bem_diabetes__tratamento_dm.xmi",
				"Pathways/Clinical_Approval/viver_bem_dm_ajustes.xmi",
				"Pathways/Clinical_Approval/viver_bem_duploterapia_bar.xmi",
				"Pathways/Clinical_Approval/viver_bem_duploterapia_linha_afro.xmi",
				"Pathways/Clinical_Approval/viver_bem_duploterapia_linha_ieca.xmi",
				"Pathways/Clinical_Approval/viver_bem_hipertensao.xmi",
				"Pathways/Clinical_Approval/viver_bem_ieca__bbc__betabloqueador.xmi",
				"Pathways/Clinical_Approval/viver_bem_monoterapia_bar.xmi",
				"Pathways/Clinical_Approval/viver_bem_monoterapia_linha_afro.xmi",
				"Pathways/Clinical_Approval/viver_bem_monoterapia_linha_ieca.xmi",
				"Pathways/Clinical_Approval/viver_bem_protocolo_farmacologico_afroamericano.xmi",
				"Pathways/Clinical_Approval/viver_bem_tetraterapia_bar.xmi",
				"Pathways/Clinical_Approval/viver_bem_tetraterapia_linha_afro.xmi",
				"Pathways/Clinical_Approval/viver_bem_tetraterapia_linha_ieca.xmi",
				"Pathways/Clinical_Approval/viver_bem_tratamento_dislipidemia__risco_alto.xmi",
				"Pathways/Clinical_Approval/viver_bem_tratamento_dislipidemia__risco_baixo.xmi",
				"Pathways/Clinical_Approval/viver_bem_tratamento_dislipidemia__risco_intermediario.xmi",
				"Pathways/Clinical_Approval/viver_bem_tratamento_dislipidemia__risco_muito_alto.xmi",
				"Pathways/Clinical_Approval/viver_bem_triploterapia_bar.xmi",
				"Pathways/Clinical_Approval/viver_bem_triploterapia_linha_afro.xmi"
				};
		
        String file1 = files[28];
        
        
        PathwayReader pathwayReader = new PathwayReader(file1);
        Pathway pathway = pathwayReader.createPathway();
        FindSolutions findSolutions = new FindSolutions(pathway);
        FindInaccessibleStep inaccessibleStep = new FindInaccessibleStep(pathway);
        
//        //print all valid solutions for variables in pathway
//        printFoundSolutions(findSolutions.findAllValidSolutions(), "FIND ALL VALID SOLUTIONS");
//        System.out.println("-------------------------------------------------------------------------");
//        
//        //print some solutions that occur non determinism problem
//        printFoundSolutions(findSolutions.findNonDeterminismSolutions(), "FIND NON DETERMINISM SOLUTIONS");
//        System.out.println("-------------------------------------------------------------------------");
//        
        //print all solutions that occur deadlock
//        printFoundSolutions(findSolutions.findDeadLockSolutions(), "FIND DEADLOCK SOLUTIONS");
//        System.out.println("-------------------------------------------------------------------------");
//        
        //print logically equivalent sequences
//        printEquivalentSequences(findSolutions.findLogicallyEquivalentSequence());
//        System.out.println("-------------------------------------------------------------------------");
//        
//        //Verify if there are Inaccessible Step
//        printInaccessibleStep(inaccessibleStep.findInaccessibleSteps());
//        System.out.println("-------------------------------------------------------------------------");        
    }
    
    public static void printFoundSolutions(Map<Element, List<Solution>> mapSolutions, String solutionsName) {
    	System.out.println("\n" + solutionsName);
    	//Go through all lists of solutions from mapSolutions.
        for (int k = 0; k < mapSolutions.size(); k++) {
        	List<Solution> solutionsList = (List<Solution>) mapSolutions.values().toArray()[k];
        	Element step = (Element) mapSolutions.keySet().toArray()[k];//get the output step of the sequences which was analyzed.
        	System.out.println("\n" + step.getClass().getSimpleName() +": "+ step.getName());//print the name of the output step.
        	printSequencesOperations(step);
        	//printSequencesOperations(step);
        	//Go through the list of solutions
        	for (int i = 0; i < solutionsList.size(); i++) {
	        	String str[] = solutionsList.get(i).toString().split(",");
	        	for(int j = 0; j < str.length; j++) {
	        		//print just true operands from the vector str.
	        		//REIF is a constraint that was reify to a BoolVar.
	        		//exp is a result from a mathematical expression.
	        		if(!str[j].contains("REIF") && !str[j].contains("exp")) {
	        			System.out.print(str[j]);
	        		}
	        	}
	        	System.out.println();
	        }
        }
    }
    
    public static void printEquivalentSequences(Map<Element, List<Sequence>> mapEquivalentSequences) {
    	System.out.println("\nLOGICALLY EQUIVALENT SEQUENCES");
    	//Go through all lists of solutions from mapDeadLockSolutions.
        for (int k = 0; k < mapEquivalentSequences.size(); k++) {
        	List<Sequence> sequenceList = (List<Sequence>) mapEquivalentSequences.values().toArray()[k];
        	Element step = (Element) mapEquivalentSequences.keySet().toArray()[k];//get the output step of the sequences which was analyzed.
        	System.out.println("\n" + step.getClass().getSimpleName() +": "+ step.getName());//print the name of the output step.
        	for(int i = 0; i < sequenceList.size(); i++) {
        		System.out.println(sequenceList.get(i).getName() + ": " + sequenceList.get(i).getOperation().getName());
        	}
        	System.out.println();
        }
    }
    
    public static void printInaccessibleStep(List<Element> elements) {
    	System.out.println("\nINACCESSIBLE STEPS");
    	for(int k = 0; k < elements.size(); k++) {
    		System.out.println("\n" + elements.get(k).getClass().getSimpleName() +": "+ elements.get(k).getName());//print the name of the Element.
        }
    }
    
    public static void printSequencesOperations(Element element) {
    	for(int i = 0; i < element.getOutputSequences().size(); i ++) {
    		if(element.getOutputSequences().get(i).getOperation() != null) {
    			System.out.println(element.getOutputSequences().get(i).getName()+ ": " + element.getOutputSequences().get(i).getOperation().getName());
    		}
    	}
    }
}
