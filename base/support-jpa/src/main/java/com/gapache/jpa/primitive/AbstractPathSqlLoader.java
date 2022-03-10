package com.gapache.jpa.primitive;

import com.gapache.commons.utils.IStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author HuSen
 * @since 2021/1/29 10:45 上午
 */
@Slf4j
public abstract class AbstractPathSqlLoader implements SqlLoader {
	
	private boolean loaded;
	
	@Override
	public String loading() {
		try {
			byte[] bytes = FileCopyUtils.copyToByteArray(in());
			String sql = IStringUtils.newString(bytes);
			this.loaded = true;
			return sql;
		} catch (Exception e) {
			log.error("loading error.", e);
			return null;
		}
	}

	@Override
	public boolean loaded() {
		return loaded;
	}

	/**
	 * sql文件的流
	 * 子类必须实现该方法
	 *
	 * @return sql文件的流
	 */
	protected abstract InputStream in() throws IOException;
}
