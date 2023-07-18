import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * ...
 * @author Black-skyline (Hu Shujun)
 * @email 2031649401@qq.com
 * @date 2023/7/15
 */
@Suppress("MemberVisibilityCanBePrivate", "ObjectPropertyName", "SpellCheckingInspection")
object Android {
    // 基础库 这个版本号跟 targetSdk 相关
    const val appcompat = "androidx.appcompat:appcompat:1.6.1"

    // 官方控件库
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.1.4"
    const val recyclerview = "androidx.recyclerview:recyclerview:1.3.0"
    const val cardview = "androidx.cardview:cardview:1.0.0"
    const val viewpager2 = "androidx.viewpager2:viewpager2:1.0.0"
    const val material = "com.google.android.material:material:1.8.0"
    const val swiperefreshlayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
    const val flexbox = "com.google.android.flexbox:flexbox:3.0.0"

    // 官方扩展库
    // https://developer.android.google.cn/kotlin/ktx?hl=zh_cn#core
    const val core_ktx = "androidx.core:core-ktx:1.10.1"

    // https://developer.android.google.cn/kotlin/ktx/extensions-list#dependency_4
    const val collection_ktx = "androidx.collection:collection-ktx:1.2.0"

    // https://developer.android.google.cn/kotlin/ktx/extensions-list#androidxfragmentapp
    const val fragment_ktx = "androidx.fragment:fragment-ktx:1.5.7"

    // https://developer.android.google.cn/kotlin/ktx/extensions-list#androidxactivity
    const val activity_ktx = "androidx.activity:activity-ktx:1.7.2"
}



/**
 * 导入常用的view控件与Ui布局依赖
 */
fun Project.dependAndroidView() {
    dependencies {
        "implementation"(Android.constraintlayout)
        "implementation"(Android.recyclerview)
        "implementation"(Android.cardview)
        "implementation"(Android.viewpager2)
        "implementation"(Android.material)
        "implementation"(Android.swiperefreshlayout)
        "implementation"(Android.flexbox)
    }
}

/**
 * 关于kts的官方拓展库
 */
fun Project.dependAndroidKtx() {
    dependencies {
        "implementation"(Android.core_ktx)
        "implementation"(Android.collection_ktx)
        "implementation"(Android.fragment_ktx)
        "implementation"(Android.activity_ktx)
    }
}

/**
 * 给lib模块准备的，这些基本都是所有Android工程都需要
 */
fun Project.dependAndroidCommonBase() {
    dependencies {
        "api"(Android.appcompat)
        "api"(Android.core_ktx)
        "api"(Android.constraintlayout)
        "api"(Android.material)
        "api"(ARouter.arouter_api)
        "kapt"(ARouter.arouter_compiler)
    }
}

/**
 * Android最基础的appcompat依赖，作为dependAndroidCommonBase()的低位替补
 */
fun Project.dependAndroidLowBase() {
    dependencies {
        "implementation"(Android.appcompat)
    }
}