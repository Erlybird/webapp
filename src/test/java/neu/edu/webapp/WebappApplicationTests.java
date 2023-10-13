package neu.edu.webapp;

import neu.edu.webapp.Controller.HealthzController;
import neu.edu.webapp.DAO.ConnectionCheckDB;
import neu.edu.webapp.Service.ConnectionCheckService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
class WebappApplicationTests {

    @SpringBootTest
    @AutoConfigureMockMvc
    class AssignmentCloud1ApplicationTests {

        //        @Autowired
//        private MockMvc mv;
//
//        @MockBean
//        private ConnectionCheckService connectionCheckService;
//
//        @Test
//        void contextLoads() throws Exception {
//            when(connectionCheckService.isDatabaseConnected()).thenReturn(true);
//
//            // Perform a GET request to the /healthz endpoint
//            ResultActions result = mv.perform(get("/healthz")
//                    .contentType(MediaType.APPLICATION_JSON));
//
//            // Verify that the response has status 200 OK
//            result.andExpect(status().isOk());
        @Autowired
        ConnectionCheckDB connectionCheckDB;

        @Test
        public void testDatabase(){
            assert connectionCheckDB.checkDBConnection();
    }

}

}
