package xtensus.services;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;

import xtensus.entity.Courrier;
import xtensus.entity.Entite;

public interface IGenericManager {

	public void insert(Entite entity) throws Exception;

	public void update(Entite entity) throws Exception;

	public void delete(Entite entity) throws Exception;

	public <T extends Entite> List<T> getList(Class<T> t) throws Exception;

	public <T extends Entite> List<T> getListUnique(Class<T> t)
			throws Exception;

	public List<Courrier> getListCourrier(String refrenceCourrier,
			String necessiteReponse, Date dateReception, String keywords,
			Date dateReponse) throws Exception;

	
	public List<Object[]> executeQuery3(String myRequet)throws HibernateException;

}
