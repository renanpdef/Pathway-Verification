package parser.ProtocolValidationTest;

import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;

import protocolosv2.Operation;

public class BoolVarOperations {
	private int[] indexes = null;	
	
	public int[] getIndexes() {
		return indexes;
	}

	//add the bool variables on choco solver
	public void addSequences(List<BoolVar> sequences, Operation op, List<BoolVar> boolVars, int[] index){
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
				
				sequences.add(bool);				
				break;
			case OR:
				bool = auxModel.arithm(boolVars.get(index[0]), "+", boolVars.get(index[1]), "!=", 0).reify();
				
				for (int i = 2; i < index.length; i++) {
					bool2 = auxModel.arithm(bool, "+", boolVars.get(index[i]), "!=", 0).reify();
					bool = bool2;
				}
				
				sequences.add(bool);		
				break;
			case IMPLIES:
				bool = auxModel.arithm(boolVars.get(index[0]), "-", boolVars.get(index[1]), "=", 1).reify();
				
				for (int i = 2; i < index.length; i++) {
					bool2 = auxModel.arithm(bool, "-", boolVars.get(index[i]), "=", 1).reify();
					bool = bool2;
				}
				
				sequences.add(bool);
				break;
			case XOR:
				bool = auxModel.arithm(boolVars.get(index[0]), "+", boolVars.get(index[1]), "=", 1).reify();
				
				for (int i = 2; i < index.length; i++) {
					bool2 = auxModel.arithm(bool, "+", boolVars.get(index[i]), "=", 1).reify();
					bool = bool2;
				}
				
				sequences.add(bool);
				break;
			default:
				break;
		}
	}
		
	//create the BoolVars and return the index of the operands of the operation.
	public void createBoolVars(List<BoolVar> boolVars, Model model, Operation operation) {
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
