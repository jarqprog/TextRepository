package com.jarq.system.models.content;

import com.jarq.system.dao.Dao;
import com.jarq.system.exceptions.DaoFailure;

import java.util.List;

public interface IDaoContent extends Dao {

    IContent createNullContent();

    IContent createContent(int textId, String filepath)
            throws DaoFailure;

    IContent importContent(int contentId) throws DaoFailure;

    List<IContent> importContentsByTextId(int textId) throws DaoFailure;

    boolean removeContent(IContent content) throws DaoFailure;

    boolean removeContent(int contentId) throws DaoFailure;

    boolean removeContentsByTextId(int textId) throws DaoFailure;
}