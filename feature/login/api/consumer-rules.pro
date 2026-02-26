# Navigation3 NavKey : @Serializable NavKey 클래스 보존
# Navigation3는 런타임에 NavKey를 (역)직렬화해서 클래스, companion 직렬화기 보존 필요
-keep @kotlinx.serialization.Serializable class com.omteam.api.LoginNavKey { *; }
-keep @kotlinx.serialization.Serializable class com.omteam.api.AccountLinkCompleteNavKey { *; }
-keepclassmembers @kotlinx.serialization.Serializable class com.omteam.api.** {
    *** Companion;
    *** INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.omteam.api.**$$serializer { *; }