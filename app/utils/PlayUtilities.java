package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import models.Doctor;

import com.codesnippets4all.json.generators.JsonGeneratorFactory;
import com.codesnippets4all.json.parsers.JSONParser;

public class PlayUtilities {

	public static <K,V extends Comparable<? super V>> 
	Map<K, V> entriesSortedByValues(Map<K,V> map) {

		List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());

		Collections.sort(sortedEntries, 
				new Comparator<Entry<K,V>>() {
			@Override
			public int compare(Entry<K,V> e1, Entry<K,V> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
		}
				);

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : sortedEntries)
		{
			result.put( entry.getKey(), entry.getValue() );
		}
		return result;
	}

	public static Map getMapFromJson(String json)
	{
		JsonGeneratorFactory factory=JsonGeneratorFactory.getInstance();
		JSONParser parser= new JSONParser();
		Map jsonData;
		try {
			jsonData=parser.parseJson(json);
		}
		catch(Exception e)
		{ 
			jsonData = null;
		}
		return jsonData;
	}
	
	public static List<String> slotGenerator()
	{
		ArrayList<String> list = new ArrayList<String>() {{
		    add("9:00 - 11:00");
		    add("11:00 - 13:00");
		    add("13:00 - :1500");
		}};
		return list;
	}
	
	public static void sendEmail(String name, String date, String slot, String email, Doctor d)
	{
		final String username = "cs6440.doctorwho@gmail.com";
		final String password = "doctorwhodoctorwho";

       Properties props = new Properties();
       props.put("mail.smtp.auth", "true");
       props.put("mail.smtp.starttls.enable", "true");
       props.put("mail.smtp.host", "smtp.gmail.com");
       props.put("mail.smtp.port", "587");

       Session session = Session.getInstance(props,
               new javax.mail.Authenticator() {
                   protected PasswordAuthentication  getPasswordAuthentication() {
                       return new PasswordAuthentication(username, password);
                   }
               });

       try {

           Message message = new MimeMessage(session);
           message.setFrom(new InternetAddress("cs6440.doctorwho@gmail.com"));
           message.setRecipients(Message.RecipientType.TO,
                   InternetAddress.parse(email));
           message.setSubject("Appointment confirmation");
           message.setText("Hi, " + name + "! "+
                   "Your appointment is confirmed with " + d.name + " on "
        		   +date + " at " +slot + "!");

           Transport.send(message);

           
           Message message2 = new MimeMessage(session);
           message2.setFrom(new InternetAddress("cs6440.doctorwho@gmail.com"));
           message2.setRecipients(Message.RecipientType.TO,
                   InternetAddress.parse("cs6440.doctorwho@gmail.com"));
           message2.setSubject("Appointment alert");
           message2.setText("Hi, " + d.name + ", an appointment is confirmed with " + name + "on "
        		   +date + " at " +slot + "!");

           Transport.send(message2);

           System.out.println("Mail sent succesfully!");

       } catch (MessagingException e) {
           throw new RuntimeException(e);
       }
   }
	

}
