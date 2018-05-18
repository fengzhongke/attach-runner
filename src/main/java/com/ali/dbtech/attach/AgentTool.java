package com.ali.dbtech.attach;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AgentTool {

	/**
	 * all logic
	 * 
	 * @param className
	 * @param inst
	 */
	public static void attach(String className, Instrumentation inst) {
		System.out.println("hello world");
		try {
			Set<ClassLoader> loaders = new HashSet<ClassLoader>();
			for (Class<?> clasz : inst.getAllLoadedClasses()) {
				ClassLoader classLoader = clasz.getClassLoader();
				if (classLoader != null) {
					loaders.add(classLoader);
				}
			}
			String classFile = className;
			if (className.indexOf(".") > 0) {
				classFile = className.substring(className.lastIndexOf('.') + 1);
			}
			classFile += ".class";
			Set<String> paths = new HashSet<String>();
			for (ClassLoader loader : loaders) {
				Enumeration<URL> res = loader.getResources("");
				while (res.hasMoreElements()) {
					URL url = res.nextElement();
					String fileName = url.getFile();
					paths.add(fileName);
				}
			}

			InputStream in = null;
			try {
				List<File> files = new ArrayList<File>();
				for (String path : paths) {
					System.out.println("path : " + path);
					File file = new File(path);
					if (file.exists()) {
						files.add(file);
					}
				}
				classFile = FileFindUtils.getAbsoluteFile(files, classFile);
				in = new FileInputStream(classFile);

				byte[] bytes = new byte[in.available()];
				in.read(bytes);
				Class<Runnable> clasz = new SingleClassLoader(loaders, className, bytes).getClasz(Runnable.class);
				if (clasz != null) {
					// System.out.println("start run " + className);
					clasz.newInstance().run();
					// System.out.println("end run " + className);
				} else {
					System.out.println("class " + className + " is not Runnable, only Runnable can run");
				}

			} catch (Exception e) {
				System.out.println("class file exception : " + classFile);
				e.printStackTrace();
			} finally {
				if (in != null) {
					in.close();
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
