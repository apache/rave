package org.apache.rave.portal.model;

import org.apache.rave.portal.model.impl.PageUserImpl;
import org.apache.rave.portal.repository.UserRepository;

public class MongoDbPageUser extends PageUserImpl {

    private UserRepository userRepository;

    private Long userId;

    public MongoDbPageUser() {}

    public MongoDbPageUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser() {
        User user = super.getUser();
        if(user == null) {
            user = userRepository.get(userId);
            super.setUser(user);
        }
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageUser)) return false;
        PageUser that = (PageUser) o;
        return !(this.getId() != null ? !this.getId().equals(that.getId()) : that.getId() != null);

    }

    @Override
    public int hashCode() {
        return this.getId() != null ? this.getId().hashCode() : 0;
    }
}
