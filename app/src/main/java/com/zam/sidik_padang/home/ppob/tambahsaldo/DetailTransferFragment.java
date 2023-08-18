package com.zam.sidik_padang.home.ppob.tambahsaldo;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zam.sidik_padang.R;


/**
 * Created by supriyadi on 8/11/17.
 */

public class DetailTransferFragment extends Fragment {
    private String text;

    public DetailTransferFragment() {
    }

    public static DetailTransferFragment getInstance(String text) {
        DetailTransferFragment fragment = new DetailTransferFragment();
        Bundle bundle = new Bundle();
        bundle.putString("text", text);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("text")) text = bundle.getString("text");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (text != null) outState.putString("text", text);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("text"))
            text = savedInstanceState.getString("text");
        super.onViewStateRestored(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_transfer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spanned spannedText;
        spannedText = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        ((TextView) view.findViewById(R.id.fragment_detail_transfer_TextView)).setText(spannedText);
    }
}
