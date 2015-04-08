package controllers;

import utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.sql.*;

import models.HomeViewModel;
import models.Symptom;
import play.*;
import play.libs.Json;
import play.data.Form;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import play.mvc.*;
import utils.SQLiteJDBC;
import views.html.*;

public class Application extends Controller {
	
	public static Form<HomeViewModel> homeForm = Form.form(HomeViewModel.class);
	public static int AUTOCOMPLETE_MAX = 5;
	
    public Application()
	{
		
	}
    
    public static Result index()
    {
    	return ok(index.render("hello"));
    }
    
    public static Result showDisease() throws ClassNotFoundException, SQLException {
    	
    	//List<Symptom> symptoms = Symptom.symptomFinder.all();
    	
    	Form<HomeViewModel> filledForm = homeForm.bindFromRequest();
    	
//		if(filledForm.hasErrors()) {
//    	    return badRequest(
//    	      views.html.index.render("Wrong")
//    	    );
//    	  } 
//		else {    	    
			Map<String, String[]> m = new HashMap<String, String[]>();
	    	m = request().body().asFormUrlEncoded();
	    	String[] selectedSymptomsAsStrings = m.get("selectedSymptoms");
	    	System.out.println(Arrays.toString(selectedSymptomsAsStrings));
	    	SQLiteJDBC s = new SQLiteJDBC();
	    	
	    	DiagnosisDecisionAlgorithm algo = new DiagnosisDecisionAlgorithm();
	    	HashMap<String,Float> diseases = algo.getScoredDiagnosis(selectedSymptomsAsStrings);
	    	Collection<Float> values = diseases.values();
	    	Set<String> diagnosis = diseases.keySet();
	    	return ok(diagnosis.iterator().next().toString());
//    	  }	
    }
    
    public static Result symtoms() throws ClassNotFoundException, SQLException
    {
    	SQLiteJDBC sql = new SQLiteJDBC();
    	List<Symptom> symptoms = new ArrayList<Symptom>();
    	ArrayList<String> syms = sql.getSymptomListByName();
    	for(String s:syms)
    	{
    		Symptom sm = new Symptom();
    		sm.name = s;
    		sm.diagnosis = null;
    		symptoms.add(sm);
    	}
    	return ok(Json.toJson(symptoms));
    }
    
    public static Result BookAppointment() {
    	
    	
    	
    	return ok();
    }

}