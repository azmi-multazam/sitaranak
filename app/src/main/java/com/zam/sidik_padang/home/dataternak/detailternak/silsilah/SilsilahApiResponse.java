package com.zam.sidik_padang.home.dataternak.detailternak.silsilah;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.BaseApiResponse;

public class SilsilahApiResponse extends BaseApiResponse {

    public List<Generasi1> generasi1 = new ArrayList<>();
    public List<Generasi2> generasi2 = new ArrayList<>();

    public static class Generasi1 {
        public String induk_kiri1, earteg_indukkiri1,
                bapak_kanan1, earteg_bapakkanan1, generasi;
    }

    public class Generasi2 {
        public String induk_kiri2, earteg_indukkiri2,
                bapak_kiri2, earteg_bapakkiri2,
                induk_kanan2, earteg_indukkanan2,
                bapak_kanan2, earteg_bapakkanan2, generasi;
    }
}
