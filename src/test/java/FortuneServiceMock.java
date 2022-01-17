import chef.services.FortuneService;

public class FortuneServiceMock implements FortuneService {
    private int invocationCount;
    public String randomFortune() {
        invocationCount++;
        return "MOCK";
    }
    public boolean calledOnce() {
        return invocationCount == 1;
    }

}
