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

package xyz.michaelobi.paperplayer.data.library.local

import android.content.Context
import android.database.Cursor
import android.net.Uri

/**
 * PaperPlayer
 * Michael Obi
 * 17 11 2016 10:32 PM
 */

class QueryBuilder(private val context: Context, private val uri: Uri) {

    private var where: String? = null
    private var orderBy: String? = null

    /**
     * Add a where clause to the MediaStore query

     * @param clause
     * *
     * @return
     */
    fun where(clause: String): QueryBuilder {
        this.where = clause
        return this
    }

    /**
     * Set the column to order query results

     * @param sortOrder
     * *
     * @return
     */
    fun orderBy(sortOrder: String): QueryBuilder {
        this.orderBy = sortOrder
        return this
    }

    /**
     * Query the ContentResolver and return cursor
     * @return
     */
    fun query(): Cursor {
        return context.contentResolver.query(
                uri,
                null,
                where, null,
                orderBy
        )
    }

}
