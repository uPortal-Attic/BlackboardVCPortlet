/**
 * @author Brad Leege <leege@doit.wisc.edu>
 * Created on 4/25/13 at 3:31 PM
 */
package org.jasig.portlet.blackboardvcportlet.validations.test;

import org.jasig.portlet.blackboardvcportlet.service.SessionForm;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class CustomValidationsTest
{
	private static Logger logger = LoggerFactory.getLogger(CustomValidationsTest.class);
	private static Validator validator;

	@Before
	public void setUp() throws Exception
	{
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	/**
	 * Tests the QuarterHourCheck
	 * The StartMinute should pass, while the EndMinute should trigger a violation.
	 */
	@Test
	public void testQuarterHourCheck()
	{
		logger.info("testQuarterHourCheck() started....");
		SessionForm sessionForm = new SessionForm();
		sessionForm.setSessionName("Test Session");
		sessionForm.setBoundaryTime(15);
		sessionForm.setStartTime(DateTime.now().plusDays(7).withHourOfDay(5).withMinuteOfHour(15).withSecondOfMinute(0).withMillisOfSecond(0));
		sessionForm.setEndTime(sessionForm.getStartTime().withHourOfDay(6).withMinuteOfHour(35).withSecondOfMinute(0).withMillisOfSecond(0));

		Set<ConstraintViolation<SessionForm>> constraintViolations = validator.validate(sessionForm);
		assertEquals(1, constraintViolations.size());
		assertEquals("endMinute", constraintViolations.iterator().next().getPropertyPath().toString());
		logger.info("testQuarterHourCheck() finished.");
	}

	@Test
	public void testSessionStartTimeRangeCheck() throws Exception
	{
		logger.info("testSessionStartTimeRangeCheck() started....");
		SessionForm sessionForm = new SessionForm();
		sessionForm.setSessionName("Test Session");
		sessionForm.setBoundaryTime(15);
		sessionForm.setStartTime(DateTime.now().plusDays(7).withHourOfDay(20).withMinuteOfHour(15).withSecondOfMinute(0).withMillisOfSecond(0));
		sessionForm.setEndTime(sessionForm.getStartTime().withHourOfDay(20).withMinuteOfHour(15).withSecondOfMinute(0).withMillisOfSecond(0));

		Set<ConstraintViolation<SessionForm>> constraintViolations = validator.validate(sessionForm);
		assertEquals(1, constraintViolations.size());

		// Test All Good Scenario
		sessionForm.setEndTime(sessionForm.getStartTime().withHourOfDay(21).withMinuteOfHour(45).withSecondOfMinute(0).withMillisOfSecond(0));
		constraintViolations = validator.validate(sessionForm);
		assertEquals(0, constraintViolations.size());

		logger.info("testSessionStartTimeRangeCheck() finished.");
	}

	/**
	 * Tests theq SessionEndTimeRangeCheck
	 * The first attempt should pass all constraint checks, while the second attempt
	 * should trigger an out of range violation.
	 * @throws Exception
	 */
	@Test
	public void testSessionEndTimeRangeCheck() throws Exception
	{
		logger.info("testSessionEndTimeRangeCheck() started....");
		SessionForm sessionForm = new SessionForm();
		sessionForm.setSessionName("Test Session");
		sessionForm.setBoundaryTime(15);
		sessionForm.setStartTime(DateTime.now().plusDays(7).withHourOfDay(20).withMinuteOfHour(15).withSecondOfMinute(0).withMillisOfSecond(0));
		sessionForm.setEndTime(sessionForm.getStartTime().withHourOfDay(21).withMinuteOfHour(45).withSecondOfMinute(0).withMillisOfSecond(0));

		Set<ConstraintViolation<SessionForm>> constraintViolations = validator.validate(sessionForm);
		assertEquals(0, constraintViolations.size());

		sessionForm.setEndDate(DateMidnight.now().plusDays(2));
		constraintViolations = validator.validate(sessionForm);
		assertEquals(1, constraintViolations.size());

		logger.info("testSessionEndTimeRangeCheck() finished.");
	}

	@Test
	public void testSessionNameCheck() throws Exception
	{
		logger.info("testSessionNameCheck() started....");
		SessionForm sessionForm = new SessionForm();
		sessionForm.setBoundaryTime(15);
		sessionForm.setStartTime(DateTime.now().plusDays(7).withHourOfDay(20).withMinuteOfHour(15).withSecondOfMinute(0).withMillisOfSecond(0));
		sessionForm.setEndTime(sessionForm.getStartTime().withHourOfDay(21).withMinuteOfHour(45).withSecondOfMinute(0).withMillisOfSecond(0));

		// Test The Length Flag
		sessionForm.setSessionName("Test Session Name That Will Be Longer Than Two Hundred And Fifty Five Characters So That It'll Trigger The Validation Error.  Test Session Name That Will Be Longer Than Two Hundred And Fifty Five Characters So That It'll Trigger The Validation Error.  Test Session Name That Will Be Longer Than Two Hundred And Fifty Five Characters So That It'll Trigger The Validation Error.  Test Session Name That Will Be Longer Than Two Hundred And Fifty Five Characters So That It'll Trigger The Validation Error.  Test Session Name That Will Be Longer Than Two Hundred And Fifty Five Characters So That It'll Trigger The Validation Error.  Test Session Name That Will Be Longer Than Two Hundred And Fifty Five Characters So That It'll Trigger The Validation Error.  Test Session Name That Will Be Longer Than Two Hundred And Fifty Five Characters So That It'll Trigger The Validation Error.  Test Session Name That Will Be Longer Than Two Hundred And Fifty Five Characters So That It'll Trigger The Validation Error.");
		Set<ConstraintViolation<SessionForm>> constraintViolations = validator.validate(sessionForm);
		assertEquals(1, constraintViolations.size());
		assertEquals("sessionName", constraintViolations.iterator().next().getPropertyPath().toString());

		// Test the illegal charachters
		sessionForm.setSessionName("Dortmund < Bayern");
		constraintViolations = validator.validate(sessionForm);
		assertEquals(1, constraintViolations.size());
		assertEquals("sessionName", constraintViolations.iterator().next().getPropertyPath().toString());

		// Acceptable Name
		sessionForm.setSessionName("Mia San Mia");
		constraintViolations = validator.validate(sessionForm);
		assertEquals(0, constraintViolations.size());

		logger.info("testSessionNameCheck() finished.");
	}
}
