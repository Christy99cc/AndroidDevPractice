package bjfu.it.zhangsixuan.weather.bean;

import androidx.annotation.Keep;

@Keep
public class CityIPBean {

    String ip;
    String city;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
