package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.sql.DataSource;

import play.db.*;


public class SQLiteJDBC {
	Connection connection = null;
	Statement stmt = null;
	public SQLiteJDBC() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:database/DoctorWho");
		connection.setAutoCommit(false);
		stmt = connection.createStatement();
		//connection = DB.getConnection();
	}
	
	public Integer[] getSymptomIdsFromNames(String[] symptomNames) throws SQLException {
		Integer[] symptomIds = new Integer[symptomNames.length];
		for(int i=0;i<symptomNames.length;i++) {
			String sname = symptomNames[i];
			System.out.println(sname);
		try{
	    ResultSet rs = stmt.executeQuery("SELECT id as id FROM Symptom where symptomName ="+"'"+sname+"';");
	    while (rs.next()) {
	    	symptomIds[i]=rs.getInt("id");
	    }
		}
		catch(Exception e){
			e.printStackTrace();
		}
		}
		return symptomIds;		
	}
	
	public ArrayList<String> getDiagnosisNamesFromIds(Integer[] diagnosisIds) throws SQLException {
		ArrayList<String> diagnosisNames = new ArrayList<String>();
		for(int i=0;i<diagnosisIds.length;i++) {
			int did = diagnosisIds[i];
	    ResultSet rs = stmt.executeQuery("SELECT diagnosisName as name FROM Diagnosis where id ="+did+";");
	    while (rs.next()) {
	    	diagnosisNames.add(rs.getString("name"));
	    }
		}
		return diagnosisNames;		
	}
	
	public ArrayList<Integer> getFilteredDiagnosisList(Integer[] inputSymptoms) throws SQLException
	{
		ArrayList<Integer> filteredDiagnosisList = new ArrayList<Integer>();
		for(int i=0;i<inputSymptoms.length;i++) {
			int sid = inputSymptoms[i];
			ResultSet rs = stmt.executeQuery("select d.id as diagnosis from Diagnosis as d join Diagnosis_Symptom as ds on d.id = ds.diagnosis_id where ds.symptom_id ="+sid+";");	    
	    while (rs.next()) {
	    	filteredDiagnosisList.add(rs.getInt("diagnosis"));
	    }
		}
		return filteredDiagnosisList;
	}
	
	public ArrayList<Integer> getSymptomListByID() throws SQLException
	{
		ArrayList<Integer> symptoms = new ArrayList<Integer>();
	    ResultSet rs = stmt.executeQuery("SELECT id FROM Symptom;");
	    while (rs.next()) {
	    	symptoms.add(rs.getInt("id"));
	    }
		return symptoms;
	}
	
	public ArrayList<String> getSymptomListByName() throws SQLException
	{
		ArrayList<String> symptoms = new ArrayList<String>();
	    ResultSet rs = stmt.executeQuery("SELECT symptomName FROM Symptom;");
	    while (rs.next()) {
	    	symptoms.add(rs.getString("symptomName"));
	    }
		return symptoms;
	}

	public HashMap<Integer, Integer[]> getDiagnosisSymptomMap() throws SQLException {
		// TODO Auto-generated method stub
		ArrayList<Integer> diagList = getDiagnosisListByID();
		HashMap<Integer, Integer[]> diagnosisSymptomMap = new HashMap<Integer, Integer[]>();
		for(int i=0;i<diagList.size();i++) {
			ArrayList<Integer> dList = new ArrayList<>();
			int did = diagList.get(i);
			//System.out.println("diag id "+did);
			ResultSet rs = stmt.executeQuery("select s.id as symptom from Symptom as s join Diagnosis_Symptom as ds on s.id = ds.symptom_id where ds.diagnosis_id ="+did+";");	    
	    while (rs.next()) {
	    	//System.out.println(rs.getInt("symptom"));
	    	dList.add(rs.getInt("symptom"));
	    }
	    Integer[] diags = new Integer[diagList.size()];
	    diags = (Integer[]) dList.toArray(new Integer[dList.size()]);
	    diagnosisSymptomMap.put(did,diags);
		}
		return diagnosisSymptomMap;
	}

	public ArrayList<Integer> getDiagnosisListByID() throws SQLException {
		// TODO Auto-generated method stub
		ArrayList<Integer> diagnosis = new ArrayList<Integer>();
	    ResultSet rs2 = stmt.executeQuery("SELECT id FROM Diagnosis;");
	    while (rs2.next()) {
	    	diagnosis.add(rs2.getInt("id"));
	    }
		return diagnosis;
	}

}
