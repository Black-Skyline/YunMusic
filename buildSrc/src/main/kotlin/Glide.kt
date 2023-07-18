import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * ...
 * @author Black-skyline (Hu Shujun)
 * @email 2031649401@qq.com
 * @date 2023/7/15
 */
@Suppress("MemberVisibilityCanBePrivate", "ObjectPropertyName", "SpellCheckingInspection")
object Glide {
  // https://github.com/bumptech/glide
  const val glide_version = "4.15.1"
  
  const val glide = "com.github.bumptech.glide:glide:$glide_version"

  const val glide_compiler = "com.github.bumptech.glide:compiler:$glide_version"
}

/**
 * 导入必要的Glide依赖，默认让java编译器处理注解
 */
fun Project.dependGlide() {
  dependencies {
    "implementation"(Glide.glide)
//    "kapt"(Glide.glide_compiler)
    "annotationProcessor"(Glide.glide_compiler)
  }
}
/**
 * 以api关键字导入必要的Glide依赖，可传递给父模块 (仍然需要在父模块中添加注解处理依赖)
 */
fun Project.dependGlideByApi() {
  dependencies {
    "api"(Glide.glide)
//    "kapt"(Glide.glide_compiler)
    "annotationProcessor"(Glide.glide_compiler)
  }
}