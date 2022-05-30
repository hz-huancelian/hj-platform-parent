package com.hj.chin.platform.sys;

import com.deepoove.poi.data.RowRenderData;

import java.util.LinkedHashMap;
import java.util.List;

public class DetailData {
    
    // 货品数据
    private List<RowRenderData> goods;
    
    // 人工费数据
    private List<RowRenderData> labors;
    private LinkedHashMap <String, List<RowRenderData>> laborsMap;

    public List<RowRenderData> getGoods() {
        return goods;
    }

    public void setGoods(List<RowRenderData> goods) {
        this.goods = goods;
    }

    public List<RowRenderData> getLabors() {
        return labors;
    }

    public void setLabors(List<RowRenderData> labors) {
        this.labors = labors;
    }

    public LinkedHashMap<String, List<RowRenderData>> getLaborsMap() {
        return laborsMap;
    }

    public void setLaborsMap(LinkedHashMap<String, List<RowRenderData>> laborsMap) {
        this.laborsMap = laborsMap;
    }
}
