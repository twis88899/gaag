package com.twis.web.util.uri;


/**
 * 缺省的实现
 * @author yxm
 * @since 2018/3/18
 */
public class DefaultURLBroker extends URLBroker {

	public DefaultURLBroker(URLBroker urlBroker) {
		super(urlBroker);
	}

	public DefaultURLBroker() {

	}

	@Override
	public URLBroker newInstance() {
		return new DefaultURLBroker(this);
	}

}
