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

            personNameTV = (TextView) itemView.findViewById(R.id.person_name);
            dateSpanTV = (TextView) itemView.findViewById(R.id.life_death_date_span);
            obitTextTV = (TextView) itemView.findViewById(R.id.obit_text);
            providerNameTV = (TextView) itemView.findViewById(R.id.provider_name);
            address1TV = (TextView) itemView.findViewById(R.id.address_1);
            address2TV = (TextView) itemView.findViewById(R.id.address_2);
            cityStateZipTV = (TextView) itemView.findViewById(R.id.city_state_zip);
            phoneNumTV = (TextView) itemView.findViewById(R.id.phone_num);
        }
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedview = LayoutInflater.from(context).inflate(R.layout.search_result_list_item, parent, false);
        return new ResultViewHolder(inflatedview);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        Object currentObject = objectsList.get(position);
        if (currentObject instanceof Obituary) {
            personNameTV.setText(((Obituary) currentObject).getPersonName());
            dateSpanTV.setText(((Obituary) currentObject).getBirthDate());
            obitTextTV.setText(((Obituary) currentObject).getObitText());
        } else {
            providerNameTV.setText(((Provider) currentObject).getProviderName());
            address1TV.setText(((Provider) currentObject).getAddress1());
            address2TV.setText(((Provider) currentObject).getAddress2());
            cityStateZipTV.setText(((Provider) currentObject).getCityStateZip());
            phoneNumTV.setText(((Provider) currentObject).getPhoneNum());

        }

//        obitViewLL.setContentDescription(String.format(context.getString(R.string.obituary_view_cd),
//                personNameTV.getText()));
//        providerViewLL.setContentDescription(String.format
//                (context.getString(R.string.provider_view_cd), providerNameTV.getText()));
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
