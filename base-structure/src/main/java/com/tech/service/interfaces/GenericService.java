package com.tech.service.interfaces;

import java.io.Serializable;

import com.tech.dao.interfaces.GenericDAO;


public interface GenericService<T, ID extends Serializable> extends GenericDAO<T, ID>{

}
