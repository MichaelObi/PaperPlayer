package net.devdome.paperplayer.data.library.local;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * PaperPlayer
 * Michael Obi
 * 17 11 2016 10:32 PM
 */

public class QueryBuilder {


    private final Context context;
    private final Uri uri;

    private String where;
    private String orderBy;

    public QueryBuilder(Context context, Uri uri) {
        this.context = context;
        this.uri = uri;
    }

    /**
     * Add a where clause to the MediaStore query
     *
     * @param clause
     * @return
     */
    public QueryBuilder where(String clause) {
        this.where = clause;
        return this;
    }

    /**
     * Set the column to order query results
     *
     * @param sortOrder
     * @return
     */
    public QueryBuilder orderBy(String sortOrder) {
        this.orderBy = sortOrder;
        return this;
    }

    /**
     * Query the ContentResolver and return cursor
     * @return
     */
    public Cursor query() {
        return context.getContentResolver().query(
                uri,
                null,
                where,
                null,
                orderBy
        );
    }

}
