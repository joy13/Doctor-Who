package models;

import java.util.Date;
import java.util.List;

import play.data.format.Formats.DateTime;
import play.libs.Time;

public class Models {
	
	class Disease
	{
		public String diagnosis;
		public List<Symptom> Symptoms;
		public List<Integer> age;
		public List<String> lifestyle;
		public List<String> ethnicity;
		public List<String> specialties;
	}

	class Symptom
	{
		public String name;
		public List<String> diagnosis;
	}

	class Patient
	{
		public int age;
		public String ethnicity;
		public String lifestyle;
		public int zipcode;
	}

	class Appointment
	{
		public Doctor doc;
		public DateTime time;
		public Patient patient;
	}

	class Doctor
	{
		public List<String> specialty;
		public ContactClass contactInfo;
		public List<TimeSlot> slots;
	}

	class ContactClass 
	{
		public String address;
		public String phone;
		public int zipcode;
		
	}

	class TimeSlot
	{
		public Date date;
		public Time time;
		public Boolean isAvailable;
	}
	
	//ViewModels

	class CreateAppointmentViewModel
	{
		public Disease disease;
		public Patient patient;
	}


}
