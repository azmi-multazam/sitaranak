package com.zam.sidik_padang.util.data;

public class ProdukPulsa extends Object {
    public static final String ID = "id", CODE = "code", PRODUK = "produk";
    public String id = "", code = "", produk = "";

    public ProdukPulsa() {
    }

    public ProdukPulsa(String p) {
        this.produk = p;
    }

    @Override
    public String toString() {
        // TODO: Implement this method
        return produk;
    }


}
/*
{"produk_pulsa":[{
	"id":"2",
	"code":"S",
	"produk":"TELKOMSEL"},
	
	{"id":"3","code":"AX","produk":"AXIS"},{"id":"4","code":"AXD","produk":"AXIS INTERNET"},{"id":"6","code":"C","produk":"CERIA"},{"id":"7","code":"N","produk":"FREN"},{"id":"8","code":"I","produk":"INDOSAT"},{"id":"12","code":"IS","produk":" INDOSAT SMS"},{"id":"13","code":"T","produk":"THREE"},{"id":"15","code":"X","produk":"XL "},{"id":"19","code":"TS","produk":"TELKOMSEL SMS"},{"id":"21","code":"SM","produk":"SMARTFREN"}],"success":true,"message":"berhasil"}
*/

