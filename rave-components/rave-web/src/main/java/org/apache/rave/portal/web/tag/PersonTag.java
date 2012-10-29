package org.apache.rave.portal.web.tag;

import org.apache.rave.portal.model.Person;
import org.apache.rave.portal.repository.PersonRepository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * Adds the requested person to the requested scope for access via JSPs
 */
public class PersonTag extends AbstractContextAwareSingletonBeanDependentTag<PersonRepository> {

    private String var;

    private String id;
    private int scope;
    private boolean scopeSpecified;

    public PersonTag() {
        super(PersonRepository.class);
        this.init();
    }

    @Override
    public int doStartTag() throws JspException {
        return super.doStartTag();
    }

    @Override
    public int doEndTag() throws JspException {
        this.updateContext(this.getBean().get(id));
        this.init();
        return EVAL_PAGE;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getScope() {
        return scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
        scopeSpecified = true;
    }

    private void init() {
        this.var = null;
        this.id = null;
        this.scope = PageContext.PAGE_SCOPE;
    }

    private void updateContext(Person result) throws JspException {
        if (var != null) {
            /*
             * Store the result, letting an IllegalArgumentException
             * propagate back if the scope is invalid (e.g., if an attempt
             * is made to store something in the session without any
             * HttpSession existing).
             */
            if (result != null) {
                pageContext.setAttribute(var, result, scope);
            } else {
                if (scopeSpecified) {
                    pageContext.removeAttribute(var, scope);
                } else {
                    pageContext.removeAttribute(var);
                }
            }
        } else {
            writeString(result.getUsername());
        }
    }
}
