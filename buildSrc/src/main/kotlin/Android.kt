import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies


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
    const val `core-ktx` = "androidx.core:core-ktx:1.10.0"

    // https://developer.android.google.cn/kotlin/ktx/extensions-list#dependency_4
    const val `collection-ktx` = "androidx.collection:collection-ktx:1.2.0"

    // https://developer.android.google.cn/kotlin/ktx/extensions-list#androidxfragmentapp
    const val `fragment-ktx` = "androidx.fragment:fragment-ktx:1.5.6"

    // https://developer.android.google.cn/kotlin/ktx/extensions-list#androidxactivity
    const val `activity-ktx` = "androidx.activity:activity-ktx:1.6.1"
}

/**
 * 所有使用 build_logic 插件的模块都默认依赖了该 Android 最基础依赖
 */
fun Project.dependAndroidBase() {
    dependencies {
        "implementation"(Android.appcompat)
    }
}

/*
* 所有 module 模块都已经默认依赖
*
* 如果你的 api 模块需要使用，建议自己按需依赖，一般情况下 api 模块是不需要这些东西的
* */
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

/*
* 所有 module 模块都已经默认依赖
*
* 如果你的 api 模块需要使用，建议自己按需依赖，一般情况下 api 模块是不需要这些东西的
* */
fun Project.dependAndroidKtx() {
    dependencies {
        "implementation"(Android.`core-ktx`)
        "implementation"(Android.`collection-ktx`)
        "implementation"(Android.`fragment-ktx`)
        "implementation"(Android.`activity-ktx`)
    }
}

/**
 * 给lib模块准备的，这些基本都是所有Android工程都需要
 */
fun Project.dependAndroidCommonBase() {
    dependencies {
        "api"(Android.appcompat)
        "api"(Android.`core-ktx`)
        "api"(Android.constraintlayout)
        "api"(Android.material)
        "api"(ARouter.arouter_api)
        "kapt"(ARouter.arouter_compiler)
    }
}