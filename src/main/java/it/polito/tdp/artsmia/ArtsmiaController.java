package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Coppie connesse:\n");
    	for (Adiacenza a : this.model.getArchi()) {
    		txtResult.appendText(a.toString()+"\n");
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	try {
    		int id = Integer.parseInt(txtArtista.getText());
    		List<Artist> risultato = this.model.getPercorso(id);
    		if (risultato==null) {
    			txtResult.setText("Errore: verificare di aver inserito un id corretto");
    		} else {
    			txtResult.appendText("Percorso migliore:\n");
    			for (Artist a : risultato) {
    				txtResult.appendText(a.toString()+"\n");
    			}
    			txtResult.appendText("Esposizioni che massimizzano il percorso: "+this.model.getEsposizioni());
    		}
    	} catch (NumberFormatException e) {
    		txtResult.setText("Errore: inserire un numero valido nel campo id artista");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	if (this.boxRuolo.getValue()==null) {
    		txtResult.setText("Selezionare un ruolo!");
    		return;
    	}
    	this.model.creaGrafo(this.boxRuolo.getValue());
    	txtResult.appendText("Grafo creato!\n");
    	txtResult.appendText("# Vertici: "+this.model.getNumVertici()+"\n");
    	txtResult.appendText("# Archi: "+this.model.getNumArchi()+"\n");
    	btnArtistiConnessi.setDisable(false);
    	btnCalcolaPercorso.setDisable(false);

    }

    public void setModel(Model model) {
    	this.model = model;
    	this.boxRuolo.getItems().addAll(this.model.getRuoli());
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
