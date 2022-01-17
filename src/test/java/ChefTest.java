import chef.Chef;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChefTest {
    @Test
    public void makeFortuneCookie() {
        FortuneServiceMock mock = new FortuneServiceMock();
        Chef chef = new Chef(mock);
        chef.makeFortuneCookie();
        assertTrue(mock.calledOnce());
    }
}
