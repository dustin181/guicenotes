package chef;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class FortuneApplication {
    public static void main(String[] args) {
        Injector i = Guice.createInjector(new BindingAnnotationModule());
        Chef chef = i.getInstance(Chef.class);
        chef.makeFortuneCookie();
    }
}