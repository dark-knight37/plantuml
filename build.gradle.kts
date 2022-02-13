//    permits to start the build setting the javac release parameter, no parameter means build for java8:
// gradle clean build -x javaDoc -PjavacRelease=8
// gradle clean build -x javaDoc -PjavacRelease=17
//    also supported is to build first, with java17, then switch the java version, and run the test with java8:
// gradle clean build -x javaDoc -x test
// gradle test
val javacRelease = (project.findProperty("javacRelease") ?: "8") as String

plugins {
	java
	`maven-publish`
	signing
}

repositories {
	mavenLocal()
	mavenCentral()
}

dependencies {
	compileOnly("org.apache.ant:ant:1.10.12")
	testImplementation("org.assertj:assertj-core:3.22.0")
	testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
	testImplementation("org.scilab.forge:jlatexmath:1.0.7")
}

group = "net.sourceforge.plantuml"
description = "PlantUML"

java {
	withSourcesJar()
	withJavadocJar()
}

sourceSets {
	main {
		java {
			srcDirs("src")
		}
		resources {
			srcDirs("src")
			include("**/graphviz.dat")
			include("**/*.png")
			include("**/*.svg")
			include("**/*.txt")
		}
	}
	test {
		java {
			srcDirs("test")
		}
		resources {
			srcDirs(".")
			include("skin/**/*.skin")
			include("themes/**/*.puml")
		}
	}
}

tasks.compileJava {
	if (JavaVersion.current().isJava8) {
		java.targetCompatibility = JavaVersion.VERSION_1_8
	} else {
		options.release.set(Integer.parseInt(javacRelease))
	}
}

tasks.withType<Jar> {
	manifest {
		attributes["Main-Class"] = "net.sourceforge.plantuml.Run"
		attributes["Implementation-Version"] = archiveVersion
		attributes["Build-Jdk-Spec"] = System.getProperty("java.specification.version")
		from("manifest.txt")
	}

	dependsOn(configurations.runtimeClasspath)
	from({
		configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
	})

	// source sets for java and resources are on "src", only put once into the jar
	duplicatesStrategy = org.gradle.api.file.DuplicatesStrategy.EXCLUDE
	from("skin") { into("skin") }
	from("stdlib") { into("stdlib") }
	from("svg") { into("svg") }
	from("themes") { into("themes") }
}

publishing {
	publications.create<MavenPublication>("maven") {
		from(components["java"])
	}
}

tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
	options {
		this as StandardJavadocDocletOptions
		addBooleanOption("Xdoclint:none", true)
		addStringOption("Xmaxwarns", "1")
		encoding = "UTF-8"
		isUse = true
	}
}

tasks.test {
	useJUnitPlatform()
	testLogging.showStandardStreams = true
}

signing {
	if (hasProperty("signing.gnupg.passphrase")) {
		useGpgCmd()
		sign(publishing.publications["maven"])
	}
}
