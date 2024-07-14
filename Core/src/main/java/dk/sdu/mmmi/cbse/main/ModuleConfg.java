package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import org.springframework.context.annotation.Bean;

import java.security.Provider;
import java.util.List;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class ModuleConfg {

    @Bean
    public Game game() {
        return new Game(
                entityProcessingServiceList(),
                postEntityProcessingServiceList(),
                gamePluginServiceList());
    }

    @Bean
    public List<IEntityProcessingService> entityProcessingServiceList() {
        return ServiceLoader.load(IEntityProcessingService.class).stream()
                .map(ServiceLoader.Provider::get).collect(toList());
    }

    @Bean
    public List<IPostEntityProcessingService> postEntityProcessingServiceList() {
        return ServiceLoader.load(IPostEntityProcessingService.class).stream()
                .map(ServiceLoader.Provider::get).collect(toList());
    }

    @Bean
    public List<IGamePluginService> gamePluginServiceList() {
        return ServiceLoader.load(IGamePluginService.class).stream()
                .map(ServiceLoader.Provider::get).collect(toList());
    }
}
