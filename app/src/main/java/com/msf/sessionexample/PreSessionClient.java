package com.msf.sessionexample;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PreSessionClient {

   // @Field({"deviceID","apikey","versionNo","osVersion","language"})

    @FormUrlEncoded
    @POST("global/onefa/presession")
    Call<PreSessionData>getPreSessionData(@Field("osVersion") String osVer,
                                          @Field("language") int lang,
                                          @Header("deviceId") String devId,
                                          @Header("apikey") String apikey,
                                          @Header("versionNo") String versionNo);


    @FormUrlEncoded
    @POST("global/onefa/authenticate")
    Call<LoginData>getLoginData(@Field("osVersion") String osVer,
                                @Field("language") int lang,
                                @Header("deviceId") String devId,
                                @Header("apikey") String apikey,
                                @Header("versionNo") String versionNo,
                                @Header("accountNo") String accntNo,
                                @Header("sessionId") String sessionId,
                                @Header("EncryptedPIN") String encryptedPin);
}
