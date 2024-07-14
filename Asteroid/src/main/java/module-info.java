import dk.sdu.mmmi.cbse.common.services.entityprocessing.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.gameplugin.IGamePluginService;

module Astroid {
    requires Common;
    requires CommonBullet;
    uses dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
    provides IGamePluginService with dk.sdu.mmmi.cbse.asteroidsystem.AsteroidPlugin;
    provides IEntityProcessingService with dk.sdu.mmmi.cbse.asteroidsystem.AsteroidControlSystem;
}