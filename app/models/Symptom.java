package models;

import java.util.Date;
import java.util.List;

import play.data.format.Formats.DateTime;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import play.libs.Time;

public class Symptom
{
	public String name;
	public List<String> diagnosis;
	
	public static Finder<Integer, Symptom> symptomFinder = new Model.Finder<>(Integer.class, Symptom.class);

	public static Object findByName(String term, int aUTOCOMPLETE_MAX) {
		// TODO Auto-generated method stub
		return null;
	}
}
