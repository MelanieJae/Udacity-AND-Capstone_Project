package com.android.melanieh.dignitymemorialandroid.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.android.melanieh.dignitymemorialandroid.data.UserSelectionContract.PlanEntry;
import com.android.melanieh.dignitymemorialandroid.R;

import timber.log.Timber;

/**
 * Created by melanieh on 4/10/17.
 */

public class UserSelectionProvider extends ContentProvider {

    public static final String plansTable = PlanEntry.TABLE_NAME;
    // This cursor will hold the result of the query
    Cursor cursor;

    /** {@link UserSelectionProvider} */

    /** URI matcher code for the plan table content URI */
    private static final int PLANS = 100;

    /** URI matcher code for a single plan table entry's content URI */
    private static final int PLAN_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.
        sUriMatcher.addURI(UserSelectionContract.CONTENT_AUTHORITY,
                UserSelectionContract.PATH_PLANS, PLANS);

        sUriMatcher.addURI(UserSelectionContract.CONTENT_AUTHORITY,
                UserSelectionContract.PATH_PLANS + "/#", PLAN_ID);
    }

    SQLiteDatabase db;
    private UserSelectionDBHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = UserSelectionDBHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        db = mDbHelper.getReadableDatabase();

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PLANS:
                cursor = db.query(PlanEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PLAN_ID:
                selection = PlanEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                // Cursor containing that row of the table.
                cursor = db.query(PlanEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PLANS:
                // updating for the favorites table is only supported for the entire table so the selection
                // and selectionArgs values are set to null here
                return saveFavorite(uri, contentValues, null, null);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /*** helper method for updating the favorites  */
    private Uri saveFavorite(Uri uri, ContentValues contentValues, String selection, String selectionArgs) {
        db = mDbHelper.getWritableDatabase();

        long newRowId = db.insert(PlanEntry.TABLE_NAME, null, contentValues);

        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(PlanEntry.CONTENT_URI, newRowId);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        db = mDbHelper.getWritableDatabase();
        selection = PlanEntry._ID + "=?";
        selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
        int numRowsUpdated = db.update(PlanEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        return numRowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PLANS:
                // Delete all rows that match the selection and selection args
                deleteAllPlans();
            case PLAN_ID:
                // Delete a single row given by the ID in the URI
                selection = PlanEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return db.delete(PlanEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    private void deleteAllPlans() {
        db = mDbHelper.getWritableDatabase();
        int numRowsDeleted = db.delete(plansTable, null, null);

        if (numRowsDeleted == 0) {
            Timber.i(getContext().getString(R.string.error_deleting_plans_table));
        } else {
            Timber.i(getContext().getString(R.string.plan_table_deletion_successful));
        }
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PLANS:
                return PlanEntry.CONTENT_LIST_TYPE;
            case PLAN_ID:
                return PlanEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
