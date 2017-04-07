package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.Context;

import com.squareup.picasso.Cache;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executors;

/**
 * Created by melanieh on 4/2/17.
 */

public class ImageHandler {

    private static Picasso instance;

    public static Picasso getSharedInstance(Context context)
    {
        if(instance == null)
        {
            instance =
                    new Picasso.Builder(context).executor(Executors.newSingleThreadExecutor())
                            .memoryCache(Cache.NONE).indicatorsEnabled(true).build();
        }
        return instance;
    }
}