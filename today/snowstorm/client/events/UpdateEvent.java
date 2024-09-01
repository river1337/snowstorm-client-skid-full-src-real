package today.snowstorm.client.events;

import today.snowstorm.api.events.Event;

public final class UpdateEvent extends Event {
    public double posX, posY, posZ;
    private boolean onGround;
    private float rotationYaw, rotationPitch;

    public UpdateEvent(double posX, double posY, double posZ, float rotationYaw, float rotationPitch, boolean onGround) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.rotationYaw = rotationYaw;
        this.rotationPitch = rotationPitch;
        this.onGround = onGround;
    }

    public void setX(double x){
        this.posX = x;
    }

    public void setY(double y){
        this.posY = y;
    }

    public void setZ(double z){
        this.posZ = z;
    }

    public void setYaw(float yaw){
        this.rotationYaw = yaw;
    }

    public void setPitch(float pitch){
        this.rotationPitch = pitch;
    }

    public void setOnGround(boolean onground){
        this.onGround = onground;
    }

    public double x(){
        return this.posX;
    }

    public double y(){
        return this.posY;
    }

    public double z(){
        return this.posZ;
    }

    public float getRotYaw(){
        return this.rotationYaw;
    }

    public float getRotPitch(){
        return this.rotationPitch;
    }

    public boolean onGround(){
        return this.onGround;
    }
}