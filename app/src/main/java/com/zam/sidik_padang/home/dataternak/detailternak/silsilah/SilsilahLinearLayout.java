package com.zam.sidik_padang.home.dataternak.detailternak.silsilah;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.zam.sidik_padang.R;

public class SilsilahLinearLayout extends LinearLayoutCompat {

    private TextView textTitle, textId, textEartag;
    private ImageView imageView;
    public String generasi = "";

    public SilsilahLinearLayout(Context context) {
        super(context);
    }

    public SilsilahLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SilsilahLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        textTitle = findViewById(R.id.textViewTitle);
        textId = findViewById(R.id.textViewIdTernak);
        textEartag = findViewById(R.id.textViewEarTag);
        imageView = findViewById(R.id.imageView);
    }

    public void setText(String title, String idternak, String eartag) {
        textTitle.setText(title);
        textId.setText(idternak);
        textEartag.setText(eartag);
    }

    public String getIdTernak() {
        return textId.getText().toString().trim();
    }

    public String getEartag() {
        return textEartag.getText().toString().trim();
    }

    public void setTextIdClickListener(OnClickListener clickListener) {
        textId.setOnClickListener(clickListener);
    }

}
