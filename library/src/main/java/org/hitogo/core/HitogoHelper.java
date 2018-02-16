package org.hitogo.core;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import org.hitogo.alert.core.AlertParams;
import org.hitogo.button.core.Button;

import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public class HitogoHelper {

    public boolean isNotEmpty(@Nullable String text) {
        return !isEmpty(text);
    }

    public boolean isEmpty(@Nullable String text) {
        return text == null || text.isEmpty();
    }

    public void measureView(@NonNull Activity activity, @NonNull View customView,
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

    public int generateAlertHashCode(@NonNull AlertParams params) {
        int hashCode = 0;

        String title = params.getTitle();
        hashCode += title != null ? title.hashCode() : 0;

        SparseArray<String> textMap = params.getTextMap();
        for (int i = 0; i < textMap.size(); i++) {
            String text = textMap.valueAt(i);
            hashCode += text.hashCode();
        }

        List<Button> buttonList = params.getButtons();
        for (Button button : buttonList) {
            hashCode += button.hashCode();
        }

        Button closeButton = params.getCloseButton();
        hashCode += closeButton != null ? closeButton.hashCode() : 0;

        hashCode += params.getType().ordinal();
        hashCode += params.getState() != null ? params.getState() : 0;

        return hashCode;
    }
}
