package com.zam.sidik_padang;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;
import androidx.annotation.NonNull;

import com.zam.sidik_padang.util.PermissionListener;

import java.util.ArrayList;
import java.util.Arrays;


public class BaseActivity extends AppCompatActivity {
    public boolean isStoped = true;
    protected boolean isResummed = false;
    protected SharedPreferences sharedPreferences;

    private final int BLUETOOTH_PERMISSION_CODE_Q = 1234554;
    private final ArrayList<String> permisions_q = new ArrayList<>();
    private ArrayList<String> permissionsToRequest_q = new ArrayList<>();
    private final ArrayList<String> permissionsAccepted_q = new ArrayList<>();
    private ArrayList<String> permissionsRejected_q = new ArrayList<>();
    private final int BLUETOOTH_PERMISSION_CODE_S = 1234555;
    private final ArrayList<String> permisions_s = new ArrayList<>();
    private ArrayList<String> permissionsToRequest_s = new ArrayList<>();
    private final ArrayList<String> permissionsAccepted_s = new ArrayList<>();
    private ArrayList<String> permissionsRejected_s = new ArrayList<>();


    private final int ALL_PERMISSION_CODE = 1234556;
    private ArrayList<String> permissionsToRequest;
    private final ArrayList<String> permissionsRejected = new ArrayList<>();
    private final ArrayList<String> permissionsAccepted = new ArrayList<>();
    private final ArrayList<String> permissions = new ArrayList<>();
    private final PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionAccepted() {
            Log.d("permission", "ACEEPTED");
        }

        @Override
        public void onPermissionRejected(String[] arrString) {
            Log.d("rejected", Arrays.toString(arrString));
            //Toast.makeText(BaseActivity.this, "Aplikasi tidak berjalan sempurna tanpa perizinan", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        requestStoragePermission();
    }

    protected void requestStoragePermission() {
        permissions.add(Manifest.permission.BLUETOOTH);
        permissions.add(Manifest.permission.BLUETOOTH_ADMIN);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.CAMERA);

        permissionsToRequest = findUnAskedPermissions(permissions);
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
            }, ALL_PERMISSION_CODE);
        }

        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permisions_q.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
            permissionsToRequest_q = findUnAskedPermissions(permisions_q);

            if (permissionsToRequest_q.size() > 0) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                }, BLUETOOTH_PERMISSION_CODE_Q);
            }
        }
         */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permisions_s.add(Manifest.permission.BLUETOOTH_CONNECT);
            permisions_s.add(Manifest.permission.BLUETOOTH_SCAN);
            permissionsToRequest_s = findUnAskedPermissions(permisions_s);

            if (permissionsToRequest_s.size() > 0) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN
                }, BLUETOOTH_PERMISSION_CODE_S);
            }
        }

    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();
        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ALL_PERMISSION_CODE) {
            for (String perms : permissionsToRequest) {
                if (hasPermission(perms)) {
                    permissionsAccepted.add(perms);
                } else {
                    permissionsRejected.add(perms);
                }
            }
            if (permissionsAccepted.size() < permissionsToRequest.size()) {
                String[] arrString = permissionsRejected.toArray(new String[0]);
                permissionListener.onPermissionRejected(arrString);
            } else {
                permissionListener.onPermissionAccepted();
            }

            if (permissionsRejected.size() > 0) {
                if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                    showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                            (dialog, which) -> {
                                //Log.d("API123", "permisionrejected " + permissionsRejected.size());
                                String[] arrString = permissionsRejected.toArray(new String[0]);
                                requestPermissions(arrString, ALL_PERMISSION_CODE);
                                permissionListener.onPermissionRejected(arrString);
                            });
                }
            }
            /*
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("PERMISSIONS", "GRANT ALL");
            } else {
                Log.d("PERMISSIONS", grantResults.length + "NOT GRANT");
            }
             */
        } else if (requestCode == BLUETOOTH_PERMISSION_CODE_Q) {
            managePermisiion(permissionsToRequest_q, permissionsAccepted_q, permissionsRejected_q, BLUETOOTH_PERMISSION_CODE_Q);
        } else if (requestCode == BLUETOOTH_PERMISSION_CODE_S) {
            managePermisiion(permissionsToRequest_s, permissionsAccepted_s, permissionsRejected_s, BLUETOOTH_PERMISSION_CODE_S);
        }
    }

    private void managePermisiion(ArrayList<String> toRequest, ArrayList<String> accepted, ArrayList<String> rejected, int CODE) {
        for (String perms : toRequest) {
            if (hasPermission(perms)) {
                accepted.add(perms);
            } else {
                rejected.add(perms);
            }
        }
        if (accepted.size() < toRequest.size()) {
            String[] arrString = rejected.toArray(new String[0]);
            permissionListener.onPermissionRejected(arrString);
        } else {
            permissionListener.onPermissionAccepted();
        }

        if (rejected.size() > 0) {
            if (shouldShowRequestPermissionRationale(rejected.get(0))) {
                showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                        (dialog, which) -> {
                            String[] arrString = rejected.toArray(new String[0]);
                            requestPermissions(arrString, CODE);
                            permissionListener.onPermissionRejected(arrString);
                        });
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onStart() {
        // TODO: Implement this method
        super.onStart();
        isStoped = false;
    }

    @Override
    protected void onStop() {
        isStoped = true;
        super.onStop();
    }

    @Override
    protected void onResume() {
        // TODO: Implement this method
        super.onResume();
        isResummed = true;
    }

    @Override
    protected void onPause() {
        isResummed = false;
        super.onPause();
    }


    protected void debug(Class<?> cls, String msgs) {
        Log.d(cls.getName(), msgs);
        //if (Config.DEBUG) Log.e(cls.getName(), msgs);
    }
}
