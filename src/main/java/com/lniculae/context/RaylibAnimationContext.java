package com.lniculae.context;

import com.lniculae.Animation.Vec2;
import com.lniculae.Animation.Context.AnimationContext;
import com.raylib.java.Raylib;
import com.raylib.java.core.Color;
import com.raylib.java.raymath.Vector2;

public class RaylibAnimationContext extends Raylib implements AnimationContext {
    int width, height, fps;
    Color backgroundColor;

    public RaylibAnimationContext(int width, int height, int fps) {
        super();
        this.config.setSupportTracelog(false);
        this.config.setSupportTracelogDebug(false);

        this.width = width;
        this.height = height;
        this.fps = fps;
        this.backgroundColor = Color.BLACK;
    }

    public RaylibAnimationContext(int width, int height, int fps, Color backgroundColor) {
        super();
        this.width = width;
        this.height = height;
        this.fps = fps;
        this.backgroundColor = backgroundColor;
    }

    public void setBackgroundColor(com.lniculae.Animation.Color color) {
        backgroundColor = new Color(color.r, color.g, color.b, color.a);
    }

    public com.lniculae.Animation.Color getBackgroundColor() {
        return new com.lniculae.Animation.Color(
            backgroundColor.r, 
            backgroundColor.g, 
            backgroundColor.b, 
            backgroundColor.a
        );
    }

    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getFPS() {
        return fps;
    }

    public void drawLine(Vec2 startPos, Vec2 endPos, float thick, com.lniculae.Animation.Color color) {
        shapes.DrawLineEx(
            new Vector2(startPos.x, startPos.y), 
            new Vector2(endPos.x, endPos.y), 
            thick, 
            new Color(color.r, color.g, color.b, color.a)
        );
    }

    public void drawCircle(Vec2 center, float radius, com.lniculae.Animation.Color color) {
        shapes.DrawCircle(
            (int) center.x, (int) center.y, 
            radius,
            new Color(color.r, color.g, color.b, color.a)
        );
    }

    public void clearBackground(com.lniculae.Animation.Color color) {
        core.ClearBackground(new Color(color.r, color.g, color.b, color.a));
    }

    public void init() {
        core.InitWindow(width, height, "Raylib");
    }

    public void close() {
        core.CloseWindow();
    }

}
