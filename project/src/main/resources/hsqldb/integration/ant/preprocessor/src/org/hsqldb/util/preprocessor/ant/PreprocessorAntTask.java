/* Copyright (c) 2001-2022, The HSQL Development Group
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the HSQL Development Group nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL HSQL DEVELOPMENT GROUP, HSQLDB.ORG,
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package org.hsqldb.util.preprocessor.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.hsqldb.util.preprocessor.IResolver;
import org.hsqldb.util.preprocessor.Option;
import org.hsqldb.util.preprocessor.Preprocessor;

/**
 * <p>Provides a facility for invoking the Preprocessor from ANT.</p>
 * <p><b>Example ANT target:</b></p><pre>{@code <target
 *     name="preprocess"
 *     depends="build-preprocessor">
 *
 *     <taskdef
 *         name="preprocess"
 *         classname="org.hsqldb.util.preprocessor.PreprocessorAntTask"
 *         classpath="${preprocessor.class.path}"/>
 *
 *     <preprocess
 *         srcdir="${src.dir}"
 *         targetdir="${target.dir}"
 *         altext=".pre"
 *         backup="true"
 *         encoding="UTF8"
 *         filter="true"
 *         indent="false"
 *         symbols="java_version=${java.version}, jdbc_version=${jdbc.version}"
 *         testonly="false"
 *         verbose="true"
 *         if="${preprocess.if}"
 *         unless="${preprocess.unless}">
 *     </preprocess>
 *
 * </target>}</pre><p><b>Task Attributes</b></p>
 * <table summary="Task Attributes" border="1" cellspacing="1">
 *     <thead>
 *         <tr><th>Name</th><th>Description</th></tr>
 *     </thead>
 *     <tbody>
 *     <tr>
 *         <td><b>srcdir</b> (required)</td>
 *         <td><b>file</b> - directory under which input files are located</td>
 *     </tr>
 *     <tr>
 *         <td><b>targetdir</b> (required)</td>
 *         <td><b>file</b> - directory under which output files are to be written</td>
 *     </tr>
 *     <tr>
 *         <td><b>altext</b> (optional)</td>
 *         <td><b>string</b> - alternate extension to use for output file names. <br>
 *         If needed, leading dot should be provided</td>
 *     </tr>
 *     <tr>
 *         <td><b>backup</b> (optional - default: <b>false</b>)</td>
 *         <td><b>boolean</b> - whether to back up pre-existing target files. <br>
 *         When true, pre-existing target files are preserved by renaming with
 *         postfix "~"</td>
 *     </tr>
 *     <tr>
 *         <td><b>encoding</b> (optional)</td>
 *         <td><b>string</b> - the encoding with which to read and write file content.<br>
 *         If specified, must be a valid Java encoding identifier, such as "UTF8". <br>
 *         When unspecified, the default Java platformn encoding is used.</td>
 *     </tr>
 *     <tr>
 *         <td><b>filter</b> (optional - default: <b>false</b>)</td>
 *         <td><b>boolean</b> - whether to exclude directive lines from output.</td>
 *     </tr>
 *     <tr>
 *         <td><b>indent</b> (optional - default: <b>false</b>)</td>
 *         <td><b>boolean</b> - whether to indent directive lines in output.</td>
 *     </tr>
 *     <tr>
 *         <td><b>symbols</b> (optional)</td>
 *         <td><b>string</b> - CSV list of preprocessor symbols to predefine. <br>
 *         When specified, each list element <i>must</i> be of the form: <br>
 *         {@code IDENT (ASSIGN? (STRING | NUMBER | IDENT) )?}<br>
 *         Note that forward assignments are illegal. <br>
 *         See {@link Preprocessor Preprocessor} for details</td>
 *     </tr>
 *     <tr>
 *         <td><b>testonly</b> (optional - default: <b>false</b>)</td>
 *         <td><b>boolean</b> - whether to omit writing output files.</td>
 *     </tr>
 *     <tr>
 *         <td><b>verbose</b> (optional - default: <b>false</b>)</td>
 *         <td><b>boolean</b> - whether to log detailed information.</td>
 *     </tr>
 *     </tbody>
 * </table>
 *
 * @author Campbell Burnet (campbell-burnet@users dot sourceforge.net)
 * @version 2.6.2
 * @since 1.8.1
 */
public class PreprocessorAntTask extends MatchingTask {

    private String ifExpr;
    private String unlessExpr;
    private File   sourceDir;
    private File   targetDir;
    private String defines;
    private String altExt;
    private String encoding;
    private int    options = Option.INDENT;

    public void init() {
        super.init();
    }

    public void setSrcdir(final File value) {
        sourceDir = value;
    }

    public void setTargetdir(final File value) {
        targetDir = value;
    }

    public void setSymbols(final String value) {
        defines = value;
    }

    public void setVerbose(final boolean verbose) {
        options = Option.setVerbose(options, verbose);
    }

    public void setBackup(final boolean backup) {
        options = Option.setBackup(options, backup);
    }

    public void setIndent(final boolean indent) {
        options = Option.setIndent(options, indent);
    }

    public void setTestonly(final boolean testOnly) {
        options = Option.setTestOnly(options, testOnly);
    }

    public void setFilter(final boolean filter) {
        options = Option.setFilter(options, filter);
    }

    public void setAltext(final String ext) {
        this.altExt = ext;
    }

    public void setEncoding(final String encoding) {
          this.encoding = encoding;
    }

    public void setIf(final String expr) {
        this.ifExpr = expr;
    }

    public void setUnless(final String expr) {
        this.unlessExpr = expr;
    }

    public boolean isActive() {
        return (this.ifExpr == null
                || getProject().getProperty(this.ifExpr) != null
                || this.unlessExpr == null
                || getProject().getProperty(this.unlessExpr) == null);
    }

    public void execute() throws BuildException {

        if (!isActive()) {
            return;
        }

        checkSourceDir();
        checkTargetDir();

        this.sourceDir = getProject().resolveFile("" + this.sourceDir);

        IResolver resolver = new AntResolver(getProject());
        String[]  files    = getFiles();

        log("Preprocessing " + files.length + " file(s)");

        try {
            Preprocessor.preprocessBatch(this.sourceDir, this.targetDir, files,
                    this.altExt, this.encoding, this.options, this.defines,
                    resolver);
        } catch (Exception ex) {
            ex.printStackTrace();

            throw new BuildException("Preprocessing failed: " + ex,
                    ex);
        }
    }

    private String[] getFiles() {
        return getDirectoryScanner(sourceDir).getIncludedFiles();
    }
	
	private void checkSourceDir() throws BuildException {
        if (this.sourceDir == null) {
            throw new BuildException("Source directory is required.");
        }
    }

    private void checkTargetDir() throws BuildException {
        if (targetDir == null) {
            throw new BuildException("Target directory is required.");
        }
    }
}
