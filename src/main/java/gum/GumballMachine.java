package gum;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class GumballMachine {
    @Inject
    private Provider<Gum> gumProvider;
    public Gum dispense() {
        return gumProvider.get();
    }
}
