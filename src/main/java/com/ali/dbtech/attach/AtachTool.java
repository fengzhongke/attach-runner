package com.ali.dbtech.attach;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

/**
 * java -jar /home/hanlang.hl/agent-tool.jar 242206 com.InvoiceTest
 * 
 * @author hanlang.hl
 *
 */
public class AtachTool {

	public static void main(String[] args) throws Exception {

		String javaHome = System.getProperty("java.home");
		if (javaHome == null) {
			System.out.println("evironment java.home is not set !!!");
			System.exit(1);
		}
		File javaHomeDir = new File(javaHome);
		if (!javaHomeDir.exists() || !javaHomeDir.isDirectory()) {
			System.out.println("java.home does not exists or not derectory !!!");
			System.exit(1);
		}
		String toolsPath = FileFindUtils.getAbsoluteFile(Arrays.asList(javaHomeDir.getParentFile()), "tools.jar");
		if (toolsPath == null) {
			System.out.println("tools.jar not found in java.home[" + javaHome + "]!!!");
			System.exit(1);
		}
		String path = AtachTool.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		String pid = args[0];
		String runnerName = args[1];
		URLClassLoader loader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
		Method addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
		addUrlMethod.setAccessible(true);
		addUrlMethod.invoke(loader, new File(toolsPath).toURI().toURL());

		Class<?> vmClasz = loader.loadClass("com.sun.tools.attach.VirtualMachine");
		Method detachMethod = null;
		Object vm = null;
		Method attachMethod = null;
		Method loadMethod = null;
		try {
			detachMethod = vmClasz.getDeclaredMethod("detach");
			attachMethod = vmClasz.getDeclaredMethod("attach", String.class);
			loadMethod = vmClasz.getDeclaredMethod("loadAgent", String.class, String.class);
			vm = attachMethod.invoke(null, pid);
			loadMethod.invoke(vm, path, runnerName);
		} finally {
			try {
				detachMethod.invoke(vm);
			} finally {
			}
		}

	}

}
