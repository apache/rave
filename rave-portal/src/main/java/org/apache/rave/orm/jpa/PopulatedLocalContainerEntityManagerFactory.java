package org.apache.rave.orm.jpa;

import org.apache.rave.jdbc.util.DataSourcePopulator;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;

/**
 * @author mfranklin
 *         Date: 4/21/11
 *         Time: 2:57 PM
 */
public class PopulatedLocalContainerEntityManagerFactory extends LocalContainerEntityManagerFactoryBean{
    private DataSourcePopulator populator;

    public PopulatedLocalContainerEntityManagerFactory() {
        super();
    }

    public void setPopulator(DataSourcePopulator populator) {
        this.populator = populator;
    }

    @Override
    protected void postProcessEntityManagerFactory(EntityManagerFactory emf, PersistenceUnitInfo pui) {
        //Create an entity manager to force initialization of the context and then populate
        emf.createEntityManager().close();
        populator.initialize(this.getDataSource());
        super.postProcessEntityManagerFactory(emf, pui);
    }
}
