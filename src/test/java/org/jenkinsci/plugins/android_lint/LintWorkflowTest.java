package org.jenkinsci.plugins.android_lint;

import hudson.FilePath;
import hudson.model.Result;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.BuildWatcher;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LintWorkflowTest {

    @ClassRule
    public static BuildWatcher bw = new BuildWatcher();

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule();

    @Test
    public void lintPublisherWorkflowStep() throws Exception {
        runLintWorkflowAndAssertResult("step([$class: 'LintPublisher'])", Result.SUCCESS, 4);
    }

    @Test
    public void lintPublisherWorkflowStepSetLimits() throws Exception {
        runLintWorkflowAndAssertResult(
                "step([$class: 'LintPublisher', pattern: '**/lint-results.xml', failedTotalAll: '0', usePreviousBuildAsReference: false])",
                Result.FAILURE,
                4
        );
    }

    @Test
    public void lintPublisherWorkflowStepFailure() throws Exception {
        runLintWorkflowAndAssertResult(
                "step([$class: 'LintPublisher', pattern: 'target/lint-results.xml', unstableTotalAll: '0', usePreviousBuildAsReference: false])",
                Result.UNSTABLE,
                4
        );
    }

    @Test
    public void lintPublisherWorkflowStepUsingNotDefaultPattern() throws Exception {
        runLintWorkflowAndAssertResult(
                "output/results.xml",
                "step([$class: 'LintPublisher', pattern: 'output/results.xml'])",
                Result.SUCCESS,
                4
        );
    }

    private void runLintWorkflowAndAssertResult(String step, Result expectedResult, int expectedIssueCount) throws Exception {
        runLintWorkflowAndAssertResult("target/lint-results.xml", step, expectedResult, expectedIssueCount);
    }

    private void runLintWorkflowAndAssertResult(String targetPath, String step,
                                                Result expectedResult, int expectedIssueCount) throws Exception {
        // Create a Pipeline job with the given step
        WorkflowJob job = jenkinsRule.createProject(WorkflowJob.class);
        job.setDefinition(new CpsFlowDefinition(""
                + "node {\n"
                + "  " + step + "\n"
                + "}\n", true)
        );

        // Place a Lint results file somewhere in the workspace
        String[] path = targetPath.split("/");
        FilePath workspace = jenkinsRule.jenkins.getWorkspaceFor(job);
        workspace.child(path[0]).child(path[1]).copyFrom(getClass().getResourceAsStream("./parser/lint-results_r20.xml"));

        // Enqueue a build of the project, wait for it to complete, and check the result
        WorkflowRun build = jenkinsRule.assertBuildStatus(expectedResult, job.scheduleBuild2(0));

        // Check that Lint results were added to the build, and the expected number of issues were found
        LintResultAction action = build.getAction(LintResultAction.class);
        assertNotNull(action);
        assertEquals(expectedIssueCount, action.getResult().getAnnotations().size());
    }

}

