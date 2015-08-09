package org.jenkinsci.plugins.android_lint;

import hudson.FilePath;
import hudson.model.Result;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.assertEquals;

public class LintWorkflowTest {

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule();

    /**
     * Run a workflow job using {@link LintPublisher} and check for success.
     */
    @Test
    public void lintPublisherWorkflowStep() throws Exception {
        WorkflowJob job = jenkinsRule.jenkins.createProject(WorkflowJob.class, "lintPublisherWorkflowStep");
        FilePath workspace = jenkinsRule.jenkins.getWorkspaceFor(job);
        FilePath report = workspace.child("target").child("lint-results.xml");
        report.copyFrom(getClass().getResourceAsStream("./parser/lint-results_r20.xml"));
        job.setDefinition(new CpsFlowDefinition(""
                        + "node {\n"
                        + "  step([$class: 'LintPublisher'])\n"
                        + "}\n", true)
        );
        jenkinsRule.assertBuildStatusSuccess(job.scheduleBuild2(0));
        LintResultAction result = job.getLastBuild().getAction(LintResultAction.class);
        assertEquals(4, result.getResult().getAnnotations().size());
    }

    /**
     * Run a workflow job using {@link LintPublisher} with a failing threshold of 0, so the given example file
     * "/org/jenkinsci/plugins/android_lint/parser/lint-results_r20.xml" will make the build to fail.
     */
    @Test
    public void lintPublisherWorkflowStepSetLimits() throws Exception {
        WorkflowJob job = jenkinsRule.jenkins.createProject(WorkflowJob.class, "lintPublisherWorkflowStepSetLimits");
        FilePath workspace = jenkinsRule.jenkins.getWorkspaceFor(job);
        FilePath report = workspace.child("target").child("lint-results.xml");
        report.copyFrom(getClass().getResourceAsStream("./parser/lint-results_r20.xml"));
        job.setDefinition(new CpsFlowDefinition(""
                        + "node {\n"
                        + "  step([$class: 'LintPublisher', pattern: '**/lint-results.xml', failedTotalAll: '0', usePreviousBuildAsReference: false])\n"
                        + "}\n", true)
        );
        jenkinsRule.assertBuildStatus(Result.FAILURE, job.scheduleBuild2(0).get());
        LintResultAction result = job.getLastBuild().getAction(LintResultAction.class);
        assertEquals(4, result.getResult().getAnnotations().size());
    }

    /**
     * Run a workflow job using {@link LintPublisher} with a unstable threshold of 0, so the given example file
     * "/org/jenkinsci/plugins/android_lint/parser/lint-results_r20.xml" will make the build to fail.
     */
    @Test
    public void lintPublisherWorkflowStepFailure() throws Exception {
        WorkflowJob job = jenkinsRule.jenkins.createProject(WorkflowJob.class, "lintPublisherWorkflowStepFailure");
        FilePath workspace = jenkinsRule.jenkins.getWorkspaceFor(job);
        FilePath report = workspace.child("target").child("lint-results.xml");
        report.copyFrom(getClass().getResourceAsStream("./parser/lint-results_r20.xml"));
        job.setDefinition(new CpsFlowDefinition(""
                        + "node {\n"
                        + "  step([$class: 'LintPublisher', pattern: '**/lint-results.xml', unstableTotalAll: '0', usePreviousBuildAsReference: false])\n"
                        + "}\n")
        );
        jenkinsRule.assertBuildStatus(Result.UNSTABLE, job.scheduleBuild2(0).get());
        LintResultAction result = job.getLastBuild().getAction(LintResultAction.class);
        assertEquals(4, result.getResult().getAnnotations().size());
    }

    /**
     * This test aims to verify that a non-default pattern works fine.
     */
    @Test
    public void lintPublisherWorkflowStepUsingNotDefaultPattern() throws Exception {
        WorkflowJob job = jenkinsRule.jenkins.createProject(WorkflowJob.class, "lintPublisherWorkflowStepFailure");
        FilePath workspace = jenkinsRule.jenkins.getWorkspaceFor(job);
        FilePath report = workspace.child("output").child("my-lint-results.xml");
        report.copyFrom(getClass().getResourceAsStream("./parser/lint-results_r20.xml"));
        job.setDefinition(new CpsFlowDefinition(""
                        + "node {\n"
                        + "  step([$class: 'LintPublisher', pattern: 'output/my-lint-results.xml'])\n"
                        + "}\n")
        );
        jenkinsRule.assertBuildStatus(Result.SUCCESS, job.scheduleBuild2(0).get());
        LintResultAction result = job.getLastBuild().getAction(LintResultAction.class);
        assertEquals(4, result.getResult().getAnnotations().size());
    }
}

