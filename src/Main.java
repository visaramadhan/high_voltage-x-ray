import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    SerialController serialController = new SerialController();

    Label voltageLabel = new Label("Voltage: -- V");
    Label pwmLabel = new Label("PWM: --");
    Label exposureStatus = new Label("Status: ---");

    TextField pwmInput = new TextField("100");
    TextField timeInput = new TextField("1000");
    TextArea logArea = new TextArea();

    ComboBox<String> portSelector = new ComboBox<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("X-Ray Control Interface");

        Button startBtn = new Button("START");
        Button stopBtn = new Button("STOP");
        Button statusBtn = new Button("STATUS");
        Button connectBtn = new Button("CONNECT");

        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 10;");
        HBox topInfo = new HBox(10, voltageLabel, pwmLabel, exposureStatus);
        HBox inputs = new HBox(10, new Label("PWM:"), pwmInput, new Label("Time(ms):"), timeInput);
        HBox buttons = new HBox(10, startBtn, stopBtn, statusBtn);
        HBox serialBox = new HBox(10, new Label("Port:"), portSelector, connectBtn);

        logArea.setPrefRowCount(10);
        logArea.setEditable(false);

        root.getChildren().addAll(topInfo, inputs, buttons, serialBox, new Label("Log:"), logArea);

        portSelector.getItems().addAll(serialController.listAvailablePorts());

        connectBtn.setOnAction(e -> {
            String port = portSelector.getValue();
            if (serialController.connect(port)) {
                log("Connected to " + port);
            } else {
                log("Failed to connect.");
            }
        });

        startBtn.setOnAction(e -> {
            String pwm = pwmInput.getText();
            String time = timeInput.getText();
            serialController.send("SETPWM " + pwm);
            serialController.send("SETTIME " + time);
            serialController.send("START");
            log("Started exposure with PWM " + pwm + ", Time " + time + "ms");
        });

        stopBtn.setOnAction(e -> {
            serialController.send("STOP");
            log("Stopped exposure");
        });

        statusBtn.setOnAction(e -> {
            serialController.send("STATUS");
            String response = serialController.read();
            log("Status: " + response);
        });

        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    private void log(String msg) {
        logArea.appendText("> " + msg + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}