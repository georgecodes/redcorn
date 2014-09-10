# Refactoring of BeanDefinition to use a wholly up-front model

BeanDefinition has:

* ConstructorModel - models what constructors the bean *actually has*
* InjectableArgsModel - one for constructor arguments, one for property injection
* all dependencies resolved through the ReferenceResolutionContext interface