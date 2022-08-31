package com.adalab.examination.entity;

public class TaggedImage {
    String imgName;
    String imgVersion;


    public TaggedImage(String s) {
        if (s.contains(":")) {
            String[] split = s.split(":");
            imgName = split[0];
            imgVersion = split[1];
        } else {
            imgName = s;
            imgVersion = "";
        }

    }

    public String getImgName() {
        return imgName;
    }

    public String getImgVersion() {
        return imgVersion;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public void setImgVersion(String imgVersion) {
        this.imgVersion = imgVersion;
    }
}
