package com.zam.sidik_padang.roodiskusi.group;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;

import static android.widget.RelativeLayout.ALIGN_PARENT_END;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;
import static android.widget.RelativeLayout.ALIGN_PARENT_START;
import static android.widget.RelativeLayout.END_OF;
import static android.widget.RelativeLayout.LEFT_OF;
import static android.widget.RelativeLayout.RIGHT_OF;
import static android.widget.RelativeLayout.START_OF;


public class MessageViewHolder extends RecyclerView.ViewHolder {

    ImageView imageAccount, imageStatus;
    TextView textNama, textMessage, textTanggal;
    private CardView cardView;

    public MessageViewHolder(View itemView) {
        super(itemView);
        imageAccount = itemView.findViewById(R.id.imageViewAccount);
        textNama = itemView.findViewById(R.id.textViewNama);
        textMessage = itemView.findViewById(R.id.textViewMessage);
        textTanggal = itemView.findViewById(R.id.textViewTanggal);
        cardView = itemView.findViewById(R.id.cardView);
        imageStatus = itemView.findViewById(R.id.imageViewStatus);
    }

    @SuppressLint("InlinedApi")
    void setType(Type type) {
        RelativeLayout.LayoutParams imageParams = (RelativeLayout.LayoutParams) imageAccount.getLayoutParams();
        RelativeLayout.LayoutParams cardParams = (RelativeLayout.LayoutParams) cardView.getLayoutParams();

        if (type == Type.IAM) {
            imageParams.removeRule(ALIGN_PARENT_LEFT);
            imageParams.removeRule(ALIGN_PARENT_START);
            imageParams.addRule(ALIGN_PARENT_END);
            imageParams.addRule(ALIGN_PARENT_RIGHT);
            cardParams.removeRule(END_OF);
            cardParams.removeRule(RIGHT_OF);
            cardParams.addRule(START_OF, R.id.imageViewAccount);
            cardParams.addRule(LEFT_OF, R.id.imageViewAccount);
            cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.getContext(), R.color.primaryLight));
        } else {
            imageParams.removeRule(ALIGN_PARENT_END);
            imageParams.removeRule(ALIGN_PARENT_RIGHT);
            imageParams.addRule(ALIGN_PARENT_START);
            imageParams.addRule(ALIGN_PARENT_LEFT);
            cardParams.removeRule(START_OF);
            cardParams.removeRule(LEFT_OF);
            cardParams.addRule(END_OF, R.id.imageViewAccount);
            cardParams.addRule(RIGHT_OF, R.id.imageViewAccount);
            cardView.setCardBackgroundColor(Color.WHITE);
        }
        itemView.findViewById(R.id.relativeLayout).requestLayout();

    }


    static enum Type {
        IAM,
        OTHER
    }

}
