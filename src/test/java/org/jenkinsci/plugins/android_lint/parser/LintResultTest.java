package org.jenkinsci.plugins.android_lint.parser;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.plugins.analysis.core.FilesParser;
import org.jenkinsci.plugins.android_lint.LintResult;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.SingleFileSCM;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

public class LintResultTest {

    @Rule
    public JenkinsRule jenkins = new JenkinsRule();

    @Test
    public void testFoo() throws ExecutionException, InterruptedException, IOException {
        // Create a project with a lint results file in its workspace
        FreeStyleProject project = jenkins.createFreeStyleProject();
        project.setScm(new SingleFileSCM("lint-results.xml", getClass().getResource("lint-results_r21.xml")));

        // Create a build and parse the lint file
        FreeStyleBuild build = project.scheduleBuild2(0).get();
        FilesParser lintCollector = new FilesParser("android-lint", "*", new LintParser("UTF-8"), false, false);
        LintResult result = new LintResult(build, "UTF-8", build.getWorkspace().act(lintCollector));

        // Assert stuff
        assertEquals(42, result.getNumberOfAnnotations());
    }

}
