# Add project specific ProGuard rules here.

# Keep all app classes
-keep class com.ios26keyboard.** { *; }

# Keep InputMethodService
-keep class * extends android.inputmethodservice.InputMethodService { *; }
-keep class * extends android.app.Activity { *; }

# Keep keyboard views
-keep class com.ios26keyboard.view.** { *; }

# Keep model classes
-keep class com.ios26keyboard.model.** { *; }

# Keep service classes
-keep class com.ios26keyboard.service.** { *; }

# Keep animation utilities
-keep class com.ios26keyboard.utils.** { *; }

# Keep R classes
-keepclassmembers class **.R$* {
    public static <fields>;
}

# Keep AndroidX classes
-keep class androidx.** { *; }
-keep interface androidx.** { *; }
-dontwarn androidx.**

# Keep CardView
-keep class androidx.cardview.widget.CardView { *; }

# Keep AppCompat
-keep class androidx.appcompat.** { *; }

# Keep Kotlin classes
-keep class kotlin.** { *; }
-dontwarn kotlin.**

# Keep view binding
-keepclassmembers class * implements androidx.viewbinding.ViewBinding {
    public static * inflate(...);
    public static * bind(...);
}

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
