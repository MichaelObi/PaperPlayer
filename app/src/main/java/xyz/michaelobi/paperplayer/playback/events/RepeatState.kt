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

package xyz.michaelobi.paperplayer.playback.events

import android.support.annotation.IntDef


/**
 * PaperPlayer
 * Michael Obi
 * 01 08 2017 11:58 AM
 */
class RepeatState(@RepeatType val repeatType: Long = RepeatState.REPEAT_NONE) {

    @IntDef(REPEAT_NONE, REPEAT_ONE, REPEAT_ALL)
    @Retention(AnnotationRetention.SOURCE)
    annotation class RepeatType

    companion object {
        const val REPEAT_NONE = 0L
        const val REPEAT_ALL = 1L
        const val REPEAT_ONE = 2L
    }
}