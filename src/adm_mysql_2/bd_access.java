package adm_mysql_2;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.TextView;
/**
 * Classe para el manejo de datos de la BBDD
 * todos lo metodos aqu� utilizados actuan de interfaz de comunicaci�n con el servidor
 *  @author Jose Luis
 */
public class bd_access {
	
	private String servidor="";
	
	private JSONArray jArray;
	private String result = null;
	private InputStream is = null;
	private StringBuilder sb=null;
	
	/**
	 * Constructor de la clase bd_access
	 * @param servidor
	 */
	public bd_access(String servidor){
		this.servidor=servidor;
	}
	
	/**
	* Obtengo los contactos de la BBDD a traves de un script php que se ejecuta en el servidor
	* Devuelto un ArraList con los diferentes contactos existentes en el sistema
	*/
	public ArrayList getContacts(){
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		ArrayList <ArrayList> contacts = new  ArrayList  <ArrayList> ();
		//http post
		try{
		     HttpClient httpclient = new DefaultHttpClient();
		     String serv=this.servidor+"get_contacts.php";
		     HttpPost httppost = new HttpPost(serv);
		   
		     //INSERTO los valores que le paso a la LLAMADA PHP
		     //valor obtenido del input de name de la actividad share
		     nameValuePairs.add(new BasicNameValuePair("name", "12345"));
		     //valor obtenido del input de descripcion de la actividad share
		     nameValuePairs.add(new BasicNameValuePair("desc", "12345"));
		     //no hara falta... se insertara cuando se cree la fila
		     //nameValuePairs.add(new BasicNameValuePair("date_creation", "12345"));
		     //se obtienen de gmaps
		     nameValuePairs.add(new BasicNameValuePair("latitude", "12345"));
		     nameValuePairs.add(new BasicNameValuePair("longitude", "12345"));
		     // tengo que obtener el numero de telefono del usuario 
		     // y a partir de el se obtendra  el ID para insertar
		     nameValuePairs.add(new BasicNameValuePair("telefono", "678533111"));
		     //asigno los pares de valores a la llamado HTTPPOST
		     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		     //LLAMADA HTTPPOST
		     HttpResponse response = httpclient.execute(httppost);
		     //RECIBO RESPUESTA
		     HttpEntity entity = response.getEntity();
		     //SE ASIGNA A UN INPUTSTREAM
		     is = entity.getContent();
		     }catch(Exception e){
		         Log.e("log_tag", "Error in http connection"+e.toString());
		    }
		
		
		
		//convert response to string
		try{
		      BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		       sb = new StringBuilder();
		       sb.append(reader.readLine() + "\n");

		       String line="0";
		       while ((line = reader.readLine()) != null) {
		                      sb.append(line + "\n");
		        }
		        is.close();
		        result=sb.toString();
		        }catch(Exception e){
		              Log.e("log_tag", "Error converting result "+e.toString());
		        }
		
		
	   
		//paring data
		int user=0,phone=0;
		String nick="-";
		ArrayList cont= new ArrayList();
		try{
			//obtengo el objeto JSON
		      jArray = new JSONArray(result);
		      JSONObject json_data=null;
		      //recorro los resultados obtenidos por la respuesta formateda del HTTPPOST.response
		      for(int i=0;i<jArray.length();i++){
		    	 
		             json_data = jArray.getJSONObject(i);
		             user=json_data.getInt("ID_user");
		             cont.add(user);
		             phone=json_data.getInt("phone");
		             cont.add(phone);
		             nick=json_data.getString("nick");
		             cont.add(nick);
		       }
		      //almaceno cada una de las filas la lista de contactos que devuelvo al finalizar
		      contacts.add(cont);
		 }
		catch(JSONException e1){
		    	  e1.printStackTrace();
		} catch (ParseException e1) {
					e1.printStackTrace();
		}
		return contacts;
	}
	
	/**
	* Obtengo el contacto en una ArrayList a partir de un numero de telefono
	* @param telef
	*/
	public ArrayList getContactByTelef(int telef){
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		
		//http post
		try{
		     HttpClient httpclient = new DefaultHttpClient();
		     String serv=this.servidor+"get_contact_by_telef.php";
		     HttpPost httppost = new HttpPost(serv);
		     String telefono = Integer.toString(telef);
		     nameValuePairs.add(new BasicNameValuePair("telefono", telefono));
		     //asigno los pares de valores a la llamado HTTPPOST
		     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		     //LLAMADA HTTPPOST
		     HttpResponse response = httpclient.execute(httppost);
		     //RECIBO RESPUESTA
		     HttpEntity entity = response.getEntity();
		     //SE ASIGNA A UN INPUTSTREAM
		     is = entity.getContent();
		     }catch(Exception e){
		         Log.e("log_tag", "Error in http connection"+e.toString());
		    }
		
		
		
		//convert response to string
		try{
		      BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		       sb = new StringBuilder();
		       sb.append(reader.readLine() + "\n");

		       String line="0";
		       while ((line = reader.readLine()) != null) {
		                      sb.append(line + "\n");
		        }
		        is.close();
		        result=sb.toString();
		        }catch(Exception e){
		              Log.e("log_tag", "Error converting result "+e.toString());
		        }
		
		
	    
		//paring data
		ArrayList cont= new ArrayList();
		int user=0,phone=0;
		String nick="-";
		try{
			//obtengo el objeto JSON
		      jArray = new JSONArray(result);
		      JSONObject json_data=null;
		      //recorro los resultados obtenidos por la respuesta formateda del HTTPPOST.response
		      for(int i=0;i<jArray.length();i++){
		    	 
		             json_data = jArray.getJSONObject(i);
		             user=json_data.getInt("ID_user");
		             cont.add(user);
		             phone=json_data.getInt("phone");
		             cont.add(phone);
		             nick=json_data.getString("nick");
		             cont.add(nick);
		       }
		      
		 }
		catch(JSONException e1){
		    	  e1.printStackTrace();
		} catch (ParseException e1) {
					e1.printStackTrace();
		}
		return cont;
	}

	/**
	* Obtengo el contacto en una ArrayList a partir de un ID de usuario
	* @param ID_user
	*/
	public ArrayList getContactByUser(int ID_user){
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		
		//http post
		try{
		     HttpClient httpclient = new DefaultHttpClient();
		     String serv=this.servidor+"get_contact_by_user.php";
		     HttpPost httppost = new HttpPost(serv);
		     String usuario = Integer.toString(ID_user);
		     nameValuePairs.add(new BasicNameValuePair("ID_user", usuario));
		     //asigno los pares de valores a la llamado HTTPPOST
		     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		     //LLAMADA HTTPPOST
		     HttpResponse response = httpclient.execute(httppost);
		     //RECIBO RESPUESTA
		     HttpEntity entity = response.getEntity();
		     //SE ASIGNA A UN INPUTSTREAM
		     is = entity.getContent();
		     }catch(Exception e){
		         Log.e("log_tag", "Error in http connection"+e.toString());
		    }
		
		
		
		//convert response to string
		try{
		      BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		       sb = new StringBuilder();
		       sb.append(reader.readLine() + "\n");

		       String line="0";
		       while ((line = reader.readLine()) != null) {
		                      sb.append(line + "\n");
		        }
		        is.close();
		        result=sb.toString();
		        }catch(Exception e){
		              Log.e("log_tag", "Error converting result "+e.toString());
		        }
		
		
	    
		//paring data
		ArrayList cont= new ArrayList();
		int user=0,phone=0;
		String nick="-";
		try{
			//obtengo el objeto JSON
		      jArray = new JSONArray(result);
		      JSONObject json_data=null;
		      //recorro los resultados obtenidos por la respuesta formateda del HTTPPOST.response
		      for(int i=0;i<jArray.length();i++){
		    	 
		             json_data = jArray.getJSONObject(i);
		             user=json_data.getInt("ID_user");
		             cont.add(user);
		             phone=json_data.getInt("phone");
		             cont.add(phone);
		             nick=json_data.getString("nick");
		             cont.add(nick);
		       }
		      
		 }
		catch(JSONException e1){
		    	  e1.printStackTrace();
		} catch (ParseException e1) {
					e1.printStackTrace();
		}
		return cont;
	}

	/**
	* Inserto el contacto en la BBDD introduciendo un numero de telefono
	* Devuelve 0 si todo correcto
	* Devuelve -1 si el valor esta repetido
	* Devuelve -2 si ha habido un error en la petici�n
	* @param telef
	*/
	public int putContact(int telef){
		
		if(this.getContactByTelef(telef).size()>0){
			
			return -1;
		}
		else{
		
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			ArrayList <ArrayList> contacts = new  ArrayList  <ArrayList> ();
			//http post
			try{
			     HttpClient httpclient = new DefaultHttpClient();
			     String serv=this.servidor+"put_contact.php";
			     HttpPost httppost = new HttpPost(serv);
			     String telefono = Integer.toString(telef);
			     nameValuePairs.add(new BasicNameValuePair("telefono", telefono));
			     //asigno los pares de valores a la llamado HTTPPOST
			     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			     //LLAMADA HTTPPOST
			     HttpResponse response = httpclient.execute(httppost);
			     //RECIBO RESPUESTA
			     HttpEntity entity = response.getEntity();
			     //SE ASIGNA A UN INPUTSTREAM
			     is = entity.getContent();
			     
			     return 0;
		     }catch(Exception e){
		         Log.e("log_tag", "Error in http connection"+e.toString());
		         return -2;
		    }
		}
		
	}
	
	/**
	* Inserto el contacto en la BBDD introduciendo un numero de telefono y el nick
	* Devuelve 0 si todo correcto
	* Devuelve -1 si el valor esta repetido
	* Devuelve -2 si ha habido un error en la petici�n
	* @param telef
	* @param nick
	*/
	public int putContact(int telef,String nick){
		
		if(this.getContactByTelef(telef).size()>0){
			
			return -1;
		}
		else{
		
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			ArrayList <ArrayList> contacts = new  ArrayList  <ArrayList> ();
			//http post
			try{
			     HttpClient httpclient = new DefaultHttpClient();
			     String serv=this.servidor+"put_contact.php";
			     HttpPost httppost = new HttpPost(serv);
			     String telefono = Integer.toString(telef);
			     nameValuePairs.add(new BasicNameValuePair("telefono", telefono));
			     nameValuePairs.add(new BasicNameValuePair("nick", nick));
			     //asigno los pares de valores a la llamado HTTPPOST
			     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			     //LLAMADA HTTPPOST
			     HttpResponse response = httpclient.execute(httppost);
			     //RECIBO RESPUESTA
			     HttpEntity entity = response.getEntity();
			     //SE ASIGNA A UN INPUTSTREAM
			     is = entity.getContent();
			     
			     return 0;
		     }catch(Exception e){
		         Log.e("log_tag", "Error in http connection"+e.toString());
		         return -2;
		    }
		}
	}

	/**
	* Obtengo las Ubicaciones de la BBDD a traves de un script php que se ejecuta en el servidor
	* Devuelto un ArraList con las diferentes ubicaciones existentes en el sistema
	*/
	public ArrayList getPlaces(){
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		ArrayList <ArrayList> places = new  ArrayList  <ArrayList> ();
		//http post
		try{
		     HttpClient httpclient = new DefaultHttpClient();
		     String serv=this.servidor+"get_places.php";
		     HttpPost httppost = new HttpPost(serv);
		   
		     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		     //LLAMADA HTTPPOST
		     HttpResponse response = httpclient.execute(httppost);
		     //RECIBO RESPUESTA
		     HttpEntity entity = response.getEntity();
		     //SE ASIGNA A UN INPUTSTREAM
		     is = entity.getContent();
		     }catch(Exception e){
		         Log.e("log_tag", "Error in http connection"+e.toString());
		    }
		
		
		
		//convert response to string
		try{
		      BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		       sb = new StringBuilder();
		       sb.append(reader.readLine() + "\n");

		       String line="0";
		       while ((line = reader.readLine()) != null) {
		                      sb.append(line + "\n");
		        }
		        is.close();
		        result=sb.toString();
		        }catch(Exception e){
		              Log.e("log_tag", "Error converting result "+e.toString());
		  }
		
		
	   
		//paring data
		ArrayList place= new ArrayList();
		int ID_place=0,user=0;
		String name="",desc="",date_creation="";
		double latitude=0.0,longitude=0.0;
		try{
			//obtengo el objeto JSON
		      jArray = new JSONArray(result);
		      JSONObject json_data=null;
		      //recorro los resultados obtenidos por la respuesta formateda del HTTPPOST.response
		      for(int i=0;i<jArray.length();i++){
		    	 
		             json_data = jArray.getJSONObject(i);
		             user=json_data.getInt("ID_user");
		             place.add(user);
		             ID_place=json_data.getInt("ID_place");
		             place.add(ID_place);
		             name=json_data.getString("name");
		             place.add(name);
		             desc=json_data.getString("descripcion");
		             place.add(desc);
		             date_creation=json_data.getString("date_creation");
		             place.add(date_creation);
		             latitude=json_data.getDouble("latitude");
		             place.add(latitude);
		             longitude=json_data.getDouble("longitude");
		             place.add(longitude);
		             
		       }
		      places.add(place);
		      //Log.e("log_tag", "Error converting result "+places.toString());
		      
		 }
		catch(JSONException e1){
		    	  e1.printStackTrace();
		} catch (ParseException e1) {
					e1.printStackTrace();
		}
		return places;
	}
	
	/**
	* Obtengo las ubicaciones de un usuario  en una ArrayList que contiene varios Arraylist con los datos de cada ubicacion
	* @param ID_user
	*/
	public ArrayList getPlacesByUser(int ID_user){
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		ArrayList <ArrayList> places = new  ArrayList  <ArrayList> ();
		//http post
		try{
		     HttpClient httpclient = new DefaultHttpClient();
		     String serv=this.servidor+"get_places_by_user.php";
		     HttpPost httppost = new HttpPost(serv);
		     String usuario = Integer.toString(ID_user);
		     nameValuePairs.add(new BasicNameValuePair("ID_user", usuario));
		     //asigno los pares de valores a la llamado HTTPPOST
		     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		     //LLAMADA HTTPPOST
		     HttpResponse response = httpclient.execute(httppost);
		     //RECIBO RESPUESTA
		     HttpEntity entity = response.getEntity();
		     //SE ASIGNA A UN INPUTSTREAM
		     is = entity.getContent();
		     }catch(Exception e){
		         Log.e("log_tag", "Error in http connection"+e.toString());
		    }
		
		
		
		//convert response to string
		try{
		      BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		       sb = new StringBuilder();
		       sb.append(reader.readLine() + "\n");

		       String line="0";
		       while ((line = reader.readLine()) != null) {
		                      sb.append(line + "\n");
		        }
		        is.close();
		        result=sb.toString();
		        }catch(Exception e){
		              Log.e("log_tag", "Error converting result "+e.toString());
		        }
		
		
	    
		//paring data
		ArrayList place= new ArrayList();
		int ID_place=0,user=0;
		String name="",desc="",date_creation="";
		double latitude=0.0,longitude=0.0;
		try{
			//obtengo el objeto JSON
		      jArray = new JSONArray(result);
		      JSONObject json_data=null;
		      //recorro los resultados obtenidos por la respuesta formateda del HTTPPOST.response
		      for(int i=0;i<jArray.length();i++){
		    	 
		             json_data = jArray.getJSONObject(i);
		             user=json_data.getInt("ID_user");
		             place.add(user);
		             ID_place=json_data.getInt("ID_place");
		             place.add(ID_place);
		             name=json_data.getString("name");
		             place.add(name);
		             desc=json_data.getString("descripcion");
		             place.add(desc);
		             date_creation=json_data.getString("date_creation");
		             place.add(date_creation);
		             latitude=json_data.getDouble("latitude");
		             place.add(latitude);
		             longitude=json_data.getDouble("longitude");
		             place.add(longitude);
		       }
		      places.add(place);
		      
		 }
		catch(JSONException e1){
		    	  e1.printStackTrace();
		} catch (ParseException e1) {
					e1.printStackTrace();
		}
		return places;
	}
	
	/**
	* Inserto la ubicaci�n en la BBDD introduciendo todos los parametros obligatorios
	* Devuelve 0 si todo correcto
	* Devuelve -1 si el ID de usuario no existe en la BBDD
	* Devuelve -2 si ha habido un error en la petici�n
	* @param name
	* @param latitude
	* @param longitude
	* @param ID_user
	*/
	public int putPlace(String name,double latitude,double longitude,int ID_user){
		
		if(this.getContactByUser(ID_user).size()==0){
			
			//el usuario que intentas insertar no existe
			return -1;
		}
		else{
		
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			//http post
			try{
				
			     HttpClient httpclient = new DefaultHttpClient();
			     String serv=this.servidor+"put_place.php";
			     HttpPost httppost = new HttpPost(serv);
			     String lat = Double.toString(latitude);
			     nameValuePairs.add(new BasicNameValuePair("latitude", lat));
			     String lon = Double.toString(longitude);
			     nameValuePairs.add(new BasicNameValuePair("longitude", lon));
			     nameValuePairs.add(new BasicNameValuePair("name", name));
			     String ID_usr= Integer.toString(ID_user);
			     nameValuePairs.add(new BasicNameValuePair("ID_user", ID_usr));
			      
			     //asigno los pares de valores a la llamado HTTPPOST
			     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			     //LLAMADA HTTPPOST
			     HttpResponse response = httpclient.execute(httppost);
			     //RECIBO RESPUESTA
			     HttpEntity entity = response.getEntity();
			     //SE ASIGNA A UN INPUTSTREAM
			     is = entity.getContent();
			     
			     return 0;
		     }catch(Exception e){
		    	 //error de conexi�n
		         Log.e("log_tag", "Error in http connection"+e.toString());
		         return -2;
		    }
		}
	}
	
	/**
	* Inserto la ubicaci�n en la BBDD introduciendo todos los parametros obligatorios y no obligatorios
	* Devuelve 0 si todo correcto
	* Devuelve -1 si el ID de usuario no existe en la BBDD
	* Devuelve -2 si ha habido un error en la petici�n
	* @param name
	* @param latitude
	* @param longitude
	* @param ID_user
	* @param desc
	*/
	public int putPlace(String name,double latitude,double longitude,int ID_user,String descripcion){
		
		if(this.getContactByUser(ID_user).size()==0){
			
			//el usuario que intentas insertar no existe
			return -1;
		}
		else{
		
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			//http post
			try{
			     HttpClient httpclient = new DefaultHttpClient();
			     String serv=this.servidor+"put_place_desc.php";
			     HttpPost httppost = new HttpPost(serv);
			     String lat = Double.toString(latitude);
			     nameValuePairs.add(new BasicNameValuePair("latitude", lat));
			     String lon = Double.toString(longitude);
			     nameValuePairs.add(new BasicNameValuePair("longitude", lon));
			     nameValuePairs.add(new BasicNameValuePair("name", name));
			     String ID_usr= Integer.toString(ID_user);
			     nameValuePairs.add(new BasicNameValuePair("ID_user", ID_usr));
			     nameValuePairs.add(new BasicNameValuePair("desc", descripcion));
			     //asigno los pares de valores a la llamado HTTPPOST
			     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			     //LLAMADA HTTPPOST
			     HttpResponse response = httpclient.execute(httppost);
			     //RECIBO RESPUESTA
			     HttpEntity entity = response.getEntity();
			     //SE ASIGNA A UN INPUTSTREAM
			     is = entity.getContent();
			     
			     return 0;
		     }catch(Exception e){
		    	 //error de conexi�n
		         Log.e("log_tag", "Error in http connection"+e.toString());
		         return -2;
		    }
		}
	}
	/**
	* Elimino las publicaciones de las ubicaciones de un contacto pasandole el telefono
	* Devuelve false si no se ha podido eliminar
	* @param telef
	*/
	public int deletePlacesByTelef(int telef){
		ArrayList al = this.getContactByTelef(telef);
		if(al.size()==0){
			
			//el usuario que intentas insertar no existe
			return -1;
		}
		else{
			int ID_user =(Integer) al.get(0);
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			//http post
			try{
			     HttpClient httpclient = new DefaultHttpClient();
			     String serv=this.servidor+"delete_places_by_user.php";
			     HttpPost httppost = new HttpPost(serv);
			     String user= Integer.toString(ID_user);
			     nameValuePairs.add(new BasicNameValuePair("ID_user", user));
			     //asigno los pares de valores a la llamado HTTPPOST
			     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			     //LLAMADA HTTPPOST
			     HttpResponse response = httpclient.execute(httppost);
			     //RECIBO RESPUESTA
			     HttpEntity entity = response.getEntity();
			     //SE ASIGNA A UN INPUTSTREAM
			     is = entity.getContent();
			     
			     return 0;
		     }catch(Exception e){
		    	 //error de conexi�n
		         Log.e("log_tag", "Error in http connection"+e.toString());
		         return -2;
		    }
		}
	}
	/**
	* Elimino la Ultima publicacion de un contacto pasandole el telefono
	* Devuelve false si no se ha podido eliminar
	* @param telef
	*/
	public int deleteLastPlaceByTelef(int telef){
		ArrayList al = this.getContactByTelef(telef);
		if(al.size()==0){
			
			//el usuario que intentas insertar no existe
			return -1;
		}
		else{
			int ID_user =(Integer) al.get(0);
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			//http post
			try{
			     HttpClient httpclient = new DefaultHttpClient();
			     String serv=this.servidor+"delete_last_place_by_user.php";
			     HttpPost httppost = new HttpPost(serv);
			     String user= Integer.toString(ID_user);
			     nameValuePairs.add(new BasicNameValuePair("ID_user", user));
			     //asigno los pares de valores a la llamado HTTPPOST
			     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			     //LLAMADA HTTPPOST
			     HttpResponse response = httpclient.execute(httppost);
			     //RECIBO RESPUESTA
			     HttpEntity entity = response.getEntity();
			     //SE ASIGNA A UN INPUTSTREAM
			     is = entity.getContent();
			     
			     return 0;
		     }catch(Exception e){
		    	 //error de conexi�n
		         Log.e("log_tag", "Error in http connection"+e.toString());
		         return -2;
		    }
		}
	}
}
