package chef.services;

public class FunnyFortuneService implements FortuneService {
    @Override
    public String randomFortune() {
        return "funny fortune";
    }
}
