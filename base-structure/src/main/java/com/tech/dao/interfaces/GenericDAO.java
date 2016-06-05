package com.tech.dao.interfaces;

import java.io.Serializable;
import java.util.List;

public interface GenericDAO<T, ID extends Serializable>
{
    
    T retrieveById(ID id, boolean lock);

    
    T retrieveById(ID id);
    
    
    T getById(ID id);

    
    List<T> findAll();
    
    
    List<T> findByExample(T exampleInstance, String... excludeProperty);
    
    
    T persist(T entity);

    
    T save(T entity);

    
    T update(T entity);
    
    
    void delete(T entity);

   
    void clear();

    
    void evict(T entity);
    
    
    T findByName(String name);
}
