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
        mDbHelper = new UserSelectionDBHelper(getContext());
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
                selection = PlanEntry.COLUMN_ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
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

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        db = mDbHelper.getWritableDatabase();
        long newRowId = db.insert(PlanEntry.TABLE_NAME, null, contentValues);
        getContext().getContentResolver().notifyChange(uri, null);
        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(PlanEntry.CONTENT_URI, newRowId);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PLANS:
                return updatePlan(uri, contentValues, selection, selectionArgs);
            case PLAN_ID:
                selection = PlanEntry.COLUMN_ID + " = " + uri.getLastPathSegment();
                selectionArgs = null;
                return updatePlan(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        db = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PLANS:
                // Delete all rows that match the selection and selection args
                deleteAllPlans();
            case PLAN_ID:
                // Delete a single row given by the ID in the URI
                selection = PlanEntry.COLUMN_ID + " =?";
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

    /**
     * Update plan in the database with the given content values.
     * Return the number of rows that were successfully updated.
     */
    private int updatePlan(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // validation of content values
        // plan name
        if (values.containsKey(PlanEntry.COLUMN_PLAN_NAME)) {
            String name = values.getAsString(PlanEntry.COLUMN_PLAN_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Plan must have a name");
            }
        }

        // contact e-mail
        if (values.containsKey(PlanEntry.COLUMN_CONTACT_EMAIL)) {
            String contactEmail = values.getAsString(PlanEntry.COLUMN_CONTACT_EMAIL);
            if (contactEmail == null) {
                throw new IllegalArgumentException("Plan requires a contact e-mail");
            }
        }

        // ceremony selection
        if (values.containsKey(PlanEntry.COLUMN_CEREMONY_SELECTION)) {
            String ceremony = values.getAsString(PlanEntry.COLUMN_CEREMONY_SELECTION);
            if (ceremony == null) {
                throw new IllegalArgumentException("Plan requires a contact e-mail");
            }
        }

        // reception selection
        if (values.containsKey(PlanEntry.COLUMN_RECEPTION_SELECTION)) {
            String reception = values.getAsString(PlanEntry.COLUMN_RECEPTION_SELECTION);
            if (reception == null) {
                throw new IllegalArgumentException("Plan requires a contact e-mail");
            }
        }

        // visitation selection
        if (values.containsKey(PlanEntry.COLUMN_VISITATION_SELECTION)) {
            String visitation = values.getAsString(PlanEntry.COLUMN_VISITATION_SELECTION);
            if (visitation == null) {
                throw new IllegalArgumentException("Plan requires a contact e-mail");
            }
        }

        // container selection
        if (values.containsKey(PlanEntry.COLUMN_CONTAINER_SELECTION)) {
            String container = values.getAsString(PlanEntry.COLUMN_CONTAINER_SELECTION);
            if (container == null) {
                throw new IllegalArgumentException("Plan requires a contact e-mail");
            }
        }

        // site selection
        if (values.containsKey(PlanEntry.COLUMN_SITE_SELECTION)) {
            String site = values.getAsString(PlanEntry.COLUMN_SITE_SELECTION);
            if (site == null) {
                throw new IllegalArgumentException("Plan requires a contact e-mail");
            }
        }

        // estimated cost
        if (values.containsKey(PlanEntry.COLUMN_EST_COST)) {
            String estCost = values.getAsString(PlanEntry.COLUMN_EST_COST);
            if (estCost == null) {
                throw new IllegalArgumentException("Plan requires an contact e-mail");
            }
        }

        // If there are no values to update, then return without querying
        if (values.size() == 0) {
            return 0;
        }

        db = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = db.update(PlanEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }
}
