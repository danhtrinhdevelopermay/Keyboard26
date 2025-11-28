# Add project specific ProGuard rules here.

# Keep InputMethodService
-keep class * extends android.inputmethodservice.InputMethodService { *; }

# Keep keyboard views
-keep class com.ios26keyboard.view.** { *; }

# Keep model classes
-keep class com.ios26keyboard.model.** { *; }

# Keep animation utilities
-keep class com.ios26keyboard.utils.** { *; }

# Keep R classes
-keepclassmembers class **.R$* {
    public static <fields>;
}

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
