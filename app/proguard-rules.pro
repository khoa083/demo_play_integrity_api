# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# Keep DataResult and all its members

# =============================================================================
# ANDROID PROJECT SPECIFIC PROGUARD RULES
# Optimized for Play Integrity API Demo Project
# =============================================================================

# Enable aggressive optimizations
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-mergeinterfacesaggressively

# =============================================================================
# CORE ANDROID FRAMEWORK
# =============================================================================
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends androidx.fragment.app.Fragment

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep parcelable classes
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# Keep BuildConfig for build variants
-keep class **.BuildConfig { *; }

# =============================================================================
# ANDROIDX & JETPACK COMPONENTS
# =============================================================================
# Architecture Components - ViewModels
-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}
-keep class * extends androidx.lifecycle.AndroidViewModel {
    <init>(...);
}

# LiveData and observers
-keepclassmembers class androidx.lifecycle.** { *; }
-keep class androidx.lifecycle.LiveData { *; }
-keep class androidx.lifecycle.MutableLiveData { *; }

# Navigation Component
-keep class androidx.navigation.** { *; }

# =============================================================================
# PROJECT SPECIFIC MODELS & REQUESTS
# =============================================================================
# Keep all data classes with proper serialization
-keep class com.kblack.demo_play_integrity_api.model.** {
    <fields>;
    <init>(...);
    <methods>;
}

-keep class com.kblack.demo_play_integrity_api.request.** {
    <fields>;
    <init>(...);
    <methods>;
}

# Keep base framework classes
-keep class com.kblack.base.** { *; }
-keep class com.kblack.base.utils.DataResult { *; }

# =============================================================================
# RETROFIT & NETWORKING
# =============================================================================
# Retrofit interfaces and annotations
-keep interface com.kblack.demo_play_integrity_api.api.APIServices { *; }
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault

# Retrofit HTTP annotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Keep Retrofit generic signatures for R8 full mode
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# OkHttp platform calls with Java 8+
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8
-dontwarn okhttp3.internal.platform.**
-keep class okhttp3.** { *; }
-keep class okio.** { *; }
-keep class retrofit2.** { *; }

# =============================================================================
# GSON SERIALIZATION
# =============================================================================
-keep class com.google.gson.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

# Keep fields annotated with @SerializedName
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep generic signature of TypeToken
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken

# =============================================================================
# GOOGLE PLAY INTEGRITY API
# =============================================================================
-keep class com.google.android.play.core.integrity.** { *; }
-dontwarn com.google.android.play.core.integrity.**

# Keep integrity response structures
-keep class com.google.android.gms.tasks.** { *; }

# =============================================================================
# FIREBASE SERVICES
# =============================================================================
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# Firebase Performance Monitoring
-keep class com.google.firebase.perf.** { *; }

# =============================================================================
# JOSE4J - JWT/JWE LIBRARY
# =============================================================================
-keep class org.jose4j.** { *; }
-dontwarn org.jose4j.**

# Keep cryptographic classes for JWT processing
-keep class java.security.** { *; }
-keep class javax.crypto.** { *; }

# =============================================================================
# LOGGING FRAMEWORKS
# =============================================================================
# SLF4J
-dontwarn org.slf4j.**
-keep class org.slf4j.** { *; }

# Remove Android Log calls in release builds
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
    public static int i(...);
    public static int w(...);
    public static int e(...);
}

# =============================================================================
# KOTLIN SPECIFIC
# =============================================================================
# Kotlin coroutines
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# Kotlin metadata
-keepattributes *Annotation*
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }

# Kotlin companion objects
-keepclassmembers class **$Companion {
    <fields>;
    <methods>;
}

# =============================================================================
# REFLECTION & ANNOTATIONS
# =============================================================================
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# =============================================================================
# SECURITY & OBFUSCATION
# =============================================================================
# Support for @Keep annotations
-keep class androidx.annotation.Keep
-keep @androidx.annotation.Keep class * {*;}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}

# Keep Utils companion object structure
-keep class com.kblack.demo_play_integrity_api.utils.Utils$Companion

# Keep ViewModelFactory dependencies
-keep class com.kblack.demo_play_integrity_api.factory.MainActivityViewModelFactory {
    <init>(...);
}

# Keep Firebase and ErrorHandler classes
-keep class com.kblack.demo_play_integrity_api.firebase.** { *; }
-keep class com.kblack.demo_play_integrity_api.utils.ErrorHandler { *; }

# Obfuscate constants but keep structure
-keepclassmembers class com.kblack.demo_play_integrity_api.utils.Constant {
    <fields>;
}

# =============================================================================
# WARNINGS SUPPRESSION
# =============================================================================
-dontwarn java.lang.invoke.**
-dontwarn java.lang.ClassValue
-dontwarn org.bouncycastle.**
-dontwarn org.conscrypt.**
-dontwarn org.openjsse.**