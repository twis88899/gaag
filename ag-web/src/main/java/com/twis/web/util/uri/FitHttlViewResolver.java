package com.twis.web.util.uri;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import httl.web.springmvc.HttlView;
import httl.web.springmvc.HttlViewResolver;

/**
 * 缺省的实现
 * @author yxm
 * @since 2018/3/18
 */
public class FitHttlViewResolver extends HttlViewResolver {

	private URLBrokerFactory urlBrokerBean;

	public URLBrokerFactory getUrlBrokerBean() {
		return urlBrokerBean;
	}

	public void setUrlBrokerBean(URLBrokerFactory urlBrokerBean) {
		this.urlBrokerBean = urlBrokerBean;
	}

	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		HttlView httlView = (HttlView)super.buildView(viewName);
		httlView.addStaticAttribute("UrlBroker", urlBrokerBean);
		httlView.addStaticAttribute("DateUtils", DateUtils.class);
		httlView.addStaticAttribute("StringUtils", StringUtils.class);
		httlView.addStaticAttribute("CollectionUtils", CollectionUtils.class);
		httlView.addStaticAttribute("Math", Math.class);
		return httlView;
	}
}