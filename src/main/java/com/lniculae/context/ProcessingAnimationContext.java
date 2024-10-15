package com.lniculae.context;

import com.lniculae.Animation.Color;
import com.lniculae.Animation.Task;
import com.lniculae.Animation.Vec2;
import com.lniculae.Animation.Context.AnimationContext;

import processing.core.PApplet;

public class ProcessingAnimationContext extends PApplet implements AnimationContext {
    Task task;

    int windoWidth, windowHeight, fps;
    Color backgroundColor;

    public ProcessingAnimationContext(int width, int height, int fps) {
        this.windoWidth = (int) (width * 1.1);
        this.windowHeight = (int) (height * 1.1);
        this.fps = fps;
        this.backgroundColor = Color.BLACK;
    }

    public ProcessingAnimationContext(int width, int height, int fps, Color backgroundColor) {
        this.windoWidth = (int) (width * 1.1);
        this.windowHeight = (int) (height * 1.1);
        this.fps = fps;
        this.backgroundColor = backgroundColor;
    }

    public void settings() {
        size(windoWidth, windowHeight);
    }

    public void setup() {
        frameRate(fps);
    }

    public void draw() {
        clearBackground(backgroundColor);
        task.Draw((float)1/fps);

        if (task.Finished()) {
            task.Reset();
        }
    }

    public void display(Task task, int fps) {
        this.task = task;
        String[] args = {"ProcessingAnimationContext"};
        PApplet.runSketch(args, this);
    }

    public void drawLine(Vec2 startPos, Vec2 endPos, float thick, Color drawColor) {
        stroke(color(drawColor.r, drawColor.g, drawColor.b, drawColor.a));
        strokeWeight(thick);
        line(startPos.x, startPos.y, endPos.x, endPos.y);
    }

    public void drawCircle(Vec2 center, float radius, Color drawColor) {
        stroke(color(drawColor.r, drawColor.g, drawColor.b, drawColor.a));
        strokeWeight(1);
        fill(color(drawColor.r, drawColor.g, drawColor.b, drawColor.a));
        circle(center.x, center.y, radius*2);
    }

    public void clearBackground(Color drawColor) {
        background(color(drawColor.r, drawColor.g, drawColor.b, drawColor.a));
    }

    public int getWidth() {
        return windoWidth;
    }

    public int getHeight() {
        return windowHeight;
    }

    public int getFPS() {
        return fps;
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }
}
