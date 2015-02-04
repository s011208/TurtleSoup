package com.bj4.yhh.turtlesoup;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yen-Hsun_Huang on 2015/2/3.
 */
public class Story implements Parcelable {
    private String mTitle;
    private String mSummary;
    private String mContent;
    private String mAnswer;
    private boolean mHasRead = false;
    private int mIndex = -1;

    public Story(String title, String summary, String content, String answer) {
        setTitle(title);
        setAnswer(answer);
        setContent(content);
        setSummary(summary);
    }

    public Story(String title, String summary, String content, String answer, boolean hasRead) {
        this(title, summary, content, answer);
        setRead(hasRead);
    }

    public Story(String title, String summary, String content, String answer, boolean hasRead, int index) {
        this(title, summary, content, answer, hasRead);
        setIndex(index);
    }

    public Story(Parcel in) {
        String[] data = new String[6];
        in.readStringArray(data);
        setTitle(data[0]);
        setAnswer(data[1]);
        setContent(data[2]);
        setSummary(data[3]);
        setRead(Boolean.valueOf(data[4]));
        setIndex(Integer.valueOf(data[5]));
    }

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        mIndex = index;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.mTitle,
                this.mSummary,
                this.mContent,
                this.mAnswer,
                String.valueOf(this.mHasRead),
                String.valueOf(this.mIndex)});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Story createFromParcel(Parcel in) {
            return new Story(in);
        }

        public Story[] newArray(int size) {
            return new Story[size];
        }
    };
}
