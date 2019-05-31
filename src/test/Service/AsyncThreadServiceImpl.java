package com.tastmanager.test.Service;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.tastmanager.test.serviceInterface.AsyncThreadService;

@Service
public class AsyncThreadServiceImpl implements AsyncThreadService{
    private static final Logger logger = LoggerFactory.getLogger(AsyncThreadServiceImpl.class);

	@Override
	@Async(value = "AsyncExecutor")
	public Future<String> executeUpload(List<Object> list, String saveFileName) {
		logger.info("Start upload file!");
		
        String pathDir = "C:\\NTG6TescoFile\\";
        File saveFile = new File(pathDir + saveFileName);
        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();
        }
        try {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(saveFile));
            byte[] bytes = (byte[]) list.get(0);
            out.write(bytes);
            out.flush();
            out.close();
            return new AsyncResult<>("1");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new AsyncResult<>("Fail Upload," + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return new AsyncResult<>("Fail Upload," + e.getMessage());
        }
		
	}

}
