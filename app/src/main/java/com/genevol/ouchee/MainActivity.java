package com.genevol.ouchee;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity implements Runnable,MyDialogCloseListener  {

    int best = 0;

    int soundToPlay; //sound to play in background thread

    SoundPool sp1;
    SoundPool sp2;
    SoundPool sp3;
    SoundPool sp4;

    //MediaPlayer mp;
    MediaPlayer mp;



    /** soundId for Later handling of sound pool **/
    int soundId1;
    int soundId2;
    int soundId3;
    int soundId4;
    int soundId1Base;

    CountDownTimer ct;

    int countDownSecs = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("On Create", "app on create");

        /*
        if (!isTaskRoot()) {
            //Android launched another  instance of the root activity into an existing task
            Log.d("not is task root", "not is task root");
            finish();
            return;
        }
        */

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-5020947627257263/2626567233 ");

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("AC98C820A50B4AD8A2106EDE96FB87D4")  // An example device ID
                .build();


        AdView mAdView = (AdView) findViewById(R.id.adView);
        // AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mp = MediaPlayer.create(this,R.raw.ttf2);
        mp.setLooping(true);


        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/vtkschalk79.ttf");
        TextView sv = (TextView) findViewById(R.id.scoreView);
        sv.setTypeface(custom_font);
        TextView rv = (TextView) findViewById(R.id.roundView);
        rv.setTypeface(custom_font);
        TextView cv = (TextView) findViewById(R.id.countdownView);
        cv.setTypeface(custom_font);
        TextView bv = (TextView) findViewById(R.id.bestView);
        bv.setTypeface(custom_font);
        Switch sw = (Switch) findViewById(R.id.switch1);
        sw.setTypeface(custom_font);


        float dpi = getResources().getDisplayMetrics().density;
        Log.d("dpi scale: ","" +dpi);
        float r = dpi / 1.5f;



        sp1 = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        //sp2 = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        //sp3 = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        //sp4 = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        /** soundId for Later handling of sound pool **/
        soundId1 = sp1.load(this, R.raw.correct, 1); // in 2nd param u have to pass your desire ringtone
        soundId2 = sp1.load(this,R.raw.wrong,1);
        soundId3 = sp1.load(this,R.raw.coinpurchase,1);
        soundId4 = sp1.load(this,R.raw.bad,1);
        soundId1Base = sp1.load(this,R.raw.ding,1);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final Chronometer cr = (Chronometer) findViewById(R.id.chronometer);


        cv = (TextView)findViewById(R.id.countdownView);
        int millisUntilFinished = countDownSecs * 1000;
        int seconds = (int) (millisUntilFinished / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        cv.setText("" + String.format("%02d", minutes)
                + ":" + String.format("%02d", seconds));

        final HandImageView imageView   = (HandImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.hando); //handtest);

        final ImageView oucheeIV = (ImageView)findViewById(R.id.oucheeImageView);
        oucheeIV.setImageResource(R.drawable.ouchee); //handtest);


        int finalHeight = imageView.getMeasuredHeight();
        int finalWidth = imageView.getMeasuredWidth();
        Log.d("Image attributes","im w: " + finalWidth + " im h: " + finalHeight);

        final Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        int pixel = bitmap.getPixel(100, 100);
        Log.d("pixelinit","" + pixel);

        final MediaPlayer mp1,mp2;
        mp1 = MediaPlayer.create(MainActivity.this, R.raw.panpipeup);
        mp2 = MediaPlayer.create(MainActivity.this, R.raw.wrong);

        final Hand h = new Hand();

        ct = new CountDownTimer(countDownSecs* 1000+1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                TextView cv = (TextView)findViewById(R.id.countdownView);

                if (seconds <= 5) {
                    cv.setTextColor(Color.RED);
                }
                else {
                    cv.setTextColor(Color.BLACK);
                }

                cv.setText("" + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {


                Song s = new Song(mp,117);
                Log.d("Measured tempo posn","" + mp.getCurrentPosition() + " beat: " + s.getCurrentBeatNum());
                TextView cv = (TextView)findViewById(R.id.countdownView);
                cv.setText("00:00");
                int currScore = h.score;

                h.reset();
                // cr.stop();
                this.cancel();
                h.prepareToStart();
                //imageView.readyToStart = true;
                //imageView.invalidate();
                mp.pause();
                mp.seekTo(0);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                DFragment dFragment = new DFragment(currScore,best,true);
                // Show DialogFragment
                dFragment.show(ft, "Dialog Fragment");
            }
        };

        sw = (Switch) findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                //FragmentManager fm = getSupportFragmentManager();


                h.reset();
                ct.cancel();
                h.prepareToStart();

                TextView cv = (TextView) findViewById(R.id.countdownView);

                if (isChecked) {
                    int millisUntilFinished = countDownSecs * 1000;
                    int seconds = (int) (millisUntilFinished / 1000);
                    int minutes = seconds / 60;
                    seconds = seconds % 60;
                    cv.setText("" + String.format("%02d", minutes)
                            + ":" + String.format("%02d", seconds));
                    cv.setVisibility(View.VISIBLE);
                    ct.cancel();


                }
                else {
                    cv.setVisibility(View.INVISIBLE);
                    ct.cancel();

                }

            }
        });





        /* russell*/
        /*
        h.addFinger(new Finger(new Point(103,373),new Point(28,289),this));
        h.addFinger(new Finger(new Point(159,309),new Point(145,175),this));
        h.addFinger(new Finger(new Point(257,264),new Point(220,143),this));
        h.addFinger(new Finger(new Point(297,162),new Point(280,287),this));
        h.addFinger(new Finger(new Point(320,323),new Point(389,257),this));
        */
        /*cartoon
        h.addFinger(new Finger(new Point(152,350),new Point(54,247),this));
        h.addFinger(new Finger(new Point(201,296),new Point(110,89),this));
        h.addFinger(new Finger(new Point(236,292),new Point(328,99),this));
        h.addFinger(new Finger(new Point(318,292),new Point(382,146),this));
        h.addFinger(new Finger(new Point(353,312),new Point(422,268),this));
        */
        /*dominique*/
        /*
        h.addFinger(new Finger(new Point(134,251),new Point(31,211),this));
        h.addFinger(new Finger(new Point(196,164),new Point(171,39),this));
        h.addFinger(new Finger(new Point(256,149),new Point(262,22),this));
        h.addFinger(new Finger(new Point(304,158),new Point(355,59),this));
        h.addFinger(new Finger(new Point(331,217),new Point(397,164),this));
        */

        /*Olivia*/
        h.addFinger(new Finger(new Point(125,285),new Point(43,234),this));
        h.addFinger(new Finger(new Point(194,168),new Point(152,75),this));
        h.addFinger(new Finger(new Point(248,147),new Point(253,50),this));
        h.addFinger(new Finger(new Point(289,166),new Point(326,79),this));
        h.addFinger(new Finger(new Point(315,218),new Point(380,165),this));



        final Finger fing = new Finger(new Point(159,309),new Point(145,175),this);

        imageView.mDetector = new GestureDetectorCompat(this,imageView);
        // Set the gesture detector as the double tap
        // listener.
        imageView.mDetector.setOnDoubleTapListener(imageView);


        imageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    Log.d("Touch Event", "Touch event at " + x + " " + y);


                    float eventX = event.getX();
                    float eventY = event.getY();
                    float[] eventXY = new float[]{eventX, eventY};

                    Matrix invertMatrix = new Matrix();
                    ((ImageView) v).getImageMatrix().invert(invertMatrix);

                    invertMatrix.mapPoints(eventXY);
                    int xInv = Integer.valueOf((int) eventXY[0]);
                    int yInv = Integer.valueOf((int) eventXY[1]);

                    Log.d("touch inv", "x: " + xInv + " y: " + yInv);


                    int pixel = bitmap.getPixel(xInv, yInv);
                    Log.d("pixel", "" + pixel);


                    //then do what you want with the pixel data, e.g
                    int redValue = Color.red(pixel);
                    int blueValue = Color.blue(pixel);
                    int greenValue = Color.green(pixel);
                    Log.d("col", redValue + " " + blueValue + " " + greenValue);


                    TextView tv = (TextView) findViewById(R.id.textView);
                    if (pixel == 0) {
                        tv.setText("No problems");
                        //TextView ov = (TextView)findViewById(R.id.oucheeView);
                        //ov.setText("");
                        ImageView iv = (ImageView) findViewById(R.id.oucheeImageView);
                        //iv.setVisibility(View.INVISIBLE);
                        /*
                        if (mp2.isPlaying()) {
                            mp2.stop();
                        }

                        if (mp1.isPlaying()) {
                            mp1.stop();
                            mp1.prepareAsync();
                            mp1.seekTo(0);
                        } else {
                            mp1.start();
                        }
                        */
                        //Uncomment to play good sound:
                        //sp1.play(soundId1, 1, 1, 0, 0, 1);
                    } else {
                        tv.setText("Ouchee!!" + " x: " + x + " y: " + y + " wrong: " + h.fingerPos(new Point(x, y)));
                        // TextView ov = (TextView)findViewById(R.id.oucheeView);
                        //ov.setText("  Ouchee!!");
                        ImageView iv = (ImageView) findViewById(R.id.oucheeImageView);
                        //iv.setVisibility(View.VISIBLE);

                        imageView.setLatestPoint(new Point(x, y), Color.RED);

                        imageView.invalidate();
                        int sc = h.score;

                        h.reset();
                        // cr.stop();
                        ct.cancel();
                        h.prepareToStart();
                        mp.pause();
                        Song s = new Song(mp,60.0f);
                        Log.d("curr beat: ","beat: " + Double.toString(s.getCurrentBeatNum()) + " pos: " + mp.getCurrentPosition());
                        mp.seekTo(0);
                        //imageView.readyToStart = true;


                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        DFragment dFragment = new DFragment(sc,best,false);
                        // Show DialogFragment
                        dFragment.show(ft, "Dialog Fragment");
                        /*
                        if (mp1.isPlaying()) {

                            mp1.stop();
                        }


                    if (mp2.isPlaying()) {
                        mp2.stop();
                        mp2.prepareAsync();
                        mp2.seekTo(0);
                    } else {
                        mp2.start();
                    }
                    */

                        soundToPlay = 2;
                        new Thread(MainActivity.this).start();
                        //sp2.play(soundId2, 1, 1, 0, 0, 1);

                        return true;


                    }


                    if (fing.pointIsLeft(new Point(x, y))) {
                        tv.setText("Is Left " + fing.toString());
                    } else {
                        tv.setText("Is Right " + fing.toString());
                    }

                    int pos = h.fingerPos(new Point(x, y));
                    int lastRound = h.round;


                    boolean ok = h.logFingerPress(pos);

                    if ((h.lastValid == -1) && (h.nextValid == 0)) {

                    } else {
                        if (ok) {
                            if ((h.lastValid == 0) && (h.nextValid == 1) && (h.inc == 1) && (h.round == 1)) {
                                imageView.readyToStart = false; //ie started!
                                //cr.setBase(SystemClock.elapsedRealtime());
                                // cr.start();
                                Switch sw = (Switch) findViewById(R.id.switch1);
                                if (sw.isChecked()) {
                                    ct.start();
                                }
                                mp.seekTo(0);
                                mp.start();

                            } else if ((h.lastValid == -1) && h.nextValid == 0) {
                                ///ready to go
                            } else if (h.lastValid == 0) {
                                //soundToPlay = 0;
                                //new Thread(MainActivity.this).start();

                            } else {
                                soundToPlay = 1;
                                new Thread(MainActivity.this).start();
                                //sp1.play(soundId1, 1, 1, 0, 0, 1);

                            }

                            imageView.setLatestPoint(new Point(x, y), Color.GREEN);
                            imageView.invalidate();
                        } else {
                            soundToPlay = 4;
                            new Thread(MainActivity.this).start();
                            //sp4.play(soundId4,1,1,0,0,1);
                            imageView.setLatestPoint(new Point(x, y), Color.YELLOW);
                            imageView.invalidate();
                        }
                    }
                    //if (h.round != lastRound) {
                    if (h.roundFinished()) {

                        soundToPlay = 3;
                        new Thread(MainActivity.this).start();
                        //sp3.play(soundId3, 1, 1, 0, 0, 1);
                    }
                    tv.setText(tv.getText().toString() + " pos: " + h.fingerPos(new Point(x, y)) + " ok?: " + ok + " last: " + h.lastValid + " next: " + h.nextValid);





                    TextView sv = (TextView) findViewById(R.id.scoreView);
                    //sv.setTypeface(custom_font);
                    sv.setText("Score: " + h.score);
                    TextView rv = (TextView) findViewById(R.id.roundView);
                    // rv.setTypeface(custom_font);
                    rv.setText("Round: " + h.round);

                    TextView bv = (TextView) findViewById(R.id.bestView);
                    if (h.score > best) {
                        best = h.score;
                        bv.setText("Best: " + best);
                    }


                }


                return true;
            }


            public boolean onDoubleTap(MotionEvent event) {
                Log.d("debug", "onDoubleTap: " + event.toString());
                return true;
            }

        });

        h.prepareToStart();


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Pausing", "app pausing");
        mp.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Resuming", "app resuming");
        if (!mp.isPlaying() && mp.getCurrentPosition() > 1) {


            mp.start();
        }

    }

    public void run() {

        switch (soundToPlay) {
            case 0:
                sp1.play(soundId1Base,1,1,0,0,1);
                break;
            case 1:
                sp1.play(soundId1,1,1,0,0,1);
                break;
            case 2:
                sp1.play(soundId2, 1, 1, 0, 0, 1);
                break;
            case 3: //end of round
                sp1.play(soundId3, 1, 1, 0, 0, 1);
                break;
            case 4:
                sp1.play(soundId4,1,1,0,0,1);
                break;
            default:
                break;


        }

    }

    public void handleDialogClose(DialogInterface dialog) {
        Log.d("Dismissed dialog", "dismissed dialog");
        HandImageView imageView   = (HandImageView) findViewById(R.id.imageView);
        imageView.readyToStart = true;
        imageView.invalidate();


    }


}