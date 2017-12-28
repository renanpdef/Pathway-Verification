package parser.ProtocolValidationTest;

import java.util.ArrayList;
import java.util.List;

import protocolosv2.Operation;

public class OperandParser {

	private Operation operation;
	public OperandParser(Operation operation) {
		this.operation = operation;
	}
	
	private String stringParser(String str) {
		int pos1 = str.lastIndexOf("(")+1;
		int pos2 = str.lastIndexOf(")");
		char[] charValue = new char[pos2-pos1];
		str.getChars(pos1, pos2, charValue, 0);
		String strValue = String.copyValueOf(charValue);
		strValue = strValue.toString().replaceFirst("value: ", "");
		return strValue;
	}
	

	public List<String> getOprandValue() {
		int size = operation.getOperand().size();
		List<String> strValue = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			strValue.add(stringParser(operation.getOperand().get(i).toString()));
		}
		return strValue;
	}
}
