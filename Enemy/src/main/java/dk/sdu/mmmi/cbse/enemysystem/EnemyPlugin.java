package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.gameplugin.IGamePluginService;

public class EnemyPlugin implements IGamePluginService {

    private Entity enemy;

    @Override
    public void start(GameData gameData, World world) {
        enemy = createEnemyShip(gameData);
        world.addEntity(enemy);
        gameData.setEnemyLife(enemy.getLife());
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(enemy);
    }

    private Entity createEnemyShip(GameData gameData) {
        Entity enemyShip = new Enemy();
        enemyShip.setPolygonCoordinates(
                -10, -11,
                15, 0,
                -10, 11,
                -4, 0
        );
        enemyShip.setX((double) gameData.getDisplayHeight() / 1.5);
        enemyShip.setY((double) gameData.getDisplayWidth() / 1.5);
        enemyShip.setLife(5);
        return enemyShip;
    }
}