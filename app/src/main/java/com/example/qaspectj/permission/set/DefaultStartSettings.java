package com.example.qaspectj.permission.set;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.example.qaspectj.permission.interfaces.ISetting;

public class DefaultStartSettings implements ISetting {
    @Override
    public Intent getStartSettingsIntent(Context context) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));

        return intent;
    }
}
