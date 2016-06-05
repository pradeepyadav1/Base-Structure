package com.tech.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tech.dao.interfaces.GenericDAO;


@Repository
@Transactional
public abstract class AbstractDAO<T, ID extends Serializable> implements GenericDAO<T, ID>
{
	@Autowired
	private SessionFactory sessionFactory;
	
    private final transient Class<T> persistentClass;
    private Session session;

    
    public AbstractDAO()
    {
        persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                                .getActualTypeArguments()[0];
    }
    
    public AbstractDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    
    public final void setSession(final Session sess)
    {
        session = sess;
    }

    
    public final Session getSession()
    {
        if(session == null || !session.isOpen())
        {
        	System.out.println("pradeep sessionFactory is - "+sessionFactory);
        	session = sessionFactory.getCurrentSession();
        }
        System.out.println("pradeep session is - "+session);
        return session;
    }

    
    public final Session getSession(final String sessionName)
    {
        return session = sessionFactory.getCurrentSession();
    }

    
    public final Class<T> getPersistentClass()
    {
        return persistentClass;
    }

    @Override
    public T getById(ID id)
    {
        return (T) getSession().get(persistentClass, id);
    }

    @Override
    public T retrieveById(ID id, boolean lock)
    {
        if(lock)
        {
        	return (T) getSession().load(getPersistentClass(), id, LockOptions.UPGRADE);
        }
        return (T) getSession().load(getPersistentClass(), id);
    }

    @Override
    public T retrieveById(ID id)
    {
        return retrieveById(id, false);
    }

   
    @Override
    public List<T> findAll()
    {
        return findByCriteria();
    }

    
    @Override
    public final List<T> findByExample(final T exampleInstance, final String... excludeProperty)
    {
        final Criteria crit = getSession().createCriteria(getPersistentClass());
        final Example example = Example.create(exampleInstance);
        for(String exclude : excludeProperty)
        {
            example.excludeProperty(exclude);
        }
        crit.add(example);
        return crit.list();
    }

    
    @Override
    public final T persist(final T entity)
    {
        getSession().saveOrUpdate(entity);
        return entity;
    }

    
    @Override
    public T save(final T entity)
    {
        getSession().save(entity);
        return entity;
    }

    
    @Override
    public T update(final T entity)
    {
        getSession().update(entity);
        return entity;
    }

    
    @Override
    public final void delete(final T entity)
    {
        getSession().delete(entity);
    }

    
    public final void flush()
    {
        getSession().flush();
    }

    
    @Override
    public final void clear()
    {
        getSession().clear();
    }

    @Override
    public final void evict(final T entity)
    {
        getSession().evict(entity);
    }

    
    protected final List<T> findByCriteria(final Criterion... criterion)
    {
        return findByCriteria(getSession(), criterion);
    }

    
    protected final List<T> findByCriteria(final String sessionName, final Criterion... criterion)
    {
        return findByCriteria(getSession(sessionName), criterion);
    }
    
    private List<T> findByCriteria(final Session session, final Criterion... criterion)
    {
        final Criteria crit = session.createCriteria(getPersistentClass());
        for(Criterion c : criterion)
        {
            crit.add(c);
        }
        return crit.list();
    }
    
    
    @Override
    public final T findByName(final String name)
    {
    	final Criteria criteria = getSession().createCriteria(getPersistentClass());
    	criteria.add(Restrictions.eq("name", name));
    	criteria.setMaxResults(1);
        return (T) criteria.uniqueResult();
    }
}
