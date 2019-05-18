/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.scene.image.Image ;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
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
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
    private int lastxHashSeguidor;
    private int lastyHashSeguidor;
    private Scene escena;
    private Pos lastPos;
    private Stage _primaryStage;

    

    
    @Override
    public void start(Stage primaryStage) {
        _primaryStage = primaryStage;
        row = 3;
        col = 3;
        rotacioPila = 0;
        numAux = 690;
        root = new BorderPane();
        Image image2 = new Image(CarcassonneGUI.class.getResourceAsStream("images/wallpaper.jpg"));
        BackgroundSize bSize = new BackgroundSize(root.getWidth(), root.getHeight(), false, false, true, false);
        root.setBackground(new Background(new BackgroundImage(image2,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bSize)));
             
        Image image = new Image("images/cursor.png");  //pass in the image path
        Scene scene = new Scene(root, 1000, 600);
        scene.setCursor(new ImageCursor(image));
        escena = scene;
        
        
        //CREEM LA PANTALLA PER SELECCIONAR EL NUMERO DE JUGADORS I LA MOSTREM
        Scene scene1 = triarNJugadors();
        primaryStage.setTitle("CARCASSONNE");
        primaryStage.setScene(scene1);
        primaryStage.setMaximized(false);
        primaryStage.show();
        
        //INICIALITZEM LES ESTRUCTURES UNA VEGADA CLICKEM AL BOTO JUGAR
        jugar.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                numJug = Integer.parseInt(nJugadors.getText());
                _joc = new Joc("2",numJug);
                taul = getCenterGridPane();
                dreta = getRightGridPane();
                esquerra = getLeftGridPane();
                BorderPane.setAlignment(dreta,Pos.CENTER);
                BorderPane.setAlignment(esquerra,Pos.TOP_CENTER);
                BorderPane.setMargin(taul, new Insets(30, 10, 10, 50));
                BorderPane.setMargin(dreta, new Insets(10, 50, 10, 10));
                BorderPane.setMargin(esquerra, new Insets(10, 50, 10, 10));
                root.setAlignment(taul,Pos.CENTER);
                root.setRight(dreta);
                taul.setAlignment(Pos.CENTER);
                root.setCenter(taul);
                root.setLeft(esquerra);
                assignarEventListeners();
                primaryStage.setScene(escena);
                primaryStage.setMaximized(true);
                _joc.jugar();
            }
        }); 
    }
    
    private Scene triarNJugadors(){
        //INICIALITZACIÓ DEL GRID PANE
        GridPane root0 = new GridPane();
        Image image2 = new Image(CarcassonneGUI.class.getResourceAsStream("images/wallpaper1.jpg"));
        BackgroundSize bSize = new BackgroundSize(root0.getWidth(), root0.getHeight(), false, false, true, false);
        root0.setBackground(new Background(new BackgroundImage(image2,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,bSize)));
        root0.setAlignment(Pos.BOTTOM_RIGHT);
        
        //INICIALITZEM ELS ELEMENTS QUE ANIRAN AL GRID PANE
        Text category = new Text("N Jugadors (2-5)");
        aplicarStyleText(category,Color.SILVER,20);
        nJugadors = new TextField ();
        jugar = new Button("Jugar");
        
        //AFEGIM ELS ELEMENTS AL GRID PANE
        root0.add(category, 0,0);
        root0.add(nJugadors,1,0);
        root0.add(jugar,1,1);
        
        //AFEGIM EL GRID PANE A L'ESCENA I LA RETORNEM
        Scene scene = new Scene(root0, 800, 449);
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
        int numAux = 200;

        //INICIALITZACIO DEL GRID PANE
        GridPane grid = new GridPane();
        GridPane grid2 = new GridPane();
        grid.setHgap(20);
        grid.setVgap(40);
        
        
        //INICIALITZEM ELS ELEMENTS I ELS INSERIM AL GRID PANE
        Text category = new Text("JUGADORS");
        aplicarStyleText(category,Color.SILVER,20);
        grid.add(category, 0, 0);
        
        category = new Text("PUNTS");
        aplicarStyleText(category,Color.SILVER,20);
        grid.add(category, 1, 0);
        
        for(int i = 0; i < 4; i++){
            for(int j = 1; j <= numJug; j++){
                if(i == 0){
                    category = new Text("Jugador"+ j);
                    aplicarStyleText(category,Color.WHITE,22);
                    grid.add(category, i, j); 
                }
                else if(i == 1){
                    category = new Text("0");
                    aplicarStyleText(category,Color.WHITE,22);
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
                    aplicarStyleText(category,Color.WHITE,22);
                    grid.add(category, i, j);
                }
            }
        }
        
        return grid;
    }

    public GridPane getRightGridPane(){
        //CREEM UN NOU GRID PANE
        GridPane grid = new GridPane();
        grid.setHgap(0);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 0, 0, 0));
        
        //INICIALITZEM ELS ELEMENTS QUE HI HAURÀ AL GRID PANE
        pila = new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("tiles/" + _joc.peekPila().get_codi() + ".png")));
        pila.setFitHeight(numAux/5);
        pila.setFitWidth(numAux/5);
        
        rota = new Button();
        rota.setGraphic(new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("images/button.png"))));
        rota.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");
        
        passa = new Button();
        passa.setGraphic(new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("images/next.png"))));
        passa.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");
        
        afegirSeguidor = new Button();
        afegirSeguidor.setGraphic(new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("images/afegirSeguidor.png"))));
        afegirSeguidor.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");
        
        top = new Button();
        top.setGraphic(new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("images/top.png"))));
        top.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");
        
        bot = new Button();
        bot.setGraphic(new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("images/bot.png"))));
        bot.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");
        
        left = new Button();
        left.setGraphic(new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("images/left.png"))));
        left.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");
        
        right = new Button();
        right.setGraphic(new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("images/right.png"))));
        right.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");
        
        center = new Button();
        center.setGraphic(new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("images/center.png"))));
        center.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");
        
        acabar_torn = new Button();
        acabar_torn.setGraphic(new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("images/acabar_torn.png"))));
        acabar_torn.setStyle("-fx-focus-color: transparent;-fx-background-color: transparent;");
        
        numPila = new Text();
        aplicarStyleText(numPila,Color.WHITE,26);
        numPila.setText(String.valueOf(_joc.getPila().size()));
        
        tornJugador = new Text("JUGADOR 1");
        aplicarStyleText(tornJugador,Color.WHITE,20);
        
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
        
        refreshBotons(new int[]{0,0,1,1,1,1,1,1,1},false);

        return grid;
    }
    
    public GridPane getCenterGridPane(){
        //CREEM EL GRID PANE BUIT
        GridPane grid = gridPaneBuit(row,col);
        
        //AGAFEM EL CODI DE LA PEÇA INICIAL I L'INSERIM
        Peça aux = _joc.getTaulaJoc().getTauler().get(0);
        ImageView imageChart = new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("tiles/" + aux.get_codi() + ".png")));
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
    
    private void refreshTauler(int x, int y){
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
        boolean teSeguidor = false;
        int seguidor = -1;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Peça p =(Peça) pair.getValue();
            
            //DETECTEM SI LA PEÇA TE SEGUIDOR I ON
            teSeguidor = false;
            seguidor = -1;
            int i;
            for(i = 0; i <= 4 ; i++){
                if(p.getRegio(i-1)!=null){
                    if(p.getRegio(i-1).hiHaSeguidor()){
                        teSeguidor = true;
                        seguidor = i;
                    }
                }
            }
            
            //CARREGUEM LA IMATGE DE LA CASELLA
            ImageView imatgePeça = new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("tiles/" + p.get_codi() + ".png")));
            imatgePeça.setRotate(p.getIndexRotacio()*90);
            ajustarImageView(imatgePeça);

            //AFEGIM LA IMATGE A UN STACK PANE
            StackPane casella = new StackPane();
            casella.getChildren().add(imatgePeça);
            
            //SI LA PEÇA TE SEGUIDOR, CRIDEM EL METODE PER AFEGIR-LO A L'STACK PANE ON ACABEM D'AFEGIR LA IMATGE DE LA PEÇA
            if(teSeguidor){
                Jugador jug = p.getRegio(seguidor-1).getJugador();
                afegirSeguidorStack(seguidor, casella,jug);
            }
            
            //AFEGIM LA PEÇA AL TAULER
            int aux_x = getXTauler(p.get_x());
            int aux_y = getYTauler(p.get_y());
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
                if(_joc.jugadorN(_joc.getTorn()).getSeguidors()>0){
                    List<Integer> aux = _joc.getTaulaJoc().seguidorsValids(lastxHash, lastyHash, _joc.jugadorN(_joc.getTorn()));
                    mostrarBotonsSeguidor(aux);
                }      
            }
        });
        
        acabar_torn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                refreshBotons(new int[]{0,0,1,1,1,1,1,1,1},false);
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
                    passa.setVisible(false);
                    rota.setVisible(false);
                    escena = acabarJoc();
                    _primaryStage.setScene(escena);
                    _primaryStage.show();
                }
                
                numPila.setVisible(true);
                refreshBotons(new int[]{1,1,0,0,0,0,0,0,0},true);
              
                if(lastPos != null){
                    inserirSeguidor();
                }
                lastPos = null;
                
                _joc.passarTorn();
                actualitzarTornJugador();
                _joc.jugar();
                actualitzarSeguidors();
                refreshTauler(1,1);
                for(int i=0;i < _joc.getnJugadors();i++){
                    actualitzarPuntuacio(i,_joc.jugadorN(i).getPunts());
                }
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
                    passa.setVisible(false);
                    rota.setVisible(false);
                    escena = acabarJoc();
                    _primaryStage.setScene(escena);
                    _primaryStage.show();
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
                                    refreshTauler(x,y);
                                }
                                pila.setVisible(false);
                                numPila.setVisible(false);
                                refreshBotons(new int[]{1,1,0,0,0,0,0,0,0},false);
                                refreshBotons(new int[]{0,0,1,1,0,0,0,0,0},true);

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
        ImageView imatgePeça = new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("/tiles/"+_joc.getTaulaJoc().getPeça(lastxHash, lastyHash).get_codi()+".png")));
        ImageView imatgeSeguidor = new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("/seguidors/"+_joc.getTorn()+".png")));
        ajustarImageView(imatgePeça);
        ajustarSeguidor(imatgeSeguidor);
        imatgePeça.setRotate(_joc.getTaulaJoc().getPeça(lastxHash, lastyHash).getIndexRotacio()*90);
        StackPane sp = new StackPane();
        sp.getChildren().add(imatgePeça);
        sp.getChildren().add(imatgeSeguidor);
        StackPane.setAlignment(imatgeSeguidor,value);
        int x = getXTauler(lastxHash);
        int y = getYTauler(lastyHash);
        taul.add(sp,x,y);
        
        //GUARDEM LA POSICIO ON S'HA INSERIT
        lastPos = value;
    }
    
    public void refreshBotons(int[] aux,boolean mostrar){
        Button[] buttons = new Button[]{passa,rota,afegirSeguidor,acabar_torn,center,top,right,bot,left};
        for(int i = 0;i < aux.length; i++){
            if(aux[i] == 1){
                buttons[i].setVisible(mostrar);
            }
        }
    }
    
    public void mostrarBotonsSeguidor(List<Integer> aux){
        Button[] buttons = new Button[]{center,top,right,bot,left};
        for(int i = 0;i < 5; i++){
            if(aux.get(i) == 1){
                buttons[i].setVisible(true);
            }
        }
    }
    
    public void actualitzarPuntuacio(int nJugador, int puntuacio){
            String aux = String.valueOf(puntuacio);
            Text category = new Text(aux);
            aplicarStyleText(category,Color.WHITE,22);
            esquerra.getChildren().remove(getNodeFromGridPane(esquerra,1,nJugador+1));
            esquerra.add(category, 1, nJugador+1);
    }
    
    public void actualitzarSeguidors(){        
        for(int i = 0; i < numJug; i++){
            String aux1 = String.valueOf(_joc.jugadorN(i).getSeguidors());
            String aux = "(x"+aux1+")";
            Text category = new Text(aux);
            aplicarStyleText(category,Color.WHITE,22);
            esquerra.getChildren().remove(getNodeFromGridPane(esquerra,3,i+1));
            esquerra.add(category, 3, i+1);
        }     
    }
    
    public void actualitzarTornJugador(){
        String aux = String.valueOf(_joc.getTorn()+1);
        Text category = new Text("JUGADOR "+aux);
        aplicarStyleText(category,Color.WHITE,22);
        dreta.getChildren().remove(getNodeFromGridPane(dreta,1,0));
        dreta.add(category, 1, 0);
    }
    
    private void inserirSeguidor(){
        if(lastPos == Pos.CENTER){
            _joc.getTaulaJoc().afegirSeguidor(lastxHash, lastyHash, 0, _joc.jugadorN(_joc.getTorn()));
        }
        else if(lastPos == Pos.CENTER_LEFT){
            _joc.getTaulaJoc().afegirSeguidor(lastxHash, lastyHash, 4, _joc.jugadorN(_joc.getTorn()));
        }
        else if(lastPos == Pos.CENTER_RIGHT){
            _joc.getTaulaJoc().afegirSeguidor(lastxHash, lastyHash, 2, _joc.jugadorN(_joc.getTorn()));
        }
        else if(lastPos == Pos.TOP_CENTER){
            _joc.getTaulaJoc().afegirSeguidor(lastxHash, lastyHash, 1, _joc.jugadorN(_joc.getTorn()));
        }
        else if(lastPos == Pos.BOTTOM_CENTER){
            _joc.getTaulaJoc().afegirSeguidor(lastxHash, lastyHash, 3, _joc.jugadorN(_joc.getTorn()));
        }
    }
    
    private void afegirSeguidorStack(int pos, StackPane sp,Jugador jug){
        Pos[] posicions = new Pos[]{Pos.CENTER,Pos.TOP_CENTER,Pos.CENTER_RIGHT,Pos.BOTTOM_CENTER,Pos.CENTER_LEFT};
        ImageView aux = new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("/seguidors/"+jug.getId()+".png")));
        ajustarSeguidor(aux);
        sp.getChildren().add(aux);
        Pos value = posicions[pos];
        StackPane.setAlignment(aux,value);
    }
    
    public int getNumJug(){
        return numJug;
    }
    
    public Scene acabarJoc(){        
        GridPane root0 = new GridPane();
        root0.setHgap(20);
        root0.setVgap(40);
        
        Scene scene = new Scene(root0, 800, 449);
        BackgroundSize bSize = new BackgroundSize(root0.getWidth(), root0.getHeight(), false, false, true, false);

        Text category = new Text("JUGADOR");
        Text category1 = new Text("PUNTS");      
        aplicarStyleText(category,Color.BLUE,20);
        aplicarStyleText(category1,Color.BLUE,20);

        root0.add(category, 0,0);
        root0.add(category1,1,0);
        //Obtenir jugadors en una array ordenats per puntuacio
        ArrayList<Jugador> jug = new ArrayList();//FCAP UN METODE QUE ELS RETORNI(?)
        for(int i=0; i<jug.size();i++){
            Text t = new Text("JUGADOR "+jug.get(i).getId());
            Text t1 = new Text(String.valueOf(jug.get(i).getPunts()));
            ImageView iv = new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("seguidors/" + jug.get(i).getId() + ".png")));          
            aplicarStyleText(t,Color.BLUE,20);
            aplicarStyleText(t1,Color.BLUE,20);
            iv.setFitHeight(30);
            iv.setFitWidth(25);
            
            root0.add(t,0,i+1);
            root0.add(t1,1,i+1);
            root0.add(iv,2,i+1);
        }
  
        root0.setAlignment(Pos.CENTER);
        return scene;
    }
    
    
    public void aplicarStyleText(Text t,Color c,int size){
            //EFECTES DE TEXT        
            DropShadow ds = new DropShadow();
            ds.setOffsetY(3.0f);
            ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
            ///
            t.setEffect(ds);
            t.setCache(true);
            t.setFill(c);
            t.setFont(Font.font(null, FontWeight.BOLD, size));
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
