package org.apache.rave.portal.model.conversion;


import org.apache.rave.model.ModelConverter;

public interface HydratingModelConverter <S,T>  extends ModelConverter<S, T>{
    public void hydrate(T dehydrated);
}
