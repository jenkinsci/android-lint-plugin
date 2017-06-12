package org.jenkinsci.plugins.android_lint;

import hudson.model.Job;
import hudson.plugins.analysis.core.ResultAction;
import hudson.plugins.analysis.core.AbstractProjectAction;

/**
 * Entry point to visualize the trend graph in the project screen.
 * <p>
 * Drawing of the graph is delegated to the associated {@link ResultAction}.
 */
public class LintProjectAction extends AbstractProjectAction<ResultAction<LintResult>> {

    /**
     * Instantiates a new {@link LintProjectAction}.
     *
     * @param job The job that owns this action.
     */
    public LintProjectAction(final Job<?, ?> job) {
        this(job, LintResultAction.class);
    }

    /**
     * Instantiates a new {@link LintProjectAction}.
     *
     * @param job The job that owns this action.
     * @param type The result action type.
     */
    public LintProjectAction(final Job<?, ?> job,
            final Class<? extends ResultAction<LintResult>> type) {
        super(job, type, Messages._AndroidLint_ProjectAction_Name(), Messages._AndroidLint_ProjectAction_TrendName(), LintDescriptor.PLUGIN_NAME, LintDescriptor.ACTION_ICON, LintDescriptor.RESULT_URL);
    }
}
