package com.bj4.yhh.turtlesoup;

/**
 * Created by Yen-Hsun_Huang on 2015/2/3.
 */
public class Story {
    private String mTitle;
    private String mSummary;
    private String mContent;
    private String mAnswer;
    private boolean mHasRead;

    public Story(String title, String summary, String content, String answer) {
        setTitle(title);
        setAnswer(answer);
        setContent(content);
        setSummary(summary);
        setRead(false);
    }

    public Story(String title, String summary, String content, String answer, boolean hasRead) {
        this(title, summary, content, answer);
        setRead(hasRead);
    }

    public void setRead(boolean read) {
        mHasRead = read;
    }

    public boolean hasRead() {
        return mHasRead;
    }

    public void setAnswer(String mAnswer) {
        this.mAnswer = mAnswer;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public void setSummary(String mSummary) {
        this.mSummary = mSummary;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public String getContent() {
        return mContent;
    }

    public String getSummary() {
        return mSummary;
    }

    public String getTitle() {
        return mTitle;
    }
}
