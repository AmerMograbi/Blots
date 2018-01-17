package amoor.blots.blotsServices;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.icu.text.DateFormat;
import android.os.Build;
import android.provider.Settings;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by Amoor on 12/12/2016.
 */

public class Utils {

    public static final String BlotPrefs = "BLOT_PREFS";
    public static boolean firstTimeCreatingBlot = true; //from when the app was installed

    public static boolean canDrawOverlays(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        }

        return true;
    }

    public static boolean canUseSetBackground() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return false;
        }

        return true;
    }

    public static boolean canElevate() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return true;
        }

        return false;
    }

    public static String getColorCode(String color){
        switch (color){
            case "black": {
                return "#000000";
            }
            default:{
                return "#ffffff";
            }
        }
    }


    /**
     * Returns the drawable resource ID that matches the given drawable name (case insensitive).
     * @param appContext the context
     * @param drawableName the name of the drawable file
     * @return the drawable resource ID
     */
    public static int getDrawableResourceIdByName(Context appContext, String drawableName){
        Resources resources = appContext.getResources();
        //getting the resource ID of resource file named --as the value of-- shape
        final int resourceId = resources.getIdentifier(drawableName.toLowerCase(), "drawable",
                appContext.getPackageName());

        return resourceId;
    }

    /**
     * Stores the shape of the current blot to persistent storage
     * @param shape the shape of the blot as a string
     * @param context the context that contains the blot
     */
    public static void storeBlotToPrefFile(String shape, Context context){
        SharedPreferences blotStoredInfo = context.getSharedPreferences(BlotPrefs, 0);
        SharedPreferences.Editor editor = blotStoredInfo.edit();
        editor.putString("shape",shape).commit();
    }

    /**
     * Stores that the blot was created
     * @param context the context that contains the blot
     */
    public static void storeFirstTimeCreatingBlot(Context context){
        if(!firstTimeCreatingBlot){
            return;
        }
        SharedPreferences blotStoredInfo = context.getSharedPreferences(BlotPrefs, 0);
        SharedPreferences.Editor editor = blotStoredInfo.edit();
        editor.putString("firstTimeCreatingBlot",String.valueOf(false)).commit();
    }

    /**
     * Retrieves the boolean that checks if the blot was created
     * @param context the context that contains the blot
     * @return the shape of the last created blot
     */
    public static void setFirstTimeCreatingBlot(Context context){
        SharedPreferences blotStoredInfo = context.getSharedPreferences(BlotPrefs, 0);
        String tmp = blotStoredInfo.getString("firstTimeCreatingBlot", String.valueOf(true));
        firstTimeCreatingBlot = Boolean.valueOf(tmp);
    }


    /**
     * Retrieves the shape of the last created blot
     * @param context the context that contains the blot
     * @return the shape of the last created blot
     */
    public static String getBlotShapeFromPrefFile(Context context){
        SharedPreferences blotStoredInfo = context.getSharedPreferences(BlotPrefs, 0);
        String defaultShapeValue = "rectangle";
        return blotStoredInfo.getString("shape", defaultShapeValue);
    }


}
