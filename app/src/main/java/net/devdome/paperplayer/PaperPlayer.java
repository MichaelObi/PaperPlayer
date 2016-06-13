package net.devdome.paperplayer;

import android.app.Application;

import java.util.concurrent.atomic.AtomicInteger;

public class PaperPlayer extends Application {

    public static AtomicInteger primaryKeyValue;

    @Override
    public void onCreate() {
        super.onCreate();
//        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
//        Realm realm = Realm.getInstance(config);
//        try {
//            primaryKeyValue = new AtomicInteger(realm.where(PlaylistItem.class).max("songId").intValue());
//        } catch (NullPointerException e) {
//            primaryKeyValue = new AtomicInteger(1);
//        }
//        realm.close();
    }
}
