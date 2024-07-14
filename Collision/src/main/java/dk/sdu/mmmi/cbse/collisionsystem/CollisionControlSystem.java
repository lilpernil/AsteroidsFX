package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

public class CollisionControlSystem implements IPostEntityProcessingService {
    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity1 : world.getEntities()) {
            String entity1Name = entity1.getClass().getName();
            for (Entity entity2 : world.getEntities()) {
                String entity2Name = entity2.getClass().getName();
                if (entity1.getClass() == entity2.getClass()) {
                    continue;
                }
                if (collisionDetected(entity1, entity2)) {
                    if (entity1Name.contains("Asteroid")
                            && (entity2Name.contains("Player")
                            || entity2Name.contains("Enemy"))) {
                        world.removeEntity(entity2);
                    }
                    if ((entity1Name.contains("Player")
                            || entity1Name.contains("Enemy"))
                            && (entity2Name.contains("Asteroid"))) {
                        world.removeEntity(entity1);
                    }
                    entity1.setHit(true);
                    entity2.setHit(true);
                }
            }
        }
    }

    private boolean collisionDetected(Entity entity1, Entity entity2) {
        double dx = entity1.getX() - entity2.getX();
        double dy = entity1.getY() - entity2.getY();

        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance < (entity1.getEntityRadius() + entity2.getEntityRadius());
    }
}
