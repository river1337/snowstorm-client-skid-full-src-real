package today.snowstorm.client.utils.player;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import today.snowstorm.client.utils.Wrapper;

public class PlayerUtils extends Wrapper {

    public static boolean isOverVoid() {
        BlockPos block = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);

        for (double i = mc.thePlayer.posY + 1; i > 0; i -= 0.5) {
            try {
                if (mc.theWorld.getBlockState(block).getBlock() != Blocks.air) {
                    return false;
                }
            } catch (Exception e) {
            }
            block = block.add(0, -1, 0);
        }

        for (double i = 0; i < 10; i += 0.1) {
            if (MoveUtil.isOnGround(i)) {
                return false;
            }
        }

        return true;
    }

}
