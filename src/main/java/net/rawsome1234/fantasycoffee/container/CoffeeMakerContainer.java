package net.rawsome1234.fantasycoffee.container;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import net.rawsome1234.fantasycoffee.block.ModBlocks;
import net.rawsome1234.fantasycoffee.block.tileentity.CoffeeMakerTile;

public class CoffeeMakerContainer extends AbstractContainerMenu {
    private final BlockEntity tileEntity;
    private final Level level;
    private final Player player;
    private final IItemHandler playerInventory;

    private final ContainerData data;

    public CoffeeMakerContainer(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv.player.level().getBlockEntity(extraData.readBlockPos()), inv, inv.player, new SimpleContainerData(5));
    }

    public CoffeeMakerContainer(int windowID, BlockEntity entity,
                                Inventory playerInventory, Player player, ContainerData data) {
        super(ModContainers.COFFEE_MAKER_CONTAINER.get(), windowID);
        this.level = player.level();
//        System.out.println(entity.getPersistentData());
//        System.out.println(entity != null);
        this.tileEntity = entity;
        this.player = player;
        this.playerInventory = new InvWrapper(playerInventory);
        this.data = data;
        layoutPlayerInventorySlots(8, 84);
//        System.out.println(tileEntity.getLevel().isClientSide());

        if(tileEntity != null){
            tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
//                System.out.println("Capability is real");
//                System.out.println(h);
                // Water
                addSlot(new SlotItemHandler(h, 0, 37, 35));
                // Beans
                addSlot(new SlotItemHandler(h, 1, 95, 12));
                // Mug input
                addSlot(new SlotItemHandler(h, 2, 95, 58));
                // Mug Output
//                System.out.println(h.getStackInSlot(3));
                addSlot(new SlotItemHandler(h, 3, 139, 35));
                // Extra Ingredient
                addSlot(new SlotItemHandler(h, 4, 95, 35));
            });
        }

        addDataSlots(data);
    }




    private CompoundTag getCoffeeTileData(){
        return tileEntity.getPersistentData();
    }

    private CoffeeMakerTile getCoffeeTile() {
        try{
            return ((CoffeeMakerTile) tileEntity);
        }
        catch(Exception e) {
            return null;
        }
    }

    public int getBottles(){
        if(data.getCount() >= 1){
            return data.get(0);
        }
        else{
            return -1;
        }
    }

    public boolean hasBeans(){
        if(data.getCount() >= 5){
            return data.get(4) == 1;
        }
        else{
            return false;
        }
    }

    public int getMaxBottles(){
        if(data.getCount() >= 2){
            return data.get(1);
        }
        else{
            return -1;
        }
    }

    public int getRemainingTicks(){
        if(data.getCount() >= 3){
            return data.get(2);
        }
        else{
            return -1;
        }
    }

    public int getTotalTicks(){
        if(data.getCount() >= 4){
            return data.get(3);
        }
        else{
            return -1;
        }
    }

    @Override
    public boolean stillValid(Player p) {
        return stillValid(ContainerLevelAccess.create(level, tileEntity.getBlockPos()),
                p, ModBlocks.COFFEE_MAKER.get());
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }

        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }

        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }



    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 5;  // must match TileEntityInventoryBasic.NUMBER_OF_SLOTS

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
//            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }
}
