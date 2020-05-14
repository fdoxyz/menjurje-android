// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
//    ext.kotlin_version = '1.3.72'
    repositories {
        google()
        jcenter()
        
    }
    dependencies {
        val kotlinVersion = findProperty("version.org.jetbrains.kotlin") as String
        val agpVersion = findProperty("version.com.android.tools.build..gradle") as String
        classpath("com.android.tools.build:gradle:$agpVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}