import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * ...
 * @author Black-skyline (Hu Shujun)
 * @email 2031649401@qq.com
 * @date 2023/7/15
 */
@Suppress("MemberVisibilityCanBePrivate", "ObjectPropertyName", "SpellCheckingInspection")
object WorkManger {
  // https://developer.android.google.cn/kotlin/ktx?hl=zh_cn#workmanager
  const val work_runtime_ktx = "androidx.work:work-runtime-ktx:2.7.1"
}

/**
 * 导入WorkManger依赖
 */
fun Project.dependWorkManger() {
  dependencies {
    "implementation"(WorkManger.work_runtime_ktx)
  }
}

