package org.hitogo.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import org.hitogo.core.button.HitogoButton;
import org.hitogo.core.HitogoContainer;
import org.hitogo.core.HitogoObject;
import org.hitogo.core.StringUtils;

import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class HitogoDialog extends HitogoObject {

    private AlertDialog dialog;

    HitogoDialog(HitogoDialogBuilder builder) {
        super(builder.controller, builder.hashCode);

        List<HitogoButton> buttonList = builder.callToActionButtons;
        Context context = builder.context;
        Integer themeResId = builder.dialogThemeResId;

        AlertDialog.Builder dialogBuilder;
        if (themeResId != null) {
            dialogBuilder = new AlertDialog.Builder(context, themeResId);
        } else {
            dialogBuilder = new AlertDialog.Builder(context);
        }

        dialogBuilder.setMessage(builder.text)
                .setTitle(builder.title)
                .setCancelable(!builder.isDismissible);

        final HitogoButton positiveButton = buttonList.get(0);
        if (positiveButton.getText() != null && StringUtils.isNotEmpty(positiveButton.getText())) {
            dialogBuilder.setPositiveButton(positiveButton.getText(), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    positiveButton.getListener().onClick();
                    makeInvisible();
                }
            });
        }

        if (buttonList.size() > 1) {
            final HitogoButton negativeButton = buttonList.get(1);

            if (negativeButton.getText() != null && StringUtils.isNotEmpty(negativeButton.getText())) {
                dialogBuilder.setNegativeButton(negativeButton.getText(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        negativeButton.getListener().onClick();
                        makeInvisible();
                    }
                });
            }
        }

        if (buttonList.size() > 2) {
            final HitogoButton neutralButton = buttonList.get(2);

            if (neutralButton.getText() != null && StringUtils.isNotEmpty(neutralButton.getText())) {
                dialogBuilder.setNeutralButton(neutralButton.getText(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        neutralButton.getListener().onClick();
                        makeInvisible();
                    }
                });
            }
        }

        this.dialog = dialogBuilder.create();
    }

    @Override
    protected void makeVisible(@NonNull Activity activity) {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void makeInvisible() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @NonNull
    public static HitogoDialogBuilder with(@NonNull HitogoContainer container) {
        return new HitogoDialogBuilder(container.getActivity(), container.getController());
    }
}