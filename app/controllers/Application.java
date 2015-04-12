package controllers;

import utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.sql.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import models.Disease;
import models.Doctor;
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

import com.fasterxml.jackson.databind.ObjectMapper;


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
    
    public static Result diagnose()
    {    	
    	return ok(views.html.diagnoses.render());
    }
    
    public static Result showDiagnoses() throws ClassNotFoundException, SQLException, JsonProcessingException {
    	
//		if(filledForm.hasErrors()) {
//    	    return badRequest(
//    	      views.html.index.render("Wrong")
//    	    );
//    	  } 
//		else {    	    
    	JsonNode json = request().body().asJson();
		ArrayNode results = (ArrayNode)json;
    	Iterator<JsonNode> it = results.iterator();
    	ArrayList<String> syms = new ArrayList<>();
    	while (it.hasNext()) {
            JsonNode node  = it.next();               
            syms.add(node.textValue());
            
    	}
    	String[] addedSymptoms =  syms.toArray(new String[syms.size()]);
    	DiagnosisDecisionAlgorithm algo = new DiagnosisDecisionAlgorithm();
    	HashMap<String,Float> diagnosesScoreMap = algo.getScoredDiagnosis(addedSymptoms);
    	Set<String> ds = diagnosesScoreMap.keySet();
    	ArrayList<Disease> diags = new ArrayList<Disease>();
    	for(String d:ds)
    	{
    		Disease disease = new Disease();
    		ArrayList<Doctor> doctors= new ArrayList<Doctor>(); //get doctors data from FHIR here
    		disease.diagnosis = d;
    		disease.doctors = doctors;
    		diags.add(disease);
    	}
    	if(diags.size() > 4)
    	{
    		ArrayList<Disease> top4Diags = new ArrayList<>();
    		for(int i=0;i<4;i++){
    			top4Diags.add(diags.get(i));
    		}
    		JsonNode diagnoses = Json.toJson(top4Diags);
    		System.out.println("json "+diagnoses);
    		
    		return(ok(diagnoses.toString()));
    	}
    	JsonNode diagnoses = Json.toJson(diags);
    	System.out.println("json "+diagnoses);
    	return(ok(diagnoses.toString()));
//    	  }	
    }
    
    public static Result symptoms() throws ClassNotFoundException, SQLException
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