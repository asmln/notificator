plugins {
    java
    id("org.springframework.boot") version "4.1.1" apply false
}

allprojects {
    group = "com.example"
    version = "0.0.1-SNAPSHOT"
    repositories {
        mavenCentral()
    }
}

subprojects {
    pluginManager.withPlugin("java") {
        configure<JavaPluginExtension> {
            toolchain {
                // Версия java
                languageVersion.set(JavaLanguageVersion.of(25))
            }
        }
        dependencies {
            testImplementation(platform("org.junit:junit-bom:6.1.3"))
            testImplementation("org.junit.jupiter:junit-jupiter")
            testRuntimeOnly("org.junit.platform:junit-platform-launcher")
        }
    }

    // Настройки к задаче тестирования во всех подпроектах
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        jvmArgs("-XX:+EnableDynamicAgentLoading", "-Xshare:off")
    }

    // Настройки к задачам JavaExec во всех подпроектах
    tasks.withType<JavaExec>().configureEach {
        systemProperty("sun.stdout.encoding", "UTF-8")
        systemProperty("sun.stderr.encoding", "UTF-8")
    }
}