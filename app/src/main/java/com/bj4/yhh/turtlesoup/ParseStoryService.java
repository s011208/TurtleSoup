package com.bj4.yhh.turtlesoup;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
                        new TurtleSoupParser().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            }
        }
        return super.onStartCommand(intent, flag, startId);
    }

    private static class TurtleSoupParser extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            int index = 1;
            while (true) {
                try {
                    final URL url = new URL("http://gameschool.cc/puzzle/category/24/?o=date&p=" + Integer.valueOf(index++));
                    Document xmlDoc = Jsoup.parse(url, 5000);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
            return null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
