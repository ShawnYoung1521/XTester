package com.xy.permission;

public class PermissionsContract {
    public static String permission_RECORD_AUDIO = "android.permission.RECORD_AUDIO";
    public static String permission_READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static String permission_WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static String[] FileReadPermissions = {
            permission_READ_EXTERNAL_STORAGE
            ,permission_WRITE_EXTERNAL_STORAGE};
    public static String[] AidioPermissions = {
            permission_RECORD_AUDIO
    };
}
