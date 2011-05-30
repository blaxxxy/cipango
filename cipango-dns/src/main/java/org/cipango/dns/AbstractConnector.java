// ========================================================================
// Copyright 2011 NEXCOM Systems
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
package org.cipango.dns;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.log.Log;

public abstract class AbstractConnector extends AbstractLifeCycle implements DnsConnector
{
	private InetAddress _host;
	private int _port;
	
	public String getHost()
	{
		if (_host == null)
			return null;
		return _host.getHostName();
	}
	
	public InetAddress getHostAddr()
	{
		return _host;
	}

	public void setHost(String host)
	{
		try
		{
			_host = InetAddress.getByName(host);
		}
		catch (UnknownHostException e) 
		{
			Log.debug(e);
		}
	}

	public int getPort()
	{
		return _port;
	}

	public void setPort(int port)
	{
		if (port < 0 || port > 65536)
			throw new IllegalArgumentException("Invalid port: " + port);
		_port = port;
	}

}
