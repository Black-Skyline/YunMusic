package find.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.handsome.module.find.view.activity.FindActivity
import com.handsome.module.find.view.activity.RecommendDetailActivity

class DebugActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, FindActivity::class.java))
//        startActivity(Intent(this, RecommendDetailActivity::class.java))
//        startActivity(Intent(this, SpecialEditionActivity::class.java))
    }
}