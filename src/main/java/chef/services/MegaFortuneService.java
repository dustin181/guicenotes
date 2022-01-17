package chef.services;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MegaFortuneService implements FortuneService {
    private static final List<FortuneService> SERVICES =
            Arrays.asList(
                    new FunnyFortuneService(),
                    new QuoteFortuneService()
            );
    public String randomFortune() {
        int index = new Random().nextInt(SERVICES.size());
        return SERVICES.get(index).randomFortune();
    }
}