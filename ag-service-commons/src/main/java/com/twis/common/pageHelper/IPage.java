package com.twis.common.pageHelper;

public interface IPage {

	public  int getTotalRows();

	public  int getPageSize();

	public  int getCurrentPage();

	public  int getTotalPages();

	public  int getStartRow();

	public  int getEndRow();

	public  String first();

	public  String previous();

	public  String next();

	public  String last();

}