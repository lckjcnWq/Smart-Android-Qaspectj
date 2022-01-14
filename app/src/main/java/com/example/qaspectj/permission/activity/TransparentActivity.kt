package com.example.qaspectj.permission.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.qaspectj.permission.utils.PermissionUtil

/**
 * Description:
 * Created by WuQuan on 2022/1/14.
 */
class TransparentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    private fun initData() {
        val intent = intent
        intent?.apply {
            val value = getStringArrayExtra(PermissionUtil.REQUEST_PERMISSIONS)
            val requestCode = getIntExtra(
                PermissionUtil.REQUEST_CODE,
                PermissionUtil.REQUEST_CODE_DEFAULT
            )
            if (value == null || value.isEmpty() || requestCode == -1 || PermissionUtil.permissionRequstCallback == null) {
                finish()
                return
            }

            if (PermissionUtil.hasPermissionRequest(this@TransparentActivity, *value)) {
                PermissionUtil.permissionRequstCallback.permissionSuccess()
                finish()
                return
            }
            ActivityCompat.requestPermissions(this@TransparentActivity, value, requestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //权限申请成功
        if (PermissionUtil.requestPermissionSuccess(grantResults)) {
            PermissionUtil.permissionRequstCallback.permissionSuccess()
            finish()
            return
        }
        //权限拒绝，不在提示
        if (PermissionUtil.shouldShowRequestPermissionRationale(this, permissions)) {
            PermissionUtil.permissionRequstCallback.permissionDenied()
            finish()
            return
        }
        //权限拒绝
        PermissionUtil.permissionRequstCallback.permissionCancel()
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}