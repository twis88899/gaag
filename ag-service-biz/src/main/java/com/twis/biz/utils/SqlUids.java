package com.twis.biz.utils;

import com.twis.common.IDFactory;
import com.twis.common.SnowflakeIdWorker;

public class SqlUids {
	private final static IDFactory worker = new SnowflakeIdWorker(2095,999);
	public static Long nextId() {
		return worker.nextId();
	}
}
