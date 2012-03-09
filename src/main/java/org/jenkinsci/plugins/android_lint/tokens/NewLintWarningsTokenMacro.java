package org.jenkinsci.plugins.android_lint.tokens;

import hudson.plugins.analysis.tokens.AbstractNewAnnotationsTokenMacro;

import org.jenkinsci.plugins.android_lint.LintMavenResultAction;
import org.jenkinsci.plugins.android_lint.LintResultAction;

/** Provides a token that evaluates to the number of new Android Lint warnings. */
// @Extension(optional = true)
public class NewLintWarningsTokenMacro extends AbstractNewAnnotationsTokenMacro {

    @SuppressWarnings("unchecked")
    public NewLintWarningsTokenMacro() {
        super("ANDROID_LINT_NEW", LintResultAction.class, LintMavenResultAction.class);
    }

}
