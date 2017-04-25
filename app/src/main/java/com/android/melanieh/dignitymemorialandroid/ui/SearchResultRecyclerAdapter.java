package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.melanieh.dignitymemorialandroid.Obituary;
import com.android.melanieh.dignitymemorialandroid.Provider;
import com.android.melanieh.dignitymemorialandroid.R;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by melanieh on 4/16/17.
 */

public class SearchResultRecyclerAdapter extends RecyclerView.Adapter<SearchResultRecyclerAdapter.ResultViewHolder> {

    // obit UI fields
    TextView personNameTV;
    TextView dateSpanTV;
    TextView obitTextTV;

    // provider UI fields
    TextView providerNameTV;
    TextView address1TV;
    TextView address2TV;
    TextView cityStateZipTV;
    TextView phoneNumTV;
    Context context;
    ArrayList<Object> objectsList;
    TextView getSiteDirectionsLinkTV;
    TextView getProviderDirectionsLinkTV;
    LinearLayout obitViewLL;
    LinearLayout providerViewLL;

    public SearchResultRecyclerAdapter(Context context, ArrayList<Object> objectsList) {
        this.context = context;
        this.objectsList = objectsList;
    }

    /*** Created by melanieh on 4/16/17. */

    public class ResultViewHolder extends RecyclerView.ViewHolder {

        public ResultViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            obitViewLL = (LinearLayout)itemView.findViewById(R.id.obit_object_layout);
            providerViewLL = (LinearLayout)itemView.findViewById(R.id.provider_object_layout);
            personNameTV = (TextView) itemView.findViewById(R.id.person_name);
            dateSpanTV = (TextView) itemView.findViewById(R.id.life_death_date_span);
            obitTextTV = (TextView) itemView.findViewById(R.id.obit_text);
            providerNameTV = (TextView) itemView.findViewById(R.id.provider_name);
            address1TV = (TextView) itemView.findViewById(R.id.address_1);
            address2TV = (TextView) itemView.findViewById(R.id.address_2);
            cityStateZipTV = (TextView) itemView.findViewById(R.id.city_state_zip);
            phoneNumTV = (TextView) itemView.findViewById(R.id.phone_num);
            getSiteDirectionsLinkTV = (TextView) itemView.findViewById(R.id.site_get_directions);
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
        Object currentObject = objectsList.get(position);
        if (currentObject instanceof Obituary) {
            providerViewLL.setVisibility(View.GONE);
            personNameTV.setText(((Obituary) currentObject).getPersonName());
            dateSpanTV.setText(((Obituary) currentObject).getDeathDate());
            obitTextTV.setText(((Obituary) currentObject).getObitText());
            Timber.d("personNameTV: " + ((Obituary) currentObject).getPersonName());
            Timber.d("dateSpanTV: " + ((Obituary) currentObject).getDeathDate());
            Timber.d("obitTextTV: " + ((Obituary) currentObject).getObitText());

        } else {
            obitViewLL.setVisibility(View.GONE);
            providerNameTV.setText(((Provider) currentObject).getProviderName());
            address1TV.setText(((Provider) currentObject).getAddress1());
            address2TV.setText(((Provider) currentObject).getAddress2());
            cityStateZipTV.setText(((Provider) currentObject).getCityStateZip());
            phoneNumTV.setText(((Provider) currentObject).getPhoneNum());
            Timber.d("providerNameTV: " + ((Provider) currentObject).getProviderName());
            Timber.d("address1TV: " + ((Provider) currentObject).getAddress1());
            Timber.d("address2TV: " + ((Provider) currentObject).getAddress2());
            Timber.d("cityStateZipTV: " + ((Provider) currentObject).getCityStateZip());
            Timber.d("phoneNumTV: " + ((Provider) currentObject).getPhoneNum());
        }

        obitViewLL.setContentDescription(String.format(context.getString(R.string.obituary_view_cd),
                personNameTV.getText()));
        providerViewLL.setContentDescription(String.format
                (context.getString(R.string.provider_view_cd), providerNameTV.getText()));
    }

    @Override
    public int getItemCount() {
        if (objectsList != null) {
            return objectsList.size();
        } else {
            return 0;
        }
    }
}
