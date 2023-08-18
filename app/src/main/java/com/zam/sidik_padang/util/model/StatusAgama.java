package com.zam.sidik_padang.util.model;

import java.io.Serializable;

/**
 * Created by supriyadi on 10/2/17.
 */

public class StatusAgama implements Serializable {
    public String id, status = "";

    @Override
    public String toString() {
        return status;
    }
}