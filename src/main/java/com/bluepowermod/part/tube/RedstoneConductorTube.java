package com.bluepowermod.part.tube;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.part.MicroblockShape;
import uk.co.qmunity.lib.part.compat.OcclusionHelper;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.api.wire.ConnectionType;
import com.bluepowermod.api.wire.IConnection;
import com.bluepowermod.api.wire.IConnectionCache;
import com.bluepowermod.api.wire.IConnectionListener;
import com.bluepowermod.api.wire.redstone.IRedstoneConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.redstone.RedstoneApi;
import com.bluepowermod.redstone.RedstoneConnectionCache;

public class RedstoneConductorTube implements IRedstoneConductor, IConnectionListener {

    private static final List<RedstoneConductorTube> tubes = new ArrayList<RedstoneConductorTube>();

    public static RedstoneConductorTube getDevice(PneumaticTube tube) {

        for (RedstoneConductorTube dev : new ArrayList<RedstoneConductorTube>(tubes))
            if (dev.tube != null && dev.tube.equals(tube))
                return dev;

        RedstoneConductorTube dev = new RedstoneConductorTube(tube);
        tubes.add(dev);
        return dev;
    }

    private RedstoneConnectionCache connections = RedstoneApi.getInstance().createRedstoneConnectionCache(this);
    private PneumaticTube tube;

    private byte power = 0;

    private RedstoneConductorTube(PneumaticTube tube) {

        this.tube = tube;
    }

    @Override
    public World getWorld() {

        return tube.getWorld();
    }

    @Override
    public int getX() {

        return tube.getX();
    }

    @Override
    public int getY() {

        return tube.getY();
    }

    @Override
    public int getZ() {

        return tube.getZ();
    }

    @Override
    public boolean canConnect(ForgeDirection side, IRedstoneDevice device, ConnectionType type) {

        if (type == ConnectionType.STRAIGHT) {
            if (tube.getRedwireType() == null)
                return false;

            if (device instanceof IFace)
                return false;
            if (OcclusionHelper.microblockOcclusionTest(new Vec3i(this), MicroblockShape.FACE_HOLLOW, 8, side))
                return false;
            if (device instanceof RedstoneConductorTube)
                if (((RedstoneConductorTube) device).tube instanceof MagTube != tube instanceof MagTube)
                    return false;

            return true;// FIXME WireCommons.canConnect(this, device);
        }

        return false;
    }

    @Override
    public IConnectionCache<? extends IRedstoneDevice> getRedstoneConnectionCache() {

        return connections;
    }

    @Override
    public void onConnect(IConnection<?> connection) {

        tube.sendUpdatePacket();
    }

    @Override
    public void onDisconnect(IConnection<?> connection) {

        tube.sendUpdatePacket();
    }

    @Override
    public byte getRedstonePower(ForgeDirection side) {

        if (!RedstoneApi.getInstance().shouldWiresOutputPower())
            return 0;

        if (!isAnalog(side))
            return (byte) ((power & 0xFF) > 0 ? 255 : 0);

        return power;
    }

    @Override
    public void setRedstonePower(ForgeDirection side, byte power) {

        this.power = power;
    }

    @Override
    public void onRedstoneUpdate() {

        tube.sendUpdatePacket();
    }

    @Override
    public boolean hasLoss(ForgeDirection side) {

        if (tube.getRedwireType() == null)
            return false;

        return tube.getRedwireType().hasLoss();
    }

    @Override
    public boolean isAnalog(ForgeDirection side) {

        if (tube.getRedwireType() == null)
            return false;

        return tube.getRedwireType().isAnalog();
    }

    @Override
    public boolean canPropagateFrom(ForgeDirection fromSide) {

        return true;
    }

    public byte getPower() {

        return power;
    }

    public IRedstoneDevice getDeviceOnSide(ForgeDirection d) {

        return connections.getConnectionOnSide(d).getB();
    }

}