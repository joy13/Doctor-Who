package models;

import java.util.Date;
import java.util.List;

import play.data.format.Formats.DateTime;
import play.libs.Time;
	
	public class Disease
	{
		public String diagnosis;
		public List<Symptom> symptoms;
		public List<Integer> age;
		public List<String> lifestyle;
		public List<String> ethnicity;
		public List<String> specialties;
	}

	
