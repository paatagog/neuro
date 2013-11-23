package atm;

import java.util.Date;

public class Weather {

	private Date date;
	
	private Double maxTemperature;
	
	private Double minTemperature;
	
	private Double maxWindSpeed;
	
	private Double maxWindGust;
	
	private Double precipitation;
	
	private Double snowDepth;
	
	private String weatherEvent;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getMaxTemperature() {
		return maxTemperature;
	}

	public void setMaxTemperature(Double maxTemperature) {
		this.maxTemperature = maxTemperature;
	}

	public Double getMinTemperature() {
		return minTemperature;
	}

	public void setMinTemperature(Double minTemperature) {
		this.minTemperature = minTemperature;
	}

	public Double getMaxWindSpeed() {
		return maxWindSpeed;
	}

	public void setMaxWindSpeed(Double maxWindSpeed) {
		this.maxWindSpeed = maxWindSpeed;
	}

	public Double getMaxWindGust() {
		return maxWindGust;
	}

	public void setMaxWindGust(Double maxWindGust) {
		this.maxWindGust = maxWindGust;
	}

	public Double getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(Double precipitation) {
		this.precipitation = precipitation;
	}

	public Double getSnowDepth() {
		return snowDepth;
	}

	public void setSnowDepth(Double snowDepth) {
		this.snowDepth = snowDepth;
	}

	public String getWeatherEvent() {
		return weatherEvent;
	}

	public void setWeatherEvent(String weatherEvent) {
		this.weatherEvent = weatherEvent;
	}

}
