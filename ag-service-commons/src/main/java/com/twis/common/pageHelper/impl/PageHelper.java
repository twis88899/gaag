package com.twis.common.pageHelper.impl;

import com.twis.common.pageHelper.IPage;
import com.twis.common.pageHelper.IPageHelper;


public class PageHelper implements IPageHelper {
	
	IPage page;
	public PageHelper(int _totalrows, int _currentPage, int _pageSize) {
		setPage(_totalrows, _currentPage, _pageSize);
	}
	
	public PageHelper(int _totalrows, int _currentPage) {
		setPage( _totalrows,  _currentPage, 10);
	}
	
	public void setPage(int _totalrows, int _currentPage, int _pageSize){
		page = new Page(_totalrows, _currentPage, _pageSize);
	}
	
	public IPage getPage() {
		return page;
	}

}
