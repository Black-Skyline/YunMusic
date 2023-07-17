package login.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.handsome.lib.music.MusicPlayActivity
import com.handsome.lib.util.BaseApp


class DebugActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val baseContext = BaseApp.mContext
        Log.d("DebugActivity:","${baseContext.toString()}")
        startActivity(Intent(this, MusicPlayActivity::class.java))
    }

}