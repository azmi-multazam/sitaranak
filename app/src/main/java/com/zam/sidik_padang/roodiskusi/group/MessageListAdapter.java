package com.zam.sidik_padang.roodiskusi.group;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
//import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.User;
import com.zam.sidik_padang.util.Util;


public class MessageListAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private List<Message> messageList;
    private User im;
    private SimpleDateFormat dateFormat;
    private Drawable sendingDrawable, sentDrawable, readDrawable, errorDrawable;
    private int colorPrimary = -1;
    //private DocumentReference groupReference;

    /*
    public MessageListAdapter(List<Message> messageList, DocumentReference groupReference) {
        this.groupReference = groupReference;
        this.messageList = messageList;
        dateFormat = new SimpleDateFormat("dd-MM-yyyy, HH:mm", Locale.getDefault());
    }
     */

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (im == null) im = Util.getSavedUser(parent.getContext());
        if (sendingDrawable == null) {
            Resources res = parent.getResources();
            sendingDrawable = ResourcesCompat.getDrawable(res, R.drawable.ic_schedule, null);
            int b = (int) (parent.getResources().getDisplayMetrics().density * 16f);
            sendingDrawable.setBounds(0, 0, b, b);
            sentDrawable = ResourcesCompat.getDrawable(res, R.drawable.ic_done, null);
            readDrawable = ResourcesCompat.getDrawable(res, R.drawable.ic_done_all_green, null);
            errorDrawable = ResourcesCompat.getDrawable(res, R.drawable.ic_error, null);
        }
        if (colorPrimary == -1)
            colorPrimary = ContextCompat.getColor(parent.getContext(), R.color.colorPrimary);
        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        final Message message = messageList.get(position);
        holder.setType(message.isMine(im) ? MessageViewHolder.Type.IAM : MessageViewHolder.Type.OTHER);

        holder.textTanggal.setText(dateFormat.format(message.timeStamp));
        holder.textMessage.setText(message.message);
        holder.textNama.setText(message.owner.get("nama"));

        if (im.userid.equals(message.owner.get("userid"))) {
            if (message.status == Message.Status.SENDINGG)
                holder.imageStatus.setImageDrawable(sendingDrawable);
            else if (message.status == Message.Status.SENT)
                holder.imageStatus.setImageDrawable(sentDrawable);
            else if (message.status == Message.Status.READ)
                holder.imageStatus.setImageDrawable(readDrawable);
            else if (message.status == Message.Status.FAILED)
                holder.imageStatus.setImageDrawable(errorDrawable);
            else holder.imageStatus.setImageDrawable(null);
        } else {
            holder.imageStatus.setImageDrawable(null);
            //if (message.status != Message.Status.READ)
                //groupReference.collection(Message.MESSAGES).document(message.id).update("status", Message.Status.READ);
        }

        String foto = message.owner.get("foto");
        if (foto != null && foto.contains("/"))
            Glide.with(holder.imageAccount.getContext()).load(foto).into(holder.imageAccount);


    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }

}
