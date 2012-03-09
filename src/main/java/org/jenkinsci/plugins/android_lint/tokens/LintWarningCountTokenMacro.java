package org.jenkinsci.plugins.android_lint.tokens;

import hudson.plugins.analysis.tokens.AbstractAnnotationsCountTokenMacro;

import org.jenkinsci.plugins.android_lint.LintMavenResultAction;
import org.jenkinsci.plugins.android_lint.LintResultAction;

/** Provides a token that evaluates to the number of Android Lint warnings. */
// @Extension(optional = true)
public class LintWarningCountTokenMacro extends AbstractAnnotationsCountTokenMacro {

    @SuppressWarnings("unchecked")
    public LintWarningCountTokenMacro() {
        super("ANDROID_LINT_COUNT", LintResultAction.class, LintMavenResultAction.class);
    }

}
