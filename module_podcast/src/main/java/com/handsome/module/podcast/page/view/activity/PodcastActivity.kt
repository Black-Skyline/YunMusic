package com.handsome.module.podcast.page.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.handsome.module.podcast.R
import com.handsome.module.podcast.page.view.fragment.PodcastFragment

class PodcastActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_podcast)
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction().apply {
                add(
                    R.id.entrance_fragment_container,
                    PodcastFragment(),
                    "PodcastFragment_INSTANCE"
                )
                commit()
            }
    }
}