package net.rawsome1234.fantasycoffee.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.rawsome1234.fantasycoffee.FantasyCoffee;
import net.rawsome1234.fantasycoffee.container.CoffeeMakerContainer;
import net.rawsome1234.fantasycoffee.util.ModTags;

import java.util.List;

public class CoffeeMakerScreen extends AbstractContainerScreen<CoffeeMakerContainer> {

    private final ResourceLocation GUI = new ResourceLocation(FantasyCoffee.MOD_ID, "textures/gui/coffee_maker_gui.png");

    public CoffeeMakerScreen(CoffeeMakerContainer p_i51105_1_, Inventory p_i51105_2_, Component p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
    }

    @Override
    public void render(GuiGraphics stack, int mouseX, int mouseY, float partialTicks) {
        // 139 52
        // 156 105
//        System.out.println("X: " + mouseX + ", Y: " + mouseY);
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);

        if (this.isHovering(14, 16, 18, 52, mouseX, mouseY)){
            List<Component> list = Lists.newArrayList();

            list.add(Component.translatable("screen.fantasycoffee.bottle_clue", menu.getBottles(), menu.getMaxBottles()));

            stack.renderComponentTooltip(this.font, list, mouseX, mouseY);
        }

    }

    @Override
    protected void renderBg(GuiGraphics stack, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, GUI);
//        this.minecraft.textureManager.bind(GUI);
        int i = this.getGuiLeft();
        int j = this.getGuiTop();
        stack.blit(GUI, i, j, 0, 0, this.getXSize(), this.getYSize());

        if(menu.getBottles() != -1){
            float l = ((float) menu.getBottles()) / menu.getMaxBottles();
            int pixelHeight = Math.round(l * 52);
//            System.out.println(pixelHeight);
            stack.blit(GUI, i + 14, j+16+52-pixelHeight, 176, 53-pixelHeight, 18, pixelHeight);
        }

        if(!menu.hasBeans()){
            stack.blit(GUI, i + 94, j + 11, 176, 104, 18, 18);
        }



        int ticksInRecipe = menu.getTotalTicks();

        if(ticksInRecipe != -1){
            float recipeProgress = 1 - (menu.getRemainingTicks() / ((float) ticksInRecipe));
            int pixelsPainted = Math.round(recipeProgress * 23);
//            System.out.println("Pixels: " + pixelsPainted + "\nRecipe Progress: " + recipeProgress);
            stack.blit(GUI, i+111, j+17, 176, 53, pixelsPainted, 51);
        }


    }

}
