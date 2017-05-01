package com.android.melanieh.dignitymemorialandroid.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by melanieh on 4/10/17.
 */

public class UserSelectionContract {

    /** This is a contract for the sole database created and consumed in this application.
     * The database stores user preferences and selections for items such as preferred location,
     * preferred provider and all service and funeral plan sections in the 'Create a Plan' Feature.
     */

    private UserSelectionContract() {
            // left blank to prevent instantiation
    }

        public static final String CONTENT_AUTHORITY = "com.android.melanieh.dignitymemorialandroid";

        public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

        public static final String PATH_PLANS = "plans";

        public static class PlanEntry implements BaseColumns {

            public static final String TABLE_NAME = "plans";

            /**
             * content URI
             */
            public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PATH_PLANS);

            /*** The MIME type of the {@link #CONTENT_URI} for a list of movies. */
            public static final String CONTENT_LIST_TYPE =
                    ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLANS;

            /*** The MIME type of the {@link #CONTENT_URI} for a single product. */
            public static final String CONTENT_ITEM_TYPE =
                    ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLANS;

            /*** table column names */
            public static final String COLUMN_ID = BaseColumns._ID;
            public static final String COLUMN_PLAN_NAME = "Plan_Name";
            public static final String COLUMN_PLAN_TYPE = "Plan_Type"; // burial, cremation or undecided
            public static final String COLUMN_CONTACT_EMAIL = "POC_Email";
            // chosen from sharedprefs
            public static final String COLUMN_PROVIDER = "Provider_Pref";

            public static final String COLUMN_CEREMONY_SELECTION = "Ceremony_Selection";
            public static final String COLUMN_VISITATION_SELECTION = "Visitation_Selection";
            public static final String COLUMN_RECEPTION_SELECTION = "Reception_Selection";
            public static final String COLUMN_SITE_SELECTION = "Site_Selection";
            public static final String COLUMN_CONTAINER_SELECTION = "Container_Selection";
            public static final String COLUMN_EST_COST = "Estimated_Cost";

            /**
             * Valid values for plan type.
             */
            public static final int BURIAL = 0;
            public static final int CREMATION = 1;
            public static final int UNDECIDED = 2;

        }

        @Override
        public String toString() {
            return "UserSelectionContract content URIs {" +
                    "BASE_URI" + BASE_URI.toString() +
                    "PRODUCTS_CONTENT_URI" + PlanEntry.CONTENT_URI.toString() + "}";
        }
    }

