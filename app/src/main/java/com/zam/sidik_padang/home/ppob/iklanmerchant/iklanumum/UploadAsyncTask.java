package com.zam.sidik_padang.home.ppob.iklanmerchant.iklanumum;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UploadAsyncTask extends AsyncTask<Void, Integer, JsonElement> {

    private String link;
    private String filePath;
    private HashMap<String, String> params;
    private Callback callback;
    private JsonObject defaultResponseJson;

    public UploadAsyncTask(String link, String filePath, HashMap<String, String> params, Callback callback) {
        this.link = link;
        this.filePath = filePath;
        this.params = params;
        this.callback = callback;
        defaultResponseJson = new JsonObject();
        defaultResponseJson.addProperty("success", false);
        defaultResponseJson.addProperty("message", "ERROR 5241");
        execute();
    }


    @Override
    protected JsonElement doInBackground(Void[] p1) {
        debug("upload foto");
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024;
        File sourceFile = new File(filePath);
// open a URL connection to the Servlet
        try {
            FileInputStream fileInputStream = new FileInputStream(sourceFile);

            URL url = new URL(link);

            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("gambar", sourceFile.getName());


            dos = new DataOutputStream(conn.getOutputStream());

            callback.onStart();

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"gambar\";filename=" + sourceFile.getName() + "" + lineEnd);
            dos.writeBytes(lineEnd);

            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();
            final int totalKb = bytesAvailable / 1024;
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            int kb = 0;
            while (bytesRead > 0) {

                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                publishProgress(kb, totalKb);
                kb++;
            }

            // send multipart form data necesssary after file data...

            dos.writeBytes(lineEnd);
            // Upload POST Data
            Iterator<String> keys = params.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = params.get(key);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(value);
                dos.writeBytes(lineEnd);
            }

            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            InputStream is = conn.getInputStream();
            StringBuilder builder = new StringBuilder();
            String line;
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            debug(builder.toString());
            is.close();
            fileInputStream.close();
            dos.flush();
            dos.close();
            // Responses from the server (code and message)
            JsonElement je = (new Gson()).fromJson(builder.toString(), JsonElement.class);
            return je != null ? je : defaultResponseJson;


        } catch (Exception e) {
            defaultResponseJson.addProperty("message", e.getMessage());
            return defaultResponseJson;
        }

    }


    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        Uri.Builder builder = new Uri.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }
        return builder.build().getEncodedQuery();
    }

    @Override
    protected void onProgressUpdate(Integer[] values) {
        if (values != null && values.length == 2) {
            callback.onProgressUpdate(values[0], values[1]);
        }
    }

    @Override
    protected void onPostExecute(JsonElement result) {
        callback.onEnd(result.getAsJsonObject());
    }


    void debug(String s) {
        /*if (BuildConfig.DEBUG)*/
        Log.e(this.getClass().getName(), s);
    }


    public static interface Callback {
        void onStart();

        void onProgressUpdate(int progressKb, int totalKb);

        void onEnd(JsonObject je);
    }
}
