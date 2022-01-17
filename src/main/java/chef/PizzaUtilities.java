package chef;

public class PizzaUtilities {
    private static final int TIME_TO_PREPARE = 6;
    private static final int MAX_DISTANCE = 20;


    //bad code.  Static dependencies on Geography, PizzaStore, Oven and Invoice.  Impossible to test.
    /*public static Order createOrder(List<PizzaSpec> pizzas, Customer customer) {
        Directions directions = Geography.getDirections(
                PizzaStore.getStoreAddress(), customer.getDeliveryAddress());

        if (directions == null || directions.getLengthInKm() > MAX_DISTANCE) {
            throw new InvalidOrderException("Cannot deliver to , " +
                    customer.getDeliveryAddress());
        }

        int arrivalTime = TIME_TO_PREPARE
                + Oven.getCurrentOven().schedule(TIME_TO_PREPARE, pizzas)
                + directions.estimateTravelTime();

        Invoice invoice = Invoice.create(pizzas, directions.getLengthInKm());
        return new Order(pizzas, invoice, arrivalTime, customer, directions);
    }*/


}

//good - replaces static calls
/*public class PizzaServices {
    public Order createOrder(List<PizzaSpec> pizzas, Customer customer) {
        return chef.PizzaUtilities.createOrder(pizzas, customer);
    }
}*/


//bad
/*class OrderPizzaAction {
    public void order(HttpSession session) {
        Customer customer = session.getCurrentCustomer();
        chef.PizzaUtilities.createOrder(getPizzaSpecs(), customer);
    }
  ...
}*/

//replace above with this
/*
class OrderPizzaAction {
    private final PizzaServices pizzaServices;
    @Inject
    OrderPizzaAction(PizzaServices pizzaServices) {
        this.pizzaServices = pizzaServices;
    }

    public void order(HttpSession session) {
        Customer customer = session.getCurrentCustomer();
        pizzaServices.createOrder(getPizzaSpecs(), customer);
    }
}
*/

//halfway done
/*public class PizzaServices {
    private final Oven currentOven;

    @Inject
    public PizzaServices(Oven currentOven) {
        this.currentOven = currentOven;
    }

    public Order createOrder(List<PizzaSpec> pizzas, Customer customer) {
        Directions directions = Geography.getDirections(
                PizzaStore.getStoreAddress(), customer.getDeliveryAddress());

        if (directions == null || directions.getLengthInKm() > MAX_DISTANCE) {
            throw new InvalidOrderException("Cannot deliver to , " +
                    customer.getDeliveryAddress());
        }

        int arrivalTime = TIME_TO_PREPARE
                + currentOven.schedule(TIME_TO_PREPARE, pizzas)
                + directions.estimateTravelTime();

        Invoice invoice = Invoice.create(pizzas, directions.getLengthInKm());
        return new Order(pizzas, invoice, arrivalTime, customer, directions);
    }
}

class PizzaModule extends AbstractModule {
    protected void configure() {
        requestStaticInjection(OrderPizzaAction.class);
        requestStaticInjection(chef.PizzaUtilities.class);
        bind(Oven.class).toProvider(new Provider() {
            public Oven get() {
                return Oven.getCurrentOven();
            }
        });
    }
}*/


//inject named values
/*public class PizzaServices {
    private final Oven currentOven;
    private final Address storeAddress;

    @Inject
    public PizzaServices(Oven currentOven,
                         @Named("storeAddress") Address storeAddress) {
        this.currentOven = currentOven;
        this.storeAddress = storeAddress;
    }

    public Order createOrder(List<PizzaSpec> pizzas, Customer customer) {
        Directions directions = Geography.getDirections(
                storeAddress, customer.getDeliveryAddress());
    ...
    }
}*/

//bind in module
/*class PizzaModule extends AbstractModule {
    protected void configure() {
    ...
        bind(Address.class)
                .annotatedWith(Names.named("storeAddress"))
                .toInstance(PizzaStore.getStoreAddress());
    }
}*/

//other option is custom annotation
/*@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER})
@BindingAnnotation
public @interface StoreAddress {}*/


//and then this
/*@Inject
public PizzaServices(Oven currentOven,
                     @StoreAddress Address storeAddress) {
    this.currentOven = currentOven;
    this.storeAddress = storeAddress;
}*/


//good to replace GeographyUtility with Geography Service
//public class PizzaServices {
//    private final Oven currentOven;
//    private final Address storeAddress;
//    private final GeographyServices geographyServices;
//
//    @Inject
//    public PizzaServices(Oven currentOven,
//                         @StoreAddress Address storeAddress,
//                         GeographyServices geographyServices) {
//        this.currentOven = currentOven;
//        this.storeAddress = storeAddress;
//        this.geographyServices = geographyServices;
//    }
//
//    public Order createOrder(List<PizzaSpec> pizzas, Customer customer) {
//        Directions directions = geographyServices.getDirections(
//                storeAddress, customer.getDeliveryAddress());
//    ...
//    }
//}


//almost done - need to fix invoice method
/*public class PizzaServices {
    private final Oven currentOven;
    private final Address storeAddress;
    private final GeographyServices geographyServices;

    @Inject
    public PizzaServices(Oven currentOven,
                         @StoreAddress Address storeAddress,
                         GeographyServices geographyServices) {
        this.currentOven = currentOven;
        this.storeAddress = storeAddress;
        this.geographyServices = geographyServices;
    }

    public Order createOrder(List<PizzaSpec> pizzas, Customer customer) {
        Directions directions = geographyServices.getDirections(
                storeAddress, customer.getDeliveryAddress());

        if (directions == null || directions.getLengthInKm() > MAX_DISTANCE) {
            throw new InvalidOrderException("Cannot deliver to , " +
                    customer.getDeliveryAddress());
        }

        int arrivalTime = TIME_TO_PREPARE
                + currentOven.schedule(TIME_TO_PREPARE, pizzas)
                + directions.estimateTravelTime();

        Invoice invoice = Invoice.create(pizzas, directions.getLengthInKm());
        return new Order(pizzas, invoice, arrivalTime, customer, directions);
    }
}*/


//need to rewrite this
/*class Invoice {
    public Invoice(List<PizzaSpec> pizzas, int deliveryDistance,
                   Address storeAddress) {
    }
    static Invoice create(List<PizzaSpec> pizzas, int deliveryDistance) {
        return new Invoice(pizzas, deliveryDistance, PizzaStore.getStoreAddress());
    }
}*/

//new version
/*class Invoice {
  ...
    interface Factory {
        Invoice create(List<PizzaSpec> pizzas, int deliveryDistance);
    }
}*/


//put it in the module
/*Everything bound via .toInstance() is injected by Guice when the Injector is created. That way,
factories and providers can depend on chef.services provided by other modules. Although
constructor-injection is generally preferred, field injection is sufficient in this case.*/
/*Within the factory implementation, I bind a Provider<Address> rather than the address directly. This isn't strictly necessary because
the address is a constant. But in general, Providers should always be used within factories. This ensures
that I always get the correct instance, even if it depends on scope or context. I always use Providers within my implementations of Factory and Provider.*/
/*static class PizzaModule extends AbstractModule {
    protected void configure() {
    ...
        bind(Invoice.Factory.class).toInstance(new Invoice.Factory() {
            @Inject @StoreAddress Provider<Address> storeAddressProvider;
            public Invoice create(List<PizzaSpec> pizzas, int deliveryDistance) {
                return new Invoice(pizzas, deliveryDistance, storeAddressProvider.get());
            }
        });
    }
}*/


//class with factory added
/*
public class PizzaServices {
    private final Oven currentOven;
    private final Address storeAddress;
    private final GeographyServices geographyServices;
    private final Invoice.Factory invoiceFactory;

    @Inject
    public PizzaServices(Oven currentOven,
                         @StoreAddress Address storeAddress,
                         GeographyServices geographyServices,
                         Invoice.Factory invoiceFactory) {
        this.currentOven = currentOven;
        this.storeAddress = storeAddress;
        this.geographyServices = geographyServices;
        this.invoiceFactory = invoiceFactory;
    }

    public Order createOrder(List<PizzaSpec> pizzas, Customer customer) {
    ...
        Invoice invoice = invoiceFactory.create(pizzas, directions.getLengthInKm());
        return new Order(pizzas, invoice, arrivalTime, customer, directions);
    }
}*/

//more concise version of factory
/*static class Invoice {
    public Invoice(List<PizzaSpec> pizzas, int deliveryDistance,
                   @Inject @StoreAddress Address storeAddress) {
    }
  ...
}*/

//bind factory directly to the class it constructs
/*static class PizzaModule extends AbstractModule {
    protected void configure() {
    ...
        bind(Invoice.Factory.class).toFactoryFor(Invoice.class);
    }
}*/
