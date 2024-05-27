package util;

import javafx.scene.control.TableView;

@FunctionalInterface
public interface TableColumnAdjuster {
    void adjustColumns(TableView<?> table);
}