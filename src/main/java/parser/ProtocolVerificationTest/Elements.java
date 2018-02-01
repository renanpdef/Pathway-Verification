package parser.ProtocolVerificationTest;

import java.util.ArrayList;
import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;

import protocolosv2.Element;

public class Elements {

	//elements and boolSequencesList have same size.
	public List<BoolVar> elementsToBoolvarList(List<Element> elements, List<List<BoolVar>>boolSequencesList, Model model) {
		List<BoolVar> boolElements = new ArrayList<BoolVar>();
		for(int i = 0; i < boolSequencesList.size(); i ++) {
			BoolVar boolElement = elementToBoolVar(elements.get(i), boolSequencesList.get(i), model);
			boolElements.add(boolElement);
		}
		return boolElements;
	}
	
	public BoolVar elementToBoolVar(Element element, List<BoolVar> boolSequences, Model model) {
		BoolVar boolElement = model.boolVar(element.getClass().getSimpleName() +"::"+ element.getName());
//		for(int i = 0; i < boolSequences.size(); i++) {
//			model.arithm(boolSequences.get(i), "=", 0).reifyWith(boolElement);
//		}
		return boolElement;
	}
}
