package com.example.lenovo.zhihu.data;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/1/30.
 */

public class MyQuestion {
    private int id;
    private String title;
    private String content;
    private String date;
    private String recent;
    private int answerCount;
    private int authorId;
    private int excitingCount;
    private int naiveCount;
    private String authorName;
    private String authorAvatarUrlString;
    public boolean isNaive;
    public boolean isExciting;
    public boolean isFavourite;

    public boolean isNaive() {
        return isNaive;
    }

    public String getAuthorAvatarUrlString() {
        return authorAvatarUrlString;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public boolean isExciting() {

        return isExciting;
    }

    public void setAuthorAvatarUrlString(String authorAvatarUrlString) {
        this.authorAvatarUrlString = authorAvatarUrlString;
    }

    public String getAuthorName() {

        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getNaiveCount() {

        return naiveCount;
    }

    public void setNaiveCount(int naiveCount) {
        this.naiveCount = naiveCount;
    }

    public int getExcitingCount() {

        return excitingCount;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public void setExciting(boolean exciting) {

        isExciting = exciting;
    }

    public void setNaive(boolean naive) {

        isNaive = naive;
    }


    public void setExcitingCount(int excitingCount) {
        this.excitingCount = excitingCount;
    }

    public int getAuthorId() {

        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getAnswerCount() {

        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public String getRecent() {

        return recent;
    }

    public void setRecent(String recent) {
        this.recent = recent;
    }

    public String getDate() {

        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
