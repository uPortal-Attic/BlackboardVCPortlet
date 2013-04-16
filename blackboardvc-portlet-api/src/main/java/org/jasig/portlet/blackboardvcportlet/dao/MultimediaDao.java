package org.jasig.portlet.blackboardvcportlet.dao;

import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.data.Multimedia;

import com.elluminate.sas.BlackboardMultimediaResponse;

public interface MultimediaDao {

	public abstract Set<Multimedia> getAllMultimedia();

	public abstract Multimedia getMultimediaByBlackboardId(
			long bbMultimediaId);

	public abstract Multimedia createMultimedia(
			BlackboardMultimediaResponse multimediaResponse, String filename);

	public abstract void deleteMultimedia(Multimedia multimedia);

}