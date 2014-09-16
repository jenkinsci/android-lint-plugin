package org.jenkinsci.plugins.android_lint;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.plugins.analysis.views.WarningsCountColumn;
import hudson.views.ListViewColumnDescriptor;

public class LintColumn extends WarningsCountColumn<LintProjectAction> {
    @DataBoundConstructor
    public LintColumn() {
        super();
    }

    @Override
    protected Class<LintProjectAction> getProjectAction() {
        return LintProjectAction.class;
    }

    @Override
    public String getColumnCaption() {
        return Messages.AndroidLint_Warnings_ColumnHeader();
    }

    @Extension
    public static class ColumnDescriptor extends ListViewColumnDescriptor {
        @Override
        public boolean shownByDefault() {
            return false;
        }

        @Override
        public String getDisplayName() {
            return Messages.AndroidLint_Warnings_Column();
        }
    }
}
