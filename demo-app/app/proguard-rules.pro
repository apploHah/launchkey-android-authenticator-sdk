# Fraudforce
-keep class com.iovation.mobile.android.** { *; }

-keepclassmembers class * {
    *** get*();
    void set*(***);
}