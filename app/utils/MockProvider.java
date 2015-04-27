package utils;

import java.util.ArrayList;
import java.util.List;

import models.Symptom;

public class MockProvider {
	
	public static List<Symptom> MockSymptoms()
	{
		List<Symptom> symptoms = new ArrayList<Symptom>();
		Symptom s1 = new Symptom();
		s1.name = "hiccup";
		
		Symptom s2 = new Symptom();
		s2.name = "cough";
		
		Symptom s3 = new Symptom();
		s3.name = "watery eyes";
		
		Symptom s4 = new Symptom();
		s4.name = "nasal congestion";
		
		Symptom s5 = new Symptom();
		s5.name = "sore throat";
		
		Symptom s6 = new Symptom();
		s6.name = "headache";
		
		Symptom s7 = new Symptom();
		s7.name = "sneeze";
		
		Symptom s8 = new Symptom();
		s8.name = "mouth breathing";
		
		
		symptoms.add(s1);
		symptoms.add(s2);
		symptoms.add(s3);
		symptoms.add(s4);
		symptoms.add(s5);
		symptoms.add(s6);
		symptoms.add(s7);
		symptoms.add(s8);
		
		return symptoms;
	}
	
	public static List<String> MockDiagnosis()
	{
		List<String> diagnosis = new ArrayList<String>();
		diagnosis.add("Asthma");
		diagnosis.add("flu");
		return diagnosis;
	}
	
	public static String[] mockDoctors()
	{
		String[] dummyNames = new String[4];
		dummyNames[0] = "Nell K Goff, MD";
		dummyNames[1] = "Benjamin H Scott, MD";
		dummyNames[2] = "Priscilla I Smith, MD";
		dummyNames[3] = "Nadine T Johns, MD";
		
		return dummyNames;
	}

}
