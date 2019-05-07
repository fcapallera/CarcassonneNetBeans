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
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
    private GridPane root0;
    private BorderPane colocarSeg;
    private GridPane taul;
    private GridPane dreta;
    private GridPane esquerra;
    private Button rota;
    private Button passa;
    private Button afegirSeguidor;
    private Button top;
    private Button bot;
    private Button left;
    private Button right;
    private Button center;
    private Button acabar_torn;
    private Button jugar;
    private TextField nJugadors;
    private Text tornJugador;
    private ImageView pila;
    private int rotacioPila; 
    private int numJug;
    private Joc _joc;
    private Text numPila;
    private int lastxHash;
    private int lastyHash;
    private Scene escena;
    

    
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
        
        rotacioPila = 0;
        
        Scene scene = new Scene(root, 1000, 600);
        Scene scene1 = triarNJugadors();
        Image image = new Image("images/cursor.png");  //pass in the image path
        scene.setCursor(new ImageCursor(image));
        escena = scene;
        primaryStage.setTitle("CARCASSONNE");
        primaryStage.setScene(scene1);
        primaryStage.setMaximized(false);
        primaryStage.show();
        
        jugar.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                numJug = Integer.parseInt(nJugadors.getText());

                taul = getCenterGridPane();
                dreta = getRightGridPane();
                esquerra = getLeftGridPane();
                root.setAlignment(taul,Pos.CENTER);
                BorderPane.setAlignment(dreta,Pos.CENTER);
                BorderPane.setAlignment(esquerra,Pos.TOP_CENTER);
                BorderPane.setMargin(taul, new Insets(30, 10, 10, 50));
                BorderPane.setMargin(dreta, new Insets(10, 50, 10, 10));
                BorderPane.setMargin(esquerra, new Insets(10, 50, 10, 10));
                root.setRight(dreta);
                taul.setAlignment(Pos.CENTER);
                root.setCenter(taul);
                root.setLeft(esquerra);
                assignarEventListeners();
                actualitzarPuntuacio(1,10);
                actualitzarPuntuacio(2,20);
                primaryStage.setScene(escena);
                primaryStage.setMaximized(true);
            }
        });
        
        
        
        _joc.jugar();
    }
    
    private Scene triarNJugadors(){
        //EFECTES DE TEXT        
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
        ///
        
        root0 = new GridPane();
        Scene scene = new Scene(root0, 800, 449);
        Image image2 = new Image(CarcassonneGUI.class.getResourceAsStream("images/wallpaper1.jpg"));
        BackgroundSize bSize = new BackgroundSize(root0.getWidth(), root0.getHeight(), false, false, true, false);
        root0.setBackground(new Background(new BackgroundImage(image2,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bSize)));
        Text category = new Text("N Jugadors (2-5)");
        category.setEffect(ds);
        category.setCache(true);
        category.setFill(Color.SILVER);
        category.setFont(Font.font(null, FontWeight.BOLD, 20));
        nJugadors = new TextField ();
        HBox hb = new HBox();
        jugar = new Button("Jugar");
        root0.add(category, 0,0);
        root0.add(nJugadors,1,0);
        root0.add(jugar,1,1);
        root0.setAlignment(Pos.BOTTOM_RIGHT);
        return scene;
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
        grid.setHgap(20);
        grid.setVgap(40);
        
        int numAux = 200;
        
        //EFECTES DE TEXT        
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
        ///
        
        Text category = new Text("JUGADORS");
        category.setEffect(ds);
        category.setCache(true);
        category.setFill(Color.SILVER);
        category.setFont(Font.font(null, FontWeight.BOLD, 20));

        grid.add(category, 0, 0);
        category = new Text("PUNTS");
        category.setEffect(ds);
        category.setCache(true);
        category.setFill(Color.SILVER);
        category.setFont(Font.font(null, FontWeight.BOLD, 20));
        grid.add(category, 1, 0);
        for(int i = 0; i < 4; i++){
            for(int j = 1; j <= numJug; j++){
                if(i == 0){
                    category = new Text("Jugador"+ j);
                    category.setEffect(ds);
                    category.setCache(true);
                    category.setFill(Color.WHITE);
                    category.setFont(Font.font(null, FontWeight.BOLD, 22));
                    grid.add(category, i, j); 
                }
                else if(i == 1){
                    category = new Text("0");
                    category.setEffect(ds);
                    category.setCache(true);
                    category.setFill(Color.WHITE);
                    category.setFont(Font.font(null, FontWeight.BOLD, 22));
                    grid.add(category, i, j);
                }
                else if (i == 2){
                    ImageView iv = new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("seguidors/" + (j-1) + ".png")));
                    iv.setFitHeight(30);
                    iv.setFitWidth(25);
                    grid.add(iv,i,j);
                }
                else{
                    category = new Text("(x7)");
                    category.setEffect(ds);
                    category.setCache(true);
                    category.setFill(Color.WHITE);
                    category.setFont(Font.font(null, FontWeight.BOLD, 22));
                    grid.add(category, i, j);
                }
            }
        }
        return grid;
    }

    public GridPane getRightGridPane(){
        //CREEM UN NOU GRID PANE DE 1x2
        GridPane grid = new GridPane();
        grid.setHgap(0);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 0, 0, 0));
        
        //EFECTES DE TEXT        
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
        ///
        
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
        acabar_torn = new Button();
        acabar_torn.setGraphic(new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("images/acabar_torn.png"))));
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
        acabar_torn.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");
        tornJugador = new Text("JUGADOR 1");
        tornJugador.setEffect(ds);
        tornJugador.setCache(true);
        tornJugador.setFill(Color.WHITE);
        tornJugador.setFont(Font.font(null, FontWeight.BOLD, 20));
        
        //AFEGIM ELS COMPONENTS AL GRID PANE
        grid.add(tornJugador,1,0);
        grid.add(numPila,1,1);
        grid.add(pila, 1, 2);
        grid.add(rota,1,3);
        grid.add(passa,1,4);
        grid.add(afegirSeguidor,1,5);
        grid.add(acabar_torn,2,5);
        grid.add(top,1,6);
        grid.add(center,1,7);
        grid.add(left,0,7);
        grid.add(right,2,7);
        grid.add(bot,1,8);
        grid.setAlignment(Pos.CENTER);
        pila.setFitHeight(numAux/5);
        pila.setFitWidth(numAux/5);
        amagarBotons(1);
        
        
        
        
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
            StackPane casella = new StackPane();
            casella.getChildren().add(aux2);
            nouGrid.add(casella,aux_x,aux_y);
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
            event.consume();
        }
        });
        
        afegirSeguidor.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                mostrarBotons(3);
            }
        });
        
        acabar_torn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                amagarBotons(1);
                if(!_joc.getPila().isEmpty()){
                    pila.setVisible(true);
                    Image aux = new Image(CarcassonneGUI.class.getResourceAsStream("tiles/" + _joc.peekPila().get_codi() + ".png"));
                    pila.setImage(aux);
                    pila.setRotate(_joc.peekPila().getIndexRotacio()*90);
                    numPila.setText(String.valueOf(_joc.getPila().size()));
                }
                else{
                    pila.setVisible(false);
                    numPila.setText(String.valueOf(_joc.getPila().size()));
                }
                mostrarBotons(1);
                actualitzarPuntuacio(_joc.getTorn(),1);
                //actualitzarPuntuacio(_joc.getTorn(),_joc.jugadorN(_joc.getTorn()).getPunts());
                actualitzarTornJugador();
                _joc.jugar();
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
        
        top.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                afegirSeguidor(Pos.TOP_CENTER);
            }
        });
        bot.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                afegirSeguidor(Pos.BOTTOM_CENTER);            
            }
        });
        center.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                afegirSeguidor(Pos.CENTER);            
            }
        });
        left.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                afegirSeguidor(Pos.CENTER_LEFT);           
            }
        });
        right.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                afegirSeguidor(Pos.CENTER_RIGHT);
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
                            int x = cIndex == null ? -1 : cIndex; //SI L'INDEX ES NULL, ES POSA A -1
                            int y = rIndex == null ? -1 : rIndex; //SI L'INDEX ES NULL, ES POSA A -1
                            if(x != -1){
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
                        int x = cIndex == null ? -1 : cIndex;
                        int y = rIndex == null ? -1 : rIndex;
                        int xHash = getXHash(x);
                        int yHash = getYHash(y);
                        if(!_joc.getTaulaJoc().getTauler().containsKey(xHash * 100 + yHash)){
                            if(_joc.getTaulaJoc().jugadaValida(_joc.peekPila(), xHash, yHash)){
                                Image aux1 = new Image(CarcassonneGUI.class.getResourceAsStream("tiles/" + _joc.peekPila().get_codi() + ".png"));
                                ImageView iv = new ImageView(aux1);
                                iv.setRotate(_joc.peekPila().getIndexRotacio()*90);
                                ajustarImageView(iv);
                                StackPane casella = new StackPane();
                                casella.getChildren().add(iv);
                                taul.getChildren().remove(getNodeFromGridPane(taul,x,y));
                                taul.add(casella, x, y);
                                lastxHash = xHash;
                                lastyHash = yHash;
                                success = true;
                                Peça p = _joc.popPila();
                                _joc.getTaulaJoc().afegirPeça(p, xHash, yHash);
                                if(esLimit(x,y)){
                                    resizeTauler(x,y);
                                }
                                
                                amagarBotons(2);
                                mostrarBotons(2);
                                
                            }
                            else{
                                Image aux1 = new Image(CarcassonneGUI.class.getResourceAsStream("tiles/RES.png"));
                                ImageView iv = new ImageView(aux1);
                                iv.setRotate(0);
                                ajustarImageView(iv);
                                taul.getChildren().remove(getNodeFromGridPane(taul,x,y));
                                taul.add(iv, x, y);
                                success = false;
                            }
                        }
                    }
                Image image = new Image("images/cursor.png");  //pass in the image path
                escena.setCursor(new ImageCursor(image));
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
    
    private void ajustarSeguidor(ImageView iv){
        if(row >= col){
            iv.setFitHeight((numAux/row)/3.5);
            iv.setFitWidth((numAux/row)/3.5);
        }
        else{
            iv.setFitHeight((numAux/col)/3.5);
            iv.setFitWidth((numAux/col)/3.5);
        }
    }
    
    private void afegirSeguidor(Pos value){
        ImageView aux1 = new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("/tiles/"+_joc.getTaulaJoc().getPeça(lastxHash, lastyHash).get_codi()+".png")));
        ImageView aux = new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("/seguidors/"+_joc.getTorn()+".png")));
        ajustarImageView(aux1);
        ajustarSeguidor(aux);
        aux1.setRotate(_joc.getTaulaJoc().getPeça(lastxHash, lastyHash).getIndexRotacio()*90);
        StackPane sp = new StackPane();
        sp.getChildren().add(aux1);
        sp.getChildren().add(aux);
        StackPane.setAlignment(aux,value);
        int x = getXTauler(lastxHash);
        int y = getYTauler(lastyHash);
        taul.add(sp,x,y);
    }
    
    private void amagarBotons(int n){
        if(n == 1){
            top.setVisible(false);
            bot.setVisible(false);
            center.setVisible(false);
            left.setVisible(false);
            right.setVisible(false);
            acabar_torn.setVisible(false);
            afegirSeguidor.setVisible(false);
        }
        else if(n == 2){
            pila.setVisible(false);
            passa.setVisible(false);
            rota.setVisible(false);
            numPila.setVisible(false); 
        }
    }
    
    private void mostrarBotons(int n){
        if(n == 1){
            passa.setVisible(true);
            rota.setVisible(true);
            numPila.setVisible(true);
        }
        else if(n == 2){
            afegirSeguidor.setVisible(true);
            acabar_torn.setVisible(true);
        }
        else if(n == 3){
            top.setVisible(true);
            bot.setVisible(true);
            center.setVisible(true);
            left.setVisible(true);
            right.setVisible(true);
        }
    }
    
    public void actualitzarPuntuacio(int nJugador, int puntuacio){
        if(nJugador == 0){
            nJugador = _joc.getnJugadors();
        }
            //EFECTES DE TEXT        
            DropShadow ds = new DropShadow();
            ds.setOffsetY(3.0f);
            ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
            ///
            String aux = String.valueOf(puntuacio);
            Text category = new Text(aux);
            category.setEffect(ds);
            category.setCache(true);
            category.setFill(Color.WHITE);
            category.setFont(Font.font(null, FontWeight.BOLD, 22));
            esquerra.getChildren().remove(getNodeFromGridPane(esquerra,1,nJugador));
            esquerra.add(category, 1, nJugador);
    }
    
    public void actualitzarTornJugador(){
        //EFECTES DE TEXT        
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
        ///
        String aux = String.valueOf(_joc.getTorn()+1);
        Text category = new Text("JUGADOR "+aux);
        category.setEffect(ds);
        category.setCache(true);
        category.setFill(Color.WHITE);
        category.setFont(Font.font(null, FontWeight.BOLD, 22));
        dreta.getChildren().remove(getNodeFromGridPane(dreta,1,0));
        dreta.add(category, 1, 0);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
