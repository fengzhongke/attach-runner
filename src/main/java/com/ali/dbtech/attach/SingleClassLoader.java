package com.ali.dbtech.attach;

import java.util.Set;

public class SingleClassLoader extends ClassLoader {
	private Set<ClassLoader> classLoaders;
	private String className;
	private byte[] bytes;

	public SingleClassLoader(Set<ClassLoader> classLoaders, String className, byte[] bytes) {
		this.classLoaders = classLoaders;
		this.className = className;
		this.bytes = bytes;
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		Class<?> clasz = null;
		if (name.equals(className)) {
			return getClasz(null);
		}
		for (ClassLoader classLoader : classLoaders) {
			try {
				clasz = classLoader.loadClass(name);
			} catch (ClassNotFoundException e) {
			} catch (Exception e) {
			}
		}
		return clasz;
	}

	@SuppressWarnings("unchecked")
	public <T> Class<T> getClasz(Class<T> superClass) throws ClassNotFoundException {
		Class<?> clasz = this.defineClass(className, bytes, 0, bytes.length);
		if (superClass != null && !superClass.isAssignableFrom(clasz)) {
			return null;
		} else {
			return (Class<T>) clasz;
		}
	}
}
