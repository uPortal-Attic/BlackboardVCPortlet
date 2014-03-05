package org.jasig.portlet.blackboardvcportlet.dao.impl;

import org.jasig.portlet.blackboardvcportlet.dao.PresentationDao;

public interface InternalPresentationDao extends PresentationDao {
    PresentationImpl getPresentationById(long presentationId);
}
