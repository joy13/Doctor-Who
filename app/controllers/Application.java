package controllers;

import utils.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.sql.*;

import com.fasterxml.jackson.databind.JsonNode;

import models.HomeViewModel;
import models.Symptom;
import play.*;
import play.data.Form;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import play.mvc.*;
import utils.SQLiteJDBC;
import views.html.*;

public class Application extends Controller {
	
	public static Form<HomeViewModel> homeForm = Form.form(HomeViewModel.class);
	
    public Application()
	{
		
	}
    
    public static Result index() {
    	
    	//List<Symptom> symptoms = Symptom.symptomFinder.all();
    	
    	List<Symptom> symptoms = utils.MockProvider.MockSymptoms();
    	
    	HashMap<String, Boolean> symptomMap = new HashMap<String, Boolean>();
    	for(Symptom s : symptoms)
    	{
    		symptomMap.put(s.name, false);
    	}
    	
    	
		return ok(index.render(symptoms, homeForm));
    }
    
    public static Result showDisease() throws ClassNotFoundException, SQLException {
    	
    	//List<Symptom> symptoms = Symptom.symptomFinder.all();
    	
    	Form<HomeViewModel> filledForm = homeForm.bindFromRequest();
    	
		if(filledForm.hasErrors()) {
    	    return badRequest(
    	      views.html.index.render(utils.MockProvider.MockSymptoms(), filledForm)
    	    );
    	  } 
		else {    	    
			Map<String, String[]> m = new HashMap<String, String[]>();
	    	m = request().body().asFormUrlEncoded();
	    	String[] selectedSymptomsAsStrings = m.get("selectedSymptoms");
	    	SQLiteJDBC s = new SQLiteJDBC();
	    	
	    	DiagnosisDecisionAlgorithm algo = new DiagnosisDecisionAlgorithm();
	    	HashMap<String,Float> diseases = algo.getScoredDiagnosis(selectedSymptomsAsStrings);
	    	Collection<Float> values = diseases.values();
	    	Set<String> diagnosis = diseases.keySet();
	    	return ok(values.iterator().next().toString());
    	  }	
    }
    
    public static Result BookAppointment() {
    	
    	
    	
    	return ok();
    }

}