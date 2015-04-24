package ceducarneiro.com.br.weatherapp.ws;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenWeatherList {

    public List<OpenWeather> list;

}
