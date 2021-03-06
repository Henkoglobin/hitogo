package org.hitogo.examples;

import android.arch.lifecycle.Lifecycle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.hitogo.alert.core.AlertType;
import org.hitogo.alert.view.anim.TopAnimation;
import org.hitogo.core.HitogoAnimation;
import org.hitogo.core.HitogoController;

public class AlertController extends HitogoController {

    public AlertController(Lifecycle lifecycle) {
        super(lifecycle);
    }

    @Override
    public Integer provideViewLayout(int state) {
        AlertState alertState = AlertState.parse(state);

        switch (alertState) {
            case SUCCESS:
                return R.layout.hitogo_success;
            case WARNING:
                return R.layout.hitogo_warning;
            case DANGER:
                return R.layout.hitogo_danger;
            case HINT:
            default:
                return R.layout.hitogo_hint;
        }
    }

    @Nullable
    @Override
    public Integer provideDialogLayout(int state) {
        AlertState alertState = AlertState.parse(state);

        switch (alertState) {
            default:
                return R.layout.hitogo_dialog_danger;
        }
    }

    @Nullable
    @Override
    public Integer providePopupLayout(int state) {
        AlertState alertState = AlertState.parse(state);

        switch (alertState) {
            default:
                return R.layout.hitogo_popup;
        }
    }

    @Nullable
    @Override
    public Integer provideDefaultViewAlertLayoutContainerId() {
        return R.id.container_layout;
    }

    @Nullable
    @Override
    public Integer provideDefaultViewAlertOverlayContainerId() {
        return R.id.container_overview_layout;
    }

    @Nullable
    @Override
    public Integer provideDefaultAlertTitleViewId(@NonNull AlertType type) {
        return R.id.title;
    }

    @Nullable
    @Override
    public Integer provideDefaultAlertTextViewId(@NonNull AlertType type) {
        return R.id.text;
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T extends HitogoAnimation> T provideDefaultAlertAnimation(@NonNull AlertType type) {
        return (T) TopAnimation.build();
    }

    @Override
    public boolean provideIsDebugState() {
        return BuildConfig.DEBUG;
    }
}
