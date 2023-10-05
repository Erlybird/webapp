package neu.edu.webapp.Service;

import neu.edu.webapp.DAO.*;
import neu.edu.webapp.DAO.ConnectionCheckDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConnectionCheckService {

	@Autowired
	ConnectionCheckDB connectionCheckDB;

	public boolean isDatabaseConnected() {

		if (connectionCheckDB.checkDBConnection()) {
			return true;
		} else {
			return false;
		}

	}

}
