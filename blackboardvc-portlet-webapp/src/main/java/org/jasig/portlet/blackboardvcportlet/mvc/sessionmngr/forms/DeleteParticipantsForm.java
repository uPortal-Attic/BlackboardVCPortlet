/**
 * @author Brad Leege <leege@doit.wisc.edu>
 * Created on 4/24/13 at 3:05 PM
 */
package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class DeleteParticipantsForm implements Serializable
{
	private static final long serialVersionUID = 1L;

	@NotNull
	private long deleteParticipantsSessionId;

	@NotNull
	@Size(min = 1)
	private long[] nonChairId;

	/**
	 * Constructor
	 */
	public DeleteParticipantsForm()
	{
	}

	public long getDeleteParticipantsSessionId()
	{
		return deleteParticipantsSessionId;
	}

	public void setDeleteParticipantsSessionId(long deleteParticipantsSessionId)
	{
		this.deleteParticipantsSessionId = deleteParticipantsSessionId;
	}

	public long[] getNonChairId()
	{
		return nonChairId;
	}

	public void setNonChairId(long[] nonChairId)
	{
		this.nonChairId = nonChairId;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("DeleteParticipantsForm [deleteParticipantsSessionId=");
		sb.append(deleteParticipantsSessionId);
		sb.append(", nonChairId=");
		sb.append(nonChairId);
		sb.append("];");
		return sb.toString();
	}
}

