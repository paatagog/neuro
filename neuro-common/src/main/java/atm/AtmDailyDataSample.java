package atm;

import java.util.Calendar;
import java.util.Date;

public class AtmDailyDataSample {
	
	/**
	 * თარიღი
	 */
	private Date date;
	
	/**
	 * თანხა
	 */
	private double amount;
	
	/**
	 * არის თუ არა დღესასწაული
	 */
	private int holliday;

	/**
	 * ტემპერატურა
	 */
	private double temperature;
	
	/**
	 * თოვლი
	 */
	private int weatherEvent;
	
	private int adjustedWeekDay;
	
	private int adjustedWeekOfMonth;

	private int adjustedHolliday;

	private double adjustedAmount;
	
	private int adjustedTemperature;
	
	private int adjustedWeatherEvent;
	
	private double monthMaxAmount;
	
	/**
	 * მხოლოდ თვის მაქსიმალურ შემოსავალს აქვს ეს მნიშვნელობა ჭეშმარიტი
	 */
	private boolean maxElement;
	
	
	public int getWeekDay() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		return cal.get(Calendar.DAY_OF_WEEK);
		
	}
	
	public int getAbsoluteDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2010);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return Math.round((date.getTime() - cal.getTime().getTime()) / ( 1000 * 60 * 60 * 24) );		
	}
	
	public int getMonth() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH);
	}
	
	public int getWeekOfMonth() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_MONTH);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getHolliday() {
		return holliday;
	}

	public void setHolliday(int holliday) {
		this.holliday = holliday;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public int getWeatherEvent() {
		return weatherEvent;
	}

	public void setWeatherEvent(int weatherEvent) {
		this.weatherEvent = weatherEvent;
	}

	public double getAdjustedAmount() {
		return adjustedAmount;
	}

	public void setAdjustedAmount(double adjustedAmount) {
		this.adjustedAmount = adjustedAmount;
	}

	public int getAdjustedHolliday() {
		return adjustedHolliday;
	}

	public void setAdjustedHolliday(int adjustedHolliday) {
		this.adjustedHolliday = adjustedHolliday;
	}

	public int getAdjustedTemperature() {
		return adjustedTemperature;
	}

	public void setAdjustedTemperature(int adjustedTemperature) {
		this.adjustedTemperature = adjustedTemperature;
	}

	public int getAdjustedWeatherEvent() {
		return adjustedWeatherEvent;
	}

	public void setAdjustedWeatherEvent(int adjustedWeatherEvent) {
		this.adjustedWeatherEvent = adjustedWeatherEvent;
	}

	public int getAdjustedWeekDay() {
		return adjustedWeekDay;
	}

	public void setAdjustedWeekDay(int adjustedWeekDay) {
		this.adjustedWeekDay = adjustedWeekDay;
	}

	public int getAdjustedWeekOfMonth() {
		return adjustedWeekOfMonth;
	}

	public void setAdjustedWeekOfMonth(int adjustedWeekOfMonth) {
		this.adjustedWeekOfMonth = adjustedWeekOfMonth;
	}

	public boolean isMaxElement() {
		return maxElement;
	}

	public void setMaxElement(boolean maxElement) {
		this.maxElement = maxElement;
	}

	public double getMonthMaxAmount() {
		return monthMaxAmount;
	}

	public void setMonthMaxAmount(double monthMaxAmount) {
		this.monthMaxAmount = monthMaxAmount;
	}

}
