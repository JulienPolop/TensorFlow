package esiee.test_nn;
//Importer les librairies
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.text.DecimalFormat;



public class MainActivity extends AppCompatActivity {

    private TensorFlowInferenceInterface tensorFlowInferenceInterface;
    private static final String MODEL_NAME = "file:///android_asset/optimized_iris_model.pb";
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "softmax/final_result";
    private static final int[] INPUT_SIZE = {1, 4};
    private static final String[] FLOWER_TYPE = {"Iris Setosa ","Iris Versicolor","Iris Virginica"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tensorFlowInferenceInterface = new TensorFlowInferenceInterface(getAssets(), MODEL_NAME);

        final TextView textView1 = findViewById(R.id.textView);
        final TextView textView2 = findViewById(R.id.textView2);
        final Button ButtonCalculate = findViewById(R.id.button);

        final EditText editText1 = findViewById(R.id.editText1);
        final EditText editText2 = findViewById(R.id.editText2);
        final EditText editText3 = findViewById(R.id.editText3);
        final EditText editText4 = findViewById(R.id.editText4);

        ButtonCalculate.setOnClickListener
            (new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float num1 = Float.parseFloat(editText1.getText().toString());
                    float num2 = Float.parseFloat(editText2.getText().toString());
                    float num3 = Float.parseFloat(editText3.getText().toString());
                    float num4 = Float.parseFloat(editText4.getText().toString());

                    float[] InputFloats = {num1, num2, num3, num4};

                    float[] res = {0, 0, 0};
                    tensorFlowInferenceInterface.feed(INPUT_NAME, InputFloats, 1, 4);
                    tensorFlowInferenceInterface.run(new String[]{OUTPUT_NAME});
                    tensorFlowInferenceInterface.fetch(OUTPUT_NAME, res);
                    String newText = "[" + Float.toString(res[0]) + "     " + Float.toString(res[1]) + "     " + Float.toString(res[2]) + "]";
                    textView1.setText(newText);

                    float max = res[0];
                    int indice_max = 0;

                    for (int i = 1; i < res.length; i++) {
                        if (res[i] > max) {
                            max = res[i];
                            indice_max = i;
                        }
                    }

                    float maxres = max;

                    DecimalFormat df = new DecimalFormat(" ########.##");
                    String newText2 = "La fleur est une "+ FLOWER_TYPE[indice_max] + " à " + df.format(maxres * 100) + "%.";
                    textView2.setText(newText2);
                }

            }
        );
    }
}


