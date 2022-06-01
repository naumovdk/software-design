package users;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.ClassRule;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.junit.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import users.clients.StockClient;
import users.model.Stock;
import users.model.UserInfo;
import users.model.UserStock;

import static org.junit.Assert.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {ApplicationTest.Initializer.class})
public class ApplicationTest {
    @Autowired
    private MockMvc mockMvc;
    private final static String dbName = "integration-db";
    private final static String dbUserName = "stocks";
    private final static String dbPwd = "123";
    private static GenericContainer stockServer;
    private static Type arrayUserStockType = new TypeToken<ArrayList<UserStock>>() {
    }.getType();
    @ClassRule(order = 1)
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres" +
            ":11.1")
            .withDatabaseName(dbName)
            .withUsername(dbUserName)
            .withPassword(dbPwd);


    @ClassRule(order = 2)
    public static FixedHostPortGenericContainer stockServer() {
        postgreSQLContainer.start();
        return (FixedHostPortGenericContainer) new FixedHostPortGenericContainer("stock:1.0-SNAPSHOT")
                .withFixedExposedPort(8080, 8080)
                .withEnv(Map.of("DB", postgreSQLContainer.getJdbcUrl()
                                .replace("localhost", "mydb"),
                        "DB_USER", postgreSQLContainer.getUsername(),
                        "DB_PWD", postgreSQLContainer.getPassword()))
                .withExposedPorts(8080)
                .withAccessToHost(true)
                .withExtraHost("mydb", "host-gateway");
    }


    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Test
    public void testFunctionality() throws Exception {
        var gson = new Gson();
        StockClient client = new StockClient("localhost:8080/stock/");

        mockMvc.perform(get("/user/register")
                        .param("id", "1"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/user/add-money")
                .param("id", "1")
                .param("amount", "200")).andExpect(status().isOk());
        var userInfo = mockMvc.perform(get("/user/?id=1"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertEquals(gson.fromJson(userInfo, UserInfo.class), new UserInfo(1, 200));

        client.create("YNDX", 100, 100);
        mockMvc.perform(get("/user/buy")
                        .param("id", "1")
                        .param("stock", "YNDX")
                        .param("amount", "1"))
                .andExpect(status().isOk());
        assertEquals(new Stock("YNDX", 100, 99), client.getStock("YNDX"));
        var result = mockMvc.perform(get("/user/stocks").param("id", "1"))
                .andExpect(status().isOk()).andReturn();
        List<UserStock> userStocks = gson.fromJson(result.getResponse().getContentAsString(), arrayUserStockType);
        assertEquals(userStocks, List.of(new UserStock(1, "YNDX", 1, 100)));
        userInfo = mockMvc.perform(get("/user/?id=1"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertEquals(gson.fromJson(userInfo, UserInfo.class), new UserInfo(1, 200));

        // change price and sell yndx
        client.create("YNDX", 300, 300);
        mockMvc.perform(get("/user/sell?id=1&stock=YNDX&amount=1")).andExpect(status().isOk());
        userInfo = mockMvc.perform(get("/user/?id=1"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertEquals(gson.fromJson(userInfo, UserInfo.class), new UserInfo(1, 400));

        result = mockMvc.perform(get("/user/stocks?id=1"))
                .andExpect(status().isOk()).andReturn();
        userStocks = gson.fromJson(result.getResponse().getContentAsString(), arrayUserStockType);
        assertEquals(userStocks, List.of());
    }

    @Test
    public void testBadRequests() throws Exception {
        mockMvc.perform(get("/user/add-money")
                .param("id", "2")
                .param("amount", "200")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/user/buy")
                        .param("id", "2")
                        .param("stock", "YNDX")
                        .param("amount", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testBadRequestsWithStock() throws Exception {
        mockMvc.perform(get("/user/register/?id=3")).andExpect(status().isOk());
        mockMvc.perform(get("/user/buy?stock=NOT_FOUND&id=3&amount=1")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/user/buy?stock=YNDX&id=56&amount=1")).andExpect(status().isBadRequest());

        mockMvc.perform(get("/user/sell?stock=YNDX&id=3&amount=1")).andExpect(status().isBadRequest());

    }
}
