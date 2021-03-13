package com.sitp.longsongline.entity;

import java.io.Serializable;
import java.io.StringReader;

public class Music implements Serializable {
    private int ID;
    private String title;
    private String path;
    private String time;
    private String content;
    private int status;
    public Music(int _ID,String _title,String _path, String _time,String _content,int _status){
        ID = _ID;
        title = _title;
        path = _path;
        time = _time;
        content = _content;
        status = _status;
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
    public final void setStatus(int value){ status = value; }
    public final int getStatus(){ return status; }
}
