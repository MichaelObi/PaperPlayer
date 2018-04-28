/*
 * MIT License
 *
 * Copyright (c) 2017 MIchael Obi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package xyz.michaelobi.paperplayer.injection;

import android.content.Context;

import xyz.michaelobi.paperplayer.data.MusicRepository;
import xyz.michaelobi.paperplayer.data.MusicRepositoryInterface;
import xyz.michaelobi.paperplayer.data.library.LibraryManager;
import xyz.michaelobi.paperplayer.data.library.local.LocalLibraryManager;
import xyz.michaelobi.paperplayer.event.RxBus;
import xyz.michaelobi.paperplayer.playback.queue.LocalQueueManager;
import xyz.michaelobi.paperplayer.playback.queue.QueueManager;

/**
 * PaperPlayer
 * Michael Obi
 * 23 10 2016 10:36 AM
 */

public class Injector {
    private static RxBus eventBus;
    private static QueueManager queueManager;

    public static RxBus provideEventBus() {
        if (eventBus == null) {
            eventBus = new RxBus();
        }

        return eventBus;
    }

    public static QueueManager provideQueueManager() {
        if (queueManager == null) {
            queueManager = new LocalQueueManager();
        }

        return queueManager;
    }

    public static MusicRepositoryInterface provideMusicRepository(Context context) {
        return new MusicRepository(provideLibraryManager(context));
    }

    public static LibraryManager provideLibraryManager(Context context) {
        return new LocalLibraryManager(context);
    }
}
