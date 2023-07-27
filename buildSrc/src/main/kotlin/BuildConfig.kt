import org.gradle.api.Project

/**
 * ...
 * @author Black-skyline (Hu Shujun)
 * @email 2031649401@qq.com
 * @date 2023/7/15
 */
object BuildConfig {
    const val isRelease = true  // false意味着可以单模块调试,true表示打包成app
    const val testInstrumentationRunner="androidx.test.runner.AndroidJUnitRunner"

    const val minSdk = 24
    const val targetSdk = 33
    const val compileSdk = 33

    const val versionCode = 1
    const val versionName = "1.0.0"

}
