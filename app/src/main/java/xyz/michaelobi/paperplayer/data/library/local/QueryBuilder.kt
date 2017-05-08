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
