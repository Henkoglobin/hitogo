package org.hitogo.core;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class HitogoUtils {

    private HitogoUtils() {
        //Methods can only be accessed via static methods.
    }

    public static boolean isNotEmpty(@NonNull String text) {
        return !text.isEmpty();
    }

    public static boolean isEmpty(@NonNull String text) {
        return text.isEmpty();
    }

    public static void measureView(@NonNull Activity activity, @NonNull View customView,
                                   @Nullable ViewGroup viewGroup) {
        int widthSpec;
        if (viewGroup != null) {
            widthSpec = View.MeasureSpec.makeMeasureSpec(viewGroup.getMeasuredWidth(), View.MeasureSpec.EXACTLY);
        } else {
            widthSpec = View.MeasureSpec.makeMeasureSpec(activity.getWindow().getDecorView().getMeasuredWidth(),
                    View.MeasureSpec.EXACTLY);
        }
        customView.measure(widthSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    }
}
