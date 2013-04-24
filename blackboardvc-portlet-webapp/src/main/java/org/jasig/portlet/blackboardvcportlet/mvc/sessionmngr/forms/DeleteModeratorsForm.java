/**
 * @author Brad Leege <leege@doit.wisc.edu>
 * Created on 4/24/13 at 1:41 PM
 */
package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr.forms;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class DeleteModeratorsForm implements Serializable
{
	private static final long serialVersionUID = 1L;

	@NotNull
	private long deleteModeratorSessionId;

	@NotNull
	@Valid
	@Size(min = 1)
	private long[] chairId;

	/**
	 * Constructor
	 */
	public DeleteModeratorsForm()
	{
	}

	public long getDeleteModeratorSessionId()
	{
		return deleteModeratorSessionId;
	}

	public void setDeleteModeratorSessionId(long deleteModeratorSessionId)
	{
		this.deleteModeratorSessionId = deleteModeratorSessionId;
	}

	public long[] getChairId()
	{
		return chairId;
	}

	public void setChairId(long[] chairId)
	{
		this.chairId = chairId;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("DeleteModeratorsForm [deleteModeratorSessionId=");
		sb.append(deleteModeratorSessionId);
		sb.append(", chairId=");
		sb.append(chairId);
		sb.append("];");
		return sb.toString();
	}
}
