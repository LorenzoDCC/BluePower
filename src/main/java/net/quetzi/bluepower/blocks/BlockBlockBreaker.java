/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.references.GuiIDs;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier1.TileBlockBreaker;

public class BlockBlockBreaker extends BlockContainerBase {
    private IIcon textureFront;
    private IIcon textureBack;

    public BlockBlockBreaker() {
        super(Material.rock);
        this.setBlockName(Refs.BLOCKBREAKER_NAME);
    }

    @Override
    protected Class<? extends TileEntity> getTileEntity() {
        return TileBlockBreaker.class;
    }

    @Override
    public GuiIDs getGuiID() {
        return GuiIDs.INVALID; // TODO: Not sure what to return if it has no gui
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.textureFront = iconRegister.registerIcon(Refs.MODID + ":" + Refs.BLOCKBREAKER_NAME + "_front");
        this.textureBack = iconRegister.registerIcon(Refs.MODID + ":" + Refs.BLOCKBREAKER_NAME + "_back");
        this.blockIcon = iconRegister.registerIcon(Refs.MODID + ":" + Refs.BLOCKBREAKER_NAME);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        ForgeDirection direction = ForgeDirection.getOrientation(meta);
        if (side == direction.ordinal()) {
            return textureFront;
        } else if (side == direction.getOpposite().ordinal()) {
            return textureBack;
        }
        return blockIcon;
    }
}
