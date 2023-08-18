package com.zam.sidik_padang.home.ppob.isipulsa;

import java.text.NumberFormat;

public class ProdukPilihan {
    public String id = "", codetrx = "", description = "", jual = "";

    @Override
    public String toString() {
        if (jual.isEmpty()) return "--pilih produk--";
        return description + " (Rp." + NumberFormat.getNumberInstance().format(Double.parseDouble(jual)) + ")";
    }


}

/*
{"produk_pilihan":[{"id":"8",
					"codetrx":"S5",
					"description":"TELKOMSEL 5000",
					"jual":5950},
					
					{"id":"9","codetrx":"S10","description":"TELKOMSEL 10000","jual":10595},{"id":"10","codetrx":"S20","description":"TELKOMSEL 20000","jual":20325},{"id":"11","codetrx":"S25","description":"TELKOMSEL 25000","jual":25175},{"id":"12","codetrx":"S50","description":"TELKOMSEL 50000","jual":49175},{"id":"13","codetrx":"S100","description":"TELKOMSEL 100000","jual":97500},{"id":"14","codetrx":"S150","description":"TELKOMSEL 150000","jual":151400},{"id":"15","codetrx":"S200","description":"TELKOMSEL 200000","jual":201400},{"id":"16","codetrx":"S300","description":"TELKOMSEL 300000","jual":304400}],"success":true,"message":"berhasil"}
*/
