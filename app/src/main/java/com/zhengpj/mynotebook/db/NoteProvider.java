package com.zhengpj.mynotebook.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NoteProvider extends ContentProvider {
    private static final String TAG = "NoteProvider";

    private SQLiteDatabase mSqLiteDatabase;
    private DatabaseHelper mDatabaseHelper;

    public static final int NOTEBOOK_COLLECTION = 1;
    public static final int NOTEBOOK_SINGLE = 2;
    public static final int NOTEBOOK_FILTER = 5;

    public static final UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MetaData.AUTHORITY, "note", NOTEBOOK_COLLECTION);
        uriMatcher.addURI(MetaData.AUTHORITY, "note/#", NOTEBOOK_SINGLE);
        uriMatcher.addURI(MetaData.AUTHORITY, "note_filter/*", NOTEBOOK_FILTER);
    }
    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, MetaData.DATABASE_NAME, null, MetaData.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //notes table
            db.execSQL("create table " + MetaData.NoteColumns.TABLE_NAME
                    + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + MetaData.NoteColumns.OBJECT_ID + " TEXT, "
                    + MetaData.NoteColumns.CONTENT + " TEXT, "
                    + MetaData.NoteColumns.GROUP + " TEXT, "
                    + MetaData.NoteColumns.TIME + " LONG);"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //如果数据库版本发生变化，则删掉重建
            db.execSQL("DROP TABLE IF EXISTS " + MetaData.NoteColumns.TABLE_NAME);
            onCreate(db);
        }

    }
    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();

        int count = 0;
        switch (uriMatcher.match(uri)) {
            case NOTEBOOK_COLLECTION:

                count = mSqLiteDatabase.delete(MetaData.NoteColumns.TABLE_NAME, selection, selectionArgs);
                break;
            case NOTEBOOK_SINGLE:
                String segment = uri.getPathSegments().get(1);
                if (selection != null && segment.length() > 0) {
                    selection = "_id=" + segment + " AND (" + selection + ")";
                }else {
                    selection = "_id=" +  segment;
                }
                count = mSqLiteDatabase.delete(MetaData.NoteColumns.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("UnKnow Uri:" + uri);
        }

        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
