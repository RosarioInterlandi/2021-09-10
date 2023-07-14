/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.BusinessAdiacente;
import it.polito.tdp.yelp.model.Model;
import it.polito.tdp.yelp.model.risultatoRicorsione;
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

    @FXML // fx:id="btnDistante"
    private Button btnDistante; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcolaPercorso"
    private Button btnCalcolaPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtX2"
    private TextField txtX2; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB1"
    private ComboBox<Business> cmbB1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB2"
    private ComboBox<Business> cmbB2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String città = this.cmbCitta.getValue();
    	if (città == null) {
    		txtResult.setText("Inserisci una città");
    		return;
    	}
    	this.model.BuildGraph(città);
    	if (this.model.getVertici().size()!=0) {
    		txtResult.setText("Grafo creato correttamente "+ this.model.getVertici().size()+"---"+ this.model.getArchi().size());
    	}
    	this.cmbB1.getItems().clear();
    	this.cmbB2.getItems().clear();
    	this.cmbB1.getItems().addAll(this.model.getVertici());
    	this.cmbB2.getItems().addAll(this.model.getVertici());
    
    }

    @FXML
    void doCalcolaLocaleDistante(ActionEvent event) {
    	Business source = this.cmbB1.getValue();
    	if (source == null) {
    		txtResult.setText("Scegli un business");
    		return;
    	}
    	BusinessAdiacente result =  this.model.getAdiacente(source);
    	if (result != null) {
    		txtResult.setText("LOCALE PIU' LONTANO\n"+ result.getBusiness()+"="+result.getDistanza() );
    	}
    	
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	Business target =this.cmbB2.getValue();
    	if (target == null) {
    		txtResult.setText("Inserisci un business target");
    		return;
    	}
    	Double stelleMinime = 0.0;
    	try {
    		stelleMinime = Double.parseDouble(txtX2.getText()); 
    	}catch (NumberFormatException e ) {
    		//Metto un double perchè la media potrebbe venire un double 
    		txtResult.setText("Inserisci il numero di stelle minimo");
    		return;
    	}
    	risultatoRicorsione result = this.model.getPath(target, stelleMinime);
    	if (result.getBestPath().size()==0) {
    		txtResult.setText("Percorso non trovato");
    	}
    	txtResult.setText("Percorso trovato, numeri km totati "+result.getDistanza()+":\n");
    	for (Business b : result.getBestPath()) {
    		txtResult.appendText(b+"\n");
    		return;
    	}
    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDistante != null : "fx:id=\"btnDistante\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX2 != null : "fx:id=\"txtX2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB1 != null : "fx:id=\"cmbB1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB2 != null : "fx:id=\"cmbB2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbCitta.getItems().addAll(this.model.getCittà());
    }
}
