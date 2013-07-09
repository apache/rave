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

package org.apache.rave.integrationtests.stories;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.spring.SpringApplicationContextFactory;
import org.jbehave.core.steps.spring.SpringStepsFactory;
import org.jbehave.web.selenium.ContextView;
import org.jbehave.web.selenium.LocalFrameContextView;
import org.jbehave.web.selenium.SeleniumConfiguration;
import org.jbehave.web.selenium.SeleniumContext;
import org.jbehave.web.selenium.SeleniumContextOutput;
import org.jbehave.web.selenium.SeleniumStepMonitor;
import org.springframework.context.ApplicationContext;

import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.web.selenium.WebDriverHtmlOutput.WEB_DRIVER_HTML;

/**
 * Stories class that should be extended for every story
 */
public abstract class PortalStories extends JUnitStories {
  protected PortalStories() {
    CrossReference crossReference = new CrossReference().withJsonOnly().withOutputAfterEachStory(true)
        .excludingStoriesWithNoExecutedScenarios(true);
    ContextView contextView = new LocalFrameContextView().sized(640, 120);
    SeleniumContext seleniumContext = new SeleniumContext();
    SeleniumStepMonitor stepMonitor = new SeleniumStepMonitor(contextView, seleniumContext,
        crossReference.getStepMonitor());
    Format[] formats = new Format[]{new SeleniumContextOutput(seleniumContext), CONSOLE, WEB_DRIVER_HTML};
    StoryReporterBuilder reporterBuilder = new StoryReporterBuilder()
        .withCodeLocation(codeLocationFromClass(this.getClass())).withFailureTrace(true)
        .withFailureTraceCompression(true).withDefaultFormats().withFormats(formats)
        .withCrossReference(crossReference);

    Configuration configuration = new SeleniumConfiguration().useSeleniumContext(seleniumContext)
        .useFailureStrategy(new FailingUponPendingStep())
        .useStoryControls(new StoryControls().doResetStateBeforeScenario(false)).useStepMonitor(stepMonitor)
        .useStoryLoader(new LoadFromClasspath(this.getClass()))
        .useStoryReporterBuilder(reporterBuilder);
    useConfiguration(configuration);

    ApplicationContext context = new SpringApplicationContextFactory("applicationContext-tests.xml").createApplicationContext();
    useStepsFactory(new SpringStepsFactory(configuration, context));
  }

}
