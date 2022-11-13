package com.sda.doubleTee.constants;

public enum Days {
	MONDAY("Monday"),
	TUESDAY("Tuesday"),
	WEDNESDAY("Wednesday"),
	THURSDAY("Thursday"),
	FRIDAY("Friday"),
	SATURDAY("Saturday");

	private String day;

	Days(String day) {
		this.day = day;
	}

	public String getDay() {
		return day;
	}
}
