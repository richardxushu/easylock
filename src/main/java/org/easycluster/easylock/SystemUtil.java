package org.easycluster.easylock;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(SystemUtil.class);

	private static final String DEFAULT_LOCAL_IPADDRESS = "127.0.0.1";
	private static final String DEFAULT_LOCAL_HOSTNAME = "localhost";
	private static final String DEFAULT_PID = "0";

	private static String pid;
	private static ReentrantLock pidLock = new ReentrantLock();

	public static String getPid() {
		if (pid == null) {
			pidLock.lock();
			try {
				if (pid == null) {
					try {
						pid = DEFAULT_PID;
						RuntimeMXBean runtime = ManagementFactory
								.getRuntimeMXBean();
						if (runtime != null) {
							String name = runtime.getName();
							int index = name.indexOf('@');
							if (index > 0) {
								pid = name.substring(0, index);
							}
						}
					} catch (Exception e) {
						logger.warn(
								"Unable to get runtimeName with error "
										+ e.getMessage(), e);
					}
				}
			} finally {
				pidLock.unlock();
			}
		}
		return pid;
	}

	private static String hostName;
	private static ReentrantLock hostNameLock = new ReentrantLock();

	public static String getHostName() {
		if (hostName == null) {
			hostNameLock.lock();
			try {
				if (hostName == null) {
					try {
						InetAddress address = InetAddress.getLocalHost();
						hostName = address.getHostName();
					} catch (UnknownHostException e) {
						logger.warn("Unable to resolve hostname with error "
								+ e.getMessage(), e);
						hostName = DEFAULT_LOCAL_HOSTNAME;
					}
				}
			} finally {
				hostNameLock.unlock();
			}
		}
		return hostName;
	}

	private static String ipAddress;
	private static ReentrantLock ipAddressLock = new ReentrantLock();

	public static String getIpAddress() {
		if (ipAddress == null) {
			ipAddressLock.lock();
			try {
				if (ipAddress == null) {
					try {
						InetAddress address = InetAddress.getLocalHost();
						ipAddress = address.getHostAddress();
					} catch (UnknownHostException e) {
						logger.warn(
								"Unable to get hostAddress with error "
										+ e.getMessage(), e);
						ipAddress = DEFAULT_LOCAL_IPADDRESS;
					}
				}
			} finally {
				ipAddressLock.unlock();
			}
		}
		return ipAddress;
	}

}
