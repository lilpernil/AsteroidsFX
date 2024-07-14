package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.entityprocessing.IEntityProcessingService;

public class BulletControlSystem implements IEntityProcessingService, BulletSPI {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity bullet : world.getEntities(Bullet.class)) {

            if (bullet.getX() < 0) {
                world.removeEntity(bullet);
            }

            if (bullet.getX() > gameData.getDisplayWidth()) {
                world.removeEntity(bullet);
            }

            if (bullet.getY() < 0) {
                world.removeEntity(bullet);
            }

            if (bullet.getY() > gameData.getDisplayHeight()) {
                world.removeEntity(bullet);

            }

            double changeX = Math.cos(Math.toRadians(bullet.getRotation()));
            double changeY = Math.sin(Math.toRadians(bullet.getRotation()));
            bullet.setX(bullet.getX() + changeX * 2);
            bullet.setY(bullet.getY() + changeY * 2);

            if (bullet.isHit()) {
                world.removeEntity(bullet);
            }
        }
    }

    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        Entity bullet = new Bullet();
        bullet.setPolygonCoordinates(
                -2, -2,
                -2, 2,
                2, 2,
                2, -2
        );
        bullet.setX(shooter.getX() + Math.cos(Math.toRadians(shooter.getRotation())) * 20);
        bullet.setY(shooter.getY() + Math.sin(Math.toRadians(shooter.getRotation())) * 20);
        bullet.setRotation(shooter.getRotation());
        return bullet;
    }

    private void setShape(Entity entity) {
    }
}