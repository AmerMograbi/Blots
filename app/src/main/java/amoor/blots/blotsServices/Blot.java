package amoor.blots.blotsServices;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import amoor.blots.R;

/**
 * Created by Amoor on 12/20/2016.
 */

public class Blot extends ImageView{

    //constructor used by inflater
    public Blot(Context context) {
        super(context);
    }

    //constructor used by inflater
    public Blot(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Blot(Context context, String shape, String color) {
        super(context);

        shapeBlot(context, shape, color);
        elevateBlot();
    }


    public void vibrateOnLongPress(boolean val){
        setHapticFeedbackEnabled(val);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void elevateBlot(){
        if(Utils.canElevate()){
            setElevation(50);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void shapeBlot(Context appContext, String shape, String color){

        final int resourceId = Utils.getDrawableResourceIdByName(appContext, shape);

        if(Utils.canUseSetBackground()){
            setBackground(ContextCompat.getDrawable(appContext, resourceId));
        }else{
            setBackgroundDrawable(ContextCompat.getDrawable(appContext,resourceId));
        }

        //If you don't color the blot, the blot will disappear after setting the background
        colorBlot(color);
    }


    private void colorBlot(String color){

        String colorCode = Utils.getColorCode(color);
        getBackground().setColorFilter(Color.parseColor(colorCode), PorterDuff.Mode.DARKEN);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
    }



}
