package bjfu.it.zhangsixuan.weather.network;

import java.util.HashMap;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitManager {
    private static RetrofitManager singleton;

    private HashMap<String, Retrofit> retrofitHashMap;

    private RetrofitManager(){
        retrofitHashMap = new HashMap<>();
    }

    public static RetrofitManager getInstance(){
        if (singleton == null){
            singleton = new RetrofitManager();
        }
        return singleton;
    }

    private Retrofit createRetrofit(String host){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(host)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public Retrofit getRetrofit(String host){
        if (retrofitHashMap.containsKey(host)){
            return retrofitHashMap.get(host);
        }else {
            Retrofit retrofit = createRetrofit(host);
            retrofitHashMap.put(host, retrofit);
            return retrofit;
        }
    }
}
