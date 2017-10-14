package domainapp.dom.impl;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;
import org.joda.time.LocalDateTime;

import com.google.common.collect.Ordering;

import lombok.AccessLevel;

@MemberGroupLayout(
	     columnSpans={3,3,0,6},
	     left={"General", "Address"},
	     middle="Detail"
	 )

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "helloworldisis" )
@javax.jdo.annotations.DatastoreIdentity(strategy = IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column ="version")

@javax.jdo.annotations.Unique(name="Customer_name_UNQ", members = {"firstName","lastName"})
@DomainObject(auditing = Auditing.ENABLED)
@DomainObjectLayout()  // trigger events etc.
@lombok.RequiredArgsConstructor(staticName = "create")
@lombok.Getter @lombok.Setter
public class Customer  implements Comparable<Customer>{


    @javax.jdo.annotations.Column(allowsNull = "false", length = 20)
    @lombok.NonNull
    @MemberOrder(sequence="1")
	private String firstName;
    

    @javax.jdo.annotations.Column(allowsNull = "false", length = 20)
    @lombok.NonNull
    @MemberOrder(sequence="2")
	private String lastName;
    

    @javax.jdo.annotations.Column(allowsNull = "true", length = 20)
    @Property(editing = Editing.ENABLED)
    @MemberOrder(name="Address", sequence="3")
	private String street;
    

    @javax.jdo.annotations.Column(allowsNull = "true", length = 15)
    @Property(editing = Editing.ENABLED)
    @MemberOrder(name="Address", sequence="4")
	private String city;
    

    @javax.jdo.annotations.Column(allowsNull = "true", length = 8)
    @Property(editing = Editing.ENABLED)
    @MemberOrder(name="Address", sequence="5")
	private String postCode;
	
    @CollectionLayout(defaultView="table")
    @Persistent(mappedBy = "orderCustomer", dependentElement = "false")
    private SortedSet<Order> childrenOrders = new TreeSet<>(); 
    
  
    public String title() {
    	return "This is it";
    }
    
    @Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
    public Customer placeOrder(int amount, LocalDateTime delivery) {
    	
    	LocalDateTime placed = clockService.nowAsLocalDateTime();
    	Order order = new Order();
    	order.setPlaced(placed);
    	order.setDelivery(delivery);
    	order.setTotalAmount(amount);
    	
    	order.setOrderCustomer(this);
    	childrenOrders.add(order);
    	
    	messageService.informUser(String.format("'%s' placed Order", order));
    	repositoryService.persist(order);]
    	return this;
    	
    }
    
    
    @Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.removeAndFlush(this);
    }
    
   
    public void setStreet(String street) {
    	System.out.println("setStreet:" + street);
    	this.street = street;
    }
    
    public String validateStreet(String street) {
    	System.out.println("validateStreet:" + street);
    	return (street == null || street.contains("Sassafrass")) ? "Bad street name" : null;
    }
    
    @Override
    public int compareTo(final Customer other) {
        return Ordering.natural().onResultOf(Customer::getFirstName).compare(this, other);
    }
    
    //region > injected services
    @javax.inject.Inject
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    RepositoryService repositoryService;

    @javax.inject.Inject
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    TitleService titleService;

    @javax.inject.Inject
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    MessageService messageService;
    
    @javax.inject.Inject
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    ClockService clockService;
    //endregion
}
