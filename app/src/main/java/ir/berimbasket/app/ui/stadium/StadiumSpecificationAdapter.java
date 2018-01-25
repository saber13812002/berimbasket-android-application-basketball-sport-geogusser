package ir.berimbasket.app.ui.stadium;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ir.berimbasket.app.R;

/**
 * Created by mohammad hosein on 7/21/2017.
 */

public class StadiumSpecificationAdapter extends RecyclerView.Adapter<StadiumSpecificationAdapter.StadiumSpecViewHolder> {

    private final Context context;
    private final ArrayList<String> stadiumSpecListValue, stadiumSpecListKey;

    public StadiumSpecificationAdapter(ArrayList<String> playerListValue, ArrayList<String> stadiumSpecListKey, Context context) {
        this.stadiumSpecListValue = playerListValue;
        this.stadiumSpecListKey = stadiumSpecListKey;
        this.context = context;
    }

    @Override
    public StadiumSpecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_player_specification, parent, false);
        return new StadiumSpecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StadiumSpecViewHolder holder, int position) {
        holder.setData(position);
    }


    @Override
    public int getItemCount() {
        return stadiumSpecListValue.size();
    }

    class StadiumSpecViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtSpecValue, txtSpecKey;

        StadiumSpecViewHolder(View itemView) {
            super(itemView);
            this.txtSpecValue = itemView.findViewById(R.id.txtSpecValue);
            this.txtSpecKey = itemView.findViewById(R.id.txtSpecKey);
        }

        public void setData(int position) {
            txtSpecValue.setText(stadiumSpecListValue.get(position));
            txtSpecKey.setText(stadiumSpecListKey.get(position));
        }

        @Override
        public void onClick(View view) {
        }
    }
}
