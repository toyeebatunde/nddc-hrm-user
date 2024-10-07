package co.payrail.attendance_srv.integration.dojah.service;

import co.payrail.attendance_srv.integration.dojah.prop.DojahProp;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Service
public class DojahApiService {
    private final DojahProp dojahProp;
    private final OkHttpClient client;

    public DojahApiService(DojahProp dojahProp) {
        this.dojahProp = dojahProp;
//        client = null;
        client = new OkHttpClient.Builder()
//                .readTimeout(30, Duration.ZERO)
                .addInterceptor(new DojahInteceptor())
                .build();
    }

    public String post(String url, String json) throws IOException {
        RequestBody body = getRequestBody(json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String res = response.body().string();
            response.close();
            return res ;
        }
    }

    public String put(String url, String json) throws IOException {
        RequestBody body = getRequestBody(json);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String res = response.body().string();
            response.close();
            return res ;
        }
    }

    public String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            String res = response.body().string();
            response.close();
            return res ;
        }
    }

    private RequestBody getRequestBody(String json) {
        return RequestBody.create(MediaType.get("application/json; charset=utf-8"), json);
    }

    private class DojahInteceptor implements Interceptor {
        @SneakyThrows
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();

            //Build new request
            Request.Builder builder = request.newBuilder();
            String private_key = dojahProp.getPrivateKey(); //save token of this request for future
            String appId = dojahProp.getAppId(); //save token of this request for future
            setAuthHeader(builder, private_key,appId); //write current token to request
            request = builder.build();
            return chain.proceed(request); //repeat request with new token
        }

        private void setAuthHeader(Request.Builder builder, String private_key, String appId) {
            builder.header("Authorization",private_key);
            builder.header("AppId", appId);
            builder.header("Accept", " application/json");
            builder.header("Content-Type", "application/json");
        }
    }
}
