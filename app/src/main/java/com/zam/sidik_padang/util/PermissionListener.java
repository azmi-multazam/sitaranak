package com.zam.sidik_padang.util;

/**
 * Created by AbangAzmi on 22/03/2018.
 */

public interface PermissionListener {
    void onPermissionAccepted();

    void onPermissionRejected(String[] arrString);
}
