import com.toasttab.gradle.kotlin.KotlinPlugin
import com.toasttab.gradle.kotlin.configureKotlin
import com.toasttab.gradle.kotlin.configureSonarqube
import com.toasttab.gradle.kotlin.determineUnifiedVersion
import com.toasttab.gradle.kotlin.implementation
import com.toasttab.gradle.kotlin.lintKotlin
import com.toasttab.gradle.kotlin.testing.configureCrossModuleTestCoverage
import com.toasttab.gradle.kotlin.testing.configureJUnit5

buildscript {
    repositories {
        maven(url = "https://artifactory.eng.toasttab.com/artifactory/libs-release")
    }

    dependencies {
        classpath(libs.toast.gradleKotlinCommon)
        classpath(libs.toast.gradle)
    }
}

determineUnifiedVersion()
lintKotlin()
// https://toasttab.atlassian.net/wiki/spaces/ENGX/pages/1854374099/Configure+Project+in+Toast+SonarQube
configureSonarqube()
configureCrossModuleTestCoverage()

allprojects {
    group = "com.toasttab.service"
}

subprojects {
    apply(plugin = "toast-java")
    apply(plugin = "toast-maven-lite")
    apply(plugin = "toast-artifactory")

    configureKotlin(KotlinPlugin.JVM, version = rootProject.libs.versions.kotlin) {
        kotlinOptions {
            allWarningsAsErrors = true
        }
    }

    dependencies {
        implementation(platform(rootProject.libs.toast.kotlinCommonBom))
        implementation(platform(rootProject.libs.toast.servicesBom))

        implementation(rootProject.libs.kotlinLogging)
    }

    configureJUnit5(version = rootProject.libs.versions.junit)

    tasks.register<DependencyReportTask>("listAllDependencies")
    tasks.withType<Javadoc>().all { enabled = false }
}
