//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import org.gradle.api.Plugin;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.groovy.scripts.BasicScript;
import org.gradle.groovy.scripts.ScriptSource;
import org.gradle.groovy.scripts.TextResourceScriptSource;
import org.gradle.internal.resource.StringTextResource;
import org.gradle.util.GradleVersion;

public class TripleoElijahJavaConventionsPlugin implements Plugin<ProjectInternal> {
    private static final String MIN_SUPPORTED_GRADLE_VERSION = "5.0";

    public TripleoElijahJavaConventionsPlugin() {
    }

    public void apply(ProjectInternal target) {
        assertSupportedByCurrentGradleVersion();

        try {
            Class<? extends BasicScript> pluginsBlockClass = Class.forName("cp_precompiled_TripleoElijahJavaConventions").asSubclass(BasicScript.class);
            BasicScript pluginsBlockScript = (BasicScript)pluginsBlockClass.getDeclaredConstructor().newInstance();
            pluginsBlockScript.setScriptSource(scriptSource(pluginsBlockClass));
            pluginsBlockScript.init(target, target.getServices());
            pluginsBlockScript.run();
            target.getPluginManager().apply("java-library");
            target.getPluginManager().apply("maven-publish");
            Class<? extends BasicScript> precompiledScriptClass = Class.forName("precompiled_TripleoElijahJavaConventions").asSubclass(BasicScript.class);
            BasicScript script = (BasicScript)precompiledScriptClass.getDeclaredConstructor().newInstance();
            script.setScriptSource(scriptSource(precompiledScriptClass));
            script.init(target, target.getServices());
            script.run();
        } catch (Exception var6) {
            throw new RuntimeException(var6);
        }
    }

    private static ScriptSource scriptSource(Class<?> scriptClass) {
        return new TextResourceScriptSource(new StringTextResource(scriptClass.getSimpleName(), ""));
    }

    private static void assertSupportedByCurrentGradleVersion() {
        if (GradleVersion.current().getBaseVersion().compareTo(GradleVersion.version("5.0")) < 0) {
            throw new RuntimeException("Precompiled Groovy script plugins require Gradle 5.0 or higher");
        }
    }
}
