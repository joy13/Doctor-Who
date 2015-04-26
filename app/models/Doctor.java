package models;

import java.util.Date;
import java.util.List;

import play.libs.Time;

public class Doctor
{
	public String name;
	public List<String> specialty;
	public ContactInfo contactInfo;
	public List<Date> slots;
	public String email;
}
