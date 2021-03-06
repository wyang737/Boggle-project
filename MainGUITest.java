import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javafx.application.*;
import javafx.concurrent.Task;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.event.*;
import javafx.geometry.*;
import java.lang.Object;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

public class MainGUITest extends Application{
	int spots [] = new int [8];
	int lastPlayed=0;
	String word ="";
	int row;
	int column;
	Label dynamicTimeDisplayLabel2 = new Label("");
	Label displayError = new Label("");
	Label displayWord = new Label("");
	TextArea allWords = new TextArea(""); 
	Label displayScore = new Label("");
	ArrayList<Integer> playedSpots = new ArrayList<Integer>();
	GridPane gridpane = new GridPane();
	static ArrayList <Button> bList = new ArrayList<>();
	BoggleBoard boggleBoard = new BoggleBoard();
	int score  = 0;
	Dictionary d = new Dictionary();
	TextArea possibleWords = new TextArea();
	boolean stopTimer = false;
	Thread t2;
	BoggleTimer dynamicTimeTask;
	MainGUITest me;
	Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();


	public void start(Stage myStage){
				
		//		Date timerStart = new Date();
		me = this;
		boggleBoard.makeBoard();

		Scene scene= new Scene(gridpane, 700, 400);
		gridpane.setPadding(new Insets(30));
		gridpane.setHgap(10);
		gridpane.setVgap(10);
		myStage.setX(primaryScreenBounds.getMinX());
		myStage.setY(primaryScreenBounds.getMinY());
		myStage.setWidth(primaryScreenBounds.getWidth());
		myStage.setHeight(primaryScreenBounds.getHeight());
		myStage.setTitle("Corica");
		myStage.setScene(scene);
		myStage.show();
		

		 
		DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0f);
		ds.setColor(Color.color(0.4f, 0.4f, 0.4f));


		Label credits = new Label("© copyright all rights reserved");
		Label credits2 = new Label("Anthony, Daniel, Tyron, Winston ");
		Label yourWord = new Label("Your word: ");		
		Label allWordsTitle = new Label("You've found: ");
		Label Title = new Label("Boggle V 3.0");	
		Label Instructions = new Label("Instructions:" + "\n\n" + "Create words from the letters given." + "\n\n" + "Letters must be adjacent and can't be used more than once." + "\n\n" + "Press enter to submit a word and press clear to clear the current word.");
		Title.setMinWidth(Region.USE_PREF_SIZE);
		Title.setMinHeight(Region.USE_PREF_SIZE);
		yourWord.setMinWidth(Region.USE_PREF_SIZE);
		yourWord.setMinHeight(Region.USE_PREF_SIZE);
		Instructions.setMinWidth(Region.USE_PREF_SIZE);
		Instructions.setMinHeight(Region.USE_PREF_SIZE);

		Button clearButton = new Button(String.valueOf("Clear"));
		Button resetButton = new Button(String.valueOf("New Game"));
		Button enterButton = new Button(String.valueOf("Enter"));
		

		//Set Titles And Fonts==============================================================================
		Title.setTextFill(Color.DARKBLUE);
		Title.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 40));
		Title.setEffect(ds);
		yourWord.setTextFill(Color.DARKBLUE);
		yourWord.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 50));
		displayScore.setFont(Font.font("Verdana", FontWeight.NORMAL, 30));
		displayScore.setTextFill(Color.DARKBLUE);
		displayWord.setTextFill(Color.DARKBLUE);
		displayWord.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 40));
		credits2.setTextFill(Color.DARKBLUE);
		credits.setTextFill(Color.DARKBLUE);
		credits.setMinWidth(Region.USE_PREF_SIZE);
		credits.setMinHeight(Region.USE_PREF_SIZE);
		credits2.setMinWidth(Region.USE_PREF_SIZE);
		credits2.setMinHeight(Region.USE_PREF_SIZE);
		allWordsTitle.setTextFill(Color.DARKBLUE);
		allWordsTitle.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 20));
		allWordsTitle.setMinWidth(Region.USE_PREF_SIZE);
		allWordsTitle.setMinHeight(Region.USE_PREF_SIZE);
		enterButton.setTextFill(Color.DARKBLUE);
		enterButton.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 20));
		enterButton.setMinWidth(Region.USE_PREF_SIZE);
		enterButton.setMinHeight(Region.USE_PREF_SIZE);
		clearButton.setTextFill(Color.DARKBLUE);
		clearButton.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 20));
		clearButton.setMinWidth(Region.USE_PREF_SIZE);
		clearButton.setMinHeight(Region.USE_PREF_SIZE);
		Instructions.setTextFill(Color.DARKBLUE);
		Instructions.setFont(Font.font("Times New Roman",FontWeight.NORMAL, 15));
		possibleWords.setMaxSize(250, 150);
		resetButton.setTextFill(Color.DARKBLUE);
		resetButton.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 20));
		resetButton.setMinWidth(Region.USE_PREF_SIZE);
		resetButton.setMinHeight(Region.USE_PREF_SIZE);
		//==================================================================================================

		//Display Buttons===================================================================================
		gridpane.add(credits, 5, 6);
		gridpane.add(credits2, 5, 7);
		gridpane.add(yourWord, 6, 1);
		gridpane.add(displayError, 6, 3);
		gridpane.add(displayWord, 6, 2);		
		gridpane.add(allWordsTitle,  4,  7);
		gridpane.add(allWords, 4, 8);
		gridpane.add(displayScore, 5, 5);
		gridpane.add(Title,  5,  0);
		gridpane.add(dynamicTimeDisplayLabel2, 5, 1);
		gridpane.add(enterButton, 4, 4);
		gridpane.add(clearButton,  4, 5);
		gridpane.add(resetButton,  4,  6);
		gridpane.add(possibleWords, 5, 8);
		gridpane.add(Instructions,  6, 8);
		//=================================================================================================

		allWords.setMaxWidth(80);

		//Timer============================================================================================
		dynamicTimeTask = new BoggleTimer(me);
		t2 = new Thread(dynamicTimeTask);
		t2.setName("Task Time Updater");
		t2.setDaemon(true);
		t2.start();
		dynamicTimeDisplayLabel2.textProperty().bind(dynamicTimeTask.messageProperty());
		//=================================================================================================



		for (row = 0; row < 4; row++){
			for (column = 0; column < 4; column++)
			{
				char letter=boggleBoard.getLetter(row, column);//get board from Winston's BoggleBoard
				Button button = new Button(String.valueOf(""));
				if (letter=='$'){
					button.setText(String.valueOf("QU"));
					button.setTextFill(Color.DARKBLUE);
					button.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
				}
				else{
					button.setText(String.valueOf(letter));
					button.setTextFill(Color.DARKBLUE);
					button.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
				}

				bList.add(button);
				gridpane.add(button, row, column+1);
				button.setMaxWidth(60);
				button.setMinWidth(60);
				button.setMinHeight(60);
				button.setMaxHeight(60);
				button.setOnAction(new ButtonHandler());
			}
		}
		enterButton.setOnAction(new EnterButtonHandler());
		clearButton.setOnAction(new ClearButtonHandler());
		resetButton.setOnAction(new ResetButtonHandler());
	}
	//============================================================ end of start

	public void freeze()
	{
		ArrayList<String> words = boggleBoard.getWords();
		String possibleWord = "";
		for(int i = 0; i < words.size();i++){
			possibleWord += words.get(i);
			possibleWord += "\n";
		}
		possibleWords.setText(possibleWord);
		for(int i = 0; i<=16; i++){
			getButton(i).setDisable(true);
		}

	}

	private boolean isAdjacent(int currentPosition){//if true, no conflict. if false, button can't be pressed

		spots[0]=lastPlayed-11;//top left corner
		spots[1]=lastPlayed-1;//straight above
		spots[2]=lastPlayed+9;//top right corner
		spots[3]=lastPlayed-10;//left
		spots[4]=lastPlayed+10;//right
		spots[5]=lastPlayed-9;//bottom left corner
		spots[6]=lastPlayed+1;//straight below
		spots[7]=lastPlayed+11;//bottom right corner
		if (word.length()==0){//exception for first letter of a word, always allowed
			System.out.println("First letter of the word!");
			return true;
		}
		for (int i=0; i<8; i++){//runs through adjacent spots, checks for matching with current position
			System.out.println(spots[i] + "=?="+ currentPosition);
			if (spots[i]==currentPosition){
				return true;
			}
		}
		return false;
	}
	public static Button getButton (int x){

		return bList.get(x);
	}
	public String getWord(){
		return word;
	}
	class EnterButtonHandler implements EventHandler<ActionEvent>{
		public void handle (ActionEvent e){

			if(d.isWord(word)==false){

				System.out.println("Not A Real Word");
				displayError.setTextFill(Color.RED);
				displayError.setText("Not A Real Word");
				word = "";
				playedSpots.clear();

				return;
			}		

			if (word.length()>2 && boggleBoard.checkWord(word)==false){
				if (d.isWord(word)){
					if(word.length()==3)
						score = score + 1;
					if(word.length()==4)
						score = score + 2;
					if(word.length()==5)
						score = score + 4;
					if(word.length()==6)
						score = score + 6;
					if(word.length()==7)
						score = score + 10;
					if(word.length()>=8)
						score = score + 15;
					boggleBoard.addWord(word);
					word = "";
					playedSpots.clear();
					System.out.println("Word recorded! Nice job!");
					displayError.setTextFill(Color.DARKBLUE);
					displayError.setText("Nice job!");
					System.out.println("score is:" + score);
					displayScore.setText("Score: " + Integer.toString(score));
					String displayWordsString="";
					ArrayList<String> returnedWords = new ArrayList<String>();
					returnedWords= boggleBoard.returnWords();
					for (int k = 0; k<returnedWords.size(); k++){
						displayWordsString+=returnedWords.get(k);
						displayWordsString+=("\n");
					}
					allWords.setText(displayWordsString);
					word = "";
					playedSpots.clear();
					System.out.println("word recorded, find more!");

				}
				return;
			}


			if (word.length()<2 || boggleBoard.checkWord(word)==true){
				System.out.println("word too short or word existing");
				displayError.setTextFill(Color.RED);
				displayError.setText("Word too short or already exists!");
				word = "";
				playedSpots.clear();
				return;
			}

		}
	}

	class ClearButtonHandler implements EventHandler<ActionEvent>{
		public void handle (ActionEvent e){
			word = "";
			System.out.println("word cleared");
			playedSpots.clear();
			possibleWords.setText("");
			displayError.setText("");
			displayWord.setText("");
		}
	}
	class ResetButtonHandler implements EventHandler<ActionEvent>{
		public void handle (ActionEvent e){
			playedSpots.clear();
			word = "";
			score = 0;
			possibleWords.setText("");;
			allWords.setText("");
			displayError.setText("");
			displayWord.setText("");;
			displayScore.setText("Score: " + Integer.toString(score));
			//=============================================
			boggleBoard.returnWords().clear();
			String displayWordsString="";
			ArrayList<String> returnedWords = new ArrayList<String>();
			returnedWords= boggleBoard.returnWords();
			for (int k = 0; k<returnedWords.size(); k++){
				displayWordsString+=returnedWords.get(k);
				displayWordsString+=("\n");
			}
			allWords.setText(displayWordsString);
			//============================================

			boggleBoard.makeBoard();

			for (row = 0; row < 4; row++){
				for (column = 0; column < 4; column++)
				{
					char letter=boggleBoard.getLetter(row, column);//get board from Winston's BoggleBoard
					Button button = bList.get(row*4+column);//new Button(String.valueOf(""));
					button.setDisable(false);
					if (letter=='$'){
						button.setText(String.valueOf("QU"));
						button.setTextFill(Color.DARKBLUE);
						button.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
					}
					else{
						button.setText(String.valueOf(letter));
						button.setTextFill(Color.DARKBLUE);
						button.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
					}
				}
			}
			dynamicTimeTask.stopMe();
			dynamicTimeTask = new BoggleTimer(me);
			t2 = new Thread(dynamicTimeTask);
			t2.setName("Task Time Updater");
			t2.setDaemon(true);
			t2.start();
			dynamicTimeDisplayLabel2.textProperty().bind(dynamicTimeTask.messageProperty());
		}
	}
	class ButtonHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e){



			Button b = (Button) e.getSource();
			row=GridPane.getRowIndex(b);
			column=GridPane.getColumnIndex(b);

			int currentPosition=(row * 10) + column;
			System.out.println("currentPosition = " + currentPosition);
			if (isAdjacent(currentPosition)==true && !playedSpots.contains(currentPosition)){

				word+=b.getText();
				System.out.println("word is: " + word);
				playedSpots.add(currentPosition);
				lastPlayed=currentPosition;
				displayError.setText("");
				displayWord.setText(word);
				System.out.println("wordlength = " + word.length());
			}
			else{
				System.out.println("Illegal Move");
				displayError.setTextFill(Color.RED);
				displayError.setText("Illegal Move");
			}
		}
	}


	public static void main(String []args){
		launch(args);

	}
}
