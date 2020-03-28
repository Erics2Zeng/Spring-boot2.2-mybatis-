package com.drawon.mall.service;

import java.util.function.Supplier;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
public  class PageService{

    private Map<String, Object> getDataTable(PageInfo<?> pageInfo) {
        Map<String, Object> rspData = new HashMap<>();
        rspData.put("rows", pageInfo.getList());
        rspData.put("total", pageInfo.getTotal());
        return rspData;
    }

    public Map<String, Object> selectByPageNumSize(int pageNum,int pageSize, Supplier<?> s) {
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<?> pageInfo = new PageInfo<>((List<?>) s.get());
        PageHelper.clearPage();
        return getDataTable(pageInfo);
    }
}
