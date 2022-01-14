package com.example.qaspectj.permission.interfaces;

public interface PermissionRequstCallback {
    void permissionSuccess();
    void permissionCancel();
    void permissionDenied();
}
