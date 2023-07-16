package com.handsome.yunmusic

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.handsome.lib.util.BaseApp
import kotlin.system.exitProcess
import android.os.Process

class MyExceptionHandler : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(t: Thread, e: Throwable) {
        e.printStackTrace()
        val intent = Intent()
        val context = BaseApp.mContext
        // 创建一个新的Intent，将要启动的活动设置为MainActivity
        intent.setClass(context, MainActivity::class.java)
        // 添加FLAG_ACTIVITY_NEW_TASK标志，以在新任务中启动MainActivity
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        // 启动MainActivity
        context.startActivity(intent)
        // 创建一个PendingIntent，用于在延迟后启动MainActivity
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        // 获取AlarmManager实例，用于设置延迟启动MainActivity的定时任务
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // 设置定时任务，延迟100毫秒后启动MainActivity
        alarmManager[AlarmManager.RTC, System.currentTimeMillis() + 10] = pendingIntent
        // 终止当前进程
        Process.killProcess(Process.myPid())
        // 退出应用程序
        exitProcess(0)
    }
}

/**
 * RTC_WAKEUP：使用设备的实时时钟触发定时任务。如果设备处于休眠状态，该定时任务将唤醒设备并在指定的时间点触发。这是一个常用的触发类型，适用于需要在设备休眠期间执行的任务。
 * RTC：使用设备的实时时钟触发定时任务。但不会唤醒设备，只有当设备处于活动状态时，定时任务才会触发。如果设备处于休眠状态，该定时任务将被推迟，直到设备再次被唤醒。
 */

/**
 * AlarmManager是Android系统提供的一个服务类，用于实现定时任务和闹钟功能。它允许您在指定的时间点执行操作，无论应用程序是否在前台运行，甚至在设备休眠状态下也能触发。
 */