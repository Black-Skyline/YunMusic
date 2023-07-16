import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * ...
 * @author Black-skyline (Hu Shujun)
 * @email 2031649401@qq.com
 * @date 2023/7/15
 */
@Suppress("MemberVisibilityCanBePrivate", "ObjectPropertyName", "SpellCheckingInspection")
object Test {
  const val junit = "junit:junit:4.13.2"
  const val junit_android = "androidx.test.ext:junit:1.1.3"
  const val espresso_core = "androidx.test.espresso:espresso-core:3.4.0"
}

/**
 * 导入每个Android模块都有的test依赖
 */
fun Project.dependTestBase() {
  dependencies {
    "testImplementation"(Test.junit)
    "androidTestImplementation"(Test.junit_android)
    "androidTestImplementation"(Test.espresso_core)
  }
}

/**
 * 导入LiveData的test依赖
 */
fun Project.dependTestLiveData() {
  dependencies {
    "testImplementation"(Lifecycle.core_testing)
  }
}

/**
 * 导入Room的test依赖
 */
fun Project.dependTestRoom() {
  dependencies {
    "testImplementation"(Room.room_testing)
  }
}

/**
 * 导入Paging的test依赖
 */
fun Project.dependTestPaging() {
  dependencies {
    "testImplementation"(Paging.paging_testing)
  }
}

/**
 * 导入Navigation的test依赖
 */
fun Project.dependTestNavigation() {
  dependencies {
    "testImplementation"(Navigation.navigation_testing)
  }
}
