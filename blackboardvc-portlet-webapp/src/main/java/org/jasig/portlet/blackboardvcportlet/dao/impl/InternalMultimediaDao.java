package org.jasig.portlet.blackboardvcportlet.dao.impl;

import org.jasig.portlet.blackboardvcportlet.dao.MultimediaDao;


interface InternalMultimediaDao extends MultimediaDao {
    MultimediaImpl getMultimediaById(long multimediaId);
    
}
