/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portlet.blogs.pingback;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Function;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.model.MBMessageDisplay;
import com.liferay.portlet.messageboards.model.MBThread;
import com.liferay.portlet.messageboards.service.MBMessageLocalService;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.answers.CallsRealMethods;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author André de Oliveira
 */
@PrepareForTest( { MBMessageLocalServiceUtil.class })
@RunWith(PowerMockRunner.class)
public class PingbackCommentsImplTest extends PowerMockito {

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		setUpMessageBoards();
		setUpServiceContext();
	}

	@Test
	public void testAddComment() throws Exception {
		addComment();

		Mockito.verify(
			_mbMessageLocalService
		).getDiscussionMessageDisplay(
			USER_ID, GROUP_ID, BlogsEntry.class.getName(), ENTRY_ID,
			WorkflowConstants.STATUS_APPROVED
		);

		Mockito.verify(
			_mbMessageLocalService
		).getThreadMessages(
			THREAD_ID, WorkflowConstants.STATUS_APPROVED
		);

		Mockito.verify(
			_mbMessageLocalService
		).addDiscussionMessage(
			USER_ID, StringPool.BLANK, GROUP_ID, BlogsEntry.class.getName(),
			ENTRY_ID, THREAD_ID, PARENT_MESSAGE_ID, StringPool.BLANK,
			"__body__", _serviceContext
		);
	}

	@Test(expected = DuplicateCommentException.class)
	public void testDuplicateComment() throws Exception {
		MBMessage message = Mockito.mock(MBMessage.class);

		when(
			message.getBody()
		).thenReturn(
			"__body__"
		);

		List<MBMessage> messages = Collections.singletonList(message);

		when(
			_mbMessageLocalService.getThreadMessages(
				THREAD_ID, WorkflowConstants.STATUS_APPROVED)
		).thenReturn(
			messages
		);

		addComment();

		Assert.fail();
	}

	protected void addComment() throws PortalException, SystemException {

		_pingbackComments.addComment(
			USER_ID, GROUP_ID, BlogsEntry.class.getName(), ENTRY_ID, "__body__",
			_serviceContextFunction);
	}

	protected void setUpMessageBoards() throws Exception {

		when(
			_mbMessageDisplay.getThread()
		).thenReturn(
			_mbThread
		);

		when(
			_mbMessageLocalService.getDiscussionMessageDisplay(
				Matchers.anyLong(), Matchers.anyLong(),
				Matchers.eq(BlogsEntry.class.getName()), Matchers.anyLong(),
				Matchers.eq(WorkflowConstants.STATUS_APPROVED))
		).thenReturn(
			_mbMessageDisplay
		);

		when(
			_mbThread.getRootMessageId()
		).thenReturn(
			PARENT_MESSAGE_ID
		);

		when(
			_mbThread.getThreadId()
		).thenReturn(
			THREAD_ID
		);

		mockStatic(MBMessageLocalServiceUtil.class, new CallsRealMethods());

		stub(
			method(MBMessageLocalServiceUtil.class, "getService")
		).toReturn(
			_mbMessageLocalService
		);
	}

	protected void setUpServiceContext() {
		when(
			_serviceContextFunction.apply(null)
		).thenReturn(
			_serviceContext
		);
	}

	private static final long ENTRY_ID = 142857;

	private static final long GROUP_ID = 16;

	private static final long PARENT_MESSAGE_ID = 1337L;

	private static final long THREAD_ID = 7676L;

	private static final long USER_ID = 42;

	@Mock
	private MBMessageDisplay _mbMessageDisplay;

	@Mock
	private MBMessageLocalService _mbMessageLocalService;

	@Mock
	private MBThread _mbThread;

	private PingbackComments _pingbackComments = new PingbackCommentsImpl();
	private ServiceContext _serviceContext = new ServiceContext();

	@Mock
	private Function<String, ServiceContext> _serviceContextFunction;

}