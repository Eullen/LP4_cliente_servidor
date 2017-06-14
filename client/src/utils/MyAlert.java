package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MyAlert {
	
	public static void makeErrorAlert(String erro){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ERRO");
		alert.setHeaderText(null);
		alert.setContentText(erro);
		alert.showAndWait();
	} 
}
