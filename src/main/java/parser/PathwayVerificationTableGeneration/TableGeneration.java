package parser.PathwayVerificationTableGeneration;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import parser.PathwayVerificationTest.PathwayReader;
import pathwayMetamodel.Pathway;

public class TableGeneration {
	String[] files = {"Pathways/Hospital_Development/aminiorrexe_prematura.xmi", 
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
			"Pathways/Clinical_Approval/viver_bem_triploterapia_linha_afro.xmi"};
//	};
	
	public void createTable() throws IOException {
		FileWriter table = new FileWriter("pathwaysTable.txt");
		PrintWriter recordTable = new PrintWriter(table);
		String header = "Pathway, States, Transitions, Path, Deadlock, Nondeterminism, Inaccessible States, Equivalent Transitions";
		recordTable.println(header);
		for (int i = 0; i < files.length; i++) {
			PathwayReader pathwayReader = new PathwayReader(files[i]);
	        Pathway pathway = pathwayReader.createPathway();
	        Information info = new Information(pathway);
	   
			String pathwayName = files[i].split("/")[2].replaceAll(".xmi", "");
			ArrayList<Integer> problemas = new ArrayList<Integer>(){{add(24); add(26);}};
			if(!problemas.contains(i)) {
				int deadlock = info.getDeadlockNumber();
				int nonDeterminism = info.getNonDeterminismNumber();
				int inaccessible = info.getInaccessibleStepNumber();
		        int equivalentTransition = info.getEquivalentTransitionsNumber();
		        int states = info.getStatesNumber();
		        int transitions = info.getTransitionsNumber();
		        int path = info.pathNumber();
		   
		        String line = pathwayName + ",=" + states + ",=" + transitions + ",=" + path + ",=" + deadlock + ",=" + nonDeterminism + ",=" + inaccessible + ",=" + equivalentTransition;
		        
		        recordTable.println(line);
		        
		        System.out.println(pathwayName + " gravado! " + i);
			}else {
				System.out.println(pathwayName + " COM PROBLEMA!!! " + i);
			}
	        
		}
		recordTable.close();
	}
}
