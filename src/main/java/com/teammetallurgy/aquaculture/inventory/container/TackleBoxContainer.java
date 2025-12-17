package com.teammetallurgy.aquaculture.inventory.container;

import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.api.bait.IBaitItem;
import com.teammetallurgy.aquaculture.block.blockentity.TackleBoxBlockEntity;
import com.teammetallurgy.aquaculture.init.AquaBlocks;
import com.teammetallurgy.aquaculture.init.AquaGuis;
import com.teammetallurgy.aquaculture.inventory.container.slot.SlotFishingRod;
import com.teammetallurgy.aquaculture.inventory.container.slot.SlotHidable;
import com.teammetallurgy.aquaculture.item.HookItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import javax.annotation.Nonnull;
import java.util.Objects;

public class TackleBoxContainer extends AbstractContainerMenu {
    public TackleBoxBlockEntity tackleBox;
    private int rows = 4;
    private int collumns = 4;
    public Slot slotHook;
    public Slot slotBait;
    public Slot slotLine;
    public Slot slotBobber;

    public TackleBoxContainer(int windowID, BlockPos pos, Inventory playerInventory) {
        super(AquaGuis.TACKLE_BOX.get(), windowID);
        Player player = playerInventory.player;
        this.tackleBox = (TackleBoxBlockEntity) player.level().getBlockEntity(pos);
        if (this.tackleBox != null) {
            this.tackleBox.startOpen(player);
            ItemStacksResourceHandler tackleBoxCapability = (ItemStacksResourceHandler) player.level().getCapability(Capabilities.Item.BLOCK, pos, null);
            IndexModifier<ItemResource> slotModifier = tackleBoxCapability::set;

            if (tackleBoxCapability != null) {
                SlotFishingRod fishingRodSlot = (SlotFishingRod) addSlot(new SlotFishingRod(tackleBoxCapability, slotModifier, 0, 117, 21));

                FishingRodContainerWrapper wrapper = new FishingRodContainerWrapper(fishingRodSlot, 4, tackleBoxCapability);
                this.slotHook = this.addSlot(new SlotHidable(fishingRodSlot, wrapper, 0, 106, 44) {
                    @Override
                    public boolean mayPlace(@Nonnull ItemStack stack) {
                        return stack.getItem() instanceof HookItem && super.mayPlace(stack);
                    }
                });
                this.slotBait = this.addSlot(new SlotHidable(fishingRodSlot, wrapper, 1, 129, 44) {
                    @Override
                    public boolean mayPlace(@Nonnull ItemStack stack) {
                        return stack.getItem() instanceof IBaitItem && super.mayPlace(stack);
                    }

                    @Override
                    public boolean mayPickup(@Nonnull Player player) {
                        return false;
                    }
                });
                this.slotLine = this.addSlot(new SlotHidable(fishingRodSlot, wrapper, 2, 106, 67) {
                    @Override
                    public boolean mayPlace(@Nonnull ItemStack stack) {
                        return stack.is(AquacultureAPI.Tags.FISHING_LINE) && super.mayPlace(stack);
                    }
                });
                this.slotBobber = this.addSlot(new SlotHidable(fishingRodSlot, wrapper, 3, 129, 67) {
                    @Override
                    public boolean mayPlace(@Nonnull ItemStack stack) {
                        return stack.is(AquacultureAPI.Tags.BOBBER) && super.mayPlace(stack);
                    }
                });
            }

            //Tackle Box
            for (int column = 0; column < collumns; ++column) {
                for (int row = 0; row < rows; ++row) {
                    this.addSlot(new ResourceHandlerSlot(tackleBoxCapability, slotModifier, 1 + row + column * collumns, 8 + row * 18, 8 + column * 18) {
                        @Override
                        public boolean mayPlace(@Nonnull ItemStack stack) {
                            return TackleBoxBlockEntity.canBePutInTackleBox(stack);
                        }
                    });
                }
            }

            for (int column = 0; column < 3; ++column) {
                for (int row = 0; row < 9; ++row) {
                    this.addSlot(new Slot(playerInventory, row + column * 9 + 9, 8 + row * 18, 90 + column * 18));
                }
            }

            for (int row = 0; row < 9; ++row) {
                this.addSlot(new Slot(playerInventory, row, 8 + row * 18, 148));
            }
        }
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return stillValid(ContainerLevelAccess.create(Objects.requireNonNull(tackleBox.getLevel()), tackleBox.getBlockPos()), player, AquaBlocks.TACKLE_BOX.get());
    }

    @Override
    @Nonnull
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        ItemStack transferStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            transferStack = slotStack.copy();
            if (index < this.rows * this.collumns) {
                if (!this.moveItemStackTo(slotStack, this.rows * this.collumns, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 0, this.rows * this.collumns, false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return transferStack;
    }

    @Override
    public void removed(@Nonnull Player player) {
        super.removed(player);
        this.tackleBox.stopOpen(player);
    }

    @Override
    public void clicked(int slotId, int dragType, @Nonnull ClickType clickType, @Nonnull Player player) {
        //Bait replacing
        if (slotId >= 0 && clickType == ClickType.PICKUP) {
            Slot slot = this.slots.get(slotId);
            if (slot == this.slotBait) {
                ItemStack mouseStack = player.containerMenu.getCarried();
                if (slot.mayPlace(mouseStack)) {
                    if (slot.getItem().isDamaged() || slot.getItem().isEmpty() || slot.getItem().getItem() != mouseStack.getItem()) {
                        slot.set(ItemStack.EMPTY); //Set to empty, to allow new bait to get put in
                    }
                }
            }
        }
        super.clicked(slotId, dragType, clickType, player);
    }
}