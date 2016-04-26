package org.jenkinsci.plugins.android_lint.dashboard;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.plugins.analysis.core.AbstractProjectAction;
import hudson.plugins.analysis.dashboard.AbstractWarningsTablePortlet;
import hudson.plugins.view.dashboard.DashboardPortlet;

import org.jenkinsci.plugins.android_lint.LintDescriptor;
import org.jenkinsci.plugins.android_lint.LintProjectAction;
import org.jenkinsci.plugins.android_lint.Messages;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * A portlet that shows a table with the number of warnings in a job.
 *
 * @author Ulli Hafner
 */
public class WarningsTablePortlet extends AbstractWarningsTablePortlet {

    /**
     * @see #WarningsTablePortlet(String, boolean)
     */
    @Deprecated
    public WarningsTablePortlet(final String name) {
        super(name);
    }

    /**
     * @param name                        The display name of the portlet.
     * @param canHideZeroWarningsProjects Whether to hide projects from this portlet if they have no warnings.
     */
    @DataBoundConstructor
    public WarningsTablePortlet(final String name, final boolean canHideZeroWarningsProjects) {
        super(name, canHideZeroWarningsProjects);
    }

    /** {@inheritDoc} */
    @Override
    protected Class<? extends AbstractProjectAction<?>> getAction() {
        return LintProjectAction.class;
    }

    /**
     * Extension point registration.
     *
     * @author Ulli Hafner
     */
    @Extension(optional = true)
    public static class WarningsPerJobDescriptor extends Descriptor<DashboardPortlet> {
        @Override
        public String getDisplayName() {
            return Messages.Portlet_WarningsTable();
        }
    }
}

