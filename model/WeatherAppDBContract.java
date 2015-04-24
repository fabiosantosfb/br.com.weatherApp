package ceducarneiro.com.br.weatherapp.model;

public abstract class WeatherAppDBContract {

    public static class TableContract {
        public static String COLUMN_ID = "id";
    }

    public static final class Place extends TableContract {
        public static String TABLE_NAME = "place";

        public static String COLUMN_CITY = "city";
        public static String COLUMN_COUNTRY = "country";
        public static String COLUMN_LATITUDE = "latitude";
        public static String COLUMN_LONGITUDE = "longitude";
        public static String COLUMN_WEATHER_ID = "weather_id";
    }

    public static final class Weather extends TableContract {
        public static String TABLE_NAME = "weather";

        public static String COLUMN_CODE = "code";
        public static String COLUMN_ICON = "icon";
        public static String COLUMN_TEMPERATURE = "temperature";
        public static String COLUMN_HUMIDITY = "humidity";
        public static String COLUMN_PRESSURE = "pressure";
        public static String COLUMN_WIND_DEGREE = "wind_degree";
        public static String COLUMN_WIND_SPEED = "wind_speed";
        public static String COLUMN_LAST_UPDATE = "last_update";
    }

    public static final class Alert extends TableContract {

        public static String TABLE_NAME = "alert";

        public static String COLUMN_OPTION = "option";
        public static String COLUMN_CONDITION = "condition";
        public static String COLUMN_VALUE = "value";
        public static String COLUMN_PLACE_ID = "place_id";
    }

}
