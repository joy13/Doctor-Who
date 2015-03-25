package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.HomeViewModel;
import models.Symptom;
import play.*;
import play.data.*;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import play.api.data.*;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
	
	static play.data.Form<HomeViewModel> homeForm = play.data.Form.form(HomeViewModel.class);
	
    public Application()
	{
		
	}
    
    public static Result index() {
    	
    	List<Symptom> symptoms = Symptom.symptomFinder.all();
    	
    	HashMap<String, Boolean> symptomMap = new HashMap<String, Boolean>();
    	for(Symptom s : symptoms)
    	{
    		symptomMap.put(s.name, false);
    	}
    	
    	
		return ok(index.render(symptoms, homeForm));
    }
    
    public static Result showDisease() {
    	List<Symptom> symptoms = Symptom.symptomFinder.all();
    	
    	play.data.Form<HomeViewModel> filledForm = homeForm.bindFromRequest();
    	  if(filledForm.hasErrors()) {
    	    return badRequest(
    	      views.html.index.render(symptoms, filledForm)
    	    );
    	  } else {
    		  //call algo and show results
    	    return redirect(routes.Application.index());  
    	  }
    	
    }

}