package parser.PathwayVerificationTest;

import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import pathwayMetamodel.Pathway;
import pathwayMetamodel.PathwayMetamodelPackage;

public class PathwayReader {
	private String file;
	
	public PathwayReader() {}
	
	//Constructor: initialize the protocol with XMI file.
	public PathwayReader(String file) {
		this.file = file;
	}	
	
	public Pathway createPathway() {
		PathwayMetamodelPackage.eINSTANCE.eClass();
		
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("xmi", new XMIResourceFactoryImpl());
    
        ResourceSet resSet = new ResourceSetImpl();
        Resource resouce = resSet.getResource(URI.createURI(file), true);
        Pathway protocol = (Pathway) resouce.getContents().get(0);
		
        return protocol; 
	}
}
