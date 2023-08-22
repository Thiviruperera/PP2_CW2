package train;


import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PassengerQueueUI {


    //Class containing grids
    private PassengerQueueDisplay passengerQueueDisplay;
    private int queueCount = 0;
    private TrainStation trainStation;

    public PassengerQueueUI(TrainStation trainStation) {
        this.trainStation = trainStation;
        passengerQueueDisplay = new PassengerQueueDisplay();
    }

    private static Rectangle createRectangles(Color color) {
        Rectangle rectangle = new Rectangle(100, 100);

        rectangle.setStroke(Color.BLACK);
        rectangle.setFill(color);
        rectangle.setArcWidth(15.0);
        rectangle.setArcHeight(15.0);
        rectangle.setHeight(40);
        rectangle.setWidth(60);
        return rectangle;
    }


    //Class responsible for displaying the grid containing the Rectangles
    public class PassengerQueueDisplay {


        GridPane bottomPart = new GridPane();
        private int nRows = 42;
        private int nCols = 1;

        public PassengerQueueDisplay() {
            bottomPart.setHgap(70);
            bottomPart.setVgap(10);
            setColumns(nCols);
            setRows(nRows);
        }

        public void setColumns(int newColumns) {
            nCols = newColumns;
            createElements();
        }

        public void setRows(int newRows) {
            nRows = newRows;
            createElements();
        }

        public GridPane getDisplay() {
            return bottomPart;
        }

        private void createElements() {
            bottomPart.getChildren().clear();
            //create a button

            Label lb;
            for (int i = 0; i < nRows; i++) {

                int number = 1 * i + 1;

                if (number <= queueCount) {
                    bottomPart.add(createRectangles(Color.RED), 0, i);
                    lb = new Label(TrainStation.getPassengerQueue().getItem(number - 1).getFirstName());
                    GridPane.setHalignment(lb, HPos.CENTER);
                    bottomPart.add(lb, 0, i);

                    String seatNo = String.valueOf(TrainStation.getPassengerQueue().getItem(number - 1).getSeatNo());
                    lb = new Label(seatNo);
                    lb.setPadding(new Insets(0, 0, 0, 5));
                    bottomPart.add(lb, 1, i);
                } else {
                    bottomPart.add(createRectangles(Color.GRAY), 0, i);

                    lb = new Label(Integer.toString(number));
                    lb.setPadding(new Insets(0, 0, 0, 20));
                    bottomPart.add(lb, 0, i);

                    lb = new Label("Empty");
                    lb.setPadding(new Insets(0, 0, 0, 5));
                    bottomPart.add(lb, 1, i);
                }

            }

            Label label = new Label("PASSENGER QUEUE");
            label.setStyle("-fx-font-weight: bold;-fx-border-color: red;-fx-border-width:5px;" +
                    " -fx-border-radius: 10 10 10 10; -fx-padding: 10 80 10 80;");
            HBox hb = new HBox(label);
            hb.setStyle("-fx-font-size:20px");
            bottomPart.add(hb, 7, 0);
        }


    }

    public void setQueueCount(int queueCount) {
        this.queueCount = queueCount;
        passengerQueueDisplay.createElements();
    }

    public int getQueueCount() {
        return queueCount;
    }



    public PassengerQueueDisplay getPassengerQueueDisplay() {
        return passengerQueueDisplay;
    }
}
