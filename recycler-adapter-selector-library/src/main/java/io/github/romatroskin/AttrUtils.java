package io.github.romatroskin;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AnyRes;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;

/**
 * Theme attributes related utilities
 * Created by romatroskin on 12/8/16.
 */
public class AttrUtils {
    /**
     * Utility method to remove boilerplate code obtaining any resource id from theme attribute
     * @param context {@link Context} to obtain styled attribute on
     * @param attrRes attribute to obtain from the {@code context}
     * @return resource id obtained from {@code context} theme
     */
    public static @AnyRes int obtainResId(@NonNull Context context, @AttrRes int attrRes) {
        final int[] attrs = new int[]{attrRes};
        final TypedArray ta = context.obtainStyledAttributes(attrs);
        int resultResId = ta.getResourceId(0, 0);
        ta.recycle();

        return resultResId;
    }

    private AttrUtils() {}
}
