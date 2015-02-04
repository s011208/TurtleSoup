package com.bj4.yhh.turtlesoup;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Yen-Hsun_Huang on 2015/2/3.
 */
public class ParseStoryService extends Service {
    public static final String INTENT_START_PARSE = "com.bj4.yhh.turtlesoup.start_parse";
    public static final String INTENT_EXTRAS_TURTLESOUP = "turtle_soup";

    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flag, int startId) {
        if (intent != null) {
            final String action = intent.getAction();
            if (INTENT_START_PARSE.equals(action)) {
                final Bundle extras = intent.getExtras();
                if (extras != null) {
                    if (extras.getString(INTENT_EXTRAS_TURTLESOUP) != null) {
                        // parse turtle soup
                        new TurtleSoupParser(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            }
        }
        return super.onStartCommand(intent, flag, startId);
    }

    private static class TurtleSoupParser extends AsyncTask<Void, Void, Void> {
        private final Context mContext;
        public TurtleSoupParser(Context context) {
            mContext = context;
        }
        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<Story> stories = new ArrayList<Story>();
            int index = 1;
            while (index < 200) {
                try {
                    final String questionsListUrl = "http://gameschool.cc/puzzle/category/24/?o=date&p=" + Integer.valueOf(index++);
                    final URL url = new URL(questionsListUrl);
                    Document questionsListDoc = Jsoup.parse(url, 5000);
                    Elements questionsUrl = questionsListDoc.select("div[class=puz_list]").select("a[href*=puzzle]");
                    for (Element ele : questionsUrl) {
                        final String questionLink = "http://gameschool.cc" + ele.select("a").first().attr("href");
                        try {
                            final URL questionUrl = new URL(questionLink);
                            Document questionDoc = Jsoup.parse(questionUrl, 5000);
                            Elements whiteStage = questionDoc.select("div[class=whitestage puz_full]");
                            final String title = whiteStage.select("h2").first().ownText();
                            final String content = whiteStage.select("div[class=puz_Q]").first().ownText();
                            final String summary = content.substring(0, 30 >= content.length() ? content.length() : 30);
                            final String answer = whiteStage.select("div[class=puz_A_content]").first().ownText();
                            final boolean hasRead = false;
                            final int questionIndex = Integer.valueOf(ele.select("a").first().attr("href").replace("/", "").replace("puzzle", ""));
                            stories.add(new Story(title, summary, content, answer, hasRead, questionIndex));
                        } catch (Exception e) {
                            Log.w("QQQQ", "failed", e);
                        }
                    }
                    Log.d("QQQQ", "CURRENT INDEX: " + index);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
//            final StoryDatabaseHelper helper = StoryDatabaseHelper.getInstnace(mContext.getApplicationContext());
//            helper.bulkInsert(stories);
//            StoryDatabaseHelper.convertFromStoriesIntoJson(mContext, Environment.getExternalStorageDirectory() + "/raw.txt", stories);
            return null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
