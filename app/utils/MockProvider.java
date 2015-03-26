package utils;

import java.util.ArrayList;
import java.util.List;

import models.Symptom;

public class MockProvider {
	
	public static List<Symptom> MockSymptoms()
	{
		List<Symptom> symptoms = new ArrayList<Symptom>();
		Symptom s1 = new Symptom();
		s1.name = "Wheeze";
		
		Symptom s2 = new Symptom();
		s2.name = "Fever";
		
		symptoms.add(s1);
		symptoms.add(s2);
		
		return symptoms;
	}
	
	public static List<String> MockDiagnosis()
	{
		List<String> diagnosis = new ArrayList<String>();
		diagnosis.add("Asthma");
		diagnosis.add("flu");
		return diagnosis;
	}

}
