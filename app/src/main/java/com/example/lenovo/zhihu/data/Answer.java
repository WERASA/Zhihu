package com.example.lenovo.zhihu.data;

/**
 * Created by lenovo on 2017/2/6.
 */

public class Answer {
    private int ID;
    private  String content;
    private  int exciting;
    private  int naive;
    public  boolean isExitied;
    public  boolean isNaive;
    private  String authorName;
    private  int  authorId;

    public String getUserHeadUrl() {
        return userHeadUrl;
    }

    public void setUserHeadUrl(String userHeadUrl) {
        this.userHeadUrl = userHeadUrl;
    }

    private   String userHeadUrl;

    public int getIsBest() {
        return isBest;
    }

    public void setIsBest(int isBest) {
        this.isBest = isBest;
    }

    private int isBest;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public boolean isNaive() {
        return isNaive;
    }

    public void setNaive(boolean naive) {
        isNaive = naive;
    }

    public boolean isExitied() {

        return isExitied;
    }

    public void setExitied(boolean exitied) {
        isExitied = exitied;
    }

    public int getNaive() {

        return naive;
    }

    public void setNaive(int naive) {
        this.naive = naive;
    }

    public int getExciting() {

        return exciting;
    }

    public void setExciting(int exciting) {
        this.exciting = exciting;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getID() {

        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
