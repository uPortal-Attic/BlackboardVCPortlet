package org.jasig.portlet.blackboardvcportlet.dao;

import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.data.Presentation;

import com.elluminate.sas.BlackboardPresentationResponse;

public interface PresentationDao {
	
	Set<Presentation> getAllPresentations();
	
	Presentation getPresentationById(long presentationId);

	Presentation getPresentationByBlackboardId(long bbPresentationId);

	Presentation createPresentation(BlackboardPresentationResponse presentationResponse, String filename);

	void deletePresentation(Presentation presentation);

}
