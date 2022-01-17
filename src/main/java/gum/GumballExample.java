package gum;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class GumballExample {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new GumModule());
        GumballMachine m = injector.getInstance(GumballMachine.class);
        System.out.println(m.dispense());
        System.out.println(m.dispense());
    }
}
