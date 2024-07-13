package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.Random;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class EnemyControlSystem implements IEntityProcessingService {

    private Random random = new Random();

    @Override
    public void process(GameData gameData, World world) {
        for (Entity enemy : world.getEntities(Enemy.class)) {
            if (random.nextInt(100) > 52) {
                enemy.setRotation(enemy.getRotation() - 5);
            } else {
                enemy.setRotation(enemy.getRotation() + 5);
            }

            double changeX = Math.cos(Math.toRadians(enemy.getRotation()));
            double changeY = Math.sin(Math.toRadians(enemy.getRotation()));
            enemy.setX(enemy.getX() + changeX / 2);
            enemy.setY(enemy.getY() + changeY / 2);

            if (random.nextInt(100) > 98) {
                for (BulletSPI bulletSPI : getBulletSPIs()) {
                    Entity newBullet = bulletSPI.createBullet(enemy, gameData);
                    world.addEntity(newBullet);
                }
            }
            if (enemy.getX() < 0) {
                enemy.setX(1);
                enemy.setRotation(enemy.getRotation() - 5);
            }
            if (enemy.getX() > gameData.getDisplayWidth()) {
                enemy.setX(gameData.getDisplayWidth() - 1);
                enemy.setRotation(enemy.getRotation() - 5);
            }
            if (enemy.getY() < 0) {
                enemy.setY(1);
                enemy.setRotation(enemy.getRotation() + 5);
            }
            if (enemy.getY() > gameData.getDisplayHeight()) {
                enemy.setY(gameData.getDisplayHeight() - 1);
                enemy.setRotation(enemy.getRotation() + 5);
            }
            if (enemy.isHit()) {
                enemy.setLife(enemy.getLife() - 1);
                enemy.setHit(false);
                gameData.setEnemyLife(enemy.getLife());
            }
            if (enemy.getLife() == 0) {
                world.removeEntity(enemy);
                gameData.setEnemyLife(0);
            }
        }
    }

    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
