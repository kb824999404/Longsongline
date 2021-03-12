package com.sitp.longsongline.entity;

public class Poem {
    public String title;
    public String author;
    public String[] contents;
    public Poem(String _title,String _author,String[] _contents){
        title = _title;
        author = _author;
        contents = _contents;
    }
}
