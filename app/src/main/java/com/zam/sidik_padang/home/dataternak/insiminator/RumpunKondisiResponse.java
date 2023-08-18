package com.zam.sidik_padang.home.dataternak.insiminator;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.BaseApiResponse;

/**
 * Created by supriyadi on 4/27/18.
 */

public class RumpunKondisiResponse extends BaseApiResponse {

    public List<Kondisi> kondisi_ternak = new ArrayList<>();
    public List<Rumpun> rumpun_sapi = new ArrayList<>();

    static class Rumpun {
        public String id_bangsa, rumpun;

        @Override
        public String toString() {
            return rumpun;
        }
    }

    static class Kondisi {
        public String id_kondisi, kondisi_ternak, kondisi_warna;

        @Override
        public String toString() {
            return kondisi_ternak;
        }
    }
}
