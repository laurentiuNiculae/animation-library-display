package com.lniculae.examples;

import java.util.HashMap;

import com.raylib.java.core.Color;
import com.raylib.java.core.input.Keyboard;
import com.raylib.java.raymath.Raymath;

import com.lniculae.Animation.*;
import com.lniculae.context.RaylibAnimationContext;
import com.lniculae.Graph.Graph;

public class Example1 {
    static int Width = 800;
    static int Height = 600;

    Graph graph;

    public Example1() {

        graph = new Graph() {
            {
                setNode(1, 5);
                setNode(2, 25);
                setNode(3, 50);
                setNode(4, 100);
                setNode(5, 200);
                setNode(6, 250);
                setEdge(1, 2);
                setEdge(1, 3);
                setEdge(1, 4, 32);
                setEdge(2, 3);
                setEdge(4, 1);
            }
        };
    }
    
    public void RunExample() {
        var ctx = new RaylibAnimationContext(Width, Height, 60);

        ctx.core.InitWindow(ctx.getWidth(), ctx.getHeight(), "Raylib-J Example");
        int nodeWidth = 20;
        var centerPoint = new Vec2(Width / 2, Height / 2);
        int radius = Height / 3;

        var background = new Color(18, 18, 18, 255);

        float angle = 0;
        float deltaAngle = 2 * Raymath.PI / graph.nodeCount();
        var startPoint = new Vec2(radius, 0);
        var nodePositions = new HashMap<Integer, Vec2>();

        for (var node : graph.Nodes()) {
            var point = Vec2.Rotate(startPoint, angle);
            nodePositions.put(node.Id, new Vec2(point.x + centerPoint.x, point.y + centerPoint.y));

            angle += deltaAngle;
        }

        boolean canStart = false;
        float timeElapsed = 0;

        var lineRenderTask = new DrawLine(
                new Vec2(100, 100),
                new Vec2(300, 300),
                2).setEasingFunction(Example1::easeOutBounce);
        lineRenderTask.SetAnimationCtx(ctx);

        ctx.core.SetTargetFPS(60);

        while (!ctx.core.WindowShouldClose()) {
            ctx.core.BeginDrawing();
            ctx.core.ClearBackground(background);

            float dt = ctx.core.GetFrameTime();
            timeElapsed += dt;

            if (ctx.core.IsKeyDown(Keyboard.KEY_E)) {
                canStart = true;
                lineRenderTask.Reset();
                timeElapsed = 0;
            }

            if (canStart) {
                lineRenderTask.Draw(dt);

                for (var edge : graph.Edges()) {
                    var drawLine = new DrawLine(ctx,
                        nodePositions.get(edge.From.Id),
                        nodePositions.get(edge.To.Id),
                        2
                        );
                    
                    drawLine.setEasingFunction(Example1::easeOutBounce)
                    .setTimeElapsed(timeElapsed)
                    .Draw(0);
                }

                for (var node : graph.Nodes()) {
                    var color = DrawingUtils.GetNodeColor(node);
                    ctx.drawCircle(nodePositions.get(node.Id), nodeWidth, color);
                }
            }

            ctx.core.EndDrawing();
        }
    }

    static public float easeOutBounce(float x) {
        float n1 = (float) 7.5625;
        float d1 = (float) 2.75;

        if (x < 1 / d1) {
            return n1 * x * x;
        } else if (x < 2 / d1) {
            return (float) (n1 * (x -= 1.5 / d1) * x + 0.75);
        } else if (x < 2.5 / d1) {
            return (float) (n1 * (x -= 2.25 / d1) * x + 0.9375);
        } else {
            return (float) (n1 * (x -= 2.625 / d1) * x + 0.984375);
        }
    }
}
