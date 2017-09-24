package org.hitogo.core;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

@SuppressWarnings("WeakerAccess")
public class HitogoParamsHolder {

    private Bundle bundle = new Bundle();

    public final void provideString(String key, String value) {
        checkForKey(key);
        bundle.putString(key, value);
    }

    public final void provideInteger(String key, Integer value) {
        checkForKey(key);
        bundle.putSerializable(key, value);
    }

    public final void provideBoolean(String key, Boolean value) {
        checkForKey(key);
        bundle.putSerializable(key, value);
    }

    protected void checkForKey(String key) {
        if(bundle.containsKey(key)) {
            Log.e(HitogoParams.class.getName(), "Error: Your builder value will be overridden by" +
                    " a private value. Make sure that you don't clash with private value keys!");
            Log.e(HitogoParams.class.getName(), "Key that will be overridden: " + key);
        }
    }

    public final String getString(String key) {
        return bundle.getString(key);
    }

    public final Integer getInteger(String key) {
        return (Integer) bundle.getSerializable(key);
    }

    public final Boolean getBoolean(String key) {
        return bundle.getBoolean(key);
    }

    @NonNull
    protected Bundle getBundle() {
        return bundle;
    }
}
