package org.jenkinsci.plugins.android_lint.tokens;

import hudson.plugins.analysis.tokens.AbstractResultTokenMacro;

import org.jenkinsci.plugins.android_lint.LintMavenResultAction;
import org.jenkinsci.plugins.android_lint.LintResultAction;

/** Provides a token that evaluates to the Android Lint build result. */
// @Extension(optional = true)
public class LintResultTokenMacro extends AbstractResultTokenMacro {

    @SuppressWarnings("unchecked")
    public LintResultTokenMacro() {
        super("ANDROID_LINT_RESULT", LintResultAction.class, LintMavenResultAction.class);
    }

}
