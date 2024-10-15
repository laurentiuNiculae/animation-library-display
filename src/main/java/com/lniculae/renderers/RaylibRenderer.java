package com.lniculae.renderers;

import com.lniculae.Animation.Task;
import com.lniculae.context.RaylibAnimationContext;
import com.lniculae.AnimationOutput.AnimationFileRenderer;
import com.lniculae.AnimationOutput.AnimationRenderer;
import com.lniculae.Encoding.FFmpegEncoder;
import com.lniculae.Encoding.VideoEncoder;
import com.lniculae.Helper.Result;
import com.lniculae.Helper.Result.Empty;
import com.raylib.java.core.Color;
import com.raylib.java.textures.Image;

public class RaylibRenderer implements AnimationRenderer, AnimationFileRenderer {
    RaylibAnimationContext ctx;
    VideoEncoder videoEncoder;
    Task task;

    public RaylibRenderer(Task task, int width, int height, int fps) {
        this.ctx = new RaylibAnimationContext(width, height, fps);
        task.SetAnimationCtx(this.ctx);
        this.task = task;
        this.videoEncoder = new FFmpegEncoder(); 
    }

    @Override
    public Result<Empty> renderToScreen() {
        ctx.core.InitWindow(ctx.getWidth(), ctx.getHeight(), "Raylib-J Example");
        ctx.core.SetTargetFPS(ctx.getFPS());

        while (!ctx.core.WindowShouldClose()) {
            ctx.core.BeginDrawing();

            if (task.Finished()) {
                task.Reset();
            }

            try {
                var bc = ctx.getBackgroundColor();
                ctx.core.ClearBackground(new Color(bc.r, bc.g, bc.b, bc.a));
                task.Draw((float)1/ctx.getFPS());
            } catch (Exception e) {
                e.printStackTrace();
            }

            ctx.core.EndDrawing();
        }

        return Result.None;
    }

    @Override
    public Result<Empty> renderToFile(String path) {
        ctx.core.InitWindow(ctx.getWidth(), ctx.getHeight(), "Raylib-J Example");
        var renderTarget = ctx.textures.LoadRenderTexture(ctx.getWidth(), ctx.getHeight());

        var result = videoEncoder.startRendering(path, ctx.getWidth(), ctx.getHeight(), ctx.getFPS());
        if (!result.Ok()) {
            return result;
        }

        float dt = (float)1/ctx.getFPS();
        int i = 0;

        while (!task.Finished()) {
            ctx.core.BeginTextureMode(renderTarget);
            com.lniculae.Animation.Color c = ctx.getBackgroundColor();
            ctx.core.ClearBackground(new Color(c.r, c.g, c.b, c.a));
                task.Draw(dt);
            ctx.core.EndTextureMode();

            Image img = ctx.textures.LoadImageFromTexture(renderTarget.texture);
            ctx.textures.ImageFlipVertical(img);

            result = videoEncoder.writeFrame(img.getData());
            if (!result.Ok()) {
                return result;
            }

            System.out.println(i);
            i++;
        }

        result = videoEncoder.finalizeRendering();
        if (!result.Ok()) {
            return result;
        }

        task.Reset();

        ctx.core.CloseWindow();

        return Result.None;
    }
    
}
