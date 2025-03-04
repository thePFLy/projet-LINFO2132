plugins {
    application
    java
}

repositories {
    mavenCentral()
}

sourceSets.main.get().java.srcDir("src")
sourceSets.test.get().java.srcDir("test")

dependencies {
    testImplementation("junit:junit:4.13.2")
    implementation("junit:junit:4.13.2")
    implementation("com.google.guava:guava:31.1-jre")
}

application {
    mainClass.set("compiler.Compiler")
}

// Ajout de la tâche pour empaqueter le code source en ZIP
tasks.register<Zip>("packageSource") {
    dependsOn("test") // Exécute les tests avant de créer l'archive
    archiveBaseName.set("${project.name}-source")
    destinationDirectory.set(file("${layout.buildDirectory.get()}/distributions"))

    from(projectDir) {
        into(projectDir.name)
        exclude(".gradle/**", ".vscode/**", ".idea/**", "build/**", ".github/**", ".git/**")
    }
}

// Si tu veux l'exécuter automatiquement lors de `build`
tasks.named("build") {
    dependsOn("packageSource")
}
