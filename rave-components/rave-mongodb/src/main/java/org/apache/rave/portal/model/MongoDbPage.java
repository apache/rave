package org.apache.rave.portal.model;

import org.apache.rave.portal.model.impl.PageImpl;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.apache.rave.portal.repository.UserRepository;

public class MongoDbPage extends PageImpl {

    private UserRepository userRepository;
    private PageLayoutRepository pageLayoutRepository;

    private Long ownerId;
    private String pageLayoutCode;

    public MongoDbPage() {}

    public MongoDbPage(PageLayoutRepository pageLayoutRepository, UserRepository userRepository) {
        this.pageLayoutRepository = pageLayoutRepository;
        this.userRepository = userRepository;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getPageLayoutCode() {
        return pageLayoutCode;
    }

    public void setPageLayoutCode(String pageLayoutCode) {
        this.pageLayoutCode = pageLayoutCode;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public PageLayoutRepository getPageLayoutRepository() {
        return pageLayoutRepository;
    }

    public void setPageLayoutRepository(PageLayoutRepository pageLayoutRepository) {
        this.pageLayoutRepository = pageLayoutRepository;
    }

    @Override
    public User getOwner() {
        User owner = super.getOwner();
        if(owner == null) {
            owner = userRepository.get(ownerId);
            super.setOwner(owner);
        }
        return owner;
    }

    @Override
    public PageLayout getPageLayout() {
        PageLayout layout = super.getPageLayout();
        if(layout == null) {
            layout = pageLayoutRepository.getByPageLayoutCode(pageLayoutCode);
            super.setPageLayout(layout);
        }
        return layout;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Page)) return false;
        Page that = (Page) o;
        return !(this.getId() != null ? !this.getId().equals(that.getId()) : that.getId() != null);

    }

    @Override
    public int hashCode() {
        return this.getId() != null ? this.getId().hashCode() : 0;
    }
}
