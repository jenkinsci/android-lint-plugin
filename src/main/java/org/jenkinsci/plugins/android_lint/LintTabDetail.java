package org.jenkinsci.plugins.android_lint;

import hudson.model.AbstractBuild;
import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.analysis.views.DetailFactory;
import hudson.plugins.analysis.views.TabDetail;

import java.util.Collection;

/** Allows overriding of specific tab views for Lint results. */
public class LintTabDetail extends TabDetail {

    private static final long serialVersionUID = -6238979032162559455L;

    /**
     * @param owner Build owning the result on display.
     * @param detailFactory Detail factory to create details.
     * @param annotations Annotations being shown.
     * @param url URL to render the content of this tab.
     * @param defaultEncoding Default encoding to use when reading files.
     */
    public LintTabDetail(final AbstractBuild<?, ?> owner, final DetailFactory detailFactory,
            final Collection<FileAnnotation> annotations, final String url,
            final String defaultEncoding) {
        super(owner, detailFactory, annotations, url, defaultEncoding);
    }

    @Override
    public String getDetails() {
        return "details.jelly";
    }

    @Override
    public String getWarnings() {
        return "warnings.jelly";
    }

}
