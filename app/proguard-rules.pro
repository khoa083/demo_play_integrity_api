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
-keep class com.kblack.base.utils.DataResult { *; }

# Keep all model classes (adjust the package if needed)
-keep class com.kblack.demo_play_integrity_api.model.** { *; }
-keep class com.kblack.demo_play_integrity_api.request.** { *; }

# Keep Retrofit interfaces
-keep interface com.kblack.demo_play_integrity_api.api.APIServices { *; }

# Keep Gson type adapters and model fields
-keep class com.google.gson.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

# Retrofit and OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class okio.** { *; }

# Keep all public fields and methods for Gson serialization/deserialization
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
    <fields>;
    <methods>;
}

# SLF4J - Logging framework
-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn org.slf4j.**
-keep class org.slf4j.** { *; }

# JOSE4J - JWT library
-dontwarn org.slf4j.impl.StaticLoggerBinder
-keep class org.jose4j.** { *; }
-dontwarn org.jose4j.**

# Play Integrity API
-keep class com.google.android.play.core.integrity.** { *; }
-dontwarn com.google.android.play.core.integrity.**

# Retrofit & OkHttp
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault

-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8

# Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# Android Architecture Components
-keep class * extends androidx.lifecycle.ViewModel { *; }
-keep class * extends androidx.lifecycle.AndroidViewModel { *; }

# Navigation Component
-keep class androidx.navigation.** { *; }

# General Android rules
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# Keep BuildConfig
-keep class **.BuildConfig { *; }

# Logging
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}