import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import models.Disease;
import models.Doctor;

import org.apache.http.client.ClientProtocolException;

import utils.CallFHIR;
import utils.DiagnosisDecisionAlgorithm;
import utils.MockProvider;
import utils.SQLiteJDBC;


public class UnitTest {

public static void main(String[] args) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		SQLiteJDBC sql = new SQLiteJDBC();
		HashMap<String,ArrayList<String>> cd = sql.getConditionDoctorMap();
		for (Entry<String, ArrayList<String>> e : cd.entrySet())
		{
			System.out.println(e.getKey());
			for(String s: e.getValue())
			{
				System.out.println(s);
			}
				
		}
	}

}