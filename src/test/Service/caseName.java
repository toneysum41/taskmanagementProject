package com.tastmanager.test.Service;

import org.springframework.stereotype.Service;

@Service
public class caseName {
     public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;
}
