package parser.ProtocolValidationTest;

import java.util.ArrayList;


import java.util.List;
import java.util.Map;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import protocolosv2.Protocolosv2Package;
import protocolosv2.Operation;
import protocolosv2.Protocol;

public class SequenceParser {
	private Protocol protocol;	
	
	List<String> elements = new ArrayList<String>();
	
	//Constructor: initialize the protocol with XMI file.
	public SequenceParser(String file) {
		Protocolosv2Package.eINSTANCE.eClass();
		
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("xmi", new XMIResourceFactoryImpl());
    
        ResourceSet resSet = new ResourceSetImpl();
        Resource resouce = resSet.getResource(URI.createURI(file), true);
        this.protocol = (Protocol) resouce.getContents().get(0);
	}
	
	public List<Solution> findDeadLockSolutions(){
		//Initialize choco solver model
		Model model = new Model("Same Guard Condition");
		List<BoolVar> sequences = getSequences(model);
		if (sequences!=null && !sequences.isEmpty()) {
			//Add clauses list to the model
			for(int k = 1; k < sequences.size(); k++) {
				model.arithm(sequences.get(k), "=", 0).post();
			}

			//List<Solution> solutions = new ArrayList<Solution>();
			return model.getSolver().findAllSolutions();
		}
		return null;
	}
	
	public List<Solution> findNonDeterminismSolutions(){
		//Initialize choco solver model
		Model model = new Model("Same Guard Condition");
		List<BoolVar> sequences = getSequences(model);
		if (sequences!=null && !sequences.isEmpty()) {
			//Add clauses list to the model
			List<BoolVar> seq_aux = new ArrayList<BoolVar>();
			BoolVar b_flag = model.boolVar();
			BoolVar b_flag2 = model.boolVar();
			for(int k = 1; k < sequences.size(); k++) {
				if (seq_aux.isEmpty()) {
					BoolVar s = model.arithm(sequences.get(k-1), "+", sequences.get(k), ">=", 1).reify();
					model.arithm(sequences.get(k-1), "+", sequences.get(k), "=", 2).reifyWith(b_flag);
					
					seq_aux.add(s);
				}else if(k < sequences.size()-1){
					BoolVar s = model.arithm(seq_aux.get(seq_aux.size()-1), "+", sequences.get(k), ">=", 1).reify();
					model.arithm(seq_aux.get(seq_aux.size()-1), "+", sequences.get(k), "=", 2).reifyWith(b_flag);
					seq_aux.add(s);
				}else {
					model.arithm(seq_aux.get(seq_aux.size()-1), "+", sequences.get(k), ">=", 1).post();
					model.arithm(seq_aux.get(seq_aux.size()-1), "+", sequences.get(k), "=", 2).reifyWith(b_flag2);
					model.arithm(b_flag, "+", b_flag2, ">=", 1).post();
				}
			}

			//List<Solution> solutions = new ArrayList<Solution>();
			return model.getSolver().findAllSolutions();
		}
		return null;
	}
	
	//Function to return all solutions in a list of sequences with the same output step.
	//Use the choco solver to get possible solutions and return them
	public List<Solution> findAllSolutions() {
		//Initialize choco solver model
		Model model = new Model("Same Guard Condition");
		List<BoolVar> sequences = getSequences(model);
		if (sequences!=null && !sequences.isEmpty()) {
			//Add clauses list to the model
			List<BoolVar> seq_aux = new ArrayList<BoolVar>();
			for(int k = 1; k < sequences.size(); k++) {
				if (seq_aux.isEmpty()) {
					model.arithm(sequences.get(k-1), "+", sequences.get(k), "<", 2).post();
					BoolVar s = model.arithm(sequences.get(k-1), "+", sequences.get(k), "=", 1).reify();
					seq_aux.add(s);
				}else if(k < sequences.size()-1){
					model.arithm(seq_aux.get(seq_aux.size()-1), "+", sequences.get(k), "<", 2).post();
					BoolVar s = model.arithm(seq_aux.get(seq_aux.size()-1), "+", sequences.get(k), "=", 1).reify();
					seq_aux.add(s);
				}else {
					model.arithm(seq_aux.get(seq_aux.size()-1), "+", sequences.get(k), "=", 1).post();
				}
			}

			//List<Solution> solutions = new ArrayList<Solution>();
			return model.getSolver().findAllSolutions();
		}
		return null;
	}
	
	//Function to verify whether there are sequences with the same output step
	public boolean isThereSameOutputStep() {
		for (int i = 0; i < protocol.getSequence().size(); i++) {
			for (int j = i+1; j < protocol.getSequence().size(); j++) {
				if(protocol.getSequence().get(i).getOutputStep() == protocol.getSequence().get(j).getOutputStep()) {
					return true;
				}
			}
		}
		return false;
	}

	//Function to get the sequences with the same output step
	public List<BoolVar> getSequences(Model model) {
		//Lista de operandos
		List<BoolVar>  boolVars = new ArrayList<BoolVar>();
		List<IntVar>  intVars = new ArrayList<IntVar>();
		//List of operations with the operations of each sequence
		List<Operation> operations = new ArrayList<Operation>();
		//Loop to get sequences with the same output step
		for (int i = 0; i < protocol.getSequence().size(); i++) {
			//List of boolVars with a boolVar for each sequence
			//These boolVar are the conditions guard
			List<BoolVar> sequences = new ArrayList<BoolVar>();
			
			for (int j = i+1; j < protocol.getSequence().size(); j++) {
				if(protocol.getSequence().get(i).getOutputStep() == protocol.getSequence().get(j).getOutputStep()) {	
					Operation opi = protocol.getSequence().get(i).getOperation();
					Operation opj = protocol.getSequence().get(j).getOperation();
					if(!operations.contains(opi)) {
						operations.add(opi);
						createSequenceList(boolVars, intVars, sequences, model, opi);
					}
					if(!operations.contains(opj)) {
						operations.add(opj);
						createSequenceList(boolVars, intVars, sequences, model, opj);
					}
				}
			}
			
			if (sequences!=null && !sequences.isEmpty()) {
				return sequences;
			}
		}
		return null;
	}		
	
	//Create Clauses for one operation
	public void createSequenceList(List<BoolVar> boolVars, List<IntVar> intVars, List<BoolVar> sequences, Model model, Operation operation){
		int index[] = null;
		//Verify the operator of each operation.
		//Create the clauses according to the operator and 
		//add them on the clauses list
		switch (operation.getOperator()) {
			case AND:
			case OR:
			case IMPLIES:
			case XOR:
				index = createBoolVars(boolVars, model, operation);
				
				addSequencesBoolVar(sequences, operation, boolVars, index);
				break;
			
			case EQUAL:
			case EQUAL_OR_GREATER:
			case EQUAL_OR_SMALLER:
			case BIGGER_THAN:
			case SMALLER_THAN:
				index = createIntVars(intVars, model, operation);//ATENCAO
				
				addSequencesIntVar(sequences, operation, intVars, index);								
				break;
			case SUM:								
				break;							
			case MINUS:								
				break;								
			case MULTIPLICATION:								
				break;								
			case DIVISION:							
				break;									
			case AFFIRMATION:								
				break;					
			default:
				index = createBoolVars(boolVars, model, operation);
				BoolVar bool_s2 = model.arithm(boolVars.get(index[0]), "+", boolVars.get(index[0]), "=", 0).reify();
				sequences.add(bool_s2);
				break;
		}
	}
	
	//calculate the int variables on choco solver
	public void addSequencesIntVar(List<BoolVar> sequences, Operation op, List<IntVar> intVars, int[] index) {
		Model model_aux = new Model("Axiliary IntVar Model");
		BoolVar boolVar = null;
		
		switch(op.getOperator()) {
			case EQUAL:
				boolVar = model_aux.arithm(intVars.get(index[0]),"==",intVars.get(index[1])).reify();
				sequences.add(boolVar);
				break;
			case EQUAL_OR_GREATER:
				boolVar = model_aux.arithm(intVars.get(index[0]),">=",intVars.get(index[1])).reify();
				sequences.add(boolVar);
				break;
			case EQUAL_OR_SMALLER:
				boolVar = model_aux.arithm(intVars.get(index[0]),"<=",intVars.get(index[1])).reify();
				sequences.add(boolVar);
				break;
			case BIGGER_THAN:
				boolVar = model_aux.arithm(intVars.get(index[0]),">",intVars.get(index[1])).reify();
				sequences.add(boolVar);
				break;
			case SMALLER_THAN:
				boolVar = model_aux.arithm(intVars.get(index[0]),"<",intVars.get(index[1])).reify();
				sequences.add(boolVar);
				break;
			default:
				break;
		}
	}
	
	//add the bool variables on choco solver
	public void addSequencesBoolVar(List<BoolVar> sequences, Operation op, List<BoolVar> boolVars, int[] index){
		Model model_aux = new Model("Auxiliary Model");
		BoolVar bool = null;
		BoolVar bool2 = null;
		
		switch(op.getOperator()) {
			case AND:				
				bool = model_aux.arithm(boolVars.get(index[0]), "+", boolVars.get(index[1]), "=", 2).reify();
				
				for (int i = 2; i < index.length; i++) {
					bool2 = model_aux.arithm(bool, "+", boolVars.get(index[i]), "=", 2).reify();
					bool = bool2;
				}
				
				sequences.add(bool);				
				break;
			case OR:
				bool = model_aux.arithm(boolVars.get(index[0]), "+", boolVars.get(index[1]), "!=", 0).reify();
				
				for (int i = 2; i < index.length; i++) {
					bool2 = model_aux.arithm(bool, "+", boolVars.get(index[i]), "!=", 0).reify();
					bool = bool2;
				}
				
				sequences.add(bool);		
				break;
			case IMPLIES:
				bool = model_aux.arithm(boolVars.get(index[0]), "-", boolVars.get(index[1]), "=", 1).reify();
				
				for (int i = 2; i < index.length; i++) {
					bool2 = model_aux.arithm(bool, "-", boolVars.get(index[i]), "=", 1).reify();
					bool = bool2;
				}
				
				sequences.add(bool);
				break;
			case XOR:
				bool = model_aux.arithm(boolVars.get(index[0]), "+", boolVars.get(index[1]), "=", 1).reify();
				
				for (int i = 2; i < index.length; i++) {
					bool2 = model_aux.arithm(bool, "+", boolVars.get(index[i]), "=", 1).reify();
					bool = bool2;
				}
				
				sequences.add(bool);
				break;
			default:
				break;
		}
	}
	
	//create the IntVars
	public int[] createIntVars(List<IntVar> intVars, Model model, Operation operation) {		
		int index[] = new int[operation.getOperand().size()];
		for(int i = 0; i < operation.getOperand().size(); i++) {
			Model model_aux = new Model("Auxiliary Model");
			IntVar intVar = model_aux.intVar(operation.getOperand().get(i).getName(), 0, Integer.MAX_VALUE);
			if(!containsInt(intVars, intVar)) {
				intVars.add(model.intVar(operation.getOperand().get(i).getName(), 0, Integer.MAX_VALUE));
				index[i] = intVars.size() -1;
			}else {
				index[i] = indexOfInt(intVars, intVar);
			}
		}
		return index;
	}
	
	//create the BoolVars and return the index of the operands of the operation.
	public int[] createBoolVars(List<BoolVar> boolVars, Model model, Operation operation) {
		int index[] = new int[operation.getOperand().size()];
		for(int i = 0; i < operation.getOperand().size(); i++) {
			Model model_aux = new Model("Auxiliary Model");
			BoolVar boolVar = model_aux.boolVar(operation.getOperand().get(i).getName());
			if(!containsBool(boolVars, boolVar)) {
				boolVars.add(model.boolVar(operation.getOperand().get(i).getName()));
				index[i] = boolVars.size() -1;
			}else {
				index[i] = indexOfBool(boolVars, boolVar);
			}
		}
		return index;
	}
	
	//Verify whether list boolVars already has the boolvar
	public boolean containsBool(List<BoolVar> boolVars, BoolVar boolVar) {
		String bName = boolVar.getName();
		for(int i = 0; i < boolVars.size(); i++) {
			String bNames = boolVars.get(i).getName();
			if(bNames.equals(bName)) {
				return true;
			}
		}
		return false;
	}
	
	public int indexOfBool(List<BoolVar> boolVars, BoolVar boolVar) {
		String bName = boolVar.getName();
		for(int i = 0; i < boolVars.size(); i++) {
			String bNames = boolVars.get(i).getName();
			if(bNames.equals(bName)) {
				return i;
			}
		}
		return -1;
	}
	
	//Verify whether list boolVars already has the boolvar
	public boolean containsInt(List<IntVar> intVars, IntVar intVar) {
		String bName = intVar.getName();
		for(int i = 0; i < intVars.size(); i++) {
			String bNames = intVars.get(i).getName();
			if(bNames.equals(bName)) {
				return true;
			}
		}
		return false;
	}
	
	public int indexOfInt(List<IntVar> intVars, IntVar intVar) {
		String bName = intVar.getName();
		for(int i = 0; i < intVars.size(); i++) {
			String bNames = intVars.get(i).getName();
			if(bNames.equals(bName)) {
				return i;
			}
		}
		return -1;
	}
}
