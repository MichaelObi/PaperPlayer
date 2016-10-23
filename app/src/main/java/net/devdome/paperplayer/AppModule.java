package net.devdome.paperplayer;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * PaperPlayer
 * Michael Obi
 * 23 10 2016 7:26 AM
 */

@Module
public class AppModule {

    @Provides
    @Singleton
    Application provideApplication() {
        return new PaperPlayerApp();
    }
}
