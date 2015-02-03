package com.bj4.yhh.turtlesoup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Yen-Hsun_Huang on 2015/2/3.
 */
public class StoryDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "stories.db";
    private static final int DATABASE_VERSION = 1;
    private static StoryDatabaseHelper sInstance;

    public static synchronized StoryDatabaseHelper getInstnace(Context context) {
        if (sInstance == null) {
            sInstance = new StoryDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private StoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // table story
    private static final String TABLE_STORY = "story";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_SUMMARY = "summary";
    private static final String COLUMN_ANSWER = "answer";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_HAS_READ = "hasread";
    public static final int RESULT_READ = 1;
    public static final int RESULT_NOT_READ = 0;

    private void createTablesIfNeeded() {
        // table story
        getWritableDatabase().execSQL("CREATE TABLE if not exists " + TABLE_STORY
                + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_HAS_READ + " INTEGER DEFAULT " + RESULT_NOT_READ + ", "
                + COLUMN_TITLE + " TEXT NOT NULL, "
                + COLUMN_SUMMARY + " TEXT NOT NULL, "
                + COLUMN_ANSWER + " TEXT NOT NULL, "
                + COLUMN_CONTENT + " TEXT NOT NULL)");
    }

    private static ContentValues convertFromStoryIntoContentValues(Story story) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, story.getTitle());
        cv.put(COLUMN_SUMMARY, story.getSummary());
        cv.put(COLUMN_ANSWER, story.getAnswer());
        cv.put(COLUMN_CONTENT, story.getContent());
        cv.put(COLUMN_HAS_READ, story.hasRead() ? RESULT_READ : RESULT_NOT_READ);
        return cv;
    }

    private static Story convertFromContentValuesIntoStory(ContentValues cv) {
        return new Story(cv.getAsString(COLUMN_TITLE),
                cv.getAsString(COLUMN_SUMMARY),
                cv.getAsString(COLUMN_CONTENT),
                cv.getAsString(COLUMN_ANSWER),
                cv.getAsInteger(COLUMN_HAS_READ) == RESULT_READ);
    }

    public int bulkInsert(ArrayList<Story> stories) {
        if (stories == null || stories.size() == 0)
            return 0;
        final ArrayList<ContentValues> values = new ArrayList<ContentValues>();
        for (Story story : stories) {
            values.add(convertFromStoryIntoContentValues(story));
        }
        final SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (ContentValues cv : values) {
                long newID = db.insertOrThrow(TABLE_STORY, null, cv);
                if (newID <= 0) {
                    throw new SQLException("Failed to insert row");
                }
            }
            db.setTransactionSuccessful();
            return values.size();
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Story> queryStories(final int start, final int offset) {
        final ArrayList<Story> rtn = new ArrayList<Story>();
        final SQLiteDatabase db = getWritableDatabase();
        Cursor data = db.rawQuery("select * from " + TABLE_STORY + " ", null);
        if (data != null) {
            try {
                final int titleIndex = data.getColumnIndex(COLUMN_TITLE);
                final int summaryIndex = data.getColumnIndex(COLUMN_SUMMARY);
                final int contentIndex = data.getColumnIndex(COLUMN_CONTENT);
                final int answerIndex = data.getColumnIndex(COLUMN_ANSWER);
                final int hasReadIndex = data.getColumnIndex(COLUMN_HAS_READ);
                while (data.moveToNext()) {
                    rtn.add(new Story(data.getString(titleIndex)
                            , data.getString(summaryIndex)
                            , data.getString(contentIndex)
                            , data.getString(answerIndex)
                            , data.getInt(hasReadIndex) == RESULT_READ));
                }
            } finally {
                data.close();
            }
        }
        return rtn;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTablesIfNeeded();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
