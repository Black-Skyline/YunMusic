import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * ...
 * @author Black-skyline (Hu Shujun)
 * @email 2031649401@qq.com
 * @date 2023/7/15
 */
@Suppress("MemberVisibilityCanBePrivate", "ObjectPropertyName", "SpellCheckingInspection")
object Navigation {
  // navigation 因为有些坑，所以暂不使用，大部分情况可以使用 vp2 代替
  // https://developer.android.com/guide/navigation/navigation-getting-started#Set-up
  // https://developer.android.google.cn/kotlin/ktx?hl=zh_cn#navigation
  const val navigation_version = "2.5.3"
  const val navigation_runtime_ktx = "androidx.navigation:navigation-runtime-ktx:$navigation_version"
  const val navigation_fragment_ktx = "androidx.navigation:navigation-fragment-ktx:$navigation_version"
  const val navigation_ui_ktx = "androidx.navigation:navigation-test.ui-ktx:$navigation_version"
  
  // Testing Navigation
  const val navigation_testing = "androidx.navigation:navigation-testing:$navigation_version"
}

/**
 * 导入必要的Navigation依赖，用起来比较麻烦，大部分时候vp2可以作为其替代
 */
fun Project.dependNavigation() {
  dependencies {
    "implementation"(Navigation.navigation_runtime_ktx)
    "implementation"(Navigation.navigation_fragment_ktx)
    "implementation"(Navigation.navigation_ui_ktx)
  }
}