package util;

import javafx.collections.ListChangeListener;

/**
 * This class is for methods regarding UI handling.
 */
public class UserInterfaceUtil {

    /**
     * This lambda adjusts the table column width for a more clean tableview look.
     */
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
