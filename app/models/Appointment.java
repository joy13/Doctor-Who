package models;

import java.util.Date;
import java.util.List;

import play.data.format.Formats.DateTime;
import play.libs.Time;


public class Appointment
{
	public Doctor doc;
	public DateTime time;
	public Patient patient;
}


