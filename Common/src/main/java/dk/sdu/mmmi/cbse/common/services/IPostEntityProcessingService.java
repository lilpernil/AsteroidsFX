package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 *
 * @author jcs
 */
public interface IPostEntityProcessingService {

    /**
     * executing post-processing tasks for entities
     *
     * @param gameData the data for the current loop of the game
     * @param world contains entities for each loop of the game
     * @precondition neither of gameData and world must be
     * null and world contains entities that are updated in the current
     * loop of the game
     * @postcondition entities might be modified based on the
     * post-processing tasks
     */
    void process(GameData gameData, World world);
}
