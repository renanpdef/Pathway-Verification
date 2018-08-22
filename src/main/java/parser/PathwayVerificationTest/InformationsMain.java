package parser.PathwayVerificationTest;
import pathwayMetamodel.Pathway;


public class InformationsMain {

	public static void main(String[] args) {
//		String file1 = "XMIs_Real_Test\\sifilis.xmi";
//		
//		PathwayReader pathwayReader = new PathwayReader(file1);
//        Pathway pathway = pathwayReader.createPathway();
//        
//        System.out.println("States: "+pathway.getElement().size());
//        System.out.println("Transitions: "+pathway.getSequence().size());
		int n = 0;
		String str = getsDomain("", "1;40;7;");
		int[] domain = stringSetToIntSet(n, str);
		System.out.println(domain[0]);
		System.out.println(domain[1]);
		System.out.println(domain[2]);
		System.out.println(domain[3]);
		System.out.println(domain[4]);
		System.out.println(domain[5]);
		System.out.println(domain[6]);
		System.out.println(domain[7]);
		
	}
	
	private static int[] stringSetToIntSet(int operandValue, String domainSTR) {
		String[] domainArray = domainSTR.split(";");
		int[] domain = new int[domainArray.length + 1];
		domain[0] = operandValue;
		for(int i = 0; i < domainArray.length; i++) {
			String[] subDomain = domainArray[i].split(",");
			int addition = operandValue;
			for (int j = 0; j < subDomain.length; j++) {
				addition += Integer.parseInt(subDomain[j]);
			}
			domain[i+1] = addition;
		}
		return domain;
	}
	
	private static String getsDomain(String auxOptions, String options) {
		if(options.equals("")) {
			return auxOptions;
		}
		else {
			String[] strArray = options.split(";");
			String str = strArray[0];
			return getsDomain(auxOptions + str + ",", options.substring(str.length()+1)) + ";" + getsDomain(auxOptions, options.substring(str.length()+1));
		}
	}
}
