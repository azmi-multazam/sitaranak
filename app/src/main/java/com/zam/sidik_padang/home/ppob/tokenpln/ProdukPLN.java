package com.zam.sidik_padang.home.ppob.tokenpln;

import java.text.NumberFormat;

public class ProdukPLN {
    public String id = "",
            codetrx = "",
            description = "";
    public double jual = 0;

    @Override
    public String toString() {
        return description + "\n(Rp." + NumberFormat.getNumberInstance().format(jual) + ")";
    }


}

/*
	{"produk_pilihan":[{"id":"2",
						"codetrx":"PLN20",
						"description":"Voucher PLN 20000",
						"jual":"21190"},
						
						{"id":"3","codetrx":"PLN50","description":"Voucher PLN 50000","jual":"51190"},{"id":"4","codetrx":"PLN100","description":"Voucher PLN 100000","jual":"101190"},{"id":"5","codetrx":"PLN200","description":"Voucher PLN 200000","jual":"201190"},{"id":"6","codetrx":"PLN500","description":"Voucher PLN 500000","jual":"501190"},{"id":"7","codetrx":"PLN1000","description":"Voucher PLN 1000000","jual":"1001190"}],"success":true,"message":"berhasil"}
*/
