package net.rawsome1234.fantasycoffee.block.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.rawsome1234.fantasycoffee.block.custom.CoffeeMakerBlock;
import net.rawsome1234.fantasycoffee.container.CoffeeMakerContainer;
import net.rawsome1234.fantasycoffee.data.recipes.CoffeeMakerRecipe;
import net.rawsome1234.fantasycoffee.data.recipes.ModRecipeTypes;
import net.rawsome1234.fantasycoffee.item.ModItems;
import net.rawsome1234.fantasycoffee.util.ModTags;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CoffeeMakerTile extends BlockEntity implements MenuProvider, WorldlyContainer {

    private final ItemStackHandler itemHandler = createHandler();

    public int waterBottles;
    public static int maxWaterStore = 12;

    public int ticksInRecipe;
    public int ticksLeft;

    public boolean hasCoffeeBeans;

    public boolean hasOutput;

    public ContainerData data;

    private CoffeeMakerRecipe recipe;

    public CoffeeMakerTile(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COFFEE_MAKER_TILE.get(), pos, state);
        ticksLeft = 0;
        waterBottles = 0;
        ticksInRecipe = -1;
        hasCoffeeBeans = false;
        hasOutput = false;

        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> CoffeeMakerTile.this.waterBottles;
                    case 1 -> CoffeeMakerTile.this.maxWaterStore;
                    case 2 -> CoffeeMakerTile.this.ticksLeft;
                    case 3 -> CoffeeMakerTile.this.ticksInRecipe;
                    case 4 -> !CoffeeMakerTile.this.hasCoffeeBeans ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {

//                return;
                switch (pIndex) {
                    case 0 -> CoffeeMakerTile.this.waterBottles = pValue;
                    case 1 -> CoffeeMakerTile.this.maxWaterStore = pValue;
                    case 2 -> CoffeeMakerTile.this.ticksLeft = pValue;
                    case 3 -> CoffeeMakerTile.this.ticksInRecipe = pValue;
                    case 4 -> CoffeeMakerTile.this.hasCoffeeBeans = pValue == 1;
//                  }
                }
            }

            @Override
            public int getCount () {
                return 5;
            }
        };
    }


    @Override
    public void saveAdditional(CompoundTag nbt) {
        nbt.put("inv", itemHandler.serializeNBT());
        nbt.putInt("water", waterBottles);
        nbt.putInt("maxWater", maxWaterStore);
        nbt.putInt("ticksRemaining", ticksLeft);
        nbt.putInt("totalTicks", ticksInRecipe);
        nbt.putBoolean("has_coffee", hasCoffeeBeans);
        nbt.putBoolean("has_brewed", hasOutput);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        waterBottles = nbt.getInt("water");
        maxWaterStore = nbt.getInt("maxWater");
        ticksLeft = nbt.getInt("ticksRemaining");
        ticksInRecipe = nbt.getInt("totalTicks");
        hasCoffeeBeans = nbt.getBoolean("has_coffee");
        hasOutput = nbt.getBoolean("has_brewed");
        super.load(nbt);
    }

    private ItemStackHandler createHandler(){
        return new ItemStackHandler(5){
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if(stack.isEmpty()){
                    return true;
                }
                return switch (slot) {
                    case 0 -> stack.is(ModTags.Items.COFFEE_BUCKET);
                    case 1 -> stack.is(ModTags.Items.BEANS);
                    case 2 -> stack.is(ModItems.MUG.get());
                    case 4 -> true;
                    default -> false;
                };
            }

            @Override
            public int getSlotLimit(int slot) {
                return switch (slot) {
                    case 1, 2, 3, 4 -> 64;
                    default -> 1;
                };
            }

            @Override
            public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                if(!isItemValid(slot, stack)){
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    private void updateData(){
        data.set(0, waterBottles);
        data.set(1, maxWaterStore);
        data.set(2, ticksLeft);
        data.set(3, ticksInRecipe);
        data.set(4, hasCoffeeBeans ? 1 : 0);
    }

    public ItemStack overrideInsert(int slot, @Nonnull ItemStack stack, boolean simulate){
        ItemStack current = itemHandler.getStackInSlot(slot);
        if (current.is(stack.getItem())){
            if(current.getMaxStackSize() < current.getCount()+1)
                return stack;
            current.setCount(current.getCount()+1);
            itemHandler.setStackInSlot(slot, current);
            return ItemStack.EMPTY;
        }
        else if (current.getCount() == 0) {
            itemHandler.setStackInSlot(slot, stack);
        }

        return stack;

    }

    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =

            net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

//    private LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] generateLazyHandlers {
//        var handler = LazyOptional.of(() -> itemHandler.);
//
//    }

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
//        System.out.println("Facing: " + facing);
//        System.out.println(handlers[0]);
//        System.out.println(handlers[1]);
//        System.out.println(handlers[2]);
        if (!this.remove && capability == net.minecraftforge.common.capabilities.ForgeCapabilities.ITEM_HANDLER) {
//            return handlers[0].cast();
            if (facing == Direction.UP || facing == null){
//                System.out.println("Returning up capability");
                return handlers[0].cast();
            }
            else if (facing == Direction.DOWN)
                return handlers[1].cast();
            else
                return handlers[2].cast();
        }
        return super.getCapability(capability, facing);
    }

    public boolean addWater(float buckets)
    {
//        System.out.println("Filling...");

        if(waterBottles + (buckets * 3) > maxWaterStore) {
            return false;
        }
        waterBottles += (buckets * 3);
        this.setChanged();
        return true;
    }

    public List<ItemStack> getDrops(){
        List<ItemStack> stacks = new ArrayList<ItemStack>();
        for(int i = 0; i < itemHandler.getSlots(); i++){
            if(!itemHandler.getStackInSlot(i).isEmpty()){
                stacks.add(itemHandler.getStackInSlot(i).copy());
            }
        }
        return stacks;
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public boolean removeWater(int buckets){
        if (waterBottles - (buckets * 3) >= 0){
            waterBottles -= buckets * 3;
            this.setChanged();
            return true;
        }
        return false;
    }

    public boolean hasWater(){
        return waterBottles > 0;
    }

    public boolean hasEnoughWater(int bottles){
        return waterBottles >= bottles;
    }

    public boolean recipeInAction(){
        return ticksLeft > 0 && ticksInRecipe > 0;
    }

    public int getWaterBottles(){
        return waterBottles;
    }

    public int getMaxWaterStore(){
        return maxWaterStore;
    }

    public int getTicksInRecipe(){
        return ticksInRecipe;
    }

    public int getTicksLeft(){
        return ticksLeft;
    }

    public void startCraft(){
//        System.out.println("Attempting to start a recipe");
        Optional<CoffeeMakerRecipe> recipe = getCurrentRecipe();
//        System.out.println("The recipe is null: " + (recipe.isPresent()));
        recipe.ifPresent(iRecipe -> {
//            System.out.println("Ahhh, the recipe exists");
            if(waterBottles >= iRecipe.getBottlesReq() && hasMug()){
                ticksInRecipe = iRecipe.getTicksReq();
                this.recipe = iRecipe;
                ticksLeft = ticksInRecipe;
                sendUpdate();
            }
        });


    }

    private Optional<CoffeeMakerRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(CoffeeMakerRecipe.CoffeeRecipeType.INSTANCE, inventory, level);
    }


    public void craftRecipe(CoffeeMakerRecipe iRecipe){
        ItemStack output = iRecipe.getResultItem(null);

        if(iRecipe.getBottlesReq() <= waterBottles){
            itemHandler.extractItem(1, 1, false);
            itemHandler.extractItem(2, 1, false);
            if(recipe.getIngredients().size() == 2)
                if(itemHandler.getStackInSlot(4).is(new ItemStack(Items.MILK_BUCKET).getItem())){
                    emptyBucketIngredient();
                }
                else{
                    itemHandler.extractItem(4, 1, false);
                }


            waterBottles -= iRecipe.getBottlesReq();
            overrideInsert(3, output, false);
        }

        resetRecipe();
    }

    public void resetRecipe(){
        recipe = null;
        ticksInRecipe = -1;
        ticksLeft = 0;

        sendUpdate();
    }

    public boolean hasMug(){
        return itemHandler.getStackInSlot(2).is(new ItemStack(ModItems.MUG.get()).getItem());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        saveAdditional(nbt);
        return nbt;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("water", waterBottles);
        nbt.putInt("maxWater", maxWaterStore);
        nbt.putInt("ticksRemaining", ticksLeft);
        nbt.putInt("totalTicks", ticksInRecipe);
        nbt.putBoolean("has_coffee", hasCoffeeBeans);
        nbt.putBoolean("has_brewed", hasOutput);
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public boolean recipeValid(){
        if (recipe != null) {
            if (recipe.getIngredients().size() == 1 && !itemHandler.getStackInSlot(4).is(ItemStack.EMPTY.getItem())) {
                return false;
            }
            boolean fin = recipe.getIngredients().get(0).test(itemHandler.getStackInSlot(1)) && hasMug();
//        System.out.println(fin);
            if (recipe.getIngredients().size() == 2) {
                fin = fin && recipe.getIngredients().get(1).test(itemHandler.getStackInSlot(4));
            }
            return fin;
        }
        return false;
    }

    public void emptyBucketIngredient(){
        ItemStack waterItem = itemHandler.getStackInSlot(0);
        if (waterItem.getItem() == Items.WATER_BUCKET){
            itemHandler.setStackInSlot(0, new ItemStack(Items.BUCKET));
        }
        else{
            itemHandler.setStackInSlot(0, new ItemStack(Items.GLASS_BOTTLE));
        }

        if (itemHandler.getStackInSlot(4).is(Items.MILK_BUCKET)){
            itemHandler.setStackInSlot(4, new ItemStack(Items.BUCKET));
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        handlers = net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    }


    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
//        System.out.println("Received update packed on client: " + level.isClientSide());
        CompoundTag tag = pkt.getTag();
        if(tag.contains("inv")){
            itemHandler.deserializeNBT(tag.getCompound("inv"));

            this.getPersistentData().put("inv", itemHandler.serializeNBT());
        }
        if(tag.contains("water")){
            waterBottles = tag.getInt("water");

            this.getPersistentData().putInt("water", waterBottles);
        }
        if(tag.contains("maxWater")){
            maxWaterStore = tag.getInt("maxWater");
            this.getPersistentData().putInt("maxWater", maxWaterStore);
        }
        if(tag.contains("ticksRemaining")){
            ticksLeft = tag.getInt("ticksRemaining");
            this.getPersistentData().putInt("ticksRemaining", ticksLeft);
        }
        if(tag.contains("totalTicks")){
            ticksInRecipe = tag.getInt("totalTicks");
            this.getPersistentData().putInt("totalTicks", ticksInRecipe);
        }
        if(tag.contains("has_coffee")){
            hasCoffeeBeans = tag.getBoolean("has_coffee");
            this.getPersistentData().putBoolean("has_coffee", hasCoffeeBeans);
        }
        if(tag.contains("has_brewed")){
            hasOutput = tag.getBoolean("has_brewed");
            this.getPersistentData().putBoolean("has_brewed", hasOutput);
        }
    }

    public void tick(Level pLevel, BlockPos pos, BlockState state) {
        if(level.isClientSide()){
            return;
        }

        state = state.setValue(CoffeeMakerBlock.BREWED, !itemHandler.getStackInSlot(3).isEmpty());
        hasCoffeeBeans = !itemHandler.getStackInSlot(1).isEmpty();
        state = state.setValue(CoffeeMakerBlock.HAS_BEANS, hasCoffeeBeans);
        state = state.setValue(CoffeeMakerBlock.WATER_STORED, waterBottles);

        pLevel.setBlock(pos, state, 3);
        setChanged(pLevel, pos, state);

        if(itemHandler.getStackInSlot(0).is(ModTags.Items.COFFEE_BUCKET)){
            ItemStack replaceStack = new ItemStack(Items.BUCKET);
            float water = 1f;
            if (itemHandler.getStackInSlot(0).is(Items.POTION)){
                replaceStack = new ItemStack(Items.GLASS_BOTTLE);
                water = 1f/3f;
            }
            if(addWater(water)) {
                itemHandler.setStackInSlot(0, replaceStack);
            }
            sendUpdate();
        }
        if(recipeInAction()){
            ticksLeft -= 1;
//            System.out.println("Ticking Down...");
            if(!recipeInAction()){
//                System.out.println("Ticking done");
                craftRecipe(recipe);
            }
            else if (!recipeValid()){
//                System.out.println("Invalid recipe, removing");
                resetRecipe();
            }
        }
        else {
            startCraft();
        }
        sendUpdate();
//        updateData();
    }

    private void sendUpdate(){
        updateData();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        for (int x = 0; x < handlers.length; x++)
            handlers[x].invalidate();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("screen.fantasycoffee.coffee_maker");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        updateData();
        return new CoffeeMakerContainer(pContainerId, this, pPlayerInventory, pPlayer, data);
    }

    // BELOW: For Worldly Container

    @Override
    public int[] getSlotsForFace(Direction pSide) {
        return switch (pSide) {
            case DOWN -> new int[]{0, 3, 4};
            case EAST, SOUTH, WEST, NORTH -> new int[]{0};
            default -> new int[]{0, 1, 2, 3, 4};
        };
    }

    @Override
    public boolean canPlaceItem(int pIndex, ItemStack pStack) {
//        System.out.println("canPlaceItem: " + itemHandler.isItemValid(pIndex, pStack) + " of " + pStack + " in slot " + pIndex);
        return itemHandler.isItemValid(pIndex, pStack);

//        return switch (pIndex) {
//            case 0 -> pStack.is(ModTags.Items.COFFEE_BUCKET);
//            case 1 -> pStack.is(ModTags.Items.BEANS);
//            case 2 -> pStack.is(ModItems.MUG.get());
//            case 4 -> true;
//            default -> false;
//        };
    }

    @Override
    public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @org.jetbrains.annotations.Nullable Direction pDirection) {
//        System.out.println("canPlaceItemThroughFace was called");
//        System.out.println("result is: " + canPlaceItem(pIndex, pItemStack));
        return canPlaceItem(pIndex, pItemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
        if(pDirection == Direction.DOWN){
            if(pIndex == 0){
                if(pStack.is(Items.GLASS_BOTTLE) || pStack.is(Items.BUCKET)){
//                    System.out.println("canTakeItemThroughFace: " + true);
                    return true;
                }
            }
            else if (pIndex == 4){
//                System.out.println("canTakeItemThroughFace: " + pStack.is(Items.BUCKET));
                return pStack.is(Items.BUCKET);
            }
//            System.out.println("canTakeItemThroughFace: " + pIndex);
            return pIndex == 3;
        }
//        System.out.println("canTakeItemThroughFace: " + true);
        return true;
    }

    @Override
    public int getContainerSize() {
//        System.out.println("getContainerSize: " + this.itemHandler.getSlots());
        return this.itemHandler.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
//                System.out.println("isEmpty: " + false);
                return false;
            }
        }

//            System.out.println("isEmpty: " + true);
        return true;
    }

    @Override
    public ItemStack getItem(int pSlot) {
//        System.out.println("getItem: " + itemHandler.getStackInSlot(pSlot));
        return itemHandler.getStackInSlot(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        ItemStack s = itemHandler.extractItem(pSlot, pAmount, false);
//        System.out.println("removeItem: " + s);
        return s;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        ItemStack s = itemHandler.extractItem(pSlot, itemHandler.getStackInSlot(pSlot).getCount(), false);
//        System.out.println("removeItemNoUpdate: " + s);
        return s;
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        if(canPlaceItem(pSlot, pStack)){
//            System.out.println("Set stack " + pStack + " in slot " + pSlot);
            itemHandler.setStackInSlot(pSlot, pStack);
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
//        System.out.println("stillValid: " + Container.stillValidBlockEntity(this, pPlayer));
        return Container.stillValidBlockEntity(this, pPlayer);
    }

    @Override
    public void clearContent() {
        for(int i = 0; i < itemHandler.getSlots(); i++){
            itemHandler.setStackInSlot(i, new ItemStack(Items.AIR));
        }
//        System.out.println("clearContent");
    }
}


