package com.bj4.yhh.turtlesoup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Yen-Hsun_Huang on 2015/2/4.
 */
public class StoryContentDialog extends DialogFragment {
    public static final String TAG = "StoryContentDialog";
    private static final String EXTRAS_STORY = "story";

    public static StoryContentDialog getNewInstance(Context context, Story story) {
        StoryContentDialog dialog = new StoryContentDialog();
        Bundle extras = new Bundle();
        extras.putParcelable(EXTRAS_STORY, story);
        dialog.setArguments(extras);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Story story = (Story) getArguments().getParcelable(EXTRAS_STORY);
        return new AlertDialog.Builder(getActivity()).setTitle(story.getTitle()).setMessage(story.getSummary() + "\n" + story.getContent() + "\n" + story.getAnswer()
                + "\n" + story.getIndex() + "\n" + story.hasRead()).create();
    }
}
