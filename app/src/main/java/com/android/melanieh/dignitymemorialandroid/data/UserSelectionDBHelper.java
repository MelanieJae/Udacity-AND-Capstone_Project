package com.android.melanieh.dignitymemorialandroid.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.android.melanieh.dignitymemorialandroid.data.UserSelectionContract.PlanEntry;

/*** Created by melanieh on 4/10/17. */

public class UserSelectionDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="plans.db";
    // it is not intended that the database change versions so this will almost always stay at 1.
    public static final int DATABASE_VERSION = 1;
    private static UserSelectionDBHelper mInstance = null;


    public static UserSelectionDBHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new UserSelectionDBHelper(context.getApplicationContext());
        }
        return mInstance;
    }
    private UserSelectionDBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // SQL command strings
    /** data type strings for SQL command strings */
    private static final String INT_PRIMARY_KEY_AUTOINC = " INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String BIGINT = " BIGINT";
    private static final String VARCHAR_NOT_NULL_DEFAULT = " VARCHAR(50) NOT NULL DEFAULT 'Not Available'";
    private static final String DECIMAL_6_2 = " DECIMAL(6,2)";
    private static final String CONSTRAINT = " CONSTRAINT";
    private static final String UNIQUE = " UNIQUE";
    private static final String PLAN_CONSTRAINT_NAME = " " + PlanEntry.COLUMN_PLAN_NAME + "_unique";

    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_PLAN_NAME = PlanEntry.COLUMN_PLAN_NAME;
    public static final String COLUMN_PLAN_TYPE = PlanEntry.COLUMN_PLAN_TYPE;
    public static final String COLUMN_CONTACT_NAME = PlanEntry.COLUMN_CONTACT_NAME;
    public static final String COLUMN_CONTACT_PHONE = PlanEntry.COLUMN_CONTACT_PHONE;
    public static final String COLUMN_CONTACT_EMAIL = PlanEntry.COLUMN_CONTACT_EMAIL;
    // pulled from sharedprefs
    public static final String COLUMN_ZIP_CODE = PlanEntry.COLUMN_ZIP_CODE;
    public static final String COLUMN_PROVIDER = PlanEntry.COLUMN_PROVIDER;

    public static final String COLUMN_CEREMONY_SELECTION = "Ceremony_Selection";
    public static final String COLUMN_VISITATION_SELECTION = "Visitation_Selection";
    public static final String COLUMN_RECEPTION_SELECTION = "Reception_Selection";
    public static final String COLUMN_SITE_SELECTION = "Site_Selection";
    public static final String COLUMN_CONTAINER_SELECTION = "Container_Selection";
    public static final String COLUMN_EST_COST = "Estimated_Cost";

    private static final String CREATE_PLAN_TABLE = "CREATE TABLE"
            + UserSelectionContract.PlanEntry.TABLE_NAME
            + "(" + PlanEntry.COLUMN_ID + " " + INT_PRIMARY_KEY_AUTOINC + ", "
            + COLUMN_PLAN_NAME + VARCHAR_NOT_NULL_DEFAULT + ", "
            + COLUMN_PLAN_TYPE + VARCHAR_NOT_NULL_DEFAULT + ", "
            + COLUMN_CONTACT_NAME + VARCHAR_NOT_NULL_DEFAULT + ", "
            + COLUMN_CONTACT_PHONE + BIGINT + ", "
            + COLUMN_CONTACT_EMAIL + VARCHAR_NOT_NULL_DEFAULT + ", "
            + COLUMN_ZIP_CODE + BIGINT + ", "
            + COLUMN_PROVIDER + VARCHAR_NOT_NULL_DEFAULT + ", "
            + COLUMN_CEREMONY_SELECTION + VARCHAR_NOT_NULL_DEFAULT + ", "
            + COLUMN_VISITATION_SELECTION + VARCHAR_NOT_NULL_DEFAULT + ", "
            + COLUMN_RECEPTION_SELECTION + VARCHAR_NOT_NULL_DEFAULT + ", "
            + COLUMN_SITE_SELECTION + VARCHAR_NOT_NULL_DEFAULT + ", "
            + COLUMN_CONTAINER_SELECTION + VARCHAR_NOT_NULL_DEFAULT + ", "
            + COLUMN_EST_COST + DECIMAL_6_2 + ", "
            + CONSTRAINT + PLAN_CONSTRAINT_NAME + UNIQUE + "(" + PlanEntry.COLUMN_PLAN_NAME + "));";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PLAN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // since it is not intended that the database change, this method is currently not implemented.
    }


}
