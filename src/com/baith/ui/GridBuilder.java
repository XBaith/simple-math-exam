package com.baith.ui;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

/**
 * 表格布局建造者
 * 用于构建填充问题列表表格
 */
public class GridBuilder implements Builder<GridPane> {

    private GridPane gridPane = new GridPane();

    public GridBuilder buildVHGap(int vgap, int hgap) {
        gridPane.setVgap(vgap);
        gridPane.setHgap(hgap);
        return this;
    }

    public GridBuilder full(Node[][] nodes, Class<?> clazz) {
        for(int r = 0; r < nodes.length; r++) {
            for(int c = 0; c < nodes[0].length; c++) {
                try {
                    nodes[r][c] = (Node) clazz.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                gridPane.add(nodes[r][c], c, r);
            }
        }
        return this;
    }

    public GridBuilder buildGridLinesVisible(boolean visible) {
        gridPane.setGridLinesVisible(visible);
        return this;
    }

    @Override
    public GridPane build() {
        return gridPane;
    }

}
