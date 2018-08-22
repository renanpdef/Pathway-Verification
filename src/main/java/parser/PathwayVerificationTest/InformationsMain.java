package parser.PathwayVerificationTest;
import pathwayMetamodel.Pathway;


public class InformationsMain {

	public static void main(String[] args) {
		String file1 = "XMIs_Real_Test\\sifilis.xmi";
		
		PathwayReader pathwayReader = new PathwayReader(file1);
        Pathway pathway = pathwayReader.createPathway();
        
        System.out.println("States: "+pathway.getElement().size());
        System.out.println("Transitions: "+pathway.getSequence().size());
	}
}
