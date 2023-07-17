import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * ...
 * @author Black-skyline (Hu Shujun)
 * @email 2031649401@qq.com
 * @date 2023/7/15
 */
@Suppress("MemberVisibilityCanBePrivate", "ObjectPropertyName", "SpellCheckingInspection")
object Lifecycle {
  // 官方 lifecycle 扩展
  // https://developer.android.google.cn/jetpack/androidx/releases/lifecycle
  // https://developer.android.google.cn/kotlin/ktx/extensions-list?hl=zh_cn#androidxlifecycle
  const val lifecycle_version = "2.5.1"
  const val viewmodel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
  const val livedata_ktx = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
  // Lifecycles only (without ViewModel or LiveData)
  const val runtime_ktx = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
  // Annotation processor
  const val lifecycle_compiler = "androidx.lifecycle:lifecycle-compiler:$lifecycle_version"
  
  // 下面这几个是可选的，默认不主动依赖
  // optional - helpers for implementing LifecycleOwner in a Service
  const val lifecycle_service = "androidx.lifecycle:lifecycle-service:$lifecycle_version"
  
  // optional - ProcessLifecycleOwner provides a lifecycle for the whole application process
  const val lifecycle_process = "androidx.lifecycle:lifecycle-process:$lifecycle_version"
  
  // optional - ReactiveStreams support for LiveData
  // LiveData 与 Rxjava、Flow 的转换
  const val lifecycle_reactivestreams_ktx = "androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycle_version"
  
  // optional - Test helpers for LiveData
  const val arch_version = "2.1.0"
  const val core_testing = "androidx.arch.core:core-testing:$arch_version"
}

/**
 * 导入Lifecycle相关依赖
 */
fun Project.dependLifecycleKtx() {
  dependencies {
    "implementation"(Lifecycle.viewmodel_ktx)
    "implementation"(Lifecycle.livedata_ktx)
    "implementation"(Lifecycle.runtime_ktx)
    "kapt"(Lifecycle.lifecycle_compiler)
  }
}
/**
 * 以api关键字导入Lifecycle相关依赖，可传递给父模块，仍需手动添加lifecycle_compiler的注解处理依赖
 */
fun Project.dependLifecycleKtxByApi() {
  dependencies {
    "api"(Lifecycle.viewmodel_ktx)
    "api"(Lifecycle.livedata_ktx)
    "api"(Lifecycle.runtime_ktx)
    "kapt"(Lifecycle.lifecycle_compiler)
  }
}