package com.tech.service.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.access.prepost.PreAuthorize;

import com.tech.dao.interfaces.GenericDAO;
import com.tech.service.interfaces.GenericService;


public abstract class AbstractService<T, ID extends Serializable> implements GenericService<T, ID>{

     protected abstract GenericDAO<T,ID> getDao();
     
     //protected final String permissionPrefix = "";
    
    @Override
    //@PreAuthorize("hashRole('"+permissionPrefix+"'-getById")
    public T getById(ID id)
    {
        return getDao().getById(id);
    }

    
    @Override
    public T retrieveById(ID id)
    {
        return getDao().retrieveById(id);
    }
    
    
    @Override
    public List<T> findAll()
    {
        return getDao().findAll();
    }
    
    
    @Override
    public final T persist(final T entity)
    {
    	return getDao().persist(entity);
    }

    @Override
    public T save(final T entity)
    {
    	return getDao().save(entity);
    }
    
    
    @Override
    public T update(final T entity)
    {
    	getDao().update(entity);
        return entity;
    }
    
    
    @Override
    public final void delete(final T entity)
    {
    	getDao().delete(entity);
    }
    
    @Override
    public final void clear()
    {
    	getDao().clear();
    }

    @Override
    public final void evict(final T entity)
    {
    	getDao().evict(entity);
    }



    /****************************************
     * Not required methods
     * *************************************
     */
	@Override
	public T retrieveById(ID id, boolean lock) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<T> findByExample(T exampleInstance, String... excludeProperty) {
		return getDao().findByExample(exampleInstance, excludeProperty);
	}
	
	@Override
	public final T findByName(final String name)
    {
		return getDao().findByName(name);
    }
}
