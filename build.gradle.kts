import nu.studer.gradle.jooq.JooqExtension
import nu.studer.gradle.jooq.JooqGenerate
import org.flywaydb.gradle.task.FlywayMigrateTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Logging
import org.jooq.meta.jaxb.Property

val environment = System.getenv()

plugins {
    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.spring") version "1.8.0"
    id("org.flywaydb.flyway") version "9.8.1"
    id("nu.studer.jooq") version "7.0"
}

group = "osu.salat23"
version = "ALPHA"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven(url="https://jitpack.io")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

    implementation("com.github.cvb941:kotlin-parallel-operations:1.5.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.github.yvasyliev:java-vk-bots-longpoll-api:3.5.3")
    implementation("org.telegram:telegrambots-spring-boot-starter:6.3.0")
    implementation("com.microsoft.playwright:driver-bundle:1.28.1")
    implementation("com.microsoft.playwright:playwright:1.28.1")

    runtimeOnly("org.postgresql:postgresql")
    compileOnly("com.github.holgerbrandl:jsonbuilder:-SNAPSHOT")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.10.0")
    testImplementation("junit:junit:4.13.2")

    jooqGenerator("org.postgresql:postgresql:42.5.1")
    jooqGenerator("jakarta.xml.bind:jakarta.xml.bind-api:3.0.1")
}
configurations {
    all {
        exclude("org.apache.logging.log4j", "log4j-slf4j-impl")
    }
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<JooqGenerate> {
    dependsOn("flywayMigrate")
}

flyway {
    url = "jdbc:postgresql://localhost:5432/postgres"
    user = "postgres"
    password = "postgres"
    schemas = arrayOf("public")
    locations = arrayOf("filesystem:${project.projectDir}/src/main/resources/db/migration")
}

jooq {
    version.set("3.16.0")  // default (can be omitted)
    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)  // default (can be omitted)

    configurations {
        create("main") {  // name of the jOOQ configuration
            generateSchemaSourceOnCompilation.set(true)  // default (can be omitted)

            jooqConfiguration.apply {
                logging = Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:5432/postgres"
                    user = "postgres"
                    password = "postgres"
                    properties.add(Property().apply {
                        key = "ssl"
                        value = "false"
                    })
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        forcedTypes.addAll(listOf(
                            ForcedType().apply {
                                name = "varchar"
                                includeExpression = ".*"
                                includeTypes = "JSONB?"
                            },
                            ForcedType().apply {
                                name = "varchar"
                                includeExpression = ".*"
                                includeTypes = "INET"
                            }
                        ))
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                    }
                    target.apply {
                        packageName = "osu.salat23.circler"
                        directory = "generated/generated-src/jooq/main"  // default (can be omitted)
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

