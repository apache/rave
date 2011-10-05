package org.apache.rave.portal.repository;

import org.apache.rave.portal.model.Authority;
import org.apache.rave.portal.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 *
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaAuthorityRepositoryTest {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private AuthorityRepository repository;

    @Autowired
    private UserRepository userRepository;

    private static final Long VALID_ID = 1L;

    @Test
    public void getById_validId() {
        final Authority authority = repository.get(VALID_ID);
        assertNotNull(authority);
        assertEquals(VALID_ID, authority.getEntityId());
    }

    @Test
    public void getByAuthorityName() {
        String authorityName = "administrator";
        Authority authority = repository.getByAuthority(authorityName);
        assertNotNull(authority);
        assertEquals(authorityName, authority.getAuthority());
        assertTrue(authority.getUsers().isEmpty());
    }

    @Test
    public void getUsersByAuthorityName() {
        String authorityName = "administrator";
        Authority authority = repository.getByAuthority(authorityName);
        assertNotNull(authority);
        assertEquals(authorityName, authority.getAuthority());
        assertTrue(authority.getUsers().isEmpty());

        User newUser = new User();
        newUser.setUsername("adminuser");
        newUser.addAuthority(authority);
        newUser = userRepository.save(newUser);
        assertEquals(authority, newUser.getAuthorities().iterator().next());

        authority = repository.getByAuthority(authorityName);
        assertEquals(1, authority.getUsers().size());
    }

    @Test
    public void addOrDeleteAuthorityDoesNotAffectUser() {
        final String authorityName = "guest";
        Authority authority = new Authority(authorityName);
        User user = userRepository.get(1L);

        Assert.assertNotNull("User is not null", user);
        Assert.assertTrue("User has no authorities", user.getAuthorities().isEmpty());
        assertNull("No authority guest", repository.getByAuthority(authorityName));

        user.addAuthority(authority);
        user = userRepository.save(user);

        assertNull("Persisting a user does not persist an unknown Authority", repository.getByAuthority(authorityName));
        repository.save(authority);

        Assert.assertEquals("Found authority", authorityName, user.getAuthorities().iterator().next().getAuthority());
        Assert.assertNotNull("New authority: guest", authority);

        repository.delete(authority);
        assertNull("No authority guest", repository.getByAuthority(authorityName));

        user = userRepository.get(1L);
        Assert.assertNotNull("User should not be deleted after removing an authority", user);
        Assert.assertTrue("User should have no authorities", user.getAuthorities().isEmpty());
    }
}
