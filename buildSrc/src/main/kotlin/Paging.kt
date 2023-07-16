import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * ...
 * @author Black-skyline (Hu Shujun)
 * @email 2031649401@qq.com
 * @date 2023/7/15
 */
@Suppress("MemberVisibilityCanBePrivate", "ObjectPropertyName", "SpellCheckingInspection")
object Paging {
  // https://developer.android.com/jetpack/androidx/releases/paging
  const val paging_version = "3.1.1"
  
  const val paging_runtime = "androidx.paging:paging-runtime:$paging_version"
  const val paging_rxjava3 = "androidx.paging:paging-rxjava3:$paging_version"
  
  // alternatively - without Android dependencies for tests
  const val paging_testing = "androidx.paging:paging-common:$paging_version"
}

/**
 * 导入必要的Paging依赖
 */
fun Project.dependPaging() {
  dependencies {
    "implementation"(Paging.paging_runtime)
    "implementation"(Paging.paging_rxjava3)
  }
}

