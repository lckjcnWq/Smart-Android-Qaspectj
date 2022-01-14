package com.example.qaspectj.permission.activity

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qaspectj.R
import com.example.qaspectj.permission.annotion.Permission
import com.example.qaspectj.permission.annotion.PermissionCancle
import com.example.qaspectj.permission.annotion.PermissionDenied
import com.example.qaspectj.permission.utils.PermissionUtil

/**
 * Description:
 * Created by WuQuan on 2022/1/14.
 */
class PermissionActivity : AppCompatActivity() {

    private lateinit var btnRead: Button
    private lateinit var btnOnClick: Button
    private lateinit var btnSet: Button
    companion object{
        val TAG="PermissionActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)
        initView()
        initData()
    }

    private fun initView(){
        btnRead=findViewById(R.id.btnRead)
        btnOnClick=findViewById(R.id.btnOnclick)
        btnSet=findViewById(R.id.btnSet)
    }

    private fun initData() {
        btnOnClick.setOnClickListener {
            Log.i(TAG, "btnOnClick")
        }
        btnSet.setOnClickListener {
            PermissionUtil.startAndroidSettings(this)
        }
    }

    //权限申请
    @Permission(value = [Manifest.permission.READ_EXTERNAL_STORAGE], requestCode = 1)
    public fun getSdCard(){
        Log.i(TAG, "getSdCard")
    }

    /**
     * 用户拒绝权限申请的回调方法
     * @param
     */
    @PermissionCancle(requestCode = 1)
    private fun requestPermissionFailed() {
        Toast.makeText(this, "用户拒绝了权限", Toast.LENGTH_SHORT).show()
    }


    /**
     * 权限申请失败的回调方法
     * @param
     */
    @PermissionDenied(requestCode = 1)
    private fun requestPermissionDenied() {
        Toast.makeText(this, "权限申请失败,不再询问", Toast.LENGTH_SHORT).show()
//        PermissionUtil.startAndroidSettings(this)
    }

}