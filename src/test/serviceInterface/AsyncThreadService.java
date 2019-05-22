package com.tastmanager.test.serviceInterface;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.web.multipart.MultipartFile;

public interface AsyncThreadService {
	/**
	 * Async-execute
	 */
	Future<String> executeUpload(List<Object> list, String saveFileName);
      
}
