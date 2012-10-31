package org.apache.rave.portal.web.tag;

import org.apache.rave.portal.model.Person;
import org.apache.rave.portal.repository.PersonRepository;

import javax.servlet.jsp.JspException;

/**
 * Adds the requested person to the requested scope for access via JSPs
 */
public class PersonTag extends ModelContextTag<Person, PersonRepository> {

    public PersonTag() {
        super(PersonRepository.class);
    }

    @Override
    protected void handleNoVar(Person result) throws JspException {
        writeString(result.getUsername());
    }
}
