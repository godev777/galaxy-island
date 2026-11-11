# Dynamic Island Proguard Rules
-keepattributes *Annotation*
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}
-dontwarn androidx.**
