package xyz.michaelobi.paperplayer.playback.queue

import xyz.michaelobi.paperplayer.data.model.Song

/**
 * A song in a queue to improve SoC
 */
class QueueItem(val song: Song, val isPlaying: Boolean)