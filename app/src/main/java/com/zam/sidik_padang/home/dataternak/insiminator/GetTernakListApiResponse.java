package com.zam.sidik_padang.home.dataternak.insiminator;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.BaseApiResponse;

/**
 * Created by supriyadi on 4/17/18.
 */

public class GetTernakListApiResponse extends BaseApiResponse {
    public int total_sapi = 0;
    public List<TernakIb> data_ternak = new ArrayList<>();
}
