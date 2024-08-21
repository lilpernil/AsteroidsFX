package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

public interface IEntityProcessingService {

    /**
     * the IEntityProcessingService has the responsibility for processing
     * the game logic for entities in each loop of the game
     *
     * @param gameData the data for the current loop of the game
     * @param world contains entities for each loop of the game
     * @precondition neither of gameData and world must be
     * null and world contains entities
     * @postcondition depending on the implementation, the entities might
     * be updated
     */
    void process(GameData gameData, World world);
}