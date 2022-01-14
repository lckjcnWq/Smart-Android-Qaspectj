package com.example.qaspectj

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.qaspectj.permission.activity.PermissionActivity

class MainActivity : AppCompatActivity() {
    private lateinit var btnRequest: Button
    private lateinit var btnUpLoad: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initData()
    }

    private fun initData() {
        btnRequest.setOnClickListener {
            startActivity(Intent(this, PermissionActivity::class.java))
        }
        btnUpLoad.setOnClickListener {

        }
    }

    private fun initView() {
        btnRequest = findViewById<Button>(R.id.btnRequest)
        btnUpLoad = findViewById<Button>(R.id.btnUpLoad)
    }
}