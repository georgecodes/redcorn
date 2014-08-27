# Redcorn - lightweight Java IoC container

## Introduction

Redcorn is an IoC container, written in Java, that aims to be lightweight and have minimal external dependencies. In fact, the only runtime dependency it currently has is on Logback. In this day and age, the case for inversion of control, and dependency injection, needs no explanation, so there isn't really going to be one here. The idea behind the pattern is that code is handed the code it depends on, rather than having to create or find it itself. The idea behind Redcorn is equally simple - you tell the container what classes to manage, and it figures out the relationships between them.

Of course, Redcorn is far from the first such product. The excellent [PicoContainer](http://picocontainer.codehaus.org) for example. Redcorn just happened by half-accident in another project, as is often the way. 

### A quick example

Redcorn doesn't provide any fancy configuration options out of the box - although fitting configuration to it wouldn't be difficult - and everything can be done with plain old Java code. No XML, no YAML, no annotations, just good clean code.

    RedcornContainer container = new ConstructorInjectionRedcornContainer();
    container.register(MyObject.class):
    container.start();
    MyObject obj = container.get(MyObject.class); // that's it
    
Self-explanatory, not that useful.

### Getting hold of Redcorn

Redcorn releases are available in Maven central, so there's no need to configure special repositories. To use Redcorn in your Maven project:

    <dependency>
      <groupId>com.elevenware</groupId>
      <artifactId>redcorn</artifactId>
      <version>0.1.0</version>
    </dependency>
    
To use in your Gradle project:

    compile "com.elevenware:redcorn:0.1.0"

## Constructor injection

### Implicit

    public class BusinessServiceImpl implements BusinessService {
      
        private DataService dataService;
        
        public BusinessService(DataService dataService) {
            this.dataService = dataService;
        }
        
        // other methods
        
    }
    
    public class HibernateDataService implements DataService {
       // methods
    }
    
    RedcornContainer container = new ConstructorInjectionRedcornContainer();
    container.register(BusinessServiceImpl.class);
    container.register(HibernateDataService.class);
    container.start();
    BusinessService service = container.get(BusinessService.class);
    
The container will look at what types it knows about, and look for constructors that take those types, and wire them up automatically. Notice that I provide a concrete class to the container to create, but can look up objects by their interface types.    

### By reference

Sometimes, our needs will be a little more complex. We may have multiple different instances configured, that might appear to belong to more than one dependent object, but don't.

    class FooService {
        public FooService(DataService fooDS) {}
    }
    
    class BarService {
        public BarService DataService barDS) {}
    }
    
    container.register(FooService.class);
    container.register(BarService.class);
    container.register(DataService.class);
    container.register(DataService.class);
 
Which instance of DataService belongs to which other service? This is solvable using...

### Named references
    
    container.register(FooService.class)
             .addConstructorRef("fooDS");
    container.register(BarService.class)
             .addConstructorRef("barDS");
    container.register("fooDS", DataService.class);
    container.register("barDS", DataService.class);
    
Everything fits!

Of course, this wouldn't be much of an issue in the above example, since both instances of DataService seem to be identical.

### Value-type arguments

    container.register(FooService.class)
                 .addConstructorRef("fooDS");
        container.register(BarService.class)
                 .addConstructorRef("barDS");
        container.register("fooDS", DataService.class)
                 .addConstructorArg("jdbc:postgres:foo.data.company.com:fooDB");
        container.register("barDS", DataService.class)
                 ..addConstructorArg("jdbc:postgres:bar.data.company.com:barDB");
    
Redcorn just figures it out.
