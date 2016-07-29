# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/armando/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}



#do not use -libraryjars or -in* -out* related to jars because it is done automatically
#-libraryjars libs/zbar.jar
#-libraryjars libs/sc-light-jdk15on-1.47.0.2.jar
#-libraryjars libs/scpkix-jdk15on-1.47.0.2.jar
#-libraryjars libs/scprov-jdk15on-1.47.0.2.jar
#-libraryjars libs/android-smart-image-view-1.0.0.jar
#-libraryjars libs/pass-v1.0.0.jar
#-libraryjars libs/sdk-v1.0.0.jar
#-libraryjars libs/sd-sdk-facial-processing.jar

#lk sdk
-keep class com.launchkey.android.whitelabel.sdk.** { public protected *; }

#security/encryption
-keep class org.spongycastle.** {*;}

#samsung
-keep class com.samsung.android.** {*;}
-keep interface com.samsung.android.** {*;}
-keepnames class com.samsung.android.** {*;}
-keep class * extends com.samsung.android.** { *; }
-keep public class * extends com.samsung.android.** { *; }

#zbar
-keep class net.sourceforge.** {*;}
-keep interface net.sourceforge.** {*;}
-keepnames class net.sourceforge.** {*;}

# keep setters so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers class * {
   void set*(***);
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keepattributes *Annotation*

-dontwarn com.samsung.android.**
-dontwarn javax.**
-dontwarn org.**