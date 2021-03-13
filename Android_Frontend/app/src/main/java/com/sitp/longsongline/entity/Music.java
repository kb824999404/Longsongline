package com.sitp.longsongline.entity;

import java.io.Serializable;

public class Music implements Serializable {
    private int ID;
    private String title;
    private String path;
    private String time;
    private String content;
    public Music(int _ID,String _title,String _path, String _time,String _content){
        ID = _ID;
        title = _title;
        path = _path;
        time = _time;
        content = _content;
    }
    public final void setID(int value){
        ID = value;
    }
    public final int getID(){
        return ID;
    }
    public final void setTitle(String value){
        title = value;
    }
    public final String getTitle(){
        return title;
    }
    public final void setPath(String value){path=value;}
    public final String getPath(){return path;}
    public final void setTime(String value){
        time = value;
    }
    public final String getTime(){
        return time;
    }
    public final void setContent(String value){
        content = value;
    }
    public final String getContent(){
        return content;
    }
}
