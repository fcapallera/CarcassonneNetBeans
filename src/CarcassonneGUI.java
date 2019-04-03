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
        row = 9;
        col = 8;
        numAux = 690;
        root = new BorderPane();
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
        
        
        
        
       
        
        Scene scene = new Scene(root, 1000, 600);
        
        
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        
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
                pila.setVisible(false);
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
                            node.setOpacity(0.7);

                            node.setOnDragExited(new EventHandler<DragEvent>() {
                                public void handle(DragEvent event) {
                                    //mouse moved away, remove graphical cues
                                    node.setOpacity(1);
                                    event.consume();
                                }
                            });
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
                    
                    
                    pila.setVisible(false);
                }
                event.consume();
            }
        });
        
        //Drag exited reverts the appearance of the receiving node when the mouse is outside of the node
        taul.setOnDragExited(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                //mouse moved away, remove graphical cues
                pila.setVisible(true);
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
                        // TODO: set image size; use correct column/row span
                        taul.add(image, x, y);
                        success = true;
                }
                //let the source know whether the image was successfully transferred and used
                event.setDropCompleted(success);


                event.consume();
            }
        });
        
        pila.setOnDragDone(new EventHandler<DragEvent>() {
        public void handle(DragEvent event) {
            //the drag and drop gesture has ended
            //if the data was successfully moved, clear it
            if(event.getTransferMode() == TransferMode.MOVE){
                pila.setVisible(false);
            }
            event.consume();
        }
        });
        
        

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
        pila = new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("tiles/CCFCF.png")));
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
        
        for(int i = 0; i < col; i++){
            for(int j = 0; j < row; j++){
                ImageView imageChart;
                if(i!=0 && i!=col-1 && j!=0 && j!=row-1){
                    imageChart = new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("tiles/CCFCF.png")));
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
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
