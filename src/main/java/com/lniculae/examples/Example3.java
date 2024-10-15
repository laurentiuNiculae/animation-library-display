package com.lniculae.examples;

import java.util.HashMap;

import com.lniculae.Animation.*;
import com.lniculae.Animation.Context.AnimationContext;
import com.lniculae.AnimationOutput.AnimationRenderer;
import com.lniculae.renderers.RaylibRenderer;
import com.lniculae.Graph.Graph;
import com.raylib.java.raymath.Raymath;

public class Example3 {
    static int Width = 800;
    static int Height = 600;

    Graph graph;

    public Example3() {
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
                setEdge(3, 4);
                setEdge(4, 6);
                setEdge(4, 5);
                setEdge(5, 6);
                setEdge(5, 1);
                setEdge(4, 2);
                setEdge(6, 1);
            }
        };
    }

    private Task GetAnimation(AnimationContext ctx) {
        var centerPoint = new Vec2(Width / 2, Height / 2);
        int radius = Height / 3;
        float angle = 0;
        float deltaAngle = 2 * Raymath.PI / graph.nodeCount();
        var startPoint = new Vec2(radius, 0);
        
        int nodeWidth = 20;
        var nodePositions = new HashMap<Integer, Vec2>();

        var mainAnimation = new Syncronous(ctx);
            var drawNodes = new Syncronous();
            var drawNodesInstant = new Syncronous();

            for (var node : graph.Nodes()) {
                var rotationOffset = Vec2.Rotate(startPoint, angle);
                var point = new Vec2(rotationOffset.x + centerPoint.x, rotationOffset.y + centerPoint.y);
                nodePositions.put(node.Id, point);

                drawNodes.addTask(
                    new DrawNode(point, nodeWidth, DrawingUtils.GetNodeColor(node), 1)
                        .radius(1, nodeWidth, Example3::easeOutBounce)
                );
                drawNodesInstant.addTask(
                    new DrawNode(point, nodeWidth, DrawingUtils.GetNodeColor(node), 0)
                );

                angle += deltaAngle;
            }

            var drawLinesSync = new PersistentSecvential();

            for (var edge : graph.Edges()) {
                var drawLine = new DrawLine(
                    nodePositions.get(edge.From.Id), nodePositions.get(edge.To.Id), (float) 0.25
                ).setEasingFunction(Example3::easeOut)
                 .setLineWidth(10, 2, Example3::easeOutBounce);
                
                drawLinesSync.addTask(drawLine);
            }
            drawLinesSync.addTask(new EmptyTask());

            var backgroundColor = new DrawBackground(
                Color.DARKBLUE,
                Color.BLUE,
                1
            ).setEasingFunc(Example3::easeOut);

        mainAnimation.addTask(backgroundColor);
        mainAnimation.addTask(new PersistentSecvential(){{
            addTask(drawNodes);
            addTask(new Syncronous() {{
                addTask(drawLinesSync);
                addTask(drawNodesInstant);
            }});
        }});

        var waitWrapper = new Secvential(ctx) {{
            addTask(new DrawBackground(Color.BLACK, Color.DARKBLUE, 0.75f));
            addTask(new DrawWait(0.5f));
            addTask(new PersistentSecvential(){{
                addTask(mainAnimation);
                addTask(new DrawBackground(Color.BLUE, Color.DARKBLUE, 0.75f));
                addTask(new DrawBackground(Color.DARKBLUE, Color.BLACK, 0.75f));
                addTask(new DrawWait(0.5f));
            }});
        }};

        return waitWrapper;
    }

    public void RunExample() {
        var animation = GetAnimation(null);
        // AnimationRenderer renderer = new ProcessingRenderer(animation, Width, Height, 60);
        AnimationRenderer renderer = new RaylibRenderer(animation, Width, Height, 60);

        renderer.renderToScreen();
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

    static public float easeOut(float x) {
        return x == 1 ? 1 : 1 - (float) Math.pow(2, -10 * x);
    }
    
}