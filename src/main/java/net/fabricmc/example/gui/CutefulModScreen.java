package net.fabricmc.example.gui;

import net.fabricmc.example.config.Config;
import net.fabricmc.example.config.Configs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.io.IOException;

public class CutefulModScreen extends Screen {

    private ButtonListWidget list;
    private final Configs configs;

    public CutefulModScreen() {
        super(new LiteralText("CutefulMod Options"));
        configs = Configs.getInstance();
    }

    @Override
    public void init() {
        //super.init(client, width, height);
        list = new ButtonListWidget(client, width, height, 32, this.height - 32, 25);

        int heightOfButton = 40;
        int widthOfButton = 170;
        for (Config config : configs.allConfigs) {
            this.addDrawableChild(new ButtonWidget(this.width / 2 - widthOfButton / 2, heightOfButton, widthOfButton, 20, new LiteralText(config.name + " : " + config.value), (buttonWidget) -> {
                config.value = !config.value;
                this.client.setScreen(new CutefulModScreen());
            }));
            heightOfButton += 24;
        }
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155 + 160, this.height - 29, 150, 20, new LiteralText("Done"), (buttonWidget) -> {
            this.client.setScreen(null);
        }));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155, this.height - 29, 150, 20, new LiteralText("Reset config"), (buttonWidget) -> {
            for (Config config : configs.allConfigs) {
                {
                    config.value = false;
                }
            }
            this.client.setScreen(new CutefulModScreen());
        }));
    }

    @Override
    public void onClose() {
        super.onClose();
        try {
            configs.saveToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(MatrixStack matrices, int mousex, int mousey, float delta) {
        this.renderBackgroundTexture(0);
        this.list.render(matrices, mousex, mousey, delta);
        assert client != null;
        drawCenteredText(matrices ,client.textRenderer, this.getNarratedTitle(), this.width / 2, 15, 16777215);
        super.render(matrices ,mousex, mousey, delta);
    }

    public boolean isPauseScreen() {return false;}
}
