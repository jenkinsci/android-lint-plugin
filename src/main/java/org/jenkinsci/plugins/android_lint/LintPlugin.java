package org.jenkinsci.plugins.android_lint;

import hudson.Plugin;
import hudson.plugins.analysis.views.DetailFactory;

/** Hooks up result actions with detail builders at startup. */
public class LintPlugin extends Plugin {

    @Override
    public void start() {
        LintDetailBuilder detailBuilder = new LintDetailBuilder();
        DetailFactory.addDetailBuilder(LintResultAction.class, detailBuilder);
        DetailFactory.addDetailBuilder(LintMavenResultAction.class, detailBuilder);
    }

}
