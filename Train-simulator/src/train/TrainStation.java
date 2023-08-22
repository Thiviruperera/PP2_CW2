package train;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrainStation extends Application {

    private static final String PROJECT_PATH = "D:\\A\\Train-simulator\\src\\train\\";
    //private static final String DB_FILE_NAME = "D:\\PP2 CW Submission\\Code\\W1761355\\AllSystemBookings.txt"; //  file to store passenger details for waiting list from cw1
    private static final String DB_FILE_NAME = PROJECT_PATH + "AllSystemBookings.txt"; //  file to store passenger details for waiting list from hard code

    private static final String TRAIN_QUEUE_FILE_NAME = PROJECT_PATH + "TrainQueue.txt"; //  file to store queue details
    private static final String QUEUE_STAT_FILE_NAME = PROJECT_PATH + "QueueStat.txt"; // file to store queue statistics details

    private static ArrayList<Passenger> WAITING_ROOM_ARRAY = new ArrayList<Passenger>();  //This is the set of seats in this system,so this should be global
    private static PassengerQueue passengerQueue;

    private AddUserUI addUserUI;
    private PassengerQueueUI passengerQueueUI;
    private ReportUI reportUI;

    private AddUserUI.WaitingRoomDisplay waitingRoomDisplay;
    private AddUserUI.PassengerQueueDisplay passengerQueueDisplay;

    private PassengerQueueUI.PassengerQueueDisplay passengerQueueUIDisplay;

    private ReportUI.PassengerQueueGridDisplay passengerQueueGridDisplay;

    public static TrainStation ts;

    public static void main(String[] args) {
        ts = new TrainStation();

        //loading passengeer data into waiting list
        ts.loadPassengerData();
        ts.runThis();

    }

    //Loading the data from a file
    private void loadPassengerData() {

        File file = new File(DB_FILE_NAME);//opening the file

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = "_";
        try {
            br = new BufferedReader(new FileReader(file));
            int userId = 1;
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] records = line.split(cvsSplitBy);
                String[] names = records[1].split(" ");
                addPassengersToWaitingRoom(userId, names[0], names[1], Integer.valueOf(records[0]));
                ++userId;
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR:- No File Found...");
            return;
        } catch (IOException e) {
        }
        System.out.println("Loading Data : Success");
    }

    //Adding a seat to the array
    private void addPassengersToWaitingRoom(int id, String firstName, String surname, int seatNo) {

        if (WAITING_ROOM_ARRAY.size() < 43) {
            Passenger passenger = new Passenger();
            passenger.setId(id);
            passenger.setName(firstName, surname);
            passenger.setSeatNo(seatNo);

            WAITING_ROOM_ARRAY.add(passenger);
            Collections.sort(WAITING_ROOM_ARRAY);
        }


    }

    //return rendom number(dice)
    public static int getRandomInteger(int maximum, int minimum) {
        return ((int) (Math.random() * (maximum - minimum))) + minimum;
    }

    //display passenger details in waiting list
    private void viewWaitingRoom() {
        Iterator iterator = WAITING_ROOM_ARRAY.iterator();

        System.out.println("Waitng room Details : ");

        while (iterator.hasNext()) {
            Passenger p = (Passenger) iterator.next();
            System.out.println(p.getId() + "," + p.getName() + "," + p.getSecondsInQueue());
        }
    }

    //display passenger details in queue
    private void showQueue() {
        System.out.println("This is the list of all passengers: ");
        for (int i = 0; i < passengerQueue.size(); i++)//Iterating the booking array
        {
            System.out.println("(" + passengerQueue.getItem(i).getId() + ")" + passengerQueue.getItem(i).getName());
        }
    }

    //searching a given customer from the array list using name and delete
    private void deletePassenger(int id) {
        boolean isCusFound = false;
        ArrayList<Passenger> temp = new ArrayList<Passenger>();  //This is the set of seats in this system,so this should be global

        for (int i = 0; i < passengerQueue.size(); i++) {
            if (passengerQueue.getItem(i).getId() != id) {
                temp.add(passengerQueue.getItem(i));
            } else {
                isCusFound = true;
                System.out.println("Deleted " + passengerQueue.getItem(i).getName());
            }
        }
        passengerQueue.removeAll();

        temp.stream().forEach((temp1) -> {
            passengerQueue.enqueue(temp1);
        });

        if (isCusFound == false) {
            System.out.println("No customer found");
        }
    }

    //Loading passenger details of queue from a file
    private void loadQueueData() {

        File file = new File(TRAIN_QUEUE_FILE_NAME);//opening the file

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String st = "";
            int passengerId = 1;
            while ((st = br.readLine()) != null) { //read line by line
                StringTokenizer stok = new StringTokenizer(st);  //Extracting both seatID and Customer name
                int seatId = Integer.parseInt(stok.nextToken("_"));
                String cusName = stok.nextToken("_");
                Passenger passenger = new Passenger();
                passenger.setId(passengerId);
                passenger.setSeatNo(seatId);
                passenger.setName(cusName.split(" ")[0], cusName.split(" ")[1]);
                passengerQueue.enqueue(passenger);
                if (WAITING_ROOM_ARRAY.contains(passenger)) {
                    WAITING_ROOM_ARRAY.remove(WAITING_ROOM_ARRAY.indexOf(passenger));
                    Collections.sort(WAITING_ROOM_ARRAY);
                }
                ++passengerId;
            }


        } catch (FileNotFoundException e) {
            System.out.println("ERROR:- No File Found...");
            return;
        } catch (IOException e) {
        }
        System.out.println("Loading Data : Success");
    }

    //Clear the file content
    private void clearFile(String file) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(file);
        writer.print("");
        writer.close();
    }

    //add given details to the file
    private void addPassengerToFile(String line) throws IOException {

        FileWriter fw = new FileWriter(TRAIN_QUEUE_FILE_NAME, true);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write(line);
        bw.newLine();

        bw.close();
        fw.close();
    }

    //saving given passenger details of queue to the file
    private void saveQueueData() {
        System.out.println("Saving Queue Data");
        try {
            this.clearFile(TRAIN_QUEUE_FILE_NAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < passengerQueue.size(); i++) {
            String dataLine = passengerQueue.getItem(i).getSeatNo()
                    + "_" + passengerQueue.getItem(i).getName();
            try {
                System.out.println(dataLine);
                this.addPassengerToFile(dataLine);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Saving Queue Data : Success");
    }

    //return statistics of queue
    public HashMap<String, String> getStatistics() {

        int lengthOfQueue = passengerQueue.size();
        int minWaiting = passengerQueue.peek().getSecondsInQueue();
        int maxWaiting = passengerQueue.peek().getSecondsInQueue();
        int totalWaiting = 0;
        double avgWaiting = 0;
        StringBuilder passengerDetaiils = new StringBuilder();
        passengerDetaiils.append("----Passenger Details----\n\n");

        for (int i = 0; i < passengerQueue.size(); i++) {
            Passenger passenger = passengerQueue.getItem(i);
            passengerDetaiils.append("Passenger Name(" + (i + 1) + "): " + passenger.getName() + "\n");
            passengerDetaiils.append("Waiting time: " + passenger.getSecondsInQueue() + "\n");
            passengerDetaiils.append("Seat No: " + passenger.getSeatNo() + "\n\n");
            if (passenger.getSecondsInQueue() > maxWaiting) {
                maxWaiting = passenger.getSecondsInQueue();
            } else if (passenger.getSecondsInQueue() < minWaiting) {
                minWaiting = passenger.getSecondsInQueue();
            }
            totalWaiting = totalWaiting + passenger.getSecondsInQueue();
        }
        avgWaiting = (double) totalWaiting / (double) passengerQueue.size();
        StringBuilder dataLine = new StringBuilder();
        dataLine.append("----Summery----\n\n");
        dataLine.append("Length of the queue:" + lengthOfQueue + "\n");
        dataLine.append("Minimum Waiting:" + minWaiting + "s\n");
        dataLine.append("Maximum Waiting:" + maxWaiting + "s\n");
        dataLine.append("Average Waiting:" + avgWaiting + "s\n");
        dataLine.append("\n----End of Summary----\n\n");

        dataLine.append(passengerDetaiils.toString());

        try {
            addStatisticsToFile(dataLine.toString());
            setWaitingRoomArray(new ArrayList<Passenger>());
            ts.loadPassengerData();
            passengerQueue.removeAll();

        } catch (IOException ex) {
            Logger.getLogger(TrainStation.class.getName()).log(Level.SEVERE, null, ex);
        }

        HashMap<String, String> statisticsHmap = new HashMap<String, String>();

        /*Adding elements to HashMap*/
        statisticsHmap.put("lengthOfQueue", Integer.toString(lengthOfQueue));
        statisticsHmap.put("minWaiting", Integer.toString(minWaiting));
        statisticsHmap.put("maxWaiting", Integer.toString(maxWaiting));
        statisticsHmap.put("avgWaiting", Double.toString(avgWaiting));
        return statisticsHmap;
    }

    //add given statistics details to the file
    private void addStatisticsToFile(String line) throws IOException {
        FileWriter fw = new FileWriter(QUEUE_STAT_FILE_NAME, true);
        BufferedWriter bw = new BufferedWriter(fw);

        System.out.println("Saving Data");
        try {
            this.clearFile(QUEUE_STAT_FILE_NAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            System.out.println(line);

            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Saving Data : Success");

        bw.close();
        fw.close();
    }

    public void runThis() {
        passengerQueue = new PassengerQueue(42);
        String option = "";
        do { //This is the main loop
            this.showMenu();
            Scanner consoleScanner = new Scanner(System.in);
            System.out.print("Enter your option: ");
            option = consoleScanner.nextLine();
            option = option.toUpperCase();

            if (option.equals("A")) {
                launch(option);

            } else if (option.equals("V")) {
                this.showQueue();
                launch(option);
            } else if (option.equals("D")) {
                System.out.print("Queue is empty");

            } else if (option.equals("S")) {
                System.out.print("Queue is empty");
            } else if (option.equals("R")) {

                if (passengerQueue.isEmpty()) {
                    System.out.println("Queue is empty");
                }

            } else if (option.equals("L")) {
                this.loadQueueData();
            } else if (option.equals("Q")) {
                System.out.println("Exiting from the program");
                System.exit(0);
            } else {
                System.out.println("Please Enter Valid Input");
                continue;
            }

        } while (!option.equals("Q"));

    }

    //Adding passengers to the queue
    public int addPassengerToQueue() {
        int noOfPassengers = getRandomInteger(7, 1);
        int waitingRoomCount = WAITING_ROOM_ARRAY.size();
        if (waitingRoomCount >= noOfPassengers && !passengerQueue.isFull()) {
            for (int i = 0; i < noOfPassengers; i++) {
                Passenger passenger = WAITING_ROOM_ARRAY.get(i);
                //add waiting time in the queue by dice
                passenger.setSecondsInQueue(getRandomInteger(7, 1) + getRandomInteger(7, 1) + getRandomInteger(7, 1));

                passengerQueue.enqueue(passenger);
            }
            WAITING_ROOM_ARRAY.subList(0, noOfPassengers).clear();
        } else if (waitingRoomCount < noOfPassengers && waitingRoomCount > 0 && !passengerQueue.isFull()) {
            for (int i = 0; i < waitingRoomCount; i++) {
                Passenger passenger = WAITING_ROOM_ARRAY.get(i);
                //add waiting time in the queue by dice
                passenger.setSecondsInQueue(getRandomInteger(7, 1) + getRandomInteger(7, 1) + getRandomInteger(7, 1));

                passengerQueue.enqueue(passenger);
            }
            WAITING_ROOM_ARRAY.subList(0, waitingRoomCount).clear();

        } else if (passengerQueue.isFull()) {
            System.out.println("Passenger Queue is full");
            noOfPassengers = 0;
        } else {
            System.out.println("Waiting room is empty");
        }

        return noOfPassengers;
    }

    public static ArrayList<Passenger> getWaitingRoomArray() {
        return WAITING_ROOM_ARRAY;
    }

    public static void setWaitingRoomArray(ArrayList<Passenger> waitingRoomArray) {
        WAITING_ROOM_ARRAY = waitingRoomArray;
    }

    public static PassengerQueue getPassengerQueue() {
        return passengerQueue;
    }

    public static void setPassengerQueue(PassengerQueue passengerQueue) {
        TrainStation.passengerQueue = passengerQueue;
    }


    //showing the menu
    private void showMenu() {
        System.out.println("");
        System.out.println("A: Add a User to the queue");
        System.out.println("V: View all the seats");
        System.out.println("D: Delete customer from seat");
        System.out.println("S: Store program data into file");
        System.out.println("L: Load program data from the file");
        System.out.println("R: Run the simulation and produce report");
        System.out.println("Q: Exit");

    }

    //return Updated GUI 
    private BorderPane getUpdate(String mode) {
        BorderPane mainPanel = new BorderPane();

        if (mode.equals("A")) {

            addUserUI.setQueueWaitingRoomCount(passengerQueue.size(), WAITING_ROOM_ARRAY.size());

            //initialize queue and waiting room grids
            waitingRoomDisplay = addUserUI.getWaitingRoomDisplay();
            passengerQueueDisplay = addUserUI.getPassengerQueueDisplay();


            GridPane waitingRoomDisplayGrid = waitingRoomDisplay.getDisplay();
            ScrollPane waitingRoomDisplayScrollPane1 = new ScrollPane(waitingRoomDisplayGrid);
            waitingRoomDisplayScrollPane1.setPrefHeight(400);
            waitingRoomDisplayScrollPane1.setPadding(new Insets(10, 0, 0, 300));

            //get waiting room grid
            mainPanel.setCenter(waitingRoomDisplayScrollPane1);


            //get queue grid
            GridPane passengerQueueDisplayGrid = passengerQueueDisplay.getDisplay();

            ScrollPane passengerQueueDisplayScrollPane = new ScrollPane(passengerQueueDisplayGrid);
            passengerQueueDisplayScrollPane.setPrefHeight(450);
            passengerQueueDisplayScrollPane.setPadding(new Insets(10, 0, 0, 300));

            mainPanel.setBottom(passengerQueueDisplayScrollPane);

        } else if (mode.equals("V")) {
            passengerQueueUI.setQueueCount(passengerQueue.size());

            GridPane passengerQueueUIDisplayGrid = passengerQueueUIDisplay.getDisplay();

            ScrollPane passengerQueueUIDisplayScrollPane = new ScrollPane(passengerQueueUIDisplayGrid);
            passengerQueueUIDisplayScrollPane.setPrefHeight(400);
            passengerQueueUIDisplayScrollPane.setPadding(new Insets(50, 0, 0, 538));

            mainPanel.setCenter(passengerQueueUIDisplayScrollPane);


        } else if (mode.equals("R")) {
            if (!passengerQueue.isEmpty()) {
                reportUI.setQueueCount(passengerQueue.size());
                Group passengerDetailsCards = passengerQueueGridDisplay.getDisplay();

                mainPanel.setCenter(passengerDetailsCards);

                // top part of the window
                HBox fields = new HBox(10);
                Label ReportTitleLabel = new Label("-REPORT-");
                ReportTitleLabel.setStyle("-fx-border-color: red;-fx-border-width:5px;" +
                        " -fx-border-radius: 10 10 10 10; -fx-padding: 10 80 10 80;");
                fields.getChildren().add(ReportTitleLabel);
                fields.setStyle("-fx-font-size:20px;-fx-font-weight: bold;");
                fields.setPadding(new Insets(0, 0, 2, 600));
                mainPanel.setTop(fields);

                Label queueLengthLabel = new Label("Length of the queue:");
                Label minWaitingtimeLabel = new Label("Minimum Waiting:");
                Label maxWaitingtimeLabel = new Label("Maximum Waiting:");
                Label avgWaitingtimeLabel = new Label("Average Waiting:");

                HashMap hmapStatistics = getStatistics();

                Label queueLengthValue = new Label(hmapStatistics.get("lengthOfQueue").toString());
                Label minWaitingtimeValue = new Label(hmapStatistics.get("minWaiting").toString() + "s");
                Label maxWaitingtimeValue = new Label(hmapStatistics.get("maxWaiting").toString() + "s");
                Label avgWaitingtimeValue = new Label(hmapStatistics.get("avgWaiting").toString() + "s");

                setLabelStyle(queueLengthLabel, minWaitingtimeLabel, maxWaitingtimeLabel, avgWaitingtimeLabel,
                        queueLengthValue, minWaitingtimeValue, maxWaitingtimeValue, avgWaitingtimeLabel, avgWaitingtimeValue);

                GridPane summaryGrid = new GridPane();

                //bottom part of the window
                Label titleSummary = new Label("-Summary-");
                titleSummary.setStyle("-fx-border-color: red;-fx-border-width:5px;-fx-border-radius: 10 10 10 10;" +
                        " -fx-padding: 5 50 5 50;-fx-font-size:16px;-fx-font-weight: bold;");
                summaryGrid.addRow(0, titleSummary);

                summaryGrid.addRow(2, queueLengthLabel, queueLengthValue);
                summaryGrid.addRow(3, minWaitingtimeLabel, minWaitingtimeValue);
                summaryGrid.addRow(4, maxWaitingtimeLabel, maxWaitingtimeValue);
                summaryGrid.addRow(5, avgWaitingtimeLabel, avgWaitingtimeValue);


                ScrollPane summaryScrollPane = new ScrollPane(summaryGrid);
                summaryScrollPane.setPrefHeight(500);
                summaryScrollPane.setPadding(new Insets(0, 0, 0, 10));

                summaryScrollPane.setStyle("-fx-background-color:transparent;");
                summaryScrollPane.setPadding(new Insets(5, 0, 0, 30));
                mainPanel.setBottom(summaryScrollPane);

            } else {
                // top part of the window
                HBox fields = new HBox(10);
                Label ReportTitleLabel = new Label("Queue is empty!");

                fields.getChildren().add(ReportTitleLabel);
                fields.setStyle("-fx-font-size:30px;-fx-font-weight: bold;");
                fields.setPadding(new Insets(0, 0, 5, 830));
                mainPanel.setTop(fields);
                System.out.println("Queue is empty");
            }


        }

        return mainPanel;
    }

    private void setLabelStyle(Node... nodes) {
        for (Node node : nodes) {
            node.setStyle("-fx-font-size: 16");
        }
    }


    public void runThis(Stage main) {

        String option = "";
        this.showMenu();
        Scanner consoleScanner = new Scanner(System.in);
        System.out.print("Select your option:");
        option = consoleScanner.nextLine();
        option = option.toUpperCase();
        BorderPane borderPane;

        if (option.equals("A")) {

            borderPane = getUpdate("A");
            Scene scene = new Scene(borderPane, 3840, 2160);

            main.setScene(scene);
            main = getCancellableEvent(main);
            main.setTitle("Add passengers to queue");
            main.show();
        } else if (option.equals("V")) {
            this.showQueue();
            borderPane = getUpdate("V");
            Scene scene = new Scene(borderPane, 3840, 2160);

            main.setScene(scene);
            main = getCancellableEvent(main);
            main.setTitle("View passengers in queue");
            main.show();

        } else if (option.equals("D")) {

            Scanner scanner = new Scanner(System.in);

            if (!passengerQueue.isEmpty()) {
                this.showQueue();
                System.out.print("Enter customer id: ");
                int id = 0;
                if (scanner.hasNextInt()) {
                    id = scanner.nextInt();
                } else {
                    System.out.println("Invalid number !!");
                    runThis(main);
                }

                this.deletePassenger(id);
                runThis(main);
            } else {
                System.out.println("Queue is empty !!");
                runThis(main);
            }


        } else if (option.equals("R")) {

            borderPane = getUpdate("R");
            Scene scene = new Scene(borderPane, 3840, 2160);
            main.setScene(scene);
            main = getCancellableEvent(main);
//            main.setMaximized(true);
            main.setTitle("Report");
            main.show();

        } else if (option.equals("S")) {
            this.saveQueueData();
            runThis(main);
        } else if (option.equals("L")) {
            this.loadQueueData();
            runThis(main);
        } else if (option.equals("Q")) {
            System.out.println("Exiting from the program");
            System.exit(0);
        } else {
            System.out.println("Please Enter Valid Input");
        }

    }


    //initial GUI
    @Override
    public void start(Stage stage) {
        Parameters parameters = getParameters();
        String action = parameters.getRaw().get(0);
        Scene scene = null;
        BorderPane mainPanel = new BorderPane();

        addUserUI = new AddUserUI(ts);
        //initialize queue and waiting room grids
        waitingRoomDisplay = addUserUI.getWaitingRoomDisplay();
        passengerQueueDisplay = addUserUI.getPassengerQueueDisplay();

        passengerQueueUI = new PassengerQueueUI(ts);
        passengerQueueUIDisplay = passengerQueueUI.getPassengerQueueDisplay();

        reportUI = new ReportUI(ts);
        passengerQueueGridDisplay = reportUI.getPassengerQueueGridDisplay();


        if (action.equals("A")) {

            addUserUI.setQueueWaitingRoomCount(passengerQueue.size(), WAITING_ROOM_ARRAY.size());

            GridPane waitingRoomDisplayGrid = waitingRoomDisplay.getDisplay();
            ScrollPane waitingRoomDisplayScrollPane = new ScrollPane(waitingRoomDisplayGrid);
            waitingRoomDisplayScrollPane.setPrefHeight(400);
            waitingRoomDisplayScrollPane.setPadding(new Insets(10, 0, 0, 300));
            //get waiting room grid
            mainPanel.setCenter(waitingRoomDisplayScrollPane);

            //get queue grid
            GridPane passengerQueueDisplayGrid = passengerQueueDisplay.getDisplay();

            ScrollPane passengerQueueDisplayScrollPane = new ScrollPane(passengerQueueDisplayGrid);
            passengerQueueDisplayScrollPane.setPrefHeight(450);
            passengerQueueDisplayScrollPane.setPadding(new Insets(10, 0, 0, 300));

            mainPanel.setBottom(passengerQueueDisplayScrollPane);
            scene = new Scene(mainPanel, 3840, 2160);
            stage.setTitle("Add Passengers");
        } else if (action.equals("V")) {
            //get queue grid
            passengerQueueUI.setQueueCount(passengerQueue.size());
            GridPane passengerQueueUIDisplayGrid = passengerQueueUIDisplay.getDisplay();

            ScrollPane passengerQueueUIDisplayScrollPane = new ScrollPane(passengerQueueUIDisplayGrid);
            passengerQueueUIDisplayScrollPane.setPrefHeight(500);
            passengerQueueUIDisplayScrollPane.setPadding(new Insets(50, 0, 0, 538));

            mainPanel.setCenter(passengerQueueUIDisplayScrollPane);
            scene = new Scene(mainPanel, 3840, 2160);
            stage.setTitle("View Queue");
        }


        stage = getCancellableEvent(stage);

        stage.setScene(scene);
//        stage.setMaximized(true);
        stage.show();

    }

    private Stage getCancellableEvent(Stage stage) {

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) { //handling the close button event

                boolean actionNeeded = false;
                if (actionNeeded) {
                    System.exit(0);
                } else {
                    event.consume();
                    stage.hide();
                    runThis(stage);
                }

            }
        });
        return stage;
    }


    @Override
    public void stop() {
        System.out.println("Exiting from the program ...");
    }

}
