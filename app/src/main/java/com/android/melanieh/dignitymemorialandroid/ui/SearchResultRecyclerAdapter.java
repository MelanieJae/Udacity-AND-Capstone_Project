package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.melanieh.dignitymemorialandroid.BuildConfig;
import com.android.melanieh.dignitymemorialandroid.Obituary;
import com.android.melanieh.dignitymemorialandroid.Provider;
import com.android.melanieh.dignitymemorialandroid.R;
import com.android.melanieh.dignitymemorialandroid.Utility;

import org.w3c.dom.Text;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by melanieh on 4/16/17.
 */

public class SearchResultRecyclerAdapter
        extends RecyclerView.Adapter<SearchResultRecyclerAdapter.ResultViewHolder> {

    // obit UI fields
    TextView personNameTV;
    TextView obitPreviewTextTV;
    TextView obitFullTextLinkTV;

    // provider UI fields
    TextView providerNameTV;
    TextView addressTV;
    TextView phoneNumTV;
    TextView urlTV;
    Context context;
    ArrayList<? extends Object> objectsList;
    TextView getSiteDirectionsLinkTV;
    TextView getProviderDirectionsLinkTV;
    LinearLayout obitViewLL;
    LinearLayout providerViewLL;

    // view types:
    private static final int VIEW_OBITUARY = 0;
    private static final int VIEW_PROVIDER = 1;


    public SearchResultRecyclerAdapter(Context context, ArrayList<? extends Object> objectsList) {
        Timber.d("adapter constructor");
        Timber.d("objectsList: " + objectsList);
        this.context = context;
        this.objectsList = objectsList;
    }

    /*** Created by melanieh on 4/16/17. */

    public class ResultViewHolder extends RecyclerView.ViewHolder {

        public ResultViewHolder(View itemView) {
            super(itemView);
            personNameTV = (TextView)itemView.findViewById(R.id.person_name);
            obitPreviewTextTV = (TextView)itemView.findViewById(R.id.obit_preview_text);
            obitFullTextLinkTV = (TextView)itemView.findViewById(R.id.obit_full_text_link);

            providerNameTV = (TextView) itemView.findViewById(R.id.provider_name);
            addressTV = (TextView) itemView.findViewById(R.id.address);
            phoneNumTV = (TextView) itemView.findViewById(R.id.phone_num);
            urlTV = (TextView) itemView.findViewById(R.id.url);
            getProviderDirectionsLinkTV = (TextView) itemView.findViewById(R.id.provider_get_directions);

        }
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Timber.d("onCreateViewHolder: ");
        if ( parent instanceof RecyclerView ) {
            int layoutId = -1;
            switch (viewType) {
                case VIEW_OBITUARY: {
                    layoutId = R.layout.obit_search_list_item;
                    break;
                }
                case VIEW_PROVIDER: {
                    layoutId = R.layout.provider_search_list_item;
                    break;
                }
            }
            View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new ResultViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }

    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        Timber.d("onBindViewHolder: ");

        final Object currentObject = objectsList.get(position);

        switch (getItemViewType(position)) {
            case VIEW_OBITUARY:
                Timber.d("personNameTV: " + ((Obituary) currentObject).getPersonName());
                Timber.d("obitPreviewTextTV: " + ((Obituary) currentObject).getObitPreviewText());
                Timber.d("obitFullTextLinkTV: " + ((Obituary) currentObject).getObitFullTextLink());
                personNameTV.setText(((Obituary) currentObject).getPersonName());
                obitPreviewTextTV.setText(((Obituary) currentObject).getObitPreviewText());
                obitFullTextLinkTV.setText(((Obituary) currentObject).getObitFullTextLink());
                holder.itemView.setContentDescription(String.format(context.getString(R.string.obituary_view_cd),
                        personNameTV.getText()));

                break;
            case VIEW_PROVIDER:
                Timber.d("providerNameTV: " + ((Provider) currentObject).getProviderName());
                Timber.d("addressTV: " + ((Provider) currentObject).getAddress());
                Timber.d("phoneNumTV: " + ((Provider) currentObject).getPhoneNum());
                Timber.d("urlTV: " + ((Provider) currentObject).getProviderURL());
                providerNameTV.setText(((Provider) currentObject).getProviderName());
                addressTV.setText(((Provider) currentObject).getAddress());
                phoneNumTV.setText(((Provider) currentObject).getPhoneNum());
                urlTV.setText(((Provider) currentObject).getProviderURL());
                holder.itemView.setContentDescription(String.format
                        (context.getString(R.string.provider_view_cd), providerNameTV.getText()));
                break;
        }

        //click listeners
//        if (providerNameTV != null) {
//            providerNameTV.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (currentObject instanceof Provider) {
//                        saveProviderEntry((Provider) currentObject);
//                    }
//                }
//            });
//        }
//
//        if (getProviderDirectionsLinkTV != null) {
//            getProviderDirectionsLinkTV.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (currentObject instanceof Provider) {
//                        String address = ((Provider) currentObject).getAddress();
//                        getSiteDirections(address);
//                    }
//                }
//            });
//        }
    }

    @Override
    public int getItemCount() {
        Timber.d("getItemCount");
        if (objectsList != null) {
            return objectsList.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (objectsList.get(position) instanceof Obituary) ? VIEW_OBITUARY : VIEW_PROVIDER;
    }

    private void saveProviderEntry(Provider provider) {
        if (provider != null) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(context.getString(R.string.pref_provider_key), provider.toString());
            editor.commit();
        }
    }

    // address from query is formatted as "street address, city, state zip"
    // e.g. 100 Main St., MainStreetAmericaLand, MO 12345
    // from the JSON response so incoming address argument strings must also be formatted this way.

    private void getSiteDirections(String address) {
        // provides driving, biking and walking directions
        String mode = "dbw";
        StringBuilder gmNavigationQueryBuilder = new StringBuilder(BuildConfig.GM_NAV_BASE_QUERY);
        gmNavigationQueryBuilder.append(address);
        gmNavigationQueryBuilder.append("&mode=" + mode);
        Timber.d("gmNavQuery= " + gmNavigationQueryBuilder.toString());
        Uri gmNavIntentUri = Uri.parse(gmNavigationQueryBuilder.toString());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmNavIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);

    }

}
