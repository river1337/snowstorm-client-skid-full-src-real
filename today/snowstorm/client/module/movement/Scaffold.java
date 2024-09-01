package today.snowstorm.client.module.movement;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import today.snowstorm.api.events.EventType;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleType;
import today.snowstorm.client.events.OverlayEvent;
import today.snowstorm.client.events.Render3DEvent;
import today.snowstorm.client.events.UpdateEvent;
import today.snowstorm.client.module.movement.scaffold.ScaffoldUtils;
import today.snowstorm.client.settings.BooleanSetting;
import today.snowstorm.client.settings.NumberSetting;
import today.snowstorm.client.utils.Wrapper;
import today.snowstorm.client.utils.animations.Direction;
import today.snowstorm.client.utils.animations.impl.DecelerateAnimation;
import today.snowstorm.client.utils.other.TimerUtil;
import today.snowstorm.client.utils.player.RotationUtils;
import today.snowstorm.client.utils.render.RenderUtils;
import tv.twitch.chat.ChatUserInfo;
import wtf.Snowstorm.client.events.*;

public class Scaffold extends Module {

    protected static Minecraft mc = Minecraft.getMinecraft();
    TimerUtil timer;

    private BooleanSetting devRots = new BooleanSetting("Dev Rots", false), ghostSwang = new BooleanSetting("Ghost Swing", false), blockCatch = new BooleanSetting("Block Catch", true);
    private NumberSetting slider = new NumberSetting("Click Delay:", 20, 0, 120, 10);

    public Scaffold() {
        super("Scaffold", "Scaffold", ModuleType.MOVEMENT, Keyboard.KEY_M);
        timer = new TimerUtil();

        addSetting(slider, blockCatch, ghostSwang, devRots);
    }

    Vec3i rotate = null;
    private static EnumFacing enumFacing;
    float curYaw, curPitch;

    int lastSlot = 1;

    private DecelerateAnimation xPos = new DecelerateAnimation(250, 1.0f), opacity = new DecelerateAnimation(250, 1.0f);

    @EventHandler
    public Listener<OverlayEvent> onFatDobber = e -> {
        //ScaledResolution sr = new ScaledResolution(mc);
//
        //// Render nice thing lol
        //int boxWidth = 50;
        //int boxHeight = 50;
//
        //RenderUtils.drawRoundedRect(sr.getScaledWidth() / 2f - boxWidth / 2f, (float) (sr.getScaledHeight() - 105 * xPos.getOutput()), boxWidth, boxHeight, 3f, new Color(10, 10, 10, 255));
    };

    @Override
    public void onEnable() {
        super.onEnable();

        opacity.reset();
        xPos.setDirection(Direction.FORWARDS);
    }

    @Override
    public void onDisable() {
        //mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(lastSlot));

        xPos.setDirection(Direction.BACKWARDS);
        super.onDisable();
    }

    public static BlockPos blockPos;
    public Vec3i v3;

    TimerUtil timer2 = new TimerUtil();

    private boolean shouldPlace = false;
    private ItemStack itemStack = null;

    @EventHandler
    public Listener<UpdateEvent> onUpdateEvent = e -> {
        if(e.getType() == EventType.POST) {

            int slot = -1;
            // Set blok slot
            slot = ScaffoldUtils.grabBlockSlot();
            if (slot == -1) return;

            //mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));

            blockPos = getBlockPosToPlaceOn(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ));

            // If no blockpos, return (bugfix)
            if (blockPos == null) {
                e.setYaw(mc.thePlayer.rotationYaw);
                e.setPitch(mc.thePlayer.rotationPitch);
                return;
            }

            rotate = translate(blockPos, enumFacing);

            // If there is no correct translation, do nothing. (bugfix)
            if (rotate == null) {
                e.setYaw(mc.thePlayer.rotationYaw);
                e.setPitch(mc.thePlayer.rotationPitch);
                return;
            }

            // current item
            itemStack = mc.thePlayer.inventory.getStackInSlot(slot);

            mc.playerController.sendSlotPacket(itemStack, slot);

            // convert block position to Vec3
            v3 = new Vec3i(rotate.getX(), rotate.getY(), rotate.getZ());

            // If the rotations have been completed, this returns true which makes placing a block possible.
            shouldPlace = doRotationThings(e);

            // if you have an item and the block position isn't null
            if(itemStack != null && blockPos != null) {
                // Place delay
                if (timer.stopAfter((1000 / slider.getValueAsInt()), true)) {
                    // If rotations have been set
                    if(shouldPlace) {

                        // Run this code only once per call.
                        for(int i = 0; i < 1; i ++) {
                            // Send block place.
                            final Vec3 newV = new Vec3(v3.getX(), v3.getY(), v3.getZ());
                            mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, itemStack, blockPos, enumFacing, newV);

                            // No swing
                            if (!ghostSwang.isEnabled()) {
                                mc.thePlayer.swingItem();
                            } else {
                                // Send swing animation packet
                                mc.getNetHandler().getNetworkManager().sendPacket(new C0APacketAnimation());
                            }
                        }
                    }
                }
            } else {
                //Block block = mc.theWorld.getBlockState(underneath).getBlock();
                //Wrapper.addChatMessage("noblock " + block, false);
            }
        } else if(e.getType() == EventType.PRE) {
            BlockPos underneath = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0f, mc.thePlayer.posZ);

            boolean canPlaceRightNow = mc.theWorld.getBlockState(underneath).getBlock() == Blocks.air;

            if(itemStack != null && blockPos != null && canPlaceRightNow && overBlock(mc.thePlayer.getHorizontalFacing().getOpposite(), blockPos, true)) {
                // Place delay
                if (timer.stopAfter((1000 / slider.getValueAsInt()), true)) {
                    // If rotations have been set
                    if(shouldPlace) {
                        // Run this code only once per call.
                        for(int i = 0; i < 1; i ++) {
                            // Send block place.
                            final Vec3 newV = new Vec3(v3.getX(), v3.getY(), v3.getZ());
                            mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, itemStack, blockPos, enumFacing, newV);
                            // No swing
                            if (!ghostSwang.isEnabled()) {
                                mc.thePlayer.swingItem();
                            } else {
                                // Send swing animation packet
                                mc.getNetHandler().getNetworkManager().sendPacket(new C0APacketAnimation());
                            }
                        }
                    }
                }
            } else {
                Block block = mc.theWorld.getBlockState(underneath).getBlock();
                //Wrapper.addChatMessage("noblock " + block, false);
            }
        }
    };

    public float interpolateRotation(float start, float end, float t) {
        float diff = end - start;

        // return start + diff * time and make the speed match your current mouse sens
        return (start + diff * t) / getSensitivityMultiplier() * getSensitivityMultiplier();
    }

    @EventHandler
    public Listener<Render3DEvent> on3DRender = e -> {
        RenderUtils.drawBox(v3.getX(), v3.getY(), v3.getZ(), v3.getX() + 1.0, v3.getY() + 1.0, v3.getZ() + 1.0);
    };

    private boolean doRotationThings(UpdateEvent e) {
        if(e.getType() == EventType.PRE) return false;

        double x = blockPos.getX() + 0.1f;
        double y = blockPos.getY() + 0.1f;
        double z = blockPos.getZ() + 0.1f;

        Vec3i vec3i = new Vec3i(x, y, z);

        // Make the rotations face the block.
        v3 = RotationUtils.getBlockFace(vec3i);

        float[] rots = RotationUtils.getRotation(v3, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, 180, enumFacing);

        rots[0] = Math.round(rots[0] / getSensitivityMultiplier()) * getSensitivityMultiplier();
        rots[1] = Math.round(rots[1] / getSensitivityMultiplier()) * getSensitivityMultiplier();

        e.setYaw(rots[0]);
        e.setPitch(rots[1]);

        RotationUtils.setCustomYaw(e.getRotYaw());
        RotationUtils.setCustomPitch(e.getRotPitch());

        if (mc.thePlayer.ticksExisted % 10 == 0) {
            Wrapper.addChatMessage(enumFacing, false);
        }

        if (devRots.isEnabled()) {
            mc.thePlayer.rotationYaw = e.getRotYaw();
            mc.thePlayer.rotationPitch = e.getRotPitch();
        }

        return true;
    }

    // RayCast util found on GitHub (cant find link lol)
    public static boolean overBlock(final EnumFacing facing, final BlockPos pos, final boolean strict) {
        final MovingObjectPosition mop = mc.objectMouseOver;

        if (mop == null) {
            return false;
        }

        final Vec3 hv = mop.hitVec;
        if (hv == null) return false;

        return mop.getBlockPos().equals(pos) && (!strict || mop.sideHit == facing);
    }

    public static float getSensitivityMultiplier() {
        float SENS = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
        return (SENS * SENS * SENS * 8.0F) * 0.15F;
    }

    public static float getYawFromMovement() {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if ((forward == 0.0D) && (strafe == 0.0D)) {

        } else if (forward != 0.0D) {
//			strafe = 0.0D;
            if (forward > 0.0D) {
                forward = 1;
            } else if (forward < 0.0D) {
                forward = -1;
            }
            if (strafe > 0.0D) {
                yaw += (forward > 0.0D ? -45 : 45);
            } else if (strafe < 0.0D) {
                yaw += (forward > 0.0D ? 45 : -45);
            }
        }

        return yaw + 90.0F;

    }

    private int diagonal =0;
    public static Vec3i translate(BlockPos blockPos, EnumFacing enumFacing) {
        double x = blockPos.getX();
        double y = blockPos.getY();
        double z = blockPos.getZ();
        //double r1 = ThreadLocalRandom.current().nextDouble(0.2, 0.5);
        //double r2 = ThreadLocalRandom.current().nextDouble(0.7, 1.0);
        double r1 = 0.001d;
        double r2 = 0.001d;
        if (enumFacing.equals(EnumFacing.UP)) {
            x += r1;
            z += r1;
            y += 1.0;
        } else if (enumFacing.equals(EnumFacing.DOWN)) {
            x += r1;
            z += r1;
        } else if (enumFacing.equals(EnumFacing.WEST)) {
            y += r2;
            z += r1;
        } else if (enumFacing.equals(EnumFacing.EAST)) {
            y += r2;
            z += r1;
            x += 1.0;
        } else if (enumFacing.equals(EnumFacing.SOUTH)) {
            y += r2;
            x += r1;
            z += 1.0;
        } else if (enumFacing.equals(EnumFacing.NORTH)) {
            y += r2;
            x += r1;
        }
        return new Vec3i(x, y, z);
    }

    // ported
    private BlockPos getBlockPosToPlaceOn(BlockPos pos) {
        BlockPos blockPos1 = pos.add(-1, 0, 0);
        BlockPos blockPos2 = pos.add(1, 0, 0);
        BlockPos blockPos3 = pos.add(0, 0, -1);
        BlockPos blockPos4 = pos.add(0, 0, 1);
        float down = 0;

        if(mc.thePlayer.fallDistance > 3 && blockCatch.isEnabled()) {
            down = 1;
        }

        if (mc.theWorld.getBlockState(pos.add(0, -1 - down, 0)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.UP;
            return (pos.add(0, -1, 0));
        } else if (mc.theWorld.getBlockState(pos.add(-1, 0 - down, 0)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.EAST;
            return (pos.add(-1, 0 - down, 0));
        } else if (mc.theWorld.getBlockState(pos.add(1, 0 - down, 0)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.WEST;
            return (pos.add(1, 0 - down, 0));
        } else if (mc.theWorld.getBlockState(pos.add(0, 0 - down, -0.8)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.SOUTH;
            return (pos.add(0, 0 - down, -1));
        } else if (mc.theWorld.getBlockState(pos.add(0, 0 - down, 1)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.NORTH;
            return (pos.add(0, 0 - down, 1));
        } else if (mc.theWorld.getBlockState(blockPos1.add(0, -1 - down, 0)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.UP;
            return (blockPos1.add(0, -1 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos1.add(-1, 0 - down, 0)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.EAST;
            return (blockPos1.add(-1, 0 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos1.add(1, 0 - down, 0)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.WEST;
            return (blockPos1.add(1, 0 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos1.add(0, 0 - down, -1)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.SOUTH;
            return (blockPos1.add(0, 0 - down, -1));
        } else if (mc.theWorld.getBlockState(blockPos1.add(0, 0 - down, 1)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.NORTH;
            return (blockPos1.add(0, 0 - down, 1));
        } else if (mc.theWorld.getBlockState(blockPos2.add(0, -1 - down, 0)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.UP;
            return (blockPos2.add(0, -1 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos2.add(-1, 0 - down, 0)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.EAST;
            return (blockPos2.add(-1, 0 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos2.add(1, 0 - down, 0)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.WEST;
            return (blockPos2.add(1, 0 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos2.add(0, 0 - down, -1)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.SOUTH;
            return (blockPos2.add(0, 0 - down, -1));
        } else if (mc.theWorld.getBlockState(blockPos2.add(0, 0 - down, 1)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.NORTH;
            return (blockPos2.add(0, 0 - down, 1));
        } else if (mc.theWorld.getBlockState(blockPos3.add(0, -1 - down, 0)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.UP;
            return (blockPos3.add(0, -1 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos3.add(-1, 0 - down, 0)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.EAST;
            return (blockPos3.add(-1, 0 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos3.add(1, 0 - down, 0)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.WEST;
            return (blockPos3.add(1, 0 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos3.add(0, 0 - down, -1)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.SOUTH;
            return (blockPos3.add(0, 0 - down, -1));
        } else if (mc.theWorld.getBlockState(blockPos3.add(0, 0 - down, 1)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.NORTH;
            return (blockPos3.add(0, 0 - down, 1));
        } else if (mc.theWorld.getBlockState(blockPos4.add(0, -1 - down, 0)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.UP;
            return (blockPos4.add(0, -1 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos4.add(-1, 0 - down, 0)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.EAST;
            return (blockPos4.add(-1, 0 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos4.add(1, 0 - down, 0)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.WEST;
            return (blockPos4.add(1, 0 - down, 0));
        } else if (mc.theWorld.getBlockState(blockPos4.add(0, 0 - down, -1)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.SOUTH;
            return (blockPos4.add(0, 0 - down, -1));
        } else if (mc.theWorld.getBlockState(blockPos4.add(0, 0 - down, 1)).getBlock() != Blocks.air) {
            enumFacing = EnumFacing.NORTH;
            return (blockPos4.add(0, 0 - down, 1));
        }
        return null;
    }

}
