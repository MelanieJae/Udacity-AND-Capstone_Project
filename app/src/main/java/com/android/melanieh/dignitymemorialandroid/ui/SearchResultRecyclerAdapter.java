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
            obitViewLL = (LinearLayout)itemView.findViewById(R.id.obit_object_layout);
            providerViewLL = (LinearLayout)itemView.findViewById(R.id.provider_object_layout);
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
        View inflatedview = LayoutInflater.from(context).inflate(R.layout.search_result_list_item, parent, false);
        return new ResultViewHolder(inflatedview);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        Timber.d("onBindViewHolder: ");

        final Object currentObject = objectsList.get(position);

        if (currentObject instanceof Obituary) {
            Timber.d("personNameTV: " + ((Obituary) currentObject).getPersonName());
            Timber.d("obitPreviewTextTV: " + ((Obituary) currentObject).getObitPreviewText());
            Timber.d("obitFullTextLinkTV: " + ((Obituary) currentObject).getObitFullTextLink());
            providerViewLL.setVisibility(View.GONE);
            personNameTV.setText(((Obituary) currentObject).getPersonName());
            obitPreviewTextTV.setText(((Obituary) currentObject).getObitPreviewText());
            obitFullTextLinkTV.setText(((Obituary) currentObject).getObitFullTextLink());



        } else {
            Timber.d("providerNameTV: " + ((Provider) currentObject).getProviderName());
            Timber.d("addressTV: " + ((Provider) currentObject).getAddress());
            Timber.d("phoneNumTV: " + ((Provider) currentObject).getPhoneNum());
            Timber.d("urlTV: " + ((Provider) currentObject).getProviderURL());
            obitViewLL.setVisibility(View.GONE);
            providerNameTV.setText(((Provider) currentObject).getProviderName());
            addressTV.setText(((Provider) currentObject).getAddress());
            phoneNumTV.setText(((Provider) currentObject).getPhoneNum());
            urlTV.setText(((Provider) currentObject).getProviderURL());
        }

//        obitViewLL.setContentDescription(String.format(context.getString(R.string.obituary_view_cd),
//                personNameTV.getText()));
//        providerViewLL.setContentDescription(String.format
//                (context.getString(R.string.provider_view_cd), providerNameTV.getText()));

        // click listeners
//        providerNameTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (currentObject instanceof Provider) {
//                    saveProviderEntry((Provider)currentObject);
//                }
//            }
//        });
//        getProviderDirectionsLinkTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (currentObject instanceof Provider) {
//                    String address1 = ((Provider)currentObject).getAddress1();
//                    String address2 = ((Provider)currentObject).getAddress2();
//                    String cityStateZip = ((Provider) currentObject).getCityStateZip();
//                    getSiteDirections(address1, cityStateZip);
//                }
//            }
//        });
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

    private void saveProviderEntry(Provider provider) {
        if (provider != null) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(context.getString(R.string.pref_provider_key), provider.toString());
            editor.commit();
        }
    }

    private void getSiteDirections(String address1, String cityStateZip) {
        String destAddress = address1 + ", " + cityStateZip;
        String mode = "dbw";
        StringBuilder gmNavigationQueryBuilder = new StringBuilder(BuildConfig.GM_NAV_BASE_QUERY);
        gmNavigationQueryBuilder.append(destAddress);
        gmNavigationQueryBuilder.append("&mode=" + mode);
        Timber.d("gmNavQuery= " + gmNavigationQueryBuilder.toString());
        Uri gmNavIntentUri = Uri.parse(gmNavigationQueryBuilder.toString());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmNavIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);

    }

}
