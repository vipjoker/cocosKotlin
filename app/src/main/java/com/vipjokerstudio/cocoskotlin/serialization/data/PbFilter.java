package com.vipjokerstudio.cocoskotlin.serialization.data;

public class PbFilter {
    private int cateagoryBits;
    private int groupIndex;
    private int maskBits;

    public int getCategoryBits() {
        return cateagoryBits;
    }

    public int getGroupIndex() {
        return groupIndex;
    }

    public int getMaskBits() {
        return maskBits;
    }

    public void setCategoryBits(int categoryBits) {
        this.cateagoryBits = categoryBits;
    }

    public void setGroupIndex(int groupIndex) {
        this.groupIndex = groupIndex;
    }

    public void setMaskBits(int maskBits) {
        this.maskBits = maskBits;
    }
}
