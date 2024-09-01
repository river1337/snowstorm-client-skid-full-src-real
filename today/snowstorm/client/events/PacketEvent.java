package today.snowstorm.client.events;

import net.minecraft.network.Packet;
import today.snowstorm.api.events.Event;

public class PacketEvent extends Event {
    private Packet packet;

    public PacketEvent(Packet packet){
        this.packet = packet;
    }

    public Packet getPacket(){
        return packet;
    }
    public void setPacket(Packet pack){
        this.packet = pack;
    }

}
