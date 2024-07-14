package dk.sdu.mmmi.cbse.common.services.gameplugin;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

import java.util.Collection;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class GamePluginServiceProvider {

    private static GamePluginServiceProvider serviceProvider;
    private ServiceLoader<IGamePluginService> loader;

    private GamePluginServiceProvider() {
        loader = ServiceLoader.load(IGamePluginService.class);
    }

    public static synchronized GamePluginServiceProvider getInstance() {
        if (serviceProvider == null) {
            serviceProvider = new GamePluginServiceProvider();
        }
        return serviceProvider;
    }

    public void start(GameData gameData, World world) {
        for (IGamePluginService iGamePlugin : getPluginServices()) {
            iGamePlugin.start(gameData, world);
        }
    }

    private Collection<? extends IGamePluginService> getPluginServices() {
        return loader.stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
