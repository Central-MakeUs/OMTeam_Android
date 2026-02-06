# ====================================
# Room Database
# ====================================

# Room 기본 규칙
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Room DAO 인터페이스 유지
-keep interface * extends androidx.room.Dao { *; }
-keepclassmembers interface * extends androidx.room.Dao {
    *;
}

# Room Entity 클래스의 모든 필드 유지
-keepclassmembers @androidx.room.Entity class * {
    *;
}

# Room Database 구현체 유지
-keep class com.omteam.omt.core.database.** extends androidx.room.RoomDatabase {
    *;
}

# Room TypeConverter 유지
-keep class * {
    @androidx.room.TypeConverter <methods>;
}
-keepclassmembers class * {
    @androidx.room.TypeConverter *;
}

# ====================================
# Database 모듈 클래스
# ====================================

# Entity 클래스 유지
-keep class com.omteam.omt.core.database.entity.** { *; }

# DAO 인터페이스 유지
-keep interface com.omteam.omt.core.database.dao.** { *; }

# Database 클래스 유지
-keep class com.omteam.omt.core.database.AppDatabase { *; }
-keep class com.omteam.omt.core.database.*Database { *; }