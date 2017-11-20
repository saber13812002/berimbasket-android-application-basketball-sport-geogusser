package ir.berimbasket.app.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.entity.EntityMission;
import ir.berimbasket.app.network.HttpFunctions;
import ir.berimbasket.app.util.PrefManager;

/**
 * Created by mohammad hosein on 7/22/2017.
 */

public class AdapterMission extends RecyclerView.Adapter<AdapterMission.MissionViewHolder> {

    private final Context context;
    private final ArrayList<EntityMission> missionList;

    public AdapterMission(ArrayList<EntityMission> xpList, Context context) {
        this.missionList = xpList;
        this.context = context;
    }

    @Override
    public MissionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_mission, parent, false);
        MissionViewHolder missionViewHolder = new MissionViewHolder(view);
        return missionViewHolder;
    }

    @Override
    public void onBindViewHolder(final MissionViewHolder holder, final int position) {

        holder.txtMissionTitle.setText(missionList.get(holder.getLayoutPosition()).getTitle());
        holder.txtMissionBadge.setText(String.valueOf(missionList.get(holder.getLayoutPosition()).getLevel()));
        holder.txtMissionScore.setText(String.valueOf(missionList.get(holder.getLayoutPosition()).getScore()) + "  امتیاز ");
        if (missionList.get(holder.getLayoutPosition()).getIsLock() != 0) {
            holder.imgMissionLock.setVisibility(View.VISIBLE);
            holder.txtMissionScore.setVisibility(View.GONE);

        } else {
            holder.imgMissionLock.setVisibility(View.INVISIBLE);
            holder.txtMissionScore.setVisibility(View.VISIBLE);
        }

        holder.cardMissionItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (missionList.get(holder.getLayoutPosition()).getIsLock() == 0)
                new RegisterMissionToUser(new AsyncResponse() {

                    @Override
                    public void processFinish(String output) {
                        if (!output.equals("")) {
                            // TODO: 04/10/2017 Uncomment this part of code when isDone in missionDoneUrl added
//                            missionList.get(holder.getLayoutPosition()).setIsLock(1);
//                            holder.imgMissionLock.setVisibility(View.VISIBLE);
//                            holder.txtMissionScore.setVisibility(View.INVISIBLE);
//                            notifyItemChanged(holder.getLayoutPosition());
                        }
                    }
                }).execute(missionList.get(holder.getLayoutPosition()));
            }
        });
    }


    @Override
    public int getItemCount() {
        return missionList.size();
    }

    class MissionViewHolder extends RecyclerView.ViewHolder {

        TextView txtMissionTitle, txtMissionBadge, txtMissionScore;
        ImageView imgMissionLock;
        CardView cardMissionItem;

        MissionViewHolder(View itemView) {
            super(itemView);
            this.txtMissionTitle = (TextView) itemView.findViewById(R.id.txtMissionTitle);
            this.txtMissionBadge = (TextView) itemView.findViewById(R.id.txtMissionBadge);
            this.txtMissionScore = (TextView) itemView.findViewById(R.id.txtMissionScore);
            this.imgMissionLock = (ImageView) itemView.findViewById(R.id.imgMissionLock);
            cardMissionItem = (CardView) itemView.findViewById(R.id.cardMissionItem);

        }
    }


    private final String MISSION_DONE_URL = "berimbasket.ir/bball/www/setMissionDone.php?";

    private class RegisterMissionToUser extends AsyncTask<EntityMission, Void, String> {

        @Override
        protected String doInBackground(EntityMission... entityMissions) {
            EntityMission entityMission = entityMissions[0];
            HttpFunctions httpFunctions = new HttpFunctions(HttpFunctions.RequestType.GET);
            String jsonStr = httpFunctions.makeServiceCall(completeMissionDoneUrl(entityMission.getId()));

            // FIXME: 04/10/2017 Json String Doesn't return anything, call server admin to fix it
//            if (jsonStr != null) {
//                try {
//                    JSONArray missionDoneResult = new JSONArray(jsonStr);
            if (true) {
                return entityMission.getLink();
            } else {
                return "";
            }
//                } catch (final JSONException e) {
//                    Log.i("jsonError", e.toString());
//                }
//            }
//            return "";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!result.equals("")) {
                setMissionDone(result);
            } else {
                Toast.makeText(context, "متاسفانه عملیات ثبت ماموریت ناموفق بود، لطفا در زمان دیگری امتحان کنید", Toast.LENGTH_LONG).show();
            }
            delegate.processFinish(result);
        }

        private String completeMissionDoneUrl(int missionId) {
            PrefManager pref = new PrefManager(context);
            String pusheId = Pushe.getPusheId(context);
            String userName = pref.getUserName();
            return MISSION_DONE_URL + "id=" + missionId + "&token=asf" + "&user=" + userName + "&pusheid=" + pusheId + "&username=" + userName;
        }

        private void setMissionDone(String missionLink) {
            Uri missionUri = Uri.parse(missionLink);
            CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
            intentBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
            intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            CustomTabsIntent customTabsIntent = intentBuilder.build();
            customTabsIntent.launchUrl(context, missionUri);
        }


        AsyncResponse delegate = null;

        RegisterMissionToUser(AsyncResponse delegate) {
            this.delegate = delegate;
        }

    }

    interface AsyncResponse {
        void processFinish(String output);
    }


}
