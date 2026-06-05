import org.junit.jupiter.api.*;
import services.PlatformService;
import users.RegularUser;
import problems.AlgorithmicProblem;
import problems.Problem;
import matches.Match;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlatformServiceTest {

    private static PlatformService service;

    @BeforeAll
    static void setUp() {
        // Ensure DB is initialized
        config.DatabaseConfiguration.getInstance();
        service = new PlatformService();
    }

    @Test
    @Order(1)
    void testAddAndFindUser() {
        RegularUser user = new RegularUser(999, "TestUser");
        service.addUser(user);
        
        RegularUser found = (RegularUser) service.findUserById(999);
        assertNotNull(found, "User should be found in database");
        assertEquals("TestUser", found.getUsername());
    }

    @Test
    @Order(2)
    void testAddAndFindProblem() {
        AlgorithmicProblem prob = new AlgorithmicProblem(888, "Test Problem", "Easy", 1.0);
        service.addProblem(prob);
        
        AlgorithmicProblem found = (AlgorithmicProblem) service.findProblemById(888);
        assertNotNull(found, "Problem should be found in database");
        assertEquals("Test Problem", found.getTitle());
    }

    @Test
    @Order(3)
    void testMatchCreationAndELO() {
        RegularUser p1 = (RegularUser) service.findUserById(999);
        RegularUser p2 = new RegularUser(998, "Opponent");
        service.addUser(p2);
        
        AlgorithmicProblem prob = (AlgorithmicProblem) service.findProblemById(888);
        
        int initialEloP1 = p1.getEloRating();
        
        Match match = service.createMatch(777, p1, p2, prob);
        assertNotNull(match);
        
        // Submit correct code for P1 to finish match and win
        service.submitCode(444, match, p1, "test code", true);
        
        assertTrue(match.isFinished(), "Match should be finished");
        assertEquals(initialEloP1 + 25, p1.getEloRating(), "Winner should gain 25 ELO");
    }
}
