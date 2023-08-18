package com.zam.sidik_padang.home.sklb.print.sertifikat.vm;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.zam.sidik_padang.BuildConfig;
import com.zam.sidik_padang.api.ApiService;
import com.zam.sidik_padang.api.BaseResponse;
import com.zam.sidik_padang.api.Injection;
import com.zam.sidik_padang.api.ResponseData;
import com.zam.sidik_padang.home.sklb.setting.kuantitatif.SettingDinas;
import com.zam.sidik_padang.home.sklb.setting.skor.ListKuantitatif;
import com.zam.sidik_padang.home.sklb.setting.skor.ScoreEntity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.zam.sidik_padang.home.sklb.setting.kuantitatif.KuantitatifListFragment.setting_dinas;

public class SertifikatViewModel extends AndroidViewModel {

    private final MutableLiveData<ResponseData<SertifikatResponse>> responseData = new MutableLiveData<>();
    private final MutableLiveData<ResponseData<SertifikatResponse>> responseSettingDinas = new MutableLiveData<>();
    private final MutableLiveData<ResponseData<String>> responseKuantitatif = new MutableLiveData<>();
    private final MutableLiveData<ResponseData<SertifikatResponse>> responseSertifikat = new MutableLiveData<>();
    private final MutableLiveData<ResponseData<String>> responseBitmap = new MutableLiveData<>();
    private final MutableLiveData<ResponseData<ListKuantitatif>> responseSettingSklb = new MutableLiveData<>();

    private Call<SertifikatResponse> callSertifikat;
    private Call<ListKuantitatif> callGetSettingSklb;
    private Call<BaseResponse> callSendSettingSklb;
    private Call<BaseResponse> callSendSettingDinas;

    private final ApiService apiService;
    private Application application;

    public SertifikatViewModel(@NonNull @NotNull Application application) {
        super(application);
        this.application = application;
        apiService = Injection.getInstance().getService();
    }

    public LiveData<ResponseData<SertifikatResponse>> getResponseData() {
        return responseData;
    }

    public LiveData<ResponseData<ListKuantitatif>> getResponseSettingSklb() {
        return responseSettingSklb;
    }

    public LiveData<ResponseData<String>> getResponseKuantitatif() {
        return responseKuantitatif;
    }

    public void getSertifikat(String userid) {
        responseData.setValue(ResponseData.loading("Memuat data..."));
        callSertifikat = apiService.getSertifikat(userid, "1");
        callSertifikat.enqueue(new Callback<SertifikatResponse>() {
            @Override
            public void onResponse(@NonNull Call<SertifikatResponse> call,
                                   @NonNull Response<SertifikatResponse> response) {
                SertifikatResponse dataResponse = response.body();
                if (response.code() == 200) {
                    if (dataResponse != null) {
                        responseData.setValue(ResponseData.success(dataResponse, dataResponse.getMessage(), dataResponse.isSuccess()));
                    }
                } else {
                    responseData.setValue(ResponseData.error("error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SertifikatResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseData.setValue(ResponseData.failure("cancelled"));
                } else {
                    responseData.setValue(ResponseData.failure("failure"));
                }
            }
        });
    }

    public void sendSettingDinas(SettingDinas settingDinas, String userid) {
        settingDinas.setUserId(userid);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(settingDinas);
        Log.d("json", json);
    }

    public void sendKuantitatif(List<ScoreEntity> entityJantan, List<ScoreEntity> entityBetina, String userid) {
        responseKuantitatif.setValue(ResponseData.loading("...."));
        ListKuantitatif listKuantitatif = new ListKuantitatif(userid, entityJantan, entityBetina);
        //Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //String json = gson.toJson(list);
        /*
        int max = 900;
        for(int i = 0; i <= json.length() / max; i++) {
            int start = i * max;
            int end = (i+1) * max;
            end = Math.min(end, json.length());
            Log.v("json", json.substring(start, end));
        }
         */
        callSendSettingSklb = apiService.sendKuantitatifList(listKuantitatif);
        callSendSettingSklb.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                //BaseResponse baseResponse = response.body();
                if (response.code() == 200) {
                    responseKuantitatif.setValue(ResponseData.success("1", "sukses", true));
                    Log.v("Upload", "success");
                } else {
                    responseKuantitatif.setValue(ResponseData.error("error"));
                    Log.v("Upload", "error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
                Log.v("Upload", t.getMessage());
                if (call.isCanceled()) {
                    responseKuantitatif.setValue(ResponseData.error("Upload canceled"));
                } else {
                    responseKuantitatif.setValue(ResponseData.error(t.getMessage()));
                }
            }
        });
    }

    public void getSettingSklb(String userid) {
        responseSettingSklb.setValue(ResponseData.loading("downloading..."));
        callGetSettingSklb = apiService.getSettingSklb();
        callGetSettingSklb.enqueue(new Callback<ListKuantitatif>() {
            @Override
            public void onResponse(@NonNull Call<ListKuantitatif> call, @NonNull Response<ListKuantitatif> response) {
                if (response.code() == 200) {
                    ListKuantitatif listKuantitatif = response.body();
                    responseSettingSklb.setValue(ResponseData.success(listKuantitatif, "sukses", response.isSuccessful()));
                } else {
                    responseSettingSklb.setValue(ResponseData.error("error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ListKuantitatif> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseSettingSklb.setValue(ResponseData.error("cancelled"));
                } else {
                    responseSettingSklb.setValue(ResponseData.error(t.getMessage()));
                }
            }
        });
    }

    /*
    int i = 0;
    private void fowardLoop(){
        if(i => mFileArrayList.size()){
            return; //loop is finished;
        }

        i++;
        WebServiceManager.getInstance().getFrogService().postNotes("HI", "Hello", "Done", new Callback<NotesResponse>() {

            @Override
            public void success(NotesResponse response, retrofit.client.Response response2) {
                System.out.println(response);
                Toast.makeText(AllKPIActivity.this, "Success", Toast.LENGTH_SHORT).show();
                forwardLoop(); // loop continues only if you get the success callback from previous request.
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println(error);
                Toast.makeText(AllKPIActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                // the loop breaks when there is failure callabck.
            }
        });
    }
     */

    public void hapusKuantitatif(String userid) {

    }

    public void sendSertifikat() {

    }

    public MutableLiveData<ResponseData<String>> getResponseBitmap() {
        return responseBitmap;
    }

    public void cancelCall() {
        if (callSertifikat != null) callSertifikat.cancel();
    }

    private File newFile(String fileName) {
        File dir = new File(application.getBaseContext().getFilesDir(), "sklb");
        if(!dir.exists()){
            dir.mkdir();
        }
        return new File(dir, fileName);
    }

    private Uri writeFileSetting(File file, String sBody){
        try {
            FileWriter writer = new FileWriter(file);
            writer.append(sBody);
            writer.flush();
            writer.close();
            return FileProvider.getUriForFile(application.getBaseContext(),
                    "com.zam.sidik_padang.provider",//BuildConfig.APPLICATION_ID + ".provider",
                    file);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}