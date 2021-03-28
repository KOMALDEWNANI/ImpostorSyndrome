package com.example.impostorsyndrome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.custom.FirebaseCustomRemoteModel;

import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameScreen_Theme1 extends AppCompatActivity {
    private static final String TAG = "TextClassificationDemo";

    private Button giveans_button;
    private TextView points_text ;
    private TextView question_text;
    private EditText answer_text;

    private Button hint_button;
    private ExecutorService executorService;
    private NLClassifier textClassifier;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen__theme1);
        giveans_button = findViewById(R.id.give_answer);
        points_text = findViewById(R.id.points);
        question_text = findViewById(R.id.Question);
        answer_text = findViewById(R.id.answer);
        hint_button = findViewById(R.id.hint);
        executorService = Executors.newSingleThreadExecutor();
        giveans_button.setOnClickListener(
                (View v) -> {
                    classify(answer_text.getText().toString());
                });
        // TODO 3: Call the method to download TFLite model
        downloadModel("sentiment_analysis");

    }
    /** Send input text to TextClassificationClient and get the classify messages. */
    private void classify(final String text) {
        executorService.execute(
                () -> {
                    // TODO 7: Run sentiment analysis on the input text
                    List<Category> results = textClassifier.classify(text);
                    // TODO 8: Convert the result to a human-readable text
                    Category result = results.get(0);
                    double overall_max = result.getScore();
                    int points = Integer.parseInt(points_text.getText().toString());
              for (int i = 1; i < results.size(); i++) {
                  Category result1 = results.get(i);
                  double window_max = result1.getScore();
                  if(window_max<overall_max)
                           points+=5;
                  else if(window_max>overall_max)
                      points-=5;
                  

                    }


                    // Show classification result on screen
                  final_output(points);
                });
    }

    private void final_output(int points) {
        runOnUiThread(
                () -> {
                    // Append the result to the UI.
                    points_text.setText(points);

                    // Clear the input text.
                    answer_text.getText().clear();
                    change_Question();

                });
    }

    private void change_Question() {

    }


    //Todo 2
    private synchronized void downloadModel(String modelName) {
        final FirebaseCustomRemoteModel remoteModel =
                new FirebaseCustomRemoteModel
                        .Builder(modelName)
                        .build();
        FirebaseModelDownloadConditions conditions =
                new FirebaseModelDownloadConditions.Builder()
                        .requireWifi()
                        .build();
        final FirebaseModelManager firebaseModelManager =
                FirebaseModelManager.getInstance();
        firebaseModelManager
                .download(remoteModel, conditions)
                .continueWithTask(task ->
                        firebaseModelManager.getLatestModelFile(remoteModel)
                )
                .continueWith(executorService, (Continuation<File, Void>) task -> {
                    // Initialize a text classifier instance with the model
                    File modelFile = task.getResult();
                    // TODO 6: Initialize a TextClassifier with the downloaded model
                    if(modelFile!=null) {
                        textClassifier =NLClassifier.createFromFile(this,modelFile);
                    }

                    // Enable predict button
                    giveans_button.setEnabled(true);
                    return null;
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to download and initialize the model. ", e);
                    Toast.makeText(
                            GameScreen_Theme1.this,
                            "Model download failed, please check your connection.",
                            Toast.LENGTH_LONG)
                            .show();
                    giveans_button.setEnabled(false);
                });
                }}