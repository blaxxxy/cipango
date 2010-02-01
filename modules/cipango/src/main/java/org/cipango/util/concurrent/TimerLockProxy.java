// ========================================================================
// Copyright 2008-2009 NEXCOM Systems
// ------------------------------------------------------------------------
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at 
// http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ========================================================================

package org.cipango.util.concurrent;

import java.io.Serializable;

import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipApplicationSession;

import org.cipango.SessionManager;
import org.cipango.SessionManager.SessionTransaction;
import org.cipango.servlet.AppSession;

public class TimerLockProxy implements ServletTimer
{
	private SessionManager _callManager;
	private ServletTimer _timer;
	private AppSession _appSession;
	
	public TimerLockProxy(AppSession session, long delay, boolean isPersistent, Serializable info)
	{
		_appSession = session;
		SessionTransaction workUnit = begin();
		try
		{
			_timer = _appSession.newTimer(delay, isPersistent, info);
		}
		finally
		{
			workUnit.done();
		}
	}
	
	public TimerLockProxy(AppSession session, long delay, long period, boolean fixedDelay, boolean isPersistent, Serializable info)
	{
		_appSession = session;
		SessionTransaction transaction = begin();
		try
		{
			_timer = _appSession.newTimer(delay, period, fixedDelay, isPersistent, info);
		}
		finally
		{
			transaction.done();
		}
	}
	
	protected SessionManager getCallSessionManager()
	{
		if (_callManager == null)
			_callManager = _appSession.getCallSession().getServer().getSessionManager();
		return _callManager;
	}
	
	protected SessionTransaction begin()
	{
		return getCallSessionManager().begin(_appSession.getCallSession());
	}
	
	public void cancel()
	{
		SessionTransaction transaction = begin();
		try
		{
			_timer.cancel();
		}
		finally
		{
			transaction.done();
		}
	}

	public SipApplicationSession getApplicationSession()
	{
		return _timer.getApplicationSession();
	}

	public String getId()
	{
		return _timer.getId();
	}

	public Serializable getInfo()
	{
		return _timer.getInfo();
	}

	public long getTimeRemaining()
	{
		return _timer.getTimeRemaining();
	}

	public long scheduledExecutionTime()
	{
		return _timer.scheduledExecutionTime();
	}
}