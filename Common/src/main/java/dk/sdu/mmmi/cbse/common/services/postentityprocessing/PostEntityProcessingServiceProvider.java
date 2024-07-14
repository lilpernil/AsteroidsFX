package dk.sdu.mmmi.cbse.common.services.postentityprocessing;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

import java.util.Collection;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class PostEntityProcessingServiceProvider {

    private static PostEntityProcessingServiceProvider serviceProvider;
    private ServiceLoader<IPostEntityProcessingService> loader;

    private PostEntityProcessingServiceProvider() {
        loader = ServiceLoader.load(IPostEntityProcessingService.class);
    }

    public static synchronized PostEntityProcessingServiceProvider getInstance() {
        if (serviceProvider == null) {
            serviceProvider = new PostEntityProcessingServiceProvider();
        }
        return serviceProvider;
    }

    public void process(GameData gameData, World world) {
        for (IPostEntityProcessingService postEntityProcessorService : getPostEntityProcessingServices()) {
            postEntityProcessorService.process(gameData, world);
        }
    }

    private Collection<? extends IPostEntityProcessingService> getPostEntityProcessingServices() {
        return loader.stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
