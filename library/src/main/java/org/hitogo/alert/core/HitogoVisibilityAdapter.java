package org.hitogo.alert.core;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class HitogoVisibilityAdapter implements HitogoVisibilityListener {

    @Override
    public void onCreate(HitogoAlert object) {
        //implementation optional
    }

    @Override
    public void onShow(HitogoAlert object) {
        //implementation optional
    }

    @Override
    public void onClose(HitogoAlert object) {
        //implementation optional
    }
}
