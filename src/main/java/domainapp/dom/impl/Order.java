package domainapp.dom.impl;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;
import org.joda.time.LocalDateTime;

import lombok.AccessLevel;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "helloworldisis" )
@javax.jdo.annotations.DatastoreIdentity(strategy = IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column ="version")

@javax.jdo.annotations.Unique(name="Order_name_UNQ", members = {"orderCustomer","totalAmount"})
@DomainObject(auditing = Auditing.ENABLED)
@DomainObjectLayout()  // trigger events etc.

@lombok.Getter @lombok.Setter
public class Order implements Comparable<Order>{
	
	@Column(allowsNull = "false")
	private Customer orderCustomer;
	
    @lombok.NonNull
    @MemberOrder(sequence="1")
    @javax.jdo.annotations.Column(allowsNull = "false")
	private LocalDateTime placed;
    
    @lombok.NonNull
    @MemberOrder(sequence="2")
    @javax.jdo.annotations.Column(allowsNull = "false")
	private LocalDateTime delivery;
    
    @lombok.NonNull
    @MemberOrder(sequence="3")
    @javax.jdo.annotations.Column(allowsNull = "false")
	private int totalAmount ;

	@Override
	public String toString() {
		return "Order [orderCustomer=" + orderCustomer + ", placed=" + placed + ", delivery=" + delivery + ", totalAmount="
				+ totalAmount + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((delivery == null) ? 0 : delivery.hashCode());
		result = prime * result + ((orderCustomer == null) ? 0 : orderCustomer.hashCode());
		result = prime * result + ((placed == null) ? 0 : placed.hashCode());
		result = prime * result + totalAmount;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (delivery == null) {
			if (other.delivery != null)
				return false;
		} else if (!delivery.equals(other.delivery))
			return false;
		if (orderCustomer == null) {
			if (other.orderCustomer != null)
				return false;
		} else if (!orderCustomer.equals(other.orderCustomer))
			return false;
		if (placed == null) {
			if (other.placed != null)
				return false;
		} else if (!placed.equals(other.placed))
			return false;
		if (totalAmount != other.totalAmount)
			return false;
		return true;
	}

	public int compareTo(Order other){
	    return this.hashCode() > other.hashCode() ? 1 : this.hashCode() < other.hashCode() ? -1 : 0;
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
    
    //endregion
}
