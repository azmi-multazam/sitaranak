package com.zam.sidik_padang.home.sklb.setting.skor;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListKuantitatif {

    @SerializedName("userid")
    public String userid;

    @SerializedName("jantan")
    public List<ScoreEntity> entityListJantan;

    @SerializedName("betina")
    public List<ScoreEntity> entityListBetina;

    public ListKuantitatif(String userid, List<ScoreEntity> entityListJantan, List<ScoreEntity> entityListBetina) {
        this.userid = userid;
        this.entityListJantan = entityListJantan;
        this.entityListBetina = entityListBetina;
    }
}
