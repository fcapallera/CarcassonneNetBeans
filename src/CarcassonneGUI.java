/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileNotFoundException;
import javafx.scene.image.Image ;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;


/**
 *
 * @author usuario
 */
public class CarcassonneGUI extends Application {
    private int numAux;
    private int col;
    private int row;   
    private BorderPane root;
    private BorderPane colocarSeg;
    private GridPane taul;
    private GridPane dreta;
    private Button rota;
    private Button passa;
    private Button afegirSeguidor;
    private Button top;
    private Button bot;
    private Button left;
    private Button right;
    private Button center;
    private ImageView pila;
    private int rotacioPila; 
    private Joc _joc;
    private Text numPila;
    
    @Override
    public void start(Stage primaryStage) {
        _joc = new Joc("2");
        row = 3;
        col = 3;
        numAux = 690;
        root = new BorderPane();
        Image image2 = new Image(CarcassonneGUI.class.getResourceAsStream("images/wallpaper.jpg"));

        BackgroundSize bSize = new BackgroundSize(root.getWidth(), root.getHeight(), false, false, true, false);


        root.setBackground(new Background(new BackgroundImage(image2,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bSize)));
        taul = getCenterGridPane();
        dreta = getRightGridPane();
        root.setAlignment(taul,Pos.CENTER);
        BorderPane.setAlignment(dreta,Pos.CENTER);
        BorderPane.setMargin(taul, new Insets(30, 10, 10, 50));
        BorderPane.setMargin(dreta, new Insets(10, 50, 10, 10));
        root.setRight(dreta);
        taul.setAlignment(Pos.CENTER);
        root.setCenter(taul);
        root.setLeft(getLeftGridPane());
        rotacioPila = 0;
        assignarEventListeners();

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("CARCASSONNE");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();       
    }
    
    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
    
    public GridPane getLeftGridPane(){
        GridPane grid = new GridPane();
        GridPane grid2 = new GridPane();
        grid.setHgap(75);
        grid.setVgap(20);
        
        int nJug = 4;
        
        //EFECTES DE TEXT
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
        ///
        
        
        int numAux = 200;
        Text category = new Text("JUGADORS");
        category.setEffect(ds);
        category.setCache(true);
        category.setFill(Color.BLUE);
        category.setFont(Font.font(null, FontWeight.BOLD, 16));
        grid.add(category, 0, 0);
        category = new Text("PUNTUACIO");
        category.setEffect(ds);
        category.setCache(true);
        category.setFill(Color.BLUE);
        category.setFont(Font.font(null, FontWeight.BOLD, 20));
        grid.add(category, 1, 0);
        for(int i = 0; i < 2; i++){
            for(int j = 1; j <= nJug; j++){
                if(i == 0){
                    category = new Text("Jugador"+ j);
                    category.setEffect(ds);
                    category.setCache(true);
                    category.setFill(Color.BLUE);
                    category.setFont(Font.font(null, FontWeight.BOLD, 18));
                }
                else{
                    category = new Text("0");
                    category.setEffect(ds);
                    category.setCache(true);
                    category.setFill(Color.BLUE);
                    category.setFont(Font.font(null, FontWeight.BOLD, 18));
                }
                grid.add(category, i, j); 
            }
        }
        grid.setAlignment(Pos.TOP_RIGHT);
        return grid;
    }
    
    
    
    public GridPane getRightGridPane(){
        //CREEM UN NOU GRID PANE DE 1x2
        GridPane grid = new GridPane();
        grid.setHgap(0);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 0, 0, 0));
        
        //CARREGUEM IMATGES DE LA PEÇA SUPERIOR DE LA PILA I DEL BOTÓ "ROTA"
        pila = new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("tiles/" + _joc.peekPila().get_codi() + ".png")));
        rota = new Button();
        rota.setGraphic(new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("images/button.png"))));
        passa = new Button();
        passa.setGraphic(new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("images/next.png"))));
        afegirSeguidor = new Button();
        afegirSeguidor.setGraphic(new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("images/afegirSeguidor.png"))));
        top = new Button();
        top.setGraphic(new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("images/top.png"))));
        bot = new Button();
        bot.setGraphic(new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("images/bot.png"))));
        left = new Button();
        left.setGraphic(new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("images/left.png"))));
        right = new Button();
        right.setGraphic(new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("images/right.png"))));
        center = new Button();
        center.setGraphic(new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("images/center.png"))));
        numPila = new Text();
        numPila.setCache(true);
        numPila.setFill(Color.WHITE);
        numPila.setFont(Font.font(null, FontWeight.BOLD, 26));
        numPila.setText(String.valueOf(_joc.getPila().size()));
        passa.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");
        rota.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");
        afegirSeguidor.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");
        top.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");
        bot.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");
        center.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");
        left.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");
        right.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");
     
        
        //AFEGIM LA IMATGE DE LA PILA I EL BOTÓ AL GRID PANE
        /*grid.add(numPila,0,0);
        grid.add(pila, 0, 1);
        grid.add(rota,0,2);
        grid.add(passa,0,3);
        grid.add(afegirSeguidor,0,4);
        grid.add(colocarSeg,0,5);*/
        
        grid.add(numPila,1,0);
        grid.add(pila, 1, 1);
        grid.add(rota,1,2);
        grid.add(passa,1,3);
        grid.add(afegirSeguidor,1,4);
        grid.add(top,1,5);
        grid.add(center,1,6);
        grid.add(left,0,6);
        grid.add(right,2,6);
        grid.add(bot,1,7);
        grid.setAlignment(Pos.CENTER);
        pila.setFitHeight(numAux/5);
        pila.setFitWidth(numAux/5);
        top.setVisible(false);
        bot.setVisible(false);
        center.setVisible(false);
        left.setVisible(false);
        right.setVisible(false);
        
        return grid;
    }
    
    public GridPane getCenterGridPane(){
        //CREEM EL GRID PANE
        GridPane grid = gridPaneBuit(row,col);
        
        //ITEREM EL MAP DE PECES AL TAULER PER OBTENIR LA SEVA IMATGE
        Iterator it = _joc.getTaulaJoc().getTauler().entrySet().iterator();
        Map.Entry pair = (Map.Entry)it.next();
        Peça aux =(Peça) pair.getValue();
        ImageView imageChart = new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("tiles/" + aux.get_codi() + ".png")));
        
        //AJUSTEM LA IMATGE I LA INSERIM AL SEU LLOC CORRESPONENT
        ajustarImageView(imageChart);
        int aux_x = getXTauler(aux.get_x());
        int aux_y = getYTauler(aux.get_y());
        grid.add(imageChart,aux_x,aux_y);
        
        
        return grid;
    }
    

    private boolean esLimit(int x, int y){
        boolean res = false;
        if(x == 0 || y == 0 || x == col-1 || y == row-1){
            res = true;
        }
        return res;
    }
    
    private GridPane gridPaneBuit(int x, int y){
        //CREEM UN GRID PANE DE (x,y) I L'OMPLIM, A TOTES LES POSICIONS, AMB LA IMATGE QUE SIMBOLITZA "CASELLA BUIDA"
        GridPane res = new GridPane();
        for(int i = 0; i < col; i++){
            for(int j = 0; j < row; j++){
                ImageView imageChart;
                imageChart = new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("tiles/RES.png")));
                if(row >= col){
                    imageChart.setFitHeight(numAux/row);
                    imageChart.setFitWidth(numAux/row);
                }
                else{
                    imageChart.setFitHeight(numAux/col);
                    imageChart.setFitWidth(numAux/col);
                }
                res.add(imageChart, i, j); 
            }
        }
        return res;
    }
    
    private void resizeTauler(int x, int y){
        //MIREM QUINA VARIABLE HEM D'INCREMENTAR
        if(x == 0 && y == 0 || x == 0 && y == row-1 || y == 0 && x == col-1 || x == col-1 && y == row-1){
            row++;
            col++;
        }
        else if(x == 0 || x == col-1){
            col ++;
        }
        else if(y == 0 || y == row-1){
            row ++;
        }
        
        //CREEM UN GRIDPANE BUIT I L'ASSIGNEM COM A GRID PANE ACTUAL PER VISUALITZAR EL TAULER
        GridPane nouGrid = gridPaneBuit(x,y);
        nouGrid.setAlignment(Pos.CENTER);
        taul = nouGrid;
        
        //ITEREM EL MAP DE PECES COL·LOCADES AL TAULER.
        //OBTENIM LA X I LA Y HASH I LES TRANSFORMEM A LES X I Y DEL TAULER
        //AFEGIM LA IMATGE A LA SEVA POSICIO CORRESPONENT DEL GRID PANE
        Iterator it = _joc.getTaulaJoc().getTauler().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Peça aux =(Peça) pair.getValue();
            ImageView aux2 = new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("tiles/" + aux.get_codi() + ".png")));
            aux2.setRotate(aux.getIndexRotacio()*90);
            int aux_x = getXTauler(aux.get_x());
            int aux_y = getYTauler(aux.get_y());
            ajustarImageView(aux2);
            nouGrid.add(aux2,aux_x,aux_y);
        }
        //ACTUALITZEM EL CENTRE DEL BORDER PANE AMB EL NOSTRE TAULER NOU I CRIDEM ELS EVENT LISTENERS PERQUÈ SE LI APLIQUIN
        root.setCenter(taul);
        assignarEventListeners();
    }
    
    private boolean adjacenciesValides(int x, int y){ //X i Y del gridPane
        boolean compatible = false;
	boolean valid = true;
	if(x != col-1){		
            if(_joc.getTaulaJoc().getTauler().containsKey((x+1)*100+y)){
                valid = false;
                if(_joc.peekPila().getRegioAdjacent(1)/*EST*/ == _joc.getTaulaJoc().getTauler().get((x+1)*100+y).getRegioAdjacent(3)/*OEST*/){
                    valid = true;
                }

            }
	}
	if(x != 0){
            if(_joc.getTaulaJoc().getTauler().containsKey((x-1)*100+y)){
                valid = false;
                if(_joc.peekPila().getRegioAdjacent(3) == _joc.getTaulaJoc().getTauler().get((x+1)*100+y).getRegioAdjacent(1)){
                    valid = true;
                }
            }
	}
	if(y != row-1){
            if(_joc.getTaulaJoc().getTauler().containsKey(x*100+y+1)){
                valid = false;
                if(_joc.peekPila().getRegioAdjacent(2)/*SUD*/ == _joc.getTaulaJoc().getTauler().get((x+1)*100+y).getRegioAdjacent(0)/*NORD*/){
                    valid = true;
                }
            }
	}
	if(y != 0){
            if(_joc.getTaulaJoc().getTauler().containsKey(x*100+y-1)){
                valid = false;
                if(_joc.peekPila().getRegioAdjacent(0) == _joc.getTaulaJoc().getTauler().get((x+1)*100+y).getRegioAdjacent(2)){
                    valid = true;
                }
            }
	}
	compatible = valid;
        return compatible;
    }
    
    private int getXTauler(int xHash){
        int res = xHash - _joc.getTaulaJoc().getMinX() + 1;
        return res;
    }
    
    private int getYTauler(int yHash){
        int res = (yHash*(-1)) + _joc.getTaulaJoc().getMaxY() + 1;
        return res;
    }
    
    private int getXHash(int x){
        int res = x + _joc.getTaulaJoc().getMinX() - 1;
        return res;                      
    }
    
    private int getYHash(int y){
        int res = (y - _joc.getTaulaJoc().getMaxY() - 1)/(-1);
        return res;
    }
    
    private void assignarEventListeners(){
        pila.setOnDragDone(new EventHandler<DragEvent>() {
        public void handle(DragEvent event) {
            if(event.getTransferMode() == TransferMode.MOVE){
                if(!_joc.getPila().isEmpty()){
                    Image aux = new Image(CarcassonneGUI.class.getResourceAsStream("tiles/" + _joc.peekPila().get_codi() + ".png"));
                    pila.setImage(aux);
                    pila.setRotate(_joc.peekPila().getIndexRotacio()*90);
                    numPila.setText(String.valueOf(_joc.getPila().size()));
                }
                else{
                    pila.setVisible(false);
                    numPila.setText(String.valueOf(_joc.getPila().size()));
                }
            }
            event.consume();
        }
        });
        
        afegirSeguidor.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                top.setVisible(true);
                bot.setVisible(true);
                center.setVisible(true);
                left.setVisible(true);
                right.setVisible(true);             
            }
        });
        
        rota.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                _joc.peekPila().rotarClockWise();
                pila.setRotate(_joc.peekPila().getIndexRotacio()*90);              
            }
        });
        
        passa.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                _joc.popPila();
                if(!_joc.getPila().isEmpty()){
                    Image aux = new Image(CarcassonneGUI.class.getResourceAsStream("tiles/" + _joc.peekPila().get_codi() + ".png"));
                    pila.setImage(aux);
                    pila.setRotate(0);
                    numPila.setText(String.valueOf(_joc.getPila().size()));
                }
                else{
                    pila.setVisible(false);
                    numPila.setText(String.valueOf(_joc.getPila().size()));
                }
                
            }
        });
        
        
        
        
        //Drag detected event handler is used for adding drag functionality to the boat node
        pila.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event){
                Dragboard db = pila.startDragAndDrop(TransferMode.ANY);
                ClipboardContent cbContent = new ClipboardContent();
                Image image = pila.getImage();
                cbContent.putImage(image);
                db.setContent(cbContent);
                event.consume();
            }

            private Rotate newRotate() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        //Drag over event handler is used for the receiving node to allow movement
        taul.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if(event.getGestureSource() != taul && event.getDragboard().hasImage()){
                    event.acceptTransferModes(TransferMode.MOVE);
                    Node node = event.getPickResult().getIntersectedNode();
                    if(node != taul){
                            Integer cIndex = GridPane.getColumnIndex(node);
                            Integer rIndex = GridPane.getRowIndex(node);
                            int x = cIndex == null ? 0 : cIndex;
                            int y = rIndex == null ? 0 : rIndex;
                            int xHash = getXHash(x);
                            int yHash = getYHash(y);
                            if(!_joc.getTaulaJoc().getTauler().containsKey(xHash * 100 + yHash)){ //CASELLA NO TE FOTO
                                Image aux1 = new Image(CarcassonneGUI.class.getResourceAsStream("tiles/" + _joc.peekPila().get_codi() + ".png"));
                                Image aux2 = new Image(CarcassonneGUI.class.getResourceAsStream("tiles/RES.png"));
                                ImageView iv = new ImageView(aux1);
                                iv.setRotate(_joc.peekPila().getIndexRotacio()*90);
                                ajustarImageView(iv);
                                taul.add(iv, x, y);

                                node.setOnDragExited(new EventHandler<DragEvent>() {
                                    public void handle(DragEvent event) {
                                        iv.setImage(aux2);
                                        event.consume();
                                    }
                                });
                            }
                    }
                }                  
                event.consume();
            }
        });

        //Drag entered changes the appearance of the receiving node to indicate to the player that they can place there
        taul.setOnDragEntered(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                event.consume();
            }
        });
        
        //Drag exited reverts the appearance of the receiving node when the mouse is outside of the node
        taul.setOnDragExited(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                event.consume();
            }
        });
        
        //Drag dropped draws the image to the receiving node
        taul.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                //Data dropped
                //If there is an image on the dragboard, read it and use it
                Dragboard db = event.getDragboard();
                boolean success = false;
                Node node = event.getPickResult().getIntersectedNode();
                    if(node != taul && db.hasImage()){
                        Integer cIndex = GridPane.getColumnIndex(node);
                        Integer rIndex = GridPane.getRowIndex(node);
                        int x = cIndex == null ? 0 : cIndex;
                        int y = rIndex == null ? 0 : rIndex;
                        int xHash = getXHash(x);
                        int yHash = getYHash(y);
                        if(!_joc.getTaulaJoc().getTauler().containsKey(xHash * 100 + yHash)){
                            if(_joc.getTaulaJoc().jugadaValida(_joc.peekPila(), xHash, yHash)){
                                Image aux1 = new Image(CarcassonneGUI.class.getResourceAsStream("tiles/" + _joc.peekPila().get_codi() + ".png"));
                                ImageView iv = new ImageView(aux1);
                                iv.setRotate(_joc.peekPila().getIndexRotacio()*90);
                                ajustarImageView(iv);
                                taul.add(iv, x, y);
                                success = true;
                                Peça p = _joc.popPila();
                                _joc.getTaulaJoc().afegirPeça(p, xHash, yHash);
                                if(esLimit(x,y)){
                                    resizeTauler(x,y);
                                }
                            }
                            else{
                                Image aux1 = new Image(CarcassonneGUI.class.getResourceAsStream("tiles/RES.png"));
                                ImageView iv = new ImageView(aux1);
                                iv.setRotate(0);
                                ajustarImageView(iv);
                                taul.add(iv, x, y);
                                success = true;
                            }
                        }
                    }
                //let the source know whether the image was successfully transferred and used
                event.setDropCompleted(success);
                event.consume();
            }
        });
        
        root.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                //Data dropped
                //If there is an image on the dragboard, read it and use it
                boolean success = false;
                pila.setVisible(true);
                //let the source know whether the image was successfully transferred and used
                event.setDropCompleted(success);
                event.consume();
            }
        });
        
    }
    
    private void ajustarImageView(ImageView iv){
        if(row >= col){
            iv.setFitHeight(numAux/row);
            iv.setFitWidth(numAux/row);
        }
        else{
            iv.setFitHeight(numAux/col);
            iv.setFitWidth(numAux/col);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
