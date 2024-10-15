package com.lniculae.renderers;

import com.lniculae.Animation.Task;
import com.lniculae.Animation.Context.ProcessingAnimationContext;
import com.lniculae.AnimationOutput.AnimationRenderer;
import com.lniculae.Helper.Result;
import com.lniculae.Helper.Result.Empty;

public class ProcessingRenderer implements AnimationRenderer {
    ProcessingAnimationContext ctx;
    Task task;

    public ProcessingRenderer(Task task, int width, int height, int fps) {
        this.ctx = new ProcessingAnimationContext(width, height, fps);
        task.SetAnimationCtx(this.ctx);
        this.task = task;
    }

    @Override
    public Result<Empty> renderToScreen() {
        ctx.display(task, ctx.getFPS());

        return Result.None;
    }

}
