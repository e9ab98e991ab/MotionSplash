# 使用MotionLayout 实现启动页动画 Splash

## 效果图

![capture](\assets\capture.gif)

[项目地址](https://github.com/e9ab98e991ab/MotionSplash)

## 具体代码实现

> activity_splash.xml

```xml
<androidx.constraintlayout.motion.widget.MotionLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/_splashMotion"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/white"
    android:gravity="center"
    android:orientation="vertical"
    app:layoutDescription="@xml/activity_motion_scene">

    <ImageView
        android:id="@+id/main_image2"
        android:layout_width="30dp"
        android:layout_height="30dp"
        android:src="@drawable/ic_sun"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        tools:ignore="ContentDescription" />
    <androidx.appcompat.widget.AppCompatTextView
        android:id="@+id/aplashTv"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="center"
        android:lineSpacingExtra="20dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        android:text="" />


    <TextView
        android:id="@+id/app_name_by"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal|bottom"
        android:layout_marginBottom="12dp"
        android:text="@string/app_name_by "
        android:textColor="#dd000000"
        app:layout_goneMarginBottom="60dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        android:textSize="14sp"/>

</androidx.constraintlayout.motion.widget.MotionLayout>
```

> xml/activity_motion_scene/xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<MotionScene xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:motion="http://schemas.android.com/apk/res-auto">

    <Transition
        motion:constraintSetEnd="@id/end"
        motion:constraintSetStart="@id/start"
        motion:duration="4000"
        motion:motionInterpolator="easeInOut">
        <OnClick
            motion:clickAction="toggle"
            motion:targetId="@id/main_image2"
            motion:dragDirection="dragRight" />
        <KeyFrameSet>
            <KeyTimeCycle
                motion:motionTarget="@id/main_image2"
                motion:motionProgress="60"
                motion:framePosition="50"
                motion:wavePeriod="43"
                motion:waveShape="sawtooth"
                motion:waveOffset="60"
                />
            <KeyPosition
                motion:framePosition="10"
                motion:keyPositionType="parentRelative"
                motion:motionTarget="@id/main_image2"
                motion:pathMotionArc="startHorizontal"
                motion:curveFit="spline"
                motion:percentX="2"
                motion:percentY="0" />
            <KeyPosition
                motion:framePosition="50"
                motion:keyPositionType="pathRelative"
                motion:motionTarget="@id/main_image2"
                motion:pathMotionArc="flip"
                motion:curveFit="spline"
                motion:percentX="1.6"
                motion:percentY="0.55"/>
            <KeyPosition
                motion:framePosition="85"
                motion:keyPositionType="pathRelative"
                motion:motionTarget="@id/main_image2"
                motion:pathMotionArc="flip"
                motion:curveFit="spline"
                motion:percentX="1.3"
                motion:percentY="-0.65"/>
            <KeyAttribute
                motion:motionTarget="@id/main_image2"
                motion:framePosition="30"
                android:rotationX="50"
                android:rotationY="50"/>

        </KeyFrameSet>
    </Transition>

    <ConstraintSet android:id="@+id/start">

        <Constraint
            android:id="@+id/main_image2"
            android:layout_width="60dp"
            android:layout_height="60dp"
            android:alpha="0"
            motion:drawPath="path"
            motion:layout_constraintBottom_toBottomOf="parent"
            motion:layout_constraintRight_toRightOf="parent"
            motion:layout_constraintTop_toTopOf="parent" />

        <Constraint
            android:id="@+id/app_name_by"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:alpha="0"
            motion:drawPath="path"
            motion:layout_constraintBottom_toBottomOf="parent"
            motion:layout_constraintLeft_toLeftOf="parent"
            motion:layout_constraintRight_toRightOf="parent"  />

    </ConstraintSet>

    <ConstraintSet android:id="@+id/end">
        <Constraint
            android:id="@+id/main_image2"
            android:layout_width="50dp"
            android:layout_height="50dp"
            motion:layout_constraintBottom_toBottomOf="parent"
            motion:layout_constraintLeft_toLeftOf="parent"
            motion:layout_constraintRight_toRightOf="parent"
            motion:layout_constraintTop_toTopOf="parent"  />
        <Constraint
            android:id="@+id/app_name_by"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="60dp"
            motion:layout_constraintBottom_toBottomOf="parent"
            motion:layout_constraintLeft_toLeftOf="parent"
            motion:layout_constraintRight_toRightOf="parent"   />

    </ConstraintSet>

</MotionScene>
```

> SplashActivity

```kotlin
package com.e9ab98e991ab.motionsplash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        main_image2.post {
            main_image2.performClick()
        }
        _splashMotion.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                this@SplashActivity.finish()
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }
        })
    }
}
```

##  实现原理

第一步：SplashActivity 中使用MotionLayout  （id： _splashMotion）添加 ImageView （id： main_image2）

第二步：MotionScene->Transition添加 如下事件监听 并在MotionScene 布局中调整动画角度。

```xml
<OnClick
    motion:clickAction="toggle"
    motion:targetId="@id/main_image2"
    motion:dragDirection="dragRight" />
```

第三步 SplashActivity 中 执行 performClick()开始执行动画

```kotlin
main_image2.post {
    main_image2.performClick()
}
```

第四步 在SplashActivity（id： _splashMotion）  设置动画监听 

```kotlin
_splashMotion.setTransitionListener(object : MotionLayout.TransitionListener {
	//开始时
    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
    }

    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
    }
	//结束
    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        this@SplashActivity.finish()
    }

    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
    }
})
```