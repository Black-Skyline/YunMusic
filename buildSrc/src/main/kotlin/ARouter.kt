//import com.google.devtools.ksp.gradle.KspExtension

@Suppress("MemberVisibilityCanBePrivate", "ObjectPropertyName", "SpellCheckingInspection")
object ARouter {
  // 原生ARouter https://github.com/alibaba/ARouter
  // 适配 AGP8: https://github.com/jadepeakpoet/ARouter
  const val arouter_version = "1.5.2"
  const val arouter_version_jadepeakpoet = "1.0.3"
  
  const val arouter_api_low = "com.alibaba:arouter-api:$arouter_version"
  const val arouter_compiler_low = "com.alibaba:arouter-compiler:$arouter_version"
  const val arouter_api = "com.github.jadepeakpoet.ARouter:arouter-api:$arouter_version_jadepeakpoet"
  const val arouter_compiler = "com.github.jadepeakpoet.ARouter:arouter-compiler:$arouter_version_jadepeakpoet"
  
  // 第三方的 ksp 版本: https://github.com/JailedBird/ArouterKspCompiler
  const val arouter_ksp = "com.github.JailedBird:ArouterKspCompiler:1.8.20-1.0.2"
}

///**
// * 所有使用 build_logic 插件的模块都默认依赖了 ARouter
// */
//fun Project.dependARouter() {
//  // ksp 设置
//  extensions.configure<KspExtension> {
//    arg("AROUTER_MODULE_NAME", project.name)
//  }
//  dependencies {
//    "implementation"(ARouter.`arouter-api`)
////    "kapt"(ARouter.`arouter-compiler`)
//    "ksp"(ARouter.`arouter-ksp`)
//  }
//}