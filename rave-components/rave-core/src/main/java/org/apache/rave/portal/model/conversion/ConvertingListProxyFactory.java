package org.apache.rave.portal.model.conversion;

import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Creates a {@link java.util.List} proxy that converts the added object to an entity
 */
@Component
public class ConvertingListProxyFactory {

    @SuppressWarnings("unchecked")
    public static <E, T extends E> List createProxyList(Class<E> targetType, List<T> underlyingList) {
        return (List) Proxy.newProxyInstance(ConvertingListProxyFactory.class.getClassLoader(),
                new Class<?>[]{List.class},
                new ConvertingListInvocationHandler<E, T>(underlyingList, targetType));
    }

    public static class ConvertingListInvocationHandler<S,T> implements InvocationHandler {

        public static final String ADD_METHOD = "add";
        public static final String SET_METHOD = "set";
        public static final String ADD_ALL_METHOD = "addAll";

        private List<T> underlying;
        private Class<S> targetClass;

        public ConvertingListInvocationHandler(List<T> underlying, Class<S> targetClass) {
            this.underlying = underlying;
            this.targetClass = targetClass;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Object invoke(Object proxy, Method method, Object[] parameters) throws Throwable {
            String methodName = method.getName();
            int convertIndex = method.getParameterTypes().length == 1 ? 0 : 1;
            if(ADD_METHOD.equals(methodName) || SET_METHOD.equals(methodName)) {
                parameters[convertIndex] = JpaConverter.getInstance().convert((S)parameters[convertIndex], targetClass);
            } else if(ADD_ALL_METHOD.equals(methodName)) {
                convertAll((List)parameters[convertIndex]);
            }
            return method.invoke(underlying, parameters);
        }

        @SuppressWarnings("unchecked")
        private void convertAll(List<S> parameter) {
            for(int i=0; i<parameter.size(); i++) {
                parameter.set(i, (S)JpaConverter.getInstance().convert(parameter.get(i), targetClass));
            }
        }
    }
}
