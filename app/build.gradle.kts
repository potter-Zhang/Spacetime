import com.android.build.api.dsl.Packaging

plugins {
    id("com.android.application")
}

android {
    namespace = "edu.whu.spacetime"
    compileSdk = 34

    defaultConfig {
        applicationId = "edu.whu.spacetime"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas".toString()
            }
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        dataBinding = true
    }
    configurations {
        all {
            // 排除导致冲突的依赖(由dashscope引起)
            exclude("com.google.guava", "listenablefuture")
        }
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.room:room-common:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation(files("libs/poishadow-all.jar"))
    testImplementation("junit:junit:4.13.2")
    implementation("androidx.databinding:databinding-runtime:8.3.0")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("com.github.xuexiangjys:XUI:1.2.1")
    implementation ("androidx.appcompat:appcompat:1.3.1")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation ("com.google.android.material:material:1.4.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    // 悬浮球
    implementation ("com.getbase:floatingactionbutton:1.10.1")

    // 弹出菜单
    implementation ("com.github.li-xiaojun:XPopupExt:1.0.1")
    implementation ("com.github.li-xiaojun:XPopup:2.10.0")
    implementation ("androidx.appcompat:appcompat:1.3.1")
    implementation ("com.google.android.material:material:1.4.0")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")

    // 日历
    implementation ("com.github.angcyo:CalendarView:3.7.1.37")

    // 通义千问DashScope
    implementation("com.alibaba:dashscope-sdk-java:2.12.0")

    // Googla AR
    // ARCore (Google Play Services for AR) library.
    implementation("com.google.ar:core:1.42.0")

    // Obj - a simple Wavefront OBJ file loader
    // https://github.com/javagl/Obj
    implementation("de.javagl:obj:0.4.0")

    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("com.google.android.material:material:1.1.0")

    // pdfbox
    implementation ("com.tom-roush:pdfbox-android:2.0.27.0")
}