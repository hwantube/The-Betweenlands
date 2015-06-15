package thebetweenlands.event.player;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEvent;
import thebetweenlands.TheBetweenlands;
import thebetweenlands.blocks.BLBlockRegistry;
import thebetweenlands.world.teleporter.TeleporterHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PlayerPortalHandler {
	int timer = 120;


	@SubscribeEvent
	public void teleportCheck(LivingEvent.LivingUpdateEvent event)
	{
		if(event.entity instanceof EntityPlayerMP){
			EntityPlayerMP player = (EntityPlayerMP)event.entity;
			NBTTagCompound nbt = player.getEntityData();
			if(nbt.getBoolean("INPORTAL")){
				if(player.worldObj.getBlock(floor(player.posX), floor(player.posY), floor(player.posZ)) == BLBlockRegistry.treePortalBlock) {
					if(timer == 119)
						player.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0));
					if (timer == 0 || player.capabilities.isCreativeMode) {
						if (player.dimension == 0) {
							player.timeUntilPortal = 10;
							TeleporterHandler.transferToBL(player);
						} else {
							player.timeUntilPortal = 10;
							TeleporterHandler.transferToOverworld(player);
						}
						nbt.setBoolean("INPORTAL", false);
						timer = 120;
					} else
						timer--;
				}else {
					timer = 120;
					nbt.setBoolean("INPORTAL", false);
				}
			}
		}
		TheBetweenlands.proxy.playPortalSounds(event.entity, timer);
	}


	public static int floor(double x){
		int xi = (int)x;
		return x<xi ? xi-1 : xi;
	}
}
