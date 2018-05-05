package com.twis.common.pageHelper.impl;

import com.twis.common.pageHelper.IPage;

import java.io.Serializable;


public class Page implements IPage, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int totalRows; // 总行数
    private int pageSize = 10; // 每页显示的行数
    private int currentPage; // 当前页
    private int totalPages; // 总页数
    private int startRow; // 当前页在数据中的起始行
    private int endRow; // 当前页在数据中的起始行

    public Page() {

    }

    public Page(int _totalrows, int _currentPage, int _pageSize) {
        this.pageSize = _pageSize;
        this.currentPage = _currentPage;
       // this.totalRows = _totalrows==0?1:_totalrows;
        this.totalRows = _totalrows;
        
        this.totalPages = this.totalRows / pageSize;
        int mod = totalRows % pageSize;
        if (mod > 0)
            totalPages++;
        startRow = ((currentPage - 1) * pageSize + 1);
        endRow = pageSize * currentPage;
    }

    public int getTotalRows() {
        //return totalRows==0?1:totalRows;
    	return totalRows;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public String first() {
        return 1 + "";
    }

    public String previous() {
        if (currentPage > 1 && currentPage <= totalPages) {
            return currentPage - 1 + "";
        }
        return null;
    }

    public String next() {
        if (currentPage < totalPages && totalPages > 1) {
            return currentPage + 1 + "";
        }
        return null;
    }

    public String last() {
        return totalPages + "";
    }

}
