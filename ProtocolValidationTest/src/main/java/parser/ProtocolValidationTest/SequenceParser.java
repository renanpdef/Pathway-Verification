package parser.ProtocolValidationTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;
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
	
	//Construtor: inicializa o protocolo ao receber um arquivo xmi.
	public SequenceParser(String file) {
		Protocolosv2Package.eINSTANCE.eClass();
		
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("xmi", new XMIResourceFactoryImpl());
    
        ResourceSet resSet = new ResourceSetImpl();
        Resource resouce = resSet.getResource(URI.createURI(file), true);
        this.protocol = (Protocol) resouce.getContents().get(0);
	}
	
	//Funcao para verificar se existe sequencias com o mesmo passo de saida.
	public boolean isThereSameOutputStep() {
		for (int i = 0; i < protocol.getSequence().size(); i++) {
			for (int j = i+1; j < protocol.getSequence().size(); j++) {
				if(protocol.getSequence().get(i).getPassoDeSaida() == protocol.getSequence().get(j).getPassoDeSaida()) {
					return true;
				}
			}
		}
		return false;
	}

	//Funcao para verificar se as sequencias com mesmo passo de saida tem a mesma condicao de guarda
	//usa o choco para ver solucoes possiveis
	//Retorna possivei solucoes
	public List<Solution> sameGuardCondition() {
		
		//Inicializa model do choco
		Model model = new Model("Same Guard Condition");
		
		//Laco para obter sequencias com o memsmo passo de saida
		for (int i = 0; i < protocol.getSequence().size(); i++) {
			for (int j = i+1; j < protocol.getSequence().size(); j++) {
				if(protocol.getSequence().get(i).getPassoDeSaida() == protocol.getSequence().get(j).getPassoDeSaida()) {
					//System.out.println(protocol.getSequence().get(i).getOperation().getOperator().getValue());
					Operation opi =  protocol.getSequence().get(i).getOperation();
					Operation opj =  protocol.getSequence().get(j).getOperation();
					
					//Lista de operacoes com as operacoes de cada sequencia.
					List<Operation> operations = new ArrayList<Operation>();
					operations.add(opi);
					operations.add(opj);
					
					//Lista de clausulas para encontrar solucoes possiveis com o choco.
					//Essas clausulas sao as condicoes de guarda.
					List<LogOp> clauses = new ArrayList<LogOp>();
					
					//Laco para armazenar as clausulas de cada operacao dentro da lista clauses.
					for(int k = 0; k < operations.size(); k++){
						
						//Verifica o operador de cada operacao.
						//Cria as clausulas conforme o operador e adiciona-as na lista clauses.
						switch (operations.get(k).getOperator()) {
							case AND:
							case OR:
							case IMPLIES:
							case XOR:
								BoolVar boolA = model.boolVar("A"+k);
								BoolVar boolB = model.boolVar("B"+k);
								switch(operations.get(k).getOperator()) {
									case AND:
										clauses.add(LogOp.and(boolA,boolB));
										break;
									case OR:
										clauses.add(LogOp.or(boolA,boolB));
										break;
									case IMPLIES:
										clauses.add(LogOp.implies(boolA, boolB));
										break;
									case XOR:
										clauses.add(LogOp.xor(boolA,boolB));
										break;
									default:
										break;
								}
								break;
							
							case EQUAL:
							case EQUAL_OR_GREATER:
							case EQUAL_OR_SMALLER:
							case BIGGER_THAN:
							case SMALLER_THAN:
								IntVar intA = model.intVar("A"+k,0,Integer.MAX_VALUE);
								IntVar intB = model.intVar("A"+k,0,Integer.MAX_VALUE); 
								switch(operations.get(k).getOperator()) {
									case EQUAL:
										model.arithm(intA,"==",intB);
										break;
									case EQUAL_OR_GREATER:
										model.arithm(intA,">=",intB);
										break;
									case EQUAL_OR_SMALLER:
										model.arithm(intA,"<=",intB);
										break;
									case BIGGER_THAN:
										model.arithm(intA,">",intB);
										break;
									case SMALLER_THAN:
										model.arithm(intA,"<",intB);
										break;
									default:
										break;
								}
								break;
							case SUM:
								
								break;
							
							case MINUS:
								
								break;
								
							case MULTIPLICATION:
								
								break;
								
							case DIVISION:
								
								break;
								
							case NOT:
								
								break;
							
							case AFFIRMATION:
								
								break;
					
							default:
								break;
						}
					}
					
					//Adiciona a lista clauses no model
					model.addClauses(LogOp.xor(clauses.get(0), clauses.get(1)));
					List<Solution> solutions = new ArrayList<Solution>();
					
					//Obtem as solucoes possiveis.
					while(model.getSolver().solve()) {
						solutions.add(model.getSolver().findSolution());
					}
					return solutions;
				}
			}
		}
		return null;
	}
}
