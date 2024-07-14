package dk.sdu.mmmi.cbse.common.services.entityprocessing;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

import java.util.Collection;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class EntityProcessingServiceProvider {

    private static EntityProcessingServiceProvider serviceProvider;
    private ServiceLoader<IEntityProcessingService> loader;

    private EntityProcessingServiceProvider() {
        loader = ServiceLoader.load(IEntityProcessingService.class);
    }

    public static synchronized EntityProcessingServiceProvider getInstance() {
        if (serviceProvider == null) {
            serviceProvider = new EntityProcessingServiceProvider();
        }
        return serviceProvider;
    }

    public void process(GameData gameData, World world) {
        for (IEntityProcessingService entityProcessorService : getEntityProcessingServices()) {
            entityProcessorService.process(gameData, world);
        }
    }

    private Collection<? extends IEntityProcessingService> getEntityProcessingServices() {
        return loader.stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
