package train;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class AddUserUI {


    //Class containing grids
    private WaitingRoomDisplay waitingRoomDisplay;
    private PassengerQueueDisplay passengerQueueDisplay;


    private int queueCount = 0;
    private int waitingRoomCount = 0;
    private int calledPassengersCount = 0;

    private TrainStation trainStation;

    public AddUserUI(TrainStation trainStation) {
        this.trainStation = trainStation;
        waitingRoomDisplay = new WaitingRoomDisplay();
        passengerQueueDisplay = new PassengerQueueDisplay();
    }

    //Class responsible for displaying the grid containing the Rectangles
    public class WaitingRoomDisplay {


        GridPane topPart = new GridPane();
        private int nRows = 7;
        private int nCols = 7;

        public WaitingRoomDisplay() {
            topPart.setHgap(70);
            topPart.setVgap(10);
            setColumns(nCols);
            setRows(nRows);
        }

        public void setColumns(int newColumns) {
            nCols = newColumns;
        }

        public void setRows(int newRows) {
            nRows = newRows;
        }


        public GridPane getDisplay() {
            return topPart;
        }

        private void createElements() {
            topPart.getChildren().clear();
            Label lb;
            topPart.setMaxSize(150.0, 100.0);
            topPart.setMinSize(150.0, 100.0);

            for (int i = 1; i < nRows; i++) {
                for (int j = 0; j < nCols; j++) {
                    int number = 7 * (i - 1) + j + 1;


                    if (number <= waitingRoomCount) {
                        topPart.add(createRectangles(Color.RED), j, i);
                        lb = new Label(TrainStation.getWaitingRoomArray().get(number - 1).getFirstName());
                        lb.setPadding(new Insets(0, 0, 0, 5));
                        topPart.add(lb, j, i);
                    } else {
                        topPart.add(createRectangles(Color.GRAY), j, i);
                        lb = new Label("Empty");
                        lb.setPadding(new Insets(0, 0, 0, 5));
                        topPart.add(lb, j, i);
                    }

                }
            }

            Label label = new Label("Waiting List");
            label.setStyle("-fx-font-weight: bold;-fx-border-color: red;-fx-border-width:5px;" +
                    " -fx-border-radius: 10 10 10 10; -fx-padding: 10 80 10 80;");
            HBox hb = new HBox(label);
            hb.setStyle("-fx-font-size:20px");
            topPart.add(hb, 7, 0, 7, 1);

        }


    }

    private static Rectangle createRectangles(Color color) {
        Rectangle rectangle = new Rectangle(150, 150);

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
        private int nRows = 7;
        private int nCols = 7;

        public PassengerQueueDisplay() {
            bottomPart.setHgap(70);
            bottomPart.setVgap(10);
            setColumns(nCols);
            setRows(nRows);
        }

        public void setColumns(int newColumns) {
            nCols = newColumns;
        }

        public void setRows(int newRows) {
            nRows = newRows;
        }

        public GridPane getDisplay() {
            return bottomPart;
        }

        private void createElements() {
            bottomPart.getChildren().clear();
            //create a button
            Button b = new Button("Add passengers");
            b.addEventHandler(MouseEvent.MOUSE_CLICKED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            calledPassengersCount = trainStation.addPassengerToQueue();
                            waitingRoomCount = TrainStation.getWaitingRoomArray().size();
                            queueCount = TrainStation.getPassengerQueue().size();
                            waitingRoomDisplay.createElements();
                            passengerQueueDisplay.createElements();
                        }
                    });
            Label lb;
            for (int i = 1; i < nRows; i++) {
                for (int j = 0; j < nCols; j++) {
                    int number = 7 * (i - 1) + j + 1;

                    bottomPart.setMaxSize(150.0, 100.0);
                    bottomPart.setMinSize(150.0, 100.0);

                    if (number <= queueCount) {
                        bottomPart.add(createRectangles(Color.RED), j, i);

                        lb = new Label(TrainStation.getPassengerQueue().getItem(number - 1).getFirstName());
                        lb.setPadding(new Insets(0, 0, 0, 5));
                        bottomPart.add(lb, j, i);
                    } else {
                        bottomPart.add(createRectangles(Color.GRAY), j, i);
                        lb = new Label("Empty");
                        lb.setPadding(new Insets(0, 0, 0, 5));
                        bottomPart.add(lb, j, i);
                    }
                }
            }
            Label labelPassengersCount;
            if (!TrainStation.getPassengerQueue().isFull() && !TrainStation.getWaitingRoomArray().isEmpty()) {
                labelPassengersCount = new Label("Calling Passengers: " + calledPassengersCount);
                labelPassengersCount.setStyle("-fx-font-weight: bold;-fx-font-size:20px;");
            } else if (TrainStation.getWaitingRoomArray().isEmpty() && !TrainStation.getPassengerQueue().isFull()) {
                labelPassengersCount = new Label("Waiting room is empty");
                labelPassengersCount.setStyle("-fx-font-weight: bold;-fx-font-size:20px;");
            } else {
                labelPassengersCount = new Label("Passenger Queue is full");
                labelPassengersCount.setStyle("-fx-font-weight: bold;-fx-font-size:20px;");
            }

            bottomPart.add(labelPassengersCount, 7, 2, 5, 1);

            bottomPart.add(b, 7, 8, 5, 1);

            Label label = new Label("QUEUE");
            label.setStyle("-fx-font-weight: bold;-fx-border-color: red;-fx-border-width:5px;" +
                    " -fx-border-radius: 10 10 10 10; -fx-padding: 10 80 10 80;");
            HBox hb = new HBox(label);
            hb.setStyle("-fx-font-size:20px");

//            bottomPart.add(hb, 7, 0);
            bottomPart.add(hb, 7, 0, 5, 1);
        }


    }


    public void setQueueWaitingRoomCount(int queueCount, int waitingRoomCount) {
        this.queueCount = queueCount;
        this.waitingRoomCount = waitingRoomCount;
        passengerQueueDisplay.createElements();
        waitingRoomDisplay.createElements();
    }


    public WaitingRoomDisplay getWaitingRoomDisplay() {
        return waitingRoomDisplay;
    }

    public PassengerQueueDisplay getPassengerQueueDisplay() {
        return passengerQueueDisplay;
    }
}
