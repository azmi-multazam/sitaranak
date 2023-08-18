package com.zam.sidik_padang.util;

public abstract class RoomAppDatabase {
}
/*
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Items.class, Feed.class, Paging.class}, version = 1)
public abstract class RoomAppDatabase extends RoomDatabase {

    private static RoomAppDatabase appDatabase;

    public static RoomAppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            synchronized (RoomAppDatabase.class) {
                if (appDatabase == null) {
                    appDatabase = Room.databaseBuilder(
                            context.getApplicationContext(), RoomAppDatabase.class, "db_piss_ktb.db")
                            .fallbackToDestructiveMigration()
                            //.allowMainThreadQueries()
                            .build();
                }
            }
        }
        return appDatabase;
    }

    public static void destroyInstance() {
        if (appDatabase != null) {
            appDatabase = null;
        }
    }


    //public abstract HomeDao homeDao();

}
*/