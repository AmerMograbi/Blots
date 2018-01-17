package amoor.blots;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;

import amoor.blots.blotsServices.Blot;
import amoor.blots.blotsServices.Utils;

/**
 * Created by Amoor on 12/22/2016.
 */

public class BlotsChooserAdapter extends BaseAdapter {
    private Context context;
    private Blot[] blotsData;
    private String blackColor = "#000000";
    String [] shapesArray;

    public BlotsChooserAdapter(Context context) {
        this.context = context;
        shapesArray = context.getResources().getStringArray(R.array.shapes);
        blotsData = new Blot[shapesArray.length];
    }


    @Override
    public int getCount() {
        return blotsData.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public View getView(int position, View convertView, ViewGroup parent) {
        //a square in the grid
        View square;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null){
            square = inflater.inflate(R.layout.blot_view, null);

            TextView textView = (TextView) square.findViewById(R.id.tv_blot_desc);
            textView.setText(shapesArray[position]);

            Blot currBlot = (Blot)square.findViewById(R.id.iv_blot_image);
            String iconPrefix = context.getResources().getString(R.string.icon_prefix);
            String iconName = iconPrefix + shapesArray[position];
            final int resourceId = Utils.getDrawableResourceIdByName(context,iconName);

            currBlot.setImageResource(resourceId);

        }else{
            square = convertView;
        }

        return square;
    }

}
