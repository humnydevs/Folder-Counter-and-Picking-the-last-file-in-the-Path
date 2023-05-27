
package foldercounterpage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;


public class FolderCounterPage {
    int totalFolderDepth = 0;
    String lastFolderName = "";
    String lastFileName = null;
    String transversePath = "";
    String escapedFile =  "";

    public  VBox folderCouter(){
        int folderDepth = 0;
        VBox folderCounterContainer =  new VBox();
        folderCounterContainer.setAlignment(Pos.CENTER);
        folderCounterContainer.setPadding(new Insets(100));

        HBox btnContainer =  new HBox();
        Button folderBtn =  new Button("Folder");
        folderBtn.setOnAction(e->{
            File initialDirectory =  new File("C:\\Users\\humna\\Desktop\\AIINd");
            countFolder(folderDepth,initialDirectory,3);
            System.out.println("total folder transversed  :"+totalFolderDepth);
            System.out.println("last folder :"+lastFolderName);
            System.out.println("last file :" +lastFileName);
        });
        btnContainer.getChildren().add(folderBtn);
        btnContainer.setAlignment(Pos.CENTER);
        folderCounterContainer.getChildren().add(btnContainer);



        return folderCounterContainer;

    }
    private    void countFolder (int folderDepth,File initialDirectory,int maxDepth){
        if(folderDepth==0) {
            folderDepth=1;
            transversePath="";
            escapedFile ="";
        }


            if (folderDepth == 1) // when the folder is the first
                transversePath += initialDirectory.getName();
            else  // if the folder is the last but not the first
                transversePath += "->" + initialDirectory.getName();


        File [] fileLists = initialDirectory.listFiles();

        if(fileLists.length==0 || maxDepth == folderDepth){
            lastFolderName =  initialDirectory.getName();
            lastFileName = null ;
            totalFolderDepth =  folderDepth;
            return;

        }

        for (File file: fileLists)
        {
            if (file.isDirectory() && maxDepth !=folderDepth){
                folderDepth++;
                    countFolder(folderDepth,file,maxDepth);
                    break;
            }
            if(file.isFile()){  // if the file was not folder
                File anotherFolder =  null;
                for (File fileAgain: fileLists){ // checking if there is folder after the file

                    if (fileAgain.isDirectory()){
                        escapedFile += file.getName()+" at depth of [" +folderDepth+"],";
                        anotherFolder = fileAgain;
                        break;
                    }
                    else continue;
                }
                if(anotherFolder!=null && maxDepth !=folderDepth) {  // if  another folder is found
                    folderDepth++;
                    countFolder(folderDepth, anotherFolder, maxDepth);
                }
                else if(anotherFolder == null ){  // if another folder is not found after the file
                    File parentFile = file.getParentFile();
                    lastFolderName = parentFile.getName();
                    lastFileName = file.getName();
                    totalFolderDepth = folderDepth;
                    return;
                }
            }
            break;

        }

    }
    public  VBox   folderCounter(Stage stage){

        VBox folderCounterContainer =  new VBox(); //whole folder counter UI
        folderCounterContainer.setSpacing(5);
        folderCounterContainer.setAlignment(Pos.TOP_CENTER);
        folderCounterContainer.setPadding(new Insets(5,5,5,250));


        ObservableList<String> options  = FXCollections.observableArrayList("CountMe","Desktop","Document");
        String [] folderPaths  =  { "C:\\Users\\humna\\OneDrive\\Documents","C:\\Users\\humna\\Desktop","C:\\Users\\humna\\Downloads\\Video"};
        ComboBox folderList = new ComboBox(options);
        folderList.getSelectionModel().selectFirst();
        folderList.setPadding(new Insets(5,10,5,10));
        folderList.setStyle("-fx-font-size:14px;");

        VBox folderListContainer =  new VBox();
        folderListContainer.setAlignment(Pos.TOP_CENTER);
        folderCounterContainer.setPadding(new Insets(10,40,40,40));
        folderCounterContainer.setAlignment(Pos.TOP_CENTER);

        folderListContainer.getChildren().add(folderList);

        VBox radioContainerAll =  new VBox();
        radioContainerAll.setAlignment(Pos.TOP_CENTER);
        Label radioText =  new Label(" Select  folder depth");
        radioText.setStyle("-fx-font-size:14px;");
        HBox radioContainer =  new HBox();
        radioContainer.setAlignment(Pos.TOP_CENTER);
        radioContainer.setSpacing(5);
        radioContainer.setPadding(new Insets(0,0,0,5));
        radioContainerAll.getChildren().addAll(radioText,radioContainer);

        ToggleGroup radioGroup = new ToggleGroup();
        RadioButton depth3 = new RadioButton(" 3 ");

        depth3.setToggleGroup(radioGroup);
        depth3.setSelected(true);
        RadioButton depth5 = new RadioButton(" 5 ");
        depth5.setToggleGroup(radioGroup);
        RadioButton depth10 = new RadioButton(" 10 ");
        depth10.setToggleGroup(radioGroup);

        radioContainer.getChildren().addAll(depth3,depth5,depth10);

        Button countBtn =  new Button("Count Folder");
        countBtn.setPadding(new Insets(5,10,5,10));
        countBtn.setStyle("" +
                "-fx-font-size:16px;" +
                "-fx-background-color:orange;" +
                "-fx-color:red;");

      // text area for counting folder
        VBox countResult =  new VBox();
        countResult.setAlignment(Pos.CENTER);
        TextArea countResultTextArea =  new TextArea();
        countResultTextArea.setPrefHeight(150);
        countResultTextArea.setPrefWidth(170);
        countResultTextArea.setStyle("" +
                "-fx-opacity:0;" +
                "-fx-font-size:18px;"
        );

        countBtn.setOnAction(e->{
            final int maxDepth ;
            final  int folderIndex =  folderList.getSelectionModel().getSelectedIndex();
            final File initialDirectory  =  new File(folderPaths[folderIndex]);

            if(depth3.isSelected()) {
                maxDepth = 3;
                countFolder(0,initialDirectory,maxDepth);

            }
            else  if(depth5.isSelected()) {
                maxDepth = 5;
                countFolder(0, initialDirectory,maxDepth);
            }else  if(depth10.isSelected()) {
                maxDepth = 10;
                countFolder(0, initialDirectory,maxDepth);
            }

            // constructing the folder count result after count  button is  clicked
           String countResultText =
                   "Total Depth Transversed: "+ totalFolderDepth+
                   "\nLast Folder : "+ lastFolderName    ;
            if(lastFileName == null)
                countResultText +="\nLast File Found : No file Found! ";
            else
                countResultText += "\nLast File Found : "+lastFileName;
            countResultText +="\nTransverse Path : "+transversePath;
            if(!escapedFile.isEmpty())
            countResultText +="\nEscaped File : "+escapedFile;



           // displaying the result of counting the folders
           countResultTextArea.setText(countResultText);
           countResultTextArea.setStyle("" +
                    "-fx-opacity:1;" +
                   "-fx-font-size:16px;" +
                   "-fx-font-weight:bold;"
            );

        });


//        countResult.setPadding(new Insets(0,0,0,10));
//        Label folderDepthResult =  new Label("the folder has 8 depth ");
//        folderDepthResult.setStyle("" +
//                "-fx-font-size:14px;" +
//                "-fx-border:solid thin black;");

//        folderDepthResult.setAlignment(Pos.CENTER_LEFT);
//        Label folderContentResult =  new Label("the final dhhdhd");
//        folderContentResult.setStyle("-fx-font-size:14px;");
//        folderContentResult.setAlignment(Pos.CENTER_LEFT);

        countResult.getChildren().addAll(countResultTextArea);



        folderCounterContainer.getChildren().addAll(folderListContainer,radioContainerAll,countBtn,countResult);

        folderCounterContainer.setPrefWidth(550);
        folderCounterContainer.setPrefHeight(400);

        return folderCounterContainer;
    }
}
