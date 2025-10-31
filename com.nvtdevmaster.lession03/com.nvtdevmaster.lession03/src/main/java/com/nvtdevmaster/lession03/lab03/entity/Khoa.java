package com.nvtdevmaster.lession03.lab03.entity;

public class Khoa {
    private String makh;
    private String tenkh;

    public Khoa() {}
    public Khoa(String makh, String tenkh) { this.makh = makh; this.tenkh = tenkh; }

    public String getMakh() { return makh; }
    public void setMakh(String makh) { this.makh = makh; }
    public String getTenkh() { return tenkh; }
    public void setTenkh(String tenkh) { this.tenkh = tenkh; }
}
