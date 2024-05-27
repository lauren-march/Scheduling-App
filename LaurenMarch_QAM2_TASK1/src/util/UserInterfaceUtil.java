package util;

import javafx.collections.ListChangeListener;

public class UserInterfaceUtil {

    public static final TableColumnAdjuster adjuster = table -> {
        table.getColumns().forEach(column -> {
            column.setPrefWidth(column.getWidth());
            column.setMinWidth(110);
        });

        table.getItems().addListener((ListChangeListener<Object>) change ->
                table.getColumns().forEach(column -> {
                    column.setPrefWidth(column.getWidth());
                    column.setMinWidth(50);
                })
        );
    };
}
