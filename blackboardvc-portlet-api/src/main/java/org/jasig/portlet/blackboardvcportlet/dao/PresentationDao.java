package org.jasig.portlet.blackboardvcportlet.dao;

import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.data.Presentation;

import com.elluminate.sas.BlackboardPresentationResponse;

public interface PresentationDao {
	public abstract Set<Presentation> getAllPresentations();

	public abstract Presentation getPresentationByBlackboardId(long bbPresentationId);

	public abstract Presentation createMultimedia(BlackboardPresentationResponse presentationResponse, String filename);

	public abstract void deleteMultimedia(Presentation presentation);

}
