package com.junmei.mylove.updatelover.domain;

import java.io.Serializable;

/**
 * Created by junmei on 2016/9/30.
 */
public class MediaItem implements Serializable {
    private String display_name;
    private long duration;   //!!!注意大Long和小Long不一样
    private long size;
    private String artist;
    private String data;

    private String imgUrl;
    private String desc;

    private int imageID;
    private String tv_sys1;
    private String tv_sys2;
    private String tv_sys3;

    public MediaItem() {
    }

    public MediaItem(String display_name, long duration, long size, String artist, String data) {
        this.display_name = display_name;
        this.duration = duration;
        this.size = size;
        this.artist = artist;
        this.data = data;
    }

    public MediaItem(String display_name, long duration, Long size, String artist, String data, int imageID, String tv_sys1, String tv_sys2, String tv_sys3) {
        this.display_name = display_name;
        this.duration = duration;
        this.size = size;
        this.artist = artist;
        this.data = data;
        this.imageID = imageID;
        this.tv_sys1 = tv_sys1;
        this.tv_sys2 = tv_sys2;
        this.tv_sys3 = tv_sys3;

    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTv_sys1() {
        return tv_sys1;
    }

    public String getTv_sys2() {
        return tv_sys2;
    }

    public String getTv_sys3() {
        return tv_sys3;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }


    public String getDisplay_name() {
        return display_name;
    }

    public long getDuration() {
        return duration;
    }

    public long getSize() {
        return size;
    }

    public String getArtist() {
        return artist;
    }

    public String getData() {
        return data;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public void setDuration(Long description) {
        this.duration = description;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MediaItem{" +
                "display_name='" + display_name + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", artist='" + artist + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
