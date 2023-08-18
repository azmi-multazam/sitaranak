package com.zam.sidik_padang.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.zam.sidik_padang.base.BasePresenter;

public class PermissionPresenter extends BasePresenter<PermissionContract.View>
        implements PermissionContract.Presenter {

    private final Context context;

    public PermissionPresenter(Context context, PermissionContract.View view) {
        this.context = context;
        this.view = view;
    }

    /*
    @Override
    public void checkPermission(Context context, String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                view.onShowDialogRationale(requestCode);
            }
            view.onPermissionDenied(requestCode);
        } else {
            view.onPermissionGranted(requestCode);
        }
    }*/

    @Override
    public void requestPermission(String[] permission, int requestCode) {
        ActivityCompat.requestPermissions((Activity) context, permission, requestCode);
    }

    @Override
    public boolean grantedPermission(String permission) {
        if (view.needPermission()) {
            return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }
}