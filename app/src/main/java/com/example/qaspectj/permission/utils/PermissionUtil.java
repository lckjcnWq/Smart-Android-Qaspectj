package com.example.qaspectj.permission.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.qaspectj.permission.activity.TransparentActivity;
import com.example.qaspectj.permission.annotion.PermissionCancle;
import com.example.qaspectj.permission.annotion.PermissionDenied;
import com.example.qaspectj.permission.interfaces.ISetting;
import com.example.qaspectj.permission.interfaces.PermissionRequstCallback;
import com.example.qaspectj.permission.set.DefaultStartSettings;
import com.example.qaspectj.permission.set.OPPOStartSettings;
import com.example.qaspectj.permission.set.VIVOStartSettings;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;

public class PermissionUtil {
    /**
     * 权限
     */
    public static final String REQUEST_PERMISSIONS = "request_permissions";

    //请求码
    public static final String REQUEST_CODE = "request_code";
    //默认请求码
    public static final int REQUEST_CODE_DEFAULT = -1;
    //回调接口
    public static PermissionRequstCallback permissionRequstCallback;

    private static HashMap<String, Class<? extends ISetting>> permissionMenu = new HashMap<>();

    private static final String MANUFACTURER_DEFAULT = "Default";
    public static final String MANUFACTURER_HUAWEI = "huawei";
    public static final String MANUFACTURER_MEIZU = "meizu";
    public static final String MANUFACTURER_XIAOMI = "xiaomi";
    public static final String MANUFACTURER_SONY = "sony";
    public static final String MANUFACTURER_OPPO = "oppo";
    public static final String MANUFACTURER_LG = "lg";
    public static final String MANUFACTURER_VIVO = "vivo";
    public static final String MANUFACTURER_SAMSUNG = "samsung";
    public static final String MANUFACTURER_LETV = "letv";
    public static final String MANUFACTURER_ZTE = "zte";
    public static final String MANUFACTURER_YULONG = "yulong";
    public static final String MANUFACTURER_LENOVO = "lenovo";

    static {
        permissionMenu.put(MANUFACTURER_DEFAULT, DefaultStartSettings.class);
        permissionMenu.put(MANUFACTURER_OPPO, OPPOStartSettings.class);
        permissionMenu.put(MANUFACTURER_VIVO, VIVOStartSettings.class);
    }

    public static void launchActivity(Context context, String[] value, int requestCod, PermissionRequstCallback callback) {
        permissionRequstCallback = callback;
        Bundle bundle = new Bundle();
        bundle.putStringArray(REQUEST_PERMISSIONS, value);
        bundle.putInt(REQUEST_CODE, requestCod);

        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(context, TransparentActivity.class);
        context.startActivity(intent);
    }

    /**
     * 判断所有权限是否都给了  如果有一个权限没给  就返回false
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermissionRequest(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 最后判断下 是否全部真正的成功
     *
     * @param gantedResult
     * @return
     */
    public static boolean requestPermissionSuccess(int[] gantedResult) {
        if (gantedResult == null || gantedResult.length <= 0) {
            return false;
        }
        for (int permissionValue : gantedResult) {
            if (permissionValue != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    /**
     * 用户是否拒绝了并且点击了不再提示
     *
     * @param activity
     * @param permissions
     * @return
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity,String[] permissions) {
        for (String permission : permissions) {
            //如果用户点击了不再提示  就返回true
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 专门去 callback invoke ---》 执行  被注解的方法
     *
     * @param object
     * @param annotationClass
     */
    public static void invokeAnnotation(Object object, Class annotationClass, int requstCode) {
        // 获取 object 的 Class对象
        Class<?> objectClass = object.getClass();

        // 遍历 所有的方法
        Method[] methods = objectClass.getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            boolean annotationPresent = method.isAnnotationPresent(annotationClass);
            if (annotationPresent) {
                Annotation annotation = method.getAnnotation(annotationClass);
                int mrequstCode = -1;
                if (annotationClass == PermissionDenied.class) {
                    PermissionDenied permission = (PermissionDenied) annotation;
                    mrequstCode = permission.requestCode();
                } else if (annotationClass == PermissionCancle.class) {
                    PermissionCancle permission = (PermissionCancle) annotation;
                    mrequstCode = permission.requestCode();
                }
                try {
                    if (requstCode == mrequstCode) {
                        method.invoke(object);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // TODO 专门去 跳转到 设置界面
    public static void startAndroidSettings(Context context) {

        Class aClass = permissionMenu.get(Build.MANUFACTURER.toLowerCase());
        if (aClass == null) {
            aClass = permissionMenu.get(MANUFACTURER_DEFAULT);
        }

        try {
            Object newInstance = aClass.newInstance(); // new OPPOStartSettings()
            ISetting iMenu = (ISetting) newInstance; // ISetting iMenu = (ISetting) oPPOStartSettings;
            Intent startActivityIntent = iMenu.getStartSettingsIntent(context);
            if (startActivityIntent != null) {
                context.startActivity(startActivityIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
