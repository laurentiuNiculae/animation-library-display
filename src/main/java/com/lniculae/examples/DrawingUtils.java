package com.lniculae.examples;

import com.lniculae.Animation.Color;
import com.lniculae.Graph.Node;

public class DrawingUtils {
    public static Color GetNodeColor(Node node) {
        var value = (int)(node.Value/1.25) + 50;
        int r = value;
        int g = value;
        int b = value;

        return new Color(r, g, b, 255); 
    }
}
