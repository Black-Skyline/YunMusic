//import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * ...
 * @author Black-skyline (Hu Shujun)
 * @email 2031649401@qq.com
 * @date 2023/7/15
 */
@Suppress("MemberVisibilityCanBePrivate", "ObjectPropertyName", "SpellCheckingInspection")
object Room {
  // https://developer.android.com/training/data-storage/room
  // https://developer.android.com/jetpack/androidx/releases/room?hl=en
  const val room_version = "2.5.0"
  
  const val room_runtime = "androidx.room:room-runtime:$room_version"
  const val room_compiler = "androidx.room:room-compiler:$room_version"
  
  // https://developer.android.google.cn/kotlin/ktx?hl=zh_cn#room
  const val room_ktx = "androidx.room:room-ktx:$room_version"
  const val room_rxjava3 = "androidx.room:room-rxjava3:$room_version"
  const val room_paging = "androidx.room:room-paging:$room_version"
  
  // optional - Test helpers
  const val room_testing = "androidx.room:room-testing:$room_version"
}

/**
 * 导入必要的room依赖
 */
fun Project.dependRoom() {
  dependencies {
    "implementation"(Room.room_runtime)
    "implementation"(Room.room_ktx)
//    "annotationProcessor"(Room.room_compiler)
    "kapt"(Room.room_compiler)
  }
}

/**
 * 导入让room支持Rxjava3的依赖
 */
fun Project.dependRoomRxjava() {
  dependencies {
    "implementation"(Room.room_rxjava3)
  }
}

/**
 * 导入让room支持Paging的依赖
 */
fun Project.dependRoomPaging() {
  dependencies {
    "implementation"(Room.room_paging)
  }
}