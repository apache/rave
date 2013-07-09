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
package org.apache.rave.service.impl;

import org.apache.rave.model.StaticContent;
import org.apache.rave.service.StaticContentFetcherConsumer;
import org.apache.rave.service.StaticContentFetcherService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DefaultStaticContentFetcherServiceTest {
    private RestTemplate restTemplate;
    private StaticContentFetcherConsumer consumer;

    private final String VALID_URL1 = "http://www.google.com/";
    private final String VALID_URL1_ID = "google";
    private final String VALID_URL1_CONTENT = "Bogus www.google.page";
    private StaticContent VALID_CONTENT_1;

    private final String VALID_URL2 = "http://www.yahoo.com/";
    private final String VALID_URL2_ID = "yahoo";
    private final String VALID_URL2_CONTENT = "Bogus www.yahoo.page";
    private StaticContent VALID_CONTENT_2;

    private final String VALID_URL3 = "http://www.bing.com/";
    private final String VALID_URL3_ID = "bing";
    private final String VALID_URL3_CONTENT = "Bogus www.bing.page with replacement tokens: {token1} && {token2}";
    private final String VALID_URL3_TOKEN_1 = "\\{token1\\}";
    private final String VALID_URL3_TOKEN_2 = "\\{token2\\}";
    private final String VALID_URL3_TOKEN_1_REPLACEMENT = "token-1-value";
    private final String VALID_URL3_TOKEN_2_REPLACEMENT = "token-2-value";
    private final String VALID_URL3_CONTENT_WITH_REPLACEMENTS = VALID_URL3_CONTENT
            .replaceAll(VALID_URL3_TOKEN_1, VALID_URL3_TOKEN_1_REPLACEMENT)
            .replaceAll(VALID_URL3_TOKEN_2, VALID_URL3_TOKEN_2_REPLACEMENT);
    private StaticContent VALID_CONTENT_3;

    @Before
    public void setUp() throws URISyntaxException {
        VALID_CONTENT_1 = new StaticContent(VALID_URL1_ID, new URI(VALID_URL1), null);
        VALID_CONTENT_2 = new StaticContent(VALID_URL2_ID, new URI(VALID_URL2), null);
        VALID_CONTENT_3 = new StaticContent(VALID_URL3_ID, new URI(VALID_URL3), null);

        restTemplate = createNiceMock(RestTemplate.class);
        consumer = createMock(StaticContentFetcherConsumer.class);
    }

    @Test
    public void getContent_validId() throws URISyntaxException {
        DefaultStaticContentFetcherService service = new DefaultStaticContentFetcherService(restTemplate, Arrays.asList(VALID_CONTENT_1, VALID_CONTENT_2, VALID_CONTENT_3));
        expectAllRestTemplateGetForObject();
        replay(restTemplate);

        //Initially all content will be blank
        assertBlankInitialContent(service);

        service.refreshAll();

        //Now that the content has been refreshed we should get a valid value
        assertThat(service.getContent(VALID_URL1_ID), is(VALID_URL1_CONTENT));

        verify(restTemplate);
    }

    @Test
    public void getContent_validIdsWithReplacementTokens() throws URISyntaxException {
        //Setup our third content object to use the replacement feature and test for it
        Map<String, String> tokenReplacements = new HashMap<String, String>();
        tokenReplacements.put(VALID_URL3_TOKEN_1, VALID_URL3_TOKEN_1_REPLACEMENT);
        tokenReplacements.put(VALID_URL3_TOKEN_2, VALID_URL3_TOKEN_2_REPLACEMENT);
        VALID_CONTENT_3 = new StaticContent(VALID_URL3_ID, new URI(VALID_URL3), tokenReplacements);

        expectAllRestTemplateGetForObject();
        replay(restTemplate);

        DefaultStaticContentFetcherService service = new DefaultStaticContentFetcherService(restTemplate, Arrays.asList(VALID_CONTENT_1, VALID_CONTENT_2, VALID_CONTENT_3));

        service.refreshAll();

        assertThat(service.getContent(VALID_URL1_ID), is(VALID_URL1_CONTENT));
        assertThat(service.getContent(VALID_URL2_ID), is(VALID_URL2_CONTENT));
        assertThat(service.getContent(VALID_URL3_ID), is(VALID_URL3_CONTENT_WITH_REPLACEMENTS));

        verify(restTemplate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getContent_invalidId() {
        DefaultStaticContentFetcherService service = new DefaultStaticContentFetcherService(restTemplate, Arrays.asList(VALID_CONTENT_1, VALID_CONTENT_2, VALID_CONTENT_3));
        service.getContent("INVALID");
    }

    @Test
    public void refreshAll() throws URISyntaxException {
        DefaultStaticContentFetcherService service = new DefaultStaticContentFetcherService(restTemplate, Arrays.asList(VALID_CONTENT_1, VALID_CONTENT_2, VALID_CONTENT_3));
        service.registerConsumer(consumer);
        assertBlankInitialContent(service);

        expectAllRestTemplateGetForObject();
        consumer.notify(VALID_CONTENT_1.getId());
        expectLastCall();
        consumer.notify(VALID_CONTENT_2.getId());
        // simulate an exception in the middle of the notification loop to ensure it continues processing
        expectLastCall().andThrow(new RuntimeException("boom"));
        consumer.notify(VALID_CONTENT_3.getId());
        expectLastCall();
        replay(restTemplate, consumer);

        service.refreshAll();

        //Now that the content has been refreshed we should get a valid value
        assertThat(service.getContent(VALID_URL1_ID), is(VALID_URL1_CONTENT));
        assertThat(service.getContent(VALID_URL2_ID), is(VALID_URL2_CONTENT));
        assertThat(service.getContent(VALID_URL3_ID), is(VALID_URL3_CONTENT));
        verify(restTemplate, consumer);
    }

    @Test
    public void refresh_invalidKey() throws URISyntaxException {
        DefaultStaticContentFetcherService service = new DefaultStaticContentFetcherService(restTemplate, Arrays.asList(VALID_CONTENT_1, VALID_CONTENT_2, VALID_CONTENT_3));

        assertBlankInitialContent(service);

        expectAllRestTemplateGetForObject();
        replay(restTemplate);

        service.refresh("INVALID_CACHE_KEY");

        //Now that the content has been refreshed we should get a valid value
        assertThat(service.getContent(VALID_URL1_ID), is(""));
        assertThat(service.getContent(VALID_URL2_ID), is(""));
        assertThat(service.getContent(VALID_URL3_ID), is(""));
    }

    @Test
    public void refresh_validKey() throws URISyntaxException {
        DefaultStaticContentFetcherService service = new DefaultStaticContentFetcherService(restTemplate, Arrays.asList(VALID_CONTENT_1, VALID_CONTENT_2, VALID_CONTENT_3));

        assertBlankInitialContent(service);

        expectAllRestTemplateGetForObject();
        replay(restTemplate);

        service.refresh(VALID_URL2_ID);

        //Now that the content has been refreshed we should get a valid value
        assertThat(service.getContent(VALID_URL1_ID), is(""));
        assertThat(service.getContent(VALID_URL2_ID), is(VALID_URL2_CONTENT));
        assertThat(service.getContent(VALID_URL3_ID), is(""));
    }

    @Test
    public void refresh_fetchErrorExistingValidContentPreserved() throws URISyntaxException {
        //Create the fetcher, refresh the content and then validate that we have the expected content
        DefaultStaticContentFetcherService service = new DefaultStaticContentFetcherService(restTemplate, Arrays.asList(VALID_CONTENT_1, VALID_CONTENT_2, VALID_CONTENT_3));

        expectAllRestTemplateGetForObject();
        replay(restTemplate);

        service.refreshAll();

        assertThat(service.getContent(VALID_URL1_ID), is(VALID_URL1_CONTENT));
        assertThat(service.getContent(VALID_URL2_ID), is(VALID_URL2_CONTENT));
        assertThat(service.getContent(VALID_URL3_ID), is(VALID_URL3_CONTENT));

        //Now create a new RestTemplate mock and have it return new content for two of the items and throw an exception
        //for the third -- in this case the two items should have the new content but the third with the exception should
        //retain the old good cached content.
        String newContent = "new content from refresh";
        restTemplate = createNiceMock(RestTemplate.class);
        expect(restTemplate.getForObject(new URI(VALID_URL1), String.class)).andReturn(newContent);
        expect(restTemplate.getForObject(new URI(VALID_URL2), String.class)).andThrow(new RestClientException("Boom"));
        expect(restTemplate.getForObject(new URI(VALID_URL3), String.class)).andReturn(newContent);
        replay(restTemplate);
        //Use reflection to stuff the new RestTemplate instance into our existing content fetcher instance
        ReflectionTestUtils.setField(service, "restTemplate", restTemplate);

        service.refreshAll();

        assertThat(service.getContent(VALID_URL1_ID), is(newContent));
        assertThat(service.getContent(VALID_URL2_ID), is(VALID_URL2_CONTENT));
        assertThat(service.getContent(VALID_URL3_ID), is(newContent));
    }

    @Test
    public void registerAndUnregisterConsumer() {
        DefaultStaticContentFetcherService service = new DefaultStaticContentFetcherService(restTemplate, Arrays.asList(VALID_CONTENT_1, VALID_CONTENT_2, VALID_CONTENT_3));
        assertThat(getInternalConsumersSet(service).contains(consumer), is(false));
        service.registerConsumer(consumer);
        assertThat(getInternalConsumersSet(service).contains(consumer), is(true));
        service.unregisterConsumer(consumer);
        assertThat(getInternalConsumersSet(service).contains(consumer), is(false));
    }

    private void expectAllRestTemplateGetForObject() throws URISyntaxException {
        expect(restTemplate.getForObject(new URI(VALID_URL1), String.class)).andReturn(VALID_URL1_CONTENT);
        expect(restTemplate.getForObject(new URI(VALID_URL2), String.class)).andReturn(VALID_URL2_CONTENT);
        expect(restTemplate.getForObject(new URI(VALID_URL3), String.class)).andReturn(VALID_URL3_CONTENT);
    }

    private void assertBlankInitialContent(DefaultStaticContentFetcherService service) {
        assertThat(service.getContent(VALID_URL1_ID), is(""));
        assertThat(service.getContent(VALID_URL2_ID), is(""));
        assertThat(service.getContent(VALID_URL3_ID), is(""));
    }

    private Set<StaticContentFetcherConsumer> getInternalConsumersSet(StaticContentFetcherService service) {
        return (Set<StaticContentFetcherConsumer>) ReflectionTestUtils.getField(service, "staticContentFetcherConsumers");
    }
}
