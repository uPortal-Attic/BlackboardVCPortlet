/**
 * @author Brad Leege <leege@doit.wisc.edu>
 * Created on 4/1/13 at 2:01 PM
 */

package org.jasig.portlet.blackboardvcportlet.service.util;

import java.util.List;
import java.util.Map;

public class MailTask
{
	private String from;
	private List<String> to;
	private String subject;
	private Map substitutions;
	private MailMessages template;

	/**
	 * Constructor
	 * @param from From Address
	 * @param to To Addresses
	 * @param subject Email Subject
	 * @param substitutions Data for the email template
	 * @param template Email Message To Send
	 */
	public MailTask(String from, List<String> to, String subject, Map substitutions, MailMessages template)
	{
		this.setFrom(from);
		this.setTo(to);
		this.setSubject(subject);
		this.setSubstitutions(substitutions);
		this.setTemplate(template);
	}

	public String getFrom()
	{
		return from;
	}

	public void setFrom(String from)
	{
		this.from = from;
	}

	public List<String> getTo()
	{
		return to;
	}

	public void setTo(List<String> to)
	{
		this.to = to;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public Map getSubstitutions()
	{
		return substitutions;
	}

	public void setSubstitutions(Map substitutions)
	{
		this.substitutions = substitutions;
	}

	public MailMessages getTemplate()
	{
		return template;
	}

	public void setTemplate(MailMessages template)
	{
		this.template = template;
	}
}