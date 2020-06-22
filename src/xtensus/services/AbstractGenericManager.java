package xtensus.services;

import java.util.List;

import org.hibernate.HibernateException;

import xtensus.dao.IGenericDao;
import xtensus.entity.Entite;

public abstract class AbstractGenericManager implements IGenericManager {

	protected IGenericDao dao;

	/**
	 * @return the dao
	 */
	public IGenericDao getDao() {
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(IGenericDao dao) {
		this.dao = dao;
	}

	public void insert(Entite entity) throws Exception {
		dao.insert(entity);
	}

	public void update(Entite entity) throws Exception {
		dao.update(entity);
	}

	public void delete(Entite entity) throws Exception {
		dao.delete(entity);
	}

	public <T extends Entite> List<T> getList(Class<T> t) throws Exception {
		return dao.getList(t);
	}


	
}
