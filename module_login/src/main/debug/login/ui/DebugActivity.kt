package login.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.handsome.lib.music.MusicPlayActivity
import com.handsome.lib.util.BaseApp
import com.handsome.module.login.page.view.LoginActivity


class DebugActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val baseContext = BaseApp.mContext
        Log.d("DebugActivity:","${baseContext.toString()}")
//        val tempData = mutableListOf<WrapPlayInfo>()
//
//        tempData.add(WrapPlayInfo("나 어떡해 ('77 제1회 MBC대학가요제 대상')", "V.A.", 2064562707))
//        tempData.add(WrapPlayInfo("画", "赵雷", 202369))
//        tempData.add(WrapPlayInfo("独一无二的美丽", "李萌", 2065902610))
//        tempData.add(WrapPlayInfo("逝年", "夏小虎", 32957955))
        startActivity(Intent(this, MusicPlayActivity::class.java))
//        MusicPlayActivity.startWithPlayList(this, tempData, 0)
    }
}