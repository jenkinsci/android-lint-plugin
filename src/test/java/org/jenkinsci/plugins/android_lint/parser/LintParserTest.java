package org.jenkinsci.plugins.android_lint.parser;


import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.analysis.util.model.Priority;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import junit.framework.TestCase;

import org.jenkinsci.plugins.android_lint.Messages;

public class LintParserTest extends TestCase {

    private static final String MODULE_NAME = "test";

    // No explanations should be available for any issues
    public void testParser_pre_r21() throws Exception {
        List<LintAnnotation> annotations = parseResults("lint-results_r20.xml");
        assertEquals(4, annotations.size());

        LintAnnotation a = annotations.get(0);
        assertNull(a.getMessage());
        assertEquals(Priority.HIGH, a.getPriority());
        assertEquals(LintParser.FILENAME_UNKNOWN, a.getFileName());
        assertEquals("UnknownId", a.getType());
        assertUnknownIssue(a);

        a = annotations.get(1);
        assertEquals("Call requires API level 8 (current min is 7): "
                + "android.view.MotionEvent#getActionIndex",
                a.getMessage());
        assertEquals(Priority.HIGH, a.getPriority());
        assertEquals("bin/classes/InputObject.class", a.getFileName());
        assertEquals("NewApi", a.getType());
        assertUnknownIssue(a);

        a = annotations.get(2);
        assertEquals("The &lt;activity&gt; MonitoredActivity is not registered in the manifest",
                a.getMessage());
        assertEquals(Priority.NORMAL, a.getPriority());
        assertEquals("bin/classes/MonitoredActivity.class", a.getFileName());
        assertEquals("Registered", a.getType());
        assertUnknownIssue(a);

        a = annotations.get(3);
        assertEquals(Priority.LOW, a.getPriority());
        assertEquals("Avoid using &quot;px&quot; as units; use &quot;dp&quot; instead",
                a.getMessage());
        assertEquals("res/layout/foo.xml", a.getFileName());
        assertEquals(19, a.getPrimaryLineNumber());
        assertUnknownIssue(a);
    }

    // Explanations and context info should be available from r21
    public void testParser_post_r21() throws Exception {
        List<LintAnnotation> annotations = parseResults("lint-results_r21.xml");
        assertEquals(3, annotations.size());

        LintAnnotation a = annotations.get(0);
        assertEquals("&lt;uses-sdk&gt; tag should specify a target API level (the highest "
                + "verified version; when running on later versions, compatibility behaviors may "
                + "be enabled) with android:targetSdkVersion=&quot;?&quot;", a.getMessage());
        assertEquals(Priority.NORMAL, a.getPriority());
        assertEquals("AndroidManifest.xml", a.getFileName());
        assertEquals(9, a.getPrimaryLineNumber());
        assertEquals("UsesMinSdkAttributes", a.getType());
        assertEquals("Correctness", a.getCategory());
        assertEquals("The manifest should contain a `<uses-sdk>` element which defines the "
                + "minimum API Level required for the application to run, as well as the target "
                + "version (the highest API level you have tested the version for.)",
                a.getExplanation());
        assertEquals("    &lt;uses-sdk android:minSdkVersion=&quot;7&quot; /&gt;", //
                a.getErrorLines().get(0));
        assertEquals("    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~", a.getErrorLines().get(1));

        a = annotations.get(1);
        assertEquals("Found bitmap drawable res/drawable/thumbnail.png in densityless folder",
                a.getMessage());
        assertEquals(Priority.NORMAL, a.getPriority());
        assertEquals("res/drawable/thumbnail.png", a.getFileName());
        assertEquals("IconLocation", a.getType());
        assertEquals("Usability:Icons", a.getCategory());
        assertEquals("The res/drawable folder is intended for density-independent graphics...",
                a.getExplanation());
        assertEquals(0, a.getPrimaryLineNumber());

        a = annotations.get(2);
        assertEquals("Translation 'foo_bar' is missing.", a.getMessage());
        assertEquals(Priority.NORMAL, a.getPriority());
        assertEquals("MissingTranslation", a.getType());
        Location[] locs = a.getLocations().toArray(new Location[0]);
        Arrays.sort(locs, new Comparator<Location>() {
            public int compare(Location a, Location b) {
                return a.getFile().compareTo(b.getFile());
            }
        });
        assertEquals(2, locs.length);
        assertEquals("res/values-en-rGB/strings.xml", locs[0].getFile());
        assertEquals("res/values-fr/strings.xml", locs[1].getFile());
        assertEquals("res/values-de/strings.xml", a.getFileName());
    }

    // Asserts that an issue has the pre-r21 'unknown issue' explanations
    private static void assertUnknownIssue(LintAnnotation a) {
        assertEquals(Messages.AndroidLint_Parser_UnknownCategory(), a.getCategory());
        assertEquals(Messages.AndroidLint_Parser_UnknownExplanation(a.getType()),
                a.getExplanation());
    }

    private List<LintAnnotation> parseResults(String filename) throws InvocationTargetException {
        LintParser parser = new LintParser("UTF-8");
        InputStream stream = getClass().getResourceAsStream(filename);
        List<LintAnnotation> list = new ArrayList<LintAnnotation>();
        for (FileAnnotation a : parser.parse(stream, MODULE_NAME)) {
            assertEquals(MODULE_NAME, a.getModuleName());
            list.add((LintAnnotation) a);
        }
        return list;
    }

}
