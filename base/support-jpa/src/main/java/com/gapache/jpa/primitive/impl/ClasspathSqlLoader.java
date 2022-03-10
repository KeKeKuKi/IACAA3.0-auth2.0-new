package com.gapache.jpa.primitive.impl;

import com.gapache.jpa.primitive.AbstractPathSqlLoader;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author HuSen
 * @since 2021/1/29 10:58 上午
 */
public class ClasspathSqlLoader extends AbstractPathSqlLoader {
	
	private final String path;

	public ClasspathSqlLoader(String path) {
		this.path = path;
	}

	@Override
	protected InputStream in() throws IOException {
		return new ClassPathResource(path).getInputStream();
	}
}
