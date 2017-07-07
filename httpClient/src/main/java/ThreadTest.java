/**
 * Created by Administrator on 2017/6/22.
 */

import java.io.File;
import java.io.IOException;
import java.util.List;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.io.Files;
import com.google.common.base.Charsets;

public class ThreadTest {
    //check if this file it exists
    public void test() {
        String dirName = "d://IntelliJ IDEA//httpClient";
        File dir = new File(dirName);
        File[] files = dir.listFiles(new java.io.FileFilter() {

            public boolean accept(File file) {
                if(file.getName().endsWith(".json")){
                    return true;
                }

                return false;
            }
        });

        // use ThreadPool tp read the data of a file
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for(final File file:files)
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        String fileName = file.getName();
                        List<String> lines = Files.readLines(file, Charsets.UTF_8);
                        for (int i = 0; i < lines.size(); i++)
                           System.out.println(lines.get(i));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        executor.shutdown();
    }
}