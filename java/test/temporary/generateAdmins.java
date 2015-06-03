package test.temporary;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class generateAdmins {
	HashMap admins = new HashMap();
	public String hash(String password){
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();  
		  String hash = passwordEncoder.encode(password);  
		  return hash;
	}
	
	
public static void main(String[] args){
	generateAdmins gm = new generateAdmins();
	String str = "admin";
	String pass = "Welcome@123";
	int number = 1234567890;
	String email = "skulkar9@asu.edu";
	int ssn = 100000000;
	
	String Passwrod = gm.hash(pass);
	
	for(int i=0;i<30;i++){
		String username = str+""+i;
		ssn = ssn+1;
		
		String query = "INSERT INTO users VALUES ('"+username+"'"+ ", "+"'"+Passwrod+"'"+", "+"'"+Passwrod+"'"+", "+"'"+username+"'"+" , "+"'"+username+"'"+" , "+" 'male','Merchant', "+number+", 'skulkar9@asu.edu', '"+ssn+"', 'address', true,true,true,true);";
System.out.println(query);
		
		String roles = "INSERT INTO user_roles (username, role) VALUES ("+"'"+username+"'"+""+", 'ROLE_ADMIN');";
		System.out.println(roles);
	}
}
public static void printMap(Map mp) {
    Iterator it = mp.entrySet().iterator();
    while (it.hasNext()) {
        Map.Entry pairs = (Map.Entry)it.next();
        System.out.println(pairs.getKey() + " = " + pairs.getValue());
        it.remove(); // avoids a ConcurrentModificationException
    }
}
}
