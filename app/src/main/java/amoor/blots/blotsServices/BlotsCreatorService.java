package amoor.blots.blotsServices;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import amoor.blots.R;

/**
 * Created by Amoor on 12/12/2016.
 */

public class BlotsCreatorService extends Service {

    private WindowManager windowManager;

    private boolean BlotFirstTimeClicked = true;
    private int blotTimesClicked= 0;
    private int blotTimesClickedThreshold = 3;
    private static Blot blot;
    public static boolean blotCreated = false;
    private TextView tv;

    private int lastShapeWidth;


    public enum State {
        DRAG,
        PINCH_HORIZONTAL,
        PINCH_VERTICAL,
        NONE
    }

    @Override public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    void removeBlot(){
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.blot_on_remove) , Toast.LENGTH_SHORT).show();
        blotCreated = false;
        stopService(new Intent(BlotsCreatorService.this, BlotsCreatorService.class));
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void addBlotToWindowManager(){
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                // Be on top of other application windows, but only for the current user
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                // Don't grab the input focus
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                // Make the underlying application window visible through any transparent parts
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        windowManager.addView(blot, params);
    }





    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        String shape;
        String color = "white";
        boolean serviceWasReset = (intent == null);
        if(!serviceWasReset) {
            shape = intent.getStringExtra("shape");
            Utils.storeBlotToPrefFile(shape, this);
        }
        else{
            shape = Utils.getBlotShapeFromPrefFile(this);
        }
        //color = intent.getStringExtra("color");

        blotCreated = true;
        blot = new Blot(this,shape,color);
        blot.elevateBlot();

        addBlotToWindowManager();

        blot.setOnTouchListener(new View.OnTouchListener() {
            float dX = 0, dY = 0;
            State state = State.NONE;
            boolean clickedRecently = false;
            final int doubleClickDelay = 250;
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


            @Override
            public boolean onTouch(View view, MotionEvent event) {
                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) view.getLayoutParams();

                ImageView blot = (ImageView) view;
                blot.setScaleType(ImageView.ScaleType.MATRIX);

                switch (event.getActionMasked()) {

                    case MotionEvent.ACTION_DOWN: { //only one finger touching
                        dX = layoutParams.x - event.getRawX();
                        dY = layoutParams.y - event.getRawY();
                        state = State.DRAG;
                        if(blotDoubleClicked()){
                            v.vibrate(20);
                            removeBlot();
                        }
                        return true;
                    }

                    case MotionEvent.ACTION_UP: {//first finger lifted

                        if(BlotFirstTimeClicked || blotTimesClicked >= blotTimesClickedThreshold){
                            Toast.makeText(getApplicationContext(), getString(R.string.blot_on_finger_up_prompt) , Toast.LENGTH_SHORT).show();
                            blotTimesClicked = 0;
                            BlotFirstTimeClicked = false;
                            blotTimesClickedThreshold *= 2;
                        }else{
                            blotTimesClicked++;
                        }
                        return false;
                    }

                    case MotionEvent.ACTION_MOVE: {
                        if( state == State.DRAG){
                            layoutParams.x = (int)(event.getRawX() + dX);
                            layoutParams.y = (int)(event.getRawY() + dY);
                            windowManager.updateViewLayout(view,layoutParams);
                        }
                        return true;
                    }

                    default: {
                        return false;
                    }
                }

            }

            private boolean blotDoubleClicked(){
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        clickedRecently = false;
                    }
                };

                if(!clickedRecently){
                    clickedRecently = true;
                    handler.postDelayed(r, doubleClickDelay);
                }else{
                    //Double click
                    return true;
                }
                return false;
            }

        });





        return START_STICKY;
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        if (blot != null) windowManager.removeView(blot);
    }


    public static void changeBlotShape(Context context, String shape, String color) {
        blot.shapeBlot(context ,shape, color);
        Utils.storeBlotToPrefFile(shape, context);
    }
}
