package com.crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class Crawler implements Callable<List<String>> {

	private String fileName;

	private File directoryName;

	public Crawler(String fileName, File directoryName) {
		this.fileName = fileName;
		this.directoryName = directoryName;
	}

	@Override
	public List<String> call() {
		
		List<String> foundFiles = new ArrayList<>();
		try {

			File[] files = directoryName.listFiles();

			List<Future<List<String>>> taskList = new ArrayList<>();
			if (files != null) {
				for (File file : files) {
					System.out.println(file.getAbsolutePath());
					if (file.isDirectory()) {
						Crawler crawler = new Crawler(fileName, file);
						FutureTask<List<String>> task = new FutureTask<List<String>>(crawler);

						taskList.add(task);

						Thread nextComingThread = new Thread(task);
						nextComingThread.start();
						nextComingThread.join();
					} else {
						if (file.getName().contains(fileName)) {
							foundFiles.add(file.getAbsolutePath());
						}
					}
				}
			}

			for (Future<List<String>> future : taskList) {
				foundFiles.addAll(future.get());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return foundFiles;
	}

}
