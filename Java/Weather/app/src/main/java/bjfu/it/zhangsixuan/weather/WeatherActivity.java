package bjfu.it.zhangsixuan.weather;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import bjfu.it.zhangsixuan.weather.bean.CityIPBean;
import bjfu.it.zhangsixuan.weather.bean.CityInfoWeather;
import bjfu.it.zhangsixuan.weather.bean.SKWeather;
import bjfu.it.zhangsixuan.weather.network.ApiService;
import bjfu.it.zhangsixuan.weather.network.RetrofitManager;
import bjfu.it.zhangsixuan.weather.utils.CityHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bjfu.it.zhangsixuan.weather.CityListActivity.CITY_ID_RESULT_CODE;

public class WeatherActivity extends AppCompatActivity {

    private ApiService apiService;
    private ApiService apiServiceIP;


    private String cityId;

    private TextView cityNameTv;
    private TextView temperatureTv;
    private TextView weatherTv;
    private TextView windDirectionTv;
    private TextView windSpeedTv;
    private TextView humidTv;
    private TextView airPressureTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        cityNameTv = findViewById(R.id.cityName);
        temperatureTv = findViewById(R.id.temperature);
        weatherTv = findViewById(R.id.weather);
        windDirectionTv = findViewById(R.id.wind_direction);
        windSpeedTv = findViewById(R.id.wind_speed);
        humidTv = findViewById(R.id.humid);
        airPressureTv = findViewById(R.id.atmospheric_pressure);

        cityNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到城市选择列表项
                Intent intent = new Intent(WeatherActivity.this, CityListActivity.class);
                startActivityForResult(intent, CITY_ID_RESULT_CODE);
            }
        });

        apiService = RetrofitManager.getInstance()
                .getRetrofit(ApiService.HOST_WEATHER)
                .create(ApiService.class);

        apiServiceIP = RetrofitManager.getInstance().getRetrofit("http://ip.seeip.org").create(ApiService.class);
        // 请求当前城市
        final Call<CityIPBean> cityIPBeanCall = apiServiceIP.getCityFromIP();
        cityIPBeanCall.enqueue(new Callback<CityIPBean>() {
            @Override
            public void onResponse(Call<CityIPBean> call, Response<CityIPBean> response) {
                CityIPBean cityIPBean = response.body();
                String cityNameEn = cityIPBean.getCity(); // 当前城市的英文名
                final String cityIdn = CityHelper.getInstance().getCityIdFromCityNameEn(cityNameEn); // 当前城市ID
                Toast.makeText(WeatherActivity.this, "定位城市", Toast.LENGTH_LONG).show();
                refresh(cityIdn); // 刷新
            }

            @Override
            public void onFailure(Call<CityIPBean> call, Throwable t) {
                Toast.makeText(WeatherActivity.this, "定位城市失败", Toast.LENGTH_SHORT).show();
                refresh(CityHelper.getDefaultCityId());
            }
        });
    }

    // 把跳转到城市⻚面返回的cityId给接收到，然后刷新⻚面
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CityListActivity.CITY_ID_RESULT_CODE) {
            if (data != null) {
                String cityId = data.getStringExtra(CityListActivity.CITY_ID_KEY);
                // refresh
                refresh(cityId);
            }
        }
    }

    private void refresh(final String cityId) {
        if (TextUtils.isEmpty(cityId) || cityId.equals(this.cityId)) {
            return;
        }

        // 获取Call，并且开始异步网络请求
        final Call<CityInfoWeather> cityInfoWeatherCall = apiService.getCityInfoWeather(cityId);
        cityInfoWeatherCall.enqueue(new Callback<CityInfoWeather>() {
            @Override
            public void onResponse(Call<CityInfoWeather> cityInfoWeatherCall,
                                   final Response<CityInfoWeather> cityInfoWeatherResponse) {

                Toast.makeText(WeatherActivity.this, "请求CityInfo接口成功", Toast.LENGTH_LONG).show();

                Call<SKWeather> skWeatherCall = apiService.getSKWeather(cityId);
                skWeatherCall.enqueue(new Callback<SKWeather>() {
                    @Override
                    public void onResponse(Call<SKWeather> skWeatherCall, Response<SKWeather> skWeatherResponse) {

                        /*
                         *  两个接口都请求成功才refresh
                         */

                        //获取返回Json的对象，直接response.body()即可
                        CityInfoWeather cityInfoWeather = cityInfoWeatherResponse.body();
                        SKWeather skWeather = skWeatherResponse.body();
                        WeatherActivity.this.cityId = cityId;

                        cityNameTv.setText(getString(R.string.city_name_str, CityHelper.getInstance().getCityInfoMap().get(cityId).getName()));
                        temperatureTv.setText(getString(R.string.temp_str, skWeather.getWeatherinfo().getTemp()));
                        weatherTv.setText(cityInfoWeather.getWeatherinfo().getWeather());
                        windDirectionTv.setText(getString(R.string.wd_str, skWeather.getWeatherinfo().getWD()));
                        windSpeedTv.setText(getString(R.string.ws_str, skWeather.getWeatherinfo().getWS()));
                        humidTv.setText(getString(R.string.sd_str, skWeather.getWeatherinfo().getSD()));
                        airPressureTv.setText(getString(R.string.ap_str, skWeather.getWeatherinfo().getAP()));
                        Toast.makeText(WeatherActivity.this, "请求SK接口成功", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(Call<SKWeather> skWeatherCall, Throwable t) {
                        Toast.makeText(WeatherActivity.this, "请求SK接口失败", Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onFailure(Call<CityInfoWeather> cityInfoWeatherCall, Throwable t) {
                Toast.makeText(WeatherActivity.this, "请求CityInfo接口失败", Toast.LENGTH_LONG).show();
            }
        });


    }

}
