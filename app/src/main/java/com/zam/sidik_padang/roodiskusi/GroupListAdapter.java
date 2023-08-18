package com.zam.sidik_padang.roodiskusi;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.User;
import com.zam.sidik_padang.util.Util;

public class GroupListAdapter extends RecyclerView.Adapter<GroupViewHolder> {

    private List<Group> groupList;
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener longClickListener;
    private User imUser;
    private SimpleDateFormat fullDateFormat, hourFormat;
    private Calendar diniHari, tengahMalam;

    public GroupListAdapter(List<Group> groupList, OnItemClickListener itemClickListener, OnItemLongClickListener longClickListener) {
        this.groupList = groupList;
        this.itemClickListener = itemClickListener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if (imUser == null) {
            imUser = Util.getSavedUser(context);
            fullDateFormat = new SimpleDateFormat("d/M/yy H:mm", Locale.getDefault());
            hourFormat = new SimpleDateFormat("H:mm", Locale.getDefault());
            diniHari = Calendar.getInstance();
            diniHari.set(Calendar.HOUR_OF_DAY, 0);
            diniHari.set(Calendar.MINUTE, 0);
            diniHari.set(Calendar.SECOND, 0);
            diniHari.set(Calendar.MILLISECOND, 0);
            tengahMalam = Calendar.getInstance();
            tengahMalam.set(Calendar.HOUR_OF_DAY, 23);
            tengahMalam.set(Calendar.MINUTE, 59);
            tengahMalam.set(Calendar.SECOND, 59);
            tengahMalam.set(Calendar.MILLISECOND, 99);

        }
        return new GroupViewHolder(LayoutInflater.from(context).inflate(R.layout.item_group, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupViewHolder holder, int position) {
        final Group group = groupList.get(position);
        holder.viewSelectionIndicator.setVisibility(group.selected ? View.VISIBLE : View.INVISIBLE);
        String judul = group.name;
        if (judul == null || judul.isEmpty()) {
            judul = "";
            Set<String> keySet = group.users.keySet();
            StringBuilder judulBuilder = new StringBuilder(judul);
            int total = 0;
            for (String s : keySet) {
                if (s.equals(imUser.userid)) continue;
                total++;
                Map<String, Object> userMap = (Map<String, Object>) group.users.get(s);
                if (judulBuilder.toString().equals(""))
                    judulBuilder.append(userMap.get("name"));
                else judulBuilder.append(", ").append(userMap.get("name"));
            }

            judul = judulBuilder.toString();
            if (total > 1) {
                judul = "Anda, " + judul;
            }
        }
        holder.textName.setText(judul);
        if (group.lastMessage != null) {
            Object sender = imUser.userid.equals(group.lastMessage.get("userid")) ? "Anda" : group.lastMessage.get("nama");
            holder.textLastMessage.setText(sender + ": " + group.lastMessage.get("message"));
            try {
                Object o = group.lastMessage.get("timestamp");
                if (o != null && o instanceof Long) {
                    long timeStamp = (long) o;
                    if (timeStamp > 0) {
                        if (timeStamp > diniHari.getTimeInMillis() && timeStamp < tengahMalam.getTimeInMillis())
                            holder.textTanggal.setText(hourFormat.format(timeStamp));
                        else holder.textTanggal.setText(fullDateFormat.format(timeStamp));
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(getClass().getName(), e.getMessage());
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(group, holder.getAdapterPosition());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                longClickListener.onItemLongClick(group, holder.getAdapterPosition());
                return true;
            }
        });

        holder.textUnread.setVisibility(View.INVISIBLE);
        String foto = "";
        int total = 0;
        for (String userid : group.users.keySet()) {
            Map<String, Object> map = (Map<String, Object>) group.users.get(userid);
            if (userid.equals(imUser.userid)) {
                long unread = map.get("unread") == null ? 0 : (long) map.get("unread");
                if (unread > 0) {
                    holder.textUnread.setText("" + unread);
                    holder.textUnread.setVisibility(View.VISIBLE);
                }
            } else {
                foto = (String) map.get("foto");
            }
            total++;
        }

        Drawable drawable = ContextCompat.getDrawable(holder.imageFoto.getContext(), R.drawable.ic_group);
        if (total == 2 && foto != null && !foto.isEmpty() && foto.contains("/")) {
            Glide.with(holder.imageFoto.getContext()).load(foto).placeholder(drawable).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageFoto);
            holder.imageFoto.setPadding(0, 0, 0, 0);
        } else {
            int p = (int) (holder.imageFoto.getResources().getDisplayMetrics().density * 5F);
            holder.imageFoto.setPadding(p, p, p, p);
            holder.imageFoto.setImageDrawable(drawable);
        }


    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }


}
