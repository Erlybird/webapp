package neu.edu.webapp.DAO;

import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

@Component
public class ConnectionCheckDB extends DAO  {
	
	public boolean checkDBConnection() {
		
		 try {
	            begin();
	              
	            Query<?> res = getSession().createNativeQuery("SELECT 1");
	            res.getSingleResult();
	            commit();
	            return true;
	          
	        } catch (Exception e) {
	            System.out.println("Exception: " + e.getMessage());
	            return false;
	        } finally {
	            close();
	        }
		
	}

}
