package xyz.vrana.popularmovies.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class QueryHelper {
    public boolean isInDB(String table, String rowAttribute, String fieldValue, Context context) {
        final FavoritesDB mDBHelper;
        mDBHelper = new FavoritesDB(context);
        final SQLiteDatabase db = mDBHelper.getReadableDatabase();
        String Query = "Select * from " + table + " where " + rowAttribute + " = " + fieldValue;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
