# Room: @Entity 클래스의 모든 필드 보존 (DB 컬럼명 매핑에 사용)
-keep @androidx.room.Entity class * { *; }

# Room: @Dao 인터페이스의 모든 함수 보존 (런타임 프록시 생성에 사용)
-keep @androidx.room.Dao interface * { *; }

# Room : Database 클래스의 abstract DAO 접근자 함수 보존
-keepclassmembers class * extends androidx.room.RoomDatabase {
    abstract *;
}