// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.3" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("org.sonarqube") version "3.5.0.2730"
}

sonarqube {
    properties {
        property ("sonar.projectKey", "GestorDeTareas")
        property ("sonar.host.url",   "http://localhost:9000")
        property("sonar.login",      "sqp_3357d61a304b175659ab775deafb50f9ef2b1d3a")
    }
}

