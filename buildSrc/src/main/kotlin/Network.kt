import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * ...
 * @author Black-skyline (Hu Shujun)
 * @email 2031649401@qq.com
 * @date 2023/7/15
 */
@Suppress("MemberVisibilityCanBePrivate", "ObjectPropertyName", "SpellCheckingInspection")
object Network {
  // https://github.com/square/retrofit
  const val retrofit_version = "2.9.0"
  
  const val retrofit = "com.squareup.retrofit2:retrofit:$retrofit_version"
  const val converter_gson = "com.squareup.retrofit2:converter-gson:$retrofit_version"
  const val adapter_rxjava3 = "com.squareup.retrofit2:adapter-rxjava3:$retrofit_version"
  
  // https://github.com/square/okhttp
  const val okhttp_version = "4.10.0"
  const val okhttp = "com.squareup.okhttp3:okhttp:$okhttp_version"
  const val logging_interceptor = "com.squareup.okhttp3:logging-interceptor:$okhttp_version"
  
  // https://github.com/google/gson
  const val gson = "com.google.code.gson:gson:2.10.1"
}

/**
 * 导入必要的关于网络请求的依赖
 */
fun Project.dependNetwork() {
  dependencies {
    "implementation"(Network.retrofit)
    "implementation"(Network.okhttp)
    "implementation"(Network.gson)
  }
}

/**
 * 以api关键字导入必要的关于网络请求的依赖，可传递给父模块，有需要再用
 */
fun Project.dependNetworkByApi() {
  dependencies {
    "api"(Network.retrofit)
    "api"(Network.okhttp)
    "api"(Network.gson)
  }
}

/**
 * 配合dependNetwork()里的依赖使用，有使用需求才依赖
 */
fun Project.dependNetworkInternal() {
  dependencies {
    "implementation"(Network.converter_gson)
    "implementation"(Network.adapter_rxjava3)
    "implementation"(Network.logging_interceptor)
  }
}

/**
 * 以api关键字导入，配合dependNetwork()里的依赖使用，可传递给父模块，有使用需求才依赖
 */
fun Project.dependNetworkInternalByApi() {
  dependencies {
    "api"(Network.converter_gson)
    "api"(Network.adapter_rxjava3)
    "api"(Network.logging_interceptor)
  }
}