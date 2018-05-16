package com.ali.dbtech.attach;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class FileFindUtils {

	/**
	 * find absolute path from files(or directories) list with file name
	 * 
	 * @param files
	 * @param fileName
	 * @return
	 */
	public static String getAbsoluteFile(List<File> files, String fileName) {
		Set<String> filesVisited = new HashSet<String>();
		Stack<File> stack = new Stack<File>();
		stack.addAll(files);
		while (!stack.isEmpty()) {
			File file = stack.pop();
			if (file.exists()) {
				String filePath = file.getAbsoluteFile().getAbsolutePath();
				if (!filesVisited.contains(filePath)) {
					filesVisited.add(filePath);
					if (file.isDirectory()) {
						for (File subFile : file.listFiles()) {
							stack.push(subFile);
						}
					} else if (file.getName().equalsIgnoreCase(fileName)) {
						System.out.println("file : [" + filePath + "] found");
						return filePath;
					} else {
					}
				}
			}
		}
		System.out.println("file :[" + fileName + "] not found");
		return null;
	}
}
