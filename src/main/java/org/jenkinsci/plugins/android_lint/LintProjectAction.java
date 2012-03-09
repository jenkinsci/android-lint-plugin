package org.jenkinsci.plugins.android_lint;

import hudson.model.AbstractProject;
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
     * @param project The project that owns this action.
     */
    public LintProjectAction(final AbstractProject<?, ?> project) {
        this(project, LintResultAction.class);
    }

    /**
     * Instantiates a new {@link LintProjectAction}.
     *
     * @param project The project that owns this action.
     * @param type The result action type.
     */
    public LintProjectAction(final AbstractProject<?, ?> project,
            final Class<? extends ResultAction<LintResult>> type) {
        super(project, type, new LintDescriptor());
    }

    public String getDisplayName() {
        return Messages.AndroidLint_ProjectAction_Name();
    }

    @Override
    public String getTrendName() {
        return Messages.AndroidLint_ProjectAction_TrendName();
    }

}
