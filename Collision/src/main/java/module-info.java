import dk.sdu.mmmi.cbse.common.services.gameplugin.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.postentityprocessing.IPostEntityProcessingService;

module Collision {
    requires Common;
    requires CommonBullet;
    uses dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
    provides IGamePluginService with dk.sdu.mmmi.cbse.collisionsystem.CollisionPlugin;
    provides IPostEntityProcessingService with dk.sdu.mmmi.cbse.collisionsystem.CollisionControlSystem;
}

