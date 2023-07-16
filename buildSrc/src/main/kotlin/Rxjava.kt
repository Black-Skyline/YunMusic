import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * ...
 * @author Black-skyline (Hu Shujun)
 * @email 2031649401@qq.com
 * @date 2023/7/15
 */
@Suppress("MemberVisibilityCanBePrivate", "ObjectPropertyName", "SpellCheckingInspection")
object Rxjava {
  // https://github.com/ReactiveX/RxJava
  const val rxjava3 = "io.reactivex.rxjava3:rxjava:3.1.6"
  // https://github.com/ReactiveX/RxAndroid
  const val rxjava3_android = "io.reactivex.rxjava3:rxandroid:3.0.2"
  // https://github.com/ReactiveX/RxKotlin
  const val rxjava3_kotlin = "io.reactivex.rxjava3:rxkotlin:3.0.1"
}

/**
 * 导入必要的Rxjava3依赖
 */
fun Project.dependRxjava() {
  dependencies {
    "implementation"(Rxjava.rxjava3)
    "implementation"(Rxjava.rxjava3_android)
    "implementation"(Rxjava.rxjava3_kotlin)
  }
}