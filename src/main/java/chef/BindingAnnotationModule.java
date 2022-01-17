package chef;

import chef.services.*;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class BindingAnnotationModule extends AbstractModule {
    protected void configure() {
        bind(FortuneService.class).to(FortuneServiceImpl.class);
        bind(FortuneService.class).annotatedWith(Names.named("megaFortuneService")).to(MegaFortuneService.class);
        bind(FortuneService.class).annotatedWith(Names.named("funnyFortuneService")).to(FunnyFortuneService.class);
        bind(FortuneService.class).annotatedWith(Names.named("quoteFortuneService")).to(QuoteFortuneService.class);
//        bind(FortuneService.class)
//                .annotatedWith(Mega.class)
//                .to(MegaFortuneService.class);
    }
}