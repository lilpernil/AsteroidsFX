import dk.sdu.mmmi.cbse.common.services.entityprocessing.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.gameplugin.IGamePluginService;

module Player {
    requires Common;
    requires CommonBullet;   
    uses dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
    provides IGamePluginService with dk.sdu.mmmi.cbse.playersystem.PlayerPlugin;
    provides IEntityProcessingService with dk.sdu.mmmi.cbse.playersystem.PlayerControlSystem;
}