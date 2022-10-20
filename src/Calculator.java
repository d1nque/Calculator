import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.geometry.Insets;

import javafx.event.*;

public class Calculator extends Application {
    private Label label_equation = new Label("");

    //Flag about availability operator in equation
    private boolean isAlreadyOperator = false;

    //Flag minus as operator
    private boolean isMinusOperator = false;

    //Flag minus before number, not operator
    private boolean isMinusNumber = false;

    //Flag after getting result
    private boolean isTotal = false;

    //Flag point in number
    private boolean isFraction = false;

    //General style configuration for label and buttons
    private final Font components_style = Font.font("PFAgoraSlabPro Bold", FontWeight.BLACK, 15);

    /*
        Stage creatnig...
     */
    @Override
    public void start(Stage stage) throws Exception {
        try {
            stage.setTitle("Calculator 1.0");
            Scene scene = new Scene(getButtons());
            label_configuration();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setWidth(341);
            stage.setHeight(300);
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /*
       "Visible" label settings
     */
    private void label_configuration() {
        label_equation.setMinWidth(300);
        label_equation.setMinHeight(50);
        label_equation.setAlignment(Pos.BASELINE_RIGHT);
        label_equation.setFont(components_style);
        label_equation.setTextFill(Color.color(1, 1, 1));
    }

    /*
        Getting and styling our button "grid",
        button layout like in google online calculator
     */
    private FlowPane getButtons() {

        Button[] number_buttons = getButtonsArray();

        for (Button number_button : number_buttons) {
            number_button.setFont(components_style);
            number_button.setMinSize(75, 45);
            number_button.setOnAction(getNumberButtonEvent());
        }

        FlowPane buttonGrid = new FlowPane();

        buttonGrid.getChildren().add(label_equation);
        buttonGrid.getChildren().addAll(number_buttons);
        buttonGrid.setPadding(new Insets(5, 5, 5, 5));
        buttonGrid.setHgap(5);
        buttonGrid.setVgap(5);
        buttonGrid.setStyle("-fx-background-color: #414141;");

        return buttonGrid;
    }

    private void addingToLabel(String number) {
        label_equation.setText(label_equation.getText() + number);
    }


    private EventHandler<ActionEvent> getNumberButtonEvent() {

        return event -> {
            Button bt = (Button) event.getTarget();

            if (isTotal && !bt.getText().equals("/") && !bt.getText().equals("*") && !bt.getText().equals("+") && !bt.getText().equals("-")) {
                label_equation.setText("");
                isTotal = false;
            }

            switch (bt.getText()) {
                case "1" -> addingToLabel("1");
                case "2" -> addingToLabel("2");
                case "3" -> addingToLabel("3");
                case "4" -> addingToLabel("4");
                case "5" -> addingToLabel("5");
                case "6" -> addingToLabel("6");
                case "7" -> addingToLabel("7");
                case "8" -> addingToLabel("8");
                case "9" -> addingToLabel("9");
                case "0" -> addingToLabel("0");
                case "*" -> adding_operator("*");
                case "-" -> minus_operator();
                case "/" -> adding_operator("/");
                case "+" -> adding_operator("+");
                case "=" -> total();
                case "." -> adding_point();
            }

        };
    }

    /*
     */
    private Button[] getButtonsArray() {
        return new Button[]{
                new Button("7"),
                new Button("8"),
                new Button("9"),
                new Button("/"),
                new Button("4"),
                new Button("5"),
                new Button("6"),
                new Button("*"),
                new Button("1"),
                new Button("2"),
                new Button("3"),
                new Button("-"),
                new Button("0"),
                new Button("."),
                new Button("="),
                new Button("+")};
    }

    /*
    Making easier work with point for the user
     */
    private void adding_point() {

        //Point in clear equation
        if (label_equation.getText().equals("")) {
            label_equation.setText("0.");
        }

        //Point after minus number, nor operator
        else if (label_equation.getText().equals("-")) {
            label_equation.setText("-0.");
        }

        //Just point in number
        else if (!isFraction && !isAlreadyOperator) {
            addingToLabel(".");
            isFraction = true;
        }

        //Point after operator in equation
        else if (isAlreadyOperator) {
            String[] splitedEquation = label_equation.getText().split(" ");

            //Point after any operator
            if (splitedEquation.length == 2) {
                addingToLabel("0.");
            }
            //If operator - minus
            else if (splitedEquation[2].equals("-")) {
                addingToLabel("-0.");
            }

            else {
                addingToLabel(".");
            }
        }
    }


    //In general resetting flag after adding operators except for the minus
    private void adding_operator(String operator) {
        if (!isAlreadyOperator && !label_equation.getText().equals("")) {
            label_equation.setText(label_equation.getText() + " " + operator + " ");
            isTotal = false;
            isFraction = false;
            isAlreadyOperator = true;
            isMinusOperator = false;
        }
    }

    /*
    Just calculator :D
    and resetting some flags
     */
    private void total() {
        if(!isTotal) {
            String[] equation = label_equation.getText().split(" ");
            double firstNumber = Double.parseDouble(equation[0]);
            double secondNumber = Double.parseDouble(equation[2]);
            isTotal = true;
            isFraction = false;
            isAlreadyOperator = false;
            isMinusOperator = false;
            switch (equation[1]) {
                case "*" -> label_equation.setText(Double.toString(firstNumber * secondNumber));
                case "/" -> label_equation.setText(Double.toString(firstNumber / secondNumber));
                case "+" -> label_equation.setText(Double.toString(firstNumber + secondNumber));
                case "-" -> label_equation.setText(Double.toString(firstNumber - secondNumber));
            }
        }
    }

    /*
    Special settings for minus operator to avoid problems with minus in number
     */
    private void minus_operator() {
        //Minus in clear equation and resetting old flags
        if (label_equation.getText().equals("")) {
            label_equation.setText("-" + label_equation.getText());
            isFraction = false;
            isMinusNumber = true;

        }
        //after operation resetting flags and adding minus for old result number
        else if (isTotal) {
            label_equation.setText(label_equation.getText() + " " + "-" + " ");
            isTotal = false;
            isFraction = false;
            isAlreadyOperator = true;
            isMinusOperator = true;

        }
        //Adding minus as operator
        else if (!isAlreadyOperator && !isMinusOperator) {
            label_equation.setText(label_equation.getText() + " " + "-" + " ");
            isAlreadyOperator = true;
            isMinusOperator = true;
        }
        //Negative number
        else if (isMinusOperator && !isMinusNumber) {
            addingToLabel("-");
            isMinusNumber = true;
        }
    }

}


