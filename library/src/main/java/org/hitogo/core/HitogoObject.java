package org.hitogo.core;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.ref.WeakReference;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class HitogoObject<T extends HitogoParams> extends HitogoLifecycleCallback<T> {

    private static final int NO_ANIMATION_LENGTH = 0;

    private static final int CURRENT_CROUTON = 0;
    private static final int LAST_CROUTON = 1;

    public enum HitogoType {
        VIEW, DIALOG
    }

    private boolean attached;
    private boolean detached;
    private boolean hasAnimation;

    private int hashCode;
    private HitogoType type;

    private WeakReference<HitogoContainer> containerRef;
    private View view;
    private Dialog dialog;

    public final HitogoObject<T> startHitogo(@NonNull HitogoContainer container, @NonNull T params)
            throws IllegalAccessException {

        if(attached) {
            throw new IllegalAccessException("Cannot apply parameter to a visible Hitogo.");
        }

        this.containerRef = new WeakReference<>(container);
        this.hashCode = params.getHashCode();
        this.hasAnimation = params.hasAnimation();
        this.type = params.getType();

        onCheckStart(params);
        onCheckStart(getController(), params);

        onCreate(params);
        onCreate(getController(), params);

        if(type == HitogoType.VIEW) {
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = onCreateView(inflater, getActivity(), params);
        } else {
            dialog = onCreateDialog(getActivity(), params);
        }

        return this;
    }

    public final void show() {
        HitogoContainer container = containerRef.get();
        if(container instanceof Fragment) {
            internalShow(container.getActivity(), (Fragment) container);
        } else {
            internalShow(container.getActivity(), null);
        }
    }

    private void internalShow(final Activity activity, final Fragment fragment) {
        final HitogoObject[] objects = getController().validate(this);
        final HitogoObject current = objects[CURRENT_CROUTON];
        final HitogoObject last = objects[LAST_CROUTON];

        if(last != null && last.hasAnimation() && last.isClosing()) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if((fragment != null && fragment.isAdded()) || !activity.isFinishing()) {
                        makeVisible(activity, current);
                    }
                }
            }, current.getAnimationDuration() + 100);
        } else {
            makeVisible(activity, current);
        }
    }

    private void makeVisible(Activity activity, HitogoObject object) {
        if(!object.isAttached()) {
            object.onAttach(activity);
            attached = true;
            detached = false;

            if(hasAnimation) {
                onShowAnimation(activity);
            } else {
                onShowDefault(activity);
            }
        }
    }

    protected final void makeInvisible() {
        if(isAttached()) {
            if(hasAnimation) {
                onDetachAnimation(getActivity());

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        detached = true;
                    }
                }, getAnimationDuration());
            } else {
                onDetachDefault(getActivity());
                detached = true;
            }
        }
    }

    public final void close() {
        getController().closeHitogo();
    }

    public long getAnimationDuration() {
        return NO_ANIMATION_LENGTH;
    }

    protected final boolean isDetached() {
        return detached;
    }

    protected final boolean isAttached() {
        return attached;
    }

    protected final boolean isClosing() {
        return !attached && !detached;
    }

    public final boolean hasAnimation() {
        return hasAnimation;
    }

    @NonNull
    public final Activity getActivity() {
        return containerRef.get().getActivity();
    }

    @Nullable
    public final View getRootView() {
        return containerRef.get().getView();
    }

    @NonNull
    public final HitogoController getController() {
        return containerRef.get().getController();
    }

    public final HitogoType getType() {
        return type;
    }

    @Nullable
    protected final View getView() {
        return view;
    }

    @Nullable
    protected final Dialog getDialog() {
        return dialog;
    }

    @Override
    public final int hashCode() {
        return hashCode;
    }

    @Override
    public final boolean equals(@NonNull Object obj) {
        return obj instanceof HitogoObject && this.hashCode == obj.hashCode();
    }
}
