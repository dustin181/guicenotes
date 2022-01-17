package chef;

import com.google.inject.Inject;
import chef.services.FortuneService;
import com.google.inject.name.Named;

public class Chef {

    private final FortuneService fortuneService;

    @Inject
    public Chef(@Named("quoteFortuneService") FortuneService fortuneService) {
        this.fortuneService = fortuneService;
    }
    public void makeFortuneCookie() {
        new FortuneCookie(fortuneService.randomFortune());
    }
}