package com.app.socialmedialikes;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    private static final String PREF_USER_NAME = "username";
    private static final String PREF_WALLET_BALANCE = "balance";
    private static final String PREF_THEME_MODE = "themeMode";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_FULL_NAME = "name";
    private static final String PREF_PASSWORD = "password";
    private static final String PREF_TASKS_LIST = "tasksList";
    private static final String PREF_FIRST_LAUNCH = "firstLaunch";

    private static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }


    public static void setUserName(Context ctx, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static void setFirstLaunch(Context ctx, String firstLaunch) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_FIRST_LAUNCH, firstLaunch);
        editor.commit();
    }

    public static String getFirstLaunch(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_FIRST_LAUNCH, "");
    }

    public static void setThemeMode(Context ctx, String themeMode) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_THEME_MODE, themeMode);
        editor.commit();
    }

    public static String getThemeMode(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_THEME_MODE, "");
    }

    public static String getPrefWalletBalance(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_WALLET_BALANCE, "");
    }

    public static void setPrefWalletBalance(Context ctx, String walletBalance) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_WALLET_BALANCE, walletBalance);
        editor.commit();
    }

    public static String getPrefEmail(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_EMAIL, "");
    }

    public static void setPrefEmail(Context ctx, String prefEmail) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_EMAIL, prefEmail);
        editor.commit();
    }

    public static String getPrefFullName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_FULL_NAME, "");
    }

    public static void setPrefFullName(Context ctx, String prefFullName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_FULL_NAME, prefFullName);
        editor.commit();
    }

    public static String getPrefPassword(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_PASSWORD, "");
    }

    public static void setPrefPassword(Context ctx, String prefPassword) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_PASSWORD, prefPassword);
        editor.commit();
    }

    public static String getPrefTasksLIst(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_TASKS_LIST, "");
    }

    public static void setPrefTasksList(Context ctx, String tasksList) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_TASKS_LIST, tasksList);
        editor.commit();
    }
}