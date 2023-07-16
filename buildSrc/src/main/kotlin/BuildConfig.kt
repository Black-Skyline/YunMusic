import org.gradle.api.Project

object BuildConfig {
    const val isRelease = false  // false意味着可以单模块调试,true表示打包成app
    const val testInstrumentationRunner="androidx.test.runner.AndroidJUnitRunner"

    const val minSdk = 24
    const val targetSdk = 33
    const val compileSdk = 33

    const val versionCode = 1
    const val versionName = "1.0.0"

//    fun getApplicationId(project: Project): String {
//        return when (project.name) {
//            "app" -> {
//                "com.handsome.test"
//                if (project.gradle.startParameter.taskNames.any { it.contains("Release") }) {
//                    "com.handsome.test"
//                } else {
//                    // debug 状态下使用 debug 的包名，方便测试
//                    "com.handsome.test.debug"
//                }
//            }
//            else -> "com.handsome.${project.name}"
//        }
//    }
}
