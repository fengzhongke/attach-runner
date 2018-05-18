package com.ali.dbtech.attach;

import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.jar.JarFile;

public class InvokeAgent {

	/**
	 * do all logic in a new class by new a classLoader
	 * 
	 * @see {@linkplain #AgentTool}
	 * @param className
	 * @param inst
	 */
	public static void agentmain(String className, Instrumentation inst) {
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(AgentTool.class.getProtectionDomain().getCodeSource().getLocation().getFile());
			InputStream in = jarFile
					.getInputStream(jarFile.getJarEntry(AgentTool.class.getName().replaceAll("\\.", "/") + ".class"));
			byte[] bytes = new byte[in.available()];
			in.read(bytes);
			Class<Runnable> clasz = new SingleClassLoader(
					new HashSet<ClassLoader>(Arrays.asList(InvokeAgent.class.getClassLoader())),
					AgentTool.class.getName(), bytes).getClasz(null);
			Method method = clasz.getDeclaredMethod("attach", String.class, Instrumentation.class);
			method.invoke(null, className, inst);
		} catch (Throwable t) {
			t.printStackTrace();
			if (jarFile != null) {
				try {
					jarFile.close();
				} catch (IOException e) {
				}
			}
		}

	}

}
