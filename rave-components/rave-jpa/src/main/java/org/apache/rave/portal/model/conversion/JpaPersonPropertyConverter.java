package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.ModelConverter;
import org.apache.rave.portal.model.JpaPersonProperty;
import org.apache.rave.portal.model.PersonProperty;
import org.springframework.stereotype.Component;

/**
 * Converts an Address to a JpaAddress
 */
@Component
public class JpaPersonPropertyConverter implements ModelConverter<PersonProperty, JpaPersonProperty> {

    @Override
    public Class<PersonProperty> getSourceType() {
        return PersonProperty.class;
    }

    @Override
    public JpaPersonProperty convert(PersonProperty source) {
        return source instanceof JpaPersonProperty ? (JpaPersonProperty) source : createEntity(source);
    }

    private JpaPersonProperty createEntity(PersonProperty source) {
        JpaPersonProperty converted = null;
        if (source != null) {
            converted  = new JpaPersonProperty();
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(PersonProperty source, JpaPersonProperty converted) {
        converted.setId(source.getId());
        converted.setQualifier(source.getQualifier());
        converted.setPrimary(source.getPrimary());
        converted.setType(source.getType());
        converted.setValue(source.getValue());
        converted.setExtendedValue(source.getExtendedValue());
    }
}
