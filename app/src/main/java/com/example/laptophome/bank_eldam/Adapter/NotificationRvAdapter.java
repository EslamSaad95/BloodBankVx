package com.example.laptophome.bank_eldam.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.data.model.notificationlist.NotificationData;
import com.example.laptophome.bank_eldam.data.rest.ApiService;
import com.example.laptophome.bank_eldam.data.rest.RetrofitClient;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationRvAdapter extends RecyclerView.Adapter<NotificationRvAdapter.Viewholder> {

    private Context context;
    private List<NotificationData> notificationlist;

    public NotificationRvAdapter(Context context, List<NotificationData> notificationlist) {
        this.context = context;
        this.notificationlist = notificationlist;
    }

    ApiService apiService;

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification_recycleview, viewGroup, false);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        return new Viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {

        setData(viewholder, i);
        setAction(viewholder, i);
    }

    private void setData(Viewholder viewholder, int i)  {
        viewholder.notficationRvTvTitle.setText(notificationlist.get(i).getTitle());


            String datestring=notificationlist.get(i).getCreatedAt();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(datestring);

                    viewholder.notficationRvTvDate.setText(sdf.format(date));

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
            String dayName=simpleDateFormat.format(date);
            viewholder.notficationRvTvDay.setText(dayName);

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void setAction(Viewholder viewholder, int i) {
    }

    @Override
    public int getItemCount() {
        return notificationlist.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {
        private View view;
        @BindView(R.id.notfication_rv_icon)
        ImageView notficationRvIcon;
        @BindView(R.id.notfication_rv_tv_title)
        TextView notficationRvTvTitle;
        @BindView(R.id.notfication_rv_tv_date)
        TextView notficationRvTvDate;
        @BindView(R.id.notfication_rv_tv_day)
        TextView notficationRvTvDay;
        @BindView(R.id.notfication_rv_tv_dateE)
        TextView notficationRvTvDateE;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
