//package com.senorpez.projectcars.staticdata;
//
//import org.springframework.hateoas.Identifiable;
//import org.springframework.hateoas.ResourceSupport;
//import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;
//
//import java.util.function.Supplier;
//
//public class APIResourceAssembler<T extends Identifiable<?>, D extends ResourceSupport> extends IdentifiableResourceAssemblerSupport<T, D> {
//    final private Supplier<D> supplier;
//
//    APIResourceAssembler(final Class<?> controllerClass, final Class<D> resourceType, final Supplier<D> supplier) {
//        super(controllerClass, resourceType);
//        this.supplier = supplier;
//    }
//
//    @Override
//    public D toResource(final T entity) {
//        return createResource(entity);
//    }
//
//    D toResource(final T entity, final Object... parameters) {
//        return createResource(entity, parameters);
//    }
//
//    @Override
//    protected D instantiateResource(final T entity) {
//        return supplier.get();
//    }
//}
