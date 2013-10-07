package org.apache.rave.portal.util.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.rave.model.*;
import org.apache.rave.portal.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the DataImporter for the {@link ModelWrapper}
 */
@Transactional
public class ModelWrapperDataExecutor implements DataImporter.Executor<ModelWrapper> {

    //TODO GROUP REPOSITORY
    @Autowired
    private PageLayoutRepository pageLayoutRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WidgetRepository widgetRepository;

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PortalPreferenceRepository portalPreferenceRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PageTemplateRepository pageTemplateRepository;

    @Autowired
    private ActivityStreamsRepository activityStreamsRepository;

    public boolean needsLoading() {
        return widgetRepository.getCountAll() == 0;
    }

    @Transactional
    public void loadData(ModelWrapper wrapper) {
        savePageLayouts(wrapper);
        saveAuthorities(wrapper);
        Map<String, User> usersByOriginalId = saveUsers(wrapper);
        Map<String, Category> categoryByOldId = saveCategories(wrapper, usersByOriginalId);
        Map<String, Widget> widgetsById = saveWidgets(wrapper, usersByOriginalId, categoryByOldId);
        savePages(wrapper, usersByOriginalId, widgetsById);
        savePreferences(wrapper);
        saveTemplates(wrapper, widgetsById);
        saveActivities(wrapper);
    }

    private void saveActivities(ModelWrapper wrapper) {
        if (wrapper.getActivities() != null) {
            for (ActivityStreamsEntry activity : wrapper.getActivities()) {
                activityStreamsRepository.save(activity);
            }
        }
    }

    private void saveTemplates(ModelWrapper wrapper, Map<String, Widget> widgetsById) {
        for (PageTemplate template : wrapper.getPageTemplates()) {
            updateTemplate(widgetsById, template);
            for (PageTemplate sub : template.getSubPageTemplates()) {
                sub.setParentPageTemplate(template);
                updateTemplate(widgetsById, sub);
            }
            pageTemplateRepository.save(template);
        }
    }

    private void updateTemplate(Map<String, Widget> widgetsById, PageTemplate template) {
        setIdViaReflection(template, null);
        for (PageTemplateRegion region : template.getPageTemplateRegions()) {
            setIdViaReflection(region, null);
            region.setPageTemplate(template);
            for (PageTemplateWidget widget : region.getPageTemplateWidgets()) {
                Widget fromDb = widgetsById.get(widget.getWidgetId());
                if (fromDb != null) {
                    widget.setWidgetId(fromDb.getId());
                }
                setIdViaReflection(widget, null);
                widget.setPageTemplateRegion(region);
            }

        }
    }

    private Map<String, Category> saveCategories(ModelWrapper wrapper, Map<String, User> usersByOriginalId) {
        Map<String, Category> categoryByOldId = Maps.newHashMap();
        if (wrapper.getCategories() != null) {
            for (Category category : wrapper.getCategories()) {
                String id = category.getId() == null ? category.getText() : category.getId();
                category.setId(null);
                User lastModified = usersByOriginalId.get(category.getLastModifiedUserId());
                if (lastModified != null) {
                    category.setLastModifiedUserId(lastModified.getId());
                }
                User created = usersByOriginalId.get(category.getCreatedUserId());
                if (created != null) {
                    category.setCreatedUserId(created.getId());
                }
                categoryByOldId.put(id, categoryRepository.save(category));
            }
        }
        return categoryByOldId;
    }

    private void savePreferences(ModelWrapper wrapper) {
        if (wrapper.getPortalPreferences() != null) {
            for (PortalPreference preference : wrapper.getPortalPreferences()) {
                portalPreferenceRepository.save(preference);
            }
        }
    }

    private Map<String, User> saveUsers(ModelWrapper wrapper) {
        Map<String, User> usersByOriginalId = Maps.newHashMap();
        if (wrapper.getUsers() != null) {
            for (User user : wrapper.getUsers()) {
                String id = user.getId();
                user.setId(null);
                usersByOriginalId.put(id, userRepository.save(user));
            }
        }
        return usersByOriginalId;
    }

    private void savePages(ModelWrapper wrapper, Map<String, User> usersByOriginalId, Map<String, Widget> widgetsById) {
        if (wrapper.getPages() != null) {
            for (Page page : wrapper.getPages()) {
                updatePage(usersByOriginalId, widgetsById, page);
                for (Page sub : page.getSubPages()) {
                    sub.setParentPage(page);
                    updatePage(usersByOriginalId, widgetsById, sub);
                }
                pageRepository.save(page);
            }
        }
    }

    private void updatePage(Map<String, User> usersByOriginalId, Map<String, Widget> widgetsById, Page page) {
        for (Region region : page.getRegions()) {
            region.setPage(page);
            setIdViaReflection(region, null);
            for (RegionWidget widget : region.getRegionWidgets()) {
                widget.setRegion(region);
                Widget fromDb = widgetsById.get(widget.getId());
                if (fromDb != null) {
                    widget.setWidgetId(fromDb.getId());
                }
                setIdViaReflection(widget, null);
            }
        }
        for (PageUser user : page.getMembers()) {
            user.setPage(page);
            setIdViaReflection(user, null);
            User fromDb = usersByOriginalId.get(user.getUserId());
            if (fromDb != null) {
                user.setUserId(fromDb.getId());
            }
        }
        User user = usersByOriginalId.get(page.getOwnerId());
        page.setOwnerId(user.getId());
    }

    private Map<String, Widget> saveWidgets(ModelWrapper wrapper, Map<String, User> usersById, Map<String, Category> categoryMap) {
        Map<String, Widget> widgetsByOldId = Maps.newHashMap();
        if (wrapper.getWidgets() != null) {
            for (Widget widget : wrapper.getWidgets()) {
                String id = widget.getId() == null ? widget.getUrl() : widget.getId();
                setIdViaReflection(widget, null);
                User user = usersById.get(widget.getOwnerId());
                if (user != null) {
                    widget.setOwnerId(user.getId());
                }
                List<Category> categories = Lists.newArrayList();
                if (widget.getCategories() != null) {
                    for (Category c : widget.getCategories()) {
                        categories.add(categoryMap.get(c.getId()));
                    }
                    widget.setCategories(categories);
                }
                widgetsByOldId.put(id, widgetRepository.save(widget));
            }
        }
        return widgetsByOldId;
    }

    private void setIdViaReflection(Object target, Object id) {
        Class clazz = target.getClass();
        Method setter = getMethod(clazz, "setId");
        if (setter != null) {
            callMethod(target, setter, id);
        } else {
            setField(target, id, clazz);
        }
    }

    private void setField(Object target, Object id, Class clazz) {
        List<String> potentialFields = Arrays.asList("id", "_id", "id_");
        for (String name : potentialFields) {
            if (findAndSetField(clazz, target, id, name)) return;
        }
        throw new IllegalStateException("Could not match field");
    }

    private boolean findAndSetField(Class clazz, Object target, Object value, String name) {
        Field idField = getField(clazz, name);
        if (idField != null) {
            setField(target, idField, value);
            return true;
        } else {
            return false;
        }
    }

    private Field getField(Class clazz, String match) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(match);
        } catch (NoSuchFieldException e) {
        }
        return field;
    }

    private Method getMethod(Class clazz, String match) {
        Method method = null;
        try {
            method = clazz.getMethod(match, Void.class);
        } catch (NoSuchMethodException e) {
        }
        return method;
    }

    private void callMethod(Object o, Method m, Object... param) {
        try {
            m.invoke(o, param);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to call method", e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("Unable to call method", e);
        }
    }

    private void setField(Object o, Field f, Object val) {
        try {
            f.set(o, val);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to set field", e);
        }
    }

    private void saveAuthorities(ModelWrapper wrapper) {
        if (wrapper.getAuthorities() != null) {
            for (Authority authority : wrapper.getAuthorities()) {
                authorityRepository.save(authority);
            }
        }
    }

    private void savePageLayouts(ModelWrapper wrapper) {
        if (wrapper.getPageLayouts() != null) {
            for (PageLayout layout : wrapper.getPageLayouts()) {
                pageLayoutRepository.save(layout);
            }
        }
    }

    public void setPageLayoutRepository(PageLayoutRepository pageLayoutRepository) {
        this.pageLayoutRepository = pageLayoutRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setWidgetRepository(WidgetRepository widgetRepository) {
        this.widgetRepository = widgetRepository;
    }

    public void setPageRepository(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    public void setAuthorityRepository(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    public void setPortalPreferenceRepository(PortalPreferenceRepository portalPreferenceRepository) {
        this.portalPreferenceRepository = portalPreferenceRepository;
    }

    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void setPageTemplateRepository(PageTemplateRepository pageTemplateRepository) {
        this.pageTemplateRepository = pageTemplateRepository;
    }

    public void setActivityStreamsRepository(ActivityStreamsRepository activityStreamsRepository) {
        this.activityStreamsRepository = activityStreamsRepository;
    }
}

