package com.cogs189.chad.bci.utils;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by Chad on 2/27/17.
 */

public class ViewUtils {

    public static final String TAG = ViewUtils.class.getName();

    public static <T extends View> T getView(@NonNull View v, @IdRes int resID) {
        return (T) v.findViewById(resID);
    }

}
