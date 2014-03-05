package org.jasig.portlet.blackboardvcportlet.dao;

import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.data.Multimedia;

import com.elluminate.sas.BlackboardMultimediaResponse;

public interface MultimediaDao {

	Set<Multimedia> getAllMultimedia();
	
	Multimedia getMultimediaById(long multimediaId);

	Multimedia getMultimediaByBlackboardId(long bbMultimediaId);

	Multimedia createMultimedia(BlackboardMultimediaResponse multimediaResponse, String filename);

	void deleteMultimedia(Multimedia multimedia);

}