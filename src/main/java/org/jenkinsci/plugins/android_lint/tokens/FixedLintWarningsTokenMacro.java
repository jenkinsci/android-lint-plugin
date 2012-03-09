package org.jenkinsci.plugins.android_lint.tokens;

import hudson.plugins.analysis.tokens.AbstractFixedAnnotationsTokenMacro;

import org.jenkinsci.plugins.android_lint.LintMavenResultAction;
import org.jenkinsci.plugins.android_lint.LintResultAction;

/** Provides a token that evaluates to the number of fixed Android Lint warnings. */
// @Extension(optional = true)
public class FixedLintWarningsTokenMacro extends AbstractFixedAnnotationsTokenMacro {

    @SuppressWarnings("unchecked")
    public FixedLintWarningsTokenMacro() {
        super("ANDROID_LINT_FIXED", LintResultAction.class, LintMavenResultAction.class);
    }

}
