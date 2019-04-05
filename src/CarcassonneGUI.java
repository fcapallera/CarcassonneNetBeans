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
    
    static final String PROVES_SRC = "src/proves/";

    private Stack<Peça> _peces;
    private ArrayList<Jugador> _jugadors;
    private Tauler _tauler;

    public void init(String arxiu){
        File f = new File(PROVES_SRC+arxiu+".txt");
        HashMap<String,Integer> peces = new HashMap<>();
        boolean error = false;
        try {
            Scanner lector = new Scanner(f);

            //Llegim els jugadors
            if(lector.hasNext() && lector.next().equals("nombre_jugadors")){
                int njugs = lector.nextInt();
                for(int i=0;i<njugs;i++) _jugadors.add(new Jugador(i));
                if(lector.hasNext() && lector.next().equals("jugadors_cpu")){
                    while(lector.hasNextInt()){
                        int j_cpu = lector.nextInt();
                        _jugadors.get(j_cpu-1).setCpu();
                    }
                } else error = true;
            } else error = true;


            //Llegim les peces
            if(lector.hasNext() && lector.next().equals("rajoles")){
                String codi;
                while(!(codi = lector.next()).equals("#")) {
                    Integer nombre = lector.nextInt();
                    peces.put(codi, nombre);
                }
            } else error = true;

            //Llegim la rajola inicial
            if(lector.hasNext() && lector.next().equals("rajola_inicial")){
                String inicial = lector.next();
                peces.put(inicial,peces.get(inicial)-1);
                _tauler.afegirPeça(new Peça(inicial),0,0);
            } else error = true;

        } catch (FileNotFoundException e){
            System.err.println(e);
        }

        //Afegim les peces al stack
        if(!error){
            Iterator it = peces.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry pair = (Map.Entry)it.next();
                for(int i=0;i<(Integer)pair.getValue();i++){
                    Peça p = new Peça((String)pair.getKey());

                    _peces.push(p);
                }
            }
            Collections.shuffle(_peces);
        } else System.out.println("Error");
    }
    
    
    
    private BorderPane root;
    private GridPane taul;
    private GridPane dreta;
    private Button rota;
    private ImageView pila;
    private int rotacioPila; 
    
    @Override
    public void start(Stage primaryStage) {
        _peces = new Stack<>();
        _jugadors = new ArrayList<>();
        _tauler = new Tauler(false);
        init("1");
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
        root.setCenter(taul);
        root.setLeft(getLeftGridPane());
        rotacioPila = 0;
        assignarEventListeners();
        
        
        
       
        
        Scene scene = new Scene(root, 1000, 600);
        
        
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
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
        category.setFont(Font.font(null, FontWeight.BOLD, 20));
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
        //Carreguem la peça de dalt de tot de l'stack
        GridPane grid = new GridPane();
        grid.setHgap(0);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 0, 0, 0));
        String a = "tiles/";
        String b = ".png";
        pila = new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream(a + _peces.peek().get_codi() + b)));
        ImageView aux = new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("images/button.png")));
        rota = new Button();
        rota.setGraphic(aux);
        grid.add(pila, 0, 0);
        grid.add(rota,0,1);
        grid.setAlignment(Pos.CENTER);
        pila.setFitHeight(numAux/5);
        pila.setFitWidth(numAux/5);
        return grid;
    }
    
    public GridPane getCenterGridPane(){
        GridPane grid = new GridPane();
        grid.setHgap(0);
        grid.setVgap(0);
        grid.setPadding(new Insets(0, 0, 0, 0));
        
        /*for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                Text category = new Text("("+String.valueOf(i)+","+String.valueOf(j)+")");
                category.setFont(Font.font("Arial", FontWeight.BOLD, 90));
                grid.add(category, i, j);
            }
        }*/
        
        String a = "tiles/";
        String b = ".png";
        Iterator it = _tauler.getTauler().entrySet().iterator();
        Map.Entry pair = (Map.Entry)it.next();
        Peça aux =(Peça) pair.getValue();

        
        
        
        for(int i = 0; i < col; i++){
            for(int j = 0; j < row; j++){
                ImageView imageChart;
                if(i!=0 && i!=col-1 && j!=0 && j!=row-1){
                    imageChart = new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream(a + aux.get_codi() + b)));
                }
                else{
                    imageChart = new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("tiles/RES.png")));
                }
                if(row >= col){
                        imageChart.setFitHeight(numAux/row);
                        imageChart.setFitWidth(numAux/row);
                    }
                    else{
                        imageChart.setFitHeight(numAux/col);
                        imageChart.setFitWidth(numAux/col);
                }
                grid.add(imageChart, i, j);
            }
        }
        grid.setAlignment(Pos.CENTER);
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
        GridPane nouGrid = gridPaneBuit(x,y);
        nouGrid.setAlignment(Pos.CENTER);
        taul = nouGrid;
        Iterator it = _tauler.getTauler().entrySet().iterator();
        String a = "tiles/";
        String b = ".png";
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Peça aux =(Peça) pair.getValue();
            ImageView aux2 = new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream(a + aux.get_codi() + b)));
            int aux_x = aux.get_x() - _tauler.getMinX() + 1;
            int aux_y = (aux.get_y()*(-1)) + _tauler.getMaxY() + 1;
            if(row >= col){
                aux2.setFitHeight(numAux/row);
                aux2.setFitWidth(numAux/row);
            }
            else{
                aux2.setFitHeight(numAux/col);
                aux2.setFitWidth(numAux/col);
            }
            nouGrid.add(aux2,aux_x,aux_y);
        }
        root.setCenter(taul);
        assignarEventListeners();
    }
    
    private boolean adjacenciesValides(int x, int y){ //X i Y del gridPane
        boolean res = false;
        boolean incompatible = false;
        System.out.println(x+" "+y);
        Peça p = _tauler.getPeça(getXHash(x),getYHash(y));
        System.out.println(getXHash(x)+" "+getYHash(y));
        if(!incompatible && x != 0){//0123 NESW
            //COMPROVAR ESQUERRA
            Peça aux = p.getPeçaAdjacent(3);
            if(aux != null){
                if(p.esCompatible(p.getPeçaAdjacent(3), 3)){
                    res = true;
                }
                else{
                    incompatible = true;
                    res = false;
                }
            }
            else{
                System.out.println("A");
            }
        }
        if(!incompatible && y != 0){
            //COMPROVAR DALT
            Peça aux = p.getPeçaAdjacent(0);
            if(aux != null){
                if(p.esCompatible(p.getPeçaAdjacent(0), 0)){
                    res = true;
                }
                else{
                    incompatible = true;
                    res = false;
                }
            }
            else{
                System.out.println("AA");
            }
        }
        if(!incompatible && x != col-1){
            //COMPROVAR DRETA
            Peça aux = p.getPeçaAdjacent(1);
            if(aux != null){
                if(p.esCompatible(p.getPeçaAdjacent(1), 1)){
                    res = true;
                }
                else{
                    incompatible = true;
                    res = false;
                }
            }
            else{
                System.out.println("AAA");
            }
        }
        if(!incompatible && y != row-1){
            //COMPROVAR BAIX
            Peça aux = p.getPeçaAdjacent(2);
            if(aux != null){
                if(p.esCompatible(p.getPeçaAdjacent(2), 2)){
                    res = true;
                }
                else{
                    incompatible = true;
                    res = false;
                }
            }
            else{
                System.out.println("AAAA");
            }
        }
        return res;
    }
    
    private int getXHash(int x){
        int res = x + _tauler.getMinX() - 1;
        return res;                      
    }
    
    private int getYHash(int y){
        int res = (y - _tauler.getMaxY() - 1)/(-1);
        return res;
    }
    
    private void assignarEventListeners(){
        pila.setOnDragDone(new EventHandler<DragEvent>() {
        public void handle(DragEvent event) {
            //the drag and drop gesture has ended
            //if the data was successfully moved, clear it
            if(event.getTransferMode() == TransferMode.MOVE){
                String a = "tiles/";
                String b = ".png";
                if(!_peces.isEmpty()){
                    Image aux = new Image(CarcassonneGUI.class.getResourceAsStream(a + _peces.peek().get_codi() + b));
                    pila.setImage(aux);
                    pila.setRotate(0);
                    rotacioPila = 0;
                }
                else{
                    pila.setVisible(false);
                }
                rotacioPila = 0;
            }
            event.consume();
        }
        });
        
        rota.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                pila.setRotate(pila.getRotate() + 90);
                if(rotacioPila != 3){
                    rotacioPila ++;
                }
                else{
                    rotacioPila = 0;
                }                
            }
        });
        
        
        //Drag detected event handler is used for adding drag functionality to the boat node
        pila.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event){
                //Drag was detected, start drap-and-drop gesture
                //Allow any transfer node
                Dragboard db = pila.startDragAndDrop(TransferMode.ANY);
                //Put ImageView on dragboard
                ClipboardContent cbContent = new ClipboardContent();
                Image snap = pila.snapshot(new SnapshotParameters(), null);
                Image image = pila.getImage();
                cbContent.putImage(image);
                db.setContent(cbContent);
                event.consume();
                //pila.setVisible(false);
            }

            private Rotate newRotate() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        //Drag over event handler is used for the receiving node to allow movement
        taul.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                //data is dragged over to target
                //accept it only if it is not dragged from the same node
                //and if it has image data
                if(event.getGestureSource() != taul && event.getDragboard().hasImage()){
                    //allow for moving
                    event.acceptTransferModes(TransferMode.MOVE);
                    Node node = event.getPickResult().getIntersectedNode();
                    if(node != taul){
                            Integer cIndex = GridPane.getColumnIndex(node);
                            Integer rIndex = GridPane.getRowIndex(node);
                            int x = cIndex == null ? 0 : cIndex;
                            int y = rIndex == null ? 0 : rIndex;
                            int xHash = getXHash(x);
                            int yHash = getYHash(y);
                            if(!_tauler.getTauler().containsKey(xHash * 100 + yHash)){ //CASELLA NO TE FOTO
                                String a = "tiles/";
                                String b = ".png";
                                Image aux1 = new Image(CarcassonneGUI.class.getResourceAsStream(a + _peces.peek().get_codi() + b));
                                Image aux2 = new Image(CarcassonneGUI.class.getResourceAsStream("tiles/RES.png"));
                                ImageView iv = new ImageView(aux1);
                                iv.setRotate(rotacioPila*90);
                                if(row >= col){
                                    iv.setFitHeight(numAux/row);
                                    iv.setFitWidth(numAux/row);
                                }
                                else{
                                    iv.setFitHeight(numAux/col);
                                    iv.setFitWidth(numAux/col);
                                }
                                taul.add(iv, x, y);

                                node.setOnDragExited(new EventHandler<DragEvent>() {
                                    public void handle(DragEvent event) {
                                        //mouse moved away, remove graphical cues
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
                //The drag-and-drop gesture entered the target
                //show the user that it is an actual gesture target
                if(event.getGestureSource() != taul && event.getDragboard().hasImage()){
                    //taul.setOpacity(0.7);
                    
                }
                event.consume();
            }
        });
        
        //Drag exited reverts the appearance of the receiving node when the mouse is outside of the node
        taul.setOnDragExited(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                //mouse moved away, remove graphical cues
                //taul.setOpacity(1);

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
                        if(!_tauler.getTauler().containsKey(xHash * 100 + yHash)){
                            //if(adjacenciesValides(x,y)){
                                ImageView image = new ImageView(db.getImage());
                                image.setRotate(image.getRotate() + (90*(rotacioPila)));
                                if(row >= col){
                                    image.setFitHeight(numAux/row);
                                    image.setFitWidth(numAux/row);
                                }
                                else{
                                    image.setFitHeight(numAux/col);
                                    image.setFitWidth(numAux/col);
                                }
                                int nouX = getXHash(x);
                                int nouY = getYHash(y);
                                // TODO: set image size; use correct column/row span
                                success = true;
                                Peça p = _peces.pop();
                                taul.add(image, x, y);
                                _tauler.afegirPeça(p, nouX, nouY);
                                if(esLimit(x,y)){
                                    resizeTauler(x,y);
                                }
                            //}    
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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
