package com.example.geoworkerv3.classes;

import com.squareup.okhttp.MediaType;
import android.content.Context;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.util.concurrent.TimeUnit;

public class GeoWorkerTransport {
    String base = "http://192.168.0.175:2193";
    String fetchUrl;
    String pushUrl;

    int timesCompleted;
    String identifier;

    Context context;

    boolean isConnected;
    int currentDataSize;

    public GeoWorkerTransport (Context _context, String _identifier) {
        this.context = _context;

        this.fetchUrl = this.base + "/fetch";
        this.pushUrl = this.base + "/push";
        this.timesCompleted = 0;
        this.identifier = _identifier;

        this.download();
    }

    void download () {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1000 * 30, TimeUnit.MILLISECONDS);
        Request request = new Request.Builder()
                .url(this.fetchUrl)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            GeoWorker geoWorker = new GeoWorker(this.context, response.body().string(), this.identifier);

            this.isConnected = geoWorker.importedAddresses.getConnected();
            this.currentDataSize = geoWorker.importedAddresses.getItems().size();
            System.out.println(this.identifier + " - Is connected: " + this.isConnected);
            System.out.println(this.identifier + " - items length: " + this.currentDataSize);

            if (this.currentDataSize == 0) {
                if (this.isConnected){
                    System.out.println(this.identifier + " - Reload App");
                    Thread.sleep(1000 * 1);
                    this.download();
                }
                else return;
            } else {
                System.out.println(this.identifier + " - MULTI CODE");
                this.upload(geoWorker.multiGeocode());
                // geoWorker.multiGeocode().then((data) => this.upload(data));
            }

        } catch (Exception ex) {
            System.out.println(this.identifier + " - ERROR DOWNLOADING");
            System.out.println(ex.toString());
        }

    }

    void upload (String data) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1000 * 30, TimeUnit.MILLISECONDS);
        client.setWriteTimeout(1000 * 30, TimeUnit.MILLISECONDS);
        client.setReadTimeout(1000 * 30, TimeUnit.MILLISECONDS);

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, data);
        Request request = new Request.Builder()
                .url(this.pushUrl)
                .header("Content-Type", "application/json")
                .post(body)
                .build();


        try {
            Response response = client.newCall(request).execute();
            System.out.println(this.identifier + " - SEND");
            System.out.println(this.identifier + " - Response status: " + response.code());
            System.out.println(this.identifier + " - Response body: " + response.body());
            this.timesCompleted++;
            this.download();
        } catch (Exception ex) {
            System.out.println(this.identifier + " - ERROR UPLOADING");
            System.out.println(ex.toString());
        }

    }
}
