package com.bj4.yhh.turtlesoup;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Yen-Hsun_Huang on 2015/2/4.
 */
public class Utils {
    private Utils() {
    }

    public static boolean writeStringAsFile(Context context, final String fileContents, String fileName) {
        try {
            FileWriter out = new FileWriter(new File(fileName));
            out.write(fileContents);
            out.close();
            return true;
        } catch (IOException e) {
            Log.w("QQQQ", "failed", e);
            return false;
        }
    }

    public static String readFileAsString(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(new File(context.getFilesDir(), fileName)));
            while ((line = in.readLine()) != null) stringBuilder.append(line);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return stringBuilder.toString();
    }
}
