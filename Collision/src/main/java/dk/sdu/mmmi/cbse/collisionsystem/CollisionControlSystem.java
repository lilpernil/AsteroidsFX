package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

public class CollisionControlSystem implements IPostEntityProcessingService {
    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity1 : world.getEntities()) {
            for (Entity entity2 : world.getEntities()) {
                System.out.println(entity1.getClass().getName());
                if (entity1.getClass() == entity2.getClass()) {
                    continue;
                }
                if (collisionDetected(entity1, entity2)) {
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
