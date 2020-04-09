package bjfu.it.zhangsixuan.weather.network;

import bjfu.it.zhangsixuan.weather.bean.CityIPBean;
import bjfu.it.zhangsixuan.weather.bean.CityInfoWeather;
import bjfu.it.zhangsixuan.weather.bean.SKWeather;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    String HOST_WEATHER = "http://www.weather.com.cn";

    @GET("/data/cityinfo/{cityId}.html")
    Call<CityInfoWeather> getCityInfoWeather(@Path("cityId") String cityId);

    @GET("/data/sk/{cityId}.html")
    Call<SKWeather> getSKWeather(@Path("cityId")String cityId);

    @GET("/geoip")
    Call<CityIPBean> getCityFromIP();

}
