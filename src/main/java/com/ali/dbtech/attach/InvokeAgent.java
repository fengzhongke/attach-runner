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

public class InvokeAgent {

	public static void agentmain(String className, Instrumentation inst) {
		try {
			String classFile = className;
			if (className.indexOf(".") > 0) {
				classFile = className.substring(className.lastIndexOf('.') + 1);
			}
			classFile += ".class";
			Class<?>[] claszes = inst.getAllLoadedClasses();
			Set<String> paths = new HashSet<String>();
			Set<ClassLoader> loaders = new HashSet<ClassLoader>();
			for (Class<?> clasz : claszes) {
				ClassLoader classLoader = clasz.getClassLoader();
				if (classLoader != null && loaders.add(classLoader)) {
					Enumeration<URL> res = classLoader.getResources("");
					while (res.hasMoreElements()) {
						URL url = res.nextElement();
						String fileName = url.getFile();
						paths.add(fileName);
					}
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
				Class<Runnable> clasz = new SingleClassLoader<Runnable>(loaders, className, bytes, Runnable.class)
						.getClasz();
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
