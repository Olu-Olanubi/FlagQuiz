package com.example.user.flagquiz;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView flag;
    Button button1, button2, button3, button4, nextButton;
    TextView remark, showScore, showQuestionNumber;
    int scoreCount, tries, questionCount, questionNumber;
    int defaultButtonColor;
    //string to save the correct answer
    String answer;
    //create a String array of list of countries
    List<String>  countryList, flags;
    //Create an ArrayList to store options from the String array
    List<String> flagSet;
    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flag = findViewById(R.id.flag_image);
        button1 = findViewById(R.id.bt_option1);
        button2 = findViewById(R.id.bt_option2);
        button3 = findViewById(R.id.bt_option3);
        button4 = findViewById(R.id.bt_option4);
        remark = findViewById(R.id.tv_remark);
        nextButton = findViewById(R.id.bt_next_question);
        showScore = findViewById(R.id.tv_show_score);
        showQuestionNumber = findViewById(R.id.tv_question_number);

        //populate array from the string-array in strings.xml
        //countryList = getResources().getStringArray(R.array.country_list);
        countryList = new ArrayList<>();
        flags = new ArrayList<>();
        flags = Arrays.asList(getResources().getStringArray(R.array.country_list));
        //
        start();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(button1);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(button2);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(button3);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(button4);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(questionNumber == 20){
                    showResult();
                }
                loadFlag();
            }
        });
    }

    protected static int getResourceID(final String resName, final Context ctx) {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, "drawable",
                        ctx.getApplicationInfo().packageName);
        if (ResourceID == 0) {
            throw new IllegalArgumentException
                    (
                            "No resource string found with name " + resName
                    );
        } else {
            return ResourceID;
        }
    }

    //method to load flag into ImageView
    private void loadFlag() {
        questionNumber++;
        showQuestionNumber.setText(String.format(getString(R.string.question_name), questionNumber));
        remark.setText(R.string.blanks);
        countryList.remove(answer);

        //set buttons to clickable
        button1.setClickable(true);
        button2.setClickable(true);
        button3.setClickable(true);
        button4.setClickable(true);

        //set default background for the buttons
        button1.setBackgroundColor(defaultButtonColor);
        button2.setBackgroundColor(defaultButtonColor);
        button3.setBackgroundColor(defaultButtonColor);
        button4.setBackgroundColor(defaultButtonColor);
        nextButton.setVisibility(View.INVISIBLE);

        //Update the score
        showScore();
        flagSet.clear();
        //shuffle the countryList array list
        Collections.shuffle(countryList);

        //Take the first four of the shuffled arraylist as options
        for (int i = 0; i < 4; i++) {
            flagSet.add(countryList.get(i));
        }
        //set a flag image to the ImageView
        flag.setImageDrawable(getResources().getDrawable(getResourceID(flagSet.get(0), getApplicationContext())));

        //set the name of the flag you loaded to answer
        answer = flagSet.get(0);

        //remove name of flag from countryList to avoid repetition


        // re-shuffle flagSet again using Collections util
        Collections.shuffle(flagSet);
        //Set the items in flagSet to the button option names, but first set them to upper case

        button1.setText(flagSet.get(0).toUpperCase());
        button2.setText(flagSet.get(1).toUpperCase());
        button3.setText(flagSet.get(2).toUpperCase());
        button4.setText(flagSet.get(3).toUpperCase());

    }

    private void checkAnswer(Button b) {
        String name = b.getText().toString();
        name = name.toLowerCase();
        if (name.equals(answer)) {
            b.setBackgroundColor(getResources().getColor(R.color.correctAnswer));
            remark.setText(R.string.correct);
            scoreCount++;
            moveToNext();
        } else {
            b.setBackgroundColor(getResources().getColor(R.color.wrongAnswer));
            tries++;
            if (tries == 2) {
                remark.setText(String.format("%s%s%s", getString(R.string.wrong)," ", answer.toUpperCase()));
                moveToNext();
            } else {
                remark.setText(R.string.try_again);
            }
        }


    }

    private void showScore() {
        showScore.setText(String.format(getString(R.string.score_sheet), scoreCount, questionCount));

    }

    private void moveToNext() {
        button1.setClickable(false);
        button2.setClickable(false);
        button3.setClickable(false);
        button4.setClickable(false);
        if(questionNumber == 20){
            nextButton.setText(R.string.finish);
        }
        nextButton.setVisibility(View.VISIBLE);
        tries = 0;
        questionCount++;
    }

    private void showResult(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("RESULT");
        dialogBuilder.setMessage(String.format(getString(R.string.score_summary), scoreCount," \n", grade(scoreCount)));
        dialogBuilder.setPositiveButton("PLAY AGAIN", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                start();
            }
        });
        dialogBuilder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
                System.exit(0);
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
    private String grade(int score){
        switch(score){
            case 20:
                return "EXCELLENT!";
            case 19:
            case 18:
            case 17:
                return "VERY GOOD!";
            case 16:
            case 15:
            case 14:
                return "GOOD";
            case 13:
            case 12:
            case 11:
                return "Above Average";
            case 10:
                return "Average!";
            case 9:
            case 8:
            case 7:
                return "Below Average!";
            default:
                return "POOR!";
        }
    }

    private void start(){
        scoreCount = 0;
        questionNumber = 0;
        tries = 0;
        questionCount = 0;
        defaultButtonColor = button1.getSolidColor();
        for(int i = 0; i < flags.size(); i++){
            countryList.add(flags.get(i));
        }
        //initialize arrayList to 4
        flagSet = new ArrayList<>(4);
        answer = " ";
        nextButton.setText(R.string.next);
        loadFlag();

    }
}
