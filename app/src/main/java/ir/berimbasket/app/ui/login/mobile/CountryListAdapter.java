package ir.berimbasket.app.ui.login.mobile;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahmadrosid.svgloader.SvgLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.model.Country;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder> {

    private List<Country> countryList;
    private Activity activity;
    private CountryListListener listener;

    interface CountryListListener {
        void onCountryItemClick(Country country);
    }

    CountryListAdapter(List<Country> CountryList, Activity activity, CountryListListener listener) {
        this.countryList = CountryList;
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Country country = countryList.get(position);
        holder.txtCountryName.setText(country.getName());
        holder.txtCountryName.setText(country.getName());
        holder.txtCountryCode.setText(activity.getString(R.string.country_code_format, "", country.getCode()));
        SvgLoader.pluck()
                .with(activity)
                .setPlaceHolder(R.drawable.ic_language, R.drawable.ic_language)
                .load(country.getImage(), holder.imgCountryImage);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    listener.onCountryItemClick(countryList.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView txtCountryName;
        TextView txtCountryCode;
        CircleImageView imgCountryImage;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            this.imgCountryImage = view.findViewById(R.id.imgCountryImage);
            this.txtCountryCode = view.findViewById(R.id.txtCountryCode);
            this.txtCountryName = view.findViewById(R.id.txtCountryName);
        }
    }
}
