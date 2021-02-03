package com.superfuns.healthcode.network.intercepter;


import com.superfuns.healthcode.Constants.HttpApi;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class MoreBaseUrlInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        List<String> urlNameList = request.headers("urlName");

        if (urlNameList.size() > 0) {

            HttpUrl oldUrl = request.url();

            Request.Builder builder = request.newBuilder();

            String urlname = urlNameList.get(0);

            // 删除原有的值，key 和 value
            builder.removeHeader("urlName");

            HttpUrl baseURL = null;
            if ("version".equals(urlname)) {
                baseURL = HttpUrl.parse(HttpApi.URL_Version);
            }

            //重建新的HttpUrl，需要重新设置的url部分
            HttpUrl newHttpUrl = oldUrl.newBuilder()
                    .scheme(baseURL.scheme())//http协议如：http或者https
                    .host(baseURL.host())//主机地址
                    .port(baseURL.port())//端口
                    .build();

            //获取处理后的新newRequest
            Request newRequest = builder.url(newHttpUrl).build();

            return chain.proceed(newRequest);
        } else {
            return chain.proceed(request);
        }
    }


}
