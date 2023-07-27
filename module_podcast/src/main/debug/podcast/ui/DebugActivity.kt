package podcast.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.handsome.module.podcast.page.view.activity.PodcastActivity


class DebugActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, PodcastActivity::class.java))
    }
}