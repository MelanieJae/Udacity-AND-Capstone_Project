package com.android.melanieh.dignitymemorialandroid;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.TimeZone;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import timber.log.Timber;

/**
 * Created by melanieh on 4/11/17.
 */

public class Utility {

    // check network connectivity
    // check play services
    // We'll default our latlong to 0.
    public static float DEFAULT_LATLONG = 0F;

//    public static boolean isLocationLatLonAvailable(Context context) {
//        SharedPreferences prefs
//                = PreferenceManager.getDefaultSharedPreferences(context);
//        return prefs.contains(context.getString(R.string.pref_location_latitude))
//                && prefs.contains(context.getString(R.string.pref_location_longitude));
//    }

    public static String getPreferredZipCode(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_zip_code_key),
                context.getString(R.string.pref_zip_code_default));
    }

    public static boolean entryIsEmpty(TextView view) {
        boolean isEntryEmpty = true;
        if (!TextUtils.isEmpty(view.getText())) {
            isEntryEmpty = false;
        }

        return isEntryEmpty;
    }


    public static String formatCityStateZip(Context context, String cityStateZipString) {
        // TODO: replace with regex
        String city =  cityStateZipString.substring(0,20);
        String state = cityStateZipString.substring(21, 23);
        String zip = cityStateZipString.substring(24);
        // For presentation, assume the user doesn't care about tenths of a degree.
        return "33884";
//        return String.format(context.getString(R.string.format_city_state_zip), city, state, zip);
    }
//
//    static String formatDate(long dateInMilliseconds) {
//        Date date = new Date(dateInMilliseconds);
//        return DateFormat.getDateInstance().format(date);
//    }
//
    // Format used for storing dates in the database.  ALso used for converting those strings
    // back into date objects for comparison/processing.
    public static final String DATE_FORMAT = "MMM-dd-YYYY";

    /**
     * Converts the database representation of the date into something to display
     * to users.
     *
     * @param context Context to use for resource localization
     * @param dateInMillis The date in milliseconds
     * @return formatted representation of the date.
     */
    public static String getFormattedDateString(Context context, long dateInMillis, boolean displayLongToday) {

//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(dateInMillis);
//
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat(DATE_FORMAT);
        return shortenedDateFormat.format(dateInMillis);

    }

    /**
     * Helper method to provide the icon resource id according to the weather condition id returned
     * by the OpenWeatherMap call.
     * @param weatherId from OpenWeatherMap API response
     * @return resource id for the corresponding icon. -1 if no relation is found.
     */
//    public static int getIconResourceForWeatherCondition(int weatherId) {
//        // Based on weather code data found at:
//        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
//        if (weatherId >= 200 && weatherId <= 232) {
//            return R.drawable.ic_storm;
//        } else if (weatherId >= 300 && weatherId <= 321) {
//            return R.drawable.ic_light_rain;
//        } else if (weatherId >= 500 && weatherId <= 504) {
//            return R.drawable.ic_rain;
//        } else if (weatherId == 511) {
//            return R.drawable.ic_snow;
//        } else if (weatherId >= 520 && weatherId <= 531) {
//            return R.drawable.ic_rain;
//        } else if (weatherId >= 600 && weatherId <= 622) {
//            return R.drawable.ic_snow;
//        } else if (weatherId >= 701 && weatherId <= 761) {
//            return R.drawable.ic_fog;
//        } else if (weatherId == 761 || weatherId == 781) {
//            return R.drawable.ic_storm;
//        } else if (weatherId == 800) {
//            return R.drawable.ic_clear;
//        } else if (weatherId == 801) {
//            return R.drawable.ic_light_clouds;
//        } else if (weatherId >= 802 && weatherId <= 804) {
//            return R.drawable.ic_cloudy;
//        }
//        return -1;
//    }


    /**
     * Helper method to provide the art urls according to the weather condition id returned
     * by the OpenWeatherMap call.
     *
     * @param context Context to use for retrieving the URL format
     * @param weatherId from OpenWeatherMap API response
     * @return url for the corresponding weather artwork. null if no relation is found.
     */
//    public static String getArtUrlForWeatherCondition(Context context, int weatherId) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        String formatArtUrl = prefs.getString(context.getString(R.string.pref_art_pack_key),
//                context.getString(R.string.pref_art_pack_sunshine));
//
//        // Based on weather code data found at:
//        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
//        if (weatherId >= 200 && weatherId <= 232) {
//            return String.format(Locale.US, formatArtUrl, "storm");
//        } else if (weatherId >= 300 && weatherId <= 321) {
//            return String.format(Locale.US, formatArtUrl, "light_rain");
//        } else if (weatherId >= 500 && weatherId <= 504) {
//            return String.format(Locale.US, formatArtUrl, "rain");
//        } else if (weatherId == 511) {
//            return String.format(Locale.US, formatArtUrl, "snow");
//        } else if (weatherId >= 520 && weatherId <= 531) {
//            return String.format(Locale.US, formatArtUrl, "rain");
//        } else if (weatherId >= 600 && weatherId <= 622) {
//            return String.format(Locale.US, formatArtUrl, "snow");
//        } else if (weatherId >= 701 && weatherId <= 761) {
//            return String.format(Locale.US, formatArtUrl, "fog");
//        } else if (weatherId == 761 || weatherId == 781) {
//            return String.format(Locale.US, formatArtUrl, "storm");
//        } else if (weatherId == 800) {
//            return String.format(Locale.US, formatArtUrl, "clear");
//        } else if (weatherId == 801) {
//            return String.format(Locale.US, formatArtUrl, "light_clouds");
//        } else if (weatherId >= 802 && weatherId <= 804) {
//            return String.format(Locale.US, formatArtUrl, "clouds");
//        }
//        return null;
//    }

    /**
     * Helper method to provide the art resource id according to the weather condition id returned
     * by the OpenWeatherMap call.
     * @param weatherId from OpenWeatherMap API response
     * @return resource id for the corresponding icon. -1 if no relation is found.
     */
//    public static int getArtResourceForWeatherCondition(int weatherId) {
//        // Based on weather code data found at:
//        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
//        if (weatherId >= 200 && weatherId <= 232) {
//            return R.drawable.art_storm;
//        } else if (weatherId >= 300 && weatherId <= 321) {
//            return R.drawable.art_light_rain;
//        } else if (weatherId >= 500 && weatherId <= 504) {
//            return R.drawable.art_rain;
//        } else if (weatherId == 511) {
//            return R.drawable.art_snow;
//        } else if (weatherId >= 520 && weatherId <= 531) {
//            return R.drawable.art_rain;
//        } else if (weatherId >= 600 && weatherId <= 622) {
//            return R.drawable.art_snow;
//        } else if (weatherId >= 701 && weatherId <= 761) {
//            return R.drawable.art_fog;
//        } else if (weatherId == 761 || weatherId == 781) {
//            return R.drawable.art_storm;
//        } else if (weatherId == 800) {
//            return R.drawable.art_clear;
//        } else if (weatherId == 801) {
//            return R.drawable.art_light_clouds;
//        } else if (weatherId >= 802 && weatherId <= 804) {
//            return R.drawable.art_clouds;
//        }
//        return -1;
//    }


    /*
     * Helper method to provide the correct image according to the weather condition id returned
     * by the OpenWeatherMap call.
     *
     * @param weatherId from OpenWeatherMap API response
     * @return A string URL to an appropriate image or null if no mapping is found
     */
    public static String getImageUrlForWeatherCondition(int weatherId) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (weatherId >= 200 && weatherId <= 232) {
            return "http://upload.wikimedia.org/wikipedia/commons/2/28/Thunderstorm_in_Annemasse,_France.jpg";
        } else if (weatherId >= 300 && weatherId <= 321) {
            return "http://upload.wikimedia.org/wikipedia/commons/a/a0/Rain_on_leaf_504605006.jpg";
        } else if (weatherId >= 500 && weatherId <= 504) {
            return "http://upload.wikimedia.org/wikipedia/commons/6/6c/Rain-on-Thassos.jpg";
        } else if (weatherId == 511) {
            return "http://upload.wikimedia.org/wikipedia/commons/b/b8/Fresh_snow.JPG";
        } else if (weatherId >= 520 && weatherId <= 531) {
            return "http://upload.wikimedia.org/wikipedia/commons/6/6c/Rain-on-Thassos.jpg";
        } else if (weatherId >= 600 && weatherId <= 622) {
            return "http://upload.wikimedia.org/wikipedia/commons/b/b8/Fresh_snow.JPG";
        } else if (weatherId >= 701 && weatherId <= 761) {
            return "http://upload.wikimedia.org/wikipedia/commons/e/e6/Westminster_fog_-_London_-_UK.jpg";
        } else if (weatherId == 761 || weatherId == 781) {
            return "http://upload.wikimedia.org/wikipedia/commons/d/dc/Raised_dust_ahead_of_a_severe_thunderstorm_1.jpg";
        } else if (weatherId == 800) {
            return "http://upload.wikimedia.org/wikipedia/commons/7/7e/A_few_trees_and_the_sun_(6009964513).jpg";
        } else if (weatherId == 801) {
            return "http://upload.wikimedia.org/wikipedia/commons/e/e7/Cloudy_Blue_Sky_(5031259890).jpg";
        } else if (weatherId >= 802 && weatherId <= 804) {
            return "http://upload.wikimedia.org/wikipedia/commons/5/54/Cloudy_hills_in_Elis,_Greece_2.jpg";
        }
        return null;
    }

    /**
     * Returns true if the network is available or about to become available.
     *
     * @param c Context used to get the ConnectivityManager
     * @return true if the network is available
     */
    static public boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    /**
     *
     * @param c Context used to get the SharedPreferences
     * @return the location status integer type
     */
//    @SuppressWarnings("ResourceType")
//    static public @SunshineSyncAdapter.LocationStatus
//    int getLocationStatus(Context c){
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
//        return sp.getInt(c.getString(R.string.pref_location_status_key), SunshineSyncAdapter.LOCATION_STATUS_UNKNOWN);
//    }

    /**
     * Resets the location status.  (Sets it to SunshineSyncAdapter.LOCATION_STATUS_UNKNOWN)
//     * @param c Context used to get the SharedPreferences
     */
//    static public void resetLocationStatus(Context c){
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
//        SharedPreferences.Editor spe = sp.edit();
//        spe.putInt(c.getString(R.string.pref_location_status_key), SunshineSyncAdapter.LOCATION_STATUS_UNKNOWN);
//        spe.apply();
//
//    }

    public boolean checkPlayServices(Activity activity) {
        final int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (status == ConnectionResult.SUCCESS)
        {
            Timber.d("Google Play Services available");
            return true;
        }


        if (GooglePlayServicesUtil.isUserRecoverableError(status))
        {
            final Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(status, activity, 1);
            if (errorDialog != null)
            {
                errorDialog.show();
            }
        }

        return false;
    }

}
