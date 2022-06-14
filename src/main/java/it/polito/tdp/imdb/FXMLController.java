/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="btnCreaGrafo"
	private Button btnCreaGrafo; // Value injected by FXMLLoader

	@FXML // fx:id="btnSimili"
	private Button btnSimili; // Value injected by FXMLLoader

	@FXML // fx:id="btnSimulazione"
	private Button btnSimulazione; // Value injected by FXMLLoader

	@FXML // fx:id="boxGenere"
	private ComboBox<String> boxGenere; // Value injected by FXMLLoader

	@FXML // fx:id="boxAttore"
	private ComboBox<Actor> boxAttore; // Value injected by FXMLLoader

	@FXML // fx:id="txtGiorni"
	private TextField txtGiorni; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML
	void doAttoriSimili(ActionEvent event) {
		txtResult.clear();
		Actor attore = boxAttore.getValue();

		if (attore == null) {
			txtResult.appendText("Seleziona un attore!");
			return;
		}

		txtResult.appendText("ATTORI SIMILI A: " + attore.getLastName() + ", " + attore.getFirstName());

		txtResult.appendText("\n" + model.linkedActors(attore).size());
		for (Actor a : model.linkedActors(attore)) {
			txtResult.appendText("\n" + a.getLastName() + ", " + a.getFirstName() + " (" + a.getId() + ")");
		}

	}

	@FXML
	void doCreaGrafo(ActionEvent event) {
		txtResult.clear();
		String genere = boxGenere.getValue();
		if (genere == null) {
			txtResult.appendText("Seleziona un genere!");
			return;
		}

		model.creaGrafo(genere);

		if (!model.grafoCreato()) {
			txtResult.appendText("Nessun grafo creato - errore.");
			return;
		}

		txtResult.appendText("Grafo creato!");
		txtResult.appendText("\n#V: " + model.getVertici().size());
		txtResult.appendText("\n#A: " + model.getArchi().size());

		List<Actor> attoriBox = new ArrayList<Actor>(model.getVertici());
		Collections.sort(attoriBox, new Comparator<Actor>() {

			@Override
			public int compare(Actor a, Actor a1) {
				return a.getLastName().compareTo(a1.getLastName());
			}
		});
		boxAttore.getItems().addAll(attoriBox);
	}

	@FXML
	void doSimulazione(ActionEvent event) {
		txtResult.clear();
		int n = 0;
		try {
			n = Integer.parseInt(txtGiorni.getText());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		model.simulate(n);
		
		txtResult.appendText("Simulazione effettuata");
		txtResult.appendText("\nPause totali: " + model.numPause() + "\n\n");
		for(Actor a : model.getIntervistati()) {
			txtResult.appendText("\nAttore intervistato: " + a.getLastName() + " " + a.getFirstName());
		}
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
		assert btnSimili != null : "fx:id=\"btnSimili\" was not injected: check your FXML file 'Scene.fxml'.";
		assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
		assert boxGenere != null : "fx:id=\"boxGenere\" was not injected: check your FXML file 'Scene.fxml'.";
		assert boxAttore != null : "fx:id=\"boxAttore\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtGiorni != null : "fx:id=\"txtGiorni\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

	}

	public void setModel(Model model) {
		this.model = model;
		boxGenere.getItems().addAll(model.getGeneri());
	}
}
