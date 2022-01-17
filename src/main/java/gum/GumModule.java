package gum;

import com.google.inject.AbstractModule;

public class GumModule extends AbstractModule {

    protected void configure() {
        bind(Gum.class).toProvider(GumProvider.class);
    }
}
