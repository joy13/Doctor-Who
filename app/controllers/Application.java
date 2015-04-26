package controllers;

import utils.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.sql.*;
import java.util.Date;

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
	
	public static Result appointmentPage()
	{    	
		return ok(views.html.appointment.render());
	}
	
	public static Result confirmPage()
	{    	
		return ok(views.html.confirmation.render());
	}

	public static Result showDiagnoses() throws ClassNotFoundException, SQLException, JsonProcessingException {
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
			Doctor doctor = new Doctor();
			doctor.name = "Dr.Who";
			doctors.add(doctor);
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
//			System.out.println("json "+diagnoses);

			return(ok(diagnoses.toString()));
		}
		JsonNode diagnoses = Json.toJson(diags);
//		System.out.println("json "+diagnoses);
		return(ok(diagnoses.toString()));	
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


	public static Result appointment() throws ClassNotFoundException, SQLException {

		SQLiteJDBC sql = new SQLiteJDBC();

		Map<String, String[]> m = new HashMap<String, String[]>();
		//m = request().body().asFormUrlEncoded();
		JsonNode json = request().body().asJson();
		System.out.println("FROM CONTROLLER "+json);
		ArrayList<String> doctors = new ArrayList<>();
		doctors.add(json.textValue());
//		ArrayNode results = (ArrayNode)json;
//		Iterator<JsonNode> it = results.iterator();
//		while (it.hasNext()) {
//			JsonNode node  = it.next();               
//			doctors.add(node.textValue());
//		}
		String doctorAsString = doctors.get(0);
		System.out.println("FROM CONTROLLER: doctor is "+doctorAsString);
		
		Doctor doc = new Doctor(); //instead of this, search based on name using sqlite.
		doc.name = doctorAsString;
		if(doc.slots == null)
			doc.slots = new ArrayList<Date>();

		return ok(Json.toJson(doc));
	}

	public static Result confirmation()
	{
		Map<String, String> appointmentMap = new HashMap<String, String>();
		JsonNode json = request().body().asJson();
		System.out.println("appointment json "+json);
		Iterator<String> keys = json.fieldNames();
		while(keys.hasNext())
		{
			String key = keys.next();
			appointmentMap.put(key,json.get(key).textValue());
			System.out.println(key+ " "+json.get(key).textValue());
		}

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = f.parse(appointmentMap.get("date"));
			Doctor d = new Doctor(); //instead of this...fetch from db or get from view
			d.name = appointmentMap.get("dr");
			d.slots = new ArrayList<>();
			if(d.slots.contains(date))
				return ok("This slot is already taken. Please try a different one.");
			d.slots.add(date);
			PlayUtilities.sendEmail(appointmentMap.get("name"), appointmentMap.get("cell"), 
					appointmentMap.get("email"), d);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok("Success!");

	}

}