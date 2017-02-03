package com.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CrawlerUtil {

	private static CrawlerUtil INSTANCE;

	public static CrawlerUtil getInstance() {
		return INSTANCE == null ? INSTANCE = new CrawlerUtil() : INSTANCE;
	}

	public void initCrawler() {

		try {

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Enter file name to search: \n");
			String fileName = br.readLine();

			File[] paths = File.listRoots();
			Arrays.stream(paths).parallel().forEach(path -> {
				System.out.println(path);
				Crawler crawler = new Crawler(fileName, path);

				//FutureTask<List<String>> task = new FutureTask<List<String>>(crawler);
				//Thread thread = new Thread(task);
				//thread.start();
				ExecutorService service = Executors.newFixedThreadPool(100);
				Future<List<String>> task = service.submit(crawler);
				

				List<String> results;
				try {
					results = task.get();
					System.out.println("Results:");
					for (String result : results) {
						System.out.println(result);
					}
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				service.shutdown();
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		long start = System.nanoTime();
		CrawlerUtil.getInstance().initCrawler();
		long end = System.nanoTime();
		System.out.println("Total Time: " + ((end - start) / 1000000000) + " seconds");
	}
}
