package com.devidea.grigoapplication;

import android.content.SharedPreferences;

public class TokenManager {

    PrefsHelper prefsHelper = PrefsHelper.getInstance();

    public String get() {
        return prefsHelper.read("token", null);
    }

    public void set(String token) {
        prefsHelper.write("token", token);

    }

    public void clear() {
        prefsHelper.destroyPref();

    }
}
