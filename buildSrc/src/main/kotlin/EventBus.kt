import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/5/27 15:48
 */
@Suppress("MemberVisibilityCanBePrivate", "ObjectPropertyName", "SpellCheckingInspection")
object EventBus {
  // https://github.com/greenrobot/EventBus
  const val eventBus = "org.greenrobot:eventbus:3.3.1"
}

fun Project.dependEventBus() {
  dependencies {
    "implementation"(EventBus.eventBus)
  }
}