package domainapp.fixture;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import domainapp.dom.impl.CustomerRespository;
import domainapp.dom.impl.HelloWorldObjects;

public class HelloWorldObjectFixtures extends FixtureScript {
	@Override
	protected void execute(ExecutionContext executionContext) {


		worlds.create("John");
		worlds.create("Jack");
		worlds.create("Jill");
		
		customers.create("Matthew", "Gardner");
		customers.create("Peter", "Gardner");
	}
	
	@javax.inject.Inject
	HelloWorldObjects worlds;
	
	
	@javax.inject.Inject
	CustomerRespository customers;
}
