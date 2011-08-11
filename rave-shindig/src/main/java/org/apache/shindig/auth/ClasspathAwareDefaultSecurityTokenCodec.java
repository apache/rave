/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.shindig.auth;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.shindig.config.ContainerConfig;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This class will only be here temporarily until we get a patch applied to Shindig to enable loading the token key
 * file from the classpath (which is why it has no test coverage and the weird nested classes).
 *
 * TODO: Remove this class once we have patches in Shindig to load the key file from the classpath.
 */
@Singleton
public class ClasspathAwareDefaultSecurityTokenCodec extends DefaultSecurityTokenCodec {
    public static final String RESOURCE_PREFIX = "classpath:";

    @Inject
    public ClasspathAwareDefaultSecurityTokenCodec(final ContainerConfig config) {
        super(new ContainerConfigWrapper(config));
    }

    private static class ContainerConfigWrapper implements ContainerConfig {
        private ContainerConfig wrappedConfig;

        private ContainerConfigWrapper(ContainerConfig config) {
            this.wrappedConfig = config;
        }

        @Override
        public Collection<String> getContainers() {
            return wrappedConfig.getContainers();
        }

        @Override
        public Map<String, Object> getProperties(String container) {
            return wrappedConfig.getProperties(container);
        }

        @Override
        public Object getProperty(String container, String name) {
            return wrappedConfig.getProperty(container, name);
        }

        @Override
        public String getString(String container, String name) {
            String value = wrappedConfig.getString(container, name);

            //if this is a request for the encryption key file location, see if it should be pulled from the classpath,
            //and, if it should, locate it and convert the resource based reference to a direct filesystem reference
            //since the Shindig BlobCrypterSecurityTokenDecoder only supports loading directly from the filesystem...
            if (BlobCrypterSecurityTokenCodec.SECURITY_TOKEN_KEY_FILE.equalsIgnoreCase(name) && value != null
                    && value.startsWith(RESOURCE_PREFIX)) {
                String resourceName = value.substring(RESOURCE_PREFIX.length());
                URL resource = this.getClass().getClassLoader().getResource(resourceName);
                if (resource != null) {
                    value = resource.getFile();
                }
            }

            return value;
        }

        @Override
        public int getInt(String container, String name) {
            return wrappedConfig.getInt(container, name);
        }

        @Override
        public boolean getBool(String container, String name) {
            return wrappedConfig.getBool(container, name);
        }

        @Override
        public <T> List<T> getList(String container, String name) {
            return wrappedConfig.getList(container, name);
        }

        @Override
        public <T> Map<String, T> getMap(String container, String name) {
            return wrappedConfig.getMap(container, name);
        }

        @Override
        public ContainerConfig.Transaction newTransaction() {
            return wrappedConfig.newTransaction();
        }

        @Override
        public void addConfigObserver(final ConfigObserver observer, boolean notifyNow) {
            wrappedConfig.addConfigObserver(new ConfigObserver() {
                @Override
                public void containersChanged(ContainerConfig config, Collection<String> changed, Collection<String> removed) {
                    if (!(config instanceof ContainerConfigWrapper)) {
                        config = new ContainerConfigWrapper(config);
                    }
                    observer.containersChanged(config, changed, removed);
                }
            }, notifyNow);
        }
    }

    public static class TemporarySecurityTokenGuiceModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(SecurityTokenCodec.class).to(ClasspathAwareDefaultSecurityTokenCodec.class);
        }
    }
}
