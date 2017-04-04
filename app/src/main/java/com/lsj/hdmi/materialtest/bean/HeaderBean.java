package com.lsj.hdmi.materialtest.bean;

/**
 * Created by hdmi on 17-3-28.
 */
public class HeaderBean {
    private static final int DEFAULT_HEADER_CACHE_SIZE=5;
    private int viewType;
    private Object data;
    private int cacheSize;


    public HeaderBean(int viewType, Object data, int cacheSize) {
        this.viewType = viewType;
        this.data = data;
        this.cacheSize = cacheSize;
    }

    public HeaderBean(int viewType, Object data) {
        this(viewType,data,DEFAULT_HEADER_CACHE_SIZE);
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }
}
