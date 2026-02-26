# Navigation3 NavKey : @Serializable NavKey 클래스 보존
# Navigation3는 런타임에 NavKey를 (역)직렬화해서 클래스, companion 직렬화기 보존 필요
-keep @kotlinx.serialization.Serializable class com.omteam.api.MainNavKey { *; }
-keep @kotlinx.serialization.Serializable class com.omteam.api.OtherNavKey { *; }
-keep @kotlinx.serialization.Serializable class com.omteam.api.EditMyGoalNavKey { *; }
-keep @kotlinx.serialization.Serializable class com.omteam.api.EditMyInfoNavKey { *; }
-keep @kotlinx.serialization.Serializable class com.omteam.api.EditExerciseTimeNavKey { *; }
-keep @kotlinx.serialization.Serializable class com.omteam.api.EditMissionTimeNavKey { *; }
-keep @kotlinx.serialization.Serializable class com.omteam.api.EditFavoriteExerciseNavKey { *; }
-keep @kotlinx.serialization.Serializable class com.omteam.api.EditPatternNavKey { *; }
-keep @kotlinx.serialization.Serializable class com.omteam.api.WebViewNavKey { *; }
-keep @kotlinx.serialization.Serializable class com.omteam.api.DetailedAnalysisNavKey { *; }
-keepclassmembers @kotlinx.serialization.Serializable class com.omteam.api.** {
    *** Companion;
    *** INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.omteam.api.**$$serializer { *; }