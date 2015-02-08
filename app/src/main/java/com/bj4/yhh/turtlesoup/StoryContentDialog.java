package com.bj4.yhh.turtlesoup;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

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
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(initCustomContent(getActivity().getLayoutInflater(), getActivity(), story));
        dialog.getWindow().setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.content_bg));
        return dialog;
    }

    private View initCustomContent(LayoutInflater inflater, Context context, Story story) {
        View parent = inflater.inflate(R.layout.story_dialog, null);
        TextView title = (TextView) parent.findViewById(R.id.title);
        TextView content = (TextView) parent.findViewById(R.id.content);
        final TextView showAnswer = (TextView) parent.findViewById(R.id.show_answer);
        final TextView answer = (TextView) parent.findViewById(R.id.answer);
        title.setText(story.getTitle());
        content.setText(story.getContent());
        answer.setText(story.getAnswer());
        showAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isShowAnswer = answer.getVisibility() == View.VISIBLE;
                if (isShowAnswer) {
                    answer.setVisibility(View.GONE);
                    showAnswer.setText(R.string.show_answer);
                } else {
                    answer.setVisibility(View.VISIBLE);
                    showAnswer.setText(R.string.hide_answer);
                }
            }
        });
        return parent;
    }
}
