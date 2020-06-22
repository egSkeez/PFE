package xtensus.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.aspectj.apache.bcel.generic.ObjectType;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.loader.criteria.CriteriaJoinWalker;
import org.hibernate.loader.criteria.CriteriaQueryTranslator;
import org.hibernate.persister.entity.OuterJoinLoadable;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;

import xtensus.beans.common.VariableGlobale;
import xtensus.beans.utils.CourrierInformations;
//import xtensus.beans.utils.RecherchePmModel;
//import xtensus.beans.utils.RecherchePpModel;
//import xtensus.beans.utils.StatistiqueCourrierStructureByNature;
//import xtensus.beans.utils.StatistiqueCourrierUtilisateur;
import xtensus.entity.AoConsultation;
import xtensus.entity.Courrier;
import xtensus.entity.CourrierDonneeSupplementaire;
import xtensus.entity.CourrierDossier;
import xtensus.entity.Entite;
import xtensus.entity.Expdestexterne;
import xtensus.entity.Mail;
import xtensus.entity.Nature;
import xtensus.entity.Pm;
import xtensus.entity.Pp;
import xtensus.entity.Transaction;
import xtensus.entity.TransactionDestination;
//import xtensus.faxmail.beans.AttachmentFileBean;
//import xtensus.faxmail.beans.AttachmentHeadBean;
import xtensus.qualifiers.Dao;

@Dao(type = Entite.class)
public class AbstractGenericDao implements IGenericDao {
	@Autowired
	private SessionFactory sessionFactory;
	private VariableGlobale vb = new VariableGlobale();

	// private VariableGlobale vb;
	/**
	 * @return the sessiionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @param sessiionFactory
	 *            the sessiionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() {

		return sessionFactory.getCurrentSession();
	}

	public void insert(Entite entity) throws HibernateException {
		getSession().persist(entity);
	}

	public void update(Entite entity) throws HibernateException {
		getSession().saveOrUpdate(entity);

	}

	public void delete(Entite entity) throws HibernateException {
		getSession().delete(entity);

	}

	@SuppressWarnings("unchecked")
	public <T extends Entite> List<T> getList(Class<T> clazz)
			throws HibernateException {
		Criteria criteria = getSession().createCriteria(clazz);

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entite> List<T> getListUnique(Class<T> clazz)
			throws HibernateException {
		Criteria criteria = getSession().createCriteria(clazz);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Courrier> getListCourrier(String refrenceCourrier,
			String necessiteReponse, Date dateReception, String keywords,
			Date dateReponse) throws HibernateException {
		Criteria criteria = getSession().createCriteria(Courrier.class);
		if (dateReception != null) {
			criteria.add(Restrictions
					.eq("courrierDateReception", dateReception));
		}
		if (dateReponse != null) {
			criteria.add(Restrictions.eq("courrierDateReponse", dateReponse));
		}
		if (refrenceCourrier != null) {
			criteria.add(Restrictions.eq("courrierReferenceCorrespondant",
					refrenceCourrier));
		}
		if (necessiteReponse != null) {
			criteria.add(Restrictions.eq("courrierNecessiteReponse",
					necessiteReponse));
		}
		if (keywords != null) {
			criteria.add(Restrictions.eq("keywords", keywords));
		}

		List<Courrier> resultat = criteria.list();

		return resultat;

	}

	/********************** Requettes *************************/

	@SuppressWarnings("unchecked")
	public List<? extends Entite> getListWithNamedQuery(String namedQueryName)
			throws HibernateException {
		Query query = getSession().getNamedQuery(namedQueryName);
		// System.out.println(query.getQueryString());
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<? extends Entite> getListWithNamedQuery(String namedQueryName,
			Map<String, Object> parameters) throws HibernateException {
		Query query = getSession().getNamedQuery(namedQueryName);
		query.setProperties(parameters);
		// System.out.println(query.getQueryString());
		// System.out.println("|||||======> "+parameters);

		return query.list();
	}

	// **
	@Override
	public Entite getObjectWithNamedQuery(String namedQueryName,
			Map<String, Object> parameters) throws HibernateException {
		Query query = getSession().getNamedQuery(namedQueryName);
		query.setProperties(parameters);
		return (Entite) query.uniqueResult();
	}

	// **
	@SuppressWarnings("unchecked")
	@Override
	public List<? extends Entite> getListLimitedWithNamedQuery(
			String namedQueryName, int rowIndex, int numberOfRows)
			throws HibernateException {
		Query query = getSession().getNamedQuery(namedQueryName);
		query.setFirstResult(rowIndex);
		query.setMaxResults(numberOfRows);
		return query.list();
	}

	// ** KS
	@SuppressWarnings("unchecked")
	@Override
	public List<? extends Entite> getListLimitedWithNamedQuery(
			String namedQueryName, Map<String, Object> parameters,
			int rowIndex, int numberOfRows) throws HibernateException {
		Query query = getSession().getNamedQuery(namedQueryName);
		query.setProperties(parameters);
		query.setFirstResult(rowIndex);
		query.setMaxResults(numberOfRows);
		return query.list();
	}

	// ** KS

	@Override
	public void deleteWithNamedQuery(final String namedQueryName,
			Map<String, Object> parameters) throws HibernateException {
		Query query = getSession().getNamedQuery(namedQueryName);
		query.setProperties(parameters);
		query.executeUpdate();
	}

	@Override
	public void deleteWithNamedQuery(final String namedQueryName)
			throws HibernateException {
		Query query = getSession().createQuery(namedQueryName);
		query.executeUpdate();
	}

	@Override
	public void updateWithNamedQuery(final String namedQueryName,
			Map<String, Object> parameters) throws HibernateException {
		Query query = getSession().getNamedQuery(namedQueryName);
		query.setProperties(parameters);
		query.executeUpdate();
	}

	@Override
	public void updateWithNamedQuery(final String namedQueryName)
			throws HibernateException {
		Query query = getSession().createQuery(namedQueryName);
		query.executeUpdate();
	}

	@Override
	public long getRowCount(String namedQueryName) throws HibernateException {
		Query query = getSession().getNamedQuery(namedQueryName);
		return (Long) query.list().get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Transaction> testRecherheMulticritere(Integer id, String type)
			throws HibernateException {
		Criteria criteria = getSession()
				.createCriteria(Transaction.class, "tr");
		List<Integer> list = new ArrayList<Integer>();
		list.add(15);
		list.add(129);
		list.add(20);
		list.add(10000);
		criteria.add(Restrictions.eq("transactionDateTransaction", new Date()));
		if (id != null) {
			criteria.add(Restrictions.eq("urgence.urgenceId", id));
		}
		if (type != null) {
			criteria.add(Restrictions
					.eq("courrierReferenceCorrespondant", type));
		}
		List<Transaction> resultat = criteria.list();
		return resultat;
	}

	@Override
	public List<Transaction> recherheMulticritereCourrier(Integer id,
			String type, String type1, Date transactionDate,
			List<Integer> listExpediteur, List<Integer> listDossier)
			throws HibernateException {
		Criteria criteria = getSession().createCriteria(Transaction.class);

		if (id != null && type1 != null) {
			Criterion criterion = Restrictions.or(
					Restrictions.eq("transactionIdIntervenant", id),
					Restrictions.eq("transactionTypeIntervenant", type));
			criterion = Restrictions.or(criterion,
					Restrictions.eq("transactionTypeIntervenant", type1));
			criteria.add(criterion);
		}
		if (id == null && type1 != null) {
			Criterion criterion = Restrictions.or(
					Restrictions.eq("transactionTypeIntervenant", type),
					Restrictions.eq("transactionTypeIntervenant", type1));
			criteria.add(criterion);
		}
		if (id == null && type1 == null) {
			criteria.add(Restrictions.eq("transactionTypeIntervenant", type));
		}
		if (transactionDate != null) {
			criteria.add(Restrictions.eq("transactionDateTransaction",
					transactionDate));
		}
		if (!listExpediteur.isEmpty()) {
			criteria.add(Restrictions.in("expdest.idExpDest", listExpediteur));
		}
		if (!listDossier.isEmpty()) {
			criteria.add(Restrictions.in("dossier.dossierId", listDossier));
		}

		@SuppressWarnings("unchecked")
		List<Transaction> resultat = criteria.list();
		return resultat;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CourrierDossier> listDossier(String objet, String motCles,
			String necessiteReponse, Integer idTransmission, Integer idNature,
			Integer idConfidentialite, Integer idUrgence)
			throws HibernateException {
		Criteria criteria = getSession().createCriteria(Courrier.class);
		if (objet != null) {
			criteria.add(Restrictions.eq("courrierObjet", objet));
		}
		if (motCles != null) {
			criteria.add(Restrictions.eq("keywords", motCles));
		}
		if (necessiteReponse != "Tous") {
			criteria.add(Restrictions.eq("courrierNecessiteReponse",
					necessiteReponse));
		}
		if (idTransmission != null) {
			criteria.add(Restrictions.eq("transmission.transmissionId",
					idTransmission));
		}
		if (idNature != null) {
			criteria.add(Restrictions.eq("nature.natureId", idNature));
		}
		if (idConfidentialite != null) {
			criteria.add(Restrictions.eq("confidentialite.confidentialiteId",
					idConfidentialite));
		}
		if (idUrgence != null) {
			criteria.add(Restrictions.eq("urgence.urgenceId", idUrgence));
		}
		List<Courrier> resultat = criteria.list();
		List<Integer> listCourrierId = new ArrayList<Integer>();
		for (Courrier courrier : resultat) {
			listCourrierId.add(courrier.getIdCourrier());
		}
		Criteria criteria1 = getSession().createCriteria(CourrierDossier.class);
		criteria1.add(Restrictions.in("id.idCourrier", listCourrierId));

		return criteria1.list();
	}

	@Override
	public long countList(String namedQueryName, Map<String, Object> parameters)
			throws HibernateException {

		return 0;
	}

	// ** KS
	public Long count(final String namedQueryName,
			final Map<String, Object> parameters) throws HibernateException {
		Query query = getSession().getNamedQuery(namedQueryName);
		query.setProperties(parameters);

		return (Long) query.uniqueResult();
	}

	// ** KS
	@SuppressWarnings("unchecked")
	@Override
	public List<Courrier> listDossier(String objet, String motCles,
			String description, String necessiteReponse,
			Integer idTransmission, Integer idNature,
			Integer idConfidentialite, Integer idUrgence)
			throws HibernateException {
		List<Courrier> resultat;
		Criteria criteria = getSession().createCriteria(Courrier.class);
		if (!objet.equals("")) {
			criteria.add(Restrictions.like("courrierObjet", objet,
					MatchMode.ANYWHERE));
		}
		if (!motCles.equals("")) {
			criteria.add(Restrictions.like("keywords", motCles,
					MatchMode.ANYWHERE));
		}
		if (!description.equals("")) {
			criteria.add(Restrictions.like("courrierCommentaire", description,
					MatchMode.ANYWHERE));
		}
		if (!necessiteReponse.equals("Tous")) {
			criteria.add(Restrictions.eq("courrierNecessiteReponse",
					necessiteReponse));
		}
		if (idTransmission != null) {
			criteria.add(Restrictions.eq("transmission.transmissionId",
					idTransmission));
		}
		if (idNature != null) {
			criteria.add(Restrictions.eq("nature.natureId", idNature));
		}
		if (idConfidentialite != null) {
			criteria.add(Restrictions.eq("confidentialite.confidentialiteId",
					idConfidentialite));
		}
		if (idUrgence != null) {
			criteria.add(Restrictions.eq("urgence.urgenceId", idUrgence));
		}
		if (objet.equals("") && motCles.equals("") && description.equals("")
				&& necessiteReponse.equals("Tous") && idTransmission == null
				&& idNature == null && idConfidentialite == null
				&& idUrgence == null) {
			resultat = new ArrayList<Courrier>();
		} else {
			resultat = new ArrayList<Courrier>();
			resultat = criteria.list();
		}
		return resultat;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CourrierDossier> listCourrierDossier(List<Integer> list)
			throws HibernateException {
		List<CourrierDossier> resultat = new ArrayList<CourrierDossier>();
		Criteria criteria = getSession().createCriteria(CourrierDossier.class);
		criteria.add(Restrictions.in("id.idCourrier", list));
		resultat = criteria.list();
		return resultat;
	}

	// @SuppressWarnings("unchecked")
	// @Override
	// public List<Transaction> recherheMulticritereCourrierEnvoye(Integer
	// idBoc,
	// Integer id, String type, String type1, Date transactionDateDebut,
	// Date transactionDateFin, Date dateReponse,
	// List<Integer> listExpediteur, List<Integer> listDossier,
	// List<Integer> listDestinataire) throws HibernateException {
	// Criteria criteria = getSession().createCriteria(Transaction.class);
	// if (idBoc != null) {
	// criteria.add(Restrictions.eq("idUtilisateur", idBoc));
	// }
	// if (id != null && type1 != null) {
	// Criterion criterion = Restrictions.or(
	// Restrictions.eq("transactionIdIntervenant", id),
	// Restrictions.eq("transactionTypeIntervenant", type));
	// criterion = Restrictions.or(criterion,
	// Restrictions.eq("transactionTypeIntervenant", type1));
	// criteria.add(criterion);
	// }
	// if (id == null && type1 != null) {
	// Criterion criterion = Restrictions.or(
	// Restrictions.eq("transactionTypeIntervenant", type),
	// Restrictions.eq("transactionTypeIntervenant", type1));
	// criteria.add(criterion);
	// }
	// if (id == null && type1 == null && type != null) {
	// criteria.add(Restrictions.eq("transactionTypeIntervenant", type));
	// }
	// if (transactionDateDebut != null && transactionDateFin == null) {
	// criteria.add(Restrictions.eq("transactionDateTransaction",
	// transactionDateDebut));
	// }
	// if (transactionDateDebut != null && transactionDateFin != null) {
	// criteria.add(Restrictions.between("transactionDateTransaction",
	// transactionDateDebut, transactionDateFin));
	// }
	// if (dateReponse != null) {
	// criteria.add(Restrictions.eq("transactionDateReponse", dateReponse));
	// }
	// if (!listExpediteur.isEmpty()) {
	// criteria.add(Restrictions.in("expdest.idExpDest", listExpediteur));
	// }
	// if (!listDossier.isEmpty()) {
	// criteria.add(Restrictions.in("dossier.dossierId", listDossier));
	// }
	// if (!listDestinataire.isEmpty()) {
	// criteria.add(Restrictions.in("transactionId", listDestinataire));
	// }
	//
	// List<Transaction> resultat = criteria.list();
	// return resultat;
	// }

	@SuppressWarnings("unchecked")
	@Override
	public List<CourrierInformations> recherheMulticritereCourrierEnvoye(
			boolean isResponsable, List<Integer> listIdsSousUnites,
			List<Integer> listIdSubordonnes, String secretaire, String sub,
			String unit, String type, String type1, String typeSecretaire,
			Integer idUser, String object, String motCle,
			Map<Integer, String> listExp, Map<Integer, String> listDes,
			Integer idTransmission, Integer idNature, Integer idCategorie,
			Date dateLimiteRep, List<Integer> listIdAnnotation,
			String description, Integer idConfidentialite, Integer idUrgence,
			Date transactionDateDebut, Date transactionDateFin,
			Date dateCourrierReel, Date dateCourrierReelFin, boolean isBoc,
			String typeCourrierBoc, String recuOrEnvoyer,
			List<Integer> listDestinataire, int firstIndex, int maxResult,
			String oldRef,Integer refGeneral, String necessiteReponse, String courrierReference,
			List<String> annees, String DBType, int etatCloturer,
			String courrierCopyTransfere, String courrierCopy, String colonne1,
			String colonne2, String colonne3, String colonne4, String colonne5,
			String colonne6, String colonne7, String colonne8, String colonne9,
			String colonne10, String colonne11, String colonne12,
			String colonne13, Date colonne14, Date colonne15, Date colonne16,
			Date colonne17, Date colonne18, Date colonne19, Date colonne20,
			String colonne21, Boolean colonne22, String courrierFlagInterne,List<Integer> listIdBocMembers)
			throws HibernateException {

		List<String> listRecuEnv = new ArrayList<String>();

		// Courriers de mes subordonnés
		if (isResponsable) {
			if (sub.equals("Oui")) {
				for (Integer idSub : listIdSubordonnes) {
					listRecuEnv.add("sub_" + idSub);
				}
			}
			// Courriers de mes sous unités
			if (unit.equals("Oui")) {
				for (Integer idSousUnit : listIdsSousUnites) {
					listRecuEnv.add("unit_" + idSousUnit);
				}
			}
			if (secretaire.equals("Oui")) {
				listRecuEnv.add(typeSecretaire);
			}
		}
		// Mes Courriers et les courriers de mon unité
		listRecuEnv.add(type);
		listRecuEnv.add(type1);
		Map<String, Object> pars = new HashMap<String, Object>();
		StringBuffer trMax = new StringBuffer(
				"SELECT MAX(tmax.transactionId) AS 'transactionId'"
						+ // , dmax.dossierId
						" FROM transactionn tmax"
						+ " INNER JOIN dossier dmax ON tmax.dossierId = dmax.dossierId"
						+ " INNER JOIN courrierdossier cdmax ON dmax.dossierId = cdmax.dossierId"
						+ " INNER JOIN courrier cmax ON cdmax.idCourrier = cmax.idCourrier"
						+ " INNER JOIN transactionn tm ON tmax.transactionFirst = tm.transactionId"
						+ " LEFT JOIN courrierdonneesupplementaire cds ON cdmax.idCourrier = cds.idCourrier");

		if (!listExp.isEmpty()) {
			trMax.append(" INNER JOIN transactionn t_all ON tmax.transactionFirst = t_all.transactionFirst"
					+ " LEFT JOIN expdest e_c ON t_all.idExpDest = e_c.idExpDest");
		}
		// System.out.println("[JS]==>isBoc : " + isBoc);
		// System.out.println("[JS]==>typeCourrierBoc : " + typeCourrierBoc);

		
		if (isBoc) {
			if (typeCourrierBoc.equals("A")) {
				trMax.append(" INNER JOIN expdest emax ON tmax.idExpDest = emax.idExpDest");
			}
			if (typeCourrierBoc.equals("D")) {
				trMax.append(" INNER JOIN expdest emin ON tm.idExpDest = emin.idExpDest");
			}
		}
		if (listIdAnnotation.size() > 0) {
			trMax.append(" LEFT JOIN transactionannotation tamax ON tamax.idTransaction = tmax.transactionId");
		}
		if (listDes.size() > 0) {
			trMax.append(" LEFT JOIN transactiondestinationreelle tdrmax ON tdrmax.transactionDestinationReelleId = tmax.transactionDestinationReelleId");
		}
		if (!isBoc && listRecuEnv.size() > 0
				&& !recuOrEnvoyer.equals("envoyes")) {
			trMax.append(" LEFT JOIN transactiondest tdmax ON tdmax.idTransaction = tmax.transactionId");
		}
		trMax.append(" WHERE cmax.courrierType != 'V' AND dmax.typeDossierId = 1 AND tmax.etatId != 99");

		System.out.println("type===================> "+type);
		if (!(type != null && type.equals("boc_100"))) {
			if(typeCourrierBoc!=null){
			if (!typeCourrierBoc.equals("A")
					&& !typeCourrierBoc.equals("D")) {
				int j = 0;
				
				String whereMax1 = "";
				trMax.append(" AND (");

				for (Integer id : listIdBocMembers) {
					System.out.println("ID : " + id);
					whereMax1 += "tmax.idUtilisateur = :id" + j + " OR ";
					pars.put("id" + j, id);
					j++;
					System.out.println("############ id == " + id);
				}
				System.out.println("############2 type == " + type);
				trMax.append(whereMax1);
				// ça était tm.transactionId
				StringBuffer trDest = new StringBuffer(
						"tmax.transactionId IN (SELECT tdes.idTransaction"
								+ " FROM transactiondest tdes"
								+ " WHERE tdes.transactionDestTypeIntervenant = :type))");
				// System.out.println("typeeee : " + type);
				pars.put("type", type);
				trMax.append(trDest);
			}
			}
		}
		
		
		
		
		int i = 0;
		String expDest = "";
		String whereMax = "";
		String whereAnnees = "";
		System.out.println("<<<<<<<<<<<<<<<<<<<<  annees  " + annees);
		if (annees != null) {
			if (annees.size() > 0) {
				trMax.append(" AND cmax.courrierDateReceptionAnnee IN (");
				for (String an : annees) {
					whereAnnees += ":an" + i + ", ";
					pars.put("an" + i, Integer.valueOf(an));
					i++;
				}
				whereAnnees = whereAnnees
						.substring(0, whereAnnees.length() - 2);
				trMax.append(whereAnnees + ")");
			}
		}
		if (isBoc) {
			// Arrivé Départ
			if (typeCourrierBoc.equals("A")) {
				trMax.append(" AND emax.typeExpDest LIKE 'Externe'");
			}
			if (typeCourrierBoc.equals("D")) {
				trMax.append(" AND emin.typeExpDest LIKE 'Interne-%'");
			}
			trMax.append("AND cmax.idTransmission!=10 ");
		} else if (listRecuEnv.size() > 0) {
			for (String ed : listRecuEnv) {
				expDest += ":ed" + i + ", ";
				pars.put("ed" + i, ed);
				i++;
			}
			expDest = expDest.substring(0, expDest.length() - 2);
			if (recuOrEnvoyer.equals("recus")) {
				whereMax += " AND tdmax.transactionDestTypeIntervenant IN ("
						+ expDest + ")";
			} else if (recuOrEnvoyer.equals("envoyes")) {
				whereMax += " AND tmax.transactionTypeIntervenant IN ("
						+ expDest + ")";
			} else if (recuOrEnvoyer.equals("tous") || recuOrEnvoyer.equals("")) {
				whereMax += " AND (tmax.transactionTypeIntervenant IN ("
						+ expDest + ")"
						+ " OR tdmax.transactionDestTypeIntervenant IN ("
						+ expDest + "))";
			}
		}
		trMax.append(whereMax);

		// les criteres specifiés au niveau du recherche
		// Expediteur Reel
		String whereExpList = "";
		if (!listExp.isEmpty()) {
			Iterator<Integer> iterE = listExp.keySet().iterator();
			while (iterE.hasNext()) {
				Integer expId = iterE.next();
				String expType = listExp.get(expId);
				// System.out.println(expId + " = " + expType);
				if (expType.equals("Externe")) {
					whereExpList += " e_c.idExpDestExterne = :expId" + i
							+ " OR ";
				} else if (expType.equals("Interne-Unité")) {
					whereExpList += "(:expType"
							+ i
							+ " = 'Interne-Unité' AND e_c.idExpDestLdap = :expId"
							+ i + ") OR ";
					pars.put("expType" + i, expType);
				} else if (expType.equals("Interne-Person")) {
					whereExpList += "(:expType"
							+ i
							+ " = 'Interne-Person' AND e_c.idExpDestLdap = :expId"
							+ i + ") OR ";
					pars.put("expType" + i, expType);
				}
				pars.put("expId" + i, expId);
				i++;
			}
			whereExpList = " AND "
					+ whereExpList.substring(0, whereExpList.length() - 3);
			trMax.append(whereExpList);
		}
		// destinataire
		String whereDestList = "";
		String hqlDest0 = "";
		if (!listDes.isEmpty()) {

			Iterator<Integer> iterD0 = listDes.keySet().iterator();
			while (iterD0.hasNext()) {
				Integer destId0 = iterD0.next();
				String destType0 = listDes.get(destId0);
				System.out.println("*******************************" + destId0
						+ " = " + destType0);

				if (destType0.equals("Externe")) {

					hqlDest0 = " AND ( (tdrmax.transactionDestinationReelleId IS NULL"
							+ " AND tmax.transactionId IN (SELECT td_d.idTransaction"
							+ " FROM transactiondest td_d"
							+ " LEFT JOIN expdest e_d ON td_d.idExpDest = e_d.idExpDest"
							+ " WHERE ";
				} else {
					hqlDest0 = " AND (tdrmax.transactionDestinationReelleIdDestinataire = :destId"
							+ i
							+ " OR (tdrmax.transactionDestinationReelleId IS NULL"
							+ " AND tmax.transactionId IN (SELECT td_d.idTransaction"
							+ " FROM transactiondest td_d"
							+ " LEFT JOIN expdest e_d ON td_d.idExpDest = e_d.idExpDest"
							+ " WHERE ";
				}
			}

			StringBuffer hqlDest = new StringBuffer(hqlDest0);

			Iterator<Integer> iterD = listDes.keySet().iterator();
			while (iterD.hasNext()) {
				Integer destId = iterD.next();
				String destType = listDes.get(destId);
				System.out.println(destId + " = " + destType);
				if (destType.equals("Externe")) {
					whereDestList += " e_d.idExpDestExterne = :destId" + i
							+ " OR ";
				} else if (destType.equals("Interne-Unité")) {
					whereDestList += "(:destType"
							+ i
							+ " = 'Interne-Unité' AND e_d.idExpDestLdap = :destId"
							+ i + ") OR ";
					pars.put("destType" + i, destType);
				} else if (destType.equals("Interne-Person")) {
					whereDestList += "(:destType"
							+ i
							+ " = 'Interne-Person' AND e_d.idExpDestLdap = :destId"
							+ i + ") OR ";
					pars.put("destType" + i, destType);
				}
				pars.put("destId" + i, destId);
				i++;
			}
			whereDestList = whereDestList.substring(0,
					whereDestList.length() - 3);
			hqlDest.append(whereDestList + ")))");
			trMax.append(hqlDest);
		}
		if (!object.equals("")) {
			trMax.append(" AND cmax.courrierObjet LIKE :object");
			pars.put("object", '%' + object + '%');
		}
		if (2 != etatCloturer) {
			trMax.append(" AND cmax.courrierEtatCloture = :etatCloturer");
			// TODO ceci utiliser dans le cas d'un courrier cloturer
			pars.put("etatCloturer", etatCloturer);
		}
		if (courrierCopyTransfere != null) {
			if (!courrierCopyTransfere.equals("")) {
				trMax.append(" AND cmax.courrierCopyTransfere LIKE :courrierCopyTransfere");

				pars.put("courrierCopyTransfere",
						'%' + courrierCopyTransfere + '%');

			}
		}
		if (courrierFlagInterne == null) {
			System.out.println("XTE : null");
		} else {

			trMax.append(" AND cmax.courrierFlagInterne LIKE :courrierFlagInterne");

			pars.put("courrierFlagInterne", '%' + courrierFlagInterne + '%');
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println("by courrierFlagInterne" + courrierFlagInterne);
			System.out.println();
			System.out.println();
			System.out.println();

		}
		if (!motCle.equals("")) {
			trMax.append(" AND cmax.keywords LIKE :motCle");
			pars.put("motCle", '%' + motCle + '%');
		}
		if (courrierReference != null) {
			if (!courrierReference.equals("")) {
				trMax.append(" AND cmax.courrierReferenceCorrespondant = :courrierReference");
				pars.put("courrierReference", courrierReference);
			}
		}
		if (courrierCopy != null) {
			if (!courrierCopy.equals("")) {
				trMax.append(" AND cmax.courrierCopyTransfere = :courrierCopy");
				pars.put("courrierCopy", courrierCopy);
			}
		}
		if (description != null) {
			if (!description.equals("")) {
				trMax.append(" AND cmax.courrierCommentaire LIKE :description");
				pars.put("description", '%' + description + '%');
			}
		}
		if (idTransmission != null) {
			trMax.append(" AND cmax.idTransmission = :idTransmission");
			pars.put("idTransmission", idTransmission);
		}
		if (idNature != null) {
			System.out.println("If idNature et IdCategorie not null");
			trMax.append(" AND cmax.idNature = :idNature");
			pars.put("idNature", idNature);
		}
		if (idConfidentialite != null) {
			trMax.append(" AND cmax.idConfidentialite = :idConfidentialite");
			pars.put("idConfidentialite", idConfidentialite);
		}
		if (idUrgence != null) {
			trMax.append(" AND cmax.idUrgence = :idUrgence");
			pars.put("idUrgence", idUrgence);
		}
		System.out.println("old ref ==>"+oldRef);
		if (oldRef != null && oldRef.length()>0) {
			trMax.append(" AND cmax.courrierRefOriginale LIKE :oldRef");
			pars.put("oldRef", '%' + oldRef + '%');
		}
		if (refGeneral!= null && refGeneral.intValue() != 0) {
			trMax.append(" AND cmax.idCourrier = :refGeneral");
			pars.put("refGeneral", refGeneral);
		}
		
		
		if (idCategorie != null) {
			trMax.append(" AND cmax.idNature IN (SELECT n.natureId FROM nature n where n.natureCategorieId = :idCategorie)");
			pars.put("idCategorie", idCategorie);
		}
		System.out.println("##### colonne 1 :" + colonne1);
		System.out.println("##### colonne 7 :" + colonne7);
		System.out.println("##### colonne 8 :" + colonne8);
		System.out.println("##### colonne 9 :" + colonne9);
		System.out.println("##### idNature :" + idNature);
		System.out.println("##### idCategorie :" + idCategorie);
		// if ((idNature != null) && (idCategorie != null)) {
		
		System.out.println("##### Dans test 00");
		if (colonne1 != null && !colonne1.equals("")) {
			System.out.println("##### I m Colonne 1");
			System.out.println("colonne 1 :" + colonne1);
			trMax.append(" AND cds.colonne1 LIKE :colonne1");
			pars.put("colonne1", '%' + colonne1 + '%');

		}
		if (colonne2 != null && !colonne2.equals("")) {
			System.out.println("I m Colonne 2");
			trMax.append(" AND cds.colonne2 LIKE :colonne2");
			pars.put("colonne2", '%' + colonne2 + '%');

		}
		if (colonne3 != null && !colonne3.equals("")) {
			System.out.println("I m Colonne 3");
			trMax.append(" AND cds.colonne3 LIKE :colonne3");
			pars.put("colonne3", '%' + colonne3 + '%');

		}
		if (colonne4 != null && !colonne4.equals("")) {
			System.out.println("I m Colonne 4");
			trMax.append(" AND cds.colonne4 LIKE :colonne4");
			pars.put("colonne4", '%' + colonne4 + '%');

		}
		if (colonne5 != null && !colonne5.equals("")) {
			System.out.println("I m Colonne 5");
			trMax.append(" AND cds.colonne5 LIKE :colonne5");
			pars.put("colonne5", '%' + colonne5 + '%');

		}
		if (colonne6 != null && !colonne6.equals("")) {
			System.out.println("I m Colonne 6");
			trMax.append(" AND cds.colonne6 LIKE :colonne6");
			pars.put("colonne6", '%' + colonne6 + '%');

		}
		System.out.println("##### colonne 7 :" + colonne7);
		if (colonne7 != null && !colonne7.equals("")) {
			System.out.println("I m Colonne 7");
			trMax.append(" AND cds.colonne7 LIKE :colonne7");
			pars.put("colonne7", '%' + colonne7 + '%');

		}
		System.out.println("##### colonne 8 :" + colonne8);
		if (colonne8 != null && !colonne8.equals("")) {
			System.out.println("I m Colonne 8");
			trMax.append(" AND cds.colonne8 LIKE :colonne8");
			pars.put("colonne8", '%' + colonne8 + '%');

		}
		System.out.println("##### colonne 9 :" + colonne9);
		if (colonne9 != null && !colonne9.equals("")) {
			System.out.println("I m Colonne 9");
			trMax.append(" AND cds.colonne9 LIKE :colonne9");
			pars.put("colonne9", '%' + colonne9 + '%');

		}
		if (colonne10 != null && !colonne10.equals("")) {
			System.out.println("I m Colonne 10");
			trMax.append(" AND cds.colonne10 LIKE :colonne10");
			pars.put("colonne10", '%' + colonne10 + '%');

		}
		if (colonne11 != null && !colonne11.equals("")) {
			System.out.println("I m Colonne 11");
			trMax.append(" AND cds.colonne11 LIKE :colonne11");
			pars.put("colonne11", '%' + colonne11 + '%');

		}
		if (colonne12 != null && !colonne12.equals("")) {
			System.out.println("I m Colonne 12");
			trMax.append(" AND cds.colonne12 LIKE :colonne12");
			pars.put("colonne12", '%' + colonne12 + '%');

		}
		if (colonne13 != null && !colonne13.equals("")) {
			System.out.println("I m Colonne 13");
			trMax.append(" AND cds.colonne12 LIKE :colonne13");
			pars.put("colonne12", '%' + colonne12 + '%');

		}

		if (colonne14 != null && !colonne14.equals("")) {
			System.out.println("I m Colonne 14");
			trMax.append(" AND cds.colonne14 LIKE :colonne14");
			pars.put("colonne14", colonne14);

		}

		if (colonne15 != null && !colonne15.equals("")) {
			System.out.println("I m Colonne 15");
			trMax.append(" AND cds.colonne15 LIKE :colonne15");
			pars.put("colonne15", colonne15);

		}

		if (colonne16 != null && !colonne16.equals("")) {
			System.out.println("I m Colonne 16");
			trMax.append(" AND cds.colonne16 = :colonne16");
			pars.put("colonne16", colonne16);
		}

		if (colonne17 != null && !colonne17.equals("")) {
			System.out.println("I m Colonne 17");
			trMax.append(" AND cds.colonne17 = :colonne17");
			pars.put("colonne17", colonne17);

		}

		if (colonne18 != null && !colonne18.equals("")) {
			System.out.println("I m Colonne 18");
			trMax.append(" AND cds.colonne18 = :colonne18");
			pars.put("colonne18", colonne18);

		}

		if (colonne19 != null && !colonne19.equals("")) {
			System.out.println("I m Colonne 19");
			trMax.append(" AND cds.colonne19 = :colonne19");
			pars.put("colonne19", colonne19);

		}

		if (colonne20 != null && !colonne20.equals("")) {
			System.out.println("I m Colonne 20");
			trMax.append(" AND cds.colonne20 = :colonne20");
			pars.put("colonne17", colonne17);

		}
		if (colonne21 != null && !colonne21.equals("")) {
			System.out.println("I m Colonne 21");
			trMax.append(" AND cds.colonne21 LIKE :colonne21");
			pars.put("colonne21", '%' + colonne21 + '%');

		}

		if (colonne22 != null && !colonne22.equals("")) {
			System.out.println("I m Colonne 22");
			trMax.append(" AND cds.colonne22 = :colonne22");
			// pars.put("colonne22" + colonne22 + '%');

		}
		// }

		// Annotaion
		if (!listIdAnnotation.isEmpty()) {
			String whereAnn = "";
			trMax.append(" AND tamax.idAnnotation IN (");
			for (Integer ann : listIdAnnotation) {
				whereAnn += ":ann" + i + ", ";
				pars.put("ann" + i, ann);
				i++;
			}
			trMax.append(whereAnn.substring(0, whereAnn.length() - 2) + ")");
		}

		if (transactionDateDebut != null && transactionDateFin == null) {
			trMax.append(" AND DATE_FORMAT(cmax.courrierDateReception,'%Y-%m-%d' )= :transactionDateDebut");
			pars.put("transactionDateDebut", transactionDateDebut);
		}
		if (transactionDateDebut != null && transactionDateFin != null) {
			trMax.append(" AND cmax.courrierDateReception BETWEEN :transactionDateDebut AND :transactionDateFin");
			pars.put("transactionDateDebut", transactionDateDebut);
			pars.put("transactionDateFin", transactionDateFin);
		}
		if (dateCourrierReel != null && dateCourrierReelFin == null) {
			trMax.append(" AND DATE_FORMAT(cmax.courrierDateCourrierReelle,'%Y-%m-%d' )= :dateCourrierReel");
			pars.put("dateCourrierReel", dateCourrierReel);

		}
		if (dateCourrierReel != null && dateCourrierReelFin != null) {
			trMax.append(" AND cmax.courrierDateCourrierReelle BETWEEN :dateCourrierReel AND :dateCourrierReelFin");
			pars.put("dateCourrierReel", dateCourrierReel);
			pars.put("dateCourrierReelFin", dateCourrierReelFin);
		}
		if (dateLimiteRep != null) {
			trMax.append(" AND tmax.transactionDateReponse = :dateLimiteRep");
			pars.put("dateLimiteRep", dateLimiteRep);
		} else if (necessiteReponse.toLowerCase().equals("oui")) {
			trMax.append(" AND cmax.courrierNecessiteReponse = 'oui'");
		} else if (necessiteReponse.toLowerCase().equals("non")) {
			trMax.append(" AND cmax.courrierNecessiteReponse = 'non'");
		}

		trMax.append(" GROUP BY dmax.dossierId"
				+ " ORDER BY dmax.dossierId DESC");
		if (DBType.contains("mysql")) {
			trMax.append(" LIMIT " + firstIndex + ", " + maxResult);
		}

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		Iterator<String> iterMax = pars.keySet().iterator();
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			
			System.out.println("JS name ===> "+name);
			System.out.println("JS value ===> "+value);
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}
		if (DBType.contains("sqlserver")) {
			queryMax.setFirstResult(firstIndex);
			queryMax.setMaxResults(maxResult);
		}

		System.out.println(queryMax.getQueryString());
		List<Integer> maxIds = queryMax.list();

		Map<String, Object> params = new HashMap<String, Object>();
		int imax = 0;
		String where = " t.transactionId IN (";
		// String whereD = " AND tm1.dossierId IN (";
		if (maxIds.size() > 0) {
			for (Integer ids : maxIds) {
				Integer idTrans = ids;
				where += ":idTrans" + imax + ", ";
				params.put("idTrans" + imax, idTrans);
				imax++;
			}
		} else {
			return new ArrayList<CourrierInformations>();
		}

		StringBuffer hql = new StringBuffer(
//				"SET group_concat_max_len=100000; " +
				" SELECT DISTINCT c.idCourrier AS 'courrierID',"
						+ " c.courrierObjet AS 'courrierObjet',"
						+ " c.courrierReferenceCorrespondant AS 'courrierReference',"
						+ " c.courrierCommentaire AS 'courrierCommentaire',"
						+ " c.courrierOldNum AS 'courrierOldNum',"
						+ " c.courrierCircuit AS 'courrierCircuit',"
						+ " d.dossierId AS 'dossierID',"
						+ " n.natureLibelle AS 'courrierNature',"
						+ " t.etatId AS 'etatID',"
						+ " c.courrierDateReception AS 'courrierDateReceptionEnvoi',"
						+ " t.transactionDateConsultation AS 'transactionDateConsultation',"
						+ " t.transactionId AS 'transactionID',"
						+ " t.idExpDest AS 'expID',"
						+ " t.transactionOrdre AS 'transactionOrdre',"
						+ " t.idUtilisateur AS 'idUtilisateur',"
						+ " t_tdr.transactionDestinationReelleIdDestinataire AS 'transactionDestinationReelID',"
						+ " t_tdr.transactionDestinationReelleTypeDestinataire AS 'transactionDestinationReelleTypeDestinataire',"
						+ " e.typeExpDest AS 'expTypeOld',"
						+ " e.idExpDestLdap AS 'expLdapOld',"
						+ " ee.Exp_Dest_ExterneNom AS 'expNomOld',"
						+ " ee.Exp_Dest_ExternePrenom AS 'expPrenomOld',"
						+ " ee.typeUtilisateurId AS 'expTypeUserOld',"
						+ " tm.transactionId AS 'minTransactionID',"
						+ " em.typeExpDest AS 'expType',"
						+ " em.idExpDestLdap AS 'expLdap',"
						+ " eem.Exp_Dest_ExterneNom AS 'expNom',"
						+ " eem.Exp_Dest_ExternePrenom AS 'expPrenom',"
						+ " eem.typeUtilisateurId AS 'expTypeUser',");
		if (DBType.contains("sqlserver")) {
			hql.append(" STUFF((SELECT '|' + CONVERT(NVARCHAR(10), t3.transactionId)"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), e3.idExpDest), '')"
					+ " + ';' + ISNULL(e3.typeExpDest, '')"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), e3.idExpDestLdap), '')"
					+ " + ';' + ISNULL(ee3.Exp_Dest_ExterneNom, '')"
					+ " + ';' + ISNULL(ee3.Exp_Dest_ExternePrenom, '')"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), ee3.typeUtilisateurId), '')"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), tdr.transactionDestinationReelleIdDestinataire), '')"
					+ " + ';' + ISNULL(tdr.transactionDestinationReelleTypeDestinataire, '')"
					+ " FROM transactionn t3 "
					+ " LEFT JOIN transactiondest td3 ON t3.transactionId = td3.idTransaction"
					+ " LEFT JOIN transactiondestinationreelle tdr ON t3.transactionDestinationReelleId = tdr.transactionDestinationReelleId"
					+ " LEFT JOIN expdest e3 ON td3.idExpDest = e3.idExpDest"
					+ " LEFT JOIN expdestexterne ee3 ON e3.idExpDestExterne = ee3.idExpDestExterne"
					+ " WHERE t3.dossierId = t.dossierId"
					+ " FOR XML PATH('')), 1, 1, '') AS 'destReelList'");
		}
		if (DBType.contains("mysql")) {
			hql.append("(SELECT GROUP_CONCAT(CONCAT(CONVERT(t3.transactionId, CHAR(10))"
					+ " , ';' , IFNULL(CONVERT(e3.idExpDest, CHAR(10)), '')"
					+ " , ';' , IFNULL(e3.typeExpDest, '')"
					+ " , ';' , IFNULL(CONVERT(e3.idExpDestLdap, CHAR(10)), '')"
					+ " , ';' , IFNULL(ee3.Exp_Dest_ExterneNom, '')"
					+ " , ';' , IFNULL(ee3.Exp_Dest_ExternePrenom, '')"
					+ " , ';' , IFNULL(CONVERT(ee3.typeUtilisateurId, CHAR(10)), '')"
					+ " , ';' , IFNULL(CONVERT(tdr.transactionDestinationReelleIdDestinataire, CHAR(10)), '')"
					+ " , ';' , IFNULL(tdr.transactionDestinationReelleTypeDestinataire, '')) SEPARATOR '|')"
					+ " FROM transactionn t3 "
					+ " LEFT JOIN transactiondest td3 ON t3.transactionId = td3.idTransaction"
					+ " LEFT JOIN transactiondestinationreelle tdr ON t3.transactionDestinationReelleId = tdr.transactionDestinationReelleId"
					+ " LEFT JOIN expdest e3 ON td3.idExpDest = e3.idExpDest"
					+ " LEFT JOIN expdestexterne ee3 ON e3.idExpDestExterne = ee3.idExpDestExterne"
					+ " WHERE t3.dossierId = t.dossierId) AS 'destReelList'");
		}
		hql.append(" FROM transactionn t"
				+ " INNER JOIN dossier d ON t.dossierId = d.dossierId"
				+ " INNER JOIN courrierdossier cd ON d.dossierId = cd.dossierId"
				+ " INNER JOIN courrier c ON cd.idCourrier = c.idCourrier"
				+ " INNER JOIN nature n ON c.idNature = n.natureId"
				+ " LEFT JOIN transactionannotation ta ON t.transactionId = ta.idTransaction"
				+ " LEFT JOIN transactiondest td ON t.transactionId = td.idTransaction"
				+ " LEFT JOIN expdest e ON t.idExpDest = e.idExpDest"
				+ " LEFT JOIN expdestexterne ee ON e.idExpDestExterne = ee.idExpDestExterne"
				+ " LEFT JOIN transactiondestinationreelle t_tdr ON t.transactionDestinationReelleId = t_tdr.transactionDestinationReelleId"
				+ " INNER JOIN transactionn tm ON t.transactionFirst = tm.transactionId"
				+ " LEFT JOIN expdest em ON tm.idExpDest = em.idExpDest"
				+ " LEFT JOIN expdestexterne eem ON em.idExpDestExterne = eem.idExpDestExterne"
				+ " WHERE " + where.substring(0, where.length() - 2) + ")"
				+ " ORDER BY c.idCourrier DESC");
		// Fin Restrictions

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		query.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));

		query.addScalar("courrierID", new IntegerType());
		query.addScalar("courrierObjet", new StringType());
		query.addScalar("courrierReference", new StringType());
		query.addScalar("courrierCommentaire", new StringType());
		query.addScalar("courrierOldNum", new IntegerType());
		query.addScalar("courrierCircuit", new StringType());
		query.addScalar("dossierID", new IntegerType());
		query.addScalar("courrierNature", new StringType());
		query.addScalar("etatID", new IntegerType());
		query.addScalar("courrierDateReceptionEnvoi", new DateType());
		query.addScalar("transactionDateConsultation", new DateType());
		query.addScalar("transactionID", new IntegerType());
		query.addScalar("expID", new IntegerType());
		query.addScalar("transactionOrdre", new IntegerType());
		query.addScalar("idUtilisateur", new IntegerType());
		query.addScalar("transactionDestinationReelID", new IntegerType());
		query.addScalar("transactionDestinationReelleTypeDestinataire",
				new StringType());
		query.addScalar("expLdapOld", new IntegerType());
		query.addScalar("expTypeOld", new StringType());
		query.addScalar("expNomOld", new StringType());
		query.addScalar("expPrenomOld", new StringType());
		query.addScalar("expTypeUserOld", new IntegerType());
		query.addScalar("minTransactionID", new IntegerType());
		query.addScalar("expLdap", new IntegerType());
		query.addScalar("expType", new StringType());
		query.addScalar("expNom", new StringType());
		query.addScalar("expPrenom", new StringType());
		query.addScalar("expTypeUser", new IntegerType());
		query.addScalar("destReelList", new StringType());

		Iterator<String> iter = params.keySet().iterator();
		while (iter.hasNext()) {
			String name = iter.next();
			Object value = params.get(name);
			// System.out.println(name + " = " + value);
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}

		System.out
				.println("++++++++++++++++++++++++++++++++////////////////////////////////+++++++++++++++++++++++++++++++++++"
						+ query.getQueryString());
		return query.list();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	
	public List recherheMulticritereCount(boolean isResponsable, boolean isBoc,
			List<Integer> listIdsSousUnites, List<Integer> listIdSubordonnes,
			String secretaire, String sub, String unit, String type,
			String type1, String typeSecretaire, Integer idUser, String object,
			String motCle, Map<Integer, String> listExp,
			Map<Integer, String> listDes, Integer idTransmission,
			Integer idNature, Integer idCategorie, Date dateLimiteRep,
			List<Integer> listIdAnnotation, String description,
			Integer idConfidentialite, Integer idUrgence,
			Date transactionDateDebut, Date transactionDateFin,Date dateCourrierReel, Date dateCourrierReelFin,
			String typeCourrierBoc, String recuOrEnvoyer,
			List<Integer> listDestinataire, String oldRef,Integer refGeneral,
			String necessiteReponse, String courrierReference,
			List<String> annees, int etatCloturer,
			String courrierCopyTransfere, String courrierCopy,String colonne1,String colonne2,String colonne3,
			String colonne4,String colonne5,String colonne6,String colonne7,String colonne8,String colonne9,String colonne10,String colonne11,String colonne12,
			String colonne13,Date colonne14,Date colonne15,Date colonne16,Date colonne17,Date colonne18,Date colonne19,Date colonne20,
			String colonne21,Boolean colonne22,
			String courrierFlagInterne,List<Integer> listIdBocMembers) throws HibernateException {
		// System.out.println("necessiteReponse : " + necessiteReponse);
		List<String> listRecuEnv = new ArrayList<String>();
		for (Integer idSub : listIdSubordonnes) {
			System.out.println("IDSUB==============> "+idSub);
		}
		
		for (Integer idSousUnit : listIdsSousUnites) {
			System.out.println("idSousUnit==============> "+idSousUnit);

		}
		if (isResponsable) {
			if (sub.equals("Oui")) {
				for (Integer idSub : listIdSubordonnes) {
					listRecuEnv.add("sub_" + idSub);
				}
			}
			// Courriers de mes sous unités
			if (unit.equals("Oui")) {
				for (Integer idSousUnit : listIdsSousUnites) {
					listRecuEnv.add("unit_" + idSousUnit);
				}
			}
			if (secretaire.equals("Oui")) {
				listRecuEnv.add(typeSecretaire);
			}
		}
		// Mes Courriers et les courriers de mon unité
		listRecuEnv.add(type);
		listRecuEnv.add(type1);
		
		
		for(String rrr:listRecuEnv){
			System.out.println("=====================> "+rrr);
		}

		Map<String, Object> pars = new HashMap<String, Object>();
		StringBuffer trMax = new StringBuffer(
				"SELECT COUNT(DISTINCT dmax.dossierId)  AS 'nbrc'"
						+ " FROM transactionn tmax"
						+ " INNER JOIN dossier dmax ON tmax.dossierId = dmax.dossierId"
						+ " INNER JOIN courrierdossier cdmax ON dmax.dossierId = cdmax.dossierId"
						+ " INNER JOIN courrier cmax ON cdmax.idCourrier = cmax.idCourrier"
						+
						// " INNER JOIN (SELECT tm2.* FROM transactionn tm2 WHERE tm2.transactionId IN (SELECT MIN(tm1.transactionId) FROM transactionn tm1 WHERE tm1.dossierId = tm2.dossierId)) tm ON tm.dossierId = tmax.dossierId");
						" INNER JOIN transactionn tm ON tmax.transactionFirst = tm.transactionId"
						+ " LEFT JOIN courrierdonneesupplementaire cds ON cdmax.idCourrier = cds.idCourrier ");

		if (courrierFlagInterne != null) {

			trMax.append(" AND cmax.courrierFlagInterne LIKE :courrierFlagInterne");
			pars.put("courrierFlagInterne", '%' + courrierFlagInterne + '%');

		}

		if (!listExp.isEmpty()) {
			trMax.append(" INNER JOIN transactionn t_all ON tmax.transactionFirst = t_all.transactionFirst"
					+ " LEFT JOIN expdest e_c ON t_all.idExpDest = e_c.idExpDest");
		}
		if (isBoc) {
			if (typeCourrierBoc.equals("A")) {
				trMax.append(" INNER JOIN expdest emax ON tmax.idExpDest = emax.idExpDest");
			}
			if (typeCourrierBoc.equals("D")) {
				trMax.append(" INNER JOIN expdest emin ON tm.idExpDest = emin.idExpDest");
			}
		}
		if (listIdAnnotation.size() > 0) {
			trMax.append(" LEFT JOIN transactionannotation tamax ON tamax.idTransaction = tmax.transactionId");
		}
		if (listDes.size() > 0) {
			trMax.append(" LEFT JOIN transactiondestinationreelle tdrmax ON tdrmax.transactionDestinationReelleId = tmax.transactionDestinationReelleId");
		}
		if (!isBoc && listRecuEnv.size() > 0
				&& !recuOrEnvoyer.equals("envoyes")) {
			trMax.append(" LEFT JOIN transactiondest tdmax ON tdmax.idTransaction = tmax.transactionId");
		}
		trMax.append(" WHERE cmax.courrierType != 'V' AND dmax.typeDossierId = 1");

		int i = 0;
		String expDest = "";
		String whereMax = "";
		String whereAnnees = "";
		if (annees != null) {
			if (annees.size() > 0) {
				trMax.append(" AND cmax.courrierDateReceptionAnnee IN (");
				for (String an : annees) {
					whereAnnees += ":an" + i + ", ";
					pars.put("an" + i, Integer.valueOf(an));
					i++;
				}
				whereAnnees = whereAnnees
						.substring(0, whereAnnees.length() - 2);
				trMax.append(whereAnnees + ")");
			}
		}
		System.out.println("type===================> "+type);
		if (!(type != null && type.equals("boc_100"))) {
			if(typeCourrierBoc!=null){
			if (!typeCourrierBoc.equals("A")
					&& !typeCourrierBoc.equals("D")) {
				int j = 0;
				
				String whereMax1 = "";
				trMax.append(" AND (");

				for (Integer id : listIdBocMembers) {
					System.out.println("ID : " + id);
					whereMax1 += "tmax.idUtilisateur = :id" + j + " OR ";
					pars.put("id" + j, id);
					j++;
					System.out.println("############ id == " + id);
				}
				System.out.println("############ whereMax == " + whereMax1);
				System.out.println("############2 type == " + type);
				trMax.append(whereMax1);
				// ça était tm.transactionId
				StringBuffer trDest = new StringBuffer(
						"tmax.transactionId IN (SELECT tdes.idTransaction"
								+ " FROM transactiondest tdes"
								+ " WHERE tdes.transactionDestTypeIntervenant = :type))");
				// System.out.println("typeeee : " + type);
				pars.put("type", type);
				trMax.append(trDest);
			}
			}
		}
		
		
		
		if (isBoc) {
			// Arrivé Départ
			if (typeCourrierBoc.equals("A")) {
				trMax.append(" AND emax.typeExpDest LIKE 'Externe'");
			}
			if (typeCourrierBoc.equals("D")) {
				trMax.append(" AND emin.typeExpDest LIKE 'Interne-%'");
			}
			trMax.append("AND cmax.idTransmission!=10 ");
		} else if (listRecuEnv.size() > 0) {
			for (String ed : listRecuEnv) {
				expDest += ":ed" + i + ", ";
				pars.put("ed" + i, ed);
				i++;
			}
			expDest = expDest.substring(0, expDest.length() - 2);
			if (recuOrEnvoyer.equals("recus")) {
				whereMax += " AND tdmax.transactionDestTypeIntervenant IN ("
						+ expDest + ")";
			} else if (recuOrEnvoyer.equals("envoyes")) {
				whereMax += " AND tmax.transactionTypeIntervenant IN ("
						+ expDest + ")";
			} else if (recuOrEnvoyer.equals("tous") || recuOrEnvoyer.equals("")) {
				whereMax += " AND (tmax.transactionTypeIntervenant IN ("
						+ expDest + ")"
						+ " OR tdmax.transactionDestTypeIntervenant IN ("
						+ expDest + "))";
			}
		}
		trMax.append(whereMax);

		// les criteres specifiés au niveau du recherche
		// Expediteur Reel
		String whereExpList = "";
		if (!listExp.isEmpty()) {
			// StringBuffer hqlExp = new StringBuffer(
			// " AND dmax.dossierId IN (SELECT t_c.dossierId" +
			// " FROM transactionn t_c" +
			// " LEFT JOIN expdest e_c ON t_c.idExpDest = e_c.idExpDest" +
			// " WHERE ");
			Iterator<Integer> iterE = listExp.keySet().iterator();
			while (iterE.hasNext()) {
				Integer expId = iterE.next();
				String expType = listExp.get(expId);
				// System.out.println(expId + " = " + expType);
				if (expType.equals("Externe")) {
					whereExpList += " e_c.idExpDestExterne = :expId" + i
							+ " OR ";
				} else if (expType.equals("Interne-Unité")) {
					whereExpList += "(:expType"
							+ i
							+ " = 'Interne-Unité' AND e_c.idExpDestLdap = :expId"
							+ i + ") OR ";
					pars.put("expType" + i, expType);
				} else if (expType.equals("Interne-Person")) {
					whereExpList += "(:expType"
							+ i
							+ " = 'Interne-Person' AND e_c.idExpDestLdap = :expId"
							+ i + ") OR ";
					pars.put("expType" + i, expType);
				}
				pars.put("expId" + i, expId);
				i++;
			}
			whereExpList = " AND "
					+ whereExpList.substring(0, whereExpList.length() - 3);
			trMax.append(whereExpList);
			// hqlExp.append(whereExpList + " GROUP BY t_c.dossierId)");
			// trMax.append(hqlExp);
		}
		// destinataire
		String whereDestList = "";
		String hqlDest0 = "";
		if (!listDes.isEmpty()) {

			Iterator<Integer> iterD0 = listDes.keySet().iterator();
			while (iterD0.hasNext()) {
				Integer destId0 = iterD0.next();
				String destType0 = listDes.get(destId0);

				if (destType0.equals("Externe")) {

					hqlDest0 = " AND ( (tdrmax.transactionDestinationReelleId IS NULL"
							+ " AND tmax.transactionId IN (SELECT td_d.idTransaction"
							+ " FROM transactiondest td_d"
							+ " LEFT JOIN expdest e_d ON td_d.idExpDest = e_d.idExpDest"
							+ " WHERE ";
				} else {
					hqlDest0 = " AND (tdrmax.transactionDestinationReelleIdDestinataire = :destId"
							+ i
							+ " OR (tdrmax.transactionDestinationReelleId IS NULL"
							+ " AND tmax.transactionId IN (SELECT td_d.idTransaction"
							+ " FROM transactiondest td_d"
							+ " LEFT JOIN expdest e_d ON td_d.idExpDest = e_d.idExpDest"
							+ " WHERE ";
				}
			}

			StringBuffer hqlDest = new StringBuffer(hqlDest0);

			Iterator<Integer> iterD = listDes.keySet().iterator();
			while (iterD.hasNext()) {
				Integer destId = iterD.next();
				String destType = listDes.get(destId);
				System.out.println(destId + " = " + destType);
				if (destType.equals("Externe")) {
					whereDestList += " e_d.idExpDestExterne = :destId" + i
							+ " OR ";
				} else if (destType.equals("Interne-Unité")) {
					whereDestList += "(:destType"
							+ i
							+ " = 'Interne-Unité' AND e_d.idExpDestLdap = :destId"
							+ i + ") OR ";
					pars.put("destType" + i, destType);
				} else if (destType.equals("Interne-Person")) {
					whereDestList += "(:destType"
							+ i
							+ " = 'Interne-Person' AND e_d.idExpDestLdap = :destId"
							+ i + ") OR ";
					pars.put("destType" + i, destType);
				}
				pars.put("destId" + i, destId);
				i++;
			}
			whereDestList = whereDestList.substring(0,
					whereDestList.length() - 3);
			hqlDest.append(whereDestList + ")))");
			trMax.append(hqlDest);
		}
		if (!object.equals("")) {
			trMax.append(" AND cmax.courrierObjet LIKE :object");

			pars.put("object", '%' + object + '%');

		}
		if (courrierCopyTransfere != null) {
			if (!courrierCopyTransfere.equals("")) {
				trMax.append(" AND cmax.courrierCopyTransfere LIKE :courrierCopyTransfere");

				pars.put("courrierCopyTransfere",
						'%' + courrierCopyTransfere + '%');

			}
		}
		if (!motCle.equals("")) {
			trMax.append(" AND cmax.keywords LIKE :motCle");
			pars.put("motCle", '%' + motCle + '%');
		}
		if (courrierReference != null) {
			if (!courrierReference.equals("")) {
				trMax.append(" AND cmax.courrierReferenceCorrespondant = :courrierReference");
				pars.put("courrierReference", courrierReference);

			}
		}
		if (etatCloturer == 0 || etatCloturer == 1) {
			trMax.append(" AND cmax.courrierEtatCloture = :etatCloturer");
			pars.put("etatCloturer", etatCloturer);
		}

		if (description != null) {
			if (!description.equals("")) {
				trMax.append(" AND cmax.courrierCommentaire LIKE :description");
				pars.put("description", '%' + description + '%');
			}
		}
		if (description != null) {
			if (courrierCopy != null) {
				if (!courrierCopy.equals("")) {
					trMax.append(" AND cmax.courrierCopyTransfere LIKE :courrierCopy");
					pars.put("courrierCopy", '%' + courrierCopy + '%');
				}
			}
		}
		if (idTransmission != null) {
			trMax.append(" AND cmax.idTransmission = :idTransmission");
			pars.put("idTransmission", idTransmission);
		}
		if (idNature != null) {
			System.out.println("id nature et id categorie not null");
			trMax.append(" AND cmax.idNature = :idNature");
			pars.put("idNature", idNature);
		}
		if (idConfidentialite != null) {
			trMax.append(" AND cmax.idConfidentialite = :idConfidentialite");
			pars.put("idConfidentialite", idConfidentialite);
		}
		if (idUrgence != null) {
			trMax.append(" AND cmax.idUrgence = :idUrgence");
			pars.put("idUrgence", idUrgence);
		}
		if (oldRef != null && oldRef.length()>0) {
			trMax.append(" AND cmax.courrierRefOriginale LIKE :oldRef");
			pars.put("oldRef", '%' + oldRef + '%');
		}
		//2020-06-02
		if(refGeneral !=null && refGeneral.intValue()!=0){
			trMax.append(" AND cmax.idCourrier  = :refGeneral");
			pars.put("refGeneral", refGeneral);

		}
		// [JS]
		if ((idCategorie != null) && (idNature == null)) {
			System.out.println("id categorie <> null && id Nature == null ");
			trMax.append(" AND cmax.idNature IN (SELECT n.natureId FROM nature n where n.natureCategorieId = :idCategorie)");
			pars.put("idCategorie", idCategorie);
		}
		//[JS] : 2020-05-13
		System.out.println("##### Dans test 00");
		if (colonne1 != null && !colonne1.equals("")) {
			System.out.println("##### I m Colonne 1");
			System.out.println("colonne 1 :" + colonne1);
			trMax.append(" AND cds.colonne1 LIKE :colonne1");
			pars.put("colonne1", '%' + colonne1 + '%');

		}
		if (colonne2 != null && !colonne2.equals("")) {
			System.out.println("I m Colonne 2");
			trMax.append(" AND cds.colonne2 LIKE :colonne2");
			pars.put("colonne2", '%' + colonne2 + '%');

		}
		if (colonne3 != null && !colonne3.equals("")) {
			System.out.println("I m Colonne 3");
			trMax.append(" AND cds.colonne3 LIKE :colonne3");
			pars.put("colonne3", '%' + colonne3 + '%');

		}
		if (colonne4 != null && !colonne4.equals("")) {
			System.out.println("I m Colonne 4");
			trMax.append(" AND cds.colonne4 LIKE :colonne4");
			pars.put("colonne4", '%' + colonne4 + '%');

		}
		if (colonne5 != null && !colonne5.equals("")) {
			System.out.println("I m Colonne 5");
			trMax.append(" AND cds.colonne5 LIKE :colonne5");
			pars.put("colonne5", '%' + colonne5 + '%');

		}
		if (colonne6 != null && !colonne6.equals("")) {
			System.out.println("I m Colonne 6");
			trMax.append(" AND cds.colonne6 LIKE :colonne6");
			pars.put("colonne6", '%' + colonne6 + '%');

		}
		System.out.println("##### colonne 7 :" + colonne7);
		if (colonne7 != null && !colonne7.equals("")) {
			System.out.println("I m Colonne 7");
			trMax.append(" AND cds.colonne7 LIKE :colonne7");
			pars.put("colonne7", '%' + colonne7 + '%');

		}
		System.out.println("##### colonne 8 :" + colonne8);
		if (colonne8 != null && !colonne8.equals("")) {
			System.out.println("I m Colonne 8");
			trMax.append(" AND cds.colonne8 LIKE :colonne8");
			pars.put("colonne8", '%' + colonne8 + '%');

		}
		System.out.println("##### colonne 9 :" + colonne9);
		if (colonne9 != null && !colonne9.equals("")) {
			System.out.println("I m Colonne 9");
			trMax.append(" AND cds.colonne9 LIKE :colonne9");
			pars.put("colonne9", '%' + colonne9 + '%');

		}
		if (colonne10 != null && !colonne10.equals("")) {
			System.out.println("I m Colonne 10");
			trMax.append(" AND cds.colonne10 LIKE :colonne10");
			pars.put("colonne10", '%' + colonne10 + '%');

		}
		if (colonne11 != null && !colonne11.equals("")) {
			System.out.println("I m Colonne 11");
			trMax.append(" AND cds.colonne11 LIKE :colonne11");
			pars.put("colonne11", '%' + colonne11 + '%');

		}
		if (colonne12 != null && !colonne12.equals("")) {
			System.out.println("I m Colonne 12");
			trMax.append(" AND cds.colonne12 LIKE :colonne12");
			pars.put("colonne12", '%' + colonne12 + '%');

		}
		if (colonne13 != null && !colonne13.equals("")) {
			System.out.println("I m Colonne 13");
			trMax.append(" AND cds.colonne12 LIKE :colonne13");
			pars.put("colonne12", '%' + colonne12 + '%');

		}

		if (colonne14 != null && !colonne14.equals("")) {
			System.out.println("I m Colonne 14");
			trMax.append(" AND cds.colonne14 LIKE :colonne14");
			pars.put("colonne14", colonne14);

		}

		if (colonne15 != null && !colonne15.equals("")) {
			System.out.println("I m Colonne 15");
			trMax.append(" AND cds.colonne15 LIKE :colonne15");
			pars.put("colonne15", colonne15);

		}

		if (colonne16 != null && !colonne16.equals("")) {
			System.out.println("I m Colonne 16");
			trMax.append(" AND cds.colonne16 = :colonne16");
			pars.put("colonne16", colonne16);
		}

		if (colonne17 != null && !colonne17.equals("")) {
			System.out.println("I m Colonne 17");
			trMax.append(" AND cds.colonne17 = :colonne17");
			pars.put("colonne17", colonne17);

		}

		if (colonne18 != null && !colonne18.equals("")) {
			System.out.println("I m Colonne 18");
			trMax.append(" AND cds.colonne18 = :colonne18");
			pars.put("colonne18", colonne18);

		}

		if (colonne19 != null && !colonne19.equals("")) {
			System.out.println("I m Colonne 19");
			trMax.append(" AND cds.colonne19 = :colonne19");
			pars.put("colonne19", colonne19);

		}

		if (colonne20 != null && !colonne20.equals("")) {
			System.out.println("I m Colonne 20");
			trMax.append(" AND cds.colonne20 = :colonne20");
			pars.put("colonne17", colonne17);

		}
		if (colonne21 != null && !colonne21.equals("")) {
			System.out.println("I m Colonne 21");
			trMax.append(" AND cds.colonne21 LIKE :colonne21");
			pars.put("colonne21", '%' + colonne21 + '%');

		}

		if (colonne22 != null && !colonne22.equals("")) {
			System.out.println("I m Colonne 22");
			trMax.append(" AND cds.colonne22 = :colonne22");
			// pars.put("colonne22" + colonne22 + '%');

		}
		
		
		
		// Annotaion
		if (!listIdAnnotation.isEmpty()) {
			String whereAnn = "";
			trMax.append(" AND tamax.idAnnotation IN (");
			for (Integer ann : listIdAnnotation) {
				whereAnn += ":ann" + i + ", ";
				pars.put("ann" + i, ann);
				i++;
			}
			trMax.append(whereAnn.substring(0, whereAnn.length() - 2) + ")");
		}
		if (transactionDateDebut != null && transactionDateFin == null) {
			trMax.append(" AND DATE_FORMAT(cmax.courrierDateReception,'%Y-%m-%d' ) =:transactionDateDebut ");
			pars.put("transactionDateDebut", transactionDateDebut);
		}
		if (transactionDateDebut != null && transactionDateFin != null) {
			trMax.append(" AND cmax.courrierDateReception BETWEEN :transactionDateDebut AND :transactionDateFin");
			pars.put("transactionDateDebut", transactionDateDebut);
			pars.put("transactionDateFin", transactionDateFin);
		}
		//2020-06-09
		

		if (dateCourrierReel != null && dateCourrierReelFin == null) {
			trMax.append(" AND DATE_FORMAT(cmax.courrierDateCourrierReelle,'%Y-%m-%d' )= :dateCourrierReel");
			pars.put("dateCourrierReel", dateCourrierReel);

		}
		if (dateCourrierReel != null && dateCourrierReelFin != null) {
			trMax.append(" AND cmax.courrierDateCourrierReelle BETWEEN :dateCourrierReel AND :dateCourrierReelFin");
			pars.put("dateCourrierReel", dateCourrierReel);
			pars.put("dateCourrierReelFin", dateCourrierReelFin);
		}
		
		if (dateLimiteRep != null) {
			trMax.append(" AND tmax.transactionDateReponse = :dateLimiteRep");
			pars.put("dateLimiteRep", dateLimiteRep);
		} else if (necessiteReponse.toLowerCase().equals("oui")) {
			trMax.append(" AND cmax.courrierNecessiteReponse = 'oui'");
		} else if (necessiteReponse.toLowerCase().equals("non")) {
			trMax.append(" AND cmax.courrierNecessiteReponse = 'non'");
		}

		// trMax.append(" GROUP BY dmax.dossierId) tab");

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());

		queryMax.addScalar("nbrc", new IntegerType());

		Iterator<String> iterMax = pars.keySet().iterator();
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			System.out.println("Count Recherrhce name =====> "+name);
			
			if(pars.get(name) != null){
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
			}
		}

		System.out.println("affiche Query "+queryMax.getQueryString());
		System.out.println("size list count "+queryMax.list().size());
		return queryMax.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Transaction> listTransactions(Date transactionDateDebut,
			Date transactionDateFin, Date dateReponse,
			List<Integer> listExpediteur, List<Integer> listDossier)
			throws HibernateException {
		List<Transaction> resultat;
		Criteria criteria = getSession().createCriteria(Transaction.class);
		if (transactionDateDebut != null && transactionDateFin == null) {
			criteria.add(Restrictions.eq("transactionDateTransaction",
					transactionDateDebut));
		}
		if (transactionDateDebut != null && transactionDateFin != null) {
			criteria.add(Restrictions.between("transactionDateTransaction",
					transactionDateDebut, transactionDateFin));
		}
		if (dateReponse != null) {
			criteria.add(Restrictions.eq("transactionDateReponse", dateReponse));
		}
		if (!listExpediteur.isEmpty()) {
			criteria.add(Restrictions.in("expdest.idExpDest", listExpediteur));
		}
		if (!listDossier.isEmpty()) {
			criteria.add(Restrictions.in("dossier.dossierId", listDossier));
		}
		if (transactionDateDebut == null && transactionDateFin == null
				&& dateReponse == null && listExpediteur.isEmpty()
				&& listDossier.isEmpty()) {
			resultat = new ArrayList<Transaction>();
		} else {
			resultat = new ArrayList<Transaction>();
			resultat = criteria.list();

			if (resultat.isEmpty()) {
				resultat = null;
			}
		}

		return resultat;
	}

	// @SuppressWarnings("unchecked")
	// @Override
	// public List<TransactionDestination> recherheMulticritereCourrierRecu(
	// Integer id, String type, String type1,
	// List<Integer> listTransaction, List<Integer> listIdTransaction,
	// List<Integer> listIdExpDest) throws HibernateException {
	// List<TransactionDestination> resultat = new
	// ArrayList<TransactionDestination>();
	// Criteria criteria = getSession().createCriteria(
	// TransactionDestination.class);
	// if (id != null && type1 != null) {
	// Criterion criterion = Restrictions.or(
	// Restrictions.eq("transactionDestIdIntervenant", id),
	// Restrictions.eq("transactionDestTypeIntervenant", type));
	// criterion = Restrictions.or(criterion,
	// Restrictions.eq("transactionDestTypeIntervenant", type1));
	// criteria.add(criterion);
	// }
	// if (id == null && type1 != null && type != null) {
	// Criterion criterion = Restrictions.or(
	// Restrictions.eq("transactionDestTypeIntervenant", type),
	// Restrictions.eq("transactionDestTypeIntervenant", type1));
	// criteria.add(criterion);
	// }
	// if (id == null && type1 != null && type == null) {
	// criteria.add(Restrictions.eq("transactionDestTypeIntervenant",
	// type1));
	// }
	// if (id == null && type1 == null) {
	// criteria.add(Restrictions
	// .eq("transactionDestTypeIntervenant", type));
	// }
	// if (!listTransaction.isEmpty()) {
	// criteria.add(Restrictions.in("id.idTransaction", listTransaction));
	// }
	// if (!listIdTransaction.isEmpty() && !listIdExpDest.isEmpty()) {
	// criteria.add(Restrictions.in("id.idTransaction", listIdTransaction));
	// criteria.add(Restrictions.in("id.idExpDest", listIdExpDest));
	// }
	// resultat = criteria.list();
	// return resultat;
	// }
	@SuppressWarnings("unchecked")
	@Override
	public List<TransactionDestination> recherheMulticritereCourrierRecu(
			boolean isResponsable, List<Integer> listIdsSousUnites,
			List<Integer> listIdSubordonnes, String secretaire, String sub,
			String unit, String type, String type1, String typeSecretaire,
			Integer idUser, String object, String motCle,
			List<Integer> listExpediteur, List<Integer> listIdExpDest,
			Integer idTransmission, Integer idNature, Date dateLimiteRep,
			List<Integer> listIdAnnotation, String description,
			Integer idConfidentialite, Integer idUrgence,
			Date transactionDateDebut, Date transactionDateFin)
			throws HibernateException {

		DetachedCriteria detachedCriteriaTransaction = DetachedCriteria
				.forClass(Transaction.class);
		detachedCriteriaTransaction.createAlias("dossier", "dossier")
				.createAlias("dossier.courriers", "dCourrier")
				.createAlias("dCourrier.nature", "nature")
				.createAlias("annotations", "annotations");
		detachedCriteriaTransaction.add(Restrictions.eq(
				"dossier.typedossier.typeDossierId", 1));
		detachedCriteriaTransaction.setProjection(Projections.projectionList()
				.add(Projections.property("transactionId"), "idTransaction"));

		// les criteres specifiés au niveau du recherche
		if (!object.equals("")) {
			detachedCriteriaTransaction.add(Restrictions.like(
					"dCourrier.courrierObjet", object, MatchMode.ANYWHERE));
		}
		if (!motCle.equals("")) {
			detachedCriteriaTransaction.add(Restrictions.like(
					"dCourrier.keywords", motCle, MatchMode.ANYWHERE));
		}
		if (!description.equals("")) {
			detachedCriteriaTransaction.add(Restrictions.like(
					"dCourrier.courrierCommentaire", description,
					MatchMode.ANYWHERE));
		}
		// if (!necessiteReponse.equals("Tous")) {
		// detachedCriteriaTransaction.add(Restrictions.eq("courrierNecessiteReponse",
		// necessiteReponse));
		// }
		if (idTransmission != null) {
			detachedCriteriaTransaction.add(Restrictions.eq(
					"dCourrier.transmission.transmissionId", idTransmission));
		}
		if (idNature != null) {
			detachedCriteriaTransaction.add(Restrictions.eq("nature.natureId",
					idNature));
		}
		if (idConfidentialite != null) {
			detachedCriteriaTransaction.add(Restrictions.eq(
					"dCourrier.confidentialite.confidentialiteId",
					idConfidentialite));
		}
		if (idUrgence != null) {
			detachedCriteriaTransaction.add(Restrictions.eq(
					"dCourrier.urgence.urgenceId", idUrgence));
		}
		// Annotaion
		if (!listIdAnnotation.isEmpty()) {
			detachedCriteriaTransaction.add(Restrictions.in(
					"annotations.annotationId", listIdAnnotation));
		}
		// expediteur
		if (!listExpediteur.isEmpty()) {
			detachedCriteriaTransaction.add(Restrictions.in(
					"expdest.idExpDest", listExpediteur));
		}
		if (transactionDateDebut != null && transactionDateFin == null) {
			detachedCriteriaTransaction.add(Restrictions.eq(
					"transactionDateTransaction", transactionDateDebut));
		}
		if (transactionDateDebut != null && transactionDateFin != null) {
			detachedCriteriaTransaction.add(Restrictions.between(
					"transactionDateTransaction", transactionDateDebut,
					transactionDateFin));
		}
		if (dateLimiteRep != null) {
			detachedCriteriaTransaction.add(Restrictions.eq(
					"transactionDateReponse", dateLimiteRep));
		}
		// if (object.equals("") && motCle.equals("") && description.equals("")
		// && necessiteReponse.equals("Tous") && idTransmission == null
		// && idNature == null && idConfidentialite == null
		// && idUrgence == null) {
		// resultat = new ArrayList<Courrier>();
		// } else {
		// resultat = new ArrayList<Courrier>();
		// resultat = criteria.list();
		// }
		// TODO //recherchemulticritére courrierRecu
		Criteria criteriaDestination = getSession().createCriteria(
				TransactionDestination.class);
		if (!listIdExpDest.isEmpty()) {
			criteriaDestination.add(Restrictions.in("id.idExpDest",
					listIdExpDest));
		}
		// Mes Courriers et les courriers de mon unité
		Criterion crit = Restrictions.or(
				Restrictions.eq("transactionDestTypeIntervenant", type),
				Restrictions.eq("transactionDestTypeIntervenant", type1));
		// // Courriers de mes subordonnés
		if (isResponsable) {
			if (sub.equals("Oui")) {
				for (Integer idSub : listIdSubordonnes) {
					crit = Restrictions.or(crit, Restrictions.eq(
							"transactionDestTypeIntervenant", "sub_" + idSub));
				}
			}
			// Courriers de mes sous unités
			if (unit.equals("Oui")) {
				for (Integer idSousUnit : listIdsSousUnites) {
					crit = Restrictions.or(crit, Restrictions.eq(
							"transactionDestTypeIntervenant", "unit_"
									+ idSousUnit));
				}
			}
			if (secretaire.equals("Oui")) {
				crit = Restrictions.or(crit, Restrictions.eq(
						"transactionDestTypeIntervenant", typeSecretaire));
			}
		}
		criteriaDestination.add(crit);
		criteriaDestination.add(Property.forName("id.idTransaction").in(
				detachedCriteriaTransaction));
		return criteriaDestination.list();
	}
/*
	@SuppressWarnings("unchecked")
	@Override
	public List<Pp> getListPpByCriteria(RecherchePpModel recherchePpModel,
			List<Integer> listExpeDestExterne) throws HibernateException {
		List<Pp> resultat = new ArrayList<Pp>();
		Criteria criteria = getSession().createCriteria(Pp.class);
		if (!recherchePpModel.getCinPp().equals("")) {
			criteria.add(Restrictions.eq("cin",
					Integer.parseInt(recherchePpModel.getCinPp())));
		}
		if (!recherchePpModel.getTelephonePortablePp().equals("")) {
			criteria.add(Restrictions.eq("ppnumPortable",
					Integer.parseInt(recherchePpModel.getTelephonePortablePp())));
		}
		if (!listExpeDestExterne.isEmpty()) {
			criteria.add(Restrictions.in("expdestexterne.idExpDestExterne",
					listExpeDestExterne));
		}
		if (!listExpeDestExterne.isEmpty()
				|| !recherchePpModel.getCinPp().equals("")
				|| !recherchePpModel.getTelephonePortablePp().equals("")) {
			resultat = criteria.list();
		}
		return resultat;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pm> getListPmByCriteria(RecherchePmModel recherchePmModel,
			List<Integer> listExpeDestExterne) throws HibernateException {
		List<Pm> resultat = new ArrayList<Pm>();
		Criteria criteria = getSession().createCriteria(Pm.class);
		if (!recherchePmModel.getMatriculeFiscalePm().equals("")) {
			criteria.add(Restrictions.like("pmmatriculeFiscal",
					recherchePmModel.getMatriculeFiscalePm(), MatchMode.EXACT));
		}
		if (!recherchePmModel.getAffiliationCNSSPm().equals("")) {
			criteria.add(Restrictions.like("pmaffiliationCnss",
					recherchePmModel.getAffiliationCNSSPm(), MatchMode.EXACT));
		}
		if (!recherchePmModel.getRegistreCommercePm().equals("")) {
			criteria.add(Restrictions.like("registreCommerce",
					recherchePmModel.getRegistreCommercePm(), MatchMode.EXACT));
		}
		if (!listExpeDestExterne.isEmpty()) {
			criteria.add(Restrictions.in("expdestexterne.idExpDestExterne",
					listExpeDestExterne));
		}
		if (!listExpeDestExterne.isEmpty()
				|| !recherchePmModel.getRegistreCommercePm().equals("")
				|| !recherchePmModel.getAffiliationCNSSPm().equals("")
				|| !recherchePmModel.getMatriculeFiscalePm().equals("")) {
			resultat = criteria.list();
		}
		return resultat;
	}*/

	@SuppressWarnings("unchecked")
	@Override
	public List<Expdestexterne> getListExpeDestExterneByCriteria(String nom,
			String prenom, String telephone, String mail, String adresse,
			String codePostal, String ville, String pays, String fax,
			String gouvernorat) throws HibernateException {
		List<Expdestexterne> resultat = new ArrayList<Expdestexterne>();
		Criteria criteria = getSession().createCriteria(Expdestexterne.class);
		criteria.add(Restrictions.ne("typeutilisateur.typeUtilisateurId", 3));
		if (!nom.equals("")) {
			criteria.add(Restrictions.like("expDestExterneNom", nom,
					MatchMode.ANYWHERE));
		}
		if (!prenom.equals("")) {
			criteria.add(Restrictions.like("expDestExternePrenom", prenom,
					MatchMode.ANYWHERE));
		}
		if (!telephone.equals("")) {
			criteria.add(Restrictions.like("expDestExterneTelephone",
					telephone, MatchMode.EXACT));
		}
		if (!mail.equals("")) {
			criteria.add(Restrictions.like("expDestExterneMail", mail,
					MatchMode.EXACT));
		}
		if (!adresse.equals("")) {
			criteria.add(Restrictions.like("expDestExterneAdresse", adresse,
					MatchMode.ANYWHERE));
		}
		if (!codePostal.equals("")) {
			criteria.add(Restrictions.like("expDestExterneCodePostale",
					codePostal, MatchMode.EXACT));
		}
		if (!ville.equals("")) {
			criteria.add(Restrictions.like("expDestExterneVille", ville,
					MatchMode.EXACT));
		}
		if (!pays.equals("")) {
			criteria.add(Restrictions.like("expDestExternePays", pays,
					MatchMode.EXACT));
		}
		if (!fax.equals("")) {
			criteria.add(Restrictions.like("expDestExterneFax", fax,
					MatchMode.EXACT));
		}
		if (!gouvernorat.equals("")) {
			criteria.add(Restrictions.like("expDestExterneGouvernerat",
					gouvernorat, MatchMode.EXACT));
		}
		if (!gouvernorat.equals("") || !fax.equals("") || !pays.equals("")
				|| !ville.equals("") || !codePostal.equals("")
				|| !adresse.equals("") || !mail.equals("")
				|| !telephone.equals("") || !prenom.equals("")
				|| !nom.equals("")) {
			resultat = criteria.list();
		}
		return resultat;
	}

	public int getMaxIdUserNotification() {
		String maxHql = "Select max(emp.idUserNotification) FROM usernotification emp";
		Session session = sessionFactory.openSession();
		Query maxQuery = session.createQuery(maxHql);
		return (Integer) maxQuery.list().get(0);
	}

	// Nouvelle Bean CourrierConsultationBean
	//
	// Requet pour la liste des courrier autre que BOCT
	//
	//
	// 2019-06-24
	@SuppressWarnings("unchecked")
	@Override
	public List<CourrierInformations> findCourrierEnvoyerANDRecuWithCriterion(
			boolean isResponsable, List<Integer> listIdsSousUnites,
			List<Integer> listIdSubordonnes, HashMap<String, Object> filterMap,
			String sortField, boolean descending, String secretaire,
			String sub, String unit, int jourOrAutre, Date dateDebut,
			Date dateFin, String type, String type1, String typeSecretaire,
			Integer idUser, Integer typeTransmission, String stateTraitement,
			int firstIndex, int maxResult, boolean forRapport,
			Integer courrierRubriqueId, String typeCourrier, String DBType,
			Integer idCourrierCourant, int flagueCloture, int flagInterne) {
//		System.out.println("flagueCloture = " + flagueCloture);
//		System.out.println("############ flagInterne == " + flagInterne);
		long startTime = System.currentTimeMillis();
		Map<String, Object> pars = new HashMap<String, Object>();
		List<String> listRecu = new ArrayList<String>();
		List<String> listEnv = new ArrayList<String>();
		List<Integer> listEnvInteger = new ArrayList<Integer>();
		 System.out.println("typeTransmission : "+typeTransmission);
		 System.out.println("stateTraitement : "+stateTraitement);
		 System.out.println("firstIndex : "+firstIndex);
		 System.out.println("maxResult : "+maxResult);
		 System.out.println("forRapport : "+forRapport);
		 System.out.println("courrierRubriqueId : "+courrierRubriqueId);
		 System.out.println("typeCourrier : "+typeCourrier);
		 System.out.println("idCourrierCourant : "+idCourrierCourant);
		 System.out.println("jourOrAutre : "+jourOrAutre);
		// ****** Courrier Recu
		if (typeCourrier.equals("Recu") || stateTraitement.equals("Avalider")
				|| typeCourrier.equals("Tous")) {

			if (isResponsable) {
				switch (courrierRubriqueId) {
				case 1:
					listRecu.add(type1);
					break;
				case 2:
					listRecu.add(type);
					break;
				case 3:
					if (listIdSubordonnes != null
							&& listIdSubordonnes.size() > 0) {
						for (Integer idSub : listIdSubordonnes) {
							listRecu.add("sub_" + idSub);
						}
					} else {
						listRecu.add("sub_xx99999xx");
					}
					break;
				case 4:
					listRecu.add(typeSecretaire);
					break;
				case 5:
					if (listIdsSousUnites != null
							&& listIdsSousUnites.size() > 0) {
						for (Integer idSousUnit : listIdsSousUnites) {
							listRecu.add("unit_" + idSousUnit);
						}
					} else {
						System.out.println("PAS DES SOUS-UNITES");
						listRecu.add("unit_xx99999xx");
					}
					break;
				case 6:
				if(listIdSubordonnes!=null )
					for (Integer idSub : listIdSubordonnes) {
						listRecu.add("sub_" + idSub);
					}
				else{
					listRecu.add("sub_xx99999xx");
					
				}
				if(listIdsSousUnites!=null)
					for (Integer idSousUnit : listIdsSousUnites) {
						listRecu.add("unit_" + idSousUnit);
					}
				else{
					listRecu.add("unit_xx99999xx");
				}
					if (secretaire != null) {
						if (secretaire.equals("Oui")) {
							if(typeSecretaire!=null)
							listRecu.add(typeSecretaire);
						}
					}
					if (type != null) {
						listRecu.add(type);
					}
					if (type1 != null) {
						listRecu.add(type1);
					}
				}
			} else {
				listRecu.add(type);
				listRecu.add(type1);
			}
		}
		System.out.println("2020 type =>" + type);
		System.out.println("2020 type1 =>" + type1);
		System.out.println("size lisre REcu " + listRecu.size());
		// ****** Courrier Envoye
		if (typeCourrier.equals("Envoyes") || typeCourrier.equals("Tous")
				|| typeCourrier.equals("Enveloppe")) {
			if (isResponsable) {
				System.out.println("courrierRubriqueId =======2=====> : "
						+ courrierRubriqueId);

				switch (courrierRubriqueId) {
				case 1:
					listEnv.add(type1);
					break;
				case 2:
					listEnv.add(type);
					break;
				case 3:

					if (!listIdSubordonnes.isEmpty()) {
						for (Integer idSub : listIdSubordonnes) {
							listEnv.add("sub_" + idSub);
						}
					} else {
						listEnv.add("sub_xx99999xx");
					}
					break;
				case 4:
					listEnv.add(typeSecretaire);
					break;
				case 5:
					if (listIdsSousUnites != null
							&& listIdsSousUnites.size() > 0) {
						for (Integer idSousUnit : listIdsSousUnites) {
							listEnv.add("unit_" + idSousUnit);
						}
					} else {
						System.out.println("PAS DES SOUS-UNITES");
						listEnv.add("unit_xx99999xx");
					}
					break;
				case 6:
					if(listIdSubordonnes!=null )
					for (Integer idSub : listIdSubordonnes) {
						listEnv.add("sub_" + idSub);
					}
					else{
						listEnv.add("sub_xx99999xx");
					}
				if(listIdsSousUnites!=null)
					for (Integer idSousUnit : listIdsSousUnites) {
						listEnv.add("unit_" + idSousUnit);
					}
				else{
					listEnv.add("unit_xx99999xx");
				}
					if (secretaire != null) {
						if (secretaire.equals("Oui")) {
							if(typeSecretaire!=null)
							listEnv.add(typeSecretaire);
						}
					}
					if (type != null) {
						listEnv.add(type);
					}
					if (type1 != null) {
						listEnv.add(type1);
					}
				case 99:
					if(listIdsSousUnites!=null)
					for (Integer idSousUnit : listIdsSousUnites) {
						listEnvInteger.add(idSousUnit);
					}
					break;

				}
			} else {
				listEnv.add(type);
				listEnv.add(type1);
			}
		}
		StringBuffer trMax;
		if (typeCourrier.equals("Enveloppe")) {
			trMax = new StringBuffer(
					"SELECT MAX(tmax.transactionId)"
							+ // , dmax.dossierId
							" FROM transactionn tmax"
							+ " INNER JOIN dossier dmax ON tmax.dossierId = dmax.dossierId"
							+ " INNER JOIN courrierdossier cdmax ON dmax.dossierId = cdmax.dossierId"
							+ " INNER JOIN courrier cmax ON cdmax.idCourrier = cmax.idCourrier"
							+ " INNER JOIN transactiondestinationreelle tdreel ON tdreel.transactionDestinationReelleId=tmax.transactionDestinationReelleId"
							+ " LEFT JOIN transactiondest tdmax ON tmax.transactionId = tdmax.idTransaction"
							+ " WHERE dmax.typeDossierId = 1");

		} else {
			trMax = new StringBuffer(
					"SELECT MAX(tmax.transactionId)"
							+ // , dmax.dossierId
							" FROM transactionn tmax"
							+ " INNER JOIN dossier dmax ON tmax.dossierId = dmax.dossierId"
							+ " INNER JOIN courrierdossier cdmax ON dmax.dossierId = cdmax.dossierId"
							+ " INNER JOIN courrier cmax ON cdmax.idCourrier = cmax.idCourrier"
							+ " LEFT JOIN transactiondest tdmax ON tmax.transactionId = tdmax.idTransaction"
							+ " WHERE dmax.typeDossierId = 1");

		}

		System.out.println("jourOrAutre  ==  " + jourOrAutre);
		System.out.println("vb.getSelectedListCourrier()  ==  "
				+ vb.getSelectedListCourrier());
		// if (vb.getSelectedListCourrier().equals("CRmois")){
		// jourOrAutre= 4;
		//
		// }
		// if (vb.getSelectedListCourrier().equals("CRannee")){
		// jourOrAutre=3;
		//
		// }
		if (flagueCloture == 4) {

			trMax.append(" AND cmax.courrierEtatCloture =1");
		}

		if (jourOrAutre == 1) {
			trMax.append(" AND tmax.transactionDateTransaction BETWEEN :dateDebut AND :dateFin");
			pars.put("dateDebut", dateDebut);
			pars.put("dateFin", dateFin);
		}
		if (jourOrAutre == 2) {
			trMax.append(" AND tmax.transactionDateTransaction < :dateDebut");
			pars.put("dateDebut", dateDebut);
		}
		// -------------------------------------------MM-----------------------------------------------
		// ------------------------------------debut changement
		// optimisation---------------------------
		// Calendar calendar = Calendar.getInstance();
		// Date dateAujourd = calendar.getTime();
		// par année
		if (jourOrAutre == 3) {
			System.out.println("#########Dans if (jourOrAutre == 3)");
			trMax.append(" AND cmax.courrierDateReceptionAnnee =:dateDebut");

			pars.put("dateDebut", dateDebut.getYear() + 1900);
		}
		// par moi
		if (jourOrAutre == 4) {
			System.out.println("#########Dans if (jourOrAutre == 4)");
			trMax.append(" AND cmax.courrierDateReceptionAnnee =:anneeEnCours AND cmax.courrierDateReceptionMois =:moisEnCours");
			pars.put("moisEnCours", dateDebut.getMonth() + 1);
			pars.put("anneeEnCours", dateDebut.getYear() + 1900);
		}
		// 2019-06-24
		// par jour
		if (jourOrAutre == 5) {
			System.out.println("#########Dans if (jourOrAutre == 5)");
			trMax.append(" AND DATE_FORMAT(cmax.courrierDateReception,'%Y-%m-%d') =:dateDebut ");
			// JS :Formatter date Début **********************************
			System.out.println("Date Début Avant formattage :" + dateDebut);
			SimpleDateFormat formatDebut = new SimpleDateFormat("yyyy-MM-dd");
			String dateD = formatDebut.format(dateDebut);
			pars.put("dateDebut", dateD);
		}

		// [JS] : Restriction du courrier courant (Courriers liées )
		if (jourOrAutre == 6) {
			trMax.append(" AND cmax.idCourrier != :idCourrierCourant AND cmax.courrierDateReceptionAnnee =:dateDebut AND cmax.courrierDateReceptionAnnee =:dateDebut AND cmax.idCourrier NOT IN ( select lc.idCourrier from liencourrier lc , courrier c ,lienscourriers lcs where lc.liensCourrier=lcs.liensCourrier and lcs.idCourrier=c.idCourrier and c.idCourrier =:idCourrierCourant)");
			pars.put("idCourrierCourant", idCourrierCourant);
			pars.put("dateDebut", dateDebut.getYear() + 1900);

		}
		// [JS] : les Courriers Liées
		if (jourOrAutre == 7) {
			trMax.append(" AND cmax.courrierDateReceptionAnnee =:dateDebut AND cmax.idCourrier in ( select lc.idCourrier from liencourrier lc , courrier c ,lienscourriers lcs where lc.liensCourrier=lcs.liensCourrier and lcs.idCourrier=c.idCourrier and c.idCourrier =:idCourrierCourant)");
			pars.put("dateDebut", dateDebut.getYear() + 1900);
			pars.put("idCourrierCourant", idCourrierCourant);

		}

		if (jourOrAutre == 8) {
			trMax.append(" AND cmax.idcourrierFK is null  AND cmax.courrierFormat is null ");

		}
		if (jourOrAutre == 9) {
			System.out.println("idCourrierCourant : " + idCourrierCourant);
			trMax.append(" AND  cmax.idcourrierFK =:idCourrierCourant  AND cmax.courrierFormat is null ");
			pars.put("idCourrierCourant", idCourrierCourant);

		}
		if (jourOrAutre == 10) {
			trMax.append(" AND  cmax.courrierFormat = 'valise' ");

		}
		if (flagInterne == 11) {
			System.out
					.println("############ requ 2 Dans Condition : flagInterne == "
							+ flagInterne);
			trMax.append(" AND  cmax.courrierType ='I' OR cmax.courrierType = 'I*'");

		}
		if (jourOrAutre == 12) {
			System.out
					.println("============================= AO Consultation ");
			System.out.println("idCourrierCourant : " + idCourrierCourant);
			trMax.append(" and cmax.idaoConsultation  =:idCourrierCourant ");
			pars.put("idCourrierCourant", idCourrierCourant);
		}
		// [JS] : les Courriers Liées
		if (jourOrAutre == 13) {
			trMax.append(" AND cmax.idTransmission=10 AND cmax.courrierDateReceptionAnnee =:dateDebut AND cmax.idCourrier in ( select lc.idCourrier from liencourrier lc , courrier c ,lienscourriers lcs where lc.liensCourrier=lcs.liensCourrier and lcs.idCourrier=c.idCourrier and c.idCourrier =:idCourrierCourant)");
			pars.put("dateDebut", dateDebut.getYear() + 1900);
			pars.put("idCourrierCourant", idCourrierCourant);

		}
		if (jourOrAutre == 99) {
			trMax.append(" AND cmax.idTransmission=10 AND cmax.idCourrier != :idCourrierCourant AND cmax.courrierDateReceptionAnnee =:dateDebut AND cmax.courrierDateReceptionAnnee =:dateDebut AND cmax.idCourrier NOT IN ( select lc.idCourrier from liencourrier lc , courrier c ,lienscourriers lcs where lc.liensCourrier=lcs.liensCourrier and lcs.idCourrier=c.idCourrier )");
			pars.put("idCourrierCourant", idCourrierCourant);
			pars.put("dateDebut", dateDebut.getYear() + 1900);
		}

		// --------------------------------------Fin changement
		// opt------------------------------------
		int i = 0;
		String whereMax = "";
		if ((typeCourrier.equals("Recu") || stateTraitement.equals("Avalider"))
				&& listRecu.size() > 0) {
			for (String recu : listRecu) {
				System.out.println("reu 1" + recu);

				System.out.println("recu: " + recu);
				whereMax += " :recu" + i + ", ";
				pars.put("recu" + i, recu);
				i++;
			}
			if (whereMax.length() > 0) {
				trMax.append(" AND tdmax.transactionDestTypeIntervenant IN ("
						+ whereMax.substring(0, whereMax.length() - 2) + ")");
			}
		} else if (typeCourrier.equals("Envoyes") && listEnv.size() > 0) {
			for (String env : listEnv) {
				whereMax += " :env" + i + ", ";
				pars.put("env" + i, env);
				i++;
			}
			if (whereMax.length() > 0) {

				trMax.append(" AND tmax.transactionTypeIntervenant IN ("
						+ whereMax.substring(0, whereMax.length() - 2) + ")");

			}
		} else if (typeCourrier.equals("Tous")
				&& (listEnv.size() > 0 || listRecu.size() > 0)) {
			trMax.append(" AND (");
			for (String env : listEnv) {
				whereMax += " :env" + i + ", ";
				pars.put("env" + i, env);
				i++;
			}
			if (whereMax.length() > 0) {
				trMax.append(" tmax.transactionTypeIntervenant IN ("
						+ whereMax.substring(0, whereMax.length() - 2) + ")");
			}
			whereMax = "";
			for (String recu : listRecu) {
				System.out
						.println("========================== Reu ==============="
								+ recu);
				whereMax += " :recu" + i + ", ";
				pars.put("recu" + i, recu);
				i++;
			}
			if (listEnv.size() > 0) {
				trMax.append(" OR ");
			}
			if (whereMax.length() > 0) {
				trMax.append(" tdmax.transactionDestTypeIntervenant IN ("
						+ whereMax.substring(0, whereMax.length() - 2) + ")");
			}
			trMax.append(") ");
		} else if (typeCourrier.equals("Enveloppe")) {

			int imax1 = 0;
			String whereMax1 = " AND tdreel.transactionDestinationReelleIdDestinataire IN (";
			if (listEnvInteger.size() > 0) {
				for (Integer ids : listEnvInteger) {
					Integer idTrans = ids;
					whereMax1 += ":idTrans" + imax1 + ", ";
					pars.put("idTrans" + imax1, idTrans);
					imax1++;
				}
				whereMax1 = whereMax1.substring(0, whereMax1.length() - 2);
			}
			whereMax1 += " ) ";
			trMax.append(whereMax1);
		}
		// Fin Restrictions

		// etat => A valider
		if (stateTraitement.equals("Avalider")) {
			trMax.append(" AND tmax.etatId IN (2, 10)");
		}
		trMax.append(" GROUP BY dmax.dossierId"
				+ " ORDER BY dmax.dossierId DESC");
		System.out.println("firstIndex : " + firstIndex);
		System.out.println("maxResult : " + maxResult);

		if (DBType.contains("mysql")) {
			trMax.append(" LIMIT " + firstIndex + ", " + maxResult);
		}

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		Iterator<String> iterMax = pars.keySet().iterator();
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			System.out.println("===============");
			System.out.println("name : " + name);
			System.out.println("value : " + value);
			System.out.println("===============");
if(pars.get(name)!=null){
			if ( pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
}
		}

		if (DBType.contains("sqlserver")) {
			queryMax.setFirstResult(firstIndex);
			queryMax.setMaxResults(maxResult);
		}

		System.out.println(queryMax.getQueryString());
		List<Integer> maxIds = queryMax.list();
//		System.out.println("list courrier ancien dure RESPONSIBLE MAX : "
//				+ (System.currentTimeMillis() - startTime));

		Map<String, Object> params = new HashMap<String, Object>();
		int imax = 0;
		String where = " t.etatId != 99 AND  t.transactionId IN (";
		// String whereD = " AND tm1.dossierId IN (";
		if (maxIds.size() > 0) {
			for (Integer ids : maxIds) {
				Integer idTrans = ids;
				where += ":idTrans" + imax + ", ";
				params.put("idTrans" + imax, idTrans);
				// Integer idDoss = (Integer) ids[1];
				// whereD += ":idDoss" + imax + ", ";
				// params.put("idDoss" + imax, idDoss);
				imax++;
			}
		} else {
			return new ArrayList<CourrierInformations>();
		}

		StringBuffer hql = new StringBuffer(
				"SELECT DISTINCT c.idCourrier AS 'courrierID',"
						+ " c.courrierObjet AS 'courrierObjet',"
						+ " c.courrierReferenceCorrespondant AS 'courrierReference',"
						+ " c.courrierCommentaire AS 'courrierCommentaire',"
						+ " c.courrierOldNum AS 'courrierOldNum',"
						+ " c.courrierCircuit AS 'courrierCircuit',"
						+ " d.dossierId AS 'dossierID',"
						+ " n.natureLibelle AS 'courrierNature',"
						+ " t.etatId AS 'etatID',"
						+ " c.courrierDateReception AS 'courrierDateReceptionEnvoi',"
						+ " t.transactionDateConsultation AS 'transactionDateConsultation',"
						+ " t.transactionId AS 'transactionID',"
						+ " t.idExpDest AS 'expID',"
						+ " t.transactionOrdre AS 'transactionOrdre',"
						+ " t.idUtilisateur AS 'idUtilisateur',"
						+ " t_tdr.transactionDestinationReelleIdDestinataire AS 'transactionDestinationReelID',"
						+ " t_tdr.transactionDestinationReelleTypeDestinataire AS 'transactionDestinationReelleTypeDestinataire',"
						+ " e.typeExpDest AS 'expTypeOld',"
						+ " e.idExpDestLdap AS 'expLdapOld',"
						+ " ee.Exp_Dest_ExterneNom AS 'expNomOld',"
						+ " ee.Exp_Dest_ExternePrenom AS 'expPrenomOld',"
						+ " ee.typeUtilisateurId AS 'expTypeUserOld',"
						+ " tm.transactionId AS 'minTransactionID',"
						+ " em.typeExpDest AS 'expType',"
						+ " em.idExpDestLdap AS 'expLdap',"
						+ " eem.Exp_Dest_ExterneNom AS 'expNom',"
						+ " eem.Exp_Dest_ExternePrenom AS 'expPrenom',"
						+ " eem.typeUtilisateurId AS 'expTypeUser',");
		if (DBType.contains("sqlserver")) {
			hql.append(" STUFF((SELECT '|' + CONVERT(NVARCHAR(10), t3.transactionId)"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), e3.idExpDest), '')"
					+ " + ';' + ISNULL(e3.typeExpDest, '')"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), e3.idExpDestLdap), '')"
					+ " + ';' + ISNULL(ee3.Exp_Dest_ExterneNom, '')"
					+ " + ';' + ISNULL(ee3.Exp_Dest_ExternePrenom, '')"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), ee3.typeUtilisateurId), '')"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), tdr.transactionDestinationReelleIdDestinataire), '')"
					+ " + ';' + ISNULL(tdr.transactionDestinationReelleTypeDestinataire, '')"
					+ " FROM transactionn t3 "
					+ " LEFT JOIN transactiondest td3 ON t3.transactionId = td3.idTransaction"
					+ " LEFT JOIN transactiondestinationreelle tdr ON t3.transactionDestinationReelleId = tdr.transactionDestinationReelleId"
					+ " LEFT JOIN expdest e3 ON td3.idExpDest = e3.idExpDest"
					+ " LEFT JOIN expdestexterne ee3 ON e3.idExpDestExterne = ee3.idExpDestExterne"
					+ " WHERE t3.dossierId = t.dossierId"
					+ " FOR XML PATH('')), 1, 1, '') AS 'destReelList'");
		}
		if (DBType.contains("mysql")) {
			hql.append("(SELECT GROUP_CONCAT(CONCAT(CONVERT(t3.transactionId, CHAR(10))"
					+ " , ';' , IFNULL(CONVERT(e3.idExpDest, CHAR(10)), '')"
					+ " , ';' , IFNULL(e3.typeExpDest, '')"
					+ " , ';' , IFNULL(CONVERT(e3.idExpDestLdap, CHAR(10)), '')"
					+ " , ';' , IFNULL(ee3.Exp_Dest_ExterneNom, '')"
					+ " , ';' , IFNULL(ee3.Exp_Dest_ExternePrenom, '')"
					+ " , ';' , IFNULL(CONVERT(ee3.typeUtilisateurId, CHAR(10)), '')"
					+ " , ';' , IFNULL(CONVERT(tdr.transactionDestinationReelleIdDestinataire, CHAR(10)), '')"
					+ " , ';' , IFNULL(tdr.transactionDestinationReelleTypeDestinataire, '')) SEPARATOR '|')"
					+ " FROM transactionn t3 "
					+ " LEFT JOIN transactiondest td3 ON t3.transactionId = td3.idTransaction"
					+ " LEFT JOIN transactiondestinationreelle tdr ON t3.transactionDestinationReelleId = tdr.transactionDestinationReelleId"
					+ " LEFT JOIN expdest e3 ON td3.idExpDest = e3.idExpDest"
					+ " LEFT JOIN expdestexterne ee3 ON e3.idExpDestExterne = ee3.idExpDestExterne"
					+ " WHERE t3.dossierId = t.dossierId) AS 'destReelList'");
		}
		hql.append(" FROM transactionn t"
				+ " INNER JOIN dossier d ON t.dossierId = d.dossierId"
				+ " INNER JOIN courrierdossier cd ON d.dossierId = cd.dossierId"
				+ " INNER JOIN courrier c ON cd.idCourrier = c.idCourrier"
				+ " INNER JOIN nature n ON c.idNature = n.natureId"
				+ " LEFT JOIN expdest e ON t.idExpDest = e.idExpDest"
				+ " LEFT JOIN expdestexterne ee ON e.idExpDestExterne = ee.idExpDestExterne"
				+ " LEFT JOIN transactiondestinationreelle t_tdr ON t.transactionDestinationReelleId = t_tdr.transactionDestinationReelleId"
				+
				// " LEFT JOIN (SELECT tm2.*" +
				// " FROM transactionn tm2" +
				// " WHERE tm2.transactionId IN (SELECT MIN(tm1.transactionId)"
				// +
				// " FROM transactionn tm1" +
				// " WHERE tm1.dossierId = tm2.dossierId" +
				// whereD.substring(0, whereD.length()-2) +
				// ") GROUP BY tm1.dossierId)) tm ON tm.dossierId = t.dossierId"
				// +
				" INNER JOIN transactionn tm ON t.transactionFirst = tm.transactionId"
				+ " LEFT JOIN expdest em ON tm.idExpDest = em.idExpDest"
				+ " LEFT JOIN expdestexterne eem ON em.idExpDestExterne = eem.idExpDestExterne"
				+ " WHERE " + where.substring(0, where.length() - 2) + ")");

		hql.append(" ORDER BY c.idCourrier DESC");

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		query.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));

		query.addScalar("courrierID", new IntegerType());
		query.addScalar("courrierObjet", new StringType());
		query.addScalar("courrierReference", new StringType());
		query.addScalar("courrierCommentaire", new StringType());
		query.addScalar("courrierOldNum", new IntegerType());
		query.addScalar("courrierCircuit", new StringType());
		query.addScalar("dossierID", new IntegerType());
		query.addScalar("courrierNature", new StringType());
		query.addScalar("etatID", new IntegerType());
		query.addScalar("courrierDateReceptionEnvoi", new DateType());
		query.addScalar("transactionDateConsultation", new DateType());
		query.addScalar("transactionID", new IntegerType());
		query.addScalar("expID", new IntegerType());
		query.addScalar("transactionOrdre", new IntegerType());
		query.addScalar("idUtilisateur", new IntegerType());
		query.addScalar("transactionDestinationReelID", new IntegerType());
		query.addScalar("transactionDestinationReelleTypeDestinataire",
				new StringType());
		query.addScalar("expLdapOld", new IntegerType());
		query.addScalar("expTypeOld", new StringType());
		query.addScalar("expNomOld", new StringType());
		query.addScalar("expPrenomOld", new StringType());
		query.addScalar("expTypeUserOld", new IntegerType());
		query.addScalar("minTransactionID", new IntegerType());
		query.addScalar("expLdap", new IntegerType());
		query.addScalar("expType", new StringType());
		query.addScalar("expNom", new StringType());
		query.addScalar("expPrenom", new StringType());
		query.addScalar("expTypeUser", new IntegerType());
		query.addScalar("destReelList", new StringType());

		Iterator<String> iter = params.keySet().iterator();
		while (iter.hasNext()) {
			String name = iter.next();
			Object value = params.get(name);
			// System.out.println(name + " = " + value);
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println("######## REQUETE ######## "+query.getQueryString());
		return query.list();
	}

	// Count
	/*
	 * @Override public Long CountAllCourrierEnvoyerANDRecuWithCriterion(
	 * boolean isResponsable, List<Integer> listIdsSousUnites, List<Integer>
	 * listIdSubordonnes, HashMap<String, Object> filterMap, String secretaire,
	 * String sub, String unit, int jourOrAutre, Date dateDebut, Date dateFin,
	 * String type, String type1, String typeSecretaire, Integer idUser, Integer
	 * typeTransmission, String stateTraitement, Integer courrierRubriqueId,
	 * boolean forStat, String typeCourrier) { // ********************Recu
	 * 
	 * DetachedCriteria detachedCriteriaTrDest = DetachedCriteria
	 * .forClass(TransactionDestination.class);
	 * detachedCriteriaTrDest.setProjection(Projections.projectionList().add(
	 * Projections.property("id.idTransaction"), "idTransaction")); Criterion
	 * critD = null; List<String> listSub = new ArrayList<String>();
	 * List<String> listUnit = new ArrayList<String>(); if (forStat) { critD =
	 * Restrictions.or( Restrictions.eq("transactionDestTypeIntervenant", type),
	 * Restrictions.eq("transactionDestTypeIntervenant", type1)); if
	 * (isResponsable) { // Courriers de mes subordonnés if (sub.equals("Oui"))
	 * { if(!listIdSubordonnes.isEmpty()){ for (Integer idSub :
	 * listIdSubordonnes) { listSub.add("sub_" + idSub); } critD =
	 * Restrictions.or(critD, Restrictions.in("transactionDestTypeIntervenant",
	 * listSub)); } } // Courriers de mes sous unités if (unit.equals("Oui")) {
	 * if(!listIdsSousUnites.isEmpty()){ for (Integer idSousUnit :
	 * listIdsSousUnites) { listUnit.add("unit_" + idSousUnit); } critD =
	 * Restrictions.or(critD, Restrictions.in("transactionDestTypeIntervenant",
	 * listUnit)); } } if (secretaire.equals("Oui")) { critD =
	 * Restrictions.or(critD, Restrictions.eq( "transactionDestTypeIntervenant",
	 * typeSecretaire)); } } } else { if (isResponsable) { switch
	 * (courrierRubriqueId) { case 1: critD =
	 * Restrictions.eq("transactionDestTypeIntervenant", type1); break; case 2:
	 * critD = Restrictions.eq("transactionDestTypeIntervenant", type); break;
	 * case 3: if(!listIdSubordonnes.isEmpty()){ for (Integer idSub :
	 * listIdSubordonnes) { listSub.add("sub_" + idSub); } critD =
	 * Restrictions.in("transactionDestTypeIntervenant", listSub); } break; case
	 * 4: critD = Restrictions.eq("transactionDestTypeIntervenant",
	 * typeSecretaire); break; case 5: if(!listIdsSousUnites.isEmpty()){ for
	 * (Integer idSousUnit : listIdsSousUnites) { listUnit.add("unit_" +
	 * idSousUnit); } critD = Restrictions.in("transactionDestTypeIntervenant",
	 * listUnit); } break; case 6: critD = Restrictions.or(Restrictions.eq(
	 * "transactionDestTypeIntervenant", type1),
	 * Restrictions.eq("transactionDestTypeIntervenant", type)); // subordonné
	 * if(!listIdSubordonnes.isEmpty()){ for (Integer idSub : listIdSubordonnes)
	 * { listSub.add("sub_" + idSub); } critD = Restrictions.or(critD,
	 * Restrictions.in("transactionDestTypeIntervenant", listSub)); } //
	 * secretary critD = Restrictions.or(critD, Restrictions.eq(
	 * "transactionDestTypeIntervenant", typeSecretaire)); // sous unité
	 * if(!listIdsSousUnites.isEmpty()){ for (Integer idSousUnit :
	 * listIdsSousUnites) { listUnit.add("unit_" + idSousUnit); } critD =
	 * Restrictions.or(critD, Restrictions.in("transactionDestTypeIntervenant",
	 * listUnit)); } } } else { // Agent ou Secretaire critD =
	 * Restrictions.or(Restrictions.eq("transactionDestTypeIntervenant", type),
	 * Restrictions.eq("transactionDestTypeIntervenant", type1)); } }
	 * detachedCriteriaTrDest.add(critD);
	 * 
	 * // ********************END Recu Criteria criteria =
	 * getSession().createCriteria(Transaction.class);
	 * criteria.createAlias("dossier", "dossier")
	 * .createAlias("dossier.courriers", "dCourrier")
	 * .createAlias("dCourrier.nature", "nature") .createAlias("etat", "etat");
	 * criteria.add(Restrictions.eq("dossier.typedossier.typeDossierId", 1));
	 * criteria
	 * .setProjection(Projections.countDistinct("dCourrier.idCourrier"));
	 * 
	 * Criterion crit = null; listSub = new ArrayList<String>(); listUnit = new
	 * ArrayList<String>(); if (forStat) { crit = Restrictions.or(
	 * Restrictions.eq("transactionTypeIntervenant", type),
	 * Restrictions.eq("transactionTypeIntervenant", type1)); if (isResponsable)
	 * { // Courriers de mes subordonnés if (sub.equals("Oui")) {
	 * if(!listIdSubordonnes.isEmpty()){ for (Integer idSub : listIdSubordonnes)
	 * { listSub.add("sub_" + idSub); } crit = Restrictions.or(crit,
	 * Restrictions.in("transactionTypeIntervenant", listSub)); } } // Courriers
	 * de mes sous unités if (unit.equals("Oui")) {
	 * if(!listIdsSousUnites.isEmpty()){ for (Integer idSousUnit :
	 * listIdsSousUnites) { listUnit.add("unit_" + idSousUnit); } crit =
	 * Restrictions.or(crit, Restrictions.in("transactionTypeIntervenant",
	 * listUnit)); } } if (secretaire.equals("Oui")) { crit =
	 * Restrictions.or(crit, Restrictions.eq( "transactionTypeIntervenant",
	 * typeSecretaire)); } } } else { if (isResponsable) { switch
	 * (courrierRubriqueId) { case 1: crit =
	 * Restrictions.eq("transactionTypeIntervenant", type1); break; case 2: crit
	 * = Restrictions.eq("transactionTypeIntervenant", type); break; case 3: //
	 * Courriers de mes subordonnés if(!listIdSubordonnes.isEmpty()){ for
	 * (Integer idSub : listIdSubordonnes) { listSub.add("sub_" + idSub); } crit
	 * = Restrictions.in("transactionTypeIntervenant", listSub); } break; case
	 * 4: crit = Restrictions.eq("transactionTypeIntervenant", typeSecretaire);
	 * break; case 5: if(!listIdsSousUnites.isEmpty()){ for (Integer idSousUnit
	 * : listIdsSousUnites) { listUnit.add("unit_" + idSousUnit); } crit =
	 * Restrictions.in("transactionTypeIntervenant", listUnit); } break; case 6:
	 * crit = Restrictions.or( Restrictions.eq("transactionTypeIntervenant",
	 * type1), Restrictions.eq("transactionTypeIntervenant", type)); //
	 * subordonné if(!listIdSubordonnes.isEmpty()){ for (Integer idSub :
	 * listIdSubordonnes) { listSub.add("sub_" + idSub); } crit =
	 * Restrictions.or(crit, Restrictions.in("transactionTypeIntervenant",
	 * listSub)); } // secretaire crit = Restrictions.or(crit, Restrictions.eq(
	 * "transactionTypeIntervenant", typeSecretaire)); // sous unité
	 * if(!listIdsSousUnites.isEmpty()){ for (Integer idSousUnit :
	 * listIdsSousUnites) { listUnit.add("unit_" + idSousUnit); } crit =
	 * Restrictions.or(crit, Restrictions.in("transactionTypeIntervenant",
	 * listUnit)); } } } else { // Agent ou Secretaire crit = Restrictions.or(
	 * Restrictions.eq("transactionTypeIntervenant", type),
	 * Restrictions.eq("transactionTypeIntervenant", type1)); } } //
	 * criteria.add(crit); if (typeCourrier.equals("Recu") ||
	 * stateTraitement.equals("Avalider")) {
	 * criteria.add(Property.forName("transactionId").in(
	 * detachedCriteriaTrDest)); } else if (typeCourrier.equals("Envoyes")) {
	 * criteria.add(crit); } else { criteria.add(Restrictions.or(crit, Property
	 * .forName("transactionId").in(detachedCriteriaTrDest))); } // etat => A
	 * valider if (stateTraitement.equals("Avalider")) { Integer[] etatAvalider
	 * = { 2, 10 }; criteria.add(Restrictions.in("etat.etatId", etatAvalider));
	 * }
	 * 
	 * if (jourOrAutre == 1) {
	 * criteria.add(Restrictions.between("transactionDateTransaction",
	 * dateDebut, dateFin)); } if (jourOrAutre == 2) {
	 * criteria.add(Restrictions.lt("transactionDateTransaction", dateDebut)); }
	 * 
	 * return (Long) criteria.uniqueResult(); }
	 */

	@SuppressWarnings("unchecked")
	@Override
	public Long CountAllCourrierEnvoyerANDRecuWithCriterion(
			boolean isResponsable, List<Integer> listIdsSousUnites,
			List<Integer> listIdSubordonnes, HashMap<String, Object> filterMap,
			String secretaire, String sub, String unit, int jourOrAutre,
			Date dateDebut, Date dateFin, String type, String type1,
			String typeSecretaire, Integer idUser, Integer typeTransmission,
			String stateTraitement, Integer courrierRubriqueId,
			boolean forStat, String typeCourrier) {
		Map<String, Object> pars = new HashMap<String, Object>();
		List<String> listRecu = new ArrayList<String>();
		List<String> listEnv = new ArrayList<String>();

		// ****** Courrier Recu
		if (typeCourrier.equals("Recu") || stateTraitement.equals("Avalider")
				|| typeCourrier.equals("Tous")) {
			if (isResponsable) {
				switch (courrierRubriqueId) {
				case 1:
					listRecu.add(type1);
					break;
				case 2:
					listRecu.add(type);
					break;
				case 3:
					if (listIdSubordonnes != null
							&& listIdSubordonnes.size() > 0) {
						for (Integer idSub : listIdSubordonnes) {
							listRecu.add("sub_" + idSub);
						}
					} else {
						listRecu.add("sub_xx99999xx");
					}

					break;
				case 4:
					listRecu.add(typeSecretaire);
					break;
				case 5:

					if (listIdsSousUnites != null
							&& listIdsSousUnites.size() > 0) {
						for (Integer idSousUnit : listIdsSousUnites) {
							listRecu.add("unit_" + idSousUnit);
						}
					} else {
						listRecu.add("unit_xx99999xx");
					}
					break;
				case 6:
					if(listIdSubordonnes!=null)
					for (Integer idSub : listIdSubordonnes) {
						listRecu.add("sub_" + idSub);
					}
					if(listIdsSousUnites!=null)
					for (Integer idSousUnit : listIdsSousUnites) {
						listRecu.add("unit_" + idSousUnit);
					}
					if (secretaire.equals("Oui")) {
						listRecu.add(typeSecretaire);
					}
					if (type != null) {
						listRecu.add(type);
					}
					if (type1 != null) {
						listRecu.add(type1);
					}
				}
			} else {
				listRecu.add(type);
				listRecu.add(type1);
			}
		}
		// ****** Courrier Envoye
		if (typeCourrier.equals("Envoyes") || typeCourrier.equals("Tous")) {
			if (isResponsable) {
				switch (courrierRubriqueId) {
				case 1:
					listEnv.add(type1);
					break;
				case 2:
					listEnv.add(type);
					break;
				case 3:

					if (!listIdSubordonnes.isEmpty()) {
						for (Integer idSub : listIdSubordonnes) {
							listEnv.add("sub_" + idSub);
						}
					} else {
						listEnv.add("sub_xx99999xx");
					}
					break;
				case 4:
					listEnv.add(typeSecretaire);
					break;
				case 5:
					if (listIdsSousUnites != null
							&& listIdsSousUnites.size() > 0) {
						for (Integer idSousUnit : listIdsSousUnites) {
							listEnv.add("unit_" + idSousUnit);
						}
					} else {
						System.out.println("PAS DES SOUS-UNITES");
						listEnv.add("unit_xx99999xx");
					}
					break;
				case 6:
					if(listIdSubordonnes!=null)
					for (Integer idSub : listIdSubordonnes) {
						listEnv.add("sub_" + idSub);
					}if(listIdsSousUnites!=null)
					for (Integer idSousUnit : listIdsSousUnites) {
						listEnv.add("unit_" + idSousUnit);
					}
					if (secretaire.equals("Oui")) {
						listEnv.add(typeSecretaire);
					}
					if (type != null) {
						listEnv.add(type);
					}
					if (type1 != null) {
						listEnv.add(type1);
					}
				}
			} else {
				listEnv.add(type);
				listEnv.add(type1);
			}
		}
		Calendar calendar = Calendar.getInstance();
		Date dateAujourd = calendar.getTime();
		StringBuffer trCount = new StringBuffer(
				"SELECT COUNT(DISTINCT d.dossierId) AS 'nbrc'"
						+ " FROM transactionn t"
						+ " INNER JOIN dossier d ON t.dossierId = d.dossierId"
						+ " INNER JOIN courrierdossier cd ON d.dossierId = cd.dossierId"
						+ " INNER JOIN courrier c ON cd.idCourrier = c.idCourrier"
						+ " LEFT JOIN transactiondest td ON t.transactionId = td.idTransaction"
						+ " WHERE d.typeDossierId = 1"
						+ " AND c.courrierFormat is null ");
		if (jourOrAutre == 1) {
			trCount.append(" AND t.transactionDateTransaction BETWEEN :dateDebut AND :dateFin");
			pars.put("dateDebut", dateDebut);
			pars.put("dateFin", dateFin);
		}

		if (jourOrAutre == 2) {
			trCount.append(" AND t.transactionDateTransaction < :dateDebut");
			pars.put("dateDebut", dateDebut);
		}
		if (jourOrAutre == 3) {
			trCount.append(" AND t.transactionDateTransaction =:dateDebut");
			pars.put("dateDebut", dateDebut);
		}
		// ----------------------------------MM-------------------------------
		// --------------------------Debut Changement pour op-----------------
		// par année

		if (jourOrAutre == 11) {
			trCount.append(" AND c.courrierDateReceptionAnnee =:dateDebut");

			pars.put("dateDebut", dateDebut.getYear() + 1900);
		}
		// par annee
		if (jourOrAutre == 12) {
			trCount.append(" AND c.courrierDateReceptionAnnee =:dateAjourdhuit");
			pars.put("dateAjourdhuit", dateAujourd.getYear() + 1900);

		}
		// par mois
		if (jourOrAutre == 13) {
			trCount.append(" AND c.courrierDateReceptionAnnee =:anneeEnCours AND c.courrierDateReceptionMois =:moisEnCours");
			pars.put("moisEnCours", dateAujourd.getMonth() + 1);
			pars.put("anneeEnCours", dateAujourd.getYear() + 1900);

		}
		// par jour
		// 2019-06-24
		if (jourOrAutre == 14) {
			trCount.append(" AND DATE_FORMAT(c.courrierDateReception,'%Y-%m-%d') =:dateDebut ");

			// JS :Formatter date Début **********************************
			System.out.println("Date Début Avant formattage :" + dateDebut);
			SimpleDateFormat formatDebut = new SimpleDateFormat("yyyy-MM-dd");
			String dateD = formatDebut.format(dateDebut);
			pars.put("dateDebut", dateD);
			pars.put("dateDebut", dateD);

		}
		// ---------------------------Fin changement op-----------------------
		int i = 0;
		String where = "";
		if ((typeCourrier.equals("Recu") || stateTraitement.equals("Avalider"))
				&& listRecu.size() > 0) {
			for (String recu : listRecu) {
				where += " :recu" + i + ", ";
				pars.put("recu" + i, recu);
				i++;
			}
			if (where.length() > 0) {
				trCount.append(" AND td.transactionDestTypeIntervenant IN ("
						+ where.substring(0, where.length() - 2) + ")");
			}
		} else if (typeCourrier.equals("Envoyes") && listEnv.size() > 0) {
			for (String env : listEnv) {
				where += " :env" + i + ", ";
				pars.put("env" + i, env);
				i++;
			}
			if (where.length() > 0) {
				trCount.append(" AND t.transactionTypeIntervenant IN ("
						+ where.substring(0, where.length() - 2) + ")");
			}
		} else if (typeCourrier.equals("Tous")
				&& (listEnv.size() > 0 || listRecu.size() > 0)) {
			trCount.append(" AND (");
			for (String env : listEnv) {
				where += " :env" + i + ", ";
				pars.put("env" + i, env);
				i++;
			}
			if (where.length() > 0) {
				trCount.append(" t.transactionTypeIntervenant IN ("
						+ where.substring(0, where.length() - 2) + ")");
			}
			where = "";
			for (String recu : listRecu) {
				where += " :recu" + i + ", ";
				pars.put("recu" + i, recu);
				i++;
			}
			if (listEnv.size() > 0) {
				trCount.append(" OR ");
			}
			if (where.length() > 0) {
				trCount.append(" td.transactionDestTypeIntervenant IN ("
						+ where.substring(0, where.length() - 2) + ")");
			}
			trCount.append(") ");
		}
		// Fin Restrictions

		// etat => A valider
		if (stateTraitement.equals("Avalider")) {
			trCount.append(" AND t.etatId IN (2, 10)");
		}

		SQLQuery query = getSession().createSQLQuery(trCount.toString());

		query.addScalar("nbrc", new LongType());

		Iterator<String> iterMax = pars.keySet().iterator();
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
//			System.out.println("liste Ranne ===> name ==> "+name );
//			System.out.println("list value ====> value ==> "+value);
			if (pars.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}

//		System.out.println("List Anne ==============================>"+query.getQueryString());
//		System.out.println("Nombre===============> "+query.uniqueResult());
		return (Long) query.uniqueResult();
	}

	@Override
	public List<TransactionDestination> findCourrierRecuWithCriterion(
			boolean isResponsable, List<Integer> listIdsSousUnites,
			List<Integer> listIdSubordonnes, HashMap<String, Object> filterMap,
			String sortField, boolean descending, String secretaire,
			String sub, String unit, int jourOrAutre, Date dateDebut,
			Date dateFin, String type, String type1, String typeSecretaire,
			Integer idUser, Integer typeTransmission, String stateTraitement,
			int firstIndex, int maxResult, boolean forRapport,
			Integer courrierRubriqueId) {
		// Criteria
		// Criteria criteriaTr = getSession().createCriteria(Transaction.class);
		// criteriaTr.createAlias("dossier", "dossier")
		// .createAlias("dossier.courriers", "dCourrier")
		// .createAlias("dCourrier.nature", "nature")
		// .createAlias("etat", "etat");
		// criteriaTr.setProjection(Projections
		// .projectionList()
		// .add(Projections
		// .property("dCourrier.courrierReferenceCorrespondant"),
		// "courrierReference")
		// .add(Projections.property("nature.natureLibelle"),
		// "courrierNature")
		// .add(Projections.property("dCourrier.courrierObjet"),
		// "courrierObjet")
		// .add(Projections.property("transactionDateTransaction"),
		// "courrierDateReceptionEnvoi")
		// .add(Projections.property("etat.etatId"), "etatID")
		// .add(Projections
		// .property("transactionDestinationReelle.transactionDestinationReelleId"),
		// "transactionDestinationReelID")
		// .add(Projections.property("transactionDateConsultation"),
		// "transactionDateConsultation")
		// .add(Projections.property("transactionId"), "transactionID")
		// .add(Projections.property("expdest"), "expDest"));
		//
		// criteriaTr.add(Restrictions.eq("dossier.typedossier.typeDossierId",
		// 1));
		// if (stateTraitement.equals("Avalider")) {
		// criteriaTr.add(Restrictions.eq("etat.etatId", 2));
		// }
		// if (jourOrAutre == 1) {
		// criteriaTr.add(Restrictions.between(
		// "transactionDateTransaction", dateDebut, dateFin));
		// }
		// if (jourOrAutre == 2) {
		// criteriaTr.add(Restrictions.lt(
		// "transactionDateTransaction", dateDebut));
		// }
		// //DetachedCriteria
		// DetachedCriteria detachedCriteriaTrDest = DetachedCriteria
		// .forClass(TransactionDestination.class);
		// detachedCriteriaTrDest.setProjection(Projections.projectionList()
		// .add(Projections.property("id.idTransaction"), "idTransaction"));
		// Criterion crit = null;
		// if (isResponsable) {
		// switch (courrierRubriqueId) {
		// case 1:
		// crit = Restrictions.eq("transactionDestTypeIntervenant", type1);
		// break;
		// case 2:
		// crit = Restrictions.eq("transactionDestTypeIntervenant", type);
		// break;
		// case 3:
		// for (Integer idSub : listIdSubordonnes) {
		// if (crit == null) {
		// crit = Restrictions.eq(
		// "transactionDestTypeIntervenant", "sub_"
		// + idSub);
		// } else {
		// crit = Restrictions.or(crit, Restrictions.eq(
		// "transactionDestTypeIntervenant", "sub_"
		// + idSub));
		// }
		//
		// }
		// break;
		// case 4:
		// crit = Restrictions.eq("transactionDestTypeIntervenant",
		// typeSecretaire);
		// break;
		// case 5:
		// for (Integer idSousUnit : listIdsSousUnites) {
		// if (crit == null) {
		// crit = Restrictions.eq(
		// "transactionDestTypeIntervenant", "unit_"
		// + idSousUnit);
		// } else {
		// crit = Restrictions.or(crit, Restrictions.eq(
		// "transactionDestTypeIntervenant", "unit_"
		// + idSousUnit));
		// }
		// }
		// break;
		// }
		//
		// } else {
		// // Agent ou Secretaire
		// crit = Restrictions.or(
		// Restrictions.eq("transactionDestTypeIntervenant", type),
		// Restrictions.eq("transactionDestTypeIntervenant", type1));
		// }
		// detachedCriteriaTrDest.add(crit);
		// criteriaTr.add(Property.forName("transactionId").in(
		// detachedCriteriaTrDest));
		//
		//
		// criteriaTr.setResultTransformer(Transformers
		// .aliasToBean(CourrierInformations.class));
		// List<Transaction> lstrecu = criteriaTr.list();
		// for (Transaction transaction : lstrecu) {
		// System.out.println("#" + transaction.getTransactionId());
		// }

		// return criteriaTr.list();
		return new ArrayList<TransactionDestination>();
	}

	// @Override
	@SuppressWarnings("unchecked")
	public List<TransactionDestination> findCourrierRecuWithCriterio(
			boolean isResponsable, List<Integer> listIdsSousUnites,
			List<Integer> listIdSubordonnes, HashMap<String, Object> filterMap,
			String sortField, boolean descending, String secretaire,
			String sub, String unit, int jourOrAutre, Date dateDebut,
			Date dateFin, String type, String type1, String typeSecretaire,
			Integer idUser, Integer typeTransmission, String stateTraitement,
			int firstIndex, int maxResult, boolean forRapport,
			Integer courrierRubriqueId) {
		// ****************** OLD Implementation ******************************
		// boolean filterContainsDateTransaction = false;
		DetachedCriteria detachedCriteriaTransaction = DetachedCriteria
				.forClass(Transaction.class);
		detachedCriteriaTransaction.createAlias("dossier", "dossier")
				.createAlias("dossier.courriers", "dCourrier")
				.createAlias("dCourrier.nature", "nature");
		detachedCriteriaTransaction.add(Restrictions.eq(
				"dossier.typedossier.typeDossierId", 1));
		detachedCriteriaTransaction.setProjection(Projections.projectionList()
				.add(Projections.property("transactionId"), "idTransaction"));
		// etat => A valider
		if (stateTraitement.equals("Avalider")) {
			detachedCriteriaTransaction.add(Restrictions.eq("etat.etatId", 2));
		}
		// if (!filterMap.entrySet().isEmpty()) {
		// for (java.util.Map.Entry<String, Object> filter : filterMap
		// .entrySet()) {
		// if (filter.getKey().equals("courrierDateReceptionEnvoi")) {
		// filterContainsDateTransaction = true;
		// break;
		// }
		// }
		//
		// }
		// if (filterContainsDateTransaction == false) {

		if (jourOrAutre == 1) {
			detachedCriteriaTransaction.add(Restrictions.between(
					"transactionDateTransaction", dateDebut, dateFin));
		}
		if (jourOrAutre == 2) {
			detachedCriteriaTransaction.add(Restrictions.lt(
					"transactionDateTransaction", dateDebut));
		}
		// }
		Criteria criteriaDestination = getSession().createCriteria(
				TransactionDestination.class);
		// criteriaDestination.add(Restrictions.or(Restrictions.eq(
		// "transactionDestIdIntervenant", idUser), Restrictions.or(
		// Restrictions.eq("transactionDestTypeIntervenant", type),
		// Restrictions.eq("transactionDestTypeIntervenant", type1))));
		// NEW

		// Mes Courriers et les courriers de mon unité
		// Criterion crit = Restrictions.or(
		// Restrictions.eq("transactionDestTypeIntervenant", type),
		// Restrictions.eq("transactionDestTypeIntervenant", type1));
		Criterion crit = null;
		if (isResponsable) {
			switch (courrierRubriqueId) {
			case 1:
				crit = Restrictions.eq("transactionDestTypeIntervenant", type1);
				break;
			case 2:
				crit = Restrictions.eq("transactionDestTypeIntervenant", type);
				break;
			case 3:
				if(listIdSubordonnes!=null)
				for (Integer idSub : listIdSubordonnes) {
					if (crit == null) {
						crit = Restrictions.eq(
								"transactionDestTypeIntervenant", "sub_"
										+ idSub);
					} else {
						crit = Restrictions.or(crit, Restrictions.eq(
								"transactionDestTypeIntervenant", "sub_"
										+ idSub));
					}

				}
				break;
			case 4:
				crit = Restrictions.eq("transactionDestTypeIntervenant",
						typeSecretaire);
				break;
			case 5:
				if(listIdsSousUnites!=null)
				for (Integer idSousUnit : listIdsSousUnites) {
					if (crit == null) {
						crit = Restrictions.eq(
								"transactionDestTypeIntervenant", "unit_"
										+ idSousUnit);
					} else {
						crit = Restrictions.or(crit, Restrictions.eq(
								"transactionDestTypeIntervenant", "unit_"
										+ idSousUnit));
					}
				}
				break;
			}

		} else {
			// Agent ou Secretaire
			crit = Restrictions.or(
					Restrictions.eq("transactionDestTypeIntervenant", type),
					Restrictions.eq("transactionDestTypeIntervenant", type1));
		}
		// // Courriers de mes subordonnés
		// if (sub.equals("Oui")) {
		// for (Integer idSub : listIdSubordonnes) {
		// crit = Restrictions.or(crit, Restrictions.eq(
		// "transactionDestTypeIntervenant", "sub_" + idSub));
		// }
		// }
		// Courriers de mes sous unités
		// if (unit.equals("Oui")) {
		// for (Integer idSousUnit : listIdsSousUnites) {
		// crit = Restrictions.or(crit, Restrictions.eq(
		// "transactionDestTypeIntervenant", "unit_"
		// + idSousUnit));
		// }
		// }
		// if (secretaire.equals("Oui")) {
		// crit = Restrictions.or(crit, Restrictions.eq(
		// "transactionDestTypeIntervenant", typeSecretaire));
		// }
		criteriaDestination.add(crit);
		criteriaDestination.add(Property.forName("id.idTransaction").in(
				detachedCriteriaTransaction));
		if (!forRapport) {
			criteriaDestination.setFirstResult(firstIndex);
			criteriaDestination.setMaxResults(maxResult);
		}
		return criteriaDestination.list();
		// FIN NEW
		// filter and sort
		// if (sortField != null) {
		// if (sortField.equals("courrierReference")) {
		// if (descending == true) {
		// detachedCriteriaTransaction.addOrder(Order.desc("crr"));
		// } else {
		// detachedCriteriaTransaction.addOrder(Order.asc("crr"));
		// }
		// } else if (sortField.equals("courrierObjet")) {
		// if (descending == true) {
		// detachedCriteriaTransaction.addOrder(Order.desc("dCourrier.courrierObjet"));
		// } else {
		// detachedCriteriaTransaction.addOrder(Order.asc("dCourrier.courrierObjet"));
		// }
		// } else if (sortField.equals("courrierNature")) {
		// if (descending == true) {
		// detachedCriteriaTransaction.addOrder(Order.desc("nature.natureLibelle"));
		// } else {
		// detachedCriteriaTransaction.addOrder(Order.asc("nature.natureLibelle"));
		// }
		// } else if (sortField.equals("courrierDateReceptionEnvoi")) {
		// if (descending == true) {
		// detachedCriteriaTransaction.addOrder(Order.desc("transactionDateTransaction"));
		// } else {
		// detachedCriteriaTransaction.addOrder(Order.asc("transactionDateTransaction"));
		// }
		// }
		// }
		// for (java.util.Map.Entry<String, Object> filter :
		// filterMap.entrySet()) {
		// System.out.println("FILTER*********");
		// System.out.println("KEY : " + filter.getKey());
		// System.out.println("Value : " + filter.getValue());
		// if (filter.getKey().equals("courrierReference")) {
		// detachedCriteriaTransaction.add(Restrictions.like(
		// "dCourrier.courrierReferenceCorrespondant", "%"
		// + filter.getValue() + "%"));
		// } else if (filter.getKey().equals("courrierObjet")) {
		// detachedCriteriaTransaction.add(Restrictions.like(
		// "dCourrier.courrierObjet", "%" + filter.getValue()
		// + "%"));
		// } else if (filter.getKey().equals("courrierNature")) {
		// detachedCriteriaTransaction.add(Restrictions.like(
		// "nature.natureLibelle", "%" + filter.getValue() + "%"));
		// } else if (filter.getKey().equals("courrierDateReceptionEnvoi")) {
		// detachedCriteriaTransaction.add(Restrictions.eq(
		// "transactionDateTransaction", filter.getValue()));
		// }
		//
		// }
		// fin filter and sort

		// *************** OLD Implementation **************************
	}

	@Override
	public Long countCourrierRecuWithCriterion(boolean isResponsable,
			List<Integer> listIdsSousUnites, List<Integer> listIdSubordonnes,
			HashMap<String, Object> filterMap, String secretaire, String sub,
			String unit, int jourOrAutre, Date dateDebut, Date dateFin,
			String type, String type1, String typeSecretaire, Integer idUser,
			Integer typeTransmission, String stateTraitement,
			Integer courrierRubriqueId, boolean forStat) {

		DetachedCriteria detachedCriteriaTransaction = DetachedCriteria
				.forClass(Transaction.class);
		detachedCriteriaTransaction.createAlias("dossier", "dossier")
				.createAlias("dossier.courriers", "dCourrier")
				.createAlias("dCourrier.nature", "nature");
		detachedCriteriaTransaction.setProjection(Projections.projectionList()
				.add(Projections.property("transactionId"), "idTransaction"));

		detachedCriteriaTransaction.add(Restrictions.eq(
				"dossier.typedossier.typeDossierId", 1));

		// etat => A valider
		if (stateTraitement.equals("Avalider")) {
			detachedCriteriaTransaction.add(Restrictions.eq("etat.etatId", 2));
		}
		if (jourOrAutre == 1) {
			detachedCriteriaTransaction.add(Restrictions.between(
					"transactionDateTransaction", dateDebut, dateFin));
		}
		if (jourOrAutre == 2) {
			detachedCriteriaTransaction.add(Restrictions.lt(
					"transactionDateTransaction", dateDebut));
		}

		Criteria criteriaDestination = getSession().createCriteria(
				TransactionDestination.class);
		criteriaDestination.add(Restrictions.or(Restrictions.eq(
				"transactionDestIdIntervenant", idUser), Restrictions.or(
				Restrictions.eq("transactionDestTypeIntervenant", type),
				Restrictions.eq("transactionDestTypeIntervenant", type1))));
		// Mes Courriers et les courriers de mon unité
		Criterion crit = null;
		if (forStat) {
			crit = Restrictions.or(
					Restrictions.eq("transactionDestTypeIntervenant", type),
					Restrictions.eq("transactionDestTypeIntervenant", type1));
			if (isResponsable) {
				// Courriers de mes subordonnés
				if (sub.equals("Oui")) {
					if(listIdSubordonnes!=null)
					for (Integer idSub : listIdSubordonnes) {
						crit = Restrictions.or(crit, Restrictions.eq(
								"transactionDestTypeIntervenant", "sub_"
										+ idSub));
					}
				}
				// Courriers de mes sous unités
				if (unit.equals("Oui")) {
					for (Integer idSousUnit : listIdsSousUnites) {
						crit = Restrictions.or(crit, Restrictions.eq(
								"transactionDestTypeIntervenant", "unit_"
										+ idSousUnit));
					}
				}
				if (secretaire.equals("Oui")) {
					crit = Restrictions.or(crit, Restrictions.eq(
							"transactionDestTypeIntervenant", typeSecretaire));
				}
			}
		} else {
			if (isResponsable) {
				switch (courrierRubriqueId) {
				case 1:
					crit = Restrictions.eq("transactionDestTypeIntervenant",
							type1);
					break;
				case 2:
					crit = Restrictions.eq("transactionDestTypeIntervenant",
							type);
					break;
				case 3:
					for (Integer idSub : listIdSubordonnes) {
						if (crit == null) {
							crit = Restrictions.eq(
									"transactionDestTypeIntervenant", "sub_"
											+ idSub);
						} else {
							crit = Restrictions.or(crit, Restrictions.eq(
									"transactionDestTypeIntervenant", "sub_"
											+ idSub));
						}

					}
					break;
				case 4:
					crit = Restrictions.eq("transactionDestTypeIntervenant",
							typeSecretaire);
					break;
				case 5:
					for (Integer idSousUnit : listIdsSousUnites) {
						if (crit == null) {
							crit = Restrictions.eq(
									"transactionDestTypeIntervenant", "unit_"
											+ idSousUnit);
						} else {
							crit = Restrictions.or(crit, Restrictions.eq(
									"transactionDestTypeIntervenant", "unit_"
											+ idSousUnit));
						}
					}
					break;
				}

			} else {
				// Agent ou Secretaire
				crit = Restrictions
						.or(Restrictions.eq("transactionDestTypeIntervenant",
								type), Restrictions.eq(
								"transactionDestTypeIntervenant", type1));
			}
		}
		criteriaDestination.add(crit);
		criteriaDestination.add(Property.forName("id.idTransaction").in(
				detachedCriteriaTransaction));
		criteriaDestination.setProjection(Projections.rowCount());
		return (Long) criteriaDestination.uniqueResult();
	}

	@Override
	public Long countCourrierRecuByUrgenceWithCriterion(boolean isResponsable,
			List<Integer> listIdsSousUnites, List<Integer> listIdSubordonnes,
			String secretaire, String sub, String unit, String type,
			String type1, String typeSecretaire, Integer idUser,
			Integer idUrgence) {
		// ********
		DetachedCriteria detachedCriteriaTrDest = DetachedCriteria
				.forClass(TransactionDestination.class);
		detachedCriteriaTrDest.setProjection(Projections.projectionList().add(
				Projections.property("id.idTransaction"), "idTransaction"));
		detachedCriteriaTrDest.add(Restrictions.or(Restrictions.eq(
				"transactionDestIdIntervenant", idUser), Restrictions.or(
				Restrictions.eq("transactionDestTypeIntervenant", type),
				Restrictions.eq("transactionDestTypeIntervenant", type1))));
		// Mes Courriers et les courriers de mon unité
		Criterion crit = Restrictions.or(
				Restrictions.eq("transactionDestTypeIntervenant", type),
				Restrictions.eq("transactionDestTypeIntervenant", type1));
		if (isResponsable) {
			// Courriers de mes subordonnés
			if (sub.equals("Oui")) {
				for (Integer idSub : listIdSubordonnes) {
					crit = Restrictions.or(crit, Restrictions.eq(
							"transactionDestTypeIntervenant", "sub_" + idSub));
				}
			}
			// Courriers de mes sous unités
			if (unit.equals("Oui")) {
				for (Integer idSousUnit : listIdsSousUnites) {
					crit = Restrictions.or(crit, Restrictions.eq(
							"transactionDestTypeIntervenant", "unit_"
									+ idSousUnit));
				}
			}
			if (secretaire.equals("Oui")) {
				crit = Restrictions.or(crit, Restrictions.eq(
						"transactionDestTypeIntervenant", typeSecretaire));
			}
		}
		detachedCriteriaTrDest.add(crit);
		Criteria criteriaTr = getSession().createCriteria(Transaction.class);
		criteriaTr.createAlias("dossier", "dossier")
				.createAlias("dossier.courriers", "dCourrier")
				.createAlias("dCourrier.nature", "nature")
				.createAlias("dCourrier.urgence", "urgence");
		criteriaTr.add(Restrictions.eq("dossier.typedossier.typeDossierId", 1));
		criteriaTr.add(Restrictions.eq("urgence.urgenceId", idUrgence));
		criteriaTr.add(Property.forName("transactionId").in(
				detachedCriteriaTrDest));
		criteriaTr.setProjection(Projections.rowCount());
		return (Long) criteriaTr.uniqueResult();
		// ********
		// 564654
		// DetachedCriteria detachedCriteriaTransaction = DetachedCriteria
		// .forClass(Transaction.class);
		// detachedCriteriaTransaction.createAlias("dossier", "dossier")
		// .createAlias("dossier.courriers", "dCourrier")
		// .createAlias("dCourrier.nature", "nature")
		// .createAlias("dCourrier.urgence", "urgence");
		// detachedCriteriaTransaction.add(Restrictions.eq(
		// "dossier.typedossier.typeDossierId", 1));
		// detachedCriteriaTransaction.setProjection(Projections.projectionList()
		// .add(Projections.property("transactionId"), "idTransaction"));
		// // Urgence
		// detachedCriteriaTransaction.add(Restrictions.eq("urgence.urgenceId",
		// idUrgence));
		//
		// // TransactionDestination
		// Criteria criteriaDestination = getSession().createCriteria(
		// TransactionDestination.class);
		// criteriaDestination.add(Restrictions.or(Restrictions.eq(
		// "transactionDestIdIntervenant", idUser), Restrictions.or(
		// Restrictions.eq("transactionDestTypeIntervenant", type),
		// Restrictions.eq("transactionDestTypeIntervenant", type1))));
		// // Mes Courriers et les courriers de mon unité
		// Criterion crit = Restrictions.or(
		// Restrictions.eq("transactionDestTypeIntervenant", type),
		// Restrictions.eq("transactionDestTypeIntervenant", type1));
		// if (isResponsable) {
		// // Courriers de mes subordonnés
		// if (sub.equals("Oui")) {
		// for (Integer idSub : listIdSubordonnes) {
		// crit = Restrictions.or(crit, Restrictions.eq(
		// "transactionDestTypeIntervenant", "sub_" + idSub));
		// }
		// }
		// // Courriers de mes sous unités
		// if (unit.equals("Oui")) {
		// for (Integer idSousUnit : listIdsSousUnites) {
		// crit = Restrictions.or(crit, Restrictions.eq(
		// "transactionDestTypeIntervenant", "unit_"
		// + idSousUnit));
		// }
		// }
		// if (secretaire.equals("Oui")) {
		// crit = Restrictions.or(crit, Restrictions.eq(
		// "transactionDestTypeIntervenant", typeSecretaire));
		// }
		// }
		// criteriaDestination.add(crit);
		//
		// criteriaDestination.add(Property.forName("id.idTransaction").in(
		// detachedCriteriaTransaction));
		// criteriaDestination.setProjection(Projections.rowCount());
		// return (Long) criteriaDestination.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> countCourrierRecuByConfOrUrgWithCriterion(
			boolean isResponsable, List<Integer> listIdsSousUnites,
			List<Integer> listIdSubordonnes, String secretaire, String sub,
			String unit, String type, String type1, String typeSecretaire,
			Integer idUser, String tab) {
		// **************
		DetachedCriteria detachedCriteriaTrDest = DetachedCriteria
				.forClass(TransactionDestination.class);
		detachedCriteriaTrDest.setProjection(Projections.projectionList().add(
				Projections.property("id.idTransaction"), "idTransaction"));
		detachedCriteriaTrDest.add(Restrictions.or(Restrictions.eq(
				"transactionDestIdIntervenant", idUser), Restrictions.or(
				Restrictions.eq("transactionDestTypeIntervenant", type),
				Restrictions.eq("transactionDestTypeIntervenant", type1))));
		// Mes Courriers et les courriers de mon unité
		Criterion crit = Restrictions.or(
				Restrictions.eq("transactionDestTypeIntervenant", type),
				Restrictions.eq("transactionDestTypeIntervenant", type1));
		if (isResponsable) {
			// Courriers de mes subordonnés
			if (sub.equals("Oui")) {
				for (Integer idSub : listIdSubordonnes) {
					crit = Restrictions.or(crit, Restrictions.eq(
							"transactionDestTypeIntervenant", "sub_" + idSub));
				}
			}
			// Courriers de mes sous unités
			if (unit.equals("Oui")) {
				for (Integer idSousUnit : listIdsSousUnites) {
					crit = Restrictions.or(crit, Restrictions.eq(
							"transactionDestTypeIntervenant", "unit_"
									+ idSousUnit));
				}
			}
			if (secretaire.equals("Oui")) {
				crit = Restrictions.or(crit, Restrictions.eq(
						"transactionDestTypeIntervenant", typeSecretaire));
			}
		}
		detachedCriteriaTrDest.add(crit);
		Criteria criteriaTr = getSession().createCriteria(Transaction.class);
		criteriaTr.createAlias("dossier", "dossier").createAlias(
				"dossier.courriers", "dCourrier");

		// Confidentialite OR Urgence
		if (tab == "c") {
			criteriaTr.createAlias("dCourrier.confidentialite",
					"confidentialite");
		} else {
			criteriaTr.createAlias("dCourrier.urgence", "urgence");
		}
		criteriaTr.add(Restrictions.eq("dossier.typedossier.typeDossierId", 1));
		criteriaTr.add(Property.forName("transactionId").in(
				detachedCriteriaTrDest));
		// Confidentialite OR Urgence
		if (tab == "c") {
			criteriaTr
					.setProjection(Projections
							.projectionList()
							.add(Projections.countDistinct("transactionId"))
							.add(Projections
									.groupProperty("confidentialite.confidentialiteId")));
		} else {
			criteriaTr.setProjection(Projections.projectionList()
					.add(Projections.countDistinct("transactionId"))
					.add(Projections.groupProperty("urgence.urgenceId")));
		}
		criteriaTr.setResultTransformer(CriteriaSpecification.PROJECTION);

		CriteriaImpl criteriaImpl = (CriteriaImpl) criteriaTr;
		SessionImplementor session = criteriaImpl.getSession();
		SessionFactoryImplementor factory = session.getFactory();
		CriteriaQueryTranslator translator = new CriteriaQueryTranslator(
				factory, criteriaImpl, criteriaImpl.getEntityOrClassName(),
				CriteriaQueryTranslator.ROOT_SQL_ALIAS);
		String[] implementors = factory.getImplementors(criteriaImpl
				.getEntityOrClassName());

		CriteriaJoinWalker walker = new CriteriaJoinWalker(
				(OuterJoinLoadable) factory.getEntityPersister(implementors[0]),
				translator, factory, criteriaImpl, criteriaImpl
						.getEntityOrClassName(), session
						.getLoadQueryInfluencers());

		String sql = walker.getSQLString();
		System.out.println("countCourrierRecuByConfOrUrgWithCriterion "+sql);
		return criteriaTr.list();
	}

	@SuppressWarnings("unchecked")
	public List<CourrierInformations> findCourrierEnvoyerBOCWithCriterion(
			HashMap<String, Object> filterMap, String sortField,
			boolean descending, int jourOrAutre, Date dateDebut, Date dateFin,
			String type, String type1, List<Integer> listIdBocMembers,
			String typeTransmission, String stateTraitement, int firstIndex,
			int maxResult, String categorieCourrierJour, Boolean forRapport,
			String DBType, Integer idCourrierCourant, int flagueCloture,
			int flagInterne) {
		System.out.println("############ flagInterne == " + flagInterne);
		System.out.println("AH AH AH AH ");
		System.out.println("type  " + type);
		System.out.println(" type1  " + type1);
		System.out.println("###############jourOrAutre : " + jourOrAutre);
		System.out.println("typeTransmission : " + typeTransmission);
		System.out.println("categorieCourrierJour : " + categorieCourrierJour);
		Map<String, Object> pars = new HashMap<String, Object>();
		StringBuffer trMax = new StringBuffer(
				"SELECT MAX(tmax.transactionId) AS 'transactionId'"
						+ " FROM transactionn tmax, dossier dmax, courrierdossier cdmax, courrier cmax, transactionn tm"
						+ " WHERE tmax.dossierId = dmax.dossierId"
						+ " AND dmax.dossierId = cdmax.dossierId"
						+ " AND cdmax.idCourrier = cmax.idCourrier"
						+ " AND tmax.transactionFirst = tm.transactionId");

		// if (vb.getSelectedListCourrier().equals("CRmois")){
		// jourOrAutre= 4;
		//
		// }
		// if (vb.getSelectedListCourrier().equals("CRannee")){
		// jourOrAutre=3;
		//
		// }
	
		
		
		if (flagueCloture == 4) {

			trMax.append(" AND cmax.courrierEtatCloture =1");
		}
		// Si type=boc_1 cad BO Centrale
		if (type != null) {
			// AH : Si c'est BO ne pas lui afficher les courrier élémentaire = le mode de transmission enveloppe : id=10
			
			trMax.append(" AND cmax.idTransmission!=10 ");
			System.out.println("AH : C'est un BOCT");
			//AH: ce code ajouté pour la liste des valise pour chaque BO
			//a l'accès à les valise ou il est l'expéditeur ou destinataitre
			if(jourOrAutre==9){
				int idBoc=0;
				int pos=type.indexOf("_");
				System.out.println(type.substring(pos+1));
				try{
					idBoc=Integer.parseInt(type.substring(pos+1));
					System.out.println("idBoc  "+idBoc);
				}catch(Exception e){
					idBoc=0;
				}
				
				trMax.append(" AND ( tmax.transactionId IN ( SELECT tr.idTransaction FROM transactiondest tr "
						+ "WHERE tr.transactionDestTypeIntervenant='"
						+ type
						+ "' ) OR tmax.transactionDestinationReelleId IN (SELECT tdr.transactionDestinationReelleId " +
								" FROM transactiondestinationreelle tdr " +
								" WHERE tdr.transactionDestinationReelleIdDestinataire="+idBoc+"))");
				
				}
		}// Si non boc_x BO Secondaire
		else {
			// if (type != null){
			int i = 0;
			String whereMax = "";
			trMax.append(" AND (");
			System.out.println("JS ==> list Id BOc Membres :"
					+ listIdBocMembers.size());

			for (Integer id : listIdBocMembers) {
				System.out.println("ID ;" + id);

				whereMax += "tmax.idUtilisateur = :id" + i + " OR ";
				pars.put("id" + i, id);
				i++;
			}
			trMax.append(whereMax);
			System.out.println("####");
			trMax.append(" tmax.transactionId IN ( SELECT tr.idTransaction FROM transactiondest tr "
					+ "WHERE tr.transactionDestTypeIntervenant='"
					+ type
					+ "' ))");
		}
		System.out.println("jourOrAutre :" + jourOrAutre);
		if (jourOrAutre == 1) {
			trMax.append(" AND tmax.transactionDateTransaction BETWEEN :dateDebut AND :dateFin");
			pars.put("dateDebut", dateDebut);
			pars.put("dateFin", dateFin);
		}
		if (jourOrAutre == 2) {
			trMax.append(" AND tmax.transactionDateTransaction < :dateDebut");
			pars.put("dateDebut", dateDebut);

		}
		// ---------------------------------------MM---------------------------------------------------------------------------------
		// ---------------------------------------Pour
		// optimisation------------------------------------------------------------------
		// **************************************************************
		Calendar calendar = Calendar.getInstance();
		Date dateAujourd = calendar.getTime();
		System.out.println("moisEnCours : " + dateAujourd);
		// Liste des couriers pour cette année
		if (jourOrAutre == 3) {
			trMax.append(" AND cmax.courrierDateReceptionAnnee =:dateDebut");
			// System.out.println("yearrrrrrrr : " + dateDebut.getYear());
			// System.out.println("yearrrrrrrr : " + dateDebut.getYear() +
			// 1900);

			pars.put("dateDebut", dateDebut.getYear() + 1900);
		}
		// Liste des couriers pour ce mois
		if (jourOrAutre == 4) {
			trMax.append(" AND cmax.courrierDateReceptionAnnee =:anneeEnCours AND cmax.courrierDateReceptionMois =:moisEnCours ");
			pars.put("anneeEnCours", dateAujourd.getYear() + 1900);
			pars.put("moisEnCours", dateAujourd.getMonth() + 1);
		}
		// 2019-06-24
		// Liste des couriers pour ce jour
		if (jourOrAutre == 5) {
			trMax.append(" AND DATE_FORMAT(cmax.courrierDateReception,'%Y-%m-%d') =:dateDebut ");

			// JS :Formatter date Début **********************************
			System.out.println("Date Début Avant formattage :" + dateDebut);
			SimpleDateFormat formatDebut = new SimpleDateFormat("yyyy-MM-dd");
			String dateD = formatDebut.format(dateDebut);
			pars.put("dateDebut", dateD);
		}
		// Liste des couriers pour cette année
		if (jourOrAutre == 6) {
			trMax.append(" AND cmax.courrierDateReceptionAnnee =:dateDebut AND cmax.idCourrier != :idCourrierCourant");
			// System.out.println("yearrrrrrrr : " + dateDebut.getYear());
			// System.out.println("yearrrrrrrr : " + dateDebut.getYear() +
			// 1900);
			pars.put("dateDebut", dateDebut.getYear() + 1900);
			pars.put("idCourrierCourant", idCourrierCourant);

		}
		if (jourOrAutre == 7) {
			trMax.append(" AND cmax.idcourrierFK is null  AND cmax.courrierFormat is null AND cmax.idTransmission=9");

		}
		if (jourOrAutre == 8) {
			System.out.println("idCourrierCourant : " + idCourrierCourant);
			trMax.append(" AND  cmax.idcourrierFK =:idCourrierCourant  AND cmax.courrierFormat is null ");
			pars.put("idCourrierCourant", idCourrierCourant);

		}
		
		//trMax.append("AND (cmax.idcourrierFK =null OR (cmax.idcourrierFK  is not null AND cmax.courrierDatePointage is not null))");
		
		
		if (jourOrAutre == 88) {
			System.out.println("idCourrierCourant : " + idCourrierCourant);
			trMax.append(" AND  cmax.idcourrierFK =:idCourrierCourant  AND cmax.courrierFormat is null AND cmax.courrierDatePointage is null ");
			pars.put("idCourrierCourant", idCourrierCourant);

		}
		if (jourOrAutre == 9) {
			trMax.append(" AND  cmax.courrierFormat = 'valise' AND cmax.courrierSupprime != 0 AND cmax.courrierSupprimeDate is null ");

		} else {
			trMax.append("  AND cmax.courrierFormat is null ");
		}
		if (jourOrAutre == 10) {
			trMax.append(" AND  cmax.idcourrierFK =:idCourrierCourant AND  cmax.courrierDatePointage is not null ");
			pars.put("idCourrierCourant", idCourrierCourant);
		}

		
///////////////////////////////////////////////2020-06-16////////////////////////////////
		if (jourOrAutre == 100) {
			trMax.append(" AND tmax.transactionId IN (SELECT tdes.idTransaction FROM transactiondest tdes WHERE tdes.transactionDestTypeIntervenant = :type) ");
			trMax.append(" AND  cmax.idcourrierFK =:idCourrierCourant AND  cmax.courrierDatePointage is not null ");
			pars.put("idCourrierCourant", idCourrierCourant);
			pars.put("type", type);
			
		}
///////////////////////////////////////////////2020-06-16////////////////////////////////		
		
		if (flagInterne == 11) {
			System.out.println("############ Dans Condition : flagInterne == "
					+ flagInterne);
			trMax.append(" AND  cmax.courrierType = 'I' OR cmax.courrierType = 'I*'");

		}

		// trMax.append(" AND cmax.courrierDateReception ='2016-07-18'");
		// ---------------------------------------Fin
		// optimisation-------------------------------------------------------------------

		// Arrivé Départ
		System.out.println("categorieCourrierJour :" + categorieCourrierJour);
		if (categorieCourrierJour.equals("A")) {
			trMax.append(" AND cmax.courrierReferenceCorrespondant LIKE 'A%'");
			// AAAA
		}
		if (categorieCourrierJour.equals("D")) {
			trMax.append(" AND cmax.courrierReferenceCorrespondant LIKE 'D%'");
		}
		if (jourOrAutre != 9 && jourOrAutre != 8 && jourOrAutre != 88
				&& jourOrAutre != 10) {
			if (!(type != null && type.equals("boc_100"))) {
				
				if (!categorieCourrierJour.equals("A")
						&& !categorieCourrierJour.equals("D")) {
					int i = 0;
					System.out.println("I m here");
					String whereMax = "";
					trMax.append(" AND (");

					for (Integer id : listIdBocMembers) {
						System.out.println("ID : " + id);
						whereMax += "tmax.idUtilisateur = :id" + i + " OR ";
						pars.put("id" + i, id);
						i++;
						System.out.println("############ id == " + id);
					}
					System.out.println("############ whereMax == " + whereMax);
					System.out.println("############2 type == " + type);
					trMax.append(whereMax);
					// ça était tm.transactionId
					StringBuffer trDest = new StringBuffer(
							"tmax.transactionId IN (SELECT tdes.idTransaction"
									+ " FROM transactiondest tdes"
									+ " WHERE tdes.transactionDestTypeIntervenant = :type))");
					// System.out.println("typeeee : " + type);
					pars.put("type", type);
					trMax.append(trDest);
				}
			}
		}
		// Fin Arrivé Départ

		// Transmission
		System.out.println("************* Dans la requete ********** : "
				+ typeTransmission);
		// if (typeTransmission.equals("e-Mail")) {
		// trMax.append(" AND cmax.idTransmission = 4");
		// }
		// if (typeTransmission.equals("Fax")) {
		// trMax.append(" AND cmax.idTransmission = 3");
		// }

		if (typeTransmission.equals("Tout les courriers")
				|| typeTransmission.equals("Tous les courriers")) {
			;
			// trMax.append(" AND cmax.idTransmission");
		} else {
			Integer id = Integer.parseInt(typeTransmission);
			trMax.append(" AND cmax.idTransmission = :id");
			pars.put("id", id);
		}

		// Fin Restrictions

		// etat => A valider
		System.out.println("state traitement :" + stateTraitement);
		if (stateTraitement.equals("traite")) {
			trMax.append(" AND (tmax.etatId IN (4, 6) OR (tmax.etatId = 10 AND tmax.transactionOrdre > 1))");
		}
		if (stateTraitement.equals("nonTraite")) {
			trMax.append(" AND (tmax.etatId IN (2, 5) OR (tmax.etatId = 10 AND tmax.transactionOrdre > 1))");
		}

		trMax.append(" GROUP BY dmax.dossierId"
				+ " ORDER BY dmax.dossierId DESC");
		if (maxResult != 0 && DBType.contains("mysql")) {
			trMax.append(" LIMIT " + firstIndex + ", " + maxResult);
		}

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		System.out.println("queryMax :" + queryMax);
		Iterator<String> iterMax = pars.keySet().iterator();
		System.out.println("iterMax ==>" + iterMax);
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		if (maxResult != 0 && DBType.contains("sqlserver")) {
			queryMax.setFirstResult(firstIndex);
			queryMax.setMaxResults(maxResult);
		}

		System.out.println("queryMax.getQueryString() : "
				+ queryMax.getQueryString());
		System.out.println("queryMax.list  :" + queryMax.list());
		List<Integer> maxIds = queryMax.list();

		Map<String, Object> params = new HashMap<String, Object>();
		int imax = 0;
		String where = " t.transactionId IN (";
		if (maxIds.size() > 0) {
			for (Integer ids : maxIds) {
				Integer idTrans = ids;
				where += ":idTrans" + imax + ", ";
				params.put("idTrans" + imax, idTrans);
				imax++;
			}
		} else {
			return new ArrayList<CourrierInformations>();
		}
		System.out.println("Liste :");
		StringBuffer hql = new StringBuffer(
				"SELECT DISTINCT c.idCourrier AS 'courrierID',"
						+ " c.courrierObjet AS 'courrierObjet',"
						+ " c.courrierReferenceCorrespondant AS 'courrierReference',"
						+ " c.courrierCommentaire AS 'courrierCommentaire',"
						+ " c.courrierOldNum AS 'courrierOldNum',"
						+ " c.courrierCircuit AS 'courrierCircuit',"
						+ " d.dossierId AS 'dossierID',"
						+ " n.natureLibelle AS 'courrierNature',"
						+ " t.etatId AS 'etatID',"
						+ " c.courrierDateReception AS 'courrierDateReceptionEnvoi',"
						+ " t.transactionDateConsultation AS 'transactionDateConsultation',"
						+ " t.transactionId AS 'transactionID',"
						+ " t.idExpDest AS 'expID',"
						+ " t.transactionOrdre AS 'transactionOrdre',"
						+ " t.idUtilisateur AS 'idUtilisateur',"
						+ " t_tdr.transactionDestinationReelleIdDestinataire AS 'transactionDestinationReelID',"
						+ " t_tdr.transactionDestinationReelleTypeDestinataire AS 'transactionDestinationReelleTypeDestinataire',"
						+ " e.typeExpDest AS 'expTypeOld',"
						+ " e.idExpDestLdap AS 'expLdapOld',"
						+ " ee.Exp_Dest_ExterneNom AS 'expNomOld',"
						+ " ee.Exp_Dest_ExternePrenom AS 'expPrenomOld',"
						+ " ee.typeUtilisateurId AS 'expTypeUserOld',"
						+ " tm.transactionId AS 'minTransactionID',"
						+ " em.typeExpDest AS 'expType',"
						+ " em.idExpDestLdap AS 'expLdap',"
						+ " eem.Exp_Dest_ExterneNom AS 'expNom',"
						+ " eem.Exp_Dest_ExternePrenom AS 'expPrenom',"
						+ " eem.typeUtilisateurId AS 'expTypeUser',");
		if (DBType.contains("sqlserver")) {
			hql.append(" STUFF((SELECT '|' + CONVERT(NVARCHAR(10), t3.transactionId)"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), e3.idExpDest), '')"
					+ " + ';' + ISNULL(e3.typeExpDest, '')"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), e3.idExpDestLdap), '')"
					+ " + ';' + ISNULL(ee3.Exp_Dest_ExterneNom, '')"
					+ " + ';' + ISNULL(ee3.Exp_Dest_ExternePrenom, '')"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), ee3.typeUtilisateurId), '')"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), tdr.transactionDestinationReelleIdDestinataire), '')"
					+ " + ';' + ISNULL(tdr.transactionDestinationReelleTypeDestinataire, '')"
					+ " FROM transactionn t3 "
					+ " LEFT JOIN transactiondest td3 ON t3.transactionId = td3.idTransaction"
					+ " LEFT JOIN transactiondestinationreelle tdr ON t3.transactionDestinationReelleId = tdr.transactionDestinationReelleId"
					+ " LEFT JOIN expdest e3 ON td3.idExpDest = e3.idExpDest"
					+ " LEFT JOIN expdestexterne ee3 ON e3.idExpDestExterne = ee3.idExpDestExterne"
					+ " WHERE t3.dossierId = t.dossierId"
					+ " FOR XML PATH('')), 1, 1, '') AS 'destReelList'");
		}
		if (DBType.contains("mysql")) {
			hql.append("(SELECT GROUP_CONCAT(CONCAT(CONVERT(t3.transactionId, CHAR(10))"
					+ " , ';' , IFNULL(CONVERT(e3.idExpDest, CHAR(10)), '')"
					+ " , ';' , IFNULL(e3.typeExpDest, '')"
					+ " , ';' , IFNULL(CONVERT(e3.idExpDestLdap, CHAR(10)), '')"
					+ " , ';' , IFNULL(ee3.Exp_Dest_ExterneNom, '')"
					+ " , ';' , IFNULL(ee3.Exp_Dest_ExternePrenom, '')"
					+ " , ';' , IFNULL(CONVERT(ee3.typeUtilisateurId, CHAR(10)), '')"
					+ " , ';' , IFNULL(CONVERT(tdr.transactionDestinationReelleIdDestinataire, CHAR(10)), '')"
					+ " , ';' , IFNULL(tdr.transactionDestinationReelleTypeDestinataire, '')) SEPARATOR '|')"
					+ " FROM transactionn t3 "
					+ " LEFT JOIN transactiondest td3 ON t3.transactionId = td3.idTransaction"
					+ " LEFT JOIN transactiondestinationreelle tdr ON t3.transactionDestinationReelleId = tdr.transactionDestinationReelleId"
					+ " LEFT JOIN expdest e3 ON td3.idExpDest = e3.idExpDest"
					+ " LEFT JOIN expdestexterne ee3 ON e3.idExpDestExterne = ee3.idExpDestExterne"
					+ " WHERE t3.dossierId = t.dossierId) AS 'destReelList'");
		}
		
		System.out.println(hql.toString());
		hql.append(" FROM transactionn t"
				+ " INNER JOIN dossier d ON t.dossierId = d.dossierId"
				+ " INNER JOIN courrierdossier cd ON d.dossierId = cd.dossierId"
				+ " INNER JOIN courrier c ON cd.idCourrier = c.idCourrier"
				+ " INNER JOIN nature n ON c.idNature = n.natureId");

		hql.append(" LEFT JOIN transactiondest td ON t.transactionId = td.idTransaction"
				+ " LEFT JOIN expdest e ON t.idExpDest = e.idExpDest"
				+ " LEFT JOIN expdestexterne ee ON e.idExpDestExterne = ee.idExpDestExterne"
				+ " LEFT JOIN transactiondestinationreelle t_tdr ON t.transactionDestinationReelleId = t_tdr.transactionDestinationReelleId"
				+ " INNER JOIN transactionn tm ON t.transactionFirst = tm.transactionId"
				+ " LEFT JOIN expdest em ON tm.idExpDest = em.idExpDest"
				+ " LEFT JOIN expdestexterne eem ON em.idExpDestExterne = eem.idExpDestExterne"
				+ " WHERE d.typeDossierId = 1 AND "
				+ where.substring(0, where.length() - 2) + ")");

		// Order by Date
		hql.append(" ORDER BY c.idCourrier DESC");

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		query.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));

		query.addScalar("courrierID", new IntegerType());
		query.addScalar("courrierObjet", new StringType());
		query.addScalar("courrierReference", new StringType());
		query.addScalar("courrierCommentaire", new StringType());
		query.addScalar("courrierOldNum", new IntegerType());
		query.addScalar("courrierCircuit", new StringType());
		query.addScalar("dossierID", new IntegerType());
		query.addScalar("courrierNature", new StringType());
		query.addScalar("etatID", new IntegerType());
		query.addScalar("courrierDateReceptionEnvoi", new DateType());
		query.addScalar("transactionDateConsultation", new DateType());
		query.addScalar("transactionID", new IntegerType());
		query.addScalar("expID", new IntegerType());
		query.addScalar("transactionOrdre", new IntegerType());
		query.addScalar("idUtilisateur", new IntegerType());
		query.addScalar("transactionDestinationReelID", new IntegerType());
		query.addScalar("transactionDestinationReelleTypeDestinataire",
				new StringType());
		query.addScalar("expLdapOld", new IntegerType());
		query.addScalar("expTypeOld", new StringType());
		query.addScalar("expNomOld", new StringType());
		query.addScalar("expPrenomOld", new StringType());
		query.addScalar("expTypeUserOld", new IntegerType());
		query.addScalar("minTransactionID", new IntegerType());
		query.addScalar("expLdap", new IntegerType());
		query.addScalar("expType", new StringType());
		query.addScalar("expNom", new StringType());
		query.addScalar("expPrenom", new StringType());
		query.addScalar("expTypeUser", new IntegerType());
		query.addScalar("destReelList", new StringType());

		Iterator<String> iter = params.keySet().iterator();
		while (iter.hasNext()) {
			System.out.println("AH  dans itr ");
			String name = iter.next();
			Object value = params.get(name);
			System.out.println(name + " = " + value);
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println(query.getQueryString());
		System.out.println("list :" + query.list());
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> courrierStatistiquesBOC(Date dateDebut, Date dateFin,
			List<String> types, String type1, List<Integer> listIdBocMembers,
			String typeTransmission, String stateTraitement,
			String categorieCourrierJour, String categorie) {

		System.out.println("Date Début :" + dateDebut);
		System.out.println("Date Fin  :" + dateFin);
		System.out.println("Courriers BOC");
		System.out.println("types#### : " + types);
		System.out.println("Type Courrier 1:" + type1);
		System.out.println("Categorie :" + categorie);
		System.out.println("Categorie Courrier Jour :" + categorieCourrierJour);
		String cat = "";
		String max = "typeExpDest";

		Map<String, Object> pars = new HashMap<String, Object>();
		if (categorie.equals("nature")) {
			cat = "c.idNature, t.typeExpDest";
		}
		if (categorie.equals("transmission")) {
			cat = "c.idTransmission, t.typeExpDest";
		}
		if (categorie.equals("urgence")) {
			cat = "c.idUrgence, t.typeExpDest";
		}
		if (categorie.equals("annotation")) {
			cat = "ta.idAnnotation, t.typeExpDest";
		}
		if (categorie.equals("organisme")) {
			cat = "t.idExpDestExterne";
			max = "idExpDestExterne";
		}
		// if (categorie.equals("tempsMoyenReponseDepartement")) {
		// cat = "t.idExpDestLdap";
		// max = "idExpDestLdap";
		// }
		StringBuffer trMax = new StringBuffer(
				"SELECT "
						+ cat
						+ ", COUNT(*)"
						+ " FROM (SELECT MAX(tmax.transactionId) AS 'id', cdmax.idCourrier, e."
						+ max
						+ " FROM transactionn tmax, dossier dmax, courrierdossier cdmax, courrier cmax, expdest e, transactionn tm");
		if (categorie.equals("organisme") && categorieCourrierJour.equals("D")) {
			trMax.append(", transactiondest td");
		}
		trMax.append(" WHERE tmax.dossierId = dmax.dossierId"
				+ " AND dmax.dossierId = cdmax.dossierId"
				+ " AND cdmax.idCourrier = cmax.idCourrier"
				+ " AND tmax.transactionFirst = tm.transactionId");

		if (categorie.equals("organisme")) {
			if (categorieCourrierJour.equals("A")) {
				trMax.append(" AND tm.idExpDest = e.idExpDest AND e.typeExpDest LIKE 'Externe'");
			}
			if (categorieCourrierJour.equals("D")) {
				trMax.append(" AND tmax.transactionId = td.idTransaction"
						+ " AND td.idExpDest = e.idExpDest AND e.typeExpDest LIKE 'Externe'");
			}
		} else {
			trMax.append(" AND tmax.idExpDest = e.idExpDest");
		}
		trMax.append(" AND tmax.transactionDateTransaction BETWEEN :dateDebut AND :dateFin");

		// JS :Formatter date Début **********************************
		System.out.println("Date Début Avant formattage :" + dateDebut);
		SimpleDateFormat formatDebut = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatFin = new SimpleDateFormat("yyyy-MM-dd");
		String dateD = formatDebut.format(dateDebut);
		System.out.println("dateD :" + dateD);
		String dateF = formatFin.format(dateFin);
		System.out.println("dateF :" + dateF);
		// **************************************
		pars.put("dateDebut", dateD);
		pars.put("dateFin", dateF);

		// Arrivé Départ
		if (categorieCourrierJour.equals("A")) {
			System.out.println("categorieCourrierJour=A");
			trMax.append(" AND tmax.courrierReferenceCorrespondant LIKE 'A%'");
		}
		if (categorieCourrierJour.equals("D")) {
			System.out.println("categorieCourrierJour=D");
			trMax.append(" AND tmax.courrierReferenceCorrespondant LIKE 'D%'");
		}

		// if (!categorieCourrierJour.equals("A")
		// && !categorieCourrierJour.equals("D")) {
		System.out.println("categorieCourrierJour not A End Not D");

		int i = 0;
		String typDest = "";
		String whereMax = "";
		trMax.append(" AND (");
		for (Integer id : listIdBocMembers) {
			whereMax += "tmax.idUtilisateur = :id" + i + " OR ";
			pars.put("id" + i, id);
			i++;
		}
		trMax.append(whereMax);

		String whereType = "";
		if (types.size() > 0) {
			for (String type : types) {
				System.out.println("type : " + type);
				whereType += " :type" + i + ", ";
				pars.put("type" + i, type);
				i++;
				System.out.println("lenght wheretype : " + whereType.length());
				System.out.println("substring : "
						+ whereType.substring(0, whereType.length() - 2));
			}

			typDest = whereType.substring(0, whereType.length() - 2);

			StringBuffer trDest = new StringBuffer(
					"tmax.transactionTypeIntervenant IN (" + typDest + ")");

			trDest.append(" OR tm.transactionId IN (SELECT tdes.idTransaction FROM transactiondest tdes WHERE tdes.transactionDestTypeIntervenant IN  ("
					+ typDest + ")))");
			trMax.append(trDest);
		}

		// StringBuffer trDest = new StringBuffer(
		// "tm.transactionId IN (SELECT tdes.idTransaction"
		// + " FROM transactiondest tdes");
		// trDest.append(" WHERE tdes.transactionDestTypeIntervenant IN  ("
		// + typDest + ")))");

		// }
		// Fin Arrivé Départ

		/*
		 * // etat => A valider if (stateTraitement.equals("traite")) {
		 * trMax.append(
		 * " AND (tmax.etatId IN (4, 6) OR (tmax.etatId = 10 AND tmax.transactionOrdre > 1))"
		 * ); } if (stateTraitement.equals("nonTraite")) { trMax.append(
		 * " AND (tmax.etatId IN (2, 5) OR (tmax.etatId = 10 AND tmax.transactionOrdre > 1))"
		 * ); }
		 */
		trMax.append(" GROUP BY cdmax.idCourrier, e." + max + ") t"
				+ " LEFT JOIN courrier c ON c.idCourrier = t.idCourrier");
		if (categorie.equals("annotation")) {
			trMax.append(" LEFT JOIN transactionannotation ta ON t.id = ta.idTransaction");
		}
		trMax.append(" GROUP BY " + cat);

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		Iterator<String> iterMax = pars.keySet().iterator();
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			// System.out.println("---------------------");
			// System.out.println("Name =" + name);
			// System.out.println("value =" + value);
			// System.out.println("---------------------");
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println(queryMax.getQueryString());
		return queryMax.list();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> courrierStatistiquesBOCStructure(Date dateDebut,
			Date dateFin, Integer categorieCourrierJour, List<String> types,
			List<Integer> listIdBocMembers) {

		Map<String, Object> pars = new HashMap<String, Object>();
		// List<String> listRecuEnv = new ArrayList<String>();
		String intervenant = "";
		System.out.println("categorieCourrierJour :" + categorieCourrierJour);

		if (categorieCourrierJour.equals(1)) {
			intervenant = "tmax.transactionTypeIntervenant";
		}
		if (categorieCourrierJour.equals(2)) {
			intervenant = "tdmax.transactionDestTypeIntervenant";
		}
		StringBuffer trMax = new StringBuffer(
				"SELECT t.intervenant, COUNT(*)"
						+ " FROM (SELECT MAX(tmax.transactionId) AS 'id', cdmax.idCourrier, "
						+ intervenant
						+ " 'intervenant'"
						+ " FROM transactionn tmax, courrierdossier cdmax, courrier cmax");
		if (categorieCourrierJour.equals(2)) {
			trMax.append(", transactiondest tdmax");
		}
		trMax.append(" WHERE tmax.dossierId = cdmax.dossierId"
				+ " AND cdmax.idCourrier = cmax.idCourrier" + " AND "
				+ intervenant + " IS NOT NULL");
		// Arrivé Départ
		if (categorieCourrierJour.equals(2)) {
			trMax.append(" AND tmax.transactionId = tdmax.idTransaction");
		}
		if (dateDebut != null && dateFin != null) {		
			trMax.append(" AND DATE_FORMAT(tmax.transactionDateTransaction,'%Y-%m-%d') BETWEEN :dateDebut AND :dateFin");
			SimpleDateFormat formatDebut = new SimpleDateFormat("yyyy-MM-dd");
			String dateD = formatDebut.format(dateDebut);
			String dateF = formatDebut.format(dateFin);
			pars.put("dateDebut", dateD);
			pars.put("dateFin", dateF);
		}
		
		int i = 0;
		String whereMax = "";
		String typDest = "";
		if (listIdBocMembers != null && listIdBocMembers.size() > 0) {
			for (Integer id : listIdBocMembers) {
				whereMax += "tmax.idUtilisateur = :id" + i + " OR ";
				pars.put("id" + i, id);
				i++;
			}
		}
		if (types != null && types.size() > 0) {
			for (String ed : types) {
				typDest += ":ed" + i + ", ";
				pars.put("ed" + i, ed);
				i++;
			}
			typDest = typDest.substring(0, typDest.length() - 2);
			System.out.println("typDest : " + typDest);

			if (categorieCourrierJour.equals(1)) {
				trMax.append(" AND (");
				whereMax += " tmax.transactionTypeIntervenant IN (" + typDest
						+ "))";
			} else if (categorieCourrierJour.equals(2)) {
				trMax.append(" AND (");
				whereMax += " tdmax.transactionDestTypeIntervenant IN ("
						+ typDest + "))";
			}

		}
		trMax.append(whereMax);
		trMax.append(" GROUP BY cdmax.idCourrier, " + intervenant + ") t"
				+ " LEFT JOIN courrier c ON c.idCourrier = t.idCourrier"
				+ " GROUP BY t.intervenant");

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		Iterator<String> iterMax = pars.keySet().iterator();
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			System.out.println("Name : " + name);
			System.out.println("Valeur : " + value);

			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println("####################" + queryMax.getQueryString());
		System.out.println("####################" + queryMax.list().size());

		return queryMax.list();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> courrierStatistiquesBOCReponse(String type,
			Date dateDebut, Date dateFin) {
		Map<String, Object> pars = new HashMap<String, Object>();
		StringBuffer trMax = new StringBuffer(
				"SELECT tr.transactionDestDateTransfert, c.courrierDateReponse, tr.idTransaction, tr.idExpDest"
						+ " FROM transactiondest tr, transactionn t, courrier c, courrierdossier cd"
						+ " WHERE t.transactionId = tr.idTransaction"
						+ " AND t.dossierId = cd.dossierId"
						+ " AND cd.idCourrier = c.idCourrier"
						+ " AND c.courrierNecessiteReponse = 'oui'"
						+ " AND t.transactionDateTransaction BETWEEN :dateDebut AND :dateFin"
						+ " AND tr.transactionDestTypeIntervenant = :type"
						+ " AND c.courrierDateReponse IS NOT NULL" +
						// " AND tr.transactionDestDateTransfert IS NOT NULL" +
						" ORDER BY tr.idTransaction DESC");

		pars.put("dateDebut", dateDebut);
		pars.put("dateFin", dateFin);
		pars.put("type", type);

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		Iterator<String> iterMax = pars.keySet().iterator();
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println(queryMax.getQueryString());
		// List l = queryMax.list();
		// System.out.println(l.get(0));
		return queryMax.list();
	}

	// *********************************************************************

	/*
	 * public Long countCourrierBOCWithCriterion( HashMap<String, Object>
	 * filterMap, int jourOrAutre, Date dateDebut, Date dateFin, String type,
	 * List<Integer> listIdBocMembers, String typeTransmission, String
	 * stateTraitement, String categorieCourrierJour) { // Transaction Criteria
	 * criteriaTransaction = getSession().createCriteria( Transaction.class);
	 * criteriaTransaction.createAlias("dossier", "dossier")
	 * .createAlias("dossier.courriers", "dCourrier")
	 * .createAlias("dCourrier.nature", "nature")
	 * .createAlias("dCourrier.transmission", "transmission")
	 * .createAlias("etat", "etat").createAlias("expdest", "expdest");
	 * criteriaTransaction.add(Restrictions.eq(
	 * "dossier.typedossier.typeDossierId", 1)); //
	 * criteriaTransaction.add(Restrictions.eq("idUtilisateur", idUser)); //
	 * Etat if (stateTraitement.equals("traite")) { //
	 * criteriaTransaction.add(Restrictions.eq("etat.etatId", 6)); Integer
	 * idEtatTraite[] = { 4, 6 };
	 * criteriaTransaction.add(Restrictions.or(Restrictions.in( "etat.etatId",
	 * idEtatTraite), Restrictions.and( Restrictions.eq("etat.etatId", 10),
	 * Restrictions.gt("transactionOrdre", 1))));
	 * 
	 * } if (stateTraitement.equals("nonTraite")) { Integer idEtatNonTraite[] =
	 * { 2, 5 }; criteriaTransaction.add(Restrictions.or(Restrictions.in(
	 * "etat.etatId", idEtatNonTraite), Restrictions.and(
	 * Restrictions.eq("etat.etatId", 10), Restrictions.eq("transactionOrdre",
	 * 1)))); } // Fin Etat // Arrivé Départ if
	 * (categorieCourrierJour.equals("A")) { // Courrier d'arrivé BOC
	 * criteriaTransaction
	 * .add(Restrictions.like("dCourrier.courrierReferenceCorrespondant",
	 * "A%"));
	 * 
	 * } if (categorieCourrierJour.equals("D")) { // Courrier de depart BOC
	 * criteriaTransaction
	 * .add(Restrictions.like("dCourrier.courrierReferenceCorrespondant",
	 * "D%")); } if (!categorieCourrierJour.equals("A") &&
	 * !categorieCourrierJour.equals("D")) { DetachedCriteria
	 * detachedCritTransactionDest = DetachedCriteria
	 * .forClass(TransactionDestination.class);
	 * detachedCritTransactionDest.setProjection(Projections
	 * .projectionList().add( Projections.property("id.idTransaction"),
	 * "idTransaction")); detachedCritTransactionDest.add(Restrictions.eq(
	 * "transactionDestTypeIntervenant", type));
	 * criteriaTransaction.add(Restrictions.or( Restrictions.in("idUtilisateur",
	 * listIdBocMembers), Property.forName("transactionId").in(
	 * detachedCritTransactionDest))); }
	 * 
	 * // Fin Arrivé Départ // Transmission if
	 * (typeTransmission.equals("e-Mail")) {
	 * criteriaTransaction.add(Restrictions.eq( "transmission.transmissionId",
	 * 4)); } if (typeTransmission.equals("Fax")) {
	 * criteriaTransaction.add(Restrictions.eq( "transmission.transmissionId",
	 * 3)); } if (typeTransmission.equals("Autre")) {
	 * criteriaTransaction.add(Restrictions.ne( "transmission.transmissionId",
	 * 4)); criteriaTransaction.add(Restrictions.ne(
	 * "transmission.transmissionId", 3)); } // Fin Transmission if (jourOrAutre
	 * == 1) { criteriaTransaction.add(Restrictions.between(
	 * "transactionDateTransaction", dateDebut, dateFin)); } if (jourOrAutre ==
	 * 2) { criteriaTransaction.add(Restrictions.lt(
	 * "transactionDateTransaction", dateDebut)); }
	 * criteriaTransaction.setResultTransformer(Transformers
	 * .aliasToBean(CourrierInformations.class)); //
	 * criteriaTransaction.setProjection(Projections.rowCount());
	 * criteriaTransaction
	 * .setProjection(Projections.countDistinct("dCourrier.idCourrier"));
	 * 
	 * CriteriaImpl criteriaImpl = (CriteriaImpl) criteriaTransaction;
	 * SessionImplementor session = criteriaImpl.getSession();
	 * SessionFactoryImplementor factory = session.getFactory();
	 * CriteriaQueryTranslator translator = new CriteriaQueryTranslator(factory,
	 * criteriaImpl, criteriaImpl.getEntityOrClassName(),
	 * CriteriaQueryTranslator.ROOT_SQL_ALIAS); String[] implementors =
	 * factory.getImplementors(criteriaImpl.getEntityOrClassName());
	 * 
	 * CriteriaJoinWalker walker = new CriteriaJoinWalker( (OuterJoinLoadable)
	 * factory.getEntityPersister(implementors[0]), translator, factory,
	 * criteriaImpl, criteriaImpl.getEntityOrClassName(),
	 * session.getLoadQueryInfluencers());
	 * 
	 * String sql=walker.getSQLString(); System.out.println(sql); return (Long)
	 * criteriaTransaction.uniqueResult(); }
	 */

	@SuppressWarnings("unchecked")
	public Long countCourrierBOCWithCriterion(
			HashMap<String, Object> filterMap, int jourOrAutre, Date dateDebut,
			Date dateFin, String type, List<Integer> listIdBocMembers,
			String typeTransmission, String stateTraitement,
			String categorieCourrierJour) {
		System.out.println("2020-05-16 : countCourrierBOCWithCriterion");

		System.out
				.println("size listIdBocMembers : " + listIdBocMembers.size());
		System.out.println("type : " + type);
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer trCount = new StringBuffer(
				"SELECT COUNT(DISTINCT d.dossierId) AS 'nbrc'"
						+ " FROM transactionn t, dossier d, courrierdossier cd, courrier c, transactionn tm"
						+ " WHERE t.dossierId = d.dossierId"
						+ " AND d.dossierId = cd.dossierId"
						+ " AND cd.idCourrier = c.idCourrier"
						+ " AND t.transactionFirst = tm.transactionId"
						+ "  AND c.courrierFormat is null " +
								" AND c.idTransmission!=10 ");
		// ---------------------------------------MM---------------------------------------
		// ---------------------------------Debut
		// optimisation-----------------------------
		Calendar calendar = Calendar.getInstance();
		Date dateAujourd = calendar.getTime();
		System.out.println("dateAujourd : " + dateAujourd);

		// JS :Formatter date Début **********************************
		// System.out.println("Date Début Avant formattage :"+dateDebut);
		// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// String dateD = format.format(dateDebut);
		System.out.println("dateDebut :" + dateDebut);

		// **************************************

		// BOCT
		// nombre d'enregistremenrt des courrier de cette année
		System.out.println("JS ==>Jour Or Autre :" + jourOrAutre);
		if (jourOrAutre == 13) {

			trCount.append(" AND c.courrierDateReceptionAnnee =:dateAjourdhuit");
			// System.out.println("psssssssss : " + dateAujourd.getYear());
			// System.out.println("psssssssss : " + dateAujourd.getYear() +
			// 1900);
			params.put("dateAjourdhuit", dateAujourd.getYear() + 1900);

		}
		// nombre d'enregistremenrt des courrier de ce mois
		if (jourOrAutre == 14) {
			System.out.println("************************");
			trCount.append(" AND c.courrierDateReceptionAnnee =:anneeEnCours AND c.courrierDateReceptionMois =:moisEnCours");
			params.put("moisEnCours", dateAujourd.getMonth() + 1);
			params.put("anneeEnCours", dateAujourd.getYear() + 1900);

		}
		// nombre d'enregistremenrt des courrier d'aujourd'hui
		if (jourOrAutre == 15) {
			trCount.append(" AND DATE_FORMAT(c.courrierDateReception,'%Y-%m-%d') BETWEEN :dateDebut AND :dateFin");
			// params.put("dateAjourdhuit", dateAujourd);
			// trCount.append(" AND c.courrierDateReception ='2016-07-18'");
			// JS :Formatter date Début **********************************
			System.out.println("Date Début Avant formattage :" + dateDebut);
			SimpleDateFormat formatDebut = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatFin = new SimpleDateFormat("yyyy-MM-dd");
			String dateD = formatDebut.format(dateDebut);
			System.out.println("dateD :" + dateD);
			String dateF = formatFin.format(dateFin);
			System.out.println("dateF :" + dateF);
			// **************************************
			params.put("dateDebut", dateD);
			System.out.println("dateFin " + dateF);
			params.put("dateFin", dateFin);

		}

		// ---------------------------------Fin
		// Optimisation-------------------------------

		if (jourOrAutre == 1) {
			//[JS] : 2020-05-12 ====================
			trCount.append(" AND DATE_FORMAT(c.courrierDateReception ,'%Y-%m-%d' )= :dateAjourdhuit");
			 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			 String dateD = format.format(dateDebut);
			params.put("dateAjourdhuit", dateD);
		}
		if (jourOrAutre == 2) {
			trCount.append(" AND t.transactionDateTransaction < :dateDebut");
			params.put("dateDebut", dateDebut);
		}
		// Arrivé Départ
		System.out.println("JS ==>categorieCourrierJour "
				+ categorieCourrierJour);
		if (categorieCourrierJour.equals("A")) {
			trCount.append(" AND t.courrierReferenceCorrespondant LIKE 'A%'");
		}

		System.out.println("JS ==>categorieCourrierJour "
				+ categorieCourrierJour);
		if (categorieCourrierJour.equals("D")) {
			trCount.append(" AND t.courrierReferenceCorrespondant LIKE 'D%'");
		}
		System.out.println("pffff :" + !(type != null && type.equals("boc_100")));
		if (!(type != null && type.equals("boc_100"))) {// cette condition donne toujours true
			
//			if (!categorieCourrierJour.equals("A")
//					&& !categorieCourrierJour.equals("D")) {

				int i = 0;
				String whereMax = "";
				trCount.append(" AND (");

				for (Integer id : listIdBocMembers) {
					whereMax += "t.idUtilisateur = :id" + i + " OR ";
					params.put("id" + i, id);
					i++;
				}
				trCount.append(whereMax);
				StringBuffer trDest = new StringBuffer(
						"tm.transactionId IN (SELECT tdes.idTransaction"
								+ " FROM transactiondest tdes"
								+ " WHERE tdes.transactionDestTypeIntervenant = :type))");
				params.put("type", type);
				trCount.append(trDest);
//			}
		}
		// Fin Arrivé Départ

		// Transmission
		System.out.println("typeTransmission :" + typeTransmission);
		if (typeTransmission.equals("e-Mail")) {
			trCount.append(" AND c.idTransmission = 4");
		}
		if (typeTransmission.equals("Fax")) {
			trCount.append(" AND c.idTransmission = 3");
		}
		if (typeTransmission.equals("Autre")) {
			trCount.append(" AND c.idTransmission NOT IN (3, 4)");
		}

		// if (typeTransmission.equals("Autre") ||
		// typeTransmission.equals("Tout les courriers")
		// || typeTransmission.equals("Tous les courriers")) {
		// trCount.append(" AND c.idTransmission NOT IN (3, 4)");
		// } else {
		// Integer id = Integer.parseInt(typeTransmission);
		// trCount.append(" AND c.idTransmission = :id");
		// params.put("id", id);
		// }
		// Fin Restrictions

		// etat => A valider
		System.out.println("stateTraitement :" + stateTraitement);
		if (stateTraitement.equals("traite")) {
			trCount.append(" AND (t.etatId IN (4, 6) OR (t.etatId = 10 AND t.transactionOrdre > 1))");
		}
		if (stateTraitement.equals("nonTraite")) {
			trCount.append(" AND (t.etatId IN (2, 5) OR (t.etatId = 10 AND t.transactionOrdre > 1))");
		}

		SQLQuery query = getSession().createSQLQuery(trCount.toString());

		query.addScalar("nbrc", new LongType());
		System.out.println("Query :" + query.getQueryString());
		System.out.println("params   :" + params);
		Iterator<String> iterMax = params.keySet().iterator();
		System.out.println("Iter MAx :" + iterMax);
		while (iterMax.hasNext()) {

			String name = iterMax.next();

			// System.out.println("Name :" + name);

			Object value = params.get(name);
			// System.out.println("value  :" + value);

			// System.out.println("==>" + params.get(name).getClass());
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
				// System.out.println("1 :" + query.getQueryString());
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
				// System.out.println("2 :" + query.getQueryString());
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
				// System.out.println("3 :" + query.getQueryString());
			} else {
				query.setParameterList(name, (List<String>) value);
				// System.out.println("4 :" + query.getQueryString());
			}
		}

		System.out.println("RQ : "+query.getQueryString());
		System.out.println("NB : " + query.uniqueResult());
		return (Long) query.uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	public Long countCourrierBOCWithCriterionStat(
			HashMap<String, Object> filterMap, int jourOrAutre, Date dateDebut,
			Date dateFin, String type, List<Integer> listIdBocMembers,
			String typeTransmission, String stateTraitement,
			String categorieCourrierJour) {

		System.out.println("type : " + type);
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer trCount = new StringBuffer(
				"SELECT COUNT(DISTINCT d.dossierId) AS 'nbrc'"
						+ " FROM transactionn t, dossier d, courrierdossier cd, courrier c, transactionn tm"
						+ " WHERE t.dossierId = d.dossierId"
						+ " AND d.dossierId = cd.dossierId"
						+ " AND cd.idCourrier = c.idCourrier"
						+ " AND t.transactionFirst = tm.transactionId"
						+ "  AND c.courrierFormat is null " +
								" AND c.idTransmission!=10 ");
		
		if (jourOrAutre == 1) {
			trCount.append(" AND DATE_FORMAT(c.courrierDateReception ,'%Y-%m-%d' )= :dateAjourdhuit");
			 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			 String dateD = format.format(dateDebut);
			params.put("dateAjourdhuit", dateD);
		}
		if (jourOrAutre == 2) {
			trCount.append(" AND t.transactionDateTransaction < :dateDebut");
			params.put("dateDebut", dateDebut);
		}
		// Arrivé Départ
		System.out.println("JS ==>categorieCourrierJour "
				+ categorieCourrierJour);
		if (categorieCourrierJour.equals("A")) {
			trCount.append(" AND t.courrierReferenceCorrespondant LIKE 'A%'");
		}

		if (categorieCourrierJour.equals("D")) {
			trCount.append(" AND t.courrierReferenceCorrespondant LIKE 'D%'");
		}
		if (type != null ) {
		
			
				int i = 0;
				String whereMax = "";
				trCount.append(" AND (");

				for (Integer id : listIdBocMembers) {
					whereMax += "t.idUtilisateur = :id" + i + " OR ";
					params.put("id" + i, id);
					i++;
				}
				trCount.append(whereMax);
				StringBuffer trDest = new StringBuffer(
						"tm.transactionId IN (SELECT tdes.idTransaction"
								+ " FROM transactiondest tdes"
								+ " WHERE tdes.transactionDestTypeIntervenant = :type))");
				params.put("type", type);
				trCount.append(trDest);
			
		}
		

		SQLQuery query = getSession().createSQLQuery(trCount.toString());

		query.addScalar("nbrc", new LongType());
		System.out.println("Query :" + query.getQueryString());
		System.out.println("params   :" + params);
		Iterator<String> iterMax = params.keySet().iterator();
		System.out.println("Iter MAx :" + iterMax);
		while (iterMax.hasNext()) {

			String name = iterMax.next();

			Object value = params.get(name);

			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println("RQ : "+query.getQueryString());
		System.out.println("NB : " + query.uniqueResult());
		return (Long) query.uniqueResult();
	}


	@Override
	public Long countCourrierBOCByUrgenceWithCriterion(String type,
			Integer idUser, Integer idUrgence, String categorieCourrierJour) {

		// Transaction
		Criteria criteriaTransaction = getSession().createCriteria(
				Transaction.class);
		criteriaTransaction.createAlias("dossier", "dossier")
				.createAlias("dossier.courriers", "dCourrier")
				.createAlias("dCourrier.nature", "nature")
				.createAlias("dCourrier.transmission", "transmission")
				.createAlias("dCourrier.urgence", "urgence")
				.createAlias("etat", "etat").createAlias("expdest", "expdest");

		// criteriaTransaction.add(Restrictions.eq("idUtilisateur", idUser));

		criteriaTransaction
				.add(Restrictions.eq("urgence.urgenceId", idUrgence));
		criteriaTransaction.add(Restrictions.eq(
				"dossier.typedossier.typeDossierId", 1));

		// Arrivé Départ
		if (categorieCourrierJour.equals("A")) { // Courrier d'arrivé BOC
			// to give they courrier that musts response
			DetachedCriteria detachedCritTransactionDest = DetachedCriteria
					.forClass(TransactionDestination.class);
			detachedCritTransactionDest.setProjection(Projections
					.projectionList().add(
							Projections.property("id.idTransaction"),
							"idTransaction"));
			detachedCritTransactionDest.add(Restrictions.eq(
					"transactionDestTypeIntervenant", type));
			criteriaTransaction.add(Restrictions.or(Restrictions.and(
					Restrictions.eq("idUtilisateur", idUser),
					Restrictions.isNotNull("expdest.expdestexterne")), Property
					.forName("transactionId").in(detachedCritTransactionDest)));
		}
		if (categorieCourrierJour.equals("D")) { // Courrier de depart BOC
			criteriaTransaction.add(Restrictions
					.isNull("expdest.expdestexterne"));
			criteriaTransaction.add(Restrictions.eq("idUtilisateur", idUser));
		}
		if (!categorieCourrierJour.equals("A")
				&& !categorieCourrierJour.equals("D")) {
			// to give they courrier that musts response
			DetachedCriteria detachedCritTransactionDest = DetachedCriteria
					.forClass(TransactionDestination.class);
			detachedCritTransactionDest.setProjection(Projections
					.projectionList().add(
							Projections.property("id.idTransaction"),
							"idTransaction"));
			detachedCritTransactionDest.add(Restrictions.eq(
					"transactionDestTypeIntervenant", type));
			criteriaTransaction.add(Restrictions.or(
					Restrictions.eq("idUtilisateur", idUser),
					Property.forName("transactionId").in(
							detachedCritTransactionDest)));
		}
		// Fin Arrivé Départ

		criteriaTransaction.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));
		criteriaTransaction.setProjection(Projections.rowCount());
		return (Long) criteriaTransaction.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> countCourrierBOCByConfOrUrgWithCriterion(String type,
			Integer idUser, String categorieCourrierJour, String tab) {
		// Transaction
		Criteria criteriaTransaction = getSession().createCriteria(
				Transaction.class);
		criteriaTransaction.createAlias("dossier", "dossier")
				.createAlias("dossier.courriers", "dCourrier")
				.createAlias("expdest", "expdest");

		// Confidentialite OR Urgence
		if (tab == "c") {
			criteriaTransaction.createAlias("dCourrier.confidentialite",
					"confidentialite");
		} else {
			criteriaTransaction.createAlias("dCourrier.urgence", "urgence");
		}
		criteriaTransaction.add(Restrictions.eq(
				"dossier.typedossier.typeDossierId", 1));

		// Arrivé Départ
		if (categorieCourrierJour.equals("A")) { // Courrier d'arrivé BOC
			// to give they courrier that musts response
			DetachedCriteria detachedCritTransactionDest = DetachedCriteria
					.forClass(TransactionDestination.class);
			detachedCritTransactionDest.setProjection(Projections
					.projectionList().add(
							Projections.property("id.idTransaction"),
							"idTransaction"));
			detachedCritTransactionDest.add(Restrictions.eq(
					"transactionDestTypeIntervenant", type));
			criteriaTransaction.add(Restrictions.or(Restrictions.and(
					Restrictions.eq("idUtilisateur", idUser),
					Restrictions.isNotNull("expdest.expdestexterne")), Property
					.forName("transactionId").in(detachedCritTransactionDest)));
		}
		if (categorieCourrierJour.equals("D")) { // Courrier de depart BOC
			criteriaTransaction.add(Restrictions
					.isNull("expdest.expdestexterne"));
			criteriaTransaction.add(Restrictions.eq("idUtilisateur", idUser));
		}
		if (!categorieCourrierJour.equals("A")
				&& !categorieCourrierJour.equals("D")) {
			// to give they courrier that musts response
			DetachedCriteria detachedCritTransactionDest = DetachedCriteria
					.forClass(TransactionDestination.class);
			detachedCritTransactionDest.setProjection(Projections
					.projectionList().add(
							Projections.property("id.idTransaction"),
							"idTransaction"));
			detachedCritTransactionDest.add(Restrictions.eq(
					"transactionDestTypeIntervenant", type));
			criteriaTransaction.add(Restrictions.or(
					Restrictions.eq("idUtilisateur", idUser),
					Property.forName("transactionId").in(
							detachedCritTransactionDest)));
		}
		// Fin Arrivé Départ
		// Confidentialite OR Urgence
		if (tab == "c") {
			criteriaTransaction
					.setProjection(Projections
							.projectionList()
							.add(Projections.countDistinct("transactionId"))
							.add(Projections
									.groupProperty("confidentialite.confidentialiteId")));
			// criteriaTransaction.addOrder(Order.desc("confidentialite.confidentialiteId"));
		} else {
			criteriaTransaction.setProjection(Projections.projectionList()
					.add(Projections.countDistinct("transactionId"))
					.add(Projections.groupProperty("urgence.urgenceId")));
			// criteriaTransaction.addOrder(Order.desc("urgence.urgenceId"));
		}
		criteriaTransaction
				.setResultTransformer(CriteriaSpecification.PROJECTION);

		CriteriaImpl criteriaImpl = (CriteriaImpl) criteriaTransaction;
		SessionImplementor session = criteriaImpl.getSession();
		SessionFactoryImplementor factory = session.getFactory();
		CriteriaQueryTranslator translator = new CriteriaQueryTranslator(
				factory, criteriaImpl, criteriaImpl.getEntityOrClassName(),
				CriteriaQueryTranslator.ROOT_SQL_ALIAS);
		String[] implementors = factory.getImplementors(criteriaImpl
				.getEntityOrClassName());

		CriteriaJoinWalker walker = new CriteriaJoinWalker(
				(OuterJoinLoadable) factory.getEntityPersister(implementors[0]),
				translator, factory, criteriaImpl, criteriaImpl
						.getEntityOrClassName(), session
						.getLoadQueryInfluencers());

		String sql = walker.getSQLString();
		System.out.println("countCourrierBOCByConfOrUrgWithCriterion "+sql);
		return criteriaTransaction.list();
	}

	public List<TransactionDestination> findCourrierRecuBOCWithCriterion(
			int jourOrAutre, Date dateDebut, Date dateFin, String type,
			String type1, Integer idUser, Integer typeTransmission,
			Integer stateTraitement, int firstIndex, int maxResult,
			Integer etatId) {
		// // Transaction DetachedCriteria
		// DetachedCriteria detachedCriteriaTransaction =
		// DetachedCriteria.forClass(Transaction.class);
		// detachedCriteriaTransaction.createAlias("etat", "etat");
		// detachedCriteriaTransaction.setProjection(Projections.projectionList().add(Projections.property("transactionId"),
		// "idTransaction"));
		// if(jourOrAutre == 1){
		// detachedCriteriaTransaction.add(Restrictions.between("transactionDateTransaction",
		// dateDebut, dateFin));
		// }
		// if(jourOrAutre == 2){
		// detachedCriteriaTransaction.add(Restrictions.lt("transactionDateTransaction",
		// dateDebut));
		// }
		// detachedCriteriaTransaction.add(Restrictions.eq("etat.etatId",
		// etatId));
		//
		//
		//
		// // Transaction destination Criteria
		// Criteria criteriaTransactionDestination =
		// getSession().createCriteria(TransactionDestination.class , "td");
		//
		// criteriaTransactionDestination.add(Property.forName("id.idTransaction").in(detachedCriteriaTransaction));
		// criteriaTransactionDestination.add(Restrictions.eq("transactionDestTypeIntervenant",
		// type));
		//
		//
		//
		// criteriaTransactionDestination.addOrder(Order.desc("id.idTransaction"));
		// criteriaTransactionDestination.setFirstResult(firstIndex);
		// criteriaTransactionDestination.setMaxResults(maxResult);

		// return criteriaTransactionDestination.list();
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Nature> findNatureByCriterion(Integer firstRow,
			Integer maxResult, HashMap<String, Object> filterMap,
			String sortField, boolean descending) {
		Criteria criteria = getSession().createCriteria(Nature.class);

		// adds for filter and sort
		if (sortField != null) {
			if (descending == true) {
				criteria.addOrder(Order.desc(sortField));
			} else {
				criteria.addOrder(Order.asc(sortField));
			}

		}
		for (java.util.Map.Entry<String, Object> filter : filterMap.entrySet()) {
			System.out.println("KEY : " + filter.getKey());
			System.out.println("Value : " + filter.getValue());
			// Criterion crit;
			if (filter.getKey().equals("natureId")) {
				criteria.add(Restrictions.eq(filter.getKey(),
						Integer.valueOf((String) filter.getValue())));
			} else {
				criteria.add(Restrictions.eq(filter.getKey(), filter.getValue()));
			}

		}

		criteria.setFirstResult(firstRow);
		criteria.setMaxResults(maxResult);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CourrierInformations> findDossierEnvoyerWithCriterion(
			boolean isResponsable, List<Integer> listIdsSousUnites,
			List<Integer> listIdSubordonnes, HashMap<String, Object> filterMap,
			String sortField, boolean descending, String secretaire,
			String sub, String unit, int jourOrAutre, Date dateDebut,
			Date dateFin, String type, String type1, String typeSecretaire,
			Integer idUser, String typeDossier, int firstIndex, int maxResult,
			Integer dossierRubriqueJourId) {
		Criteria criteria = getSession().createCriteria(Transaction.class);
		criteria.createAlias("dossier", "dossier")
				.createAlias("dossier.courriers", "dCourrier")
				.createAlias("dCourrier.nature", "nature")
				.createAlias("etat", "etat");
		// Projections
		criteria.setProjection(Projections
				.projectionList()
				.add(Projections.distinct(Projections
						.property("dossier.dossierId")))
				.add(Projections.property("dossier.dossierReference"),
						"courrierReference")
				.add(Projections.property("dossier.dossierIntitule"),
						"courrierObjet")
				.add(Projections.property("dossier.dossierDescription"),
						"courrierCommentaire")
				.add(Projections.property("transactionDateTransaction"),
						"courrierDateReceptionEnvoi")
				.add(Projections.property("etat.etatId"), "etatID")
				.add(Projections
						.property("transactionDestinationReelle.transactionDestinationReelleId"),
						"transactionDestinationReelID")
				.add(Projections.property("transactionDateConsultation"),
						"transactionDateConsultation")
				.add(Projections.property("transactionId"), "transactionID")
				.add(Projections.property("expdest"), "expDest"));

		//
		//
		// System.out.println("dans la requet findDossierEnvoyerWithCriterion");
		// Criteria criteria = getSession().createCriteria(Transaction.class);
		// criteria.createAlias("dossier", "dossier")
		// .createAlias("dossier.courriers", "dCourrier")
		// .createAlias("dCourrier.nature", "nature")
		// .createAlias("etat", "etat");

		// Projections
		// dossier.dossierId
		// criteria.setProjection(Projections
		// .projectionList()
		// .add(Projections.distinct(Projections
		// .property("dossier.dossierId")),
		// "dossierId")
		// .add(Projections.property("dossier.dossierReference"),
		// "courrierReference")
		// // .add(Projections.property("dossier.dossierIntitule"),
		// // "courrierObjet")
		// .add(Projections.property("dossier.dossierDescription"),
		// "courrierCommentaire")
		// .add(Projections.property("transactionDateTransaction"),
		// "courrierDateReceptionEnvoi")
		// .add(Projections.property("etat.etatId"), "etatID")
		// .add(Projections
		// .property("transactionDestinationReelle.transactionDestinationReelleId"),
		// "transactionDestinationReelID")
		// .add(Projections.property("transactionDateConsultation"),
		// "transactionDateConsultation")
		// .add(Projections.property("transactionId"), "transactionID")
		// .add(Projections.property("expdest"), "expDest"));
		// Restrictions
		criteria.add(Restrictions.or(Restrictions.eq(
				"transactionIdIntervenant", idUser), Restrictions.or(
				Restrictions.eq("transactionTypeIntervenant", type),
				Restrictions.eq("transactionTypeIntervenant", type1))));
		// Mes Courriers et les courriers de mon unité

		Criterion crit = null;
		if (isResponsable) {
			switch (dossierRubriqueJourId) {
			case 1:
				crit = Restrictions.eq("transactionTypeIntervenant", type1);
				break;
			case 2:
				crit = Restrictions.eq("transactionTypeIntervenant", type);
				break;
			case 3:
				for (Integer idSub : listIdSubordonnes) {
					if (crit == null) {
						crit = Restrictions.eq("transactionTypeIntervenant",
								"sub_" + idSub);
					} else {
						crit = Restrictions.or(crit, Restrictions.eq(
								"transactionTypeIntervenant", "sub_" + idSub));
					}
				}
				break;
			case 4:
				crit = Restrictions.eq("transactionTypeIntervenant",
						typeSecretaire);
				break;
			case 5:
				for (Integer idSousUnit : listIdsSousUnites) {
					if (crit == null) {
						crit = Restrictions.eq("transactionTypeIntervenant",
								"unit_" + idSousUnit);
					} else {
						crit = Restrictions.or(crit, Restrictions.eq(
								"transactionTypeIntervenant", "unit_"
										+ idSousUnit));
					}
				}
				break;
			}
		} else {
			// Agent ou Secretaire
			crit = Restrictions.or(
					Restrictions.eq("transactionTypeIntervenant", type),
					Restrictions.eq("transactionTypeIntervenant", type1));
		}

		// Criterion crit = Restrictions.or(
		// Restrictions.eq("transactionTypeIntervenant", type),
		// Restrictions.eq("transactionTypeIntervenant", type1));
		// if (isResponsable) {
		// // Courriers de mes subordonnés
		// if (sub.equals("Oui")) {
		// for (Integer idSub : listIdSubordonnes) {
		// crit = Restrictions.or(crit, Restrictions.eq(
		// "transactionTypeIntervenant", "sub_" + idSub));
		// }
		// }
		// // Courriers de mes sous unités
		// if (unit.equals("Oui")) {
		// for (Integer idSousUnit : listIdsSousUnites) {
		// crit = Restrictions
		// .or(crit, Restrictions.eq(
		// "transactionTypeIntervenant", "unit_"
		// + idSousUnit));
		// }
		// }
		// if (secretaire.equals("Oui")) {
		// crit = Restrictions.or(crit, Restrictions.eq(
		// "transactionTypeIntervenant", typeSecretaire));
		// }
		// }
		criteria.add(crit);
		if (jourOrAutre == 1) {
			criteria.add(Restrictions.between("transactionDateTransaction",
					dateDebut, dateFin));
		}
		if (jourOrAutre == 2) {
			criteria.add(Restrictions.lt("transactionDateTransaction",
					dateDebut));
		}
		// selectionné que les dossier de type personnalisé et non pas de type
		// default
		criteria.add(Restrictions.eq("dossier.typedossier.typeDossierId", 2));

		// first and maxResult
		// criteria.setFirstResult(firstIndex);
		// criteria.setMaxResults(maxResult);
		criteria.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));
		System.out.println(criteria.toString());
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CourrierInformations> findDossierRecuWithCriterion(
			boolean isResponsable, List<Integer> listIdsSousUnites,
			List<Integer> listIdSubordonnes, HashMap<String, Object> filterMap,
			String secretaire, String sub, String unit, String sortField,
			boolean descending, int jourOrAutre, Date dateDebut, Date dateFin,
			String type, String type1, String typeSecretaire, Integer idUser,
			String typeDossier, int firstIndex, int maxResult,
			Integer dossierRubriqueJourId) {
		System.out.println("dans la requete findDossierRecuWithCriterion");
		DetachedCriteria detachedcriteriaDestination = DetachedCriteria
				.forClass(TransactionDestination.class);
		detachedcriteriaDestination
				.setProjection(Projections.projectionList().add(
						Projections.property("id.idTransaction"),
						"idTransaction"));
		// detachedcriteriaDestination.add(Restrictions.or(Restrictions.eq(
		// "transactionDestIdIntervenant", idUser), Restrictions.or(
		// Restrictions.eq("transactionDestTypeIntervenant", type),
		// Restrictions.eq("transactionDestTypeIntervenant", type1))));
		// Mes Courriers et les courriers de mon unité

		Criterion crit = null;
		if (isResponsable) {
			switch (dossierRubriqueJourId) {
			case 1:
				crit = Restrictions.eq("transactionDestTypeIntervenant", type1);
				break;
			case 2:
				crit = Restrictions.eq("transactionDestTypeIntervenant", type);
				break;
			case 3:
				for (Integer idSub : listIdSubordonnes) {
					if (crit == null) {
						crit = Restrictions.eq(
								"transactionDestTypeIntervenant", "sub_"
										+ idSub);
					} else {
						crit = Restrictions.or(crit, Restrictions.eq(
								"transactionDestTypeIntervenant", "sub_"
										+ idSub));
					}

				}
				break;
			case 4:
				crit = Restrictions.eq("transactionDestTypeIntervenant",
						typeSecretaire);
				break;
			case 5:
				for (Integer idSousUnit : listIdsSousUnites) {
					if (crit == null) {
						crit = Restrictions.eq(
								"transactionDestTypeIntervenant", "unit_"
										+ idSousUnit);
					} else {
						crit = Restrictions.or(crit, Restrictions.eq(
								"transactionDestTypeIntervenant", "unit_"
										+ idSousUnit));
					}
				}
				break;

			}
		} else {
			crit = Restrictions.or(
					Restrictions.eq("transactionDestTypeIntervenant", type),
					Restrictions.eq("transactionDestTypeIntervenant", type1));
		}
		// Criterion crit = Restrictions.or(
		// Restrictions.eq("transactionDestTypeIntervenant", type),
		// Restrictions.eq("transactionDestTypeIntervenant", type1));
		// if (isResponsable) {
		// // Courriers de mes subordonnés
		// if (sub.equals("Oui")) {
		// for (Integer idSub : listIdSubordonnes) {
		// crit = Restrictions.or(crit, Restrictions.eq(
		// "transactionDestTypeIntervenant", "sub_" + idSub));
		// }
		// }
		// // Courriers de mes sous unités
		// if (unit.equals("Oui")) {
		// for (Integer idSousUnit : listIdsSousUnites) {
		// crit = Restrictions.or(crit, Restrictions.eq(
		// "transactionDestTypeIntervenant", "unit_"
		// + idSousUnit));
		// }
		// }
		// if (secretaire.equals("Oui")) {
		// crit = Restrictions.or(crit, Restrictions.eq(
		// "transactionDestTypeIntervenant", typeSecretaire));
		// }
		// }
		detachedcriteriaDestination.add(crit);
		Criteria criteriaTransaction = getSession().createCriteria(
				Transaction.class);
		criteriaTransaction.createAlias("dossier", "dossier")
				.createAlias("dossier.courriers", "dCourrier")
				.createAlias("dCourrier.nature", "nature")
				.createAlias("etat", "etat");

		// Projections
		criteriaTransaction
				.setProjection(Projections
						.projectionList()
						.add(Projections.distinct(Projections
								.property("dossier.dossierId")))
						.add(Projections.property("dossier.dossierReference"),
								"courrierReference")
						.add(Projections.property("dossier.dossierIntitule"),
								"courrierObjet")
						.add(Projections.property("dossier.dossierDescription"),
								"courrierCommentaire")
						.add(Projections.property("transactionDateTransaction"),
								"courrierDateReceptionEnvoi")
						.add(Projections.property("etat.etatId"), "etatID")
						.add(Projections
								.property("transactionDestinationReelle.transactionDestinationReelleId"),
								"transactionDestinationReelID")
						.add(Projections
								.property("transactionDateConsultation"),
								"transactionDateConsultation")
						.add(Projections.property("transactionId"),
								"transactionID")
						.add(Projections.property("expdest"), "expDest"));
		if (jourOrAutre == 1) {
			criteriaTransaction.add(Restrictions.between(
					"transactionDateTransaction", dateDebut, dateFin));
		}
		if (jourOrAutre == 2) {
			criteriaTransaction.add(Restrictions.lt(
					"transactionDateTransaction", dateDebut));
		}
		// selectionné que les dossier de type personnalisé et non pas de type
		// default
		criteriaTransaction.add(Restrictions.eq(
				"dossier.typedossier.typeDossierId", 2));
		criteriaTransaction.add(Property.forName("transactionId").in(
				detachedcriteriaDestination));
		criteriaTransaction.setFirstResult(firstIndex);
		criteriaTransaction.setMaxResults(maxResult);

		if (sortField != null) {
			if (sortField.equals("courrierReference")) {
				if (descending) {
					criteriaTransaction.addOrder(Order
							.desc("dossier.dossierReference"));
				} else {
					criteriaTransaction.addOrder(Order
							.asc("dossier.dossierReference"));
				}
			} else if (sortField.equals("courrierObjet")) {
				if (descending) {
					criteriaTransaction.addOrder(Order
							.desc("dossier.dossierIntitule"));
				} else {
					criteriaTransaction.addOrder(Order
							.asc("dossier.dossierIntitule"));
				}
			} else if (sortField.equals("courrierCommentaire")) {
				if (descending) {
					criteriaTransaction.addOrder(Order
							.desc("dossier.dossierDescription"));
				} else {
					criteriaTransaction.addOrder(Order
							.asc("dossier.dossierDescription"));
				}
			} else if (sortField.equals("courrierDateReceptionEnvoi")) {
				if (descending) {
					criteriaTransaction.addOrder(Order
							.desc("transactionDateConsultation"));
				} else {
					criteriaTransaction.addOrder(Order
							.asc("transactionDateConsultation"));
				}
			}
		}
		criteriaTransaction.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));
		return criteriaTransaction.list();

	}

	@Override
	public Long countDossierEnvoyerWithCriterion(boolean isResponsable,
			List<Integer> listIdsSousUnites, List<Integer> listIdSubordonnes,
			HashMap<String, Object> filterMap, String secretaire, String sub,
			String unit, int jourOrAutre, Date dateDebut, Date dateFin,
			String type, String type1, String typeSecretaire, Integer idUser,
			String typeDossier, Integer dossierRubriqueJourId, boolean forStat) {
		Criteria criteria = getSession().createCriteria(Transaction.class);
		criteria.createAlias("dossier", "dossier")
				.createAlias("dossier.courriers", "dCourrier")
				.createAlias("dCourrier.nature", "nature")
				.createAlias("etat", "etat");
		// Projections
		criteria.setProjection(Projections
				.projectionList()
				.add(Projections.distinct(Projections
						.property("dossier.dossierId")))
				.add(Projections.property("dossier.dossierReference"),
						"courrierReference")
				.add(Projections.property("dossier.dossierIntitule"),
						"courrierObjet")
				.add(Projections.property("dossier.dossierDescription"),
						"courrierCommentaire")
				.add(Projections.property("transactionDateTransaction"),
						"courrierDateReceptionEnvoi")
				.add(Projections.property("etat.etatId"), "etatID")
				.add(Projections
						.property("transactionDestinationReelle.transactionDestinationReelleId"),
						"transactionDestinationReelID")
				.add(Projections.property("transactionDateConsultation"),
						"transactionDateConsultation")
				.add(Projections.property("transactionId"), "transactionID")
				.add(Projections.property("expdest"), "expDest"));
		// Restrictions
		// Mes Courriers et les courriers de mon unité
		Criterion crit = null;
		if (forStat) {
			crit = Restrictions.or(
					Restrictions.eq("transactionTypeIntervenant", type),
					Restrictions.eq("transactionTypeIntervenant", type1));
			if (isResponsable) {
				// Courriers de mes subordonnés
				if (sub.equals("Oui")) {
					for (Integer idSub : listIdSubordonnes) {
						crit = Restrictions.or(crit, Restrictions.eq(
								"transactionTypeIntervenant", "sub_" + idSub));
					}
				}
				// Courriers de mes sous unités
				if (unit.equals("Oui")) {
					for (Integer idSousUnit : listIdsSousUnites) {
						crit = Restrictions.or(crit, Restrictions.eq(
								"transactionTypeIntervenant", "unit_"
										+ idSousUnit));
					}
				}
				if (secretaire.equals("Oui")) {
					crit = Restrictions.or(crit, Restrictions.eq(
							"transactionTypeIntervenant", typeSecretaire));
				}
			}
		} else {
			if (isResponsable) {
				switch (dossierRubriqueJourId) {
				case 1:
					crit = Restrictions.eq("transactionTypeIntervenant", type1);
					break;
				case 2:
					crit = Restrictions.eq("transactionTypeIntervenant", type);
					break;
				case 3:
					for (Integer idSub : listIdSubordonnes) {
						if (crit == null) {
							crit = Restrictions.eq(
									"transactionTypeIntervenant", "sub_"
											+ idSub);
						} else {
							crit = Restrictions.or(crit, Restrictions.eq(
									"transactionTypeIntervenant", "sub_"
											+ idSub));
						}
					}
					break;
				case 4:
					crit = Restrictions.eq("transactionTypeIntervenant",
							typeSecretaire);
					break;
				case 5:
					for (Integer idSousUnit : listIdsSousUnites) {
						if (crit == null) {
							crit = Restrictions.eq(
									"transactionTypeIntervenant", "unit_"
											+ idSousUnit);
						} else {
							crit = Restrictions.or(crit, Restrictions.eq(
									"transactionTypeIntervenant", "unit_"
											+ idSousUnit));
						}
					}
					break;
				}
			} else {
				// Agent ou Secretaire
				crit = Restrictions.or(
						Restrictions.eq("transactionTypeIntervenant", type),
						Restrictions.eq("transactionTypeIntervenant", type1));
			}
		}
		criteria.add(crit);
		if (jourOrAutre == 1) {
			criteria.add(Restrictions.between("transactionDateTransaction",
					dateDebut, dateFin));
		}
		if (jourOrAutre == 2) {
			criteria.add(Restrictions.lt("transactionDateTransaction",
					dateDebut));
		}
		// selectionné que les dossier de type personnalisé et non pas de type
		// default
		criteria.add(Restrictions.eq("dossier.typedossier.typeDossierId", 2));

		criteria.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));
		criteria.setProjection(Projections.countDistinct("dossier.dossierId"));
		System.out.println(criteria.toString());
		System.out.println((Long) criteria.uniqueResult());
		return (Long) criteria.uniqueResult();
	}

	@Override
	public Long countDossierRecuWithCriterion(boolean isResponsable,
			List<Integer> listIdsSousUnites, List<Integer> listIdSubordonnes,
			HashMap<String, Object> filterMap, String secretaire, String sub,
			String unit, int jourOrAutre, Date dateDebut, Date dateFin,
			String type, String type1, String typeSecretaire, Integer idUser,
			String typeDossier, Integer dossierRubriqueJourId, boolean forStat) {
		DetachedCriteria detachedcriteriaDestination = DetachedCriteria
				.forClass(TransactionDestination.class);
		detachedcriteriaDestination
				.setProjection(Projections.projectionList().add(
						Projections.property("id.idTransaction"),
						"idTransaction"));
		// detachedcriteriaDestination.add(Restrictions.or(Restrictions.eq(
		// "transactionDestIdIntervenant", idUser), Restrictions.or(
		// Restrictions.eq("transactionDestTypeIntervenant", type),
		// Restrictions.eq("transactionDestTypeIntervenant", type1))));
		// Mes Courriers et les courriers de mon unité
		Criterion crit = null;
		if (forStat) {
			crit = Restrictions.or(
					Restrictions.eq("transactionDestTypeIntervenant", type),
					Restrictions.eq("transactionDestTypeIntervenant", type1));
			if (isResponsable) {
				// Courriers de mes subordonnés
				if (sub.equals("Oui")) {
					for (Integer idSub : listIdSubordonnes) {
						crit = Restrictions.or(crit, Restrictions.eq(
								"transactionDestTypeIntervenant", "sub_"
										+ idSub));
					}
				}
				// Courriers de mes sous unités
				if (unit.equals("Oui")) {
					for (Integer idSousUnit : listIdsSousUnites) {
						crit = Restrictions.or(crit, Restrictions.eq(
								"transactionDestTypeIntervenant", "unit_"
										+ idSousUnit));
					}
				}
				if (secretaire.equals("Oui")) {
					crit = Restrictions.or(crit, Restrictions.eq(
							"transactionDestTypeIntervenant", typeSecretaire));
				}
			}
		} else {
			if (isResponsable) {
				switch (dossierRubriqueJourId) {
				case 1:
					crit = Restrictions.eq("transactionDestTypeIntervenant",
							type1);
					break;
				case 2:
					crit = Restrictions.eq("transactionDestTypeIntervenant",
							type);
					break;
				case 3:
					for (Integer idSub : listIdSubordonnes) {
						if (crit == null) {
							crit = Restrictions.eq(
									"transactionDestTypeIntervenant", "sub_"
											+ idSub);
						} else {
							crit = Restrictions.or(crit, Restrictions.eq(
									"transactionDestTypeIntervenant", "sub_"
											+ idSub));
						}

					}
					break;
				case 4:
					crit = Restrictions.eq("transactionDestTypeIntervenant",
							typeSecretaire);
					break;
				case 5:
					for (Integer idSousUnit : listIdsSousUnites) {
						if (crit == null) {
							crit = Restrictions.eq(
									"transactionDestTypeIntervenant", "unit_"
											+ idSousUnit);
						} else {
							crit = Restrictions.or(crit, Restrictions.eq(
									"transactionDestTypeIntervenant", "unit_"
											+ idSousUnit));
						}
					}
					break;
				}
			} else {
				crit = Restrictions
						.or(Restrictions.eq("transactionDestTypeIntervenant",
								type), Restrictions.eq(
								"transactionDestTypeIntervenant", type1));
			}
		}
		detachedcriteriaDestination.add(crit);
		Criteria criteriaTransaction = getSession().createCriteria(
				Transaction.class);
		criteriaTransaction.createAlias("dossier", "dossier")
				.createAlias("dossier.courriers", "dCourrier")
				.createAlias("dCourrier.nature", "nature")
				.createAlias("etat", "etat");

		// Projections
		criteriaTransaction
				.setProjection(Projections
						.projectionList()
						.add(Projections.distinct(Projections
								.property("dossier.dossierId")))
						.add(Projections.property("dossier.dossierReference"),
								"courrierReference")
						.add(Projections.property("dossier.dossierIntitule"),
								"courrierObjet")
						.add(Projections.property("dossier.dossierDescription"),
								"courrierCommentaire")
						.add(Projections.property("transactionDateTransaction"),
								"courrierDateReceptionEnvoi")
						.add(Projections.property("etat.etatId"), "etatID")
						.add(Projections
								.property("transactionDestinationReelle.transactionDestinationReelleId"),
								"transactionDestinationReelID")
						.add(Projections
								.property("transactionDateConsultation"),
								"transactionDateConsultation")
						.add(Projections.property("transactionId"),
								"transactionID")
						.add(Projections.property("expdest"), "expDest"));
		if (jourOrAutre == 1) {
			criteriaTransaction.add(Restrictions.between(
					"transactionDateTransaction", dateDebut, dateFin));
		}
		if (jourOrAutre == 2) {
			criteriaTransaction.add(Restrictions.lt(
					"transactionDateTransaction", dateDebut));
		}
		// selectionné que les dossier de type personnalisé et non pas de type
		// default
		criteriaTransaction.add(Restrictions.eq(
				"dossier.typedossier.typeDossierId", 2));
		criteriaTransaction.add(Property.forName("transactionId").in(
				detachedcriteriaDestination));
		criteriaTransaction.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));
		criteriaTransaction.setProjection(Projections
				.countDistinct("dossier.dossierId"));
		System.out.println(criteriaTransaction.toString());
		System.out.println((Long) criteriaTransaction.uniqueResult());
		return (Long) criteriaTransaction.uniqueResult();
	}

	// boolean isResponsable,List<Integer> listIdsSousUnites,List<Integer>
	// listIdSubordonnes,
	// HashMap<String, Object> filterMap, String sortField,
	// boolean descending,String secretaire,
	// String sub, String unit, int jourOrAutre, Date dateDebut, Date dateFin,
	// String type, String type1,String typeSecretaire, Integer idUser,
	// Integer typeTransmission, String stateTraitement,
	@SuppressWarnings("unchecked")
	@Override
	public List<Courrier> findCourrierEnvoyerForLien(Integer idCourrierCourant,
			boolean isResponsable, List<Integer> listIdsSousUnites,
			List<Integer> listIdSubordonnes, String secretaire, String sub,
			String unit, String type, String type1, String typeSecretaire,
			Integer idUser, boolean isForLienCourrier) {
		DetachedCriteria detachedCriteriaTransaction = DetachedCriteria
				.forClass(Transaction.class);
		detachedCriteriaTransaction.createAlias("dossier", "dossier")
				.createAlias("dossier.courriers", "dCourrier")
				.createAlias("dCourrier.nature", "nature")
				.createAlias("etat", "etat");

		// Projections
		detachedCriteriaTransaction.setProjection(Projections.projectionList()
				.add(Projections.distinct(Projections
						.property("dCourrier.idCourrier"))));
		// detachedCriteriaTransaction.setProjection(Projections.projectionList()
		// .add(Projections
		// .property("dCourrier.idCourrier")));
		detachedCriteriaTransaction.add(Restrictions.eq(
				"dossier.typedossier.typeDossierId", 1));

		// Mes Courriers et les courriers de mon unité
		Criterion crit = Restrictions.or(
				Restrictions.eq("transactionTypeIntervenant", type),
				Restrictions.eq("transactionTypeIntervenant", type1));
		if (isResponsable) {
			// Courriers de mes subordonnés
			if (sub.equals("Oui")) {
				for (Integer idSub : listIdSubordonnes) {
					crit = Restrictions.or(crit, Restrictions.eq(
							"transactionTypeIntervenant", "sub_" + idSub));
				}
			}
			// Courriers de mes sous unités
			if (unit.equals("Oui")) {
				for (Integer idSousUnit : listIdsSousUnites) {
					crit = Restrictions
							.or(crit, Restrictions.eq(
									"transactionTypeIntervenant", "unit_"
											+ idSousUnit));
				}
			}
			if (secretaire.equals("Oui")) {
				crit = Restrictions.or(crit, Restrictions.eq(
						"transactionTypeIntervenant", typeSecretaire));
			}
		}
		detachedCriteriaTransaction.add(crit);
		// restriction du courrier courant
		if (isForLienCourrier) {
			detachedCriteriaTransaction.add(Restrictions.ne(
					"dCourrier.idCourrier", idCourrierCourant));
		}

		Criteria criteriaCourrier = getSession().createCriteria(Courrier.class);
		criteriaCourrier.add(Property.forName("idCourrier").in(
				detachedCriteriaTransaction));

		return criteriaCourrier.list();
	}

	@SuppressWarnings("unchecked")
	public List<Courrier> findCourrierRecuForLien(Integer idCourrierCourant,
			boolean isResponsable, List<Integer> listIdsSousUnites,
			List<Integer> listIdSubordonnes, String secretaire, String sub,
			String unit, String type, String type1, String typeSecretaire,
			Integer idUser, boolean isForLienCourrier) {
		// TransactionDestination
		DetachedCriteria detachedCriteriaDestination = DetachedCriteria
				.forClass(TransactionDestination.class);

		detachedCriteriaDestination.setProjection(Projections.projectionList()
				.add(Projections.distinct(Projections
						.property("id.idTransaction"))));

		// Mes Courriers et les courriers de mon unité
		Criterion crit = Restrictions.or(
				Restrictions.eq("transactionDestTypeIntervenant", type),
				Restrictions.eq("transactionDestTypeIntervenant", type1));
		if (isResponsable) {
			// Courriers de mes subordonnés
			if (sub.equals("Oui")) {
				for (Integer idSub : listIdSubordonnes) {
					crit = Restrictions.or(crit, Restrictions.eq(
							"transactionDestTypeIntervenant", "sub_" + idSub));
				}
			}
			// Courriers de mes sous unités
			if (unit.equals("Oui")) {
				for (Integer idSousUnit : listIdsSousUnites) {
					crit = Restrictions.or(crit, Restrictions.eq(
							"transactionDestTypeIntervenant", "unit_"
									+ idSousUnit));
				}
			}
			if (secretaire.equals("Oui")) {
				crit = Restrictions.or(crit, Restrictions.eq(
						"transactionDestTypeIntervenant", typeSecretaire));
			}
		}
		detachedCriteriaDestination.add(crit);
		// Transaction
		DetachedCriteria detachedCriteriaTr = DetachedCriteria
				.forClass(Transaction.class);
		detachedCriteriaTr.createAlias("dossier", "dossier")
				.createAlias("dossier.courriers", "dCourrier")
				.createAlias("dCourrier.nature", "nature")
				.createAlias("etat", "etat");
		detachedCriteriaTr.setProjection(Projections.projectionList().add(
				Projections.distinct(Projections
						.property("dCourrier.idCourrier"))));
		// detachedCriteriaTr.setProjection(Projections.projectionList().add(
		// Projections
		// .property("dCourrier.idCourrier")));
		detachedCriteriaTr.add(Property.forName("transactionId").in(
				detachedCriteriaDestination));
		if (isForLienCourrier) {
			detachedCriteriaTr.add(Restrictions.ne("dCourrier.idCourrier",
					idCourrierCourant));
		}
		detachedCriteriaTr.add(Restrictions.eq(
				"dossier.typedossier.typeDossierId", 1));
		// Courrier

		Criteria criteriaCourrier = getSession().createCriteria(Courrier.class);
		criteriaCourrier.add(Property.forName("idCourrier").in(
				detachedCriteriaTr));

		return criteriaCourrier.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Courrier> listCourrierNonAffecteDossier(
			Integer idCourrierCourant, boolean isResponsable,
			List<Integer> listIdsSousUnites, List<Integer> listIdSubordonnes,
			String secretaire, String sub, String unit, String type,
			String type1, String typeSecretaire, Integer idUser,
			boolean isForLienCourrier) {

		Map<String, Object> params = new HashMap<String, Object>();
		int i = 0;
		StringBuffer sql = new StringBuffer(
				"SELECT DISTINCT cr.*"
						+ " FROM courrier cr"
						+ " INNER JOIN courrierdossier cd ON cr.idCourrier = cd.idCourrier"
						+ " INNER JOIN dossier d ON cd.dossierId = d.dossierId"
						+ " INNER JOIN transactionn t ON d.dossierId = t.dossierId"
						+ " INNER JOIN transactiondest td ON t.transactionId = td.idTransaction"
						+ " AND d.typeDossierId = 1"
						+ " AND cd.dossierId != :id"
						+ " AND (t.transactionTypeIntervenant = :type"
						+ " OR t.transactionTypeIntervenant = :type1");
		params.put("id", 1);
		params.put("type", type);
		params.put("type1", type1);
		if (isResponsable) {
			// Courriers de mes subordonnés
			if (sub.equals("Oui")) {
				for (Integer idSub : listIdSubordonnes) {
					sql.append(" OR t.transactionTypeIntervenant = :sub" + i);
					params.put("sub" + i, "sub_" + idSub);
					i++;
				}
			}
			// Courriers de mes sous unités
			if (unit.equals("Oui")) {
				for (Integer idSousUnit : listIdsSousUnites) {
					sql.append(" OR t.transactionTypeIntervenant = :unit" + i);
					params.put("unit" + i, "unit_" + idSousUnit);
					i++;
				}
			}
			if (secretaire.equals("Oui")) {
				sql.append(" OR t.transactionTypeIntervenant = :typeSecretaire");
				params.put("typeSecretaire", typeSecretaire);
			}
		}
		sql.append(" OR td.transactionDestTypeIntervenant = :type"
				+ " OR td.transactionDestTypeIntervenant = :type1");
		if (isResponsable) {
			// Courriers de mes subordonnés
			if (sub.equals("Oui")) {
				for (Integer idSub : listIdSubordonnes) {
					sql.append(" OR td.transactionDestTypeIntervenant = :sub"
							+ i);
					params.put("sub" + i, "sub_" + idSub);
					i++;
				}
			}
			// Courriers de mes sous unités
			if (unit.equals("Oui")) {
				for (Integer idSousUnit : listIdsSousUnites) {
					sql.append(" OR td.transactionDestTypeIntervenant = :unit"
							+ i);
					params.put("unit" + i, "unit_" + idSousUnit);
					i++;
				}
			}
			if (secretaire.equals("Oui")) {
				sql.append(" OR td.transactionDestTypeIntervenant = :typeSecretaire");
				params.put("typeSecretaire", typeSecretaire);
			}
		}
		sql.append(")");
		// restriction du courrier courant
		if (isForLienCourrier) {
			sql.append(" AND c.idCourrier != :idCourrierCourant");
			params.put("idCourrierCourant", idCourrierCourant);
		}

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addEntity(Courrier.class);

		Iterator<String> iterMax = params.keySet().iterator();
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = params.get(name);
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}
		// query.setProperties(params);
		System.out.println(query.getQueryString());
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Courrier> findCourrierForLienBOC(Integer idUser,
			String categorieCourrierJour, Integer idCourrierCourant,
			boolean isForLienCourrier) {
		long debut = System.currentTimeMillis();
		// Transaction
		DetachedCriteria detachedCriteriaTransaction = DetachedCriteria
				.forClass(Transaction.class);
		detachedCriteriaTransaction.createAlias("dossier", "dossier")
				.createAlias("dossier.courriers", "dCourrier")
				.createAlias("dCourrier.nature", "nature")
				.createAlias("etat", "etat").createAlias("expdest", "expdest");
		System.out.print("        ***debut***");
		System.out.println(System.currentTimeMillis() - debut);
		// Projections
		detachedCriteriaTransaction.setProjection(Projections.projectionList()
				.add(Projections.distinct(Projections
						.property("dCourrier.idCourrier"))));
		detachedCriteriaTransaction.add(Restrictions.eq(
				"dossier.typedossier.typeDossierId", 1));

		detachedCriteriaTransaction.add(Restrictions
				.eq("idUtilisateur", idUser));
		System.out.print("        ******");
		System.out.println(System.currentTimeMillis() - debut);
		// Arrivé Départ
		if (categorieCourrierJour.equals("A")) { // Courrier d'arrivé BOC
			detachedCriteriaTransaction.add(Restrictions
					.isNotNull("expdest.expdestexterne"));
		}
		if (categorieCourrierJour.equals("D")) { // Courrier de depart BOC
			detachedCriteriaTransaction.add(Restrictions
					.isNull("expdest.expdestexterne"));
		}
		System.out.print("       ******");
		System.out.println(System.currentTimeMillis() - debut);
		// Fin Arrivé Départ
		// restriction du courrier courant
		if (isForLienCourrier) {
			detachedCriteriaTransaction.add(Restrictions.ne(
					"dCourrier.idCourrier", idCourrierCourant));
		}

		Criteria criteriaCourrier = getSession().createCriteria(Courrier.class);
		criteriaCourrier.add(Property.forName("idCourrier").in(
				detachedCriteriaTransaction));
		System.out.print("         ***Fin***");
		System.out.println(System.currentTimeMillis() - debut);
		System.out.println(criteriaCourrier.toString());
		return criteriaCourrier.list();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Courrier> findAllCourrierForBOC(Integer idUser,
			Integer idCourrierCourant, boolean isForLienCourrier) {
		long debut = System.currentTimeMillis();
		// Transaction
		DetachedCriteria detachedCriteriaTransaction = DetachedCriteria
				.forClass(Transaction.class);
		detachedCriteriaTransaction.createAlias("dossier", "dossier")
				.createAlias("dossier.courriers", "dCourrier")
				.createAlias("dCourrier.nature", "nature")
				.createAlias("etat", "etat").createAlias("expdest", "expdest");
		System.out.print("        ***debut***");
		System.out.println(System.currentTimeMillis() - debut);
		// Projections
		detachedCriteriaTransaction.setProjection(Projections.projectionList()
				.add(Projections.distinct(Projections
						.property("dCourrier.idCourrier"))));
		detachedCriteriaTransaction.add(Restrictions.eq(
				"dossier.typedossier.typeDossierId", 1));

		detachedCriteriaTransaction.add(Restrictions
				.eq("idUtilisateur", idUser));
		System.out.print("        ******");
		System.out.println(System.currentTimeMillis() - debut);
		// Arrivé Départ

		System.out.print("       ******");
		System.out.println(System.currentTimeMillis() - debut);
		// Fin Arrivé Départ
		// restriction du courrier courant

		detachedCriteriaTransaction.add(Restrictions.ne("dCourrier.idCourrier",
				idCourrierCourant));

		Criteria criteriaCourrier = getSession().createCriteria(Courrier.class);
		criteriaCourrier.add(Property.forName("idCourrier").in(
				detachedCriteriaTransaction));
		System.out.print("         ***Fin***");
		System.out.println(System.currentTimeMillis() - debut);
		System.out.println(criteriaCourrier.toString());
		return criteriaCourrier.list();

	}

	public void getTEST() {
		// Criteria crit = getSession().createCriteria(Transaction);
		// crit.add(org.hibernate.criterion.Expression.e)
	}

	// Statistique new
	@SuppressWarnings("unchecked")
	public List<CourrierInformations> findCourrierExterneBOCWithCriterion(
			String type, List<Integer> listIdBocMembers,
			String categorieCourrierJour, Date dateDebut, Date dateFin) {

		Criteria criteriaTransaction = getSession().createCriteria(
				Transaction.class);
		criteriaTransaction.createAlias("dossier", "dossier")
				.createAlias("dossier.courriers", "dCourrier")
				.createAlias("dCourrier.nature", "nature")
				.createAlias("dCourrier.transmission", "transmission")
				.createAlias("etat", "etat").createAlias("expdest", "expdest");
		// Projections
		criteriaTransaction
				.setProjection(Projections
						.projectionList()
						.add(Projections.property("dCourrier.idCourrier"),
								"courrierID")
						.add(Projections.groupProperty("dCourrier.idCourrier"))
						.add(Projections.max("transactionId"), "transactionID"));

		criteriaTransaction.add(Restrictions.eq(
				"dossier.typedossier.typeDossierId", 1));
		criteriaTransaction.add(Restrictions.between(
				"transactionDateTransaction", dateDebut, dateFin));
		if (categorieCourrierJour.equals("A")) { // Courrier d'arrivé BOC
			criteriaTransaction.add(Restrictions.like(
					"dCourrier.courrierReferenceCorrespondant", "A%"));
		}
		if (categorieCourrierJour.equals("D")) { // Courrier de depart BOC
			criteriaTransaction.add(Restrictions.like(
					"dCourrier.courrierReferenceCorrespondant", "D%"));
		}
		// Fin Arrivé Départ

		criteriaTransaction.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));
		List<CourrierInformations> listCR = (List<CourrierInformations>) criteriaTransaction
				.list();
		return listCR;

	}

	@SuppressWarnings("unchecked")
	public List<CourrierInformations> findCourrierArriveeDepartByStructure(
			List<String> listMembers, Integer courrierDepartArrivee,
			Date dateDebut, Date dateFin) {

		Criteria criteria = getSession().createCriteria(Transaction.class);
		criteria.createAlias("dossier", "dossier")
				.createAlias("dossier.courriers", "dCourrier")
				.createAlias("dCourrier.nature", "nature")
				.createAlias("etat", "etat");
		criteria.add(Restrictions.eq("dossier.typedossier.typeDossierId", 1));
		criteria.add(Restrictions.between("transactionDateTransaction",
				dateDebut, dateFin));
		// Projections
		criteria.setProjection(Projections
				.projectionList()
				.add(Projections.property("dCourrier.idCourrier"), "courrierID")
				.add(Projections.groupProperty("dCourrier.idCourrier"))
				.add(Projections.max("transactionId"), "transactionID"));
		switch (courrierDepartArrivee) {
		case 1:
			// envoyé
			criteria.add(Restrictions.in("transactionTypeIntervenant",
					listMembers));
			break;

		case 2:
			// recu
			DetachedCriteria detachedCriteriaTrDest = DetachedCriteria
					.forClass(TransactionDestination.class);
			detachedCriteriaTrDest.setProjection(Projections.projectionList()
					.add(Projections.property("id.idTransaction"),
							"idTransaction"));
			Criterion critD = Restrictions.in("transactionDestTypeIntervenant",
					listMembers);
			detachedCriteriaTrDest.add(critD);
			criteria.add(Property.forName("transactionId").in(
					detachedCriteriaTrDest));
			break;
		}
		criteria.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));
		return criteria.list();
	}

	// public List<CourrierInformations> findCourrierArriveeDepartByStructure(
	// List<String> listMembers, Integer courrierDepartArrivee, Date dateDebut,
	// Date dateFin) {
	//
	// Criteria criteria = getSession().createCriteria(Transaction.class);
	// criteria.createAlias("dossier", "dossier")
	// .createAlias("dossier.courriers", "dCourrier")
	// .createAlias("dCourrier.nature", "nature")
	// .createAlias("etat", "etat");
	// criteria.add(Restrictions.eq("dossier.typedossier.typeDossierId", 1));
	// criteria.add(Restrictions.between(
	// "transactionDateTransaction", dateDebut, dateFin));
	// // Projections
	// criteria
	// .setProjection(Projections
	// .projectionList()
	// .add(Projections.property("dCourrier.idCourrier"), "courrierID")
	// .add(Projections.groupProperty("dCourrier.idCourrier"))
	// .add(Projections.max("transactionId"),"transactionID"));
	// switch (courrierDepartArrivee) {
	// case 1:
	// // envoyé
	// criteria.add(Restrictions.in("transactionTypeIntervenant", listMembers));
	// break;
	//
	// case 2:
	// // recu
	// DetachedCriteria detachedCriteriaTrDest =
	// DetachedCriteria.forClass(TransactionDestination.class);
	// detachedCriteriaTrDest.setProjection(Projections.projectionList().add(
	// Projections.property("id.idTransaction"), "idTransaction"));
	// Criterion critD = Restrictions.in("transactionDestTypeIntervenant",
	// listMembers);
	// detachedCriteriaTrDest.add(critD);
	// criteria.add(Property.forName("transactionId").in(
	// detachedCriteriaTrDest));
	// break;
	// }
	//
	// return criteria.list();
	// }

	public void executeSQLQuery(StringBuffer query) {
		SQLQuery queryMax = getSession().createSQLQuery(query.toString());
		queryMax.list();
	}

	@Override
	public Integer getCourrierLastIdByTypeOrdreAndAnnee(String type,
			Integer annee) throws HibernateException {
		StringBuffer hql = new StringBuffer(
				"SELECT MAX(c.courrierTypeOrdre) 'courrierTypeOrdre'"
						+ " FROM courrier c"
						+ " WHERE c.courrierType = :type AND c.courrierDateReceptionAnnee = :annee");

		SQLQuery query = getSession().createSQLQuery(hql.toString());

		query.setString("type", type);
		query.setInteger("annee", annee);

		System.out.println(query.getQueryString());
		return (Integer) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> executeQuery3(String myRequet) {
		StringBuilder sql = new StringBuilder(myRequet);
		SQLQuery resultat = getSession().createSQLQuery(sql.toString());
		return resultat.list();
	}

	// [JS] :Statistiques
/*
	@SuppressWarnings("unchecked")
	@Override
	public List<StatistiqueCourrierUtilisateur> countCourrierMembreBOCWithCriterion(
			HashMap<String, Object> filterMap, int jourOrAutre, Date dateDebut,
			Date dateFin, List<Integer> listIdBocMembers,
			String categorieCourrierJour) {

		Map<String, Object> params = new HashMap<String, Object>();

		StringBuffer trCount = new StringBuffer(
				"SELECT t.idUtilisateur AS 'idutilisateur',COUNT(DISTINCT d.dossierId) AS 'nbrCourrier'"
						+ " FROM transactionn t, dossier d, courrierdossier cd, courrier c, transactionn tm"
						+ " WHERE t.dossierId = d.dossierId"
						+ " AND d.dossierId = cd.dossierId"
						+ " AND cd.idCourrier = c.idCourrier"
						+ " AND t.transactionFirst = tm.transactionId");
		// ---------------------------------------MM---------------------------------------

		Calendar calendar = Calendar.getInstance();
		Date dateAujourd = calendar.getTime();
		System.out.println("dateDebut :" + dateDebut);

		// **************************************

		if (jourOrAutre == 1) {
			trCount.append(" AND c.courrierDateReception BETWEEN :dateDebut AND :dateFin");

			params.put("dateDebut", dateDebut);
			params.put("dateFin", dateFin);
		}
		int i = 0;
		String whereMax = " ( ";
		trCount.append(" AND ");
		System.out.println("list ID Boc Members : " + listIdBocMembers.size());
		for (Integer id : listIdBocMembers) {
			System.out.println("ID : " + id);
			whereMax += "t.idUtilisateur = :id" + i + " OR ";
			params.put("id" + i, id);
			i++;
		}
		int lastIndex = whereMax.lastIndexOf("OR");
		whereMax = whereMax.substring(0, lastIndex);
		whereMax += " ) ";
		trCount.append(whereMax);
		// Arrivé Départ
		System.out.println("JS ==>categorieCourrierJour "
				+ categorieCourrierJour);
		if (categorieCourrierJour.equals("A")) {
			trCount.append(" AND t.courrierReferenceCorrespondant LIKE 'A%'");
		}

		if (categorieCourrierJour.equals("D")) {
			trCount.append(" AND t.courrierReferenceCorrespondant LIKE 'D%'");
		}

		trCount.append(" GROUP BY t.idUtilisateur");

		SQLQuery query = getSession().createSQLQuery(trCount.toString());

		query.setResultTransformer(Transformers
				.aliasToBean(StatistiqueCourrierUtilisateur.class));
		query.addScalar("idutilisateur", new IntegerType());
		query.addScalar("nbrCourrier", new IntegerType());

		System.out.println("Paramètres =" + params);
		Iterator<String> iterMax = params.keySet().iterator();
		System.out.println("Iter MAx :" + iterMax);
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = params.get(name);
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}
		// query.setProperties(params);
		System.out.println(query.getQueryString());
		return query.list();

	}*/

	@SuppressWarnings("unchecked")
	public List<Object[]> courrierStatistiquesBOCStructureByNature(
			Date dateDebut, Date dateFin, Integer categorieCourrierJour,
			Integer natureId, String valueAttr, List<String> types) {

		Map<String, Object> pars = new HashMap<String, Object>();
		String intervenant = "";
		System.out.println("valueAttr  : " + valueAttr);
		System.out.println("categorieCourrierJour :" + categorieCourrierJour);
		// Départs
		if (categorieCourrierJour.equals(1)) {
			intervenant = "tmax.transactionTypeIntervenant";
		}

		// Arrivées
		if (categorieCourrierJour.equals(2)) {
			intervenant = "tdmax.transactionDestTypeIntervenant";
		}
		StringBuffer trMax = new StringBuffer(
				"SELECT t.intervenant,COUNT(*)"
						+ " FROM (SELECT MAX(tmax.transactionId), cdmax.idCourrier, "
						+ intervenant
						+ " AS 'intervenant'"
						+ " FROM transactionn tmax, courrierdossier cdmax, courrier cmax");
		if (categorieCourrierJour.equals(2)) {
			trMax.append(", transactiondest tdmax");
		}
		trMax.append(" WHERE tmax.dossierId = cdmax.dossierId"
				+ " AND cdmax.idCourrier = cmax.idCourrier" + " AND "
				+ intervenant + " IS NOT NULL");

		// Arrivé Départ
		if (categorieCourrierJour.equals(2)) {
			trMax.append(" AND tmax.transactionId = tdmax.idTransaction");
		}

		trMax.append(" AND tmax.transactionDateTransaction BETWEEN :dateDebut AND :dateFin");
		pars.put("dateDebut", dateDebut);
		pars.put("dateFin", dateFin);
		String typDest = "";
		String whereMax = "";
		int i = 0;
		if (types.size() > 0) {
			for (String ed : types) {
				typDest += ":ed" + i + ", ";
				pars.put("ed" + i, ed);
				i++;
			}
			typDest = typDest.substring(0, typDest.length() - 2);
			System.out.println("typDest : " + typDest);

			if (categorieCourrierJour.equals(1)) {
				whereMax += " AND tmax.transactionTypeIntervenant IN ("
						+ typDest + ")";
			} else if (categorieCourrierJour.equals(2)) {
				whereMax += " AND tdmax.transactionDestTypeIntervenant IN ("
						+ typDest + ")";
			}

		}
		trMax.append(whereMax);
		trMax.append(" GROUP BY cdmax.idCourrier, "
				+ intervenant
				+ ") t"
				+ " LEFT JOIN courrier c ON c.idCourrier = t.idCourrier WHERE c.idNature = :natureId "
				+ " GROUP BY t.intervenant,c.idNature");
		pars.put("natureId", natureId);

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());

		Iterator<String> iterMax = pars.keySet().iterator();
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println(queryMax.getQueryString());
		return queryMax.list();
	}

	// @SuppressWarnings("unchecked")
	// public List<StatistiqueCourrierStructureByNature>
	// courrierStatistiquesBOCStructureByNature(Date dateDebut,
	// Date dateFin, Integer categorieCourrierJour, Integer categorieId) {
	//
	// Map<String, Object> pars = new HashMap<String, Object>();
	//
	// String intervenant = "";
	// System.out.println("categorieCourrierJour :"+categorieCourrierJour);
	// //Départs
	// if (categorieCourrierJour.equals(1)) {
	// intervenant = "tmax.transactionTypeIntervenant";
	// }
	//
	// //Arrivées
	// if (categorieCourrierJour.equals(2)) {
	// intervenant = "tdmax.transactionDestTypeIntervenant";
	// }
	// StringBuffer trMax = new StringBuffer(
	// "SELECT t.intervenant as intervenantId,c.idNature as natureId,COUNT(*) as countCourrier"
	// + " FROM (SELECT MAX(tmax.transactionId), cdmax.idCourrier, "
	// + intervenant
	// + " AS 'intervenant'"
	// + " FROM transactionn tmax, courrierdossier cdmax, courrier cmax");
	// if (categorieCourrierJour.equals(2)) {
	// trMax.append(", transactiondest tdmax");
	// }
	// trMax.append(" WHERE tmax.dossierId = cdmax.dossierId"
	// + " AND cdmax.idCourrier = cmax.idCourrier" + " AND "
	// + intervenant + " IS NOT NULL");
	//
	// // Arrivé Départ
	// if (categorieCourrierJour.equals(2)) {
	// trMax.append(" AND tmax.transactionId = tdmax.idTransaction");
	// }
	//
	// trMax.append(" AND tmax.transactionDateTransaction BETWEEN :dateDebut AND :dateFin");
	// pars.put("dateDebut", dateDebut);
	// pars.put("dateFin", dateFin);
	//
	// trMax.append(" GROUP BY cdmax.idCourrier, " + intervenant + ") t"
	// +
	// " LEFT JOIN courrier c ON c.idCourrier = t.idCourrier WHERE c.idNature IN (SELECT n.natureId FROM nature n,naturecategorie cat WHERE cat.natureCategorieId= :catgId AND cat.natureCategorieId=n.natureCategorieId)"
	// + " GROUP BY t.intervenant,c.idNature");
	// pars.put("catgId", categorieId);
	//
	// SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
	//
	//
	// Iterator<String> iterMax = pars.keySet().iterator();
	// queryMax.setResultTransformer(Transformers
	// .aliasToBean(StatistiqueCourrierStructureByNature.class));
	// queryMax.addScalar("intervenantId", new StringType());
	// queryMax.addScalar("natureId", new IntegerType());
	// queryMax.addScalar("countCourrier", new IntegerType());
	// while (iterMax.hasNext()) {
	// String name = iterMax.next();
	// Object value = pars.get(name);
	// if (pars.get(name).getClass() == String.class) {
	// queryMax.setString(name, (String) value);
	// } else if (pars.get(name).getClass() == Date.class) {
	// queryMax.setDate(name, (Date) value);
	// } else if (pars.get(name).getClass() == Integer.class) {
	// queryMax.setInteger(name, (Integer) value);
	// } else {
	// queryMax.setParameterList(name, (List<String>) value);
	// }
	// }
	//
	// System.out.println(queryMax.getQueryString());
	// return queryMax.list();
	// }
	//
/*
	@SuppressWarnings("unchecked")
	public List<StatistiqueCourrierStructureByNature> courrierStatistiquesBOCStructuretest(
			Date dateDebut, Date dateFin, Integer categorieCourrierJour,
			Integer categorieId) {

		Map<String, Object> pars = new HashMap<String, Object>();

		String intervenant = "";
		System.out.println("Date Début :" + dateDebut);
		System.out.println("Date Fin :" + dateFin);
		System.out.println("categorieCourrierJour :" + categorieCourrierJour);

		if (categorieCourrierJour.equals(1)) {
			intervenant = "tmax.transactionTypeIntervenant";
		}
		if (categorieCourrierJour.equals(2)) {
			intervenant = "tdmax.transactionDestTypeIntervenant";
		}
		StringBuffer trMax = new StringBuffer(
				"SELECT t.intervenant AS intervenantId,c.idNature AS natureId , COUNT(*) AS countCourrier"
						+ " FROM (SELECT MAX(tmax.transactionId), cdmax.idCourrier, "
						+ intervenant
						+ " 'intervenant'"
						+ " FROM transactionn tmax, courrierdossier cdmax, courrier cmax");
		if (categorieCourrierJour.equals(2)) {
			trMax.append(", transactiondest tdmax");
		}
		trMax.append(" WHERE tmax.dossierId = cdmax.dossierId"
				+ " AND cdmax.idCourrier = cmax.idCourrier" + " AND "
				+ intervenant + " IS NOT NULL");

		// Arrivé Départ
		if (categorieCourrierJour.equals(2)) {
			trMax.append(" AND tmax.transactionId = tdmax.idTransaction");
		}

		trMax.append(" AND tmax.transactionDateTransaction BETWEEN :dateDebut AND :dateFin");
		pars.put("dateDebut", dateDebut);
		pars.put("dateFin", dateFin);

		trMax.append(" GROUP BY cdmax.idCourrier, "
				+ intervenant
				+ ") t"
				+ " LEFT JOIN courrier c ON c.idCourrier = t.idCourrier WHERE c.idNature IN (SELECT n.natureId FROM nature n,naturecategorie cat WHERE cat.natureCategorieId= :catgId AND cat.natureCategorieId=n.natureCategorieId)"
				+ " GROUP BY t.intervenant");
		pars.put("catgId", categorieId);

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		queryMax.setResultTransformer(Transformers
				.aliasToBean(StatistiqueCourrierStructureByNature.class));
		queryMax.addScalar("natureId", new IntegerType());
		queryMax.addScalar("countCourrier", new IntegerType());
		Iterator<String> iterMax = pars.keySet().iterator();
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println(queryMax.getQueryString());
		return queryMax.list();
	}
*/
	@Override
	public List<Object[]> courrierStatistiquesBOCReponseParUniteNature(
			String type, Date dateDebut, Date dateFin, Integer idNature) {
		Map<String, Object> pars = new HashMap<String, Object>();
		StringBuffer trMax = new StringBuffer(
				"SELECT tr.transactionDestDateTransfert, c.courrierDateReponse, tr.idTransaction, tr.idExpDest"
						+ " FROM transactiondest tr, transactionn t, courrier c, courrierdossier cd"
						+ " WHERE t.transactionId = tr.idTransaction"
						+ " AND t.dossierId = cd.dossierId"
						+ " AND cd.idCourrier = c.idCourrier"
						+ " AND c.courrierNecessiteReponse = 'oui'"
						+ " AND t.transactionDateTransaction BETWEEN :dateDebut AND :dateFin"
						+ " AND tr.transactionDestTypeIntervenant = :type"
						+ " AND c.idNature = :idNature"
						+ " AND c.courrierDateReponse IS NOT NULL" +
						// " AND tr.transactionDestDateTransfert IS NOT NULL" +
						" ORDER BY tr.idTransaction DESC");

		pars.put("dateDebut", dateDebut);
		pars.put("dateFin", dateFin);
		pars.put("type", type);
		pars.put("idNature", idNature);

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		Iterator<String> iterMax = pars.keySet().iterator();
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}
		System.out.println(queryMax.getQueryString());
		// List l = queryMax.list();
		// System.out.println(l.get(0));
		return queryMax.list();
	}

	// JS

	@Override
	public Long countCourrierNotBOCWithCriterion(
			HashMap<String, Object> filterMap, int jourOrAutre, Date dateDebut,
			Date dateFin, List<String> types, List<Integer> listIdBocMembers,
			String typeTransmission, String stateTraitement,
			String categorieCourrierJour) {

		System.out
				.println("size listIdBocMembers : " + listIdBocMembers.size());
		System.out.println("type : " + types);
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer trCount = new StringBuffer(
				"SELECT COUNT(DISTINCT d.dossierId) AS 'nbrc'"
						+ " FROM transactionn t, dossier d, courrierdossier cd, courrier c, transactionn tm"
						+ " WHERE t.dossierId = d.dossierId"
						+ " AND d.dossierId = cd.dossierId"
						+ " AND cd.idCourrier = c.idCourrier"
						+ " AND t.transactionFirst = tm.transactionId");
		// ---------------------------------------MM---------------------------------------
		// ---------------------------------Debut
		// optimisation-----------------------------
		Calendar calendar = Calendar.getInstance();
		Date dateAujourd = calendar.getTime();

		// JS :Formatter date Début **********************************
		// System.out.println("Date Début Avant formattage :"+dateDebut);
		// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// String dateD = format.format(dateDebut);
		System.out.println("dateDebut :" + dateDebut);

		// **************************************

		// BOCT
		// nombre d'enregistremenrt des courrier de cette année
		// System.out.println("JS ==>Jour Or Autre :" + jourOrAutre);
		if (jourOrAutre == 13) {

			trCount.append(" AND c.courrierDateReceptionAnnee =:dateAjourdhuit");
			params.put("dateAjourdhuit", dateAujourd.getYear() + 1900);

		}
		// nombre d'enregistremenrt des courrier de ce mois
		if (jourOrAutre == 14) {
			System.out.println("************************");
			trCount.append(" AND c.courrierDateReceptionAnnee =:anneeEnCours AND c.courrierDateReceptionMois =:moisEnCours");
			params.put("moisEnCours", dateAujourd.getMonth() + 1);
			params.put("anneeEnCours", dateAujourd.getYear() + 1900);

		}
		// nombre d'enregistremenrt des courrier d'aujourd'hui
		if (jourOrAutre == 15) {
			trCount.append(" AND c.courrierDateReception = :dateAjourdhuit");
			params.put("dateAjourdhuit", dateAujourd);
			// trCount.append(" AND c.courrierDateReception ='2016-07-18'");

		}

		// ---------------------------------Fin
		// Optimisation-------------------------------

		if (jourOrAutre == 1) {
			trCount.append(" AND c.courrierDateReception = :dateAjourdhuit");

			params.put("dateAjourdhuit", dateDebut);
		}
		if (jourOrAutre == 2) {
			trCount.append(" AND t.transactionDateTransaction < :dateDebut");
			params.put("dateDebut", dateDebut);
		}
		// Arrivé Départ
		// System.out.println("JS ==>categorieCourrierJour "
		// + categorieCourrierJour);
		if (categorieCourrierJour.equals("A")) {
			trCount.append(" AND t.courrierReferenceCorrespondant LIKE 'A%'");
		}

		System.out.println("JS ==>categorieCourrierJour "
				+ categorieCourrierJour);
		if (categorieCourrierJour.equals("D")) {
			trCount.append(" AND t.courrierReferenceCorrespondant LIKE 'D%'");
		}
		if (!(types != null && types.equals("boc_1"))) {
			if (!categorieCourrierJour.equals("A")
					&& !categorieCourrierJour.equals("D")) {

				System.out.println("Not A and Not D");
				int i = 0;
				String whereMax = "";
				trCount.append(" AND (");

				for (Integer id : listIdBocMembers) {
					whereMax += "t.idUtilisateur = :id" + i + " OR ";
					params.put("id" + i, id);
					i++;
				}
				int lastIndex = whereMax.lastIndexOf("OR");				
				whereMax = whereMax.substring(0, lastIndex);				
				whereMax += " ) ";
				trCount.append(whereMax);
				// StringBuffer trDest = new StringBuffer(
				// "tm.transactionId IN (SELECT tdes.idTransaction"
				// + " FROM transactiondest tdes"
				// + " WHERE tdes.transactionDestTypeIntervenant = :type))");
				// params.put("type", type);
				// trCount.append(trDest);
			}
		}
		// Fin Arrivé Départ

		// Transmission
		System.out.println("typeTransmission :" + typeTransmission);
		if (typeTransmission.equals("e-Mail")) {
			trCount.append(" AND c.idTransmission = 4");
		}
		if (typeTransmission.equals("Fax")) {
			trCount.append(" AND c.idTransmission = 3");
		}
		if (typeTransmission.equals("Autre")) {
			trCount.append(" AND c.idTransmission NOT IN (3, 4)");
		}

		// if (typeTransmission.equals("Autre") ||
		// typeTransmission.equals("Tout les courriers")
		// || typeTransmission.equals("Tous les courriers")) {
		// trCount.append(" AND c.idTransmission NOT IN (3, 4)");
		// } else {
		// Integer id = Integer.parseInt(typeTransmission);
		// trCount.append(" AND c.idTransmission = :id");
		// params.put("id", id);
		// }
		// Fin Restrictions

		// etat => A valider
		System.out.println("stateTraitement :" + stateTraitement);
		if (stateTraitement.equals("traite")) {
			trCount.append(" AND (t.etatId IN (4, 6) OR (t.etatId = 10 AND t.transactionOrdre > 1))");
		}
		if (stateTraitement.equals("nonTraite")) {
			trCount.append(" AND (t.etatId IN (2, 5) OR (t.etatId = 10 AND t.transactionOrdre > 1))");
		}

		SQLQuery query = getSession().createSQLQuery(trCount.toString());

		query.addScalar("nbrc", new LongType());
		System.out.println("Query :" + query.getQueryString());
		System.out.println("params   :" + params);
		Iterator<String> iterMax = params.keySet().iterator();
		System.out.println("Iter MAx :" + iterMax);
		while (iterMax.hasNext()) {

			String name = iterMax.next();

			System.out.println("Name :" + name);

			Object value = params.get(name);
			System.out.println("value  :" + value);

			System.out.println("==>" + params.get(name).getClass());
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
				// System.out.println("1 :" + query.getQueryString());
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
				// System.out.println("2 :" + query.getQueryString());
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
				// System.out.println("3 :" + query.getQueryString());
			} else {
				query.setParameterList(name, (List<String>) value);
				// System.out.println("4 :" + query.getQueryString());
			}
		}

		System.out.println("NB : " + query.uniqueResult());
		return (Long) query.uniqueResult();
	}

	public List<Courrier> courrierStatistiquesNecessitReponses(
			String necessiteReponse, Date dateDebut, Date dateFin,
			List<String> types, List<Integer> listIdBocMembers) {

		Map<String, Object> params = new HashMap<String, Object>();
		int i = 0;
		StringBuffer sql = new StringBuffer(
				"SELECT DISTINCT cr.*"
						+ " FROM courrier cr"
						+ " INNER JOIN courrierdossier cd ON cr.idCourrier = cd.idCourrier"
						+ " INNER JOIN dossier d ON cd.dossierId = d.dossierId"
						+ " INNER JOIN transactionn t ON d.dossierId = t.dossierId"
						+ " INNER JOIN transactiondest td ON t.transactionId = td.idTransaction"
						+ " INNER JOIN transactionn tm ON  t.transactionFirst = tm.transactionId"
						+ " AND cr.courrierNecessiteReponse = :necessiteReponse"
						+ " AND cr.courrierDateReception BETWEEN :dateDebut AND :dateFin");

		params.put("necessiteReponse", necessiteReponse);
		params.put("dateDebut", dateDebut);
		params.put("dateFin", dateFin);
		System.out.println("listIdBocMembers : " + listIdBocMembers.size());
		System.out.println("types :" + types);

		String whereMax = "";
		String typDest = "";
		sql.append(" AND (");
		for (Integer id : listIdBocMembers) {
			System.out.println("ID : " + id);
			whereMax += "t.idUtilisateur = :id" + i + " OR ";
			params.put("id" + i, id);
			i++;
		}
		sql.append(whereMax);

		String whereType = "";
		for (String type : types) {
			System.out.println("type  : " + type);
			whereType += " :type" + i + ", ";
			params.put("type" + i, type);
			i++;

		}
		typDest = whereType.substring(0, whereType.length() - 2);

		StringBuffer trDest = new StringBuffer(
				"t.transactionTypeIntervenant IN (" + typDest + ")");

		trDest.append(" OR tm.transactionId IN (SELECT tdes.idTransaction FROM transactiondest tdes WHERE tdes.transactionDestTypeIntervenant IN  ("
				+ typDest + ")))");
		sql.append(trDest);

		SQLQuery queryMax = getSession().createSQLQuery(sql.toString());
		queryMax.addEntity(Courrier.class);

		Iterator<String> iterMax = params.keySet().iterator();

		while (iterMax.hasNext()) {

			String name = iterMax.next();
			Object value = params.get(name);

			System.out.println("name" + name);
			System.out.println("value =" + value);

			if (params.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);

			} else if (params.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);

			} else if (params.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);

			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println(queryMax.getQueryString());
		return queryMax.list();
	}

	// KHA ==== rapport dont nature dynamique : cheque/lettre recommande/

	public List<CourrierInformations> recherheMulticritereCourrierEnvoyeRapport(
			boolean isResponsable, String secretaire, String type,
			String type1, String typeSecretaire, List<Integer> listAgent,
			Integer idNature, Date transactionDateDebut,
			Date transactionDateFin, boolean isBoc, String recuOrEnvoyer,
			String typeCourrierBoc, int firstIndex, int maxResult,
			String DBType, int jourOrAutre, Integer idTransmission)
			throws HibernateException {

		List<String> listRecuEnv = new ArrayList<String>();
		System.out.println("##########Dans recherheMulticritereCourrierEnvoye");
		System.out.println("isResponsable :" + isResponsable);
		System.out.println("jourOrAutre :" + jourOrAutre);
		if (isResponsable) {

			if (secretaire.equals("Oui")) {
				listRecuEnv.add(typeSecretaire);
			}
			if (listAgent != null) {
				for (int id : listAgent) {
					listRecuEnv.add("agent_" + id);
				}
			}

		}
		// Mes Courriers et les courriers de mon unité
		listRecuEnv.add(type);
		listRecuEnv.add(type1);
		System.out.println("listRecuEnv =  " + listRecuEnv.toString());
		Map<String, Object> pars = new HashMap<String, Object>();
		StringBuffer trMax = new StringBuffer(
				"SELECT MAX(tmax.transactionId) AS 'transactionId'"
						+ // , dmax.dossierId
						" FROM transactionn tmax"
						+ " INNER JOIN dossier dmax ON tmax.dossierId = dmax.dossierId"
						+ " INNER JOIN courrierdossier cdmax ON dmax.dossierId = cdmax.dossierId"
						+ " INNER JOIN courrier cmax ON cdmax.idCourrier = cmax.idCourrier"
						+ " INNER JOIN transactionn tm ON tmax.transactionFirst = tm.transactionId"
						+ " INNER JOIN courrierdonneesupplementaire cds ON cdmax.idCourrier = cds.idCourrier");

		System.out.println("==>isBoc : " + isBoc);
		System.out.println("==>typeCourrierBoc : " + typeCourrierBoc);
		if (isBoc) {
			if (typeCourrierBoc.equals("A")) {
				trMax.append(" INNER JOIN expdest emax ON tmax.idExpDest = emax.idExpDest");
			}
			if (typeCourrierBoc.equals("D")) {
				trMax.append(" INNER JOIN expdest emin ON tm.idExpDest = emin.idExpDest");
			}
		}

		if (!isBoc && listRecuEnv.size() > 0
				&& !recuOrEnvoyer.equals("envoyes")) {
			trMax.append(" LEFT JOIN transactiondest tdmax ON tdmax.idTransaction = tmax.transactionId");
		}
		trMax.append(" WHERE dmax.typeDossierId = 1 AND tmax.etatId != 99");

		int i = 0;
		String expDest = "";
		String whereMax = "";

		if (isBoc) {
			// Arrivé Départ
			if (typeCourrierBoc.equals("A")) {
				trMax.append(" AND emax.typeExpDest LIKE 'Externe'");
			}
			if (typeCourrierBoc.equals("D")) {
				trMax.append(" AND emin.typeExpDest LIKE 'Interne-%'");
			}
		} else if (listRecuEnv.size() > 0) {
			for (String ed : listRecuEnv) {
				expDest += ":ed" + i + ", ";
				pars.put("ed" + i, ed);
				i++;
			}
			expDest = expDest.substring(0, expDest.length() - 2);
			if (recuOrEnvoyer.equals("recus")) {

				whereMax += " AND tdmax.transactionDestTypeIntervenant IN ("
						+ expDest + ")";
			} else if (recuOrEnvoyer.equals("envoyes")) {
				whereMax += " AND tmax.transactionTypeIntervenant IN ("
						+ expDest + ")";
			} else if (recuOrEnvoyer.equals("tous") || recuOrEnvoyer.equals("")) {
				whereMax += " AND (tmax.transactionTypeIntervenant IN ("
						+ expDest + ")"
						+ " OR tdmax.transactionDestTypeIntervenant IN ("
						+ expDest + "))";
			}
		}
		trMax.append(whereMax);
		if (idNature != null) {
			System.out.println("KHA : idNature = " + idNature);
			trMax.append(" AND cmax.idNature = :idNature");
			pars.put("idNature", idNature);

		}
		if (idTransmission != null) {
			System.out.println("KHA : idTransmission = " + idTransmission);
			trMax.append(" AND cmax.idTransmission = :idTransmission");
			pars.put("idTransmission", idTransmission);

		}
		// // if (jourOrAutre == 1992) {
		// System.out.println("###### dans jourOrAutre == 1992");
		// if (idNature != null) {
		// if (idNature == 6 || idNature ==5 ){
		// System.out.println("###### dans test rapide poste et lettre recommandée");
		// System.out.println("KHA : idNature = " + idNature);
		// trMax.append(" AND cmax.idTransmission = :idNature");
		// pars.put("idNature", idNature);
		// }
		// }
		// if (idNature != null) {
		// if (idNature == 38 || idNature == 59 || idNature == 80){
		// System.out.println("###### dans test chèque");
		// System.out.println("KHA : idNature = " + idNature);
		// trMax.append(" AND cmax.idNature = :idNature");
		// pars.put("idNature", idNature);
		// }
		// }
		// if (idNature != null) {
		// if (idNature == 66){
		// System.out.println("###### dans test reclamation");
		// System.out.println("KHA : idNature = " + idNature);
		// trMax.append(" AND cmax.idNature = :idNature");
		// pars.put("idNature", idNature);
		// }
		// }
		// }

		// Calendar calendar = Calendar.getInstance();
		// Date dateAujourd = calendar.getTime();
		// par année
		if (jourOrAutre == 3) {
			System.out.println(" KHA : par anneee");
			trMax.append(" AND cmax.courrierDateReceptionAnnee =:dateDebut");
			pars.put("dateDebut", transactionDateDebut.getYear() + 1900);
		}
		// par moi
		if (jourOrAutre == 4) {
			System.out
					.println(" ###### dateAujourd == " + transactionDateDebut);
			System.out.println(" KHA : par mois");
			System.out.println(" ###### moisEnCours");
			trMax.append(" AND cmax.courrierDateReceptionAnnee =:anneeEnCours AND cmax.courrierDateReceptionMois =:moisEnCours");
			pars.put("moisEnCours", transactionDateDebut.getMonth() + 1);
			pars.put("anneeEnCours", transactionDateDebut.getYear() + 1900);
		}
		// par jour
		if (jourOrAutre == 5) {

			SimpleDateFormat debut = new SimpleDateFormat("yyyy-MM-dd");
			String dateD = debut.format(transactionDateDebut);

			System.out.println(" KHA : par jour");
			trMax.append(" AND DATE_FORMAT(cmax.courrierDateReception,'%Y-%m-%d') = :dateDebut");
			System.out.println("transactionDateDebut = " + dateD);
			pars.put("dateDebut", dateD);
		}
		// periode
		if (jourOrAutre == 1) {
			System.out.println(" KHA : par periode");
			trMax.append(" AND DATE_FORMAT(cmax.courrierDateReception,'%Y-%m-%d') BETWEEN :dateDebut"
					+ " AND :dateFin");
			pars.put("dateDebut", transactionDateDebut);
			pars.put("dateFin", transactionDateFin);

		}

		if (jourOrAutre == 2) {
			System.out.println(" KHA : par periode");
			trMax.append(" AND DATE_FORMAT(cmax.courrierDateReception,'%Y-%m-%d') BETWEEN :dateDebut"
					+ " AND :dateFin");
			pars.put("dateDebut", transactionDateDebut);
			pars.put("dateFin", transactionDateFin);

		}

		trMax.append(" GROUP BY dmax.dossierId"
				+ " ORDER BY dmax.dossierId DESC");

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		Iterator<String> iterMax = pars.keySet().iterator();
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}
		if (DBType.contains("sqlserver")) {
			queryMax.setFirstResult(firstIndex);
			queryMax.setMaxResults(maxResult);
		}

		System.out.println(queryMax.getQueryString());
		List<Integer> maxIds = queryMax.list();

		Map<String, Object> params = new HashMap<String, Object>();
		int imax = 0;
		String where = " t.transactionId IN (";
		// String whereD = " AND tm1.dossierId IN (";
		if (maxIds.size() > 0) {
			for (Integer ids : maxIds) {
				Integer idTrans = ids;
				where += ":idTrans" + imax + ", ";
				params.put("idTrans" + imax, idTrans);
				imax++;
			}
		} else {
			return new ArrayList<CourrierInformations>();
		}

		StringBuffer hql = new StringBuffer(
				"SELECT DISTINCT c.idCourrier AS 'courrierID',"
						+ " c.courrierObjet AS 'courrierObjet',"
						+ " c.courrierReferenceCorrespondant AS 'courrierReference',"
						+ " c.courrierCommentaire AS 'courrierCommentaire',"
						+ " c.courrierOldNum AS 'courrierOldNum',"
						+ " c.courrierCircuit AS 'courrierCircuit',"
						+ " d.dossierId AS 'dossierID',"
						+ " n.natureLibelle AS 'courrierNature',"
						+ " t.etatId AS 'etatID',"
						+ " c.courrierDateReception AS 'courrierDateReceptionEnvoi',"
						+ " t.transactionDateConsultation AS 'transactionDateConsultation',"
						+ " t.transactionId AS 'transactionID',"
						+ " t.idExpDest AS 'expID',"
						+ " t.transactionOrdre AS 'transactionOrdre',"
						+ " t.idUtilisateur AS 'idUtilisateur',"
						+ " t_tdr.transactionDestinationReelleIdDestinataire AS 'transactionDestinationReelID',"
						+ " t_tdr.transactionDestinationReelleTypeDestinataire AS 'transactionDestinationReelleTypeDestinataire',"
						+ " e.typeExpDest AS 'expTypeOld',"
						+ " e.idExpDestLdap AS 'expLdapOld',"
						+ " ee.Exp_Dest_ExterneNom AS 'expNomOld',"
						+ " ee.Exp_Dest_ExternePrenom AS 'expPrenomOld',"
						+ " ee.typeUtilisateurId AS 'expTypeUserOld',"
						+ " tm.transactionId AS 'minTransactionID',"
						+ " em.typeExpDest AS 'expType',"
						+ " em.idExpDestLdap AS 'expLdap',"
						+ " eem.Exp_Dest_ExterneNom AS 'expNom',"
						+ " eem.Exp_Dest_ExternePrenom AS 'expPrenom',"
						+ " eem.typeUtilisateurId AS 'expTypeUser',"
						+ " td.transactionDestDateTransfert AS 'dateReponseDest',");
		if (DBType.contains("sqlserver")) {
			hql.append(" STUFF((SELECT '|' + CONVERT(NVARCHAR(10), t3.transactionId)"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), e3.idExpDest), '')"
					+ " + ';' + ISNULL(e3.typeExpDest, '')"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), e3.idExpDestLdap), '')"
					+ " + ';' + ISNULL(ee3.Exp_Dest_ExterneNom, '')"
					+ " + ';' + ISNULL(ee3.Exp_Dest_ExternePrenom, '')"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), ee3.typeUtilisateurId), '')"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), tdr.transactionDestinationReelleIdDestinataire), '')"
					+ " + ';' + ISNULL(tdr.transactionDestinationReelleTypeDestinataire, '')"
					+ " FROM transactionn t3 "
					+ " LEFT JOIN transactiondest td3 ON t3.transactionId = td3.idTransaction"
					+ " LEFT JOIN transactiondestinationreelle tdr ON t3.transactionDestinationReelleId = tdr.transactionDestinationReelleId"
					+ " LEFT JOIN expdest e3 ON td3.idExpDest = e3.idExpDest"
					+ " LEFT JOIN expdestexterne ee3 ON e3.idExpDestExterne = ee3.idExpDestExterne"
					+ " WHERE t3.dossierId = t.dossierId"
					+ " FOR XML PATH('')), 1, 1, '') AS 'destReelList'");
		}
		if (DBType.contains("mysql")) {
			hql.append("(SELECT GROUP_CONCAT(CONCAT(CONVERT(t3.transactionId, CHAR(10))"
					+ " , ';' , IFNULL(CONVERT(e3.idExpDest, CHAR(10)), '')"
					+ " , ';' , IFNULL(e3.typeExpDest, '')"
					+ " , ';' , IFNULL(CONVERT(e3.idExpDestLdap, CHAR(10)), '')"
					+ " , ';' , IFNULL(ee3.Exp_Dest_ExterneNom, '')"
					+ " , ';' , IFNULL(ee3.Exp_Dest_ExternePrenom, '')"
					+ " , ';' , IFNULL(CONVERT(ee3.typeUtilisateurId, CHAR(10)), '')"
					+ " , ';' , IFNULL(CONVERT(tdr.transactionDestinationReelleIdDestinataire, CHAR(10)), '')"
					+ " , ';' , IFNULL(tdr.transactionDestinationReelleTypeDestinataire, '')) SEPARATOR '|')"
					+ " FROM transactionn t3 "
					+ " LEFT JOIN transactiondest td3 ON t3.transactionId = td3.idTransaction"
					+ " LEFT JOIN transactiondestinationreelle tdr ON t3.transactionDestinationReelleId = tdr.transactionDestinationReelleId"
					+ " LEFT JOIN expdest e3 ON td3.idExpDest = e3.idExpDest"
					+ " LEFT JOIN expdestexterne ee3 ON e3.idExpDestExterne = ee3.idExpDestExterne"
					+ " WHERE t3.dossierId = t.dossierId) AS 'destReelList'");
		}
		hql.append(" FROM transactionn t"
				+ " INNER JOIN dossier d ON t.dossierId = d.dossierId"
				+ " INNER JOIN courrierdossier cd ON d.dossierId = cd.dossierId"
				+ " INNER JOIN courrier c ON cd.idCourrier = c.idCourrier"
				+ " INNER JOIN nature n ON c.idNature = n.natureId"
				+ " LEFT JOIN transactionannotation ta ON t.transactionId = ta.idTransaction"
				+ " LEFT JOIN transactiondest td ON t.transactionId = td.idTransaction"
				+ " LEFT JOIN expdest e ON t.idExpDest = e.idExpDest"
				+ " LEFT JOIN expdestexterne ee ON e.idExpDestExterne = ee.idExpDestExterne"
				+ " LEFT JOIN transactiondestinationreelle t_tdr ON t.transactionDestinationReelleId = t_tdr.transactionDestinationReelleId"
				+ " INNER JOIN transactionn tm ON t.transactionFirst = tm.transactionId"
				+ " LEFT JOIN expdest em ON tm.idExpDest = em.idExpDest"
				+ " LEFT JOIN expdestexterne eem ON em.idExpDestExterne = eem.idExpDestExterne"
				+ " WHERE " + where.substring(0, where.length() - 2) + ")"
				+ " ORDER BY c.idCourrier DESC");
		// Fin Restrictions

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		query.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));

		query.addScalar("courrierID", new IntegerType());
		query.addScalar("courrierObjet", new StringType());
		query.addScalar("courrierReference", new StringType());
		query.addScalar("courrierCommentaire", new StringType());
		query.addScalar("courrierOldNum", new IntegerType());
		query.addScalar("courrierCircuit", new StringType());
		query.addScalar("dossierID", new IntegerType());
		query.addScalar("courrierNature", new StringType());
		query.addScalar("etatID", new IntegerType());
		query.addScalar("courrierDateReceptionEnvoi", new DateType());
		query.addScalar("transactionDateConsultation", new DateType());
		query.addScalar("transactionID", new IntegerType());
		query.addScalar("expID", new IntegerType());
		query.addScalar("transactionOrdre", new IntegerType());
		query.addScalar("idUtilisateur", new IntegerType());
		query.addScalar("transactionDestinationReelID", new IntegerType());
		query.addScalar("transactionDestinationReelleTypeDestinataire",
				new StringType());
		query.addScalar("expLdapOld", new IntegerType());
		query.addScalar("expTypeOld", new StringType());
		query.addScalar("expNomOld", new StringType());
		query.addScalar("expPrenomOld", new StringType());
		query.addScalar("expTypeUserOld", new IntegerType());
		query.addScalar("minTransactionID", new IntegerType());
		query.addScalar("expLdap", new IntegerType());
		query.addScalar("expType", new StringType());
		query.addScalar("expNom", new StringType());
		query.addScalar("expPrenom", new StringType());
		query.addScalar("expTypeUser", new IntegerType());
		query.addScalar("destReelList", new StringType());
		// kha
		query.addScalar("dateReponseDest", new DateType());

		Iterator<String> iter = params.keySet().iterator();
		while (iter.hasNext()) {
			String name = iter.next();
			Object value = params.get(name);
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}

		System.out
				.println("++++++++++++++++++++++++++++++++////////////////////////////////+++++++++++++++++++++++++++++++++++"
						+ query.getQueryString());
		return query.list();

	}

	// [JS] :get LastIndex Courrier et Transaction

	@Override
	public Integer getTransactionLastIdByTypeOrdreAndAnnees(String type,
			Integer annee) throws HibernateException {
		Map<String, Object> pars = new HashMap<String, Object>();

		StringBuffer hql = new StringBuffer(
				"SELECT MAX(t.courrierTypeOrdre) 'courrierTypeOrdre'"
						+ " FROM transactionn t ,expdest e "
						+ " WHERE t.idExpDest=e.idExpDest"
						+ " AND t.courrierType = :type AND t.courrierDateReceptionAnnee = :annee ");
		pars.put("type", type);
		pars.put("annee", annee);

		// for (java.util.Map.Entry<Integer, Integer> filter :
		// mapIds.entrySet()) {
		//
		// if (filter.getKey() == 1) {
		// System.out.println("[JS]=>Exp Dest Externe");
		// System.out.println("KEY : " + filter.getKey());
		// System.out.println("VALUE : " + filter.getValue());
		// hql.append(" AND e.idExpDestExterne = :idExpDestExterne ");
		// pars.put("idExpDestExterne ", filter.getValue());
		// }
		//
		// if (filter.getKey() == 2) {
		//
		// System.out.println("[JS]=>Exp Dest LDAP");
		// System.out.println("KEY : " + filter.getKey());
		// System.out.println("VALUE : " + filter.getValue());
		// hql.append(" AND e.idExpdestLdap = :idExpdestLdap ");
		// pars.put("idExpdestLdap", filter.getValue());
		//
		// }
		// }
		SQLQuery query = getSession().createSQLQuery(hql.toString());

		Iterator<String> iterMax = pars.keySet().iterator();

		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			System.out.println("-------------------");
			System.out.println("Name : " + name);
			System.out.println("Valeur : " + value);
			System.out.println("-------------------");

			if (pars.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}
		System.out.println(query.getQueryString());
		return (Integer) query.uniqueResult();
	}

	@Override
	public Integer getCourrierLastIdByTypeOrdreAndAnnees(String type,
			Integer annee) throws HibernateException {

		Map<String, Object> pars = new HashMap<String, Object>();

		StringBuffer hql = new StringBuffer(
				"SELECT MAX(c.courrierTypeOrdre) 'courrierTypeOrdre'"
						+ " FROM courrier c,courrierdossier cd,dossier d,transactionn t,expdest e"
						+ " WHERE c.idCourrier=cd.idCourrier AND cd.dossierId=d.dossierId "
						+ " AND d.dossierId=t.dossierId AND t.idExpDest=e.idExpDest"
						+ " AND c.courrierType = :type AND c.courrierDateReceptionAnnee = :annee ");

		pars.put("type", type);
		pars.put("annee", annee);

		// for (java.util.Map.Entry<Integer, Integer> filter :
		// mapIds.entrySet()) {
		//
		// if (filter.getKey() == 1) {
		// System.out.println("[JS]=>Exp Dest Externe");
		// System.out.println("KEY : " + filter.getKey());
		// System.out.println("VALUE : " + filter.getValue());
		// hql.append(" AND e.idExpDestExterne = :idExpDestExterne ");
		// pars.put("idExpDestExterne ", filter.getValue());
		// }
		//
		// if (filter.getKey() == 2) {
		//
		// System.out.println("[JS]=>Exp Dest LDAP");
		// System.out.println("KEY : " + filter.getKey());
		// System.out.println("VALUE : " + filter.getValue());
		// hql.append(" AND e.idExpdestLdap = :idExpdestLdap ");
		// pars.put("idExpdestLdap", filter.getValue());
		//
		// }
		// }

		System.out.println(hql.toString());

		SQLQuery queryMax = getSession().createSQLQuery(hql.toString());
		Iterator<String> iterMax = pars.keySet().iterator();

		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			System.out.println("Name : " + name);
			System.out.println("Valeur : " + value);

			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println(queryMax.getQueryString());
		return (Integer) queryMax.uniqueResult();
		// SQLQuery query = getSession().createSQLQuery(hql.toString());
		//
		// query.setString("type", type);
		// query.setInteger("annee", annee);

		// System.out.println(query.getQueryString());
		// return (Integer) query.uniqueResult();
	}

	// [JS][2019-06-14]: Nombre Référence Courrier (A/D) par Bureau d'ordre

	@Override
	public Integer getLastIDTransactionByTypeOrdreAndAnneeAndBO(int idDossier,String type,
			Integer annee, String boc, List<Integer> listIdBocMembers) {

		Map<String, Object> pars = new HashMap<String, Object>();

		StringBuffer hql = new StringBuffer(
				"SELECT MAX(t.courrierTypeOrdre) 'courrierTypeOrdre'"
						+ " FROM transactionn t, dossier d, courrierdossier cd, courrier c, transactionn tm"
						+ " WHERE t.dossierId = d.dossierId"
						+ " AND d.dossierId = cd.dossierId"
						+ " AND d.dossierId != "+idDossier
						+ " AND cd.idCourrier = c.idCourrier"
						+ " AND t.transactionFirst = tm.transactionId"
						+ " AND t.courrierDateReceptionAnnee= :annee ");

		pars.put("annee", annee);

		// Arrivé Départ
		System.out.println("JS ==>Type Courrier (1)  :" + type);
		if (type.equals("A")) {
			hql.append(" AND t.courrierReferenceCorrespondant LIKE 'A%'");
		}

		System.out.println("JS ==>Type Courrier (2) :" + type);
		if (type.equals("D")) {
			hql.append(" AND t.courrierReferenceCorrespondant LIKE 'D%'");
		}
		//
		// System.out.println("JS ==> list Id BOc Membres :"
		// + listIdBocMembers.size());
		int i = 0;
		String whereMax = "";
		hql.append(" AND (");
		for (Integer id : listIdBocMembers) {
			System.out.println("ID ;" + id);
			whereMax += " t.idUtilisateur = :id" + i + " OR ";
			pars.put("id" + i, id);
			i++;
		}
		hql.append(whereMax);
		StringBuffer trDest = new StringBuffer(
				"tm.transactionId IN (SELECT tdes.idTransaction"
						+ " FROM transactiondest tdes"
						+ " WHERE tdes.transactionDestTypeIntervenant = :boc))");
		pars.put("boc", boc);
		hql.append(trDest);

		SQLQuery query = getSession().createSQLQuery(hql.toString());

		Iterator<String> iterMax = pars.keySet().iterator();
		System.out.println("Iter MAx :" + iterMax);
		while (iterMax.hasNext()) {

			String name = iterMax.next();
			System.out.println("Name :" + name);

			Object value = pars.get(name);
			System.out.println("value  :" + value);

			if (pars.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
				// System.out.println("1 :" + query.getQueryString());
			} else if (pars.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
				// System.out.println("2 :" + query.getQueryString());
			} else if (pars.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
				// System.out.println("3 :" + query.getQueryString());
			} else {
				query.setParameterList(name, (List<String>) value);
				// System.out.println("4 :" + query.getQueryString());
			}
		}

		System.out.println(query.getQueryString());
		System.out.println("NB : " + query.uniqueResult());
		return (Integer) query.uniqueResult();
	}

	// [JS][2019-06-14]: Nombre Total Référence (I) Courrier par Bureau d'ordre

	@Override
	public Integer getLastIDTransactionByTypeOrdreAndAnnee(String type,
			Integer annee) throws HibernateException {
		StringBuffer hql = new StringBuffer(
				"SELECT MAX(t.courrierTypeOrdre) 'courrierTypeOrdre'"
						+ " FROM transactionn t"
						+ " WHERE t.courrierType LIKE :type AND t.courrierDateReceptionAnnee = :annee ");

		SQLQuery query = getSession().createSQLQuery(hql.toString());

		query.setString("type", '%' + type + '%');
		query.setInteger("annee", annee);

		System.out.println("==================> Query Calcul Last ID : "
				+ query.getQueryString());
		return (Integer) query.uniqueResult();
	}

	@Override
	public Integer getLastIDTransactionByTypeOrdreAndAnnee(String type,
			Integer annee, int mois) throws HibernateException {
		StringBuffer hql = new StringBuffer(
				"SELECT MAX(t.courrierTypeOrdre) 'courrierTypeOrdre'"
						+ " FROM courrier t"
						+ " WHERE t.courrierType LIKE :type AND t.courrierDateReceptionAnnee = :annee AND t.courrierDateReceptionMois = :mois " +
								" AND t.courrierSupprime =1 AND t.courrierSupprimeDate is NULL");

		SQLQuery query = getSession().createSQLQuery(hql.toString());

		query.setString("type", '%' + type + '%');
		query.setInteger("annee", annee);
		query.setInteger("mois", mois);

		System.out.println("==================> Query Calcul Last ID : "
				+ query.getQueryString());
		return (Integer) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<CourrierInformations> findCourrierEnvoyerBOCWithCriterionAO(
			int idaoConsultation, boolean descending, int firstIndex,
			int maxResult, String DBType) {
		Map<String, Object> pars = new HashMap<String, Object>();
		StringBuffer trMax = new StringBuffer(
				"SELECT MAX(tmax.transactionId) AS 'transactionId'"
						+ " FROM transactionn tmax, dossier dmax, courrierdossier cdmax, courrier cmax, transactionn tm"
						+ " WHERE cmax.idaoConsultation = " + idaoConsultation
						+ " AND tmax.dossierId = dmax.dossierId"
						+ " AND dmax.dossierId = cdmax.dossierId"
						+ " AND cdmax.idCourrier = cmax.idCourrier"
						+ " AND tmax.transactionFirst = tm.transactionId");

		trMax.append(" GROUP BY dmax.dossierId"
				+ " ORDER BY dmax.dossierId DESC");
		if (maxResult != 0 && DBType.contains("mysql")) {
			trMax.append(" LIMIT " + firstIndex + ", " + maxResult);
		}

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		System.out.println("queryMax :" + queryMax);
		Iterator<String> iterMax = pars.keySet().iterator();
		System.out.println("iterMax ==>" + iterMax);
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		if (maxResult != 0 && DBType.contains("sqlserver")) {
			queryMax.setFirstResult(firstIndex);
			queryMax.setMaxResults(maxResult);
		}

		System.out.println("queryMax.getQueryString() : "
				+ queryMax.getQueryString());
		System.out.println("queryMax.list  :" + queryMax.list());
		List<Integer> maxIds = queryMax.list();

		Map<String, Object> params = new HashMap<String, Object>();
		int imax = 0;
		String where = " t.transactionId IN (";
		if (maxIds.size() > 0) {
			for (Integer ids : maxIds) {
				Integer idTrans = ids;
				where += ":idTrans" + imax + ", ";
				params.put("idTrans" + imax, idTrans);
				imax++;
			}
		} else {
			return new ArrayList<CourrierInformations>();
		}
		System.out.println("Liste :");
		StringBuffer hql = new StringBuffer(
				"SELECT DISTINCT c.idCourrier AS 'courrierID',"
						+ " c.courrierObjet AS 'courrierObjet',"
						+ " c.courrierReferenceCorrespondant AS 'courrierReference',"
						+ " c.courrierCommentaire AS 'courrierCommentaire',"
						+ " c.courrierOldNum AS 'courrierOldNum',"
						+ " c.courrierCircuit AS 'courrierCircuit',"
						+ " d.dossierId AS 'dossierID',"
						+ " n.natureLibelle AS 'courrierNature',"
						+ " t.etatId AS 'etatID',"
						+ " c.courrierDateReception AS 'courrierDateReceptionEnvoi',"
						+ " t.transactionDateConsultation AS 'transactionDateConsultation',"
						+ " t.transactionId AS 'transactionID',"
						+ " t.idExpDest AS 'expID',"
						+ " t.transactionOrdre AS 'transactionOrdre',"
						+ " t.idUtilisateur AS 'idUtilisateur',"
						+ " t_tdr.transactionDestinationReelleIdDestinataire AS 'transactionDestinationReelID',"
						+ " t_tdr.transactionDestinationReelleTypeDestinataire AS 'transactionDestinationReelleTypeDestinataire',"
						+ " e.typeExpDest AS 'expTypeOld',"
						+ " e.idExpDestLdap AS 'expLdapOld',"
						+ " ee.Exp_Dest_ExterneNom AS 'expNomOld',"
						+ " ee.Exp_Dest_ExternePrenom AS 'expPrenomOld',"
						+ " ee.typeUtilisateurId AS 'expTypeUserOld',"
						+ " tm.transactionId AS 'minTransactionID',"
						+ " em.typeExpDest AS 'expType',"
						+ " em.idExpDestLdap AS 'expLdap',"
						+ " eem.Exp_Dest_ExterneNom AS 'expNom',"
						+ " eem.Exp_Dest_ExternePrenom AS 'expPrenom',"
						+ " eem.typeUtilisateurId AS 'expTypeUser' ");

		
		System.out.println(hql.toString());
		hql.append(" FROM transactionn t"
				+ " INNER JOIN dossier d ON t.dossierId = d.dossierId"
				+ " INNER JOIN courrierdossier cd ON d.dossierId = cd.dossierId"
				+ " INNER JOIN courrier c ON cd.idCourrier = c.idCourrier"
				+ " INNER JOIN nature n ON c.idNature = n.natureId");

		hql.append(" LEFT JOIN transactiondest td ON t.transactionId = td.idTransaction"
				+ " LEFT JOIN expdest e ON t.idExpDest = e.idExpDest"
				+ " LEFT JOIN expdestexterne ee ON e.idExpDestExterne = ee.idExpDestExterne"
				+ " LEFT JOIN transactiondestinationreelle t_tdr ON t.transactionDestinationReelleId = t_tdr.transactionDestinationReelleId"
				+ " INNER JOIN transactionn tm ON t.transactionFirst = tm.transactionId"
				+ " LEFT JOIN expdest em ON tm.idExpDest = em.idExpDest"
				+ " LEFT JOIN expdestexterne eem ON em.idExpDestExterne = eem.idExpDestExterne"
				+ " WHERE d.typeDossierId = 1 AND "
				+ where.substring(0, where.length() - 2) + ")");

		// Order by Date
		hql.append(" ORDER BY c.idCourrier DESC");

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		query.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));

		query.addScalar("courrierID", new IntegerType());
		query.addScalar("courrierObjet", new StringType());
		query.addScalar("courrierReference", new StringType());
		query.addScalar("courrierCommentaire", new StringType());
		query.addScalar("courrierOldNum", new IntegerType());
		query.addScalar("courrierCircuit", new StringType());
		query.addScalar("dossierID", new IntegerType());
		query.addScalar("courrierNature", new StringType());
		query.addScalar("etatID", new IntegerType());
		query.addScalar("courrierDateReceptionEnvoi", new DateType());
		query.addScalar("transactionDateConsultation", new DateType());
		query.addScalar("transactionID", new IntegerType());
		query.addScalar("expID", new IntegerType());
		query.addScalar("transactionOrdre", new IntegerType());
		query.addScalar("idUtilisateur", new IntegerType());
		query.addScalar("transactionDestinationReelID", new IntegerType());
		query.addScalar("transactionDestinationReelleTypeDestinataire",
				new StringType());
		query.addScalar("expLdapOld", new IntegerType());
		query.addScalar("expTypeOld", new StringType());
		query.addScalar("expNomOld", new StringType());
		query.addScalar("expPrenomOld", new StringType());
		query.addScalar("expTypeUserOld", new IntegerType());
		query.addScalar("minTransactionID", new IntegerType());
		query.addScalar("expLdap", new IntegerType());
		query.addScalar("expType", new StringType());
		query.addScalar("expNom", new StringType());
		query.addScalar("expPrenom", new StringType());
		query.addScalar("expTypeUser", new IntegerType());

		Iterator<String> iter = params.keySet().iterator();
		while (iter.hasNext()) {
			System.out.println("AH  dans itr ");
			String name = iter.next();
			Object value = params.get(name);
			System.out.println(name + " = " + value);
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println(query.getQueryString());
		System.out.println("list :" + query.list());
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<CourrierInformations> findCourrierEnvoyerBOCWithCriterionValise(
			boolean descending, int firstIndex, int maxResult, String DBType) {
		Map<String, Object> pars = new HashMap<String, Object>();
		StringBuffer trMax = new StringBuffer(
				"SELECT MAX(tmax.transactionId) AS 'transactionId'"
						+ " FROM transactionn tmax, dossier dmax, courrierdossier cdmax, courrier cmax, transactionn tm"
						+ " where tmax.dossierId = dmax.dossierId"
						+ " AND cmax.courrierFormat = 'valise'"
						+ " AND dmax.dossierId = cdmax.dossierId"
						+ " AND cdmax.idCourrier = cmax.idCourrier"
						+ " AND tmax.transactionFirst = tm.transactionId");

		trMax.append(" GROUP BY dmax.dossierId"
				+ " ORDER BY dmax.dossierId DESC");
		if (maxResult != 0 && DBType.contains("mysql")) {
			trMax.append(" LIMIT " + firstIndex + ", " + maxResult);
		}

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		System.out.println("queryMax :" + queryMax);
		Iterator<String> iterMax = pars.keySet().iterator();
		System.out.println("iterMax ==>" + iterMax);
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		if (maxResult != 0 && DBType.contains("sqlserver")) {
			queryMax.setFirstResult(firstIndex);
			queryMax.setMaxResults(maxResult);
		}

		System.out.println("queryMax.getQueryString() : "
				+ queryMax.getQueryString());
		System.out.println("queryMax.list  :" + queryMax.list());
		List<Integer> maxIds = queryMax.list();

		Map<String, Object> params = new HashMap<String, Object>();
		int imax = 0;
		String where = " t.transactionId IN (";
		if (maxIds.size() > 0) {
			for (Integer ids : maxIds) {
				Integer idTrans = ids;
				where += ":idTrans" + imax + ", ";
				params.put("idTrans" + imax, idTrans);
				imax++;
			}
		} else {
			return new ArrayList<CourrierInformations>();
		}
		System.out.println("Liste :");
		StringBuffer hql = new StringBuffer(
				"SELECT DISTINCT c.idCourrier AS 'courrierID',"
						+ " c.courrierObjet AS 'courrierObjet',"
						+ " c.courrierReferenceCorrespondant AS 'courrierReference',"
						+ " c.courrierCommentaire AS 'courrierCommentaire',"
						+ " c.courrierOldNum AS 'courrierOldNum',"
						+ " c.courrierCircuit AS 'courrierCircuit',"
						+ " d.dossierId AS 'dossierID',"
						+ " n.natureLibelle AS 'courrierNature',"
						+ " t.etatId AS 'etatID',"
						+ " c.courrierDateReception AS 'courrierDateReceptionEnvoi',"
						+ " t.transactionDateConsultation AS 'transactionDateConsultation',"
						+ " t.transactionId AS 'transactionID',"
						+ " t.idExpDest AS 'expID',"
						+ " t.transactionOrdre AS 'transactionOrdre',"
						+ " t.idUtilisateur AS 'idUtilisateur',"
						+ " t_tdr.transactionDestinationReelleIdDestinataire AS 'transactionDestinationReelID',"
						+ " t_tdr.transactionDestinationReelleTypeDestinataire AS 'transactionDestinationReelleTypeDestinataire',"
						+ " e.typeExpDest AS 'expTypeOld',"
						+ " e.idExpDestLdap AS 'expLdapOld',"
						+ " ee.Exp_Dest_ExterneNom AS 'expNomOld',"
						+ " ee.Exp_Dest_ExternePrenom AS 'expPrenomOld',"
						+ " ee.typeUtilisateurId AS 'expTypeUserOld',"
						+ " tm.transactionId AS 'minTransactionID',"
						+ " em.typeExpDest AS 'expType',"
						+ " em.idExpDestLdap AS 'expLdap',"
						+ " eem.Exp_Dest_ExterneNom AS 'expNom',"
						+ " eem.Exp_Dest_ExternePrenom AS 'expPrenom',"
						+ " eem.typeUtilisateurId AS 'expTypeUser' ");

		
		System.out.println(hql.toString());
		hql.append(" FROM transactionn t"
				+ " INNER JOIN dossier d ON t.dossierId = d.dossierId"
				+ " INNER JOIN courrierdossier cd ON d.dossierId = cd.dossierId"
				+ " INNER JOIN courrier c ON cd.idCourrier = c.idCourrier"
				+ " INNER JOIN nature n ON c.idNature = n.natureId");

		hql.append(" LEFT JOIN transactiondest td ON t.transactionId = td.idTransaction"
				+ " LEFT JOIN expdest e ON t.idExpDest = e.idExpDest"
				+ " LEFT JOIN expdestexterne ee ON e.idExpDestExterne = ee.idExpDestExterne"
				+ " LEFT JOIN transactiondestinationreelle t_tdr ON t.transactionDestinationReelleId = t_tdr.transactionDestinationReelleId"
				+ " INNER JOIN transactionn tm ON t.transactionFirst = tm.transactionId"
				+ " LEFT JOIN expdest em ON tm.idExpDest = em.idExpDest"
				+ " LEFT JOIN expdestexterne eem ON em.idExpDestExterne = eem.idExpDestExterne"
				+ " WHERE d.typeDossierId = 1 AND "
				+ where.substring(0, where.length() - 2) + ")");

		// Order by Date
		hql.append(" ORDER BY c.idCourrier DESC");

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		query.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));

		query.addScalar("courrierID", new IntegerType());
		query.addScalar("courrierObjet", new StringType());
		query.addScalar("courrierReference", new StringType());
		query.addScalar("courrierCommentaire", new StringType());
		query.addScalar("courrierOldNum", new IntegerType());
		query.addScalar("courrierCircuit", new StringType());
		query.addScalar("dossierID", new IntegerType());
		query.addScalar("courrierNature", new StringType());
		query.addScalar("etatID", new IntegerType());
		query.addScalar("courrierDateReceptionEnvoi", new DateType());
		query.addScalar("transactionDateConsultation", new DateType());
		query.addScalar("transactionID", new IntegerType());
		query.addScalar("expID", new IntegerType());
		query.addScalar("transactionOrdre", new IntegerType());
		query.addScalar("idUtilisateur", new IntegerType());
		query.addScalar("transactionDestinationReelID", new IntegerType());
		query.addScalar("transactionDestinationReelleTypeDestinataire",
				new StringType());
		query.addScalar("expLdapOld", new IntegerType());
		query.addScalar("expTypeOld", new StringType());
		query.addScalar("expNomOld", new StringType());
		query.addScalar("expPrenomOld", new StringType());
		query.addScalar("expTypeUserOld", new IntegerType());
		query.addScalar("minTransactionID", new IntegerType());
		query.addScalar("expLdap", new IntegerType());
		query.addScalar("expType", new StringType());
		query.addScalar("expNom", new StringType());
		query.addScalar("expPrenom", new StringType());
		query.addScalar("expTypeUser", new IntegerType());

		Iterator<String> iter = params.keySet().iterator();
		while (iter.hasNext()) {
			System.out.println("AH  dans itr ");
			String name = iter.next();
			Object value = params.get(name);
			System.out.println(name + " = " + value);
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println(query.getQueryString());
		System.out.println("list :" + query.list());
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<CourrierInformations> findCourrierEnvoyerBOCWithCriterionValiseFormat(
			int courrierValiseID, boolean descending, int firstIndex,
			int maxResult, String DBType) {
		Map<String, Object> pars = new HashMap<String, Object>();
		StringBuffer trMax = new StringBuffer(
				"SELECT MAX(tmax.transactionId) AS 'transactionId'"
						+ " FROM transactionn tmax, dossier dmax, courrierdossier cdmax, courrier cmax, transactionn tm"
						+ " WHERE cmax.idcourrierFK = " + courrierValiseID
						+ " AND cmax.courrierFormat != 'valise'"
						+ " AND cmax.courrierDatePointage is null "
						+ " AND tmax.dossierId = dmax.dossierId"
						+ " AND dmax.dossierId = cdmax.dossierId"
						+ " AND cdmax.idCourrier = cmax.idCourrier"
						+ " AND tmax.transactionFirst = tm.transactionId");

		trMax.append(" GROUP BY dmax.dossierId"
				+ " ORDER BY dmax.dossierId DESC");
		if (maxResult != 0 && DBType.contains("mysql")) {
			trMax.append(" LIMIT " + firstIndex + ", " + maxResult);
		}

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		System.out.println("queryMax :" + queryMax);
		Iterator<String> iterMax = pars.keySet().iterator();
		System.out.println("iterMax ==>" + iterMax);
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		if (maxResult != 0 && DBType.contains("sqlserver")) {
			queryMax.setFirstResult(firstIndex);
			queryMax.setMaxResults(maxResult);
		}

		System.out.println("queryMax.getQueryString() : "
				+ queryMax.getQueryString());
		System.out.println("queryMax.list  :" + queryMax.list());
		List<Integer> maxIds = queryMax.list();

		Map<String, Object> params = new HashMap<String, Object>();
		int imax = 0;
		String where = " t.transactionId IN (";
		if (maxIds.size() > 0) {
			for (Integer ids : maxIds) {
				Integer idTrans = ids;
				where += ":idTrans" + imax + ", ";
				params.put("idTrans" + imax, idTrans);
				imax++;
			}
		} else {
			return new ArrayList<CourrierInformations>();
		}
		System.out.println("Liste :");
		StringBuffer hql = new StringBuffer(
				"SELECT DISTINCT c.idCourrier AS 'courrierID',"
						+ " c.courrierObjet AS 'courrierObjet',"
						+ " c.courrierReferenceCorrespondant AS 'courrierReference',"
						+ " c.courrierCommentaire AS 'courrierCommentaire',"
						+ " c.courrierOldNum AS 'courrierOldNum',"
						+ " c.courrierCircuit AS 'courrierCircuit',"
						+ " d.dossierId AS 'dossierID',"
						+ " n.natureLibelle AS 'courrierNature',"
						+ " t.etatId AS 'etatID',"
						+ " c.courrierDateReception AS 'courrierDateReceptionEnvoi',"
						+ " t.transactionDateConsultation AS 'transactionDateConsultation',"
						+ " t.transactionId AS 'transactionID',"
						+ " t.idExpDest AS 'expID',"
						+ " t.transactionOrdre AS 'transactionOrdre',"
						+ " t.idUtilisateur AS 'idUtilisateur',"
						+ " t_tdr.transactionDestinationReelleIdDestinataire AS 'transactionDestinationReelID',"
						+ " t_tdr.transactionDestinationReelleTypeDestinataire AS 'transactionDestinationReelleTypeDestinataire',"
						+ " e.typeExpDest AS 'expTypeOld',"
						+ " e.idExpDestLdap AS 'expLdapOld',"
						+ " ee.Exp_Dest_ExterneNom AS 'expNomOld',"
						+ " ee.Exp_Dest_ExternePrenom AS 'expPrenomOld',"
						+ " ee.typeUtilisateurId AS 'expTypeUserOld',"
						+ " tm.transactionId AS 'minTransactionID',"
						+ " em.typeExpDest AS 'expType',"
						+ " em.idExpDestLdap AS 'expLdap',"
						+ " eem.Exp_Dest_ExterneNom AS 'expNom',"
						+ " eem.Exp_Dest_ExternePrenom AS 'expPrenom',"
						+ " eem.typeUtilisateurId AS 'expTypeUser' ");

		
		System.out.println(hql.toString());
		hql.append(" FROM transactionn t"
				+ " INNER JOIN dossier d ON t.dossierId = d.dossierId"
				+ " INNER JOIN courrierdossier cd ON d.dossierId = cd.dossierId"
				+ " INNER JOIN courrier c ON cd.idCourrier = c.idCourrier"
				+ " INNER JOIN nature n ON c.idNature = n.natureId");

		hql.append(" LEFT JOIN transactiondest td ON t.transactionId = td.idTransaction"
				+ " LEFT JOIN expdest e ON t.idExpDest = e.idExpDest"
				+ " LEFT JOIN expdestexterne ee ON e.idExpDestExterne = ee.idExpDestExterne"
				+ " LEFT JOIN transactiondestinationreelle t_tdr ON t.transactionDestinationReelleId = t_tdr.transactionDestinationReelleId"
				+ " INNER JOIN transactionn tm ON t.transactionFirst = tm.transactionId"
				+ " LEFT JOIN expdest em ON tm.idExpDest = em.idExpDest"
				+ " LEFT JOIN expdestexterne eem ON em.idExpDestExterne = eem.idExpDestExterne"
				+ " WHERE d.typeDossierId = 1 AND "
				+ where.substring(0, where.length() - 2) + ")");

		// Order by Date
		hql.append(" ORDER BY c.idCourrier DESC");

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		query.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));

		query.addScalar("courrierID", new IntegerType());
		query.addScalar("courrierObjet", new StringType());
		query.addScalar("courrierReference", new StringType());
		query.addScalar("courrierCommentaire", new StringType());
		query.addScalar("courrierOldNum", new IntegerType());
		query.addScalar("courrierCircuit", new StringType());
		query.addScalar("dossierID", new IntegerType());
		query.addScalar("courrierNature", new StringType());
		query.addScalar("etatID", new IntegerType());
		query.addScalar("courrierDateReceptionEnvoi", new DateType());
		query.addScalar("transactionDateConsultation", new DateType());
		query.addScalar("transactionID", new IntegerType());
		query.addScalar("expID", new IntegerType());
		query.addScalar("transactionOrdre", new IntegerType());
		query.addScalar("idUtilisateur", new IntegerType());
		query.addScalar("transactionDestinationReelID", new IntegerType());
		query.addScalar("transactionDestinationReelleTypeDestinataire",
				new StringType());
		query.addScalar("expLdapOld", new IntegerType());
		query.addScalar("expTypeOld", new StringType());
		query.addScalar("expNomOld", new StringType());
		query.addScalar("expPrenomOld", new StringType());
		query.addScalar("expTypeUserOld", new IntegerType());
		query.addScalar("minTransactionID", new IntegerType());
		query.addScalar("expLdap", new IntegerType());
		query.addScalar("expType", new StringType());
		query.addScalar("expNom", new StringType());
		query.addScalar("expPrenom", new StringType());
		query.addScalar("expTypeUser", new IntegerType());

		Iterator<String> iter = params.keySet().iterator();
		while (iter.hasNext()) {
			System.out.println("AH  dans itr ");
			String name = iter.next();
			Object value = params.get(name);
			System.out.println(name + " = " + value);
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println(query.getQueryString());
		System.out.println("list :" + query.list());
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<CourrierInformations> findCourrierEnvoyerBOCWithCriterionValiseFormatPointer(
			int courrierValiseID, boolean descending, int firstIndex,
			int maxResult, String DBType) {
		Map<String, Object> pars = new HashMap<String, Object>();
		StringBuffer trMax = new StringBuffer(
				"SELECT MAX(tmax.transactionId) AS 'transactionId'"
						+ " FROM transactionn tmax, dossier dmax, courrierdossier cdmax, courrier cmax, transactionn tm"
						+ " WHERE cmax.idcourrierFK = " + courrierValiseID
						+ " AND cmax.courrierFormat != 'valise'"
						+ " AND tmax.dossierId = dmax.dossierId"
						+ " AND dmax.dossierId = cdmax.dossierId"
						+ " AND cdmax.idCourrier = cmax.idCourrier"
						+ " AND tmax.transactionFirst = tm.transactionId"
						+ " AND cmax.courrierDatePointage != 'null'");

		trMax.append(" GROUP BY dmax.dossierId"
				+ " ORDER BY dmax.dossierId DESC");
		if (maxResult != 0 && DBType.contains("mysql")) {
			trMax.append(" LIMIT " + firstIndex + ", " + maxResult);
		}

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		System.out.println("queryMax :" + queryMax);
		Iterator<String> iterMax = pars.keySet().iterator();
		System.out.println("iterMax ==>" + iterMax);
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		if (maxResult != 0 && DBType.contains("sqlserver")) {
			queryMax.setFirstResult(firstIndex);
			queryMax.setMaxResults(maxResult);
		}

		System.out.println("queryMax.getQueryString() : "
				+ queryMax.getQueryString());
		System.out.println("queryMax.list  :" + queryMax.list());
		List<Integer> maxIds = queryMax.list();

		Map<String, Object> params = new HashMap<String, Object>();
		int imax = 0;
		String where = " t.transactionId IN (";
		if (maxIds.size() > 0) {
			for (Integer ids : maxIds) {
				Integer idTrans = ids;
				where += ":idTrans" + imax + ", ";
				params.put("idTrans" + imax, idTrans);
				imax++;
			}
		} else {
			return new ArrayList<CourrierInformations>();
		}
		System.out.println("Liste :");
		StringBuffer hql = new StringBuffer(
				"SELECT DISTINCT c.idCourrier AS 'courrierID',"
						+ " c.courrierObjet AS 'courrierObjet',"
						+ " c.courrierReferenceCorrespondant AS 'courrierReference',"
						+ " c.courrierCommentaire AS 'courrierCommentaire',"
						+ " c.courrierOldNum AS 'courrierOldNum',"
						+ " c.courrierCircuit AS 'courrierCircuit',"
						+ " d.dossierId AS 'dossierID',"
						+ " n.natureLibelle AS 'courrierNature',"
						+ " t.etatId AS 'etatID',"
						+ " c.courrierDateReception AS 'courrierDateReceptionEnvoi',"
						+ " t.transactionDateConsultation AS 'transactionDateConsultation',"
						+ " t.transactionId AS 'transactionID',"
						+ " t.idExpDest AS 'expID',"
						+ " t.transactionOrdre AS 'transactionOrdre',"
						+ " t.idUtilisateur AS 'idUtilisateur',"
						+ " t_tdr.transactionDestinationReelleIdDestinataire AS 'transactionDestinationReelID',"
						+ " t_tdr.transactionDestinationReelleTypeDestinataire AS 'transactionDestinationReelleTypeDestinataire',"
						+ " e.typeExpDest AS 'expTypeOld',"
						+ " e.idExpDestLdap AS 'expLdapOld',"
						+ " ee.Exp_Dest_ExterneNom AS 'expNomOld',"
						+ " ee.Exp_Dest_ExternePrenom AS 'expPrenomOld',"
						+ " ee.typeUtilisateurId AS 'expTypeUserOld',"
						+ " tm.transactionId AS 'minTransactionID',"
						+ " em.typeExpDest AS 'expType',"
						+ " em.idExpDestLdap AS 'expLdap',"
						+ " eem.Exp_Dest_ExterneNom AS 'expNom',"
						+ " eem.Exp_Dest_ExternePrenom AS 'expPrenom',"
						+ " eem.typeUtilisateurId AS 'expTypeUser' ");

		
		System.out.println(hql.toString());
		hql.append(" FROM transactionn t"
				+ " INNER JOIN dossier d ON t.dossierId = d.dossierId"
				+ " INNER JOIN courrierdossier cd ON d.dossierId = cd.dossierId"
				+ " INNER JOIN courrier c ON cd.idCourrier = c.idCourrier"
				+ " INNER JOIN nature n ON c.idNature = n.natureId");

		hql.append(" LEFT JOIN transactiondest td ON t.transactionId = td.idTransaction"
				+ " LEFT JOIN expdest e ON t.idExpDest = e.idExpDest"
				+ " LEFT JOIN expdestexterne ee ON e.idExpDestExterne = ee.idExpDestExterne"
				+ " LEFT JOIN transactiondestinationreelle t_tdr ON t.transactionDestinationReelleId = t_tdr.transactionDestinationReelleId"
				+ " INNER JOIN transactionn tm ON t.transactionFirst = tm.transactionId"
				+ " LEFT JOIN expdest em ON tm.idExpDest = em.idExpDest"
				+ " LEFT JOIN expdestexterne eem ON em.idExpDestExterne = eem.idExpDestExterne"
				+ " WHERE d.typeDossierId = 1 AND "
				+ where.substring(0, where.length() - 2) + ")");

		// Order by Date
		hql.append(" ORDER BY c.idCourrier DESC");

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		query.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));

		query.addScalar("courrierID", new IntegerType());
		query.addScalar("courrierObjet", new StringType());
		query.addScalar("courrierReference", new StringType());
		query.addScalar("courrierCommentaire", new StringType());
		query.addScalar("courrierOldNum", new IntegerType());
		query.addScalar("courrierCircuit", new StringType());
		query.addScalar("dossierID", new IntegerType());
		query.addScalar("courrierNature", new StringType());
		query.addScalar("etatID", new IntegerType());
		query.addScalar("courrierDateReceptionEnvoi", new DateType());
		query.addScalar("transactionDateConsultation", new DateType());
		query.addScalar("transactionID", new IntegerType());
		query.addScalar("expID", new IntegerType());
		query.addScalar("transactionOrdre", new IntegerType());
		query.addScalar("idUtilisateur", new IntegerType());
		query.addScalar("transactionDestinationReelID", new IntegerType());
		query.addScalar("transactionDestinationReelleTypeDestinataire",
				new StringType());
		query.addScalar("expLdapOld", new IntegerType());
		query.addScalar("expTypeOld", new StringType());
		query.addScalar("expNomOld", new StringType());
		query.addScalar("expPrenomOld", new StringType());
		query.addScalar("expTypeUserOld", new IntegerType());
		query.addScalar("minTransactionID", new IntegerType());
		query.addScalar("expLdap", new IntegerType());
		query.addScalar("expType", new StringType());
		query.addScalar("expNom", new StringType());
		query.addScalar("expPrenom", new StringType());
		query.addScalar("expTypeUser", new IntegerType());

		Iterator<String> iter = params.keySet().iterator();
		while (iter.hasNext()) {
			System.out.println("AH  dans itr ");
			String name = iter.next();
			Object value = params.get(name);
			System.out.println(name + " = " + value);
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println(query.getQueryString());
		System.out.println("list :" + query.list());
		return query.list();
	}

	@Override
	public List<AoConsultation> findAoConsultationByNumero(String reference) {
		Map<String, Object> pars = new HashMap<String, Object>();

		StringBuffer hql = new StringBuffer("SELECT a.* "
				+ " FROM aoConsultation a"
				+ " WHERE a.aoConsultationNumero LIKE :reference");

		pars.put("reference", reference + '%');
		SQLQuery query = getSession().createSQLQuery(hql.toString());

		query.addEntity(AoConsultation.class);

		Iterator<String> iterMax = pars.keySet().iterator();

		while (iterMax.hasNext()) {

			String name = iterMax.next();
			Object value = pars.get(name);

			System.out.println("name" + name);
			System.out.println("value =" + value);

			if (pars.get(name).getClass() == String.class) {
				query.setString(name, (String) value);

			} else if (pars.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);

			} else if (pars.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);

			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println(query.getQueryString());
		return query.list();
	}

	@Override
	public List<CourrierInformations> findCourrierEnvoyerBOCWithCriterionValise(
			int idcourrierFK, boolean descending, int firstIndex,
			int maxResult, String DBType) {
		Map<String, Object> pars = new HashMap<String, Object>();
		StringBuffer trMax = new StringBuffer(
				"SELECT MAX(tmax.transactionId) AS 'transactionId'"
						+ " FROM transactionn tmax, dossier dmax, courrierdossier cdmax, courrier cmax, transactionn tm"
						+ " WHERE cmax.idcourrierFK = " + idcourrierFK
						+ " AND tmax.dossierId = dmax.dossierId"
						+ " AND dmax.dossierId = cdmax.dossierId"
						+ " AND cdmax.idCourrier = cmax.idCourrier"
						+ " AND tmax.transactionFirst = tm.transactionId");

		trMax.append(" GROUP BY dmax.dossierId"
				+ " ORDER BY dmax.dossierId DESC");
		if (maxResult != 0 && DBType.contains("mysql")) {
			trMax.append(" LIMIT " + firstIndex + ", " + maxResult);
		}

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		System.out.println("queryMax :" + queryMax);
		Iterator<String> iterMax = pars.keySet().iterator();
		System.out.println("iterMax ==>" + iterMax);
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		if (maxResult != 0 && DBType.contains("sqlserver")) {
			queryMax.setFirstResult(firstIndex);
			queryMax.setMaxResults(maxResult);
		}

		System.out.println("queryMax.getQueryString() : "
				+ queryMax.getQueryString());
		System.out.println("queryMax.list  :" + queryMax.list());
		List<Integer> maxIds = queryMax.list();

		Map<String, Object> params = new HashMap<String, Object>();
		int imax = 0;
		String where = " t.transactionId IN (";
		if (maxIds.size() > 0) {
			for (Integer ids : maxIds) {
				Integer idTrans = ids;
				where += ":idTrans" + imax + ", ";
				params.put("idTrans" + imax, idTrans);
				imax++;
			}
		} else {
			return new ArrayList<CourrierInformations>();
		}
		System.out.println("Liste :");
		StringBuffer hql = new StringBuffer(
				"SELECT DISTINCT c.idCourrier AS 'courrierID',"
						+ " c.courrierObjet AS 'courrierObjet',"
						+ " c.courrierReferenceCorrespondant AS 'courrierReference',"
						+ " c.courrierCommentaire AS 'courrierCommentaire',"
						+ " c.courrierOldNum AS 'courrierOldNum',"
						+ " c.courrierCircuit AS 'courrierCircuit',"
						+ " d.dossierId AS 'dossierID',"
						+ " n.natureLibelle AS 'courrierNature',"
						+ " t.etatId AS 'etatID',"
						+ " c.courrierDateReception AS 'courrierDateReceptionEnvoi',"
						+ " t.transactionDateConsultation AS 'transactionDateConsultation',"
						+ " t.transactionId AS 'transactionID',"
						+ " t.idExpDest AS 'expID',"
						+ " t.transactionOrdre AS 'transactionOrdre',"
						+ " t.idUtilisateur AS 'idUtilisateur',"
						+ " t_tdr.transactionDestinationReelleIdDestinataire AS 'transactionDestinationReelID',"
						+ " t_tdr.transactionDestinationReelleTypeDestinataire AS 'transactionDestinationReelleTypeDestinataire',"
						+ " e.typeExpDest AS 'expTypeOld',"
						+ " e.idExpDestLdap AS 'expLdapOld',"
						+ " ee.Exp_Dest_ExterneNom AS 'expNomOld',"
						+ " ee.Exp_Dest_ExternePrenom AS 'expPrenomOld',"
						+ " ee.typeUtilisateurId AS 'expTypeUserOld',"
						+ " tm.transactionId AS 'minTransactionID',"
						+ " em.typeExpDest AS 'expType',"
						+ " em.idExpDestLdap AS 'expLdap',"
						+ " eem.Exp_Dest_ExterneNom AS 'expNom',"
						+ " eem.Exp_Dest_ExternePrenom AS 'expPrenom',"
						+ " eem.typeUtilisateurId AS 'expTypeUser' ");

		System.out.println(hql.toString());
		hql.append(" FROM transactionn t"
				+ " INNER JOIN dossier d ON t.dossierId = d.dossierId"
				+ " INNER JOIN courrierdossier cd ON d.dossierId = cd.dossierId"
				+ " INNER JOIN courrier c ON cd.idCourrier = c.idCourrier"
				+ " INNER JOIN nature n ON c.idNature = n.natureId");

		hql.append(" LEFT JOIN transactiondest td ON t.transactionId = td.idTransaction"
				+ " LEFT JOIN expdest e ON t.idExpDest = e.idExpDest"
				+ " LEFT JOIN expdestexterne ee ON e.idExpDestExterne = ee.idExpDestExterne"
				+ " LEFT JOIN transactiondestinationreelle t_tdr ON t.transactionDestinationReelleId = t_tdr.transactionDestinationReelleId"
				+ " INNER JOIN transactionn tm ON t.transactionFirst = tm.transactionId"
				+ " LEFT JOIN expdest em ON tm.idExpDest = em.idExpDest"
				+ " LEFT JOIN expdestexterne eem ON em.idExpDestExterne = eem.idExpDestExterne"
				+ " WHERE d.typeDossierId = 1 AND "
				+ where.substring(0, where.length() - 2) + ")");

		// Order by Date
		hql.append(" ORDER BY c.idCourrier DESC");

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		query.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));

		query.addScalar("courrierID", new IntegerType());
		query.addScalar("courrierObjet", new StringType());
		query.addScalar("courrierReference", new StringType());
		query.addScalar("courrierCommentaire", new StringType());
		query.addScalar("courrierOldNum", new IntegerType());
		query.addScalar("courrierCircuit", new StringType());
		query.addScalar("dossierID", new IntegerType());
		query.addScalar("courrierNature", new StringType());
		query.addScalar("etatID", new IntegerType());
		query.addScalar("courrierDateReceptionEnvoi", new DateType());
		query.addScalar("transactionDateConsultation", new DateType());
		query.addScalar("transactionID", new IntegerType());
		query.addScalar("expID", new IntegerType());
		query.addScalar("transactionOrdre", new IntegerType());
		query.addScalar("idUtilisateur", new IntegerType());
		query.addScalar("transactionDestinationReelID", new IntegerType());
		query.addScalar("transactionDestinationReelleTypeDestinataire",
				new StringType());
		query.addScalar("expLdapOld", new IntegerType());
		query.addScalar("expTypeOld", new StringType());
		query.addScalar("expNomOld", new StringType());
		query.addScalar("expPrenomOld", new StringType());
		query.addScalar("expTypeUserOld", new IntegerType());
		query.addScalar("minTransactionID", new IntegerType());
		query.addScalar("expLdap", new IntegerType());
		query.addScalar("expType", new StringType());
		query.addScalar("expNom", new StringType());
		query.addScalar("expPrenom", new StringType());
		query.addScalar("expTypeUser", new IntegerType());

		Iterator<String> iter = params.keySet().iterator();
		while (iter.hasNext()) {
			System.out.println("AH  dans itr ");
			String name = iter.next();
			Object value = params.get(name);
			System.out.println(name + " = " + value);
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println(query.getQueryString());
		System.out.println("list :" + query.list());
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<CourrierInformations> getListCourriersNonAffectesAvalise(
			boolean descending, int firstIndex, int maxResult, String DBType) {
		Map<String, Object> pars = new HashMap<String, Object>();
		StringBuffer trMax = new StringBuffer(
				"SELECT MAX(tmax.transactionId) AS 'transactionId'"
						+ " FROM transactionn tmax, dossier dmax, courrierdossier cdmax, courrier cmax, transactionn tm"
						+ " WHERE cmax.idcourrierFK is null  "
						+ " AND cmax.courrierFormat is null "
						+ " AND tmax.dossierId = dmax.dossierId"
						+ " AND dmax.dossierId = cdmax.dossierId"
						+ " AND cdmax.idCourrier = cmax.idCourrier"
						+ " AND tmax.transactionFirst = tm.transactionId");

		trMax.append(" GROUP BY dmax.dossierId"
				+ " ORDER BY dmax.dossierId DESC");
		if (maxResult != 0 && DBType.contains("mysql")) {
			trMax.append(" LIMIT " + firstIndex + ", " + maxResult);
		}

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		System.out.println("queryMax :" + queryMax);
		Iterator<String> iterMax = pars.keySet().iterator();
		System.out.println("iterMax ==>" + iterMax);
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		if (maxResult != 0 && DBType.contains("sqlserver")) {
			queryMax.setFirstResult(firstIndex);
			queryMax.setMaxResults(maxResult);
		}

		System.out.println("queryMax.getQueryString() : "
				+ queryMax.getQueryString());
		System.out.println("queryMax.list  :" + queryMax.list());
		List<Integer> maxIds = queryMax.list();

		Map<String, Object> params = new HashMap<String, Object>();
		int imax = 0;
		String where = " t.transactionId IN (";
		if (maxIds.size() > 0) {
			for (Integer ids : maxIds) {
				Integer idTrans = ids;
				where += ":idTrans" + imax + ", ";
				params.put("idTrans" + imax, idTrans);
				imax++;
			}
		} else {
			return new ArrayList<CourrierInformations>();
		}
		System.out.println("Liste :");
		StringBuffer hql = new StringBuffer(
				"SELECT DISTINCT c.idCourrier AS 'courrierID',"
						+ " c.courrierObjet AS 'courrierObjet',"
						+ " c.courrierReferenceCorrespondant AS 'courrierReference',"
						+ " c.courrierCommentaire AS 'courrierCommentaire',"
						+ " c.courrierOldNum AS 'courrierOldNum',"
						+ " c.courrierCircuit AS 'courrierCircuit',"
						+ " d.dossierId AS 'dossierID',"
						+ " n.natureLibelle AS 'courrierNature',"
						+ " t.etatId AS 'etatID',"
						+ " c.courrierDateReception AS 'courrierDateReceptionEnvoi',"
						+ " t.transactionDateConsultation AS 'transactionDateConsultation',"
						+ " t.transactionId AS 'transactionID',"
						+ " t.idExpDest AS 'expID',"
						+ " t.transactionOrdre AS 'transactionOrdre',"
						+ " t.idUtilisateur AS 'idUtilisateur',"
						+ " t_tdr.transactionDestinationReelleIdDestinataire AS 'transactionDestinationReelID',"
						+ " t_tdr.transactionDestinationReelleTypeDestinataire AS 'transactionDestinationReelleTypeDestinataire',"
						+ " e.typeExpDest AS 'expTypeOld',"
						+ " e.idExpDestLdap AS 'expLdapOld',"
						+ " ee.Exp_Dest_ExterneNom AS 'expNomOld',"
						+ " ee.Exp_Dest_ExternePrenom AS 'expPrenomOld',"
						+ " ee.typeUtilisateurId AS 'expTypeUserOld',"
						+ " tm.transactionId AS 'minTransactionID',"
						+ " em.typeExpDest AS 'expType',"
						+ " em.idExpDestLdap AS 'expLdap',"
						+ " eem.Exp_Dest_ExterneNom AS 'expNom',"
						+ " eem.Exp_Dest_ExternePrenom AS 'expPrenom',"
						+ " eem.typeUtilisateurId AS 'expTypeUser' ");

		
		System.out.println(hql.toString());
		hql.append(" FROM transactionn t"
				+ " INNER JOIN dossier d ON t.dossierId = d.dossierId"
				+ " INNER JOIN courrierdossier cd ON d.dossierId = cd.dossierId"
				+ " INNER JOIN courrier c ON cd.idCourrier = c.idCourrier"
				+ " INNER JOIN nature n ON c.idNature = n.natureId");

		hql.append(" LEFT JOIN transactiondest td ON t.transactionId = td.idTransaction"
				+ " LEFT JOIN expdest e ON t.idExpDest = e.idExpDest"
				+ " LEFT JOIN expdestexterne ee ON e.idExpDestExterne = ee.idExpDestExterne"
				+ " LEFT JOIN transactiondestinationreelle t_tdr ON t.transactionDestinationReelleId = t_tdr.transactionDestinationReelleId"
				+ " INNER JOIN transactionn tm ON t.transactionFirst = tm.transactionId"
				+ " LEFT JOIN expdest em ON tm.idExpDest = em.idExpDest"
				+ " LEFT JOIN expdestexterne eem ON em.idExpDestExterne = eem.idExpDestExterne"
				+ " WHERE d.typeDossierId = 1 AND "
				+ where.substring(0, where.length() - 2) + ")");

		// Order by Date
		hql.append(" ORDER BY c.idCourrier DESC");

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		query.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));

		query.addScalar("courrierID", new IntegerType());
		query.addScalar("courrierObjet", new StringType());
		query.addScalar("courrierReference", new StringType());
		query.addScalar("courrierCommentaire", new StringType());
		query.addScalar("courrierOldNum", new IntegerType());
		query.addScalar("courrierCircuit", new StringType());
		query.addScalar("dossierID", new IntegerType());
		query.addScalar("courrierNature", new StringType());
		query.addScalar("etatID", new IntegerType());
		query.addScalar("courrierDateReceptionEnvoi", new DateType());
		query.addScalar("transactionDateConsultation", new DateType());
		query.addScalar("transactionID", new IntegerType());
		query.addScalar("expID", new IntegerType());
		query.addScalar("transactionOrdre", new IntegerType());
		query.addScalar("idUtilisateur", new IntegerType());
		query.addScalar("transactionDestinationReelID", new IntegerType());
		query.addScalar("transactionDestinationReelleTypeDestinataire",
				new StringType());
		query.addScalar("expLdapOld", new IntegerType());
		query.addScalar("expTypeOld", new StringType());
		query.addScalar("expNomOld", new StringType());
		query.addScalar("expPrenomOld", new StringType());
		query.addScalar("expTypeUserOld", new IntegerType());
		query.addScalar("minTransactionID", new IntegerType());
		query.addScalar("expLdap", new IntegerType());
		query.addScalar("expType", new StringType());
		query.addScalar("expNom", new StringType());
		query.addScalar("expPrenom", new StringType());
		query.addScalar("expTypeUser", new IntegerType());

		Iterator<String> iter = params.keySet().iterator();
		while (iter.hasNext()) {
			System.out.println("AH  dans itr ");
			String name = iter.next();
			Object value = params.get(name);
			System.out.println(name + " = " + value);
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println(query.getQueryString());
		System.out.println("list :" + query.list());
		return query.list();
	}

	public List<CourrierInformations> findCourrierEnvoyerBOCRecuAvantLorsAO(
			int idaoConsultation, Date AoConsultationDateSeanceCommission,
			boolean descending, int firstIndex, int maxResult, String DBType) {
		Map<String, Object> pars = new HashMap<String, Object>();
		StringBuffer trMax = new StringBuffer(
				"SELECT MAX(tmax.transactionId) AS 'transactionId'"
						+ " FROM transactionn tmax, dossier dmax, courrierdossier cdmax, courrier cmax, transactionn tm"
						+ " WHERE cmax.idaoConsultation = " + idaoConsultation
						+ " AND cmax.idTransmission IN (2,5,6)"
						+ " AND tmax.dossierId = dmax.dossierId"
						+ " AND dmax.dossierId = cdmax.dossierId"
						+ " AND cdmax.idCourrier = cmax.idCourrier"
						+ " AND tmax.transactionFirst = tm.transactionId");

		SimpleDateFormat debut = new SimpleDateFormat("yyyy-MM-dd");
		String dateCom = debut.format(AoConsultationDateSeanceCommission);
		System.out
				.println("######################Dans findCourrierEnvoyerBOCReçuAvantLorsAO");
		System.out.println(" KHA : par jour");
		trMax.append(" AND DATE_FORMAT(cmax.courrierDateReception,'%Y-%m-%d') <= :dateCommission");
		System.out.println("transactionDateDebut = " + dateCom);
		pars.put("dateCommission", dateCom);

		trMax.append(" GROUP BY dmax.dossierId"
				+ " ORDER BY dmax.dossierId DESC");
		if (maxResult != 0 && DBType.contains("mysql")) {
			trMax.append(" LIMIT " + firstIndex + ", " + maxResult);
		}

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		System.out.println("queryMax :" + queryMax);
		Iterator<String> iterMax = pars.keySet().iterator();
		System.out.println("iterMax ==>" + iterMax);
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		if (maxResult != 0 && DBType.contains("sqlserver")) {
			queryMax.setFirstResult(firstIndex);
			queryMax.setMaxResults(maxResult);
		}

		System.out.println("queryMax.getQueryString() : "
				+ queryMax.getQueryString());
		System.out.println("queryMax.list  :" + queryMax.list());
		List<Integer> maxIds = queryMax.list();

		Map<String, Object> params = new HashMap<String, Object>();
		int imax = 0;
		String where = " t.transactionId IN (";
		if (maxIds.size() > 0) {
			for (Integer ids : maxIds) {
				Integer idTrans = ids;
				where += ":idTrans" + imax + ", ";
				params.put("idTrans" + imax, idTrans);
				imax++;
			}
		} else {
			return new ArrayList<CourrierInformations>();
		}
		System.out.println("Liste :");
		StringBuffer hql = new StringBuffer(
				"SELECT DISTINCT c.idCourrier AS 'courrierID',"
						+ " c.courrierObjet AS 'courrierObjet',"
						+ " c.courrierReferenceCorrespondant AS 'courrierReference',"
						+ " c.courrierCommentaire AS 'courrierCommentaire',"
						+ " c.courrierOldNum AS 'courrierOldNum',"
						+ " c.courrierCircuit AS 'courrierCircuit',"
						+ " d.dossierId AS 'dossierID',"
						+ " n.natureLibelle AS 'courrierNature',"
						+ " t.etatId AS 'etatID',"
						+ " c.courrierDateReception AS 'courrierDateReceptionEnvoi',"
						+ " t.transactionDateConsultation AS 'transactionDateConsultation',"
						+ " t.transactionId AS 'transactionID',"
						+ " t.idExpDest AS 'expID',"
						+ " t.transactionOrdre AS 'transactionOrdre',"
						+ " t.idUtilisateur AS 'idUtilisateur',"
						+ " t_tdr.transactionDestinationReelleIdDestinataire AS 'transactionDestinationReelID',"
						+ " t_tdr.transactionDestinationReelleTypeDestinataire AS 'transactionDestinationReelleTypeDestinataire',"
						+ " e.typeExpDest AS 'expTypeOld',"
						+ " e.idExpDestLdap AS 'expLdapOld',"
						+ " ee.Exp_Dest_ExterneNom AS 'expNomOld',"
						+ " ee.Exp_Dest_ExternePrenom AS 'expPrenomOld',"
						+ " ee.typeUtilisateurId AS 'expTypeUserOld',"
						+ " tm.transactionId AS 'minTransactionID',"
						+ " em.typeExpDest AS 'expType',"
						+ " em.idExpDestLdap AS 'expLdap',"
						+ " eem.Exp_Dest_ExterneNom AS 'expNom',"
						+ " eem.Exp_Dest_ExternePrenom AS 'expPrenom',"
						+ " eem.typeUtilisateurId AS 'expTypeUser' ");

		
		System.out.println(hql.toString());
		hql.append(" FROM transactionn t"
				+ " INNER JOIN dossier d ON t.dossierId = d.dossierId"
				+ " INNER JOIN courrierdossier cd ON d.dossierId = cd.dossierId"
				+ " INNER JOIN courrier c ON cd.idCourrier = c.idCourrier"
				+ " INNER JOIN nature n ON c.idNature = n.natureId");

		hql.append(" LEFT JOIN transactiondest td ON t.transactionId = td.idTransaction"
				+ " LEFT JOIN expdest e ON t.idExpDest = e.idExpDest"
				+ " LEFT JOIN expdestexterne ee ON e.idExpDestExterne = ee.idExpDestExterne"
				+ " LEFT JOIN transactiondestinationreelle t_tdr ON t.transactionDestinationReelleId = t_tdr.transactionDestinationReelleId"
				+ " INNER JOIN transactionn tm ON t.transactionFirst = tm.transactionId"
				+ " LEFT JOIN expdest em ON tm.idExpDest = em.idExpDest"
				+ " LEFT JOIN expdestexterne eem ON em.idExpDestExterne = eem.idExpDestExterne"
				+ " WHERE d.typeDossierId = 1 AND "
				+ where.substring(0, where.length() - 2) + ")");

		// Order by Date
		hql.append(" ORDER BY c.idCourrier DESC");

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		query.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));

		query.addScalar("courrierID", new IntegerType());
		query.addScalar("courrierObjet", new StringType());
		query.addScalar("courrierReference", new StringType());
		query.addScalar("courrierCommentaire", new StringType());
		query.addScalar("courrierOldNum", new IntegerType());
		query.addScalar("courrierCircuit", new StringType());
		query.addScalar("dossierID", new IntegerType());
		query.addScalar("courrierNature", new StringType());
		query.addScalar("etatID", new IntegerType());
		query.addScalar("courrierDateReceptionEnvoi", new DateType());
		query.addScalar("transactionDateConsultation", new DateType());
		query.addScalar("transactionID", new IntegerType());
		query.addScalar("expID", new IntegerType());
		query.addScalar("transactionOrdre", new IntegerType());
		query.addScalar("idUtilisateur", new IntegerType());
		query.addScalar("transactionDestinationReelID", new IntegerType());
		query.addScalar("transactionDestinationReelleTypeDestinataire",
				new StringType());
		query.addScalar("expLdapOld", new IntegerType());
		query.addScalar("expTypeOld", new StringType());
		query.addScalar("expNomOld", new StringType());
		query.addScalar("expPrenomOld", new StringType());
		query.addScalar("expTypeUserOld", new IntegerType());
		query.addScalar("minTransactionID", new IntegerType());
		query.addScalar("expLdap", new IntegerType());
		query.addScalar("expType", new StringType());
		query.addScalar("expNom", new StringType());
		query.addScalar("expPrenom", new StringType());
		query.addScalar("expTypeUser", new IntegerType());

		Iterator<String> iter = params.keySet().iterator();
		while (iter.hasNext()) {
			System.out.println("AH  dans itr ");
			String name = iter.next();
			Object value = params.get(name);
			System.out.println(name + " = " + value);
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println(query.getQueryString());
		System.out.println("list :" + query.list());
		return query.list();
	}

	public List<CourrierInformations> findCourrierEnvoyerBOCRecuAvantLorsAOPorteur(
			int idaoConsultation, Date aoConsultationDateSeanceCommission,
			boolean descending, int firstIndex, int maxResult, String DBType) {
		Map<String, Object> pars = new HashMap<String, Object>();
		StringBuffer trMax = new StringBuffer(
				"SELECT MAX(tmax.transactionId) AS 'transactionId'"
						+ " FROM transactionn tmax, dossier dmax, courrierdossier cdmax, courrier cmax, transactionn tm"
						+ " WHERE cmax.idaoConsultation = " + idaoConsultation
						+ " AND cmax.idTransmission = 1"
						+ " AND tmax.dossierId = dmax.dossierId"
						+ " AND dmax.dossierId = cdmax.dossierId"
						+ " AND cdmax.idCourrier = cmax.idCourrier"
						+ " AND tmax.transactionFirst = tm.transactionId");

		SimpleDateFormat debut = new SimpleDateFormat("yyyy-MM-dd");
		String dateCom = debut.format(aoConsultationDateSeanceCommission);

		System.out.println(" KHA : par jour");
		trMax.append(" AND DATE_FORMAT(cmax.courrierDateReception,'%Y-%m-%d') < = :dateCommission");
		pars.put("dateCommission", dateCom);

		trMax.append(" GROUP BY dmax.dossierId"
				+ " ORDER BY dmax.dossierId DESC");
		if (maxResult != 0 && DBType.contains("mysql")) {
			trMax.append(" LIMIT " + firstIndex + ", " + maxResult);
		}

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		System.out.println("queryMax :" + queryMax);
		Iterator<String> iterMax = pars.keySet().iterator();
		System.out.println("iterMax ==>" + iterMax);
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		if (maxResult != 0 && DBType.contains("sqlserver")) {
			queryMax.setFirstResult(firstIndex);
			queryMax.setMaxResults(maxResult);
		}

		System.out.println("queryMax.getQueryString() : "
				+ queryMax.getQueryString());
		System.out.println("queryMax.list  :" + queryMax.list());
		List<Integer> maxIds = queryMax.list();

		Map<String, Object> params = new HashMap<String, Object>();
		int imax = 0;
		String where = " t.transactionId IN (";
		if (maxIds.size() > 0) {
			for (Integer ids : maxIds) {
				Integer idTrans = ids;
				where += ":idTrans" + imax + ", ";
				params.put("idTrans" + imax, idTrans);
				imax++;
			}
		} else {
			return new ArrayList<CourrierInformations>();
		}
		System.out.println("Liste :");
		StringBuffer hql = new StringBuffer(
				"SELECT DISTINCT c.idCourrier AS 'courrierID',"
						+ " c.courrierObjet AS 'courrierObjet',"
						+ " c.courrierReferenceCorrespondant AS 'courrierReference',"
						+ " c.courrierCommentaire AS 'courrierCommentaire',"
						+ " c.courrierOldNum AS 'courrierOldNum',"
						+ " c.courrierCircuit AS 'courrierCircuit',"
						+ " d.dossierId AS 'dossierID',"
						+ " n.natureLibelle AS 'courrierNature',"
						+ " t.etatId AS 'etatID',"
						+ " c.courrierDateReception AS 'courrierDateReceptionEnvoi',"
						+ " t.transactionDateConsultation AS 'transactionDateConsultation',"
						+ " t.transactionId AS 'transactionID',"
						+ " t.idExpDest AS 'expID',"
						+ " t.transactionOrdre AS 'transactionOrdre',"
						+ " t.idUtilisateur AS 'idUtilisateur',"
						+ " t_tdr.transactionDestinationReelleIdDestinataire AS 'transactionDestinationReelID',"
						+ " t_tdr.transactionDestinationReelleTypeDestinataire AS 'transactionDestinationReelleTypeDestinataire',"
						+ " e.typeExpDest AS 'expTypeOld',"
						+ " e.idExpDestLdap AS 'expLdapOld',"
						+ " ee.Exp_Dest_ExterneNom AS 'expNomOld',"
						+ " ee.Exp_Dest_ExternePrenom AS 'expPrenomOld',"
						+ " ee.typeUtilisateurId AS 'expTypeUserOld',"
						+ " tm.transactionId AS 'minTransactionID',"
						+ " em.typeExpDest AS 'expType',"
						+ " em.idExpDestLdap AS 'expLdap',"
						+ " eem.Exp_Dest_ExterneNom AS 'expNom',"
						+ " eem.Exp_Dest_ExternePrenom AS 'expPrenom',"
						+ " eem.typeUtilisateurId AS 'expTypeUser' ");

		System.out.println(hql.toString());
		hql.append(" FROM transactionn t"
				+ " INNER JOIN dossier d ON t.dossierId = d.dossierId"
				+ " INNER JOIN courrierdossier cd ON d.dossierId = cd.dossierId"
				+ " INNER JOIN courrier c ON cd.idCourrier = c.idCourrier"
				+ " INNER JOIN nature n ON c.idNature = n.natureId");

		hql.append(" LEFT JOIN transactiondest td ON t.transactionId = td.idTransaction"
				+ " LEFT JOIN expdest e ON t.idExpDest = e.idExpDest"
				+ " LEFT JOIN expdestexterne ee ON e.idExpDestExterne = ee.idExpDestExterne"
				+ " LEFT JOIN transactiondestinationreelle t_tdr ON t.transactionDestinationReelleId = t_tdr.transactionDestinationReelleId"
				+ " INNER JOIN transactionn tm ON t.transactionFirst = tm.transactionId"
				+ " LEFT JOIN expdest em ON tm.idExpDest = em.idExpDest"
				+ " LEFT JOIN expdestexterne eem ON em.idExpDestExterne = eem.idExpDestExterne"
				+ " WHERE d.typeDossierId = 1 AND "
				+ where.substring(0, where.length() - 2) + ")");

		// Order by Date
		hql.append(" ORDER BY c.idCourrier DESC");

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		query.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));

		query.addScalar("courrierID", new IntegerType());
		query.addScalar("courrierObjet", new StringType());
		query.addScalar("courrierReference", new StringType());
		query.addScalar("courrierCommentaire", new StringType());
		query.addScalar("courrierOldNum", new IntegerType());
		query.addScalar("courrierCircuit", new StringType());
		query.addScalar("dossierID", new IntegerType());
		query.addScalar("courrierNature", new StringType());
		query.addScalar("etatID", new IntegerType());
		query.addScalar("courrierDateReceptionEnvoi", new DateType());
		query.addScalar("transactionDateConsultation", new DateType());
		query.addScalar("transactionID", new IntegerType());
		query.addScalar("expID", new IntegerType());
		query.addScalar("transactionOrdre", new IntegerType());
		query.addScalar("idUtilisateur", new IntegerType());
		query.addScalar("transactionDestinationReelID", new IntegerType());
		query.addScalar("transactionDestinationReelleTypeDestinataire",
				new StringType());
		query.addScalar("expLdapOld", new IntegerType());
		query.addScalar("expTypeOld", new StringType());
		query.addScalar("expNomOld", new StringType());
		query.addScalar("expPrenomOld", new StringType());
		query.addScalar("expTypeUserOld", new IntegerType());
		query.addScalar("minTransactionID", new IntegerType());
		query.addScalar("expLdap", new IntegerType());
		query.addScalar("expType", new StringType());
		query.addScalar("expNom", new StringType());
		query.addScalar("expPrenom", new StringType());
		query.addScalar("expTypeUser", new IntegerType());

		Iterator<String> iter = params.keySet().iterator();
		while (iter.hasNext()) {
			System.out.println("AH  dans itr ");
			String name = iter.next();
			Object value = params.get(name);
			System.out.println(name + " = " + value);
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println(query.getQueryString());
		System.out.println("list :" + query.list());
		return query.list();
	}

	public List<CourrierInformations> findCourrierEnvoyerBOCRecuDansDelaisAO(
			int idaoConsultation, Date aoConsultationDateLimiteReception,
			Date aoConsultationDelaisProlongation, boolean descending,
			int firstIndex, int maxResult, String DBType) {
		Map<String, Object> pars = new HashMap<String, Object>();
		StringBuffer trMax = new StringBuffer(
				"SELECT MAX(tmax.transactionId) AS 'transactionId'"
						+ " FROM transactionn tmax, dossier dmax, courrierdossier cdmax, courrier cmax, transactionn tm"
						+ " WHERE cmax.idaoConsultation = " + idaoConsultation
						+ " AND cmax.idTransmission IN (2,5,6)"
						+ " AND tmax.dossierId = dmax.dossierId"
						+ " AND dmax.dossierId = cdmax.dossierId"
						+ " AND cdmax.idCourrier = cmax.idCourrier"
						+ " AND tmax.transactionFirst = tm.transactionId");
		System.out.println("####Dans req  prolongation ");
		SimpleDateFormat debut = new SimpleDateFormat("yyyy-MM-dd");
		
		if (aoConsultationDelaisProlongation != null) {
			System.out.println("####Dans test prolongation ");
			String dateprolongation = debut.format(aoConsultationDelaisProlongation);
			trMax.append(" AND DATE_FORMAT(cmax.courrierDateReception,'%Y-%m-%d') < :dateprolongation");
			pars.put("dateprolongation", dateprolongation);
		}
		else {
			System.out.println("####Dans else test prolongation ");
			String dateLimiteReception = debut.format(aoConsultationDateLimiteReception);
			trMax.append(" AND DATE_FORMAT(cmax.courrierDateReception,'%Y-%m-%d') <= :dateLimiteReception");
			pars.put("dateLimiteReception", dateLimiteReception);
		}
		

		trMax.append(" GROUP BY dmax.dossierId"
				+ " ORDER BY dmax.dossierId DESC");
		if (maxResult != 0 && DBType.contains("mysql")) {
			trMax.append(" LIMIT " + firstIndex + ", " + maxResult);
		}

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		System.out.println("queryMax :" + queryMax);
		Iterator<String> iterMax = pars.keySet().iterator();
		System.out.println("iterMax ==>" + iterMax);
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		if (maxResult != 0 && DBType.contains("sqlserver")) {
			queryMax.setFirstResult(firstIndex);
			queryMax.setMaxResults(maxResult);
		}

		System.out.println("queryMax.getQueryString() : "
				+ queryMax.getQueryString());
		System.out.println("queryMax.list  :" + queryMax.list());
		List<Integer> maxIds = queryMax.list();

		Map<String, Object> params = new HashMap<String, Object>();
		int imax = 0;
		String where = " t.transactionId IN (";
		if (maxIds.size() > 0) {
			for (Integer ids : maxIds) {
				Integer idTrans = ids;
				where += ":idTrans" + imax + ", ";
				params.put("idTrans" + imax, idTrans);
				imax++;
			}
		} else {
			return new ArrayList<CourrierInformations>();
		}
		System.out.println("Liste :");
		StringBuffer hql = new StringBuffer(
				"SELECT DISTINCT c.idCourrier AS 'courrierID',"
						+ " c.courrierObjet AS 'courrierObjet',"
						+ " c.courrierReferenceCorrespondant AS 'courrierReference',"
						+ " c.courrierCommentaire AS 'courrierCommentaire',"
						+ " c.courrierOldNum AS 'courrierOldNum',"
						+ " c.courrierCircuit AS 'courrierCircuit',"
						+ " d.dossierId AS 'dossierID',"
						+ " n.natureLibelle AS 'courrierNature',"
						+ " t.etatId AS 'etatID',"
						+ " c.courrierDateReception AS 'courrierDateReceptionEnvoi',"
						+ " t.transactionDateConsultation AS 'transactionDateConsultation',"
						+ " t.transactionId AS 'transactionID',"
						+ " t.idExpDest AS 'expID',"
						+ " t.transactionOrdre AS 'transactionOrdre',"
						+ " t.idUtilisateur AS 'idUtilisateur',"
						+ " t_tdr.transactionDestinationReelleIdDestinataire AS 'transactionDestinationReelID',"
						+ " t_tdr.transactionDestinationReelleTypeDestinataire AS 'transactionDestinationReelleTypeDestinataire',"
						+ " e.typeExpDest AS 'expTypeOld',"
						+ " e.idExpDestLdap AS 'expLdapOld',"
						+ " ee.Exp_Dest_ExterneNom AS 'expNomOld',"
						+ " ee.Exp_Dest_ExternePrenom AS 'expPrenomOld',"
						+ " ee.typeUtilisateurId AS 'expTypeUserOld',"
						+ " tm.transactionId AS 'minTransactionID',"
						+ " em.typeExpDest AS 'expType',"
						+ " em.idExpDestLdap AS 'expLdap',"
						+ " eem.Exp_Dest_ExterneNom AS 'expNom',"
						+ " eem.Exp_Dest_ExternePrenom AS 'expPrenom',"
						+ " eem.typeUtilisateurId AS 'expTypeUser' ");

		System.out.println(hql.toString());
		hql.append(" FROM transactionn t"
				+ " INNER JOIN dossier d ON t.dossierId = d.dossierId"
				+ " INNER JOIN courrierdossier cd ON d.dossierId = cd.dossierId"
				+ " INNER JOIN courrier c ON cd.idCourrier = c.idCourrier"
				+ " INNER JOIN nature n ON c.idNature = n.natureId");

		hql.append(" LEFT JOIN transactiondest td ON t.transactionId = td.idTransaction"
				+ " LEFT JOIN expdest e ON t.idExpDest = e.idExpDest"
				+ " LEFT JOIN expdestexterne ee ON e.idExpDestExterne = ee.idExpDestExterne"
				+ " LEFT JOIN transactiondestinationreelle t_tdr ON t.transactionDestinationReelleId = t_tdr.transactionDestinationReelleId"
				+ " INNER JOIN transactionn tm ON t.transactionFirst = tm.transactionId"
				+ " LEFT JOIN expdest em ON tm.idExpDest = em.idExpDest"
				+ " LEFT JOIN expdestexterne eem ON em.idExpDestExterne = eem.idExpDestExterne"
				+ " WHERE d.typeDossierId = 1 AND "
				+ where.substring(0, where.length() - 2) + ")");

		// Order by Date
		hql.append(" ORDER BY c.idCourrier DESC");

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		query.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));

		query.addScalar("courrierID", new IntegerType());
		query.addScalar("courrierObjet", new StringType());
		query.addScalar("courrierReference", new StringType());
		query.addScalar("courrierCommentaire", new StringType());
		query.addScalar("courrierOldNum", new IntegerType());
		query.addScalar("courrierCircuit", new StringType());
		query.addScalar("dossierID", new IntegerType());
		query.addScalar("courrierNature", new StringType());
		query.addScalar("etatID", new IntegerType());
		query.addScalar("courrierDateReceptionEnvoi", new DateType());
		query.addScalar("transactionDateConsultation", new DateType());
		query.addScalar("transactionID", new IntegerType());
		query.addScalar("expID", new IntegerType());
		query.addScalar("transactionOrdre", new IntegerType());
		query.addScalar("idUtilisateur", new IntegerType());
		query.addScalar("transactionDestinationReelID", new IntegerType());
		query.addScalar("transactionDestinationReelleTypeDestinataire",
				new StringType());
		query.addScalar("expLdapOld", new IntegerType());
		query.addScalar("expTypeOld", new StringType());
		query.addScalar("expNomOld", new StringType());
		query.addScalar("expPrenomOld", new StringType());
		query.addScalar("expTypeUserOld", new IntegerType());
		query.addScalar("minTransactionID", new IntegerType());
		query.addScalar("expLdap", new IntegerType());
		query.addScalar("expType", new StringType());
		query.addScalar("expNom", new StringType());
		query.addScalar("expPrenom", new StringType());
		query.addScalar("expTypeUser", new IntegerType());

		Iterator<String> iter = params.keySet().iterator();
		while (iter.hasNext()) {
			System.out.println("AH  dans itr ");
			String name = iter.next();
			Object value = params.get(name);
			System.out.println(name + " = " + value);
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println(query.getQueryString());
		System.out.println("list :" + query.list());
		return query.list();
	}

	public List<CourrierInformations> findCourrierEnvoyerBOCRecuDansDelaisAOPorteur(
			int idaoConsultation, Date aoConsultationDateLimiteReception,
			Date aoConsultationDelaisProlongation, boolean descending,
			int firstIndex, int maxResult, String DBType) {
		Map<String, Object> pars = new HashMap<String, Object>();
		StringBuffer trMax = new StringBuffer(
				"SELECT MAX(tmax.transactionId) AS 'transactionId'"
						+ " FROM transactionn tmax, dossier dmax, courrierdossier cdmax, courrier cmax, transactionn tm"
						+ " WHERE cmax.idaoConsultation = " + idaoConsultation
						+ " AND cmax.idTransmission = 1"
						+ " AND tmax.dossierId = dmax.dossierId"
						+ " AND dmax.dossierId = cdmax.dossierId"
						+ " AND cdmax.idCourrier = cmax.idCourrier"
						+ " AND tmax.transactionFirst = tm.transactionId");

		SimpleDateFormat debut = new SimpleDateFormat("yyyy-MM-dd");
		String dateLimiteReception = debut.format(aoConsultationDateLimiteReception);
		if (aoConsultationDelaisProlongation != null) {
			System.out.println("####Dans test prolongation ");
			String dateprolongation = debut.format(aoConsultationDelaisProlongation);
			trMax.append(" AND DATE_FORMAT(cmax.courrierDateReception,'%Y-%m-%d') < :dateprolongation ");
			pars.put("dateprolongation", dateprolongation);
		}
		else {
			trMax.append(" AND DATE_FORMAT(cmax.courrierDateReception,'%Y-%m-%d') <= :dateLimiteReception ");
			pars.put("dateLimiteReception", dateLimiteReception);
		}

		trMax.append(" GROUP BY dmax.dossierId"
				+ " ORDER BY dmax.dossierId DESC");
		if (maxResult != 0 && DBType.contains("mysql")) {
			trMax.append(" LIMIT " + firstIndex + ", " + maxResult);
		}

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		System.out.println("queryMax :" + queryMax);
		Iterator<String> iterMax = pars.keySet().iterator();
		System.out.println("iterMax ==>" + iterMax);
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		if (maxResult != 0 && DBType.contains("sqlserver")) {
			queryMax.setFirstResult(firstIndex);
			queryMax.setMaxResults(maxResult);
		}

		System.out.println("queryMax.getQueryString() : "
				+ queryMax.getQueryString());
		System.out.println("queryMax.list  :" + queryMax.list());
		List<Integer> maxIds = queryMax.list();

		Map<String, Object> params = new HashMap<String, Object>();
		int imax = 0;
		String where = " t.transactionId IN (";
		if (maxIds.size() > 0) {
			for (Integer ids : maxIds) {
				Integer idTrans = ids;
				where += ":idTrans" + imax + ", ";
				params.put("idTrans" + imax, idTrans);
				imax++;
			}
		} else {
			return new ArrayList<CourrierInformations>();
		}
		System.out.println("Liste :");
		StringBuffer hql = new StringBuffer(
				"SELECT DISTINCT c.idCourrier AS 'courrierID',"
						+ " c.courrierObjet AS 'courrierObjet',"
						+ " c.courrierReferenceCorrespondant AS 'courrierReference',"
						+ " c.courrierCommentaire AS 'courrierCommentaire',"
						+ " c.courrierOldNum AS 'courrierOldNum',"
						+ " c.courrierCircuit AS 'courrierCircuit',"
						+ " d.dossierId AS 'dossierID',"
						+ " n.natureLibelle AS 'courrierNature',"
						+ " t.etatId AS 'etatID',"
						+ " c.courrierDateReception AS 'courrierDateReceptionEnvoi',"
						+ " t.transactionDateConsultation AS 'transactionDateConsultation',"
						+ " t.transactionId AS 'transactionID',"
						+ " t.idExpDest AS 'expID',"
						+ " t.transactionOrdre AS 'transactionOrdre',"
						+ " t.idUtilisateur AS 'idUtilisateur',"
						+ " t_tdr.transactionDestinationReelleIdDestinataire AS 'transactionDestinationReelID',"
						+ " t_tdr.transactionDestinationReelleTypeDestinataire AS 'transactionDestinationReelleTypeDestinataire',"
						+ " e.typeExpDest AS 'expTypeOld',"
						+ " e.idExpDestLdap AS 'expLdapOld',"
						+ " ee.Exp_Dest_ExterneNom AS 'expNomOld',"
						+ " ee.Exp_Dest_ExternePrenom AS 'expPrenomOld',"
						+ " ee.typeUtilisateurId AS 'expTypeUserOld',"
						+ " tm.transactionId AS 'minTransactionID',"
						+ " em.typeExpDest AS 'expType',"
						+ " em.idExpDestLdap AS 'expLdap',"
						+ " eem.Exp_Dest_ExterneNom AS 'expNom',"
						+ " eem.Exp_Dest_ExternePrenom AS 'expPrenom',"
						+ " eem.typeUtilisateurId AS 'expTypeUser' ");

		
		System.out.println(hql.toString());
		hql.append(" FROM transactionn t"
				+ " INNER JOIN dossier d ON t.dossierId = d.dossierId"
				+ " INNER JOIN courrierdossier cd ON d.dossierId = cd.dossierId"
				+ " INNER JOIN courrier c ON cd.idCourrier = c.idCourrier"
				+ " INNER JOIN nature n ON c.idNature = n.natureId");

		hql.append(" LEFT JOIN transactiondest td ON t.transactionId = td.idTransaction"
				+ " LEFT JOIN expdest e ON t.idExpDest = e.idExpDest"
				+ " LEFT JOIN expdestexterne ee ON e.idExpDestExterne = ee.idExpDestExterne"
				+ " LEFT JOIN transactiondestinationreelle t_tdr ON t.transactionDestinationReelleId = t_tdr.transactionDestinationReelleId"
				+ " INNER JOIN transactionn tm ON t.transactionFirst = tm.transactionId"
				+ " LEFT JOIN expdest em ON tm.idExpDest = em.idExpDest"
				+ " LEFT JOIN expdestexterne eem ON em.idExpDestExterne = eem.idExpDestExterne"
				+ " WHERE d.typeDossierId = 1 AND "
				+ where.substring(0, where.length() - 2) + ")");

		// Order by Date
		hql.append(" ORDER BY c.idCourrier DESC");

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		query.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));

		query.addScalar("courrierID", new IntegerType());
		query.addScalar("courrierObjet", new StringType());
		query.addScalar("courrierReference", new StringType());
		query.addScalar("courrierCommentaire", new StringType());
		query.addScalar("courrierOldNum", new IntegerType());
		query.addScalar("courrierCircuit", new StringType());
		query.addScalar("dossierID", new IntegerType());
		query.addScalar("courrierNature", new StringType());
		query.addScalar("etatID", new IntegerType());
		query.addScalar("courrierDateReceptionEnvoi", new DateType());
		query.addScalar("transactionDateConsultation", new DateType());
		query.addScalar("transactionID", new IntegerType());
		query.addScalar("expID", new IntegerType());
		query.addScalar("transactionOrdre", new IntegerType());
		query.addScalar("idUtilisateur", new IntegerType());
		query.addScalar("transactionDestinationReelID", new IntegerType());
		query.addScalar("transactionDestinationReelleTypeDestinataire",
				new StringType());
		query.addScalar("expLdapOld", new IntegerType());
		query.addScalar("expTypeOld", new StringType());
		query.addScalar("expNomOld", new StringType());
		query.addScalar("expPrenomOld", new StringType());
		query.addScalar("expTypeUserOld", new IntegerType());
		query.addScalar("minTransactionID", new IntegerType());
		query.addScalar("expLdap", new IntegerType());
		query.addScalar("expType", new StringType());
		query.addScalar("expNom", new StringType());
		query.addScalar("expPrenom", new StringType());
		query.addScalar("expTypeUser", new IntegerType());

		Iterator<String> iter = params.keySet().iterator();
		while (iter.hasNext()) {
			System.out.println("AH  dans itr ");
			String name = iter.next();
			Object value = params.get(name);
			System.out.println(name + " = " + value);
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println(query.getQueryString());
		System.out.println("list :" + query.list());
		return query.list();
	}

	public List<CourrierInformations> findCourrierEnvoyerBOCRecuApresDelaisAO(
			int idaoConsultation, Date aoConsultationDateSeanceCommission,
			boolean descending, int firstIndex, int maxResult, String DBType) {
		Map<String, Object> pars = new HashMap<String, Object>();
		StringBuffer trMax = new StringBuffer(
				"SELECT MAX(tmax.transactionId) AS 'transactionId'"
						+ " FROM transactionn tmax, dossier dmax, courrierdossier cdmax, courrier cmax, transactionn tm"
						+ " WHERE cmax.idaoConsultation = " + idaoConsultation
						+ " AND cmax.idTransmission IN (2,5,6)"
						+ " AND tmax.dossierId = dmax.dossierId"
						+ " AND dmax.dossierId = cdmax.dossierId"
						+ " AND cdmax.idCourrier = cmax.idCourrier"
						+ " AND tmax.transactionFirst = tm.transactionId");

		SimpleDateFormat debut = new SimpleDateFormat("yyyy-MM-dd");
		String dateCom = debut.format(aoConsultationDateSeanceCommission);

		System.out.println(" KHA : par jour");
		trMax.append(" AND DATE_FORMAT(cmax.courrierDateReception,'%Y-%m-%d') > :dateCommission");
		System.out.println("transactionDateDebut = " + dateCom);
		pars.put("dateCommission", dateCom);

		trMax.append(" GROUP BY dmax.dossierId"
				+ " ORDER BY dmax.dossierId DESC");
		if (maxResult != 0 && DBType.contains("mysql")) {
			trMax.append(" LIMIT " + firstIndex + ", " + maxResult);
		}

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		System.out.println("queryMax :" + queryMax);
		Iterator<String> iterMax = pars.keySet().iterator();
		System.out.println("iterMax ==>" + iterMax);
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		if (maxResult != 0 && DBType.contains("sqlserver")) {
			queryMax.setFirstResult(firstIndex);
			queryMax.setMaxResults(maxResult);
		}

		System.out.println("queryMax.getQueryString() : "
				+ queryMax.getQueryString());
		System.out.println("queryMax.list  :" + queryMax.list());
		List<Integer> maxIds = queryMax.list();

		Map<String, Object> params = new HashMap<String, Object>();
		int imax = 0;
		String where = " t.transactionId IN (";
		if (maxIds.size() > 0) {
			for (Integer ids : maxIds) {
				Integer idTrans = ids;
				where += ":idTrans" + imax + ", ";
				params.put("idTrans" + imax, idTrans);
				imax++;
			}
		} else {
			return new ArrayList<CourrierInformations>();
		}
		System.out.println("Liste :");
		StringBuffer hql = new StringBuffer(
				"SELECT DISTINCT c.idCourrier AS 'courrierID',"
						+ " c.courrierObjet AS 'courrierObjet',"
						+ " c.courrierReferenceCorrespondant AS 'courrierReference',"
						+ " c.courrierCommentaire AS 'courrierCommentaire',"
						+ " c.courrierOldNum AS 'courrierOldNum',"
						+ " c.courrierCircuit AS 'courrierCircuit',"
						+ " d.dossierId AS 'dossierID',"
						+ " n.natureLibelle AS 'courrierNature',"
						+ " t.etatId AS 'etatID',"
						+ " c.courrierDateReception AS 'courrierDateReceptionEnvoi',"
						+ " t.transactionDateConsultation AS 'transactionDateConsultation',"
						+ " t.transactionId AS 'transactionID',"
						+ " t.idExpDest AS 'expID',"
						+ " t.transactionOrdre AS 'transactionOrdre',"
						+ " t.idUtilisateur AS 'idUtilisateur',"
						+ " t_tdr.transactionDestinationReelleIdDestinataire AS 'transactionDestinationReelID',"
						+ " t_tdr.transactionDestinationReelleTypeDestinataire AS 'transactionDestinationReelleTypeDestinataire',"
						+ " e.typeExpDest AS 'expTypeOld',"
						+ " e.idExpDestLdap AS 'expLdapOld',"
						+ " ee.Exp_Dest_ExterneNom AS 'expNomOld',"
						+ " ee.Exp_Dest_ExternePrenom AS 'expPrenomOld',"
						+ " ee.typeUtilisateurId AS 'expTypeUserOld',"
						+ " tm.transactionId AS 'minTransactionID',"
						+ " em.typeExpDest AS 'expType',"
						+ " em.idExpDestLdap AS 'expLdap',"
						+ " eem.Exp_Dest_ExterneNom AS 'expNom',"
						+ " eem.Exp_Dest_ExternePrenom AS 'expPrenom',"
						+ " eem.typeUtilisateurId AS 'expTypeUser' ");

		System.out.println(hql.toString());
		hql.append(" FROM transactionn t"
				+ " INNER JOIN dossier d ON t.dossierId = d.dossierId"
				+ " INNER JOIN courrierdossier cd ON d.dossierId = cd.dossierId"
				+ " INNER JOIN courrier c ON cd.idCourrier = c.idCourrier"
				+ " INNER JOIN nature n ON c.idNature = n.natureId");

		hql.append(" LEFT JOIN transactiondest td ON t.transactionId = td.idTransaction"
				+ " LEFT JOIN expdest e ON t.idExpDest = e.idExpDest"
				+ " LEFT JOIN expdestexterne ee ON e.idExpDestExterne = ee.idExpDestExterne"
				+ " LEFT JOIN transactiondestinationreelle t_tdr ON t.transactionDestinationReelleId = t_tdr.transactionDestinationReelleId"
				+ " INNER JOIN transactionn tm ON t.transactionFirst = tm.transactionId"
				+ " LEFT JOIN expdest em ON tm.idExpDest = em.idExpDest"
				+ " LEFT JOIN expdestexterne eem ON em.idExpDestExterne = eem.idExpDestExterne"
				+ " WHERE d.typeDossierId = 1 AND "
				+ where.substring(0, where.length() - 2) + ")");

		// Order by Date
		hql.append(" ORDER BY c.idCourrier DESC");

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		query.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));

		query.addScalar("courrierID", new IntegerType());
		query.addScalar("courrierObjet", new StringType());
		query.addScalar("courrierReference", new StringType());
		query.addScalar("courrierCommentaire", new StringType());
		query.addScalar("courrierOldNum", new IntegerType());
		query.addScalar("courrierCircuit", new StringType());
		query.addScalar("dossierID", new IntegerType());
		query.addScalar("courrierNature", new StringType());
		query.addScalar("etatID", new IntegerType());
		query.addScalar("courrierDateReceptionEnvoi", new DateType());
		query.addScalar("transactionDateConsultation", new DateType());
		query.addScalar("transactionID", new IntegerType());
		query.addScalar("expID", new IntegerType());
		query.addScalar("transactionOrdre", new IntegerType());
		query.addScalar("idUtilisateur", new IntegerType());
		query.addScalar("transactionDestinationReelID", new IntegerType());
		query.addScalar("transactionDestinationReelleTypeDestinataire",
				new StringType());
		query.addScalar("expLdapOld", new IntegerType());
		query.addScalar("expTypeOld", new StringType());
		query.addScalar("expNomOld", new StringType());
		query.addScalar("expPrenomOld", new StringType());
		query.addScalar("expTypeUserOld", new IntegerType());
		query.addScalar("minTransactionID", new IntegerType());
		query.addScalar("expLdap", new IntegerType());
		query.addScalar("expType", new StringType());
		query.addScalar("expNom", new StringType());
		query.addScalar("expPrenom", new StringType());
		query.addScalar("expTypeUser", new IntegerType());

		Iterator<String> iter = params.keySet().iterator();
		while (iter.hasNext()) {
			System.out.println("AH  dans itr ");
			String name = iter.next();
			Object value = params.get(name);
			System.out.println(name + " = " + value);
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println(query.getQueryString());
		System.out.println("list :" + query.list());
		return query.list();
	}

	public List<CourrierInformations> findCourrierEnvoyerBOCRecuApresDelaisAOPorteur(
			int idaoConsultation, Date aoConsultationDateSeanceCommission,
			boolean descending, int firstIndex, int maxResult, String DBType) {
		Map<String, Object> pars = new HashMap<String, Object>();
		StringBuffer trMax = new StringBuffer(
				"SELECT MAX(tmax.transactionId) AS 'transactionId'"
						+ " FROM transactionn tmax, dossier dmax, courrierdossier cdmax, courrier cmax, transactionn tm"
						+ " WHERE cmax.idaoConsultation = " + idaoConsultation
						+ " AND cmax.idTransmission = 1"
						+ " AND tmax.dossierId = dmax.dossierId"
						+ " AND dmax.dossierId = cdmax.dossierId"
						+ " AND cdmax.idCourrier = cmax.idCourrier"
						+ " AND tmax.transactionFirst = tm.transactionId");

		SimpleDateFormat debut = new SimpleDateFormat("yyyy-MM-dd");
		String dateCom = debut.format(aoConsultationDateSeanceCommission);
		System.out
				.println("##############Dans findCourrierEnvoyerBOCReçuApresDelaisAOPorteur");
		System.out.println(" KHA : par jour");
		trMax.append(" AND DATE_FORMAT(cmax.courrierDateReception,'%Y-%m-%d') > :dateCommission ");
		System.out.println("transactionDateDebut = " + dateCom);
		pars.put("dateCommission", dateCom);

		trMax.append(" GROUP BY dmax.dossierId"
				+ " ORDER BY dmax.dossierId DESC");
		if (maxResult != 0 && DBType.contains("mysql")) {
			trMax.append(" LIMIT " + firstIndex + ", " + maxResult);
		}

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		System.out.println("queryMax :" + queryMax);
		Iterator<String> iterMax = pars.keySet().iterator();
		System.out.println("iterMax ==>" + iterMax);
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		if (maxResult != 0 && DBType.contains("sqlserver")) {
			queryMax.setFirstResult(firstIndex);
			queryMax.setMaxResults(maxResult);
		}

		System.out.println("queryMax.getQueryString() : "
				+ queryMax.getQueryString());
		System.out.println("queryMax.list  :" + queryMax.list());
		List<Integer> maxIds = queryMax.list();

		Map<String, Object> params = new HashMap<String, Object>();
		int imax = 0;
		String where = " t.transactionId IN (";
		if (maxIds.size() > 0) {
			for (Integer ids : maxIds) {
				Integer idTrans = ids;
				where += ":idTrans" + imax + ", ";
				params.put("idTrans" + imax, idTrans);
				imax++;
			}
		} else {
			return new ArrayList<CourrierInformations>();
		}
		System.out.println("Liste :");
		StringBuffer hql = new StringBuffer(
				"SELECT DISTINCT c.idCourrier AS 'courrierID',"
						+ " c.courrierObjet AS 'courrierObjet',"
						+ " c.courrierReferenceCorrespondant AS 'courrierReference',"
						+ " c.courrierCommentaire AS 'courrierCommentaire',"
						+ " c.courrierOldNum AS 'courrierOldNum',"
						+ " c.courrierCircuit AS 'courrierCircuit',"
						+ " d.dossierId AS 'dossierID',"
						+ " n.natureLibelle AS 'courrierNature',"
						+ " t.etatId AS 'etatID',"
						+ " c.courrierDateReception AS 'courrierDateReceptionEnvoi',"
						+ " t.transactionDateConsultation AS 'transactionDateConsultation',"
						+ " t.transactionId AS 'transactionID',"
						+ " t.idExpDest AS 'expID',"
						+ " t.transactionOrdre AS 'transactionOrdre',"
						+ " t.idUtilisateur AS 'idUtilisateur',"
						+ " t_tdr.transactionDestinationReelleIdDestinataire AS 'transactionDestinationReelID',"
						+ " t_tdr.transactionDestinationReelleTypeDestinataire AS 'transactionDestinationReelleTypeDestinataire',"
						+ " e.typeExpDest AS 'expTypeOld',"
						+ " e.idExpDestLdap AS 'expLdapOld',"
						+ " ee.Exp_Dest_ExterneNom AS 'expNomOld',"
						+ " ee.Exp_Dest_ExternePrenom AS 'expPrenomOld',"
						+ " ee.typeUtilisateurId AS 'expTypeUserOld',"
						+ " tm.transactionId AS 'minTransactionID',"
						+ " em.typeExpDest AS 'expType',"
						+ " em.idExpDestLdap AS 'expLdap',"
						+ " eem.Exp_Dest_ExterneNom AS 'expNom',"
						+ " eem.Exp_Dest_ExternePrenom AS 'expPrenom',"
						+ " eem.typeUtilisateurId AS 'expTypeUser' ");

		
		System.out.println(hql.toString());
		hql.append(" FROM transactionn t"
				+ " INNER JOIN dossier d ON t.dossierId = d.dossierId"
				+ " INNER JOIN courrierdossier cd ON d.dossierId = cd.dossierId"
				+ " INNER JOIN courrier c ON cd.idCourrier = c.idCourrier"
				+ " INNER JOIN nature n ON c.idNature = n.natureId");

		hql.append(" LEFT JOIN transactiondest td ON t.transactionId = td.idTransaction"
				+ " LEFT JOIN expdest e ON t.idExpDest = e.idExpDest"
				+ " LEFT JOIN expdestexterne ee ON e.idExpDestExterne = ee.idExpDestExterne"
				+ " LEFT JOIN transactiondestinationreelle t_tdr ON t.transactionDestinationReelleId = t_tdr.transactionDestinationReelleId"
				+ " INNER JOIN transactionn tm ON t.transactionFirst = tm.transactionId"
				+ " LEFT JOIN expdest em ON tm.idExpDest = em.idExpDest"
				+ " LEFT JOIN expdestexterne eem ON em.idExpDestExterne = eem.idExpDestExterne"
				+ " WHERE d.typeDossierId = 1 AND "
				+ where.substring(0, where.length() - 2) + ")");

		// Order by Date
		hql.append(" ORDER BY c.idCourrier DESC");

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		query.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));

		query.addScalar("courrierID", new IntegerType());
		query.addScalar("courrierObjet", new StringType());
		query.addScalar("courrierReference", new StringType());
		query.addScalar("courrierCommentaire", new StringType());
		query.addScalar("courrierOldNum", new IntegerType());
		query.addScalar("courrierCircuit", new StringType());
		query.addScalar("dossierID", new IntegerType());
		query.addScalar("courrierNature", new StringType());
		query.addScalar("etatID", new IntegerType());
		query.addScalar("courrierDateReceptionEnvoi", new DateType());
		query.addScalar("transactionDateConsultation", new DateType());
		query.addScalar("transactionID", new IntegerType());
		query.addScalar("expID", new IntegerType());
		query.addScalar("transactionOrdre", new IntegerType());
		query.addScalar("idUtilisateur", new IntegerType());
		query.addScalar("transactionDestinationReelID", new IntegerType());
		query.addScalar("transactionDestinationReelleTypeDestinataire",
				new StringType());
		query.addScalar("expLdapOld", new IntegerType());
		query.addScalar("expTypeOld", new StringType());
		query.addScalar("expNomOld", new StringType());
		query.addScalar("expPrenomOld", new StringType());
		query.addScalar("expTypeUserOld", new IntegerType());
		query.addScalar("minTransactionID", new IntegerType());
		query.addScalar("expLdap", new IntegerType());
		query.addScalar("expType", new StringType());
		query.addScalar("expNom", new StringType());
		query.addScalar("expPrenom", new StringType());
		query.addScalar("expTypeUser", new IntegerType());

		Iterator<String> iter = params.keySet().iterator();
		while (iter.hasNext()) {
			System.out.println("AH  dans itr ");
			String name = iter.next();
			Object value = params.get(name);
			System.out.println(name + " = " + value);
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println(query.getQueryString());
		System.out.println("list :" + query.list());
		return query.list();
	}

	public List<CourrierInformations> findCourrierEnvoyerBOCRecuSansReferenceAO(Date dateDebut, Date dateFin,String annee, String moi,
			 boolean descending, int firstIndex,
			int maxResult, String DBType) {
		Map<String, Object> pars = new HashMap<String, Object>();
		StringBuffer trMax = new StringBuffer(
				"SELECT MAX(tmax.transactionId) AS 'transactionId'"
				+ " FROM transactionn tmax, dossier dmax, courrierdossier cdmax, courrier cmax, transactionn tm"
				+ " WHERE cmax.idaoConsultation is null "
				+ " AND cmax.idTransmission IN (2,5,6)"
				+ " AND cmax.idNature IN (44,46)"
				+ " AND tmax.dossierId = dmax.dossierId"
				+ " AND dmax.dossierId = cdmax.dossierId"
				+ " AND cdmax.idCourrier = cmax.idCourrier"
				+ " AND tmax.transactionFirst = tm.transactionId");
		System.out.println("##### dateDebut == " + dateDebut);
		System.out.println("##### dateFin == " + dateFin);
		System.out.println("##### annee " + annee);
		System.out.println("##### moi " + moi);
		if (dateDebut != null && dateFin != null) {		
			System.out.println("##### dans periode");
			trMax.append(" AND DATE_FORMAT(tmax.transactionDateTransaction,'%Y-%m-%d') BETWEEN :dateDebut AND :dateFin");
			SimpleDateFormat formatDebut = new SimpleDateFormat("yyyy-MM-dd");
			String dateD = formatDebut.format(dateDebut);
			String dateF = formatDebut.format(dateFin);
			pars.put("dateDebut", dateD);
			pars.put("dateFin", dateF);
		}else if(dateFin == null && dateDebut != null){
			System.out.println("##### dans jour ");
			trMax.append(" AND DATE_FORMAT(tmax.transactionDateTransaction,'%Y-%m-%d')=:dateDebut");
			SimpleDateFormat formatDebut = new SimpleDateFormat("yyyy-MM-dd");
			String dateD = formatDebut.format(dateDebut);
			pars.put("dateDebut", dateD);
		}else if(dateDebut == null && dateFin != null){
			System.out.println("##### dans Annee ");
			int year =Integer.valueOf(annee);
			System.out.println("##### vb.getChoixAnnee()== " + annee);
			System.out.println("##### dans annee ");
			trMax.append(" AND tmax.courrierDateReceptionAnnee=:year");
			pars.put("year", year);
		}else{
			System.out.println("##### dans Moi ");
			int month =Integer.valueOf(moi);
			System.out.println("##### vb.getChoixAnnee()== " + month);
			System.out.println("##### dans annee ");
			trMax.append(" AND cmax.courrierDateReceptionMois=:moi");
			trMax.append(" AND cmax.courrierDateReceptionAnnee=:annee");
			pars.put("moi", moi);
			pars.put("annee", annee);
		}
		trMax.append(" GROUP BY dmax.dossierId"
				+ " ORDER BY dmax.dossierId DESC");
		if (maxResult != 0 && DBType.contains("mysql")) {
			trMax.append(" LIMIT " + firstIndex + ", " + maxResult);
		}
		
		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		System.out.println("queryMax :" + queryMax);
		Iterator<String> iterMax = pars.keySet().iterator();
		System.out.println("iterMax ==>" + iterMax);
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		if (maxResult != 0 && DBType.contains("sqlserver")) {
			queryMax.setFirstResult(firstIndex);
			queryMax.setMaxResults(maxResult);
		}

		System.out.println("queryMax.getQueryString() : "
				+ queryMax.getQueryString());
		System.out.println("queryMax.list  :" + queryMax.list());
		List<Integer> maxIds = queryMax.list();

		Map<String, Object> params = new HashMap<String, Object>();
		int imax = 0;
		String where = " t.transactionId IN (";
		if (maxIds.size() > 0) {
			for (Integer ids : maxIds) {
				Integer idTrans = ids;
				where += ":idTrans" + imax + ", ";
				params.put("idTrans" + imax, idTrans);
				imax++;
			}
		} else {
			return new ArrayList<CourrierInformations>();
		}
		System.out.println("Liste :");
		StringBuffer hql = new StringBuffer(
				"SELECT DISTINCT c.idCourrier AS 'courrierID',"
						+ " c.courrierObjet AS 'courrierObjet',"
						+ " c.courrierReferenceCorrespondant AS 'courrierReference',"
						+ " c.courrierCommentaire AS 'courrierCommentaire',"
						+ " c.courrierOldNum AS 'courrierOldNum',"
						+ " c.courrierCircuit AS 'courrierCircuit',"
						+ " d.dossierId AS 'dossierID',"
						+ " n.natureLibelle AS 'courrierNature',"
						+ " t.etatId AS 'etatID',"
						+ " c.courrierDateReception AS 'courrierDateReceptionEnvoi',"
						+ " t.transactionDateConsultation AS 'transactionDateConsultation',"
						+ " t.transactionId AS 'transactionID',"
						+ " t.idExpDest AS 'expID',"
						+ " t.transactionOrdre AS 'transactionOrdre',"
						+ " t.idUtilisateur AS 'idUtilisateur',"
						+ " t_tdr.transactionDestinationReelleIdDestinataire AS 'transactionDestinationReelID',"
						+ " t_tdr.transactionDestinationReelleTypeDestinataire AS 'transactionDestinationReelleTypeDestinataire',"
						+ " e.typeExpDest AS 'expTypeOld',"
						+ " e.idExpDestLdap AS 'expLdapOld',"
						+ " ee.Exp_Dest_ExterneNom AS 'expNomOld',"
						+ " ee.Exp_Dest_ExternePrenom AS 'expPrenomOld',"
						+ " ee.typeUtilisateurId AS 'expTypeUserOld',"
						+ " tm.transactionId AS 'minTransactionID',"
						+ " em.typeExpDest AS 'expType',"
						+ " em.idExpDestLdap AS 'expLdap',"
						+ " eem.Exp_Dest_ExterneNom AS 'expNom',"
						+ " eem.Exp_Dest_ExternePrenom AS 'expPrenom',"
						+ " eem.typeUtilisateurId AS 'expTypeUser' ");

		
		System.out.println(hql.toString());
		hql.append(" FROM transactionn t"
				+ " INNER JOIN dossier d ON t.dossierId = d.dossierId"
				+ " INNER JOIN courrierdossier cd ON d.dossierId = cd.dossierId"
				+ " INNER JOIN courrier c ON cd.idCourrier = c.idCourrier"
				+ " INNER JOIN nature n ON c.idNature = n.natureId");

		hql.append(" LEFT JOIN transactiondest td ON t.transactionId = td.idTransaction"
				+ " LEFT JOIN expdest e ON t.idExpDest = e.idExpDest"
				+ " LEFT JOIN expdestexterne ee ON e.idExpDestExterne = ee.idExpDestExterne"
				+ " LEFT JOIN transactiondestinationreelle t_tdr ON t.transactionDestinationReelleId = t_tdr.transactionDestinationReelleId"
				+ " INNER JOIN transactionn tm ON t.transactionFirst = tm.transactionId"
				+ " LEFT JOIN expdest em ON tm.idExpDest = em.idExpDest"
				+ " LEFT JOIN expdestexterne eem ON em.idExpDestExterne = eem.idExpDestExterne"
				+ " WHERE d.typeDossierId = 1 AND "
				+ where.substring(0, where.length() - 2) + ")");

		// Order by Date
		hql.append(" ORDER BY c.idCourrier DESC");

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		query.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));

		query.addScalar("courrierID", new IntegerType());
		query.addScalar("courrierObjet", new StringType());
		query.addScalar("courrierReference", new StringType());
		query.addScalar("courrierCommentaire", new StringType());
		query.addScalar("courrierOldNum", new IntegerType());
		query.addScalar("courrierCircuit", new StringType());
		query.addScalar("dossierID", new IntegerType());
		query.addScalar("courrierNature", new StringType());
		query.addScalar("etatID", new IntegerType());
		query.addScalar("courrierDateReceptionEnvoi", new DateType());
		query.addScalar("transactionDateConsultation", new DateType());
		query.addScalar("transactionID", new IntegerType());
		query.addScalar("expID", new IntegerType());
		query.addScalar("transactionOrdre", new IntegerType());
		query.addScalar("idUtilisateur", new IntegerType());
		query.addScalar("transactionDestinationReelID", new IntegerType());
		query.addScalar("transactionDestinationReelleTypeDestinataire",
				new StringType());
		query.addScalar("expLdapOld", new IntegerType());
		query.addScalar("expTypeOld", new StringType());
		query.addScalar("expNomOld", new StringType());
		query.addScalar("expPrenomOld", new StringType());
		query.addScalar("expTypeUserOld", new IntegerType());
		query.addScalar("minTransactionID", new IntegerType());
		query.addScalar("expLdap", new IntegerType());
		query.addScalar("expType", new StringType());
		query.addScalar("expNom", new StringType());
		query.addScalar("expPrenom", new StringType());
		query.addScalar("expTypeUser", new IntegerType());

		Iterator<String> iter = params.keySet().iterator();
		while (iter.hasNext()) {
			System.out.println("AH  dans itr ");
			String name = iter.next();
			Object value = params.get(name);
			System.out.println(name + " = " + value);
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println(query.getQueryString());
		System.out.println("list :" + query.list());
		return query.list();
	}

	public List<CourrierInformations> findCourrierEnvoyerBOCRecuSansReferenceAOPorteur(Date dateDebut, Date dateFin,String annee, String moi,boolean descending, int firstIndex,
			int maxResult, String DBType) {
		Map<String, Object> pars = new HashMap<String, Object>();
		StringBuffer trMax = new StringBuffer(
				"SELECT MAX(tmax.transactionId) AS 'transactionId'"
				+ " FROM transactionn tmax, dossier dmax, courrierdossier cdmax, courrier cmax, transactionn tm"
				+ " WHERE cmax.idaoConsultation is null "
				+ " AND cmax.idTransmission = 1"
				+ " AND cmax.idNature IN (44,46)"
				+ " AND tmax.dossierId = dmax.dossierId"
				+ " AND dmax.dossierId = cdmax.dossierId"
				+ " AND cdmax.idCourrier = cmax.idCourrier"
				+ " AND tmax.transactionFirst = tm.transactionId");
		System.out.println("##### dateDebut == " + dateDebut);
		System.out.println("##### dateFin == " + dateFin);
		System.out.println("##### vb.getChoixAnnee()== " + vb.getChoixAnnee());
		if (dateDebut != null && dateFin != null) {		
			System.out.println("##### dans periode");
			trMax.append(" AND DATE_FORMAT(tmax.transactionDateTransaction,'%Y-%m-%d') BETWEEN :dateDebut AND :dateFin");
			SimpleDateFormat formatDebut = new SimpleDateFormat("yyyy-MM-dd");
			String dateD = formatDebut.format(dateDebut);
			String dateF = formatDebut.format(dateFin);
			pars.put("dateDebut", dateD);
			pars.put("dateFin", dateF);
		}else if(dateFin == null && dateDebut != null){
			System.out.println("##### dans jour ");
			trMax.append(" AND DATE_FORMAT(tmax.transactionDateTransaction,'%Y-%m-%d')=:dateDebut");
			SimpleDateFormat formatDebut = new SimpleDateFormat("yyyy-MM-dd");
			String dateD = formatDebut.format(dateDebut);
			pars.put("dateDebut", dateD);
		}else if(dateDebut == null && dateFin != null){
			System.out.println("##### dans Annee ");
			int year =Integer.valueOf(annee);
			System.out.println("##### vb.getChoixAnnee()== " + annee);
			System.out.println("##### dans annee ");
			trMax.append(" AND tmax.courrierDateReceptionAnnee=:year");
			pars.put("year", year);
		}else{
			System.out.println("##### dans Moi ");
			int month =Integer.valueOf(moi);
			System.out.println("##### vb.getChoixAnnee()== " + month);
			System.out.println("##### dans annee ");
			trMax.append(" AND cmax.courrierDateReceptionMois=:moi");
			trMax.append(" AND cmax.courrierDateReceptionAnnee=:annee");
			pars.put("moi", moi);
			pars.put("annee", annee);
		}
		trMax.append(" GROUP BY dmax.dossierId"
				+ " ORDER BY dmax.dossierId DESC");
		if (maxResult != 0 && DBType.contains("mysql")) {
			trMax.append(" LIMIT " + firstIndex + ", " + maxResult);
		}
		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		System.out.println("queryMax :" + queryMax);
		Iterator<String> iterMax = pars.keySet().iterator();
		System.out.println("iterMax ==>" + iterMax);
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		if (maxResult != 0 && DBType.contains("sqlserver")) {
			queryMax.setFirstResult(firstIndex);
			queryMax.setMaxResults(maxResult);
		}

		System.out.println("queryMax.getQueryString() : "
				+ queryMax.getQueryString());
		System.out.println("queryMax.list  :" + queryMax.list());
		List<Integer> maxIds = queryMax.list();

		Map<String, Object> params = new HashMap<String, Object>();
		int imax = 0;
		String where = " t.transactionId IN (";
		if (maxIds.size() > 0) {
			for (Integer ids : maxIds) {
				Integer idTrans = ids;
				where += ":idTrans" + imax + ", ";
				params.put("idTrans" + imax, idTrans);
				imax++;
			}
		} else {
			return new ArrayList<CourrierInformations>();
		}
		System.out.println("Liste :");
		StringBuffer hql = new StringBuffer(
				"SELECT DISTINCT c.idCourrier AS 'courrierID',"
						+ " c.courrierObjet AS 'courrierObjet',"
						+ " c.courrierReferenceCorrespondant AS 'courrierReference',"
						+ " c.courrierCommentaire AS 'courrierCommentaire',"
						+ " c.courrierOldNum AS 'courrierOldNum',"
						+ " c.courrierCircuit AS 'courrierCircuit',"
						+ " d.dossierId AS 'dossierID',"
						+ " n.natureLibelle AS 'courrierNature',"
						+ " t.etatId AS 'etatID',"
						+ " c.courrierDateReception AS 'courrierDateReceptionEnvoi',"
						+ " t.transactionDateConsultation AS 'transactionDateConsultation',"
						+ " t.transactionId AS 'transactionID',"
						+ " t.idExpDest AS 'expID',"
						+ " t.transactionOrdre AS 'transactionOrdre',"
						+ " t.idUtilisateur AS 'idUtilisateur',"
						+ " t_tdr.transactionDestinationReelleIdDestinataire AS 'transactionDestinationReelID',"
						+ " t_tdr.transactionDestinationReelleTypeDestinataire AS 'transactionDestinationReelleTypeDestinataire',"
						+ " e.typeExpDest AS 'expTypeOld',"
						+ " e.idExpDestLdap AS 'expLdapOld',"
						+ " ee.Exp_Dest_ExterneNom AS 'expNomOld',"
						+ " ee.Exp_Dest_ExternePrenom AS 'expPrenomOld',"
						+ " ee.typeUtilisateurId AS 'expTypeUserOld',"
						+ " tm.transactionId AS 'minTransactionID',"
						+ " em.typeExpDest AS 'expType',"
						+ " em.idExpDestLdap AS 'expLdap',"
						+ " eem.Exp_Dest_ExterneNom AS 'expNom',"
						+ " eem.Exp_Dest_ExternePrenom AS 'expPrenom',"
						+ " eem.typeUtilisateurId AS 'expTypeUser' ");

		System.out.println(hql.toString());
		hql.append(" FROM transactionn t"
				+ " INNER JOIN dossier d ON t.dossierId = d.dossierId"
				+ " INNER JOIN courrierdossier cd ON d.dossierId = cd.dossierId"
				+ " INNER JOIN courrier c ON cd.idCourrier = c.idCourrier"
				+ " INNER JOIN nature n ON c.idNature = n.natureId");

		hql.append(" LEFT JOIN transactiondest td ON t.transactionId = td.idTransaction"
				+ " LEFT JOIN expdest e ON t.idExpDest = e.idExpDest"
				+ " LEFT JOIN expdestexterne ee ON e.idExpDestExterne = ee.idExpDestExterne"
				+ " LEFT JOIN transactiondestinationreelle t_tdr ON t.transactionDestinationReelleId = t_tdr.transactionDestinationReelleId"
				+ " INNER JOIN transactionn tm ON t.transactionFirst = tm.transactionId"
				+ " LEFT JOIN expdest em ON tm.idExpDest = em.idExpDest"
				+ " LEFT JOIN expdestexterne eem ON em.idExpDestExterne = eem.idExpDestExterne"
				+ " WHERE d.typeDossierId = 1 AND "
				+ where.substring(0, where.length() - 2) + ")");

		// Order by Date
		hql.append(" ORDER BY c.idCourrier DESC");

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		query.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));

		query.addScalar("courrierID", new IntegerType());
		query.addScalar("courrierObjet", new StringType());
		query.addScalar("courrierReference", new StringType());
		query.addScalar("courrierCommentaire", new StringType());
		query.addScalar("courrierOldNum", new IntegerType());
		query.addScalar("courrierCircuit", new StringType());
		query.addScalar("dossierID", new IntegerType());
		query.addScalar("courrierNature", new StringType());
		query.addScalar("etatID", new IntegerType());
		query.addScalar("courrierDateReceptionEnvoi", new DateType());
		query.addScalar("transactionDateConsultation", new DateType());
		query.addScalar("transactionID", new IntegerType());
		query.addScalar("expID", new IntegerType());
		query.addScalar("transactionOrdre", new IntegerType());
		query.addScalar("idUtilisateur", new IntegerType());
		query.addScalar("transactionDestinationReelID", new IntegerType());
		query.addScalar("transactionDestinationReelleTypeDestinataire",
				new StringType());
		query.addScalar("expLdapOld", new IntegerType());
		query.addScalar("expTypeOld", new StringType());
		query.addScalar("expNomOld", new StringType());
		query.addScalar("expPrenomOld", new StringType());
		query.addScalar("expTypeUserOld", new IntegerType());
		query.addScalar("minTransactionID", new IntegerType());
		query.addScalar("expLdap", new IntegerType());
		query.addScalar("expType", new StringType());
		query.addScalar("expNom", new StringType());
		query.addScalar("expPrenom", new StringType());
		query.addScalar("expTypeUser", new IntegerType());

		Iterator<String> iter = params.keySet().iterator();
		while (iter.hasNext()) {
			System.out.println("AH  dans itr ");
			String name = iter.next();
			Object value = params.get(name);
			System.out.println(name + " = " + value);
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println(query.getQueryString());
		System.out.println("list :" + query.list());
		return query.list();
	}

	public VariableGlobale getVb() {
		return vb;
	}

	public void setVb(VariableGlobale vb) {
		this.vb = vb;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getTransactionByIdDossierPourSuivi(int refDossier) {
		//String reqHql 
		
		StringBuffer hql = new StringBuffer( "Select trans.transactionId FROM transactionn trans " +
				" where trans.transactionId IN (Select A1.transactionId " +
				" From (SELECT t.transactionId, td.transactionDestTypeIntervenant " +
				" FROM transactionn t, transactiondest td WHERE t.dossierId= "+ refDossier +
				" AND t.transactionId=td.idTransaction order by t.transactionId Desc) A1 " +
				" group by A1.transactionDestTypeIntervenant)");
			
		
		SQLQuery query = getSession().createSQLQuery(hql.toString());
		
//		query.setResultTransformer(Transformers
//				.aliasToBean(Transaction.class));
//		
//		
//		query.addScalar("courrierID", new IntegerType());
//		
//		
//		System.out.println(query.getQueryString());
	System.out.println("query SUIVI : "+query);

		return query.list();

	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getTransactionByIdDossierPourSuiviDestExterne(int refDossier) {
		//String reqHql 
		
		StringBuffer hql = new StringBuffer( "Select trans.transactionId FROM transactionn trans " +
				" where trans.transactionId IN (Select A1.transactionId " +
				" From (SELECT t.transactionId, td.transactionDestTypeIntervenant " +
				" FROM transactionn t, transactiondest td WHERE t.dossierId= "+ refDossier +
				" AND t.transactionId=td.idTransaction order by t.transactionId Desc) A1 )");
			
		
		SQLQuery query = getSession().createSQLQuery(hql.toString());
		
//		query.setResultTransformer(Transformers
//				.aliasToBean(Transaction.class));
//		
//		
//		query.addScalar("courrierID", new IntegerType());
//		
//		
		System.out.println("affiche query "+query.getQueryString());
//	System.out.println("query  ::::::: "+query);

		return query.list();

	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CourrierInformations> findCourrierEnvoyerANDRecuWithCriterionLies(
			boolean isResponsable, List<Integer> listIdsSousUnites,
			List<Integer> listIdSubordonnes, HashMap<String, Object> filterMap,
			String sortField, boolean descending, String secretaire,
			String sub, String unit, int jourOrAutre, Date dateDebut,
			Date dateFin, String type, String type1, String typeSecretaire,
			Integer idUser, Integer typeTransmission, String stateTraitement,
			int firstIndex, int maxResult, boolean forRapport,
			Integer courrierRubriqueId, String typeCourrier, String DBType,
			Integer idCourrierCourant, int flagueCloture, int flagInterne) {
		System.out.println("flagueCloture = " + flagueCloture);
		System.out.println("############ flagInterne == " + flagInterne);
		long startTime = System.currentTimeMillis();
		Map<String, Object> pars = new HashMap<String, Object>();
		List<String> listRecu = new ArrayList<String>();
		List<String> listEnv = new ArrayList<String>();
		List<Integer> listEnvInteger = new ArrayList<Integer>();
		// System.out.println("typeTransmission : "+typeTransmission);
		// System.out.println("stateTraitement : "+stateTraitement);
		// System.out.println("firstIndex : "+firstIndex);
		// System.out.println("maxResult : "+maxResult);
		// System.out.println("forRapport : "+forRapport);
		// System.out.println("courrierRubriqueId : "+courrierRubriqueId);
		// System.out.println("typeCourrier : "+typeCourrier);
		// System.out.println("idCourrierCourant : "+idCourrierCourant);
		// System.out.println("jourOrAutre : "+jourOrAutre);
		// ****** Courrier Recu
		if (typeCourrier.equals("Recu") || stateTraitement.equals("Avalider")
				|| typeCourrier.equals("Tous")) {

			if (isResponsable) {
				switch (courrierRubriqueId) {
				case 1:
					listRecu.add(type1);
					break;
				case 2:
					listRecu.add(type);
					break;
				case 3:
					if (listIdSubordonnes != null
							&& listIdSubordonnes.size() > 0) {
						for (Integer idSub : listIdSubordonnes) {
							listRecu.add("sub_" + idSub);
						}
					} else {
						listRecu.add("sub_xx99999xx");
					}
					break;
				case 4:
					listRecu.add(typeSecretaire);
					break;
				case 5:
					if (listIdsSousUnites != null
							&& listIdsSousUnites.size() > 0) {
						for (Integer idSousUnit : listIdsSousUnites) {
							listRecu.add("unit_" + idSousUnit);
						}
					} else {
						System.out.println("PAS DES SOUS-UNITES");
						listRecu.add("unit_xx99999xx");
					}
					break;
				case 6:

					for (Integer idSub : listIdSubordonnes) {
						listRecu.add("sub_" + idSub);
					}
					for (Integer idSousUnit : listIdsSousUnites) {
						listRecu.add("unit_" + idSousUnit);
					}
					if (secretaire != null) {
						if (secretaire.equals("Oui")) {
							listRecu.add(typeSecretaire);
						}
					}
					if (type != null) {
						listRecu.add(type);
					}
					if (type1 != null && type1.length()>0) {
						listRecu.add(type1);
					}
				}
			} else {
				listRecu.add(type);
				listRecu.add(type1);
			}
		}
		System.out.println("2020 type =>" + type);
		System.out.println("2020 type1 =>" + type1);
		System.out.println("size lisre REcu " + listRecu.size());
		// ****** Courrier Envoye
		if (typeCourrier.equals("Envoyes") || typeCourrier.equals("Tous")
				|| typeCourrier.equals("Enveloppe")) {
			if (isResponsable) {
				System.out.println("courrierRubriqueId =======2=====> : "
						+ courrierRubriqueId);

				switch (courrierRubriqueId) {
				case 1:
					listEnv.add(type1);
					break;
				case 2:
					listEnv.add(type);
					break;
				case 3:

					if (!listIdSubordonnes.isEmpty()) {
						for (Integer idSub : listIdSubordonnes) {
							listEnv.add("sub_" + idSub);
						}
					} else {
						listEnv.add("sub_xx99999xx");
					}
					break;
				case 4:
					listEnv.add(typeSecretaire);
					break;
				case 5:
					if (listIdsSousUnites != null
							&& listIdsSousUnites.size() > 0) {
						for (Integer idSousUnit : listIdsSousUnites) {
							listEnv.add("unit_" + idSousUnit);
						}
					} else {
						System.out.println("PAS DES SOUS-UNITES");
						listEnv.add("unit_xx99999xx");
					}
					break;
				case 6:
					for (Integer idSub : listIdSubordonnes) {
						listEnv.add("sub_" + idSub);
					}
					for (Integer idSousUnit : listIdsSousUnites) {
						listEnv.add("unit_" + idSousUnit);
					}
					if (secretaire != null) {
						if (secretaire.equals("Oui")) {
							listEnv.add(typeSecretaire);
						}
					}
					if (type != null) {
						listEnv.add(type);
					}
					if (type1 != null && type1.length()>0) {
						listEnv.add(type1);
					}
				case 99:
					for (Integer idSousUnit : listIdsSousUnites) {
						listEnvInteger.add(idSousUnit);
					}
					break;

				}
			} else {
				listEnv.add(type);
				listEnv.add(type1);
			}
		}
		StringBuffer trMax;
		if (typeCourrier.equals("Enveloppe")) {
			trMax = new StringBuffer(
					"SELECT MAX(tmax.transactionId)"
							+ // , dmax.dossierId
							" FROM transactionn tmax"
							+ " INNER JOIN dossier dmax ON tmax.dossierId = dmax.dossierId"
							+ " INNER JOIN courrierdossier cdmax ON dmax.dossierId = cdmax.dossierId"
							+ " INNER JOIN courrier cmax ON cdmax.idCourrier = cmax.idCourrier"
							+ " INNER JOIN transactiondestinationreelle tdreel ON tdreel.transactionDestinationReelleId=tmax.transactionDestinationReelleId"
							+ " LEFT JOIN transactiondest tdmax ON tmax.transactionId = tdmax.idTransaction"
							+ " WHERE dmax.typeDossierId = 1");

		} else {
			trMax = new StringBuffer(
					"SELECT MAX(tmax.transactionId)"
							+ // , dmax.dossierId
							" FROM transactionn tmax"
							+ " INNER JOIN dossier dmax ON tmax.dossierId = dmax.dossierId"
							+ " INNER JOIN courrierdossier cdmax ON dmax.dossierId = cdmax.dossierId"
							+ " INNER JOIN courrier cmax ON cdmax.idCourrier = cmax.idCourrier"
							+ " LEFT JOIN transactiondest tdmax ON tmax.transactionId = tdmax.idTransaction"
							+ " WHERE dmax.typeDossierId = 1");

		}

		System.out.println("jourOrAutre  ==  " + jourOrAutre);
		System.out.println("vb.getSelectedListCourrier()  ==  "
				+ vb.getSelectedListCourrier());
		// if (vb.getSelectedListCourrier().equals("CRmois")){
		// jourOrAutre= 4;
		//
		// }
		// if (vb.getSelectedListCourrier().equals("CRannee")){
		// jourOrAutre=3;
		//
		// }
		if (flagueCloture == 4) {

			trMax.append(" AND cmax.courrierEtatCloture =1");
		}

		if (jourOrAutre == 1) {
			trMax.append(" AND tmax.transactionDateTransaction BETWEEN :dateDebut AND :dateFin");
			pars.put("dateDebut", dateDebut);
			pars.put("dateFin", dateFin);
		}
		if (jourOrAutre == 2) {
			trMax.append(" AND tmax.transactionDateTransaction < :dateDebut");
			pars.put("dateDebut", dateDebut);
		}
		// -------------------------------------------MM-----------------------------------------------
		// ------------------------------------debut changement
		// optimisation---------------------------
		// Calendar calendar = Calendar.getInstance();
		// Date dateAujourd = calendar.getTime();
		// par année
		if (jourOrAutre == 3) {
			System.out.println("#########Dans if (jourOrAutre == 3)");
			trMax.append(" AND cmax.courrierDateReceptionAnnee =:dateDebut");

			pars.put("dateDebut", dateDebut.getYear() + 1900);
		}
		// par moi
		if (jourOrAutre == 4) {
			System.out.println("#########Dans if (jourOrAutre == 4)");
			trMax.append(" AND cmax.courrierDateReceptionAnnee =:anneeEnCours AND cmax.courrierDateReceptionMois =:moisEnCours");
			pars.put("moisEnCours", dateDebut.getMonth() + 1);
			pars.put("anneeEnCours", dateDebut.getYear() + 1900);
		}
		// 2019-06-24
		// par jour
		if (jourOrAutre == 5) {
			System.out.println("#########Dans if (jourOrAutre == 5)");
			trMax.append(" AND DATE_FORMAT(cmax.courrierDateReception,'%Y-%m-%d') =:dateDebut ");
			// JS :Formatter date Début **********************************
			System.out.println("Date Début Avant formattage :" + dateDebut);
			SimpleDateFormat formatDebut = new SimpleDateFormat("yyyy-MM-dd");
			String dateD = formatDebut.format(dateDebut);
			pars.put("dateDebut", dateD);
		}

		// [JS] : Restriction du courrier courant (Courriers liées )
		if (jourOrAutre == 6) {
			trMax.append(" AND cmax.idCourrier != :idCourrierCourant AND cmax.courrierDateReceptionAnnee =:dateDebut  AND cmax.idCourrier NOT IN ( select lc.idCourrier from liencourrier lc , courrier c ,lienscourriers lcs where lc.liensCourrier=lcs.liensCourrier and lcs.idCourrier=c.idCourrier and c.idCourrier =:idCourrierCourant)");
			pars.put("idCourrierCourant", idCourrierCourant);
			pars.put("dateDebut", dateDebut.getYear() + 1900);

		}
		// [JS] : les Courriers Liées
		if (jourOrAutre == 7) {
			trMax.append(" AND cmax.courrierDateReceptionAnnee =:dateDebut AND cmax.idCourrier in ( select lc.idCourrier from liencourrier lc , courrier c ,lienscourriers lcs where lc.liensCourrier=lcs.liensCourrier and lcs.idCourrier=c.idCourrier and c.idCourrier =:idCourrierCourant)");
			pars.put("dateDebut", dateDebut.getYear() + 1900);
			pars.put("idCourrierCourant", idCourrierCourant);

		}

		if (jourOrAutre == 8) {
			trMax.append(" AND cmax.idcourrierFK is null  AND cmax.courrierFormat is null ");

		}
		if (jourOrAutre == 9) {
			System.out.println("idCourrierCourant : " + idCourrierCourant);
			trMax.append(" AND  cmax.idcourrierFK =:idCourrierCourant  AND cmax.courrierFormat is null ");
			pars.put("idCourrierCourant", idCourrierCourant);

		}
		if (jourOrAutre == 10) {
			trMax.append(" AND  cmax.courrierFormat = 'valise' ");

		}
		if (flagInterne == 11) {
			System.out
					.println("############ requ 2 Dans Condition : flagInterne == "
							+ flagInterne);
			trMax.append(" AND  cmax.courrierType ='I' OR cmax.courrierType = 'I*'");

		}
		if (jourOrAutre == 12) {
			System.out
					.println("============================= AO Consultation ");
			System.out.println("idCourrierCourant : " + idCourrierCourant);
			trMax.append(" and cmax.idaoConsultation  =:idCourrierCourant ");
			pars.put("idCourrierCourant", idCourrierCourant);
		}
		// [JS] : les Courriers Liées
		if (jourOrAutre == 13) {
			trMax.append(" AND cmax.idTransmission=10 AND cmax.courrierDateReceptionAnnee =:dateDebut AND cmax.idCourrier in ( select lc.idCourrier from liencourrier lc , courrier c ,lienscourriers lcs where lc.liensCourrier=lcs.liensCourrier and lcs.idCourrier=c.idCourrier and c.idCourrier =:idCourrierCourant)");
			pars.put("dateDebut", dateDebut.getYear() + 1900);
			pars.put("idCourrierCourant", idCourrierCourant);

		}
		if (jourOrAutre == 99) {
			trMax.append(" AND cmax.idTransmission=10 AND cmax.idCourrier != :idCourrierCourant AND cmax.courrierDateReceptionAnnee =:dateDebut AND cmax.courrierDateReceptionAnnee =:dateDebut AND cmax.idCourrier NOT IN ( select lc.idCourrier from liencourrier lc , courrier c ,lienscourriers lcs where lc.liensCourrier=lcs.liensCourrier and lcs.idCourrier=c.idCourrier )");
			pars.put("idCourrierCourant", idCourrierCourant);
			pars.put("dateDebut", dateDebut.getYear() + 1900);
		}

		// --------------------------------------Fin changement
		// opt------------------------------------
		int i = 0;
		String whereMax = "";
		if ((typeCourrier.equals("Recu") || stateTraitement.equals("Avalider"))
				&& listRecu.size() > 0) {
			for (String recu : listRecu) {
				System.out.println("reu 1" + recu);

				System.out.println("recu: " + recu);
				whereMax += " :recu" + i + ", ";
				pars.put("recu" + i, recu);
				i++;
			}
			if (whereMax.length() > 0) {
				trMax.append(" AND tdmax.transactionDestTypeIntervenant IN ("
						+ whereMax.substring(0, whereMax.length() - 2) + ")");
			}
		} else if (typeCourrier.equals("Envoyes") && listEnv.size() > 0) {
			for (String env : listEnv) {
				whereMax += " :env" + i + ", ";
				pars.put("env" + i, env);
				i++;
			}
			if (whereMax.length() > 0) {

				trMax.append(" AND tmax.transactionTypeIntervenant IN ("
						+ whereMax.substring(0, whereMax.length() - 2) + ")");

			}
		} else if (typeCourrier.equals("Tous")
				&& (listEnv.size() > 0 || listRecu.size() > 0)) {
			trMax.append(" AND (");
			for (String env : listEnv) {
				whereMax += " :env" + i + ", ";
				pars.put("env" + i, env);
				i++;
			}
			if (whereMax.length() > 0) {
				trMax.append(" tmax.transactionTypeIntervenant IN ("
						+ whereMax.substring(0, whereMax.length() - 2) + ")");
			}
			whereMax = "";
			for (String recu : listRecu) {
				System.out
						.println("========================== Reu ==============="
								+ recu);
				whereMax += " :recu" + i + ", ";
				pars.put("recu" + i, recu);
				i++;
			}
			if (listEnv.size() > 0) {
				trMax.append(" OR ");
			}
			if (whereMax.length() > 0) {
				trMax.append(" tdmax.transactionDestTypeIntervenant IN ("
						+ whereMax.substring(0, whereMax.length() - 2) + ")");
			}
			trMax.append(") ");
		} else if (typeCourrier.equals("Enveloppe")) {

			int imax1 = 0;
			String whereMax1 = " AND tdreel.transactionDestinationReelleIdDestinataire IN (";
			if (listEnvInteger.size() > 0) {
				for (Integer ids : listEnvInteger) {
					Integer idTrans = ids;
					whereMax1 += ":idTrans" + imax1 + ", ";
					pars.put("idTrans" + imax1, idTrans);
					imax1++;
				}
				whereMax1 = whereMax1.substring(0, whereMax1.length() - 2);
			}
			whereMax1 += " ) ";
			trMax.append(whereMax1);
		}
		// Fin Restrictions

		// etat => A valider
		if (stateTraitement.equals("Avalider")) {
			trMax.append(" AND tmax.etatId IN (2, 10)");
		}
		trMax.append(" GROUP BY dmax.dossierId"
				+ " ORDER BY dmax.dossierId DESC");
		System.out.println("firstIndex : " + firstIndex);
		System.out.println("maxResult : " + maxResult);

//		if (DBType.contains("mysql")) {
//			trMax.append(" LIMIT " + firstIndex + ", " + maxResult);
//		}

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		Iterator<String> iterMax = pars.keySet().iterator();
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			System.out.println("===============");
			System.out.println("name : " + name);
			System.out.println("value : " + value);
			System.out.println("===============");

			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		if (DBType.contains("sqlserver")) {
			queryMax.setFirstResult(firstIndex);
			queryMax.setMaxResults(maxResult);
		}

		System.out.println(queryMax.getQueryString());
		List<Integer> maxIds = queryMax.list();
		System.out.println("list courrier ancien dure RESPONSIBLE MAX : "
				+ (System.currentTimeMillis() - startTime));

		Map<String, Object> params = new HashMap<String, Object>();
		int imax = 0;
		String where = " t.etatId != 99 AND  t.transactionId IN (";
		// String whereD = " AND tm1.dossierId IN (";
		if (maxIds.size() > 0) {
			for (Integer ids : maxIds) {
				Integer idTrans = ids;
				where += ":idTrans" + imax + ", ";
				params.put("idTrans" + imax, idTrans);
				// Integer idDoss = (Integer) ids[1];
				// whereD += ":idDoss" + imax + ", ";
				// params.put("idDoss" + imax, idDoss);
				imax++;
			}
		} else {
			return new ArrayList<CourrierInformations>();
		}

		StringBuffer hql = new StringBuffer(
				"SELECT DISTINCT c.idCourrier AS 'courrierID',"
						+ " c.courrierObjet AS 'courrierObjet',"
						+ " c.courrierReferenceCorrespondant AS 'courrierReference',"
						+ " c.courrierCommentaire AS 'courrierCommentaire',"
						+ " c.courrierOldNum AS 'courrierOldNum',"
						+ " c.courrierCircuit AS 'courrierCircuit',"
						+ " d.dossierId AS 'dossierID',"
						+ " n.natureLibelle AS 'courrierNature',"
						+ " t.etatId AS 'etatID',"
						+ " c.courrierDateReception AS 'courrierDateReceptionEnvoi',"
						+ " t.transactionDateConsultation AS 'transactionDateConsultation',"
						+ " t.transactionId AS 'transactionID',"
						+ " t.idExpDest AS 'expID',"
						+ " t.transactionOrdre AS 'transactionOrdre',"
						+ " t.idUtilisateur AS 'idUtilisateur',"
						+ " t_tdr.transactionDestinationReelleIdDestinataire AS 'transactionDestinationReelID',"
						+ " t_tdr.transactionDestinationReelleTypeDestinataire AS 'transactionDestinationReelleTypeDestinataire',"
						+ " e.typeExpDest AS 'expTypeOld',"
						+ " e.idExpDestLdap AS 'expLdapOld',"
						+ " ee.Exp_Dest_ExterneNom AS 'expNomOld',"
						+ " ee.Exp_Dest_ExternePrenom AS 'expPrenomOld',"
						+ " ee.typeUtilisateurId AS 'expTypeUserOld',"
						+ " tm.transactionId AS 'minTransactionID',"
						+ " em.typeExpDest AS 'expType',"
						+ " em.idExpDestLdap AS 'expLdap',"
						+ " eem.Exp_Dest_ExterneNom AS 'expNom',"
						+ " eem.Exp_Dest_ExternePrenom AS 'expPrenom',"
						+ " eem.typeUtilisateurId AS 'expTypeUser',");
		if (DBType.contains("sqlserver")) {
			hql.append(" STUFF((SELECT '|' + CONVERT(NVARCHAR(10), t3.transactionId)"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), e3.idExpDest), '')"
					+ " + ';' + ISNULL(e3.typeExpDest, '')"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), e3.idExpDestLdap), '')"
					+ " + ';' + ISNULL(ee3.Exp_Dest_ExterneNom, '')"
					+ " + ';' + ISNULL(ee3.Exp_Dest_ExternePrenom, '')"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), ee3.typeUtilisateurId), '')"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), tdr.transactionDestinationReelleIdDestinataire), '')"
					+ " + ';' + ISNULL(tdr.transactionDestinationReelleTypeDestinataire, '')"
					+ " FROM transactionn t3 "
					+ " LEFT JOIN transactiondest td3 ON t3.transactionId = td3.idTransaction"
					+ " LEFT JOIN transactiondestinationreelle tdr ON t3.transactionDestinationReelleId = tdr.transactionDestinationReelleId"
					+ " LEFT JOIN expdest e3 ON td3.idExpDest = e3.idExpDest"
					+ " LEFT JOIN expdestexterne ee3 ON e3.idExpDestExterne = ee3.idExpDestExterne"
					+ " WHERE t3.dossierId = t.dossierId"
					+ " FOR XML PATH('')), 1, 1, '') AS 'destReelList'");
		}
		if (DBType.contains("mysql")) {
			hql.append("(SELECT GROUP_CONCAT(CONCAT(CONVERT(t3.transactionId, CHAR(10))"
					+ " , ';' , IFNULL(CONVERT(e3.idExpDest, CHAR(10)), '')"
					+ " , ';' , IFNULL(e3.typeExpDest, '')"
					+ " , ';' , IFNULL(CONVERT(e3.idExpDestLdap, CHAR(10)), '')"
					+ " , ';' , IFNULL(ee3.Exp_Dest_ExterneNom, '')"
					+ " , ';' , IFNULL(ee3.Exp_Dest_ExternePrenom, '')"
					+ " , ';' , IFNULL(CONVERT(ee3.typeUtilisateurId, CHAR(10)), '')"
					+ " , ';' , IFNULL(CONVERT(tdr.transactionDestinationReelleIdDestinataire, CHAR(10)), '')"
					+ " , ';' , IFNULL(tdr.transactionDestinationReelleTypeDestinataire, '')) SEPARATOR '|')"
					+ " FROM transactionn t3 "
					+ " LEFT JOIN transactiondest td3 ON t3.transactionId = td3.idTransaction"
					+ " LEFT JOIN transactiondestinationreelle tdr ON t3.transactionDestinationReelleId = tdr.transactionDestinationReelleId"
					+ " LEFT JOIN expdest e3 ON td3.idExpDest = e3.idExpDest"
					+ " LEFT JOIN expdestexterne ee3 ON e3.idExpDestExterne = ee3.idExpDestExterne"
					+ " WHERE t3.dossierId = t.dossierId) AS 'destReelList'");
		}
		hql.append(" FROM transactionn t"
				+ " INNER JOIN dossier d ON t.dossierId = d.dossierId"
				+ " INNER JOIN courrierdossier cd ON d.dossierId = cd.dossierId"
				+ " INNER JOIN courrier c ON cd.idCourrier = c.idCourrier"
				+ " INNER JOIN nature n ON c.idNature = n.natureId"
				+ " LEFT JOIN expdest e ON t.idExpDest = e.idExpDest"
				+ " LEFT JOIN expdestexterne ee ON e.idExpDestExterne = ee.idExpDestExterne"
				+ " LEFT JOIN transactiondestinationreelle t_tdr ON t.transactionDestinationReelleId = t_tdr.transactionDestinationReelleId"
				+
				// " LEFT JOIN (SELECT tm2.*" +
				// " FROM transactionn tm2" +
				// " WHERE tm2.transactionId IN (SELECT MIN(tm1.transactionId)"
				// +
				// " FROM transactionn tm1" +
				// " WHERE tm1.dossierId = tm2.dossierId" +
				// whereD.substring(0, whereD.length()-2) +
				// ") GROUP BY tm1.dossierId)) tm ON tm.dossierId = t.dossierId"
				// +
				" INNER JOIN transactionn tm ON t.transactionFirst = tm.transactionId"
				+ " LEFT JOIN expdest em ON tm.idExpDest = em.idExpDest"
				+ " LEFT JOIN expdestexterne eem ON em.idExpDestExterne = eem.idExpDestExterne"
				+ " WHERE " + where.substring(0, where.length() - 2) + ")");

		hql.append(" ORDER BY c.idCourrier DESC");

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		query.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));

		query.addScalar("courrierID", new IntegerType());
		query.addScalar("courrierObjet", new StringType());
		query.addScalar("courrierReference", new StringType());
		query.addScalar("courrierCommentaire", new StringType());
		query.addScalar("courrierOldNum", new IntegerType());
		query.addScalar("courrierCircuit", new StringType());
		query.addScalar("dossierID", new IntegerType());
		query.addScalar("courrierNature", new StringType());
		query.addScalar("etatID", new IntegerType());
		query.addScalar("courrierDateReceptionEnvoi", new DateType());
		query.addScalar("transactionDateConsultation", new DateType());
		query.addScalar("transactionID", new IntegerType());
		query.addScalar("expID", new IntegerType());
		query.addScalar("transactionOrdre", new IntegerType());
		query.addScalar("idUtilisateur", new IntegerType());
		query.addScalar("transactionDestinationReelID", new IntegerType());
		query.addScalar("transactionDestinationReelleTypeDestinataire",
				new StringType());
		query.addScalar("expLdapOld", new IntegerType());
		query.addScalar("expTypeOld", new StringType());
		query.addScalar("expNomOld", new StringType());
		query.addScalar("expPrenomOld", new StringType());
		query.addScalar("expTypeUserOld", new IntegerType());
		query.addScalar("minTransactionID", new IntegerType());
		query.addScalar("expLdap", new IntegerType());
		query.addScalar("expType", new StringType());
		query.addScalar("expNom", new StringType());
		query.addScalar("expPrenom", new StringType());
		query.addScalar("expTypeUser", new IntegerType());
		query.addScalar("destReelList", new StringType());

		Iterator<String> iter = params.keySet().iterator();
		while (iter.hasNext()) {
			String name = iter.next();
			Object value = params.get(name);
			// System.out.println(name + " = " + value);
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println("query=================>" +query.getQueryString());
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<CourrierInformations> findCourrierEnvoyerBOCWithCriterionLies(
			HashMap<String, Object> filterMap, String sortField,
			boolean descending, int jourOrAutre, Date dateDebut, Date dateFin,
			String type, String type1, List<Integer> listIdBocMembers,
			String typeTransmission, String stateTraitement, int firstIndex,
			int maxResult, String categorieCourrierJour, Boolean forRapport,
			String DBType, Integer idCourrierCourant, int flagueCloture,
			int flagInterne) {
		System.out.println("############ flagInterne == " + flagInterne);
		System.out.println("AH AH AH AH ");
		System.out.println("type  " + type);
		System.out.println(" type1  " + type1);
		System.out.println("###############jourOrAutre : " + jourOrAutre);
		System.out.println("typeTransmission : " + typeTransmission);
		System.out.println("categorieCourrierJour : " + categorieCourrierJour);
		Map<String, Object> pars = new HashMap<String, Object>();
		StringBuffer trMax = new StringBuffer(
				"SELECT MAX(tmax.transactionId) AS 'transactionId'"
						+ " FROM transactionn tmax, dossier dmax, courrierdossier cdmax, courrier cmax, transactionn tm"
						+ " WHERE tmax.dossierId = dmax.dossierId"
						+ " AND dmax.dossierId = cdmax.dossierId"
						+ " AND cdmax.idCourrier = cmax.idCourrier"
						+ " AND tmax.transactionFirst = tm.transactionId");

		// if (vb.getSelectedListCourrier().equals("CRmois")){
		// jourOrAutre= 4;
		//
		// }
		// if (vb.getSelectedListCourrier().equals("CRannee")){
		// jourOrAutre=3;
		//
		// }
		
		if (flagueCloture == 4) {

			trMax.append(" AND cmax.courrierEtatCloture =1");
		}
		// Si type=boc_1 cad BO Centrale
		if (type != null) {
			System.out.println("AH : C'est un BOCT");
			//AH: ce code ajouté pour la liste des valise pour chaque BO
			//a l'accès à les valise ou il est l'expéditeur ou destinataitre
			if(jourOrAutre==9){
				int idBoc=0;
				int pos=type.indexOf("_");
				System.out.println(type.substring(pos+1));
				try{
					idBoc=Integer.parseInt(type.substring(pos+1));
					System.out.println("idBoc  "+idBoc);
				}catch(Exception e){
					idBoc=0;
				}
				
				trMax.append(" AND ( tmax.transactionId IN ( SELECT tr.idTransaction FROM transactiondest tr "
						+ "WHERE tr.transactionDestTypeIntervenant='"
						+ type
						+ "' ) OR tmax.transactionDestinationReelleId IN (SELECT tdr.transactionDestinationReelleId " +
								" FROM transactiondestinationreelle tdr " +
								" WHERE tdr.transactionDestinationReelleIdDestinataire="+idBoc+"))");
				
				}
		}// Si non boc_x BO Secondaire
		else {
			// if (type != null){
			int i = 0;
			String whereMax = "";
			trMax.append(" AND (");
			

			for (Integer id : listIdBocMembers) {
			

				whereMax += "tmax.idUtilisateur = :id" + i + " OR ";
				pars.put("id" + i, id);
				i++;
			}
			trMax.append(whereMax);
			
			trMax.append(" tmax.transactionId IN ( SELECT tr.idTransaction FROM transactiondest tr "
					+ "WHERE tr.transactionDestTypeIntervenant='"
					+ type
					+ "' ))");
		}
		System.out.println("jourOrAutre :" + jourOrAutre);
		if (jourOrAutre == 1) {
			trMax.append(" AND tmax.transactionDateTransaction BETWEEN :dateDebut AND :dateFin");
			pars.put("dateDebut", dateDebut);
			pars.put("dateFin", dateFin);
		}
		if (jourOrAutre == 2) {
			trMax.append(" AND tmax.transactionDateTransaction < :dateDebut");
			pars.put("dateDebut", dateDebut);

		}
		// ---------------------------------------MM---------------------------------------------------------------------------------
		// ---------------------------------------Pour
		// optimisation------------------------------------------------------------------
		// **************************************************************
		Calendar calendar = Calendar.getInstance();
		Date dateAujourd = calendar.getTime();
		
		// Liste des couriers pour cette année
		if (jourOrAutre == 3) {
			trMax.append(" AND cmax.courrierDateReceptionAnnee =:dateDebut");
			
			pars.put("dateDebut", dateDebut.getYear() + 1900);
		}
		// Liste des couriers pour ce mois
		if (jourOrAutre == 4) {
			trMax.append(" AND cmax.courrierDateReceptionAnnee =:anneeEnCours AND cmax.courrierDateReceptionMois =:moisEnCours ");
			pars.put("anneeEnCours", dateAujourd.getYear() + 1900);
			pars.put("moisEnCours", dateAujourd.getMonth() + 1);
		}
		// 2019-06-24
		// Liste des couriers pour ce jour
		if (jourOrAutre == 5) {
			trMax.append(" AND DATE_FORMAT(cmax.courrierDateReception,'%Y-%m-%d') =:dateDebut ");

			// JS :Formatter date Début **********************************
			SimpleDateFormat formatDebut = new SimpleDateFormat("yyyy-MM-dd");
			String dateD = formatDebut.format(dateDebut);
			pars.put("dateDebut", dateD);
		}
		// Liste des couriers pour cette année
		if (jourOrAutre == 6) {
			trMax.append(" AND cmax.courrierDateReceptionAnnee =:dateDebut AND cmax.idCourrier != :idCourrierCourant AND cmax.idCourrier NOT IN" 
					+ "(select lc.idCourrier from liencourrier lc , courrier c ,lienscourriers lcs where lc.liensCourrier=lcs.liensCourrier and lcs.idCourrier=c.idCourrier"
					+ " and c.idCourrier =:idCourrierCourant)");
			
			pars.put("dateDebut", dateDebut.getYear() + 1900);
			pars.put("idCourrierCourant", idCourrierCourant);

		}
		
		if (jourOrAutre == 7) {
			trMax.append(" AND cmax.idcourrierFK is null  AND cmax.courrierFormat is null AND cmax.idTransmission=9");

		}
		if (jourOrAutre == 8) {
			
			trMax.append(" AND  cmax.idcourrierFK =:idCourrierCourant  AND cmax.courrierFormat is null ");
			pars.put("idCourrierCourant", idCourrierCourant);

		}
		if (jourOrAutre == 88) {
			
			trMax.append(" AND  cmax.idcourrierFK =:idCourrierCourant  AND cmax.courrierFormat is null AND cmax.courrierDatePointage is null ");
			pars.put("idCourrierCourant", idCourrierCourant);

		}
		if (jourOrAutre == 9) {
			trMax.append(" AND  cmax.courrierFormat = 'valise' ");

		} else {
			trMax.append("  AND cmax.courrierFormat is null ");
		}
		if (jourOrAutre == 10) {
			trMax.append(" AND  cmax.idcourrierFK =:idCourrierCourant AND  cmax.courrierDatePointage is not null ");
			pars.put("idCourrierCourant", idCourrierCourant);
		}
		if (flagInterne == 11) {
			
			trMax.append(" AND  cmax.courrierType = 'I' OR cmax.courrierType = 'I*'");

		}

		// trMax.append(" AND cmax.courrierDateReception ='2016-07-18'");
		// ---------------------------------------Fin
		// optimisation-------------------------------------------------------------------

		// Arrivé Départ
		
		if (categorieCourrierJour.equals("A")) {
			trMax.append(" AND cmax.courrierReferenceCorrespondant LIKE 'A%'");
			// AAAA
		}
		if (categorieCourrierJour.equals("D")) {
			trMax.append(" AND cmax.courrierReferenceCorrespondant LIKE 'D%'");
		}
		if (jourOrAutre != 9 && jourOrAutre != 8 && jourOrAutre != 88
				&& jourOrAutre != 10) {
			if (!(type != null && type.equals("boc_100"))) {
				
				if (!categorieCourrierJour.equals("A")
						&& !categorieCourrierJour.equals("D")) {
					int i = 0;
					System.out.println("I m here");
					String whereMax = "";
					trMax.append(" AND (");

					for (Integer id : listIdBocMembers) {
						System.out.println("ID : " + id);
						whereMax += "tmax.idUtilisateur = :id" + i + " OR ";
						pars.put("id" + i, id);
						i++;
					
					}
				
					trMax.append(whereMax);
					// ça était tm.transactionId
					StringBuffer trDest = new StringBuffer(
							"tmax.transactionId IN (SELECT tdes.idTransaction"
									+ " FROM transactiondest tdes"
									+ " WHERE tdes.transactionDestTypeIntervenant = :type))");
					// System.out.println("typeeee : " + type);
					pars.put("type", type);
					trMax.append(trDest);
				}
			}
		}
		// Fin Arrivé Départ

		// Transmission
		System.out.println("************* Dans la requete ********** : "
				+ typeTransmission);
		// if (typeTransmission.equals("e-Mail")) {
		// trMax.append(" AND cmax.idTransmission = 4");
		// }
		// if (typeTransmission.equals("Fax")) {
		// trMax.append(" AND cmax.idTransmission = 3");
		// }

		if (typeTransmission.equals("Tout les courriers")
				|| typeTransmission.equals("Tous les courriers")) {
			;
			// trMax.append(" AND cmax.idTransmission");
		} else {
			Integer id = Integer.parseInt(typeTransmission);
			trMax.append(" AND cmax.idTransmission = :id");
			pars.put("id", id);
		}

		// Fin Restrictions

		// etat => A valider
		System.out.println("state traitement :" + stateTraitement);
		if (stateTraitement.equals("traite")) {
			trMax.append(" AND (tmax.etatId IN (4, 6) OR (tmax.etatId = 10 AND tmax.transactionOrdre > 1))");
		}
		if (stateTraitement.equals("nonTraite")) {
			trMax.append(" AND (tmax.etatId IN (2, 5) OR (tmax.etatId = 10 AND tmax.transactionOrdre > 1))");
		}

		trMax.append(" GROUP BY dmax.dossierId"
				+ " ORDER BY dmax.dossierId DESC");
//		if (maxResult != 0 && DBType.contains("mysql")) {
//			trMax.append(" LIMIT " + firstIndex + ", " + maxResult);
//		}

		SQLQuery queryMax = getSession().createSQLQuery(trMax.toString());
		System.out.println("queryMax :" + queryMax);
		Iterator<String> iterMax = pars.keySet().iterator();
		System.out.println("iterMax ==>" + iterMax);
		while (iterMax.hasNext()) {
			String name = iterMax.next();
			Object value = pars.get(name);
			if (pars.get(name).getClass() == String.class) {
				queryMax.setString(name, (String) value);
			} else if (pars.get(name).getClass() == Date.class) {
				queryMax.setDate(name, (Date) value);
			} else if (pars.get(name).getClass() == Integer.class) {
				queryMax.setInteger(name, (Integer) value);
			} else {
				queryMax.setParameterList(name, (List<String>) value);
			}
		}

		if (maxResult != 0 && DBType.contains("sqlserver")) {
			queryMax.setFirstResult(firstIndex);
			queryMax.setMaxResults(maxResult);
		}

		System.out.println("queryMax.getQueryString() : "
				+ queryMax.getQueryString());
		System.out.println("queryMax.list  :" + queryMax.list());
		List<Integer> maxIds = queryMax.list();

		Map<String, Object> params = new HashMap<String, Object>();
		int imax = 0;
		String where = " t.transactionId IN (";
		if (maxIds.size() > 0) {
			for (Integer ids : maxIds) {
				Integer idTrans = ids;
				where += ":idTrans" + imax + ", ";
				params.put("idTrans" + imax, idTrans);
				imax++;
			}
		} else {
			return new ArrayList<CourrierInformations>();
		}
		System.out.println("Liste :");
		StringBuffer hql = new StringBuffer(
				"SELECT DISTINCT c.idCourrier AS 'courrierID',"
						+ " c.courrierObjet AS 'courrierObjet',"
						+ " c.courrierReferenceCorrespondant AS 'courrierReference',"
						+ " c.courrierCommentaire AS 'courrierCommentaire',"
						+ " c.courrierOldNum AS 'courrierOldNum',"
						+ " c.courrierCircuit AS 'courrierCircuit',"
						+ " d.dossierId AS 'dossierID',"
						+ " n.natureLibelle AS 'courrierNature',"
						+ " t.etatId AS 'etatID',"
						+ " c.courrierDateReception AS 'courrierDateReceptionEnvoi',"
						+ " t.transactionDateConsultation AS 'transactionDateConsultation',"
						+ " t.transactionId AS 'transactionID',"
						+ " t.idExpDest AS 'expID',"
						+ " t.transactionOrdre AS 'transactionOrdre',"
						+ " t.idUtilisateur AS 'idUtilisateur',"
						+ " t_tdr.transactionDestinationReelleIdDestinataire AS 'transactionDestinationReelID',"
						+ " t_tdr.transactionDestinationReelleTypeDestinataire AS 'transactionDestinationReelleTypeDestinataire',"
						+ " e.typeExpDest AS 'expTypeOld',"
						+ " e.idExpDestLdap AS 'expLdapOld',"
						+ " ee.Exp_Dest_ExterneNom AS 'expNomOld',"
						+ " ee.Exp_Dest_ExternePrenom AS 'expPrenomOld',"
						+ " ee.typeUtilisateurId AS 'expTypeUserOld',"
						+ " tm.transactionId AS 'minTransactionID',"
						+ " em.typeExpDest AS 'expType',"
						+ " em.idExpDestLdap AS 'expLdap',"
						+ " eem.Exp_Dest_ExterneNom AS 'expNom',"
						+ " eem.Exp_Dest_ExternePrenom AS 'expPrenom',"
						+ " eem.typeUtilisateurId AS 'expTypeUser',");
		if (DBType.contains("sqlserver")) {
			hql.append(" STUFF((SELECT '|' + CONVERT(NVARCHAR(10), t3.transactionId)"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), e3.idExpDest), '')"
					+ " + ';' + ISNULL(e3.typeExpDest, '')"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), e3.idExpDestLdap), '')"
					+ " + ';' + ISNULL(ee3.Exp_Dest_ExterneNom, '')"
					+ " + ';' + ISNULL(ee3.Exp_Dest_ExternePrenom, '')"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), ee3.typeUtilisateurId), '')"
					+ " + ';' + ISNULL(CONVERT(NVARCHAR(10), tdr.transactionDestinationReelleIdDestinataire), '')"
					+ " + ';' + ISNULL(tdr.transactionDestinationReelleTypeDestinataire, '')"
					+ " FROM transactionn t3 "
					+ " LEFT JOIN transactiondest td3 ON t3.transactionId = td3.idTransaction"
					+ " LEFT JOIN transactiondestinationreelle tdr ON t3.transactionDestinationReelleId = tdr.transactionDestinationReelleId"
					+ " LEFT JOIN expdest e3 ON td3.idExpDest = e3.idExpDest"
					+ " LEFT JOIN expdestexterne ee3 ON e3.idExpDestExterne = ee3.idExpDestExterne"
					+ " WHERE t3.dossierId = t.dossierId"
					+ " FOR XML PATH('')), 1, 1, '') AS 'destReelList'");
		}
		if (DBType.contains("mysql")) {
			hql.append("(SELECT GROUP_CONCAT(CONCAT(CONVERT(t3.transactionId, CHAR(10))"
					+ " , ';' , IFNULL(CONVERT(e3.idExpDest, CHAR(10)), '')"
					+ " , ';' , IFNULL(e3.typeExpDest, '')"
					+ " , ';' , IFNULL(CONVERT(e3.idExpDestLdap, CHAR(10)), '')"
					+ " , ';' , IFNULL(ee3.Exp_Dest_ExterneNom, '')"
					+ " , ';' , IFNULL(ee3.Exp_Dest_ExternePrenom, '')"
					+ " , ';' , IFNULL(CONVERT(ee3.typeUtilisateurId, CHAR(10)), '')"
					+ " , ';' , IFNULL(CONVERT(tdr.transactionDestinationReelleIdDestinataire, CHAR(10)), '')"
					+ " , ';' , IFNULL(tdr.transactionDestinationReelleTypeDestinataire, '')) SEPARATOR '|')"
					+ " FROM transactionn t3 "
					+ " LEFT JOIN transactiondest td3 ON t3.transactionId = td3.idTransaction"
					+ " LEFT JOIN transactiondestinationreelle tdr ON t3.transactionDestinationReelleId = tdr.transactionDestinationReelleId"
					+ " LEFT JOIN expdest e3 ON td3.idExpDest = e3.idExpDest"
					+ " LEFT JOIN expdestexterne ee3 ON e3.idExpDestExterne = ee3.idExpDestExterne"
					+ " WHERE t3.dossierId = t.dossierId) AS 'destReelList'");
		}
		
		System.out.println(hql.toString());
		hql.append(" FROM transactionn t"
				+ " INNER JOIN dossier d ON t.dossierId = d.dossierId"
				+ " INNER JOIN courrierdossier cd ON d.dossierId = cd.dossierId"
				+ " INNER JOIN courrier c ON cd.idCourrier = c.idCourrier"
				+ " INNER JOIN nature n ON c.idNature = n.natureId");

		hql.append(" LEFT JOIN transactiondest td ON t.transactionId = td.idTransaction"
				+ " LEFT JOIN expdest e ON t.idExpDest = e.idExpDest"
				+ " LEFT JOIN expdestexterne ee ON e.idExpDestExterne = ee.idExpDestExterne"
				+ " LEFT JOIN transactiondestinationreelle t_tdr ON t.transactionDestinationReelleId = t_tdr.transactionDestinationReelleId"
				+ " INNER JOIN transactionn tm ON t.transactionFirst = tm.transactionId"
				+ " LEFT JOIN expdest em ON tm.idExpDest = em.idExpDest"
				+ " LEFT JOIN expdestexterne eem ON em.idExpDestExterne = eem.idExpDestExterne"
				+ " WHERE d.typeDossierId = 1 AND "
				+ where.substring(0, where.length() - 2) + ")");

		// Order by Date
		hql.append(" ORDER BY c.idCourrier DESC");

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		query.setResultTransformer(Transformers
				.aliasToBean(CourrierInformations.class));

		query.addScalar("courrierID", new IntegerType());
		query.addScalar("courrierObjet", new StringType());
		query.addScalar("courrierReference", new StringType());
		query.addScalar("courrierCommentaire", new StringType());
		query.addScalar("courrierOldNum", new IntegerType());
		query.addScalar("courrierCircuit", new StringType());
		query.addScalar("dossierID", new IntegerType());
		query.addScalar("courrierNature", new StringType());
		query.addScalar("etatID", new IntegerType());
		query.addScalar("courrierDateReceptionEnvoi", new DateType());
		query.addScalar("transactionDateConsultation", new DateType());
		query.addScalar("transactionID", new IntegerType());
		query.addScalar("expID", new IntegerType());
		query.addScalar("transactionOrdre", new IntegerType());
		query.addScalar("idUtilisateur", new IntegerType());
		query.addScalar("transactionDestinationReelID", new IntegerType());
		query.addScalar("transactionDestinationReelleTypeDestinataire",
				new StringType());
		query.addScalar("expLdapOld", new IntegerType());
		query.addScalar("expTypeOld", new StringType());
		query.addScalar("expNomOld", new StringType());
		query.addScalar("expPrenomOld", new StringType());
		query.addScalar("expTypeUserOld", new IntegerType());
		query.addScalar("minTransactionID", new IntegerType());
		query.addScalar("expLdap", new IntegerType());
		query.addScalar("expType", new StringType());
		query.addScalar("expNom", new StringType());
		query.addScalar("expPrenom", new StringType());
		query.addScalar("expTypeUser", new IntegerType());
		query.addScalar("destReelList", new StringType());

		Iterator<String> iter = params.keySet().iterator();
		while (iter.hasNext()) {
			System.out.println("AH  dans itr ");
			String name = iter.next();
			Object value = params.get(name);
			System.out.println(name + " = " + value);
			if (params.get(name).getClass() == String.class) {
				query.setString(name, (String) value);
			} else if (params.get(name).getClass() == Date.class) {
				query.setDate(name, (Date) value);
			} else if (params.get(name).getClass() == Integer.class) {
				query.setInteger(name, (Integer) value);
			} else {
				query.setParameterList(name, (List<String>) value);
			}
		}

		System.out.println(query.getQueryString());
		System.out.println("list :" + query.list());
		return query.list();
	}	
	
	@SuppressWarnings("unchecked")
	public List<Mail> getListMailByDestinataireAdresse(String adresseMailConnecte,int mailState){
		String requete = "select m.mailId,m.mailFromName,m.mailFromAddress,m.mailFromOrganization,m.mailToAddress," +
				"m.mailSubject,m.mailContent,m.mailOriginalContent,m.mailReceivedDate from mail m" +
				"	WHERE m.mailDeleted = "+mailState+" " +
						" AND is_treated= " + mailState +" AND mailToAddress like '%"+adresseMailConnecte+"%';";
		
		//Query query = getSession().createSQLQuery(requete);
		// Query query = getSession().createQuery(hql);
		SQLQuery query = getSession().createSQLQuery(requete);
		query.setResultTransformer(Transformers
				.aliasToBean(Mail.class));
		query.addScalar("mailId", new IntegerType());
		query.addScalar("mailFromName", new StringType());
		query.addScalar("mailFromAddress", new StringType());
		
		query.addScalar("mailFromOrganization", new StringType());
		query.addScalar("mailToAddress", new StringType());
		query.addScalar("mailSubject", new StringType());
		query.addScalar("mailContent", new StringType());
		query.addScalar("mailOriginalContent", new StringType());
		query.addScalar("mailReceivedDate", new DateType());
		
		return query.list();	
	}
	/*
	@SuppressWarnings("unchecked")
	public List<AttachmentFileBean>listeMailAttachementFilesByIdMessage(int messageId){
		String requete = "select m.size as 'size',m.filename as 'name' from mail_mailattachmentfiles m	WHERE m.mail_mailId = "+messageId+";";
		SQLQuery query = getSession().createSQLQuery(requete);
		query.setResultTransformer(Transformers
				.aliasToBean(AttachmentFileBean.class));
		query.addScalar("size", new IntegerType());
		query.addScalar("name", new StringType());
		return query.list();	
	}
	
	@SuppressWarnings("unchecked")
	public List<AttachmentHeadBean>listeMailHederByIdMail(int messageId){
		String requete = "select m.mapkey as 'cle',m.mailHeaders as 'valeur' from mail_mailheaders m	WHERE m.mail_mailId = "+messageId+";";
		SQLQuery query = getSession().createSQLQuery(requete);
		query.setResultTransformer(Transformers
				.aliasToBean(AttachmentHeadBean.class));
		query.addScalar("valeur", new StringType());
		query.addScalar("cle", new StringType());
		return query.list();	
	}*/
	
	public int getTransactionReceptionPhysique(int idBOC, int refdossier) {
		
		
		String requete = "SELECT MAX(tmax.transactionId) AS 'transactionId' " +
						" FROM transactionn tmax, dossier dmax,transactiondest tdest,courrierdossier cdmax, courrier cmax, transactionn tm "+
						" WHERE tmax.dossierId = dmax.dossierId "+
						" AND dmax.dossierId = cdmax.dossierId "+
						" AND cdmax.idCourrier = cmax.idCourrier "+ 
						" AND tmax.transactionFirst = tm.transactionId "+
						" AND tmax.dossierId = "+refdossier +
						" AND tdest.transactionDestIdIntervenant="+idBOC +
						" AND tdest.idTransaction=tmax.transactionId";
		SQLQuery query = getSession().createSQLQuery(requete);
		return  ((Integer) query.list().get(0)).intValue();
		
		
	}
	@SuppressWarnings("unchecked")
	public List<String> listeMessageIDIdMaile(Integer mailId, String messageID) {
		String requete = "select m.mailHeaders  from mail_mailheaders m	WHERE m.mail_mailId = "+mailId+" AND mapkey like '"+ messageID +"';";
		SQLQuery query = getSession().createSQLQuery(requete);

		return query.list();	
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Transaction> listTransactionByDossier(int dossierId){
		String requete =  "SELECT * FROM transactionn td " +
				" WHERE	td.dossierId= " +dossierId+
				" AND td.transactionId IN (select Max(td2.transactionId) " +
				" FROM transactionn td2 " +
				" WHERE	td2.dossierId= "+dossierId+"" +
				" AND td.transactionDestinationReelleId=td2.transactionDestinationReelleId)" +
				" ORDER BY td.transactionId desc";
		SQLQuery query = getSession().createSQLQuery(requete);
		query.setResultTransformer(Transformers
				.aliasToBean(Transaction.class));
		return query.list();
	}
	
	public int getCountTransactionByIdExpExterne(int idExpediteur) {

		String expCount = "select count(*) from expdest ex " +
		" where idExpDestExterne="+idExpediteur;
		SQLQuery query = getSession().createSQLQuery(expCount);
		Integer res = 0;
		List results = query.list();
		res = ((BigInteger) results.get(0)).intValue();
		
		return   res;
	}
}

