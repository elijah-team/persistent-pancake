/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    `java-library`
    `maven-publish`
}

dependencies {
    api("com.squareup.okhttp3:okhttp:4.10.0")
    api("com.codahale.metrics:metrics-healthchecks:3.0.2")
    api("com.codahale.metrics:metrics-core:3.0.2")
    api("org.jooq:jool:0.9.14")
    api("org.hashids:hashids:1.0.3")
    api("com.typesafe:config:1.4.2")
    api("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    api("io.undertow:undertow-servlet:2.3.4.Final")
    api("org.apache.commons:commons-lang3:3.3")
    api("net.java.dev.jna:jna:4.0.0")
    api("io.reactivex.rxjava3:rxjava:3.1.0")
    api("me.friwi:jcefmaven:105.3.36")
    api("org.reactivestreams:reactive-streams:1.0.3")
    api("commons-codec:commons-codec:1.15")
    api("org.slf4j:slf4j-simple:1.7.25")
    api("org.jdeferred.v2:jdeferred-core:2.0.0")
    api("org.eclipse.jdt:org.eclipse.jdt.annotation:2.2.200")
    api("org.jetbrains:annotations:16.0.2")
    api("antlr:antlr:2.7.7")
    api("commons-cli:commons-cli:1.4")
    api("com.github.spotbugs:spotbugs-annotations:4.0.1")
    api("com.google.guava:guava:30.0-jre")
    api("tripleo.buffers:buffers-v1:0.0.3")
    api("tripleo.util.range:range-v1:0.0.3b")
    api("io.undertow:undertow-core:2.2.22.Final")
    testImplementation("org.easymock:easymock:5.1.0")
    testImplementation("junit:junit:4.13.1")
}

group = "tripleo.elijah"
version = "0.0.9-flum-mainline-230416-SNAPSHOT"
//name = "fluffy-undertow"
java.sourceCompatibility = JavaVersion.VERSION_16