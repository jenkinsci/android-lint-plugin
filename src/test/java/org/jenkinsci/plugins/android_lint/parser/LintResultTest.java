package org.jenkinsci.plugins.android_lint.parser;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.plugins.analysis.core.FilesParser;
import hudson.plugins.analysis.core.ParserResult;
import hudson.plugins.analysis.util.model.Priority;
import org.jenkinsci.plugins.android_lint.LintResult;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.ExtractResourceSCM;
import org.jvnet.hudson.test.JenkinsRule;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

public class LintResultTest {

    @Rule
    public JenkinsRule jenkins = new JenkinsRule();

    @Test
    public void testResultDuplicates() throws ExecutionException, InterruptedException, IOException {
        // Create a project with a Lint results file in its workspace.
        FreeStyleProject project = jenkins.createFreeStyleProject();
        project.setScm(new ExtractResourceSCM(getClass().getResource("duplicates.zip")));

        // Create a build and parse the Lint file.
        FreeStyleBuild build = project.scheduleBuild2(0).get();
        FilesParser lintCollector = new FilesParser("android-lint", "lint-results_r23.xml", new LintParser(""), false, false);
        ParserResult parserResult = build.getWorkspace().act(lintCollector);
        LintResult result = new LintResult(build, "UTF-8", parserResult);

        // Assert stuff.
        assertEquals(1, parserResult.getNumberOfAnnotations());
        assertEquals(0, parserResult.getNumberOfAnnotations(Priority.HIGH));
        assertEquals(1, parserResult.getNumberOfAnnotations(Priority.NORMAL));
        assertEquals(0, parserResult.getNumberOfAnnotations(Priority.LOW));

        assertEquals(1, result.getNumberOfAnnotations());
    }

}
