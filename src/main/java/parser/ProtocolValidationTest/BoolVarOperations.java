package parser.ProtocolValidationTest;

import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;

import protocolosv2.Operation;
import protocolosv2.Sequence;

public class BoolVarOperations { 
	private int[] indexes = null;	
	
	public int[] getIndexes() {
		return indexes;
	}

	//Add a boolVar variables on a list of sequences.
	//sequences is a list of boolvar variables where the operation of each sequence with the same output step will be stored.
	//op is the operation of each sequence with the same output step.
	//boolVars is a list of the logical operands that make up all operation in the protocol.
	//index is a vector with the index of the operands in the boolVars that are used in the operation op. 
	public BoolVar createSequences(Operation op, List<BoolVar> boolVars, int[] index){
		Model auxModel = new Model("Auxiliary Model");
		BoolVar bool = null;
		BoolVar bool2 = null;
		
		switch(op.getOperator()) {
			case AND:				
				bool = auxModel.arithm(boolVars.get(index[0]), "+", boolVars.get(index[1]), "=", 2).reify();
				
				for (int i = 2; i < index.length; i++) {
					bool2 = auxModel.arithm(bool, "+", boolVars.get(index[i]), "=", 2).reify();
					bool = bool2;
				}
				
				return bool; 
			case OR:
				bool = auxModel.arithm(boolVars.get(index[0]), "+", boolVars.get(index[1]), "!=", 0).reify();
				
				for (int i = 2; i < index.length; i++) {
					bool2 = auxModel.arithm(bool, "+", boolVars.get(index[i]), "!=", 0).reify();
					bool = bool2;
				}
				
				return bool; 
			case IMPLIES:
				bool = auxModel.arithm(boolVars.get(index[0]), "-", boolVars.get(index[1]), "!=", 1).reify();
				
				for (int i = 2; i < index.length; i++) {
					bool2 = auxModel.arithm(bool, "-", boolVars.get(index[i]), "!=", 1).reify();
					bool = bool2;
				}
				
				return bool; 
			case XOR:
				bool = auxModel.arithm(boolVars.get(index[0]), "+", boolVars.get(index[1]), "=", 1).reify();
				
				for (int i = 2; i < index.length; i++) {
					bool2 = auxModel.arithm(bool, "+", boolVars.get(index[i]), "=", 1).reify();
					bool = bool2;
				}
				
				return bool; 
			default:
				return null;
		}
	}
		
	//get the operands from the operation and put it in the list boolVars if the list do not contain it.
	//return the index of the operands of the operation in the boolVars.
	public void create(List<BoolVar> boolVars, Model model, Operation operation) {
		int indexes[] = new int[operation.getOperand().size()];
		for(int i = 0; i < operation.getOperand().size(); i++) {
			Model auxModel = new Model("Auxiliary Model");
			BoolVar boolVar = auxModel.boolVar(operation.getOperand().get(i).getName());
			if(!contains(boolVars, boolVar)) {
				boolVars.add(model.boolVar(operation.getOperand().get(i).getName()));
				indexes[i] = boolVars.size() -1;
			}else {
				indexes[i] = indexOf(boolVars, boolVar);
			}
		}
		
		this.indexes = indexes;
	}
		
	//Verify whether list boolVars already has the boolvar
	public boolean contains(List<BoolVar> boolVars, BoolVar boolVar) {
		String name = boolVar.getName();
		for(int i = 0; i < boolVars.size(); i++) {
			String bName = boolVars.get(i).getName();
			if(bName.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	//retun the index of the boolVar from the list boolVars. 
	public int indexOf(List<BoolVar> boolVars, BoolVar boolVar) {
		String name = boolVar.getName();
		for(int i = 0; i < boolVars.size(); i++) {
			String bName = boolVars.get(i).getName();
			if(bName.equals(name)) {
				return i;
			}
		}
		return -1;
	}
}
