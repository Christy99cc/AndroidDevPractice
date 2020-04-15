package bjfu.it.zhangsixuan.weather.bean;

import androidx.annotation.Keep;

@Keep
public class CityInfoWeather {

    private WeatherInfo weatherinfo;

    public WeatherInfo getWeatherinfo() {
        return weatherinfo;
    }

    public void setWeatherinfo(WeatherInfo weatherinfo) {
        this.weatherinfo = weatherinfo;
    }

    @Keep
    public class WeatherInfo{
        private String weather;

        public String getWeather(){
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }
    }

}
