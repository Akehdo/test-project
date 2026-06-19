plugins {
    `java-library`
    id("com.google.protobuf") version "0.9.4"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    // BOM фиксирует одну версию для всех grpc-* зависимостей
    api(platform("io.grpc:grpc-bom:1.69.0"))

    // Эти зависимости должны быть api, потому что сгенерированные классы будут использоваться другими модулями
    api("io.grpc:grpc-stub")
    api("io.grpc:grpc-protobuf")
    api("io.grpc:grpc-api")

    // Protobuf classes: Message, GeneratedMessageV3 и т.д.
    api("com.google.protobuf:protobuf-java:3.25.1")

    // Нужно для @javax.annotation.Generated в сгенерированном gRPC-коде
    compileOnly("javax.annotation:javax.annotation-api:1.3.2")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.1"
    }

    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.69.0"
        }
    }

    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                create("grpc")
            }
        }
    }
}
