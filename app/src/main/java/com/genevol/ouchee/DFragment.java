package com.genevol.ouchee;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by RussellM on 5/06/2016.
 */

interface MyDialogCloseListener
{
    public void handleDialogClose(DialogInterface dialog);//or whatever args you want
}

public class DFragment extends DialogFragment {

    int score,best;
    boolean finished;

    public DFragment() {

    }
    public DFragment(int inScore,int inBest,boolean inFinished ) {
        score = inScore;
        best = inBest;
        finished = inFinished;


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setCancelable(false);

        Window window = getDialog().getWindow();

        // set "origin" to top left corner, so to speak
        window.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);

        // after that, setting values for x and y works "naturally"
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 300;
        params.y = 100;
        window.setAttributes(params);

        TextView textView = (TextView) this.getDialog().findViewById(android.R.id.title);
        if(textView != null)
        {
            textView.setGravity(Gravity.CENTER);
        }

        Log.d("pos", String.format("Positioning DialogFragment to: x %d; y %d", params.x, params.y));

        View rootView = inflater.inflate(R.layout.dialogfragment, container,
                false);

        if (finished) {
            getDialog().setTitle("Time's Up");
        }
        else {
            getDialog().setTitle("Ouched!");
        }
        // Do something else

        Button b = (Button)rootView.findViewById(R.id.restartbutton);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                dismiss();
            }
        });

        TextView sv = (TextView)rootView.findViewById(R.id.scoreViewF);
        TextView bv = (TextView)rootView.findViewById(R.id.bestViewF);

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/vtkschalk79.ttf");

        sv.setTypeface(custom_font);
        bv.setTypeface(custom_font);

        sv.setText("Score: " + score);
        bv.setText("Best: " + best);

        if (finished) {

        }
        else {
            final ImageView oucheeIV = (ImageView) rootView.findViewById(R.id.oucheeImageViewF);
            oucheeIV.setImageResource(R.drawable.ouchee); //handtest);
        }






        return rootView;
    }

    public void onDismiss(DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof MyDialogCloseListener)
            ((MyDialogCloseListener) activity).handleDialogClose(dialog);

    }
}