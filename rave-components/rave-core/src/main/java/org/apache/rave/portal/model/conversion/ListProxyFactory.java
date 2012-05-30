package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creates a {@link java.util.List} proxy that converts the added object to an entity
 */
@Component
public class ListProxyFactory {

    //Workaround for inability to access spring context without a lot of machinery
    //Will allow for a getInstance method to be called.  this is needed because the
    //Converters are all Spring beans with their own dependencies.
    private static ListProxyFactory instance;

    Map<Class<?>, Converter> converterMap;

    @Autowired
    public ListProxyFactory(List<ModelConverter> converters) {
        converterMap = new HashMap<Class<?>, Converter>();
        for(ModelConverter converter : converters) {
            converterMap.put(converter.getSourceType(), converter);
        }
        instance = this;
    }

    @SuppressWarnings("unchecked")
    public <E, T extends E> List createProxyList(Class<E> targetType, List<T> underlyingList) {
        return (List) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class<?>[]{List.class},
                new ListInvocationHandler<E, T>(converterMap.get(targetType), underlyingList));
    }

    public static ListProxyFactory getInstance() {
        if(instance == null) {
            throw new IllegalStateException("Proxy factory not yet set by the Spring context");
        }
        return instance;
    }


    public static class ListInvocationHandler<S,T> implements InvocationHandler {

        public static final String ADD_METHOD = "add";
        public static final String SET_METHOD = "set";
        public static final String ADD_ALL_METHOD = "addAll";

        private Converter<S, T> converter;
        private List<T> underlying;
        public ListInvocationHandler(Converter<S, T> converter, List<T> underlying) {
            this.converter = converter;
            this.underlying = underlying;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Object invoke(Object proxy, Method method, Object[] parameters) throws Throwable {
            String methodName = method.getName();
            int convertIndex = method.getParameterTypes().length == 1 ? 0 : 1;
            if(ADD_METHOD.equals(methodName) || SET_METHOD.equals(methodName)) {
                parameters[convertIndex] = converter.convert((S)parameters[convertIndex]);
            } else if(ADD_ALL_METHOD.equals(methodName)) {
                convertAll((List)parameters[convertIndex]);
            }
            return method.invoke(underlying, parameters);
        }

        @SuppressWarnings("unchecked")
        private void convertAll(List<S> parameter) {
            for(int i=0; i<parameter.size(); i++) {
                parameter.set(i, (S)converter.convert(parameter.get(i)));
            }
        }
    }
}
