# security/encryption
-keep class org.spongycastle.jce.** { *; }
-keep class org.spongycastle.jcajce.** { *; }

# OSM and Google Maps
-keepnames class org.osmdroid.**
-keepnames class com.google.android.gms.maps.**

# Samsung fingerprint
-keepnames class com.samsung.android.sdk.pass.**

# Pebble
-keepnames class com.getpebble.android.**

# Google
-keepnames class com.google.android.gms.common.**
-keepnames class com.google.android.gms.wearable.**

# Fraudforce
-keepnames class com.iovation.mobile.android.FraudForceManager { *; }