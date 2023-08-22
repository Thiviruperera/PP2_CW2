package train;


import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ReportUI {

    private PassengerQueueGridDisplay passengerQueueGridDisplay;
    private int queueCount = 0;

    private TrainStation trainStation;

    public ReportUI(TrainStation trainStation) {
        this.trainStation = trainStation;
        passengerQueueGridDisplay = new PassengerQueueGridDisplay();

    }

    public class PassengerQueueGridDisplay {

        private static final double ELEMENT_SIZE = 400;

        private TilePane tilePane = new TilePane();
        private Group display = new Group(tilePane);
        private int nRows = 5;
        private int nCols = 9;

        public PassengerQueueGridDisplay() {
            tilePane.setHgap(2);
            tilePane.setVgap(2);
            tilePane.setPrefColumns(nCols);
            tilePane.setPrefRows(nRows);
            createElements();
        }


        public Group getDisplay() {
            return display;
        }

        private void createElements() {
            tilePane.getChildren().clear();
            for (int i = 0; i < nRows; i++) {
                for (int j = 0; j < nCols; j++) {
                    int number = 9 * i + j + 1;
                    if (number <= queueCount) {
                        tilePane.getChildren().add(createElement(number, TrainStation.getPassengerQueue().getItem(number - 1)));
                    }
                }
            }
        }


        private StackPane createElement(int idx, Passenger passenger) {
            Label pasengerNoLabel = new Label("Passenger ");
            Label firstNameLabel = new Label("First Name: ");
            Label surnameLabel = new Label("Surname: ");
            Label waitingTimeLabel = new Label("Waiting time in queue: ");
            Label seatNoLabel = new Label("Seat No: ");

            Label passengerNoValue = new Label(String.valueOf(idx));
            Label firstNameValue = new Label(passenger.getFirstName());
            Label surnameValue = new Label(passenger.getSurname());
            Label waitingTimeValue = new Label(passenger.getSecondsInQueue() + "s");
            Label seatNoValue = new Label(String.valueOf(passenger.getSeatNo()));

            StackPane sp = new StackPane();
            AnchorPane anchorPane = new AnchorPane();

            Rectangle rectangle = new Rectangle(ELEMENT_SIZE, ELEMENT_SIZE, Color.LIGHTGREY);
            rectangle.setStroke(Color.BLACK);
            rectangle.setHeight(100);
            rectangle.setWidth(165);
            rectangle.setArcWidth(15.0);
            rectangle.setArcHeight(15.0);
            anchorPane.setPrefSize(120, 120);

            Rectangle titleRect = new Rectangle(165, 30, Color.web("#1c89f4"));
            titleRect.setArcWidth(10.0);
            titleRect.setArcHeight(10.0);
            anchorPane.getChildren().addAll(titleRect,pasengerNoLabel,passengerNoValue,firstNameLabel,firstNameValue,surnameLabel,surnameValue,
                    waitingTimeLabel,waitingTimeValue,seatNoLabel,seatNoValue);

            AnchorPane.setTopAnchor(pasengerNoLabel, Double.valueOf(2));
            AnchorPane.setLeftAnchor(pasengerNoLabel, Double.valueOf(2));
            AnchorPane.setTopAnchor(passengerNoValue, Double.valueOf(2));
            AnchorPane.setLeftAnchor(passengerNoValue, Double.valueOf(85));


            AnchorPane.setTopAnchor(firstNameLabel, Double.valueOf(32));
            AnchorPane.setLeftAnchor(firstNameLabel, Double.valueOf(2));
            AnchorPane.setTopAnchor(firstNameValue, Double.valueOf(32));
            AnchorPane.setLeftAnchor(firstNameValue, Double.valueOf(85));


            AnchorPane.setTopAnchor(surnameLabel, Double.valueOf(52));
            AnchorPane.setLeftAnchor(surnameLabel, Double.valueOf(2));
            AnchorPane.setTopAnchor(surnameValue, Double.valueOf(52));
            AnchorPane.setLeftAnchor(surnameValue, Double.valueOf(70));

            AnchorPane.setTopAnchor(waitingTimeLabel, Double.valueOf(72));
            AnchorPane.setLeftAnchor(waitingTimeLabel, Double.valueOf(2));
            AnchorPane.setTopAnchor(waitingTimeValue, Double.valueOf(72));
            AnchorPane.setLeftAnchor(waitingTimeValue, Double.valueOf(130));

            AnchorPane.setTopAnchor(seatNoLabel, Double.valueOf(92));
            AnchorPane.setLeftAnchor(seatNoLabel, Double.valueOf(2));
            AnchorPane.setTopAnchor(seatNoValue, Double.valueOf(92));
            AnchorPane.setLeftAnchor(seatNoValue, Double.valueOf(65));
            sp.getChildren().addAll(rectangle,anchorPane);

            return sp;
        }

    }

    public PassengerQueueGridDisplay getPassengerQueueGridDisplay() {
        return passengerQueueGridDisplay;
    }

    public void setQueueCount(int queueCount) {
        this.queueCount = queueCount;
        passengerQueueGridDisplay.createElements();
    }

}