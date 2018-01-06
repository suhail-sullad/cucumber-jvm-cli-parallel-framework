package init;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.context.filesprovider.ConfigFilesProvider;
import org.cfg4j.source.files.FilesConfigurationSource;
import org.cfg4j.source.reload.ReloadStrategy;
import org.cfg4j.source.reload.strategy.PeriodicalReloadStrategy;

public class PropertyLoader {
	public static ConfigurationProvider provider = null;

	public static void init() {

		ClassLoader cl = PropertyLoader.class.getClassLoader();
		String[] propfiles = { "tests.properties", "browser.properties", "report.properties", "mailer.properties" };
		List<Path> path = new ArrayList<>();
		Arrays.stream(propfiles).forEach(file -> {
			try {
				path.add(Paths.get(cl.getResource(file).toURI()));
			} catch (Exception e) {
				System.err.println("File:" + file + " not found.");
			}
		});
		ConfigFilesProvider configFilesProvider = () -> path;
		ConfigurationSource source = new FilesConfigurationSource(configFilesProvider);
		// ReloadStrategy reloadStrategy = new
		// PeriodicalReloadStrategy(10,TimeUnit.SECONDS);
		provider = new ConfigurationProviderBuilder().withConfigurationSource(source).build();
		// .withReloadStrategy(reloadStrategy).build();
	}

}
