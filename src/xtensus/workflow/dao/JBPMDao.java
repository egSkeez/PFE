package xtensus.workflow.dao;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.process.Connection;
import org.drools.definition.process.Node;
import org.drools.definition.process.WorkflowProcess;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.bpmn2.core.SequenceFlow;
import org.jbpm.process.instance.ProcessInstance;
import xtensus.workflow.handlers.NotificationHandler;
import xtensus.workflow.handlers.TraitementEtapeSuivant;
import xtensus.workflow.handlers.TraitementStartProcessus;

public class JBPMDao {

	private static KnowledgeBase readKnowledgeBase(String idSchema)
			throws Exception {
		System.out.println("le nom de schema readKnowledgeBase"+idSchema);
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();

		
		kbuilder.add(ResourceFactory.newClassPathResource(idSchema +".bpmn"),
				ResourceType.BPMN2);
		/*
		 * kbuilder.add(ResourceFactory.newClassPathResource("casD_A_TF.bpmn"),
		 * ResourceType.BPMN2);
		 */
		return kbuilder.newKnowledgeBase();
	}

	/***** Request before node ********/

	public long requestBeforeNode(String processId, long nodeId) {

		KnowledgeBase kbase;
		long beforeNodeId = 0;
		Node node;
		Connection connectionIncoming;

		try {
			kbase = readKnowledgeBase(processId);
			StatefulKnowledgeSession ksession = kbase
					.newStatefulKnowledgeSession();
			ksession.getWorkItemManager().registerWorkItemHandler(
					"Notification", new NotificationHandler());
			WorkflowProcess process = (WorkflowProcess) ksession
					.getKnowledgeBase().getProcess(processId);

			node = process.getNode(nodeId);

			connectionIncoming = node.getIncomingConnections(
					org.jbpm.workflow.core.Node.CONNECTION_DEFAULT_TYPE).get(0);

			while (connectionIncoming.getFrom().getClass().getName()
					.contains("WorkItemNode")
					|| connectionIncoming.getFrom().getClass().getName()
							.contains("Split")
					|| connectionIncoming.getFrom().getClass().getName()
							.contains("Join")) {
				connectionIncoming = connectionIncoming
						.getFrom()
						.getIncomingConnections(
								org.jbpm.workflow.core.Node.CONNECTION_DEFAULT_TYPE)
						.get(0);

			}
			beforeNodeId = connectionIncoming.getFrom().getId();

		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage() + ". Requéte impossible !");
			beforeNodeId = nodeId;
		} catch (Exception e) {
			System.out.println("Erreur throw");
			e.printStackTrace();
		}
		return beforeNodeId;

	}

	/**** before node en String ****/
	public String requestBNode(String processId, long nodeId) {
		long beforeNode = requestBeforeNode(processId, nodeId);
		return "Before Node ID: " + beforeNode;
	}

	/***** Request next node ********/

	public long requestNextNode(String processId, long nodeId) {

		KnowledgeBase kbase;
		long nextNodeId = 0;
		Node node;
		Connection connectionOutgoing;

		try {
			kbase = readKnowledgeBase(processId);
			StatefulKnowledgeSession ksession = kbase
					.newStatefulKnowledgeSession();
			ksession.getWorkItemManager().registerWorkItemHandler(
					"Notification", new NotificationHandler());
			WorkflowProcess process = (WorkflowProcess) ksession
					.getKnowledgeBase().getProcess(processId);
			node = process.getNode(nodeId);
			connectionOutgoing = node.getOutgoingConnections(
					org.jbpm.workflow.core.Node.CONNECTION_DEFAULT_TYPE).get(0);

			while (connectionOutgoing.getTo().getClass().getName()
					.contains("WorkItemNode")
					|| connectionOutgoing.getTo().getClass().getName()
							.contains("Split")
					|| connectionOutgoing.getTo().getClass().getName()
							.contains("Join")) {
				connectionOutgoing = connectionOutgoing
						.getTo()
						.getOutgoingConnections(
								org.jbpm.workflow.core.Node.CONNECTION_DEFAULT_TYPE)
						.get(0);

			}
			nextNodeId = connectionOutgoing.getTo().getId();

		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage() + ". Requéte impossible !");
			nextNodeId = nodeId;
		} catch (Exception e) {
			System.out.println("Erreur throw");
			e.printStackTrace();
		}
		return nextNodeId;
	}

	/**** Next node en String ****/
	public String requestNNode(String processId, long nodeId) {
		long nextNodeId = requestNextNode(processId, nodeId);
		return "Next Node ID: " + nextNodeId;
	}

	/***** Start a process Instance ********/

	public String startNewProcessInstance(String processId) {

		KnowledgeBase kbase;
		long id = 0;
		try {
			kbase = readKnowledgeBase(processId);
			StatefulKnowledgeSession ksession = kbase
					.newStatefulKnowledgeSession();
			ksession.getWorkItemManager().registerWorkItemHandler(
					"Notification", new NotificationHandler());
			ProcessInstance process = (ProcessInstance) ksession
					.startProcess(processId);
			id = process.getId();

		} catch (Exception e) {
			System.out.println("Erreur throw");
			e.printStackTrace();
		}
		return "Started process instance: " + id;
	}

	/***** Request process ********/

	public String requestProcess(String processId, long etat, long etatDemande) {

		KnowledgeBase kbase = null;
		// long etatActuel = 0;
		long beforeTask = 0;
		// long nextTask=0;
		String message = null;

		try {
			kbase = readKnowledgeBase(processId);

			// etatActuel = requestBeforeNode(processId, etatDemande);
			beforeTask = requestBeforeNode(processId, etatDemande);
			// nextTask = requestNextNode(processId, etatDemande);

			if (etatDemande == requestNextNode(processId, etat)) {/*
																 * (etat)réalisé
																 * avec succés
																 */
				message = "-> Accés permis ! actual Node : "
						+ ((WorkflowProcess) kbase.getProcess(processId))
								.getNode(etat).getName()
						+ " ==>  Next Node : "
						+ ((WorkflowProcess) kbase.getProcess(processId))
								.getNode(etatDemande).getName();
				System.out.println(message);

			} else if (etatDemande == requestBeforeNode(processId, etat)) {
				message = "-> Accés interdit ! "
						+ ((WorkflowProcess) kbase.getProcess(processId))
								.getNode(etat).getName()
						+ ", est en cours de Traitement ..";
			} else {
				message = "-> Accés interdit ! "
						+ ((WorkflowProcess) kbase.getProcess(processId))
								.getNode(beforeTask).getName()
						+ ", n'est pas encore validée ..";
			}
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage() + ". Requéte impossible !");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "Process requested: " + etat + " : " + message;
	}

	/***** Start a process ********************************************************************/

	public TraitementStartProcessus startNewProcess(String processId) {
		TraitementStartProcessus resultatStart = new TraitementStartProcessus();
		KnowledgeBase kbase;
		long id = 0;
		try {
			kbase = readKnowledgeBase(processId);
			StatefulKnowledgeSession ksession = kbase
					.newStatefulKnowledgeSession();
			ksession.getWorkItemManager().registerWorkItemHandler(
					"Notification", new NotificationHandler());
			ProcessInstance process = (ProcessInstance) ksession
					.startProcess(processId);
			id = process.getId();
			TraitementEtapeSuivant resultatSuivant = new TraitementEtapeSuivant();
			resultatSuivant = requestProcessNextStepTraitement(processId, id);
			resultatStart.setIdNodeStart(resultatSuivant.getIdNodeSuivante());
			resultatStart.setEtatDebut(resultatSuivant.getEtatSuivant());
		} catch (Exception e) {
			System.out.println("Erreur throw");
			resultatStart.setIdNodeStart(id);
			resultatStart.setEtatDebut("Erreur");
			e.printStackTrace();
		}
		return resultatStart;
	}

	/**************** Request process Traitement ********/

	public TraitementEtapeSuivant requestProcessNextStepTraitement(
			String processId, long etatactuel) {

		KnowledgeBase kbase = null;
		String message = null;
		long nextTask = 0;
		// long beforeTask = 0;
		TraitementEtapeSuivant resultatSuivant = new TraitementEtapeSuivant();
		try {
			System.out.println("le nom de schema requestProcessNextStepTraitement "+processId);
			kbase = readKnowledgeBase(processId);
//			beforeTask = requestBeforeNode(processId, etat);
			nextTask = requestNextNode(processId, etatactuel);

			message = "Accés permis.";
			resultatSuivant.setResultat(true);
			resultatSuivant.setIdNodeSuivante(((WorkflowProcess) kbase
					.getProcess(processId)).getNode(nextTask).getId());
			resultatSuivant.setEtatActuelle(((WorkflowProcess) kbase
					.getProcess(processId)).getNode(etatactuel).getName());
			resultatSuivant.setEtatSuivant(((WorkflowProcess) kbase
					.getProcess(processId)).getNode(nextTask).getName());
			resultatSuivant.setCommentaire(message);
		//	

		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage() + ". Requéte impossible !");
			message = "Requéte impossible";
			resultatSuivant.setResultat(false);
			resultatSuivant.setCommentaire(message);
		} catch (Exception e) {
			message = "Requéte impossible";
			resultatSuivant.setResultat(false);
			resultatSuivant.setCommentaire(message);
			e.printStackTrace();
		}
		return resultatSuivant;
	}
	// C*
	public TraitementEtapeSuivant requestProcessBeforeStepTraitement(
			String processId, long etatactuel) {

		KnowledgeBase kbase = null;
		String message = null;
		long nextTask = 0;
		// long beforeTask = 0;
		TraitementEtapeSuivant resultatSuivant = new TraitementEtapeSuivant();
		try {
			kbase = readKnowledgeBase(processId);
			nextTask = requestBeforeNode(processId, etatactuel);
			message = "Accés permis.";
			resultatSuivant.setResultat(true);
			resultatSuivant.setIdNodeSuivante(((WorkflowProcess) kbase
					.getProcess(processId)).getNode(nextTask).getId());
			resultatSuivant.setEtatActuelle(((WorkflowProcess) kbase
					.getProcess(processId)).getNode(etatactuel).getName());
			resultatSuivant.setEtatSuivant(((WorkflowProcess) kbase
					.getProcess(processId)).getNode(nextTask).getName());
			resultatSuivant.setCommentaire(message);

		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage() + ". Requéte impossible !");
			message = "Requéte impossible";
			resultatSuivant.setResultat(false);
			resultatSuivant.setCommentaire(message);
		} catch (Exception e) {
			message = "Requéte impossible";
			resultatSuivant.setResultat(false);
			resultatSuivant.setCommentaire(message);
			e.printStackTrace();
		}
		return resultatSuivant;
	}
	// C*
	/**************** Request process Traitement ********/

	public TraitementEtapeSuivant requestProcessNodeTraitement(
			String processId, long etatactuel) {

		KnowledgeBase kbase = null;
		String message = null;
		TraitementEtapeSuivant resultatSuivant = new TraitementEtapeSuivant();
		try {
			kbase = readKnowledgeBase(processId);
			message = "Accés permis.";
			resultatSuivant.setResultat(true);
			resultatSuivant.setEtatActuelle(((WorkflowProcess) kbase
					.getProcess(processId)).getNode(etatactuel).getName());
			resultatSuivant.setCommentaire(message);

		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage() + ". Requéte impossible !");
			message = "Requéte impossible";
			resultatSuivant.setResultat(false);
			resultatSuivant.setCommentaire(message);
		} catch (Exception e) {
			message = "Requéte impossible";
			resultatSuivant.setResultat(false);
			resultatSuivant.setCommentaire(message);
			e.printStackTrace();
		}

		return resultatSuivant;
	}

	
	public String requestLastNodeInProcess(String processId){
		KnowledgeBase kbase;
		Node node = null;
		try {
			kbase = readKnowledgeBase(processId);
			StatefulKnowledgeSession ksession = kbase
					.newStatefulKnowledgeSession();
			ksession.getWorkItemManager().registerWorkItemHandler(
					"Notification", new NotificationHandler());
			WorkflowProcess process = (WorkflowProcess) ksession
					.getKnowledgeBase().getProcess(processId);
            Map<String,Object> map =   process.getMetaData();
            ArrayList<SequenceFlow> lstConnections = (ArrayList<SequenceFlow>) map.get("BPMN.Connections");
            for (SequenceFlow sequenceFlow : lstConnections) {
				  if (sequenceFlow.getName() != null && sequenceFlow.getName().equals("endingFlow")) {
					  node = process.getNode(Long.valueOf(sequenceFlow.getSourceRef().replace("_", "")));
					  break;
				}
			}
//            labelMap: for (Entry ent : map.entrySet()) {
//            	  System.out.println(ent.getKey() + " " + ent.getValue().getClass());
//            	  if (ent.getValue()  instanceof ArrayList) {
//            		  ArrayList lst = (ArrayList)ent.getValue();
//            		  System.out.println(lst.size());
//            		  for (Object object : lst) {
//            			  SequenceFlow sequenceFlow = (SequenceFlow) object;
//						  if (sequenceFlow.getName() != null && sequenceFlow.getName().equals("endingFlow")) {
//							  node = process.getNode(Long.valueOf(sequenceFlow.getSourceRef().replace("_", "")));
//							  break labelMap;
//						}
//					}
//					
//				  }
//            	 
//				
//			}
            
            //			System.out.println(process.getNodes()[process.getNodes().length - 2].getId() + " ---- " + process.getNodes()[process.getNodes().length - 2].getName());
//			for (Node nod : process.getNodes()) {
//				 connectionOutgoing = nod.getOutgoingConnections(
//						org.jbpm.workflow.core.Node.CONNECTION_DEFAULT_TYPE).get(0);
//				 System.out.println("id node : " + nod.getId() + " --- next node : " + connectionOutgoing.getTo().getId());
//				 
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return node.getName();
	}
	
	
	
	
}
