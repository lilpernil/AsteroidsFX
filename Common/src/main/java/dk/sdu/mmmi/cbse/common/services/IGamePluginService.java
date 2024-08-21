package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

public interface IGamePluginService {

    /**
     * setting up entities and initializing plugin
     *
     * @param gameData the data for the current loop of the game
     * @param world contains entities for each loop of the game
     * @precondition neither of gameData and world must be
     * null and world contains entities
     * @postcondition world is changed by the plugin and entities
     * are added
     */
    void start(GameData gameData, World world);

    /**
     * removing entities related to the plugin
     *
     * @param gameData the data for the current loop of the game
     * @param world contains entities for each loop of the game
     * @precondition neither of gameData and world must be
     * null and world contains entities
     * @postcondition the entities related to the plugin are removed from world
     */
    void stop(GameData gameData, World world);
}
