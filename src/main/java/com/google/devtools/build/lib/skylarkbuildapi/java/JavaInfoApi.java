// Copyright 2018 The Bazel Authors. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.devtools.build.lib.skylarkbuildapi.java;

import com.google.devtools.build.lib.events.Location;
import com.google.devtools.build.lib.skylarkbuildapi.FileApi;
import com.google.devtools.build.lib.skylarkbuildapi.core.ProviderApi;
import com.google.devtools.build.lib.skylarkbuildapi.core.StructApi;
import com.google.devtools.build.lib.skylarkinterface.Param;
import com.google.devtools.build.lib.skylarkinterface.SkylarkCallable;
import com.google.devtools.build.lib.skylarkinterface.SkylarkConstructor;
import com.google.devtools.build.lib.skylarkinterface.SkylarkModule;
import com.google.devtools.build.lib.skylarkinterface.SkylarkModuleCategory;
import com.google.devtools.build.lib.syntax.Depset;
import com.google.devtools.build.lib.syntax.EvalException;
import com.google.devtools.build.lib.syntax.Sequence;
import com.google.devtools.build.lib.syntax.StarlarkThread;

/** Info object encapsulating all information by java rules. */
@SkylarkModule(
    name = "JavaInfo",
    doc = "A provider encapsulating information about Java and Java-like targets.",
    category = SkylarkModuleCategory.PROVIDER)
public interface JavaInfoApi<FileT extends FileApi> extends StructApi {

  @SkylarkCallable(
      name = "transitive_runtime_jars",
      doc =
          "Returns a transitive set of Jars required on the target's runtime classpath. Returns"
              + " the same as <code><a class=\"anchor\""
              + " href=\"JavaInfo.html#transitive_runtime_deps\">JavaInfo.transitive_runtime_deps"
              + "</a></code> for legacy reasons.",
      structField = true)
  public Depset getTransitiveRuntimeJars();

  @SkylarkCallable(
      name = "transitive_compile_time_jars",
      doc =
          "Returns the transitive set of Jars required to build the target. Returns the same as"
              + " <code><a class=\"anchor\""
              + " href=\"JavaInfo.html#transitive_deps\">JavaInfo.transitive_deps</a></code> for"
              + " legacy reasons.",
      structField = true)
  public Depset getTransitiveCompileTimeJars();

  @SkylarkCallable(
      name = "compile_jars",
      doc =
          "Returns the Jars required by this target directly at compile time. They can be"
              + " interface jars (ijar or hjar), regular jars or both, depending on whether rule"
              + " implementations chose to create interface jars or not.",
      structField = true)
  public Depset getCompileTimeJars();

  @SkylarkCallable(
      name = "full_compile_jars",
      doc =
          "Returns the regular, full compile time Jars required by this target directly. They can"
              + " be <ul><li> the corresponding regular Jars of the interface Jars returned by"
              + " <code><a class=\"anchor\""
              + " href=\"JavaInfo.html#compile_jars\">JavaInfo.compile_jars</a></code></li><li>"
              + " the regular (full) Jars returned by <code><a class=\"anchor\""
              + " href=\"JavaInfo.html#compile_jars\">JavaInfo.compile_jars</a></code></li></ul>"
              + "<p>Note: <code><a class=\"anchor\""
              + " href=\"JavaInfo.html#compile_jars\">JavaInfo.compile_jars</a></code> can return"
              + " a mix of interface Jars and regular Jars.<p>Only use this method if interface"
              + " Jars don't work with your rule set(s) (e.g. some Scala targets) If you're"
              + " working with Java-only targets it's preferable to use interface Jars via"
              + " <code><a class=\"anchor\""
              + " href=\"JavaInfo.html#compile_jars\">JavaInfo.compile_jars</a></code></li>",
      structField = true)
  public Depset getFullCompileTimeJars();

  @SkylarkCallable(
      name = "source_jars",
      doc =
          "Returns a list of Jars with all the source files (including those generated by"
              + " annotations) of the target  itself, i.e. NOT including the sources of the"
              + " transitive dependencies.",
      structField = true)
  public Sequence<FileT> getSourceJars();

  @SkylarkCallable(
      name = "outputs",
      doc = "Returns information about outputs of this Java/Java-like target.",
      structField = true,
      allowReturnNones = true)
  public JavaRuleOutputJarsProviderApi<?> getOutputJars();

  @SkylarkCallable(
      name = "annotation_processing",
      structField = true,
      allowReturnNones = true,
      doc = "Returns information about annotation processing for this Java/Java-like target.")
  public JavaAnnotationProcessingApi<?> getGenJarsProvider();

  @SkylarkCallable(
      name = "compilation_info",
      structField = true,
      allowReturnNones = true,
      doc = "Returns compilation information for this Java/Java-like target.")
  public JavaCompilationInfoProviderApi<?> getCompilationInfoProvider();

  @SkylarkCallable(
      name = "runtime_output_jars",
      doc = "Returns a list of runtime Jars created by this Java/Java-like target.",
      structField = true)
  public Sequence<FileT> getRuntimeOutputJars();

  @SkylarkCallable(
      name = "transitive_deps",
      doc =
          "Returns the transitive set of Jars required to build the target. Returns the same as"
              + " <code><a class=\"anchor\""
              + " href=\"JavaInfo.html#transitive_compile_time_jars\">JavaInfo.transitive_compile_time_jars</a></code>"
              + " for legacy reasons.",
      structField = true)
  public Depset /*<FileT>*/ getTransitiveDeps();

  @SkylarkCallable(
      name = "transitive_runtime_deps",
      doc =
          "Returns the transitive set of Jars required on the target's runtime classpath. Returns"
              + " the same as <code><a class=\"anchor\""
              + " href=\"JavaInfo.html#transitive_runtime_jars\">JavaInfo.transitive_runtime_jars"
              + "</a></code> for legacy reasons.",
      structField = true)
  public Depset /*<FileT>*/ getTransitiveRuntimeDeps();

  @SkylarkCallable(
      name = "transitive_source_jars",
      doc =
          "Returns the Jars containing source files of the current target and all of its"
              + " transitive dependencies.",
      structField = true)
  public Depset /*<FileT>*/ getTransitiveSourceJars();

  @SkylarkCallable(
      name = "transitive_exports",
      structField = true,
      doc = "Returns a set of labels that are being exported from this rule transitively.")
  public Depset /*<Label>*/ getTransitiveExports();

  /** Provider class for {@link JavaInfoApi} objects. */
  @SkylarkModule(name = "Provider", documented = false, doc = "")
  public interface JavaInfoProviderApi extends ProviderApi {

    @SkylarkCallable(
        name = "JavaInfo",
        doc = "The <code>JavaInfo</code> constructor.",
        parameters = {
          @Param(
              name = "output_jar",
              type = FileApi.class,
              named = true,
              doc =
                  "The jar that was created as a result of a compilation "
                      + "(e.g. javac, scalac, etc)."),
          @Param(
              name = "compile_jar",
              type = FileApi.class,
              named = true,
              noneable = true,
              defaultValue = "None",
              doc =
                  "A jar that is added as the compile-time dependency in lieu of "
                      + "<code>output_jar</code>. Typically this is the ijar produced by "
                      + "<code><a class=\"anchor\" href=\"java_common.html#run_ijar\">"
                      + "run_ijar</a></code>. "
                      + "If you cannot use ijar, consider instead using the output of "
                      + "<code><a class=\"anchor\" href=\"java_common.html#stamp_jar\">"
                      + "stamp_ijar</a></code>. If you do not wish to use either, "
                      + "you can simply pass <code>output_jar</code>."),
          @Param(
              name = "source_jar",
              type = FileApi.class,
              named = true,
              noneable = true,
              defaultValue = "None",
              doc =
                  "The source jar that was used to create the output jar. "
                      + "Use <code><a class=\"anchor\" href=\"java_common.html#pack_sources\">"
                      + "pack_sources</a></code> to produce this source jar."),
          @Param(
              name = "neverlink",
              type = Boolean.class,
              named = true,
              defaultValue = "False",
              doc = "If true only use this library for compilation and not at runtime."),
          @Param(
              name = "deps",
              type = Sequence.class,
              generic1 = JavaInfoApi.class,
              named = true,
              defaultValue = "[]",
              doc = "Compile time dependencies that were used to create the output jar."),
          @Param(
              name = "runtime_deps",
              type = Sequence.class,
              generic1 = JavaInfoApi.class,
              named = true,
              defaultValue = "[]",
              doc = "Runtime dependencies that are needed for this library."),
          @Param(
              name = "exports",
              type = Sequence.class,
              generic1 = JavaInfoApi.class,
              named = true,
              defaultValue = "[]",
              doc =
                  "Libraries to make available for users of this library. See also "
                      + "<a class=\"anchor\" href=\"https://docs.bazel.build/versions/"
                      + "master/be/java.html#java_library.exports\">java_library.exports</a>."),
          @Param(
              name = "jdeps",
              type = FileApi.class,
              named = true,
              defaultValue = "None",
              noneable = true,
              doc =
                  "jdeps information for the rule output (if available). This should be a binary"
                      + " proto encoded using the deps.proto protobuf included with Bazel.  If"
                      + " available this file is typically produced by a compiler. IDEs and other"
                      + " tools can use this information for more efficient processing."),
        },
        selfCall = true,
        useLocation = true,
        useStarlarkThread = true)
    @SkylarkConstructor(objectType = JavaInfoApi.class, receiverNameForDoc = "JavaInfo")
    public JavaInfoApi<?> javaInfo(
        FileApi outputJarApi,
        Object compileJarApi,
        Object sourceJarApi,
        Boolean neverlink,
        Sequence<?> deps,
        Sequence<?> runtimeDeps,
        Sequence<?> exports,
        Object jdepsApi,
        Location loc,
        StarlarkThread thread)
        throws EvalException;
  }
}
