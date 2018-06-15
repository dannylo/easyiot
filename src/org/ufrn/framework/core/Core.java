package org.ufrn.framework.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.ufrn.framework.annotation.CheckMethod;
import org.ufrn.framework.annotation.Comunication;
import org.ufrn.framework.annotation.DiscoveryMethod;
import org.ufrn.framework.annotation.ProxyTranslate;
import org.ufrn.framework.coapserver.SampleCoapServer;
import org.ufrn.framework.comunication.interfaces.IComunication;
import org.ufrn.framework.proxy.interfaces.IProxy;

public class Core {

	private static final String DEFAULT = "DEFAULT";
	private static final String DEFAULT_PACKAGE_PROXY = "org.ufrn.framework.proxy.implementations";
	private static final String PROPERTY_PROXY_DEFINITION_USER = "application.proxy.package";
	private static final String PROPERTY_COMUNICATION_DEFINITION_USER = "application.comunication.package";
	private static final String PROPERTIES_PATH = "config.properties";
	private static final String IGNORE_DEFAULT_PROXYS = "ignore.default";
	

	// private static Logger logger = Logger.getLogger("core");

	public static void start() {
		try {
			SampleCoapServer.getInstance().stop();
			// BasicConfigurator.configure();
			// logger.info("EasyIoT initializing...");
			System.out.println("EasyIoT initializing...");
			Properties props = Core.getProps();
			List<IProxy> proxyIdentifiers = Core.discoveryProxy(props.getProperty(Core.PROPERTY_PROXY_DEFINITION_USER),props.getProperty(Core.IGNORE_DEFAULT_PROXYS));
			proxyIdentifiers.forEach(proxy -> proxy.discoveryAll());
			// Core.verifyAndExecuteComunicationClass();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public static void start(File configs) {
		try {
			SampleCoapServer.getInstance().stop();
			// BasicConfigurator.configure();
			// logger.info("EasyIoT initializing...");
			System.out.println("EasyIoT initializing...");
			Properties props = Core.getProps(configs);
			List<IProxy> proxyIdentifiers = Core.discoveryProxy(props.getProperty(Core.PROPERTY_PROXY_DEFINITION_USER), props.getProperty(Core.IGNORE_DEFAULT_PROXYS));
			proxyIdentifiers.forEach(proxy -> proxy.discoveryAll());
			// Core.verifyAndExecuteComunicationClass();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public static void start(InputStream stream) {
		try {
			SampleCoapServer.getInstance().stop();
			// BasicConfigurator.configure();
			// logger.info("EasyIoT initializing...");
			System.out.println("EasyIoT initializing...");
			Properties props = Core.getProps(stream);
			System.out.println(props.getProperty(Core.IGNORE_DEFAULT_PROXYS));
			List<IProxy> proxyIdentifiers = Core.discoveryProxy(props.getProperty(Core.PROPERTY_PROXY_DEFINITION_USER), props.getProperty(Core.IGNORE_DEFAULT_PROXYS));
			//proxyIdentifiers.forEach(proxy -> proxy.discoveryAll());
			// Core.verifyAndExecuteComunicationClass();
			for(IProxy proxy: proxyIdentifiers) {
				proxy.discoveryAll();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public static void stop() {
		SampleCoapServer.getInstance().stop();
		// logger.info("EasyIoT is stoped.");
	}

	@Deprecated
	private static void verifyAndExecuteComunicationClass() throws IOException, ClassNotFoundException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		Properties props = getProps();
		Core.executeComunicationClass(props.getProperty(Core.PROPERTY_COMUNICATION_DEFINITION_USER));
	}

	private static Properties getProps() throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream(Core.PROPERTIES_PATH);
		props.load(file);
		return props;
	}

	private static Properties getProps(File configs) throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream(configs);
		props.load(file);
		return props;
	}

	private static Properties getProps(InputStream stream) throws IOException {
		Properties props = new Properties();
		props.load(stream);
		return props;
	}

	@Deprecated
	private static void executeComunicationClass(String packageComunication) throws ClassNotFoundException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {

		packageComunication = packageComunication.replace("\"", "");
		packageComunication = packageComunication.replace(";", "");

		Reflections reflections = new Reflections(packageComunication, new SubTypesScanner(false),
				ClasspathHelper.forClassLoader());
		// logger.info("Checking comunication package "+ packageComunication);
		System.out.println("Checking comunication package " + packageComunication);
		Set<Class<?>> clazz = reflections.getSubTypesOf(Object.class);
		List<Method> checkMethods = new ArrayList<>();
		int delay = 0;
		for (Class<?> comunicationClass : clazz) {
			Class<?> entity = Class.forName(comunicationClass.getName());
			if (entity.isAnnotationPresent(Comunication.class)) {
				// logger.info("Comunication class identified: " + entity.getName());
				System.out.println("Comunication class identified: " + entity.getName());
				delay = entity.getAnnotation(Comunication.class).checkDelay();
				IComunication comunication = (IComunication) entity.newInstance();
				for (Method method : entity.getDeclaredMethods()) {
					if (method.isAnnotationPresent(DiscoveryMethod.class)) {
						method.invoke(comunication);
						// logger.info("Discovery method "+ method.getName() + " was executed.");
						System.out.println("Discovery method " + method.getName() + " was executed.");
					} else if (method.isAnnotationPresent(CheckMethod.class)) {
						checkMethods.add(method);
					}
				}
				TimerTask timerTask = new TimerTask() {
					@Override
					public void run() {
						for (Method check : checkMethods) {
							try {
								check.invoke(comunication);
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
					}
				};
				Timer timer = new Timer();
				timer.schedule(timerTask, 0, delay * 1000);
			}
		}

	}

	private static List<IProxy> discoveryProxy(String packageProxy, String ignore)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		System.out.println("PACKAGE PROXY: " + packageProxy);
		ignore = ignore.replace("\"", "");
		List<IProxy> proxys = new ArrayList<>();
		// Reflections reflections = new Reflections(Core.DEFAULT_PACKAGE_PROXY, new
		// SubTypesScanner(false),
		// ClasspathHelper.forClassLoader());
		if (packageProxy.equals(Core.DEFAULT) && !ignore.equals("yes")) {
			// Set<Class<?>> clazz = reflections.getSubTypesOf(Object.class);
			// for (Class<?> proxy : clazz) {
			// Class<?> entity = Class.forName(proxy.getName());
			// if (entity.isAnnotationPresent(ProxyTranslate.class)) {
			// logger.info("Proxy identified: " +
			// entity.getAnnotation(ProxyTranslate.class).description());
			Class<?> entity = Class.forName(Core.DEFAULT_PACKAGE_PROXY + "UPnpProxy");

			System.out.println("Proxy identified: " + entity.getAnnotation(ProxyTranslate.class).description());
			IProxy instance = (IProxy) entity.newInstance();
			proxys.add(instance);
			// }
			// }
		} else {
			// Set<Class<?>> clazz = reflections.getSubTypesOf(Object.class);
			// for (Class<?> proxy : clazz) {
			if (!ignore.equals("yes")) {
				Class<?> entity = Class.forName(Core.DEFAULT_PACKAGE_PROXY + ".UPnpProxy");
				if (entity.isAnnotationPresent(ProxyTranslate.class)) {
					// logger.info("Proxy identified: " +
					// entity.getAnnotation(ProxyTranslate.class).description());
					System.out.println(
							"Proxy identified: " + entity.getAnnotation(ProxyTranslate.class).description());
					IProxy instance = (IProxy) entity.newInstance();
					proxys.add(instance);
				}
			}
			// }

			packageProxy = packageProxy.replace("\"", "");
			// reflections = new Reflections(packageProxy, new SubTypesScanner(false),
			// ClasspathHelper.forClassLoader());

			// clazz = reflections.getSubTypesOf(Object.class);
			// for (Class<?> proxy : clazz) {
			Class<?> entity = Class.forName(packageProxy + ".ProxyBluetooth");
			if (entity.isAnnotationPresent(ProxyTranslate.class)) {
				// logger.info("Proxy identified: " +
				// entity.getAnnotation(ProxyTranslate.class).description());
				System.out.println("Proxy identified: " + entity.getAnnotation(ProxyTranslate.class).description());
				IProxy instance = (IProxy) entity.newInstance();
				proxys.add(instance);
			}
			
		    entity = Class.forName(packageProxy + ".ProxyUPnpForAndroid");
			if (entity.isAnnotationPresent(ProxyTranslate.class)) {
				// logger.info("Proxy identified: " +
				// entity.getAnnotation(ProxyTranslate.class).description());
				System.out.println("Proxy identified: " + entity.getAnnotation(ProxyTranslate.class).description());
				IProxy instance = (IProxy) entity.newInstance();
				proxys.add(instance);
			}
			// }

		}

		return proxys;
	}

	// public static Logger getLogger() {
	// return logger;
	// }
}
