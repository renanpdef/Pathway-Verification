package parser.ProtocolVerificationTest;

import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import protocolosv2.Protocol;
import protocolosv2.Protocolosv2Package;

public class ProtocolReader {
	private String file;
	
	public ProtocolReader() {}
	
	//Constructor: initialize the protocol with XMI file.
	public ProtocolReader(String file) {
		this.file = file;
	}	
	
	public Protocol createProtocol() {
		Protocolosv2Package.eINSTANCE.eClass();
		
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("xmi", new XMIResourceFactoryImpl());
    
        ResourceSet resSet = new ResourceSetImpl();
        Resource resouce = resSet.getResource(URI.createURI(file), true);
        Protocol protocol = (Protocol) resouce.getContents().get(0);
		
        return protocol; 
	}
}
