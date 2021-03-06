package com.shawnhu.seagull.seagull.twitter.content;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shawnhu.seagull.utils.querybuilder.NewColumn;
import com.shawnhu.seagull.utils.querybuilder.SQLQueryBuilder;
import com.shawnhu.seagull.utils.querybuilder.query.SQLCreateTableQuery;


public final class TwitterSQLiteOpenHelper extends SQLiteOpenHelper {

    public TwitterSQLiteOpenHelper(final Context context, final String name, final int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.beginTransaction();
        db.execSQL(
                createTableSQL(TweetStore.Accounts.TABLE_NAME,
                        TweetStore.Accounts.COLUMNS,
                        TweetStore.Accounts.TYPES,
                        true)
        );
        db.execSQL(
                createTableSQL(TweetStore.Statuses.TABLE_NAME,
                        TweetStore.Statuses.COLUMNS,
                        TweetStore.Statuses.TYPES,
                        true)
        );
        db.execSQL(
                createTableSQL(TweetStore.Mentions.TABLE_NAME,
                        TweetStore.Mentions.COLUMNS,
                        TweetStore.Mentions.TYPES,
                        true)
        );
        db.execSQL(
                createTableSQL(TweetStore.Drafts.TABLE_NAME,
                        TweetStore.Drafts.COLUMNS,
                        TweetStore.Drafts.TYPES,
                        true)
        );
        db.execSQL(
                createTableSQL(TweetStore.CachedUsers.TABLE_NAME,
                        TweetStore.CachedUsers.COLUMNS,
                        TweetStore.CachedUsers.TYPES,
                        true)
        );
        db.execSQL(
                createTableSQL(TweetStore.CachedStatuses.TABLE_NAME,
                        TweetStore.CachedStatuses.COLUMNS,
                        TweetStore.CachedStatuses.TYPES,
                        true)
        );
        db.execSQL(
                createTableSQL(TweetStore.CachedHashtags.TABLE_NAME,
                        TweetStore.CachedHashtags.COLUMNS,
                        TweetStore.CachedHashtags.TYPES,
                        true)
        );
        db.execSQL(
                createTableSQL(TweetStore.Filters.Users.TABLE_NAME,
                        TweetStore.Filters.Users.COLUMNS,
                        TweetStore.Filters.Users.TYPES,
                        true)
        );
        db.execSQL(
                createTableSQL(TweetStore.Filters.Keywords.TABLE_NAME,
                        TweetStore.Filters.Keywords.COLUMNS,
                        TweetStore.Filters.Keywords.TYPES,
                        true)
        );
        db.execSQL(
                createTableSQL(TweetStore.Filters.Sources.TABLE_NAME,
                        TweetStore.Filters.Sources.COLUMNS,
                        TweetStore.Filters.Sources.TYPES,
                        true)
        );
        db.execSQL(
                createTableSQL(TweetStore.Filters.Links.TABLE_NAME,
                        TweetStore.Filters.Links.COLUMNS,
                        TweetStore.Filters.Links.TYPES,
                        true)
        );
        db.execSQL(
                createTableSQL(TweetStore.CachedTrends.Local.TABLE_NAME,
                        TweetStore.CachedTrends.Local.COLUMNS,
                        TweetStore.CachedTrends.Local.TYPES,
                        true)
        );
        db.execSQL(
                createTableSQL(TweetStore.DirectMessages.TABLE_NAME,
                        TweetStore.DirectMessages.COLUMNS,
                        TweetStore.DirectMessages.TYPES,
                        true)
        );

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void onDowngrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        //TODO
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        //TODO
    }

    private static String createTableSQL(final String tableName, final String[] columns, final String[] types,
                                         final boolean createIfNotExists) {
        final SQLCreateTableQuery.Builder qb = SQLQueryBuilder.createTable(createIfNotExists, tableName);
        qb.columns(NewColumn.createNewColumns(columns, types));
        return qb.buildSQL();
    }

}
